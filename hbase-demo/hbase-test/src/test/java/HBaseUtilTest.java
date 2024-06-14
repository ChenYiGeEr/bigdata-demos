import com.lim.demos.hbase.util.HBaseUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

/**
 * HBaseUtilTest
 * <p>HBaseUtil的测试类</p>
 * @author lim
 * @version 1.0
 * @since 2023/8/14 11:38
 */
public class HBaseUtilTest {

    @After
    public void after () {
        try {
            HBaseUtil.closeConnection();
            System.out.println("HBase连接成功关闭");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doGet (Table table, String rowKey) throws IOException {
        // 封装一个get对象
        Result result = table.get(new Get(Bytes.toBytes(rowKey)));
        HBaseUtil.printResult(result);
        table.close();
    }

    @Test
    public void doPut () throws IOException {
        Table t1 = HBaseUtil.getTable("t1");
        Put put = HBaseUtil.getPut("1002", "info", "friend", "zhangsan");
        t1.put(put);
        t1.close();
        doGet(t1, "1002");
    }

    @Test
    public void doScanner () throws IOException {
        Table t1 = HBaseUtil.getTable("t1");
        Scan scan = new Scan();
        scan.withStartRow(Bytes.toBytes("1001"));
        scan.withStopRow(Bytes.toBytes("1003"));
        ResultScanner resultScanner = t1.getScanner(scan);
        for (Result result : resultScanner) {
            HBaseUtil.printResult(result);
        }
        resultScanner.close();
    }

    @Test
    public void doDelete () throws IOException {
        Table t1 = HBaseUtil.getTable("t1");
        Delete delete = new Delete(Bytes.toBytes("1001"));
        // 进一步明确删除 info:friend = 'lim'
        // delete.addColumn(Bytes.toBytes("info"), Bytes.toBytes("friend"));
        // 删除整列，对该列的整个列组进行删除
        t1.delete(delete);
        t1.close();
        doGet(t1, "1001");
    }
}
