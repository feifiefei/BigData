package cn.fei.scala.D002.func

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 12:19
 */
object OperatorT1 {
  def main(args: Array[String]): Unit = {
    val list = List(-89, 45, 20, 9, 78, 23, 52, -65)
    //todo: 函数一：foreach函数，遍历集合,进行操作，没有返回值
    list.foreach(item => println(item))
    //取绝对值
    list.foreach(item => Math.abs(item))
    list.foreach(s => s.abs)
    //todo: 函数二：map函数，类似foreach有返回值
    val doubles: List[Int] = list.map(item => Math.abs(item))
    //todo: 函数三：flatMap函数 等价于map+flatten
    /**
     * 词频统计WordCount
     * hadoop hive spark flink flink
     * spark flink spark
     */
    val lineList = List("hadoop hive spark flink flink spark flink spark")
    //数据进行分割
    /*lineList.map((line:String) =>{
      line.trim //去左右空格
        .split("\\s+")
    }*/
    val list1 = lineList.map(line => line.split("\\s"))
    //扁平化操作：
    val flatten = list1.flatten
    //方案二
    lineList.flatMap(line => line.split("\\s"))
    //lineList.flatMap(_.split("\\s"))
    //todo: 函数四：filter 过滤 返回值为布尔类型
    list.filter(_ > 50)
    list.filter(item => item > 50)
    //todo：函数五：排序函数 sorted（按照集合中元素进行字典升序排序）、sortBy（指定排序的字段）、sortWith
    val sorted = list.sorted
    list.sortBy(item => -item) //降序排序
    list.sortWith((x1, x2) => x1 > x2)
    //todo: 函数六：groupBy 分组函数
    val map: Map[Int, List[Int]] = list.groupBy(item => Math.abs(item) % 2)
    //todo: 函数七：聚合 reduce fold(可手动为临时变量设置初始值)
    print(list.reduce((temp, item) => temp + item))
    list.reduce(_ + _)
    list.reduceRight(_ + _) //从集合的右边开始遍历 临时变量是右边的参数 初始值是最右边的元素的初始值
    list.fold(0)(_ + _)
  }

}
