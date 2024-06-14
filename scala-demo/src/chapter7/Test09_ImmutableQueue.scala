package com.lim
package chapter7

import scala.collection.immutable.Queue

object Test09_ImmutableQueue {

  def main(args: Array[String]): Unit = {
    // 创建不可变Queue
    var queue1 = Queue[Int](22,23)
    println(queue1)

  }

}
