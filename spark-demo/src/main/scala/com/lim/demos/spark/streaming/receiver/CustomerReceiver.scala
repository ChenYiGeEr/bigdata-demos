package com.lim.demos.spark.streaming.receiver

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets

/**
 * @param host ： 主机名称
 * @param port ： 端口号
 *  Receiver[String] ：返回值类型：String
 *  StorageLevel.MEMORY_ONLY： 返回值存储级别
 */
class CustomerReceiver (host: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){

  // receiver刚启动的时候，调用该方法，作用为：读数据并将数据发送给Spark
  override def onStart(): Unit = {
    //在onStart方法里面创建一个线程,专门用来接收数据
    new Thread("Socket Receiver") {
      override def run() {
        receive()
      }
    }.start()
  }

  // 读数据并将数据发送给Spark
  def receive(): Unit = {
    // 创建一个Socket
    var socket: Socket = new Socket(host, port)
    // 字节流读取数据不方便,转换成字符流buffer,方便整行读取
    val reader = new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8))
    // 读取数据
    var input: String = reader.readLine()
    //当receiver没有关闭并且输入数据不为空，就循环发送数据给Spark
    while (!isStopped() && input != null) {
      store(input)
      input = reader.readLine()
    }

    // 如果循环结束，则关闭资源
    reader.close()
    socket.close()

    // 重启接收任务
    restart("restart")
  }

  //
  override def onStop(): Unit = {}
}
