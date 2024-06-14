package com.lim

/** 高阶函数练习 */
object Test12_HighFunctionPractice {

  def main(array: Array[String]): Unit = {

    /** 获取集合中每个元素的长度 */
    println(rule(Array("hello", "hadoop", "flume", "spark", "kafka"), (array: Array[Any]) => {
      array.map(_.toString.length).toList
    }))

    /** 获取集合中偶数元素 */
    println(rule(Array(1, 4, 3, 6, 7, 9, 10), (array: Array[Any]) => {
      array.filter(_.toString.toInt % 2 == 0).toList
    }))

    /** 对集合中的元素性别分组 */
    println(rule(Array("zhangsan man beijing", "lisi woman shanghai", "zhaoliu man beijing", "hanmeimei woman shenzhen"), (array: Array[Any]) => {
      array.map(_.toString.split(" ")).groupBy(_(1)).map((f: ((String, Array[Array[String]]))) => {
        f._1 + "," + f._2.map(_.toList).mkString("Array(", "," , ")")
      }).mkString("Map(", ", ", ")")
    }))

    /** 对集合中的元素进行求和 */
    println(rule(Array(10, 30, 20, 50), (array: Array[Any]) => {
      array.reduce(_.toString.toInt + _.toString.toInt)
    }))


    /** 获取集合中薪酬最多元素 */
    println(rule(Array("zhangsan 20 4500", "lisi 33 6800", "hanmeimei 18 10000"), (array: Array[Any]) => {
//      array.sortWith(_.toString.split(" ")(2).toInt > _.toString.split(" ")(1).toInt)(0)
        array.maxBy(_.toString.split(" ")(2).toInt)
    }))

  }

  // 根据指定的规格获取集合中每个元素操作之后返回的结果
  def rule(array: Array[Any], operate: (Array[Any] => Any)): Any = {
    operate(array)
  }

}
