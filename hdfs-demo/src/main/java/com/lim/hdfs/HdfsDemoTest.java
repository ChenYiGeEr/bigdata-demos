package com.lim.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

/**
 * HdfsDemoTest
 * <p>代码操作hdfs</p>
 * @author lim
 * @version 1.0
 * @since 2023/7/20 13:48
 */
public class HdfsDemoTest {

    /* HDFS操作客户端 */
    private static volatile FileSystem fileSystem;

    /** 在测试方法执行前执行 双重检验方式创建fileSystem单例对象 */
    @Before
    public void createFileSystem () throws IOException, InterruptedException {
        // 1.创建客户端对象
        System.out.println("--- before ---");
        if (fileSystem == null) {
            synchronized (FileSystem.class) {
                if (fileSystem == null) {
                    System.out.println("--- 创建FileSystem客户端对象 ---");
                    Configuration config = new Configuration();
                    // 设置hdfs远程地址
                    config.set("fs.defaultFS", "hdfs://Cluster-136:8020");
                    // 设置副本数
//                    config.set("dfs.replication", "2");
                    // 设置访问用户名
                    config.set("hadoop.http.staticuser.user", "parallels");
                    // 创建方式1
                    fileSystem = FileSystem.get(
                            URI.create("hdfs://Cluster-136:8020"),
                            config,
                            "parallels"
                    );
                    // 创建方式2
//                    fileSystem = FileSystem.get(config);
                }
            }
        }
    }

    /** 在测试方法执行后执行 关闭客户端对象的io流 */
    @After
    public void closeFileSystem () throws IOException {
        // 3.关闭资源
        System.out.println("--- after ---");
        if (fileSystem != null) {
            System.out.println("--- 关闭FileSystem客户端对象 ---");
            fileSystem.close();
        }
    }

    /** 上传文件 */
    @Test
    public void upload () throws IOException {
        // 具体操作（上传）
        System.out.println("--- 执行上传逻辑 ---");
        if (fileSystem == null) { System.out.println("--- FileSystem不存在 ---"); }
        /*
            boolean delSrc 是否删除源文件
            boolean overwrite 如果目标文件存在是否覆盖
            Path src 源文件路径 macOS
            Path dst 目标文件路径 hdfs
         */
        Path src = new Path("/Users/lim/Desktop/a.txt"),
             dsc = new Path("/code_input");
        fileSystem.copyFromLocalFile(false, true, src, dsc);
    }

    /** 下载文件 */
    @Test
    public void download () throws IOException {
        // 具体操作（下载）
        System.out.println("--- 执行下载逻辑 ---");
        if (fileSystem == null) { System.out.println("--- FileSystem不存在 ---"); }
        /*
            boolean delSrc 是否删除源文件 hdfs
            Path src 源文件路径 hdfs
            Path dst 目标文件路径 macOS
            boolean useRawLocalFileSystem 是否使用useRawLocalFileSystem系统对象
        */
        Path src = new Path("/code_input/a.txt"),
             dsc = new Path("/Users/lim/Desktop/a1.txt");
        fileSystem.copyToLocalFile(false, src, dsc, false);
    }


}
