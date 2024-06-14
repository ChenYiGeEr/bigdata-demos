package com.lim
package chapter7

import scala.collection.mutable

object Test05_MutableSet {

  def main(args: Array[String]): Unit = {
    // 创建可变Set的方式1
    var set1 = mutable.Set[Int](1,5,1,2)
    println(set1)

  }

}
