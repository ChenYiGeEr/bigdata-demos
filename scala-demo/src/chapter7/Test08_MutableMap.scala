package com.lim
package chapter7

import scala.collection.mutable

object Test08_MutableMap {

  def main(args: Array[String]): Unit = {
    // 创建可变Map
    var map1 = mutable.Map[String, Int]("lim" -> 22, ("lim", 23), "aa" -> 13)
    println(map1)

  }

}
