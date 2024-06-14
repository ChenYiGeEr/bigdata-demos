package com.lim
package chapter7

object Test02_ImmutableList {

  def main(args: Array[String]): Unit = {
    // 创建不可变List的方式1
    var list1: List[Int] = List(1, 1, 1)
    var list2 = List[Int](10, 20, 30)
    // 添加元素，等同于+：
    // val list3 = list2.::(10)
    val list3 = 10 :: list2
    println(list3)
    // 添加多个元素，等同于++：
    val list4 = list2 ::: List(10, 20, 30)
    println(list4)
  }

}
