package com.lim
package chapter7

object Test04_ImmutableSet {

  def main(args: Array[String]): Unit = {
    // 创建不可变Set的方式1
    var set1 = Set[Int](1, 4, 3, 2)
    println(set1)
    // 添加元素
    set1.+=(10)
    println(set1)
    set1.-=(2)
    println(set1)
  }

}
