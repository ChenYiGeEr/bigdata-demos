package com.lim
package chapter7

object Test07_ImmutableMap {

  def main(args: Array[String]): Unit = {
    // 创建不可变Map
    var map1 = Map[String, Int]("lim" -> 22, ("lim", 23))
    println(map1)

  }

}
