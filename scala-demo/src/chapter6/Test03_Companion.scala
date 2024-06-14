package com.lim
package chapter6

import scala.beans.BeanProperty

object Test03_Companion {
  def main(args: Array[String]): Unit = {

    //    val person1 = new Person11
    val person1: Companion = Companion.getCompanion

    // 如果调用的方法是apply的话  方法名apply可以不写
    val person11: Companion = Companion()

    val zhangsan: Companion = Companion("zhangsan")

    // 类的apply方法调用
    person11()
  }
}

/** object Companion的伴生类 */
class Companion private () {
  @BeanProperty
  var name: String = _

  def this(name:String) = {
    this()
    this.name = name
  }

  def apply(): Unit = println("类的apply方法调用")
}

/** class Companion的伴生对象 */
object Companion {

  // 使用伴生对象的方法来获取对象实例
  def getCompanion: Companion = new Companion

  // 伴生对象的apply方法
  def apply(): Companion = new Companion()


  // apply方法的重载
  def apply(name: String): Companion = new Companion(name)

}