package com.lim
package chapter8

object Test01_Match {
  def main(args: Array[String]): Unit = {
    val tuple = (1, 2, 3, 4)
    tuple match {
      case (1, x, y, 4) => println(s"hello 1234 $x $y")
      case (_, z, 5, _) => println(z)
      case _ => println("else")
    }

    val list = List(1, 2, 3, 4)
    list match {
      case 1 :: 2 :: Nil => println("1 2")
      case 1 :: tail => println(s"1 ... $tail")
      case _ => println("else")
    }

    val array = Array(1, 2, 3, 4)
    array match {
      case Array(1, x, y, 4) => println(s"hello 1234 $x $y")
      case Array(_, z, 5, _) => println(z)
      case _ => println("else")
    }

    val map = Map("A" -> 1, "B" -> 0, "C" -> 3)
    map match {
      case map1: Map[String, Int] => println("String -> Int")
      case _ => println("else")
    }

    val option = Some(1)
    option match {
      case Some(x) => println(x)
      case _ => println("else")
    }

    val clazz: Any = Map("A" -> 1, "B" -> 0, "C" -> 3)
    clazz match {
      case map: Map[_, _] => println("Map")
      case _ => println("else")
    }
  }
}
