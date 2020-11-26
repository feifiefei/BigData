package cn.fei.parquet

import org.apache.spark.sql.types.{IntegerType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @description:读CSV数据
 * @author: 飞
 * @date: 2020/11/22 0022 21:18
 */
object SparkSQLCsv {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      //设置sparksql在shuffle阶段的分区数据，实际项目根据数据量调整
      .config("spark.sql.shuffle.partitions", 4)
      .getOrCreate()
    //加载数据
    val uDF: DataFrame = spark.read
      //设置每行数据各个字段间的分隔符，默认为，号
      .option("sep", "\t")
      //设置数据第一行是否有数据,默认为false
      .option("header", "true")
      //设置自动推测类型，默认值为false
      .option("inferSchema", "true")
      //指定文件路径
      .csv("datas/resources/u.dat")
    uDF.printSchema()
    uDF.show(10, truncate = false)
    //定义Schema信息
    val schema = StructType(
      Array(StructField("userId", IntegerType, nullable = false),
        StructField("movieId", IntegerType, nullable = false),
        StructField("rating", IntegerType, nullable = false),
        StructField("timestamp", LongType, nullable = false)
      )
    )
    val uuDF: DataFrame = spark.read
      .option("sep", "\t")
      .schema(schema)
      .csv("datas/resources/u.dat")
  }
}
