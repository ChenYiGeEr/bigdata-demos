package com.lim
package chapter7

import scala.io.Source

/** 单词个数统计 */
object Test12_WordCount {

  def main(args: Array[String]): Unit = {
    // 1.读取文件中的数据，返回list集合
    // 2.统计单词出现个数
    println(
      Source
        // 从文件中读取出
        .fromFile("datas/wc.txt").getLines()
        // 转成list
        .toList
        // 先map后flatten
        .flatMap(_.split(" "))
        // 根据单词分组
        .groupBy(x => x)
        // 转换成map，key是单词，value是单词出现次数
        .map(x => (x._1, x._2.size))
        // 转成可变长map
        .toBuffer
        // 按照出现次数排序
        .sortBy((tuple: (String, Int)) => tuple._2)
        // 逆序，出现次数越多越靠前
        .reverse
        // 取前三名
        .take(3)
    )
  }

}