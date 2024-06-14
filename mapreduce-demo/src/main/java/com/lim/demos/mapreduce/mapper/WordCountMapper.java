package com.lim.demos.mapreduce.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Optional;

/**
 * WordCountMapper
 * <p>编写WordCountMapper实现通过mapreduce实现对单词的个数统计</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/21 16:56
 * KEYIN    LongWritable    读取数据的偏移量的类型
 * VALUEIN  Text            读取数据的类型
 * KEYOUT   Text            输出数据的偏移量的类型
 * VALUEOUT LongWritable     输出数据的类型
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    /* 无参构造 */
    public WordCountMapper () {}

    private Text outKey = new Text();

    private static final LongWritable LONG_ONE = new LongWritable(1);

    /**
     * 方法：map
     * <p>覆盖父类的map方法，在map方法中实现需要在MapTask中实现的业务逻辑代码 </p>
     * <p>该方法在被循环调用，每调用一次传入一行数据 </p>
     *
     * @param key 读取数据时的偏移量
     * @param value 读取的一行一行的数据
     * @param context 上下文 在这将key、value写出去
     * @throws IOException
     * @throws InterruptedException
     * @since 2023/7/21 17:03
     * @author lim
     * @version 1.0
     */
    @Override
    protected void map(LongWritable key,
                       Text value,
                       Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        // 1. 将数据进行切割
        // 1.1 将Text转成String
        String line = Optional.ofNullable(value).orElse(new Text(StringUtils.EMPTY)).toString();
        // 1.2 对数据进行切割
        String[] words = line.split(" ");
        // 2. 遍历数据
        for (String word : words) {
            // 3. 封装key value
            outKey.set(word);
            // 4. 将key value 写出去
            context.write(outKey, LONG_ONE);
        }
    }

}
