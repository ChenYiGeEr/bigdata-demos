package com.lim

import scala.collection.immutable
import scala.util.control.Breaks.{break, breakable}

/** 循环练习 */
object Test07_Loop {

  // 主函数 等同于 java中的psvm
  def main(args: Array[String]): Unit = {

    // scala中的for循环基础语法
    for (i <- 1 to 5) {
      println(i)
    }

    for (i <- 0 until 5) {
      println(i)
    }

    // for循环的本质
    // to是整数的方法  返回结果是一个集合
    // 使用变量i 循环遍历一遍 后面集合的内容
    val inclusive: Range.Inclusive = 0.to(5)

    println(inclusive);

    // 循环守卫
    for (i <- 0 to 10) {
      if (i > 5) {
        println(i)
      }
    }

    for (i <- 0 to 10 if i > 5) {
      println(i)
    }

    // 循环返回值
    val ints: immutable.IndexedSeq[Int] = for (i <- 0 to 3) yield {
      10
    }
    println(ints);

    var i = 0
    while (i < 5) {
      println(i)
      i += 1
    }

    // do while 一定会执行一次  不管条件是否成立
    do {
      println(i)
      i += 1
    } while (i <= 100)


    breakable (
      for (elem <- 1 to 10) {
        println(elem)
        if (elem == 5) break
      }
    )

    println("正常结束循环")

  }

}
