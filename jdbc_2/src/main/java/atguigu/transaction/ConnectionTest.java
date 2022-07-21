package atguigu.transaction;

import atguigu.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/21
 */
public class ConnectionTest {
    @Test
    public void  testConnection()throws Exception{
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);

    }
}
