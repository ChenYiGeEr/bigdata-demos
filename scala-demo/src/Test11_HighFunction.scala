package com.lim

/** 高阶函数 */
object Test11_HighFunction {

  def main(array: Array[String]): Unit = {
    //函数————求和
    def plus(x: Int, y: Int): Int = {
      x + y
    }

    //方法————求积
    def multiply(x: Int, y: Int): Int = {
      x * y
    }

    println(calculator(10, 20, multiply))
  }

  def calculator(a: Int, b: Int, operater: (Int, Int) => Int): Int = {
    operater(a, b)
  }

}
