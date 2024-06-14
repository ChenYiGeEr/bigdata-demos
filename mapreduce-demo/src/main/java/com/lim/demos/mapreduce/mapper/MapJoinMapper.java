package com.lim.demos.mapreduce.mapper;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MapJoinMapper
 * <p>MapJoinMapper</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/24 22:07
 */
public class MapJoinMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

    private LongWritable outKey = new LongWritable();
    private Text outValue = new Text();

    private Map<String, String> pdMap = new LinkedHashMap<>();

    /**
     * 缓存pd.txt中的数据
     * 该方法在任务开始后时候只执行一次，在map方法执行之前执行
     * 作用：初始化
     */
    @Override
    protected void setup(Mapper<LongWritable, Text, LongWritable, Text>.Context context) throws IOException {
        FileSystem fileSystem = null;
        FSDataInputStream fsDataInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            // 1. 创建客户端对象
            fileSystem = FileSystem.get(context.getConfiguration());
            // 2. 创建流
            URI[] cacheFiles = context.getCacheFiles();
            if (cacheFiles.length > 0) {
                fsDataInputStream = fileSystem.open(new Path(cacheFiles[0]));
                // 3. 字符缓冲流读取数据
                bufferedReader = new BufferedReader(new InputStreamReader(fsDataInputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    String[] split = line.split("\t");
                    pdMap.put(split[0], split[1]);
                }
            }
        } finally {
            if (fileSystem != null) {
                fileSystem.close();
            }
            if (fsDataInputStream != null) {
                fsDataInputStream.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    /**
     * 该方法在任务开始后时候只执行一次，在map方法执行之后执行
     * 作用：关资源
     * */
    @Override
    protected void cleanup(Mapper<LongWritable, Text, LongWritable, Text>.Context context) {}


    /**
     * 方法：map
     * <p>读取order.txt的内容并进行join操作</p>
     *
     * @param key
     * @param value
     * @param context
     * @exception IOException
     * @exception InterruptedException
     * @since 2023/7/24 22:13
     * @author lim
     */
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context) throws IOException, InterruptedException {
        String[] splits = value.toString().split("\t");
        outKey.set(Long.parseLong(splits[0]));
        outValue.set(splits[1] + "\t" + pdMap.get(splits[1]) + "\t" + splits[2]);
        context.write(outKey, outValue);
    }


}
