package com.lim

import scala.io.StdIn

/** 流程控制练习 */
object Test06_If_else {

  // 主函数 等同于 java中的psvm
  def main(args: Array[String]): Unit = {
    println("input age")
    var age: Int = StdIn.readInt()

    val res: Any = if (age < 18) {
      "童年"
    } else if (age >= 18 && age < 60) {
      "中年"
    } else {
      100
    }

    println(res)

    // Java
    // int result = flag?1:0

    // Scala
    println("input age")
    var age1 = StdIn.readInt()
    val res1: Any = if (age1 < 18) "童年" else "成年"

    println(res1)
  }

}
