package com.lim
package chapter7

/** 集合常用属性以及操作 */
object Test11_CollectionOperation {

  def main(args: Array[String]): Unit = {
    val nestedList: List[List[Int]] = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9))
    println(nestedList.flatten)
    val wordList: List[String] = List("hello world", "hello atguigu", "hello scala")
    // 扁平化+映射 注：flatMap相当于先进行map操作，在进行flatten操作
    println(wordList.flatMap(x => x.split(" ")))
  }

}
