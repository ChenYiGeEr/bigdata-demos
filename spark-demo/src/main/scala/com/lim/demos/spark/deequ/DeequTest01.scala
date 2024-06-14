package com.lim.demos.spark.deequ

import org.apache.spark.sql.{SparkSession}
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.checks.{Check, CheckLevel, CheckStatus}
import com.amazon.deequ.constraints.ConstraintStatus

object DeequTest01 {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("deequTest01")
      .master("local")
      .getOrCreate()

  def main(args: Array[String]): Unit = {

    /** 待校验的数据，以后从db中拿 */
    val items = Seq(
      Item(1, "A", "awesome thing", null, "high", 0),
      Item(2, "B", "available at http://thingb.com", null, null, 0),
      Item(3, "C", null, "1226423683@qq.com", "low", 5),
      Item(4, "D", "check https://thingd.com", "lm13183271683@gmail.com", "low", 10),
      Item(5, "E", null, "13183271683@aliyun.com", "high", 12),
      Item(6, "F", "https://www.baidu.com", "13183271683@163.com", "high", 12)
    )

    val rdd = sparkSession.sparkContext.parallelize(items)
    val data = sparkSession.createDataFrame(rdd);

    val verificationResult = VerificationSuite()
      .onData(data)
      .addCheck(
        Check(
          CheckLevel.Error,
          "unit testing my data"
        )
          // 期望返回5列
          .hasSize(_ == items.length )
          // id 不为空
          .isComplete("id")
          // id 唯一
          .isUnique("id")
          // productName 不为空
          .isUnique("produceName")
          // 优先级只能是high或low
          // 枚举校验
          .isContainedIn("priority", Array("high","low"))
          // numViews不能为负数
          .isNonNegative("numViews")
          // 产品描述至少一半包含url
          .containsURL("description", _ >= 0.5)
          // 一半小于等于10
          .hasApproxQuantile("numViews", 0.5, _ <= 10)
          // 邮箱唯一
          .isUnique("email")
          // 邮箱正则校验
          .hasPattern("email", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$".r)
          .containsEmail("email")
      )
      .run();

    if (verificationResult.status == CheckStatus.Success) {
      print("The data passed the test, everything is fine")
    } else {
      print("We found errors in the data:\n")
      val resultsForAllConstraints = verificationResult.checkResults
        .flatMap{ case (_, checkResult) => checkResult.constraintResults }
      resultsForAllConstraints
        .filter { _.status != ConstraintStatus.Success }
        .foreach { result => print(s"${result.constraint}: ${ result.message.get }") }
    }
  }
}
