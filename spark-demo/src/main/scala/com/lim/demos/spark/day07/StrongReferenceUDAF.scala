package com.lim.demos.spark.day07

import org.apache.spark.sql.{Encoder, Encoders}
import org.apache.spark.sql.expressions.Aggregator


case class Buff(var sum: Long, var count: Long)

class StrongReferenceUDAF extends Aggregator[Long, Buff, Double] {

  /** 初始化中间变量 */
  override def zero: Buff = Buff(0,0)

  override def reduce(b: Buff, a: Long): Buff = {
    b.sum += a
    b.count += 1
    b
  }

  override def merge(b1: Buff, b2: Buff): Buff = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  override def finish(reduction: Buff): Double = {
    reduction.sum.toDouble / reduction.count
  }

  override def bufferEncoder: Encoder[Buff] = Encoders.product[Buff]

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
