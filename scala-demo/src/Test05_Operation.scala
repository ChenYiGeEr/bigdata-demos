package com.lim

/** 运算法练习 */
object Test05_Operation {

  /** 主函数 */
  def main(args: Array[String]): Unit = {
    var a: Int = 2;
    var b: Int = 1;
    println(a > b) // true
    println(a >= b) // true
    println(a <= b) // false
    println(a < b) // false
    println("a==b " + (a == b)) // false
    println(a != b) // true

    println(isNotBlank(null));

    var r1 = 10

    r1 += 1 // 没有++
    r1 -= 2 // 没有--
  }


  /** 判断字符串是否为空 */
  private def isNotBlank(str: String) : Boolean = {
    //如果按位与，s为空，会发生空指针
    return str != null && !"".equals(str.trim());
  }

}
