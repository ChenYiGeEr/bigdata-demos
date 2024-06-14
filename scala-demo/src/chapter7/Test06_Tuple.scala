package com.lim
package chapter7

object Test06_Tuple {
  /**
   * 元组一旦创建，元素个数与元素都不可变，其中的元素最多存放22个元素
   * */
  def main(args: Array[String]): Unit = {
    // 1. 元组的创建方式1
    var t1 = (1, 2, 3, "hello", true)
    // 2. 元组的创建方式2
    var t2: (Int, String, Int) = Tuple3(1, "hello", 20)
    // 2. 元组的创建方式3
    var t3: (String, Int) = "张三" -> 20
    println(t1)
    println(t1._2)
    println(t2)
    println(t3)
  }
}
