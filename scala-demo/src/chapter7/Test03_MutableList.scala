package com.lim
package chapter7

import scala.collection.mutable.ListBuffer

object Test03_MutableList {

  def main(args: Array[String]): Unit = {
    // 创建可变List的方式1
    var list1 = ListBuffer[Int](10,20,30)
    var list2 = new ListBuffer[Int]()
    println(list1)
    println(list2)

  }

}
