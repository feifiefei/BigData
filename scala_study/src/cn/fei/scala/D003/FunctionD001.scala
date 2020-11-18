package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 16:07
 */
object FunctionD001 {
  def main(args: Array[String]): Unit = {
    val list = (1 to 10).toList
    val result = list.map(item => item * item)
    list.map {
      case item => item * item
    }
    //============================
    val map = Map("spark" -> 3, "flink" -> 5, "hive" -> 10)
    map.map(tuple => {
      val value = tuple._2
      val sum = value * value
      tuple._1 -> sum
    })
    map.map {
      case (key, value) => key -> value * value
    }
  }
}
