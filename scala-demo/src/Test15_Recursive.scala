package com.lim

/**
 * 递归练习
 * */
object Test15_Recursive {

  def main(array: Array[String]): Unit = {
    println(factorial(5))
  }

  /** 求出输入的阶乘 */
  def factorial: Int => Int = {
    case 0 => 1
    case n => n * factorial(n - 1)
  }

}
