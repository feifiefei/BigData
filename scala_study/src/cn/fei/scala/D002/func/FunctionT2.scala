package cn.fei.scala.D002.func

/**
 * @description:定义一个打招呼的高阶函数
 * @author: 飞
 * @date: 2020/11/13 0013 12:00
 */
object FunctionT2 {
  //定义方法
  def greetting(name: String, sayHello: (String) => Unit) = {
    sayHello(name)
  }

  def main(args: Array[String]): Unit = {
    //到公司，看到不同的人进行打招呼，说不同的问候语
    //前台小姐姐
    greetting("老乡", (name: String) => println(s"${name}吃了吗"))
    //简写1
    greetting("老乡", (name) => println(s"${name}吃了吗"))
    //简写2
    greetting("老乡", name => println(s"${name}吃了吗"))
    //简写3
    //todo: 当函数的参数在函数体重单独使用时，可以使用下划线代替，并且省略函数的参数
    greetting("老乡", println(_))

  }
}
