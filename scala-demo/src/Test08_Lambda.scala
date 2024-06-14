package com.lim

object Test08_Lambda {

  // 主函数 等同于 java中的psvm
  def main(args: Array[String]): Unit = {
    var plus = (x: Int, y: Int) => x + y
    // 最标准的方法定义
    def plus1 (x: Int, y: Int): Int = { x + y }
    // 优化1
    def plus2(x: Int, y: Int): Int = x + y
    // 优化2
    def plus3(x: Int, y: Int) = x + y


    println(plus(1, 2))
    println(plus1(1, 2))
  }

}
