package com.lim
package chapter7

import scala.collection.mutable

object Test10_MutableQueue {

  def main(args: Array[String]): Unit = {
    // 创建可变Queue
    var queue1 = mutable.Queue[Int](22,23)
    println(queue1)

  }

}
