package com.lim

object Test09_MethodParams {

  /**
   * 主函数 等同于 java中的psvm
   *  1. 默认值参数：在定义方法时给方法的参数指定默认值
   * @param args 参数
   * */
  def main(args: Array[String]): Unit = {
    println(add(2, 1))
    println(add1())
    println(add2(y=100, x=200))
    println(sum(1,2,3,4))
  }

  def add (x: Int, y: Int): Int = x + y
  /** 默认值参数 */
  def add1 (x: Int = 1, y: Int = 2): Int = x + y
  /** 带名参数，调用时指定传递参数 */
  def add2(x: Int = 1, y:Int): Int = x + y
  /** 可变长参数，可变长参数只能在最后 */
  def sum(x: Int, y:Int, z:Int*) : Int = x + y + z.sum

}
