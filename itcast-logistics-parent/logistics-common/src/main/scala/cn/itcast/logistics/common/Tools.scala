package cn.itcast.logistics.common

import java.util

import org.apache.commons.lang3.StringUtils
import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{StructField, StructType}

/**
 * Kudu操作的工具类：创建表及其他操作
 */
object Tools {
	
	/**
	 * 创建kudu表，创建Kudu表的约束？
	 * 1. Kudu表中必须要有一个主键列，更新数据和删除数据都需要根据主键更新，应该使用哪个列作为主键？id
	 * 2. Kudu表的字段有哪些？oracle和mysql的表有哪些字段，Kudu表就有哪些字段
	 * 3. kudu表的名字是什么？使用数据中传递的表明作为Kudu的表名
	 */
	def autoCreateKuduTable(tableName: String, dataframe: DataFrame, primaryField: String = "id"): Unit={
		/**
		 * 实现步骤：
			 * step1. 获取Kudu的上下文对象：KuduContext
			 * step2. 判断Kudu中是否存在这张表，如果不存在则创建
			 * step3. 判断是否指定了主键列
			 * step4. 生成Kudu表的结构信息
			 * step5. 创建表
		 */
		// step1. 获取kudu的上下文对象：KuduContext
		val kuduContext: KuduContext = new KuduContext(
			Configuration.KUDU_RPC_ADDRESS, dataframe.sparkSession.sparkContext
		)
		
		// step2. 判断Kudu中是否存在这张表，如果不存在则创建
		if(! kuduContext.tableExists(tableName)){
			
			// step3. 判断是否指定了主键列
			if(StringUtils.isEmpty(primaryField)){
				println(s"没有为${tableName}指定主键字段，将使用默认【id】作为主键列，如果表中存在该字段则创建成功，否则抛出异常退出程序！")
			}
			
			// step4. 生成Kudu表的结构信息, 不能直接使用DataFrame中schema设置，需要设置主键列不为null
			val schema: StructType = StructType(
				dataframe.schema.fields.map{field =>
					StructField(
						field.name,
						field.dataType,
						// 判断字段名称是否为主键名称，如果是，设置不为null，否则可以为null
						nullable = if(field.name.equals(primaryField)) false else true
					)
				}
			)
			
			// 设置Kudu表的分区策略和副本数目
			val options: CreateTableOptions = new CreateTableOptions()
			options.setNumReplicas(1)
			options.addHashPartitions(util.Arrays.asList(primaryField), 3)
			
			// step5. 创建表
			val table = kuduContext.createTable(tableName, schema, Seq(primaryField), options)
			println(s"Table ID = ${table.getTableId}")
		}else{
			println(s"${tableName}表已经存在，无需创建！")
		}
	}
	
}
