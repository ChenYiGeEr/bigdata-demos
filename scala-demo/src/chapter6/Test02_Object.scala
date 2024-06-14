package com.lim
package chapter6

// object单例 object中定义的所有属性与方法、函数,除开private修饰的，都可以通过对象名.属性、对象名.方法、对象名.函数 的方式调用
object Test02_Object {

  val name = "lisi"

  def main(args: Array[String]): Unit = {
    println(Test02_Object)
    println(Test02_Object)
    println(Test02_Object)
    println(Test02_Object.name)
  }


}
