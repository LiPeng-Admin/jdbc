package com.atguigu.connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description
 * @Author lipeng
 * @create 2022/7/16
 */
public class ConnectionTest {
    //方式1：
    @Test
    public void testConnection1() throws SQLException {
        //1.提供java.sql.Driver接口实现类的对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        //2.提供url，指明具体操作的数据
        String url = "jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";

        //3.提供Properties的对象，指明用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        //4.调用driver的connect()，获取连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式2：对方式一的迭代
    @Test
    public void testConnection2() throws Exception {
        //1.实例化Driver:使用反射
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();
        //2.提供url，指明具体操作的数据

        String url = "jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";

        //3.提供Properties的对象，指明用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        //4.调用driver的connect()，获取连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);


    }

    //方式3：使用DriverManager 替换Driver
    @Test
    public void testConnection3() throws Exception {
        //1：获取Driver 实现类对象
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();
        //2:提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user = "root";
        String password = "123456";
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);


    }

    @Test
    public void testConnection4() throws Exception {
        //获取连接的三个基本信息
        String url = "jdbc:mysql://localhost:3306/test?&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user = "root";
        String password = "123456";

        //加载Driver
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");

        //省略注册驱动的步骤：原因是见下面的静态代码块
//        Driver driver = (Driver) aClass.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);

        /**
         *    static {
         *         try {
         *             DriverManager.registerDriver(new Driver());
         *         } catch (SQLException var1) {
         *             throw new RuntimeException("Can't register driver!");
         *         }
         *     }
         *
         * */

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    //方式五：将数据库连接的4个基本信息 声明在配置文件中，通过读取配置文件的方式，获取连接
    /*
     * 优势：
     * 1：实现了代码与数据的分离，实现了解耦
     * 2：如果需要修改配置文件信息，可以避免重新打包
     *
     * */
    @Test
    public void testConnection5() throws Exception {
        //1:获取配置文件中的4个基本信息

        InputStream resourceAsStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //2：加载驱动

        Class.forName(driverClass);

        //3：获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }
}
