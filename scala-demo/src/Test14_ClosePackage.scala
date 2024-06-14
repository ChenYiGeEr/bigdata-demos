package com.lim

/**
 * 闭包练习
 * 在函数体重使用了外部变量的函数就是闭包
 * */
object Test14_ClosePackage {

  def main(array: Array[String]): Unit = {
    var y:Int = 10;
    var func = (x:Int) => {
      x + y
    }
    println(func(20))
  }

}
