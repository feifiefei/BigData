package cn.fei.store.hbase

import cn.fei.config.ApplicationConfig
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}

/**
 * 操作HBase数据中的表，主要插入数据到表中
 * 创建表的语句：
 * > create 'htb_orders', 'info', SPLITS => ['300000000_', '600000000_', '800000000_']
 */
object HBaseDao {
  // 初始化连接HBase Connection
  private lazy val connection: Connection = createHBaseConn()

  /**
   * 获取连接HBase数据库的Connection对象
   */
  def createHBaseConn(): Connection = {
    // 获取配置信息
    val conf: Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", ApplicationConfig.HBASE_ZK_HOSTS)
    conf.set("hbase.zookeeper.property.clientPort", ApplicationConfig.HBASE_ZK_PORT)
    conf.set("zookeeper.znode.parent", ApplicationConfig.HBASE_ZK_ZNODE)
    // 返回Connection实例对象
    ConnectionFactory.createConnection(conf)
  }

  /**
   * 依据表的名称获取HBase数据库中表的操作句柄
   * * @param tableName 表的名称
   */
  def getHTable(tableName: String): HTable = {
    // 获取Table
    val table = connection.getTable(TableName.valueOf(tableName))
    // 转换为HTable类型对象
    table.asInstanceOf[HTable]
  }

  /**
   * 插入数据到HBase表中
   * * @param tableName 存储到HBase数据库中的表的名称，对应到Topic的名称
   *
   * @param columnFamily 列簇名称
   * @param columns      列名称
   * @param datas        对应于RDD中某个分区的数据（Topic中某个分区的数据），数据格式为JSON的字符串
   *                     { * "orderId": "20200519100316283000001",
   *                     "userId": "300000856",
   *                     "orderTime": "2020-05-19 10:03:16.283",
   *                     "ip": "210.35.0.127",
   *                     "orderMoney": "319.03",
   *                     "orderStatus": 0,
   *                     "province": "江西省",
   *                     "city": "南昌市" * }
   */
  def insert(tableName: String, columnFamily: String,
             columns: Array[String], datas: Iterator[String]): Boolean = {
    var htable: HTable = null
    try {
      // 获取HBase数据库汇中的句柄（HTable）
      htable = getHTable(tableName)
      // 创建列表存储Put对象，后续进行批量插入HBase表中
      import java.util
      val puts: util.ArrayList[Put] = new util.ArrayList[Put]()
      // 列簇
      val cfBytes = Bytes.toBytes(columnFamily)
      // TODO: 解析获取JSON格式的数据，使用Alibaba FastJson库解析数据
      datas.foreach { data =>
        // i. 获取JSONObject对象
        val jsonObj: JSONObject = JSON.parseObject(data)
        // ii. 获取RowKey = userId + _ + orderTime
        val rowKey: Array[Byte] = {
          // 获取userId
          val userId = jsonObj.getString("userId")
          val orderTime = jsonObj.getString("orderTime")
          Bytes.toBytes(s"${userId.reverse}_${orderTime}")
        }
        // iii. 创建Put对象
        val put: Put = new Put(rowKey)
        // 由于ETL操作，数据量很大，实时进行写入HBase表中，为了提高性，跳过WAL
        //put.setDurability(Durability.SKIP_WAL)
        // iv. 依据列名获取值
        columns.foreach { column =>
          put.addColumn(
            cfBytes, Bytes.toBytes(column), Bytes.toBytes(jsonObj.getString(column))
          )
        }
        // v. 加入到list中
        puts.add(put)
      }
      // 批量插入数据
      htable.put(puts)
      true
    } catch {
      case e: Exception => e.printStackTrace(); false
    } finally {
      if (null != htable) htable.close()
    }
  }
}
