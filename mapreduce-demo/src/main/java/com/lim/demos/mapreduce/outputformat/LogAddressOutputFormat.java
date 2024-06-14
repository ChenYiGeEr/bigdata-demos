package com.lim.demos.mapreduce.outputformat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * LogAddressOutputFormat
 *
 * @author lim
 * @version 1.0
 * @since 2023/7/24 21:26
 * Text         reducer写出的key类型
 * NullWritable reducer写出的value类型
 */
public class LogAddressOutputFormat extends FileOutputFormat<Text, NullWritable> {

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new LogAddressRecordWriter<>(context);
    }

    protected static class LogAddressRecordWriter<K, V> extends RecordWriter<K, V> {

        private FSDataOutputStream atguigu;
        private FSDataOutputStream other;

        private LogAddressRecordWriter (TaskAttemptContext context) throws IOException {
            try {
                // 1.创建hdfs文件系统
                FileSystem fileSystem = FileSystem.get(context.getConfiguration());
                // 2.创建流
                Path outputPath = FileOutputFormat.getOutputPath(context);
                // 2.1
                atguigu = fileSystem.create(new Path(outputPath, "atguigu.txt"));
                other = fileSystem.create(new Path(outputPath, "other.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 方法：write
         * <p>用来写数据 </p>
         *
         * @param key reducer写出的key
         * @param value reducer写出的value
         * @throws IOException
         * @throws InterruptedException
         * @since 2023/7/24 21:32
         * @author lim
         */
        @Override
        public synchronized void write(K key, V value) throws IOException, InterruptedException {
            String address = key.toString() + "\n";
            if (address.contains("atguigu")) {
                // 写到atguigu.txt
                atguigu.write(address.getBytes());
            } else {
                // 写到other.txt
                other.write(address.getBytes());
            }
        }

        /**
         * 方法：close
         * <p>关闭IO资源 </p>
         * @param context
         * @exception
         * @since 2023/7/24 21:33
         * @author lim
         */
        @Override
        public synchronized void close(TaskAttemptContext context) throws IOException {
            if (atguigu != null) {
                atguigu.close();
            }
            if (other != null) {
                other.close();
            }
        }

    }
}
