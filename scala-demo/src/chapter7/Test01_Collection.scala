package com.lim
package chapter7

import scala.collection.mutable.ArrayBuffer

/**
 * 集合
 *  可变集合（scala.collection.immutable）
 *    集合长度可变，意味着可以向里面添加、删除元素
 *  不可变集合（scala.collection.mutable）
 *    集合长度不可变，不能添加、删除元素
 * */
object Test01_Collection {

  /**
   * 集合通用的添加、删除元素方法的区别
   *    带+与带-方法的区别
   *      带+是添加元素
   *      带-是删除元素
   *    一个+/-与两个+/-的区别
   *      一个+/-是添加/删除单个元素
   *      两个+/-是添加/删除集合的所有元素
   *    冒号在前、冒号在后以及不带冒号的区别
   *      冒号在前、不带冒号是将元素添加在集合最后边
   *      冒号在后是将元素添加在集合最前边
   *    带=与不带=的方法区别
   *      不带=是原集合不变，生成新集合
   *      带=是向原集合中添加、删除元素
   * */
  def main(args: Array[String]): Unit = {
    // 创建不可变长度数组 方式1
    var array1 = new Array[Int](10)
    println(array1.toList)
    // 创建不可变长度数组 方式2
    var array2 = Array[Int](1, 2, 3, 4, 5, 6, 7)
    println(array2.toList)
    // 向数组中添加单个元素
    val array3 = array1.:+(10)
    println(array1.toList)
    println(array3.toList)
    // 向数组中添加多个元素
    val array4 = array1.:++(Array(10, 20, 30))
    println(array1.toList)
    println(array4.toList)
    // WARN 不可变数据不可删除元素
    // 修改指定角标元素值
    array1(0) = 55
    println(array1.toList)

    // 创建可变长数组 方法1
    var array5 = new ArrayBuffer[Int]();
    // 创建可变长数组 方法2
    var array6 = ArrayBuffer[Int](10, 20, 30)
    println(array5)
    println(array6)
    // 添加元素
    val array7 = array6.+:(10)
    println("array6:" + array6)
    println("array7:" + array7)
    val array8 = array6.+:=(10)
    println("array6:" + array6)
    println("array8:" + array8)
  }

}
