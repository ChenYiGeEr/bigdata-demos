package com.lim

/** 数据类型的练习 */
object Test03_Type {

  /** 主函数 */
  def main(args: Array[String]): Unit = {
    // 所有的代码都是代码块
    // 表示运行一段代码  同时将最后一行的结果作为返回值
    // 千万不要写return
    val i: Int = {
      println("我是代码块")
      10 + 10
    }
    println(i)

    // 代码块为1行的时候  大括号可以省略
    val i1: Int = 10 + 10

    // 如果代码块没有计算结果  返回类型是unit
    val unit: Unit = {
      println("hello")
      println("我是代码块")
    }
    println(unit)


    // 当代码块没办法完成计算的时候  返回值类型为nothing
    //    val value: Nothing = {
    //      println("hello")
    //      throw new RuntimeException
    //    }
    //    println(value)

    // 整数类型

    val l = 1L

    // （1）Scala各整数类型有固定的表示范围和字段长度，不受具体操作的影响，以保证Scala程序的可移植性。
    val b1: Byte = 2
    //    val b0: Byte = 128

    val b2: Byte = 2
    println(b1 + b2)

    val i2 = 1

    //（2）编译器对于常量值的计算 能够直接使用结果进行编译
    // 但是如果是变量值 编译器是不知道变量的值的 所以判断不能将大类型的值赋值给小的类型
//    val b3: Byte = i2 + 1
//    println(b3)

    // （3）Scala程序中变量常声明为Int型，除非不足以表示大数，才使用Long
    val l1 = 2200000000L

    // 浮点数介绍
    // 默认使用double
    val d: Double = 3.14

    // 如果使用float 在末尾添加f
    val fl = 3.14f

    // 浮点数计算有误差
    println(0.1 / 3.3)

    var a: Char = 'a'
    var a1: Char = 65535
    var a2: Char = '\t'
    var a3: Char = '\n'
    var a4: Char = '\\'
    var a5: Char = '\"'
    println(a + 0)
    println(a1 + 0)
    println(a2 + 0)
    println(a3 + 0)
    println(a4 + 0)
    println(a5 + 0)
    var b: Char = 'b'
    var c: Char = 'c'
    var e: Char = 'e'
    var bool:Boolean = false
    println(bool)
    if (!bool) {
      println("bool is false")
    }

    var unit1: Unit = {
      println("lim");
    }
    println(unit1)

    var unit2: Unit = 2;
    println(unit2);

    var str1: String = "1";
    str1 = null
    println(str1);

    val value: Nothing = {
      println("hello")
      1 + 1
      throw new RuntimeException()
    }
    println(value)


  }

}
