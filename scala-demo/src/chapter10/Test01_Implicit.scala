package com.lim
package chapter10

/**
 * 隐式转换练习
 *  隐式转换：悄悄将一个类型转成另一个类型
 *    语法 implicit def 方法名(形参: 形参类型): 返回类型 = 代码实现
 * */
object Test01_Implicit {

  def main(args: Array[String]): Unit = {
    var a1: Int = 2.5;
    println(a1)
  }

  implicit def double2Int(d: Double): Int = d.toInt

}
