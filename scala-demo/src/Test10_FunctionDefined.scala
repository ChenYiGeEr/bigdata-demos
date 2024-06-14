package com.lim

object Test10_FunctionDefined {

  def main(array: Array[String]): Unit = {
    println(add(1, 2))
  }

  var add: (Int,Int) => Int = (x: Int, y: Int) => {
    x + y;
  }

}
