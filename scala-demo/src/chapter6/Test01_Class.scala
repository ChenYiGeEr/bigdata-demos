package com.lim
package chapter6

import scala.beans.BeanProperty

object Test01_Class {

  class Person {

    @BeanProperty
    // 姓名
    var name: String = _

    @BeanProperty
    // 年龄
    var age: Int = _


  }

  def main(args: Array[String]): Unit = {
    val p1 = new Person
    p1.setName("lm")
    p1.setAge(11)
    println(p1.getName)
    println(p1.getAge)
  }


}
