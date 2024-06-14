package com.lim.demos.spark.day05

import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD的持久化
 *    应用场景
 *     1,一个Rdd在多个job中重复使用，每个job执行时该RDD之前的处理步骤也会重复执行
 *     2,如果一个job的依赖链条很长，如果计算出错或数据丢失需要重新从头计算 浪费大量时间
 *    存储时机  在缓存所在第一个job执行过程中
 *    持久化方式
 *     1,cache 存储位置 在分区所在主机的内存中
 *     2,persist 存储位置 在分区所在主机的内存/磁盘中
 *               工作中常用的存储级别 StorageLevel.MEMORY_ONLY(数据只保存在内存中，适用于数据量较小的情况下)
 *                                StorageLevel.MEMORY_AND_DISK(数据保存在内存和磁盘中，适用于数据量大的情况下)
 * CheckPoint 检查点：通过将RDD中间结果写入磁盘
 *    使用原因：
 *      缓存是将数据保存在分区所在主机的内存/磁盘中，如果服务器宕机，数据丢失，需要根据依赖关系重新进行计算
 *    RDD数据保存位置：
 *      HDFS等容错，高可用的文件系统
 *    使用场景：
 *      当RDD的依赖链条很长，需要多次重用时，可以使用检查点
 *    使用语法：
 *      1.sc.setCheckpointDir(hdfsPath)
 *      2.持久化RDD数据：rdd.checkpoint
 *    触发时间：
 *      对RDD进行Checkpoint操作并不会马上被执行，必须执行Action操作才能触发。但是检查点为了数据安全，会从血缘关系的最开始执行一遍。
 *    结合缓存使用，避免checkpoint触发的job重复执行 [rdd.cache + rdd.checkpoint]
 *
 *  缓存与checkpoint的区别
 *    1. 数据保存位置不一样 缓存将RDD数据保存在内存/磁盘中，而checkpoint将数据保存在hdfs中
 *    2. 缓存只是将数据保存起来，不切断血缘关系，checkpoint检查点切断血缘关系
 *    3. 数据缓存时机不一样
 *      1. 缓存是在cache RDD所在的第一个job执行过程中执行的
 *      2. checkpoint是checkpoint RDD所在的第一个job执行完成之后，会再次启动一个job执行到checkpoint RDD为止得到数据进行保存
 *    4. 如果使用完了缓存，可以通过unpersist方法释放缓存
 * */
object $01_CheckPoint {

  System.setProperty("HADOOP_USER_NAME", "parallels")

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("checkPointTest")
  )

  def main(args: Array[String]): Unit = {
    // 可以保存到本机或者hdfs
    sc.setCheckpointDir("hdfs://hadoop102:8020/checkpoint")
    val rdd = sc.textFile("datas/score.txt")
    val rdd1 = rdd.map(x => {
      println("________________________________________________________________")
      val splits = x.split(",")
      val stuName = splits(0)
      val projectName = splits(1)
      val score = splits(2).toDouble
      (stuName, projectName, score)
    })
    rdd1.cache()
    rdd1.checkpoint()
    println(rdd1.collect().toList)
    val rdd2 = rdd1.sortBy(_._3)
    println(rdd2.collect().toList)
  }

}
