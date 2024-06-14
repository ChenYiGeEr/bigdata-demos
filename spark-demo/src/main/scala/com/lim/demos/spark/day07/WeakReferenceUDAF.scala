package com.lim.demos.spark.day07

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructField, StructType}


class WeakReferenceUDAF extends UserDefinedAggregateFunction {

  /** 输入参数的类型 */
  override def inputSchema: StructType = {
    StructType(
      Array(StructField("age", LongType))
    )
  }

  /** 中间变量的类型 */
  override def bufferSchema: StructType = {
    StructType(
      Array(
        StructField("sum", LongType),
        StructField("count", LongType),
      )
    )
  }

  /** 函数返回值的数据类型 */
  override def dataType: DataType = DoubleType

  /** 稳定性：对于相同的输入是否一直返回相同的输出 */
  override def deterministic: Boolean = true

  /** 函数缓冲区初始化 */
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    // sum = 0
    buffer(0) = 0L
    // count = 0
    buffer(1) = 0L
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    // sum += input
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    // count += 1
    buffer(1) = buffer.getLong(1) + 1L
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    // sum = sum1 + sum2
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    // count = count1 + count2
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  /** 计算最终结果 */
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}
