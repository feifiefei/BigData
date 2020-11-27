package cn.fei.foreach

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.sql.{ForeachWriter, Row}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/27 0027 21:16
 */
class MySQLForeachWriter extends ForeachWriter[Row] {
  //定义变量
  var conn: Connection = _
  var pstmt: PreparedStatement = _
  val insertSQL = "REPLACE INTO `tb_word_count` (`id`, `word`, `count`) VALUES (NULL, ?, ?)"

  override def open(partitionId: Long, epochId: Long): Boolean = {
    // a. 加载驱动类
    Class.forName("com.mysql.cj.jdbc.Driver")
    // b. 获取连接
    conn = DriverManager.getConnection(
      "jdbc:mysql://node1.itcast.cn:3306/db_spark?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true", //
      "root",
      "123456"
    )
    // c. 获取PreparedStatement
    pstmt = conn.prepareStatement(insertSQL)
    //println(s"p-${partitionId}: ${conn}")
    // 返回，表示获取连接成功
    true
  }

  override def process(value: Row): Unit = {
    //设置参数
    pstmt.setString(1, value.getAs[String]("value"))
    pstmt.setLong(2, value.getAs[Long]("count"))
    //执行插入
    pstmt.executeUpdate()
  }

  override def close(errorOrNull: Throwable): Unit = {
    if (null != pstmt) pstmt.close()
    if (null != conn) conn.close()
  }
}
