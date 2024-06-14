package com.lim
package test

import java.text.SimpleDateFormat
import scala.io.Source

object Test02 {

  /** 查询每个区域的平均等客时间 */
  def main(args: Array[String]): Unit = {
    // 0. 准备工作
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 1. 读取数据
    val lines:List[String] = Source.fromFile("datas/taxi.txt").getLines().toList;;
    // 2. 过滤数据
    // 3. 列裁剪[用户id、下车区域、上车时间、下车时间]
    lines.map(line => {
      val fields = line.split(",")
      // userId, toRegion, fromTime, toTime
      (fields(0), fields(2), timeFormat.parse(fields(3)).getTime, timeFormat.parse(fields(4)).getTime)
    })
    // 4. 根据用户id进行分组
    .groupBy(_._1)
    // 5. 对时间进行排序、滑窗
    .toList
    .flatMap(x => {
      // 按照上车时间进行升序排序
      val sortedList = x._2.sortBy(y => y._3)
      // 2 滑窗
      val it = sortedList.sliding(2).toList
      it.map(y => {
        // region （下次上车时间 - 上次下车时间）单位转化成秒
        (y.head._2, (y.last._3 - y.head._4).toDouble / 1000)
      })
    })
    .groupBy(_._1).map(x => {
        var sumTime = x._2.map(_._2).sum
        var times = x._2.map(_._2).size
        (x._1, sumTime / times)
      })
    .foreach(println)
  }
}
