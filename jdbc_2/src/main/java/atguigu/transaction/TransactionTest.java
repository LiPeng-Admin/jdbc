package atguigu.transaction;

import atguigu.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @Description 实现数据BB
 *
 * @Author lipeng
 * @create 2022/7/21
 */
/*
* 数据库事务介绍
一组逻辑操作单元,使数据从一种状态变换到另一种状态。
*   一组逻辑操作单元：一个或多个DML操作
*
*事务处理（事务操作）：
    * 保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
    * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存下来；
    * 要么数据库管理系统将放弃所作的所有修改，整个事务**回滚(rollback)**到最初状态。
*
* 数据一旦提交，就不可回滚
*
* 哪些操作会导致数据的自动提交
* DDL 操作一旦执行，都会自动提交
*       通过set autocommit=false的方式取消DDL不会生效
* DML 默认情况下，一旦执行，就会自动提交
*       通过set autocommit=false 的方式取消DML 操作的自动提交
*
* 默认在关闭连接时，会自动的提交数据
*
* 保数据库中数据的一致性，数据的操纵应当是离散的成组的逻辑单元：
* 当它全部完成时，数据的一致性可以保持，而当这个单元中的一部分操作失败，整个事务应全部视为错误，所有从起始点以后的操作应全部回退到开始状态。
* */
public class TransactionTest {
    @Test
    public void testUpdate(){
        String sql="UPDATE user_table SET balance=balance+100 WHERE `user`=? ";
        update(sql,"AA");

        //模拟网络异常
        System.out.println(10/0);
        String sql2="UPDATE user_table SET balance=balance-100 WHERE `user`=?";
        update(sql2,"BB");
    }
    //通用的增删改操作

    public static int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();
            //预编译sql,返回PreparedStatement 实例

            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);

            }
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, ps);
        }

        return 0;


    }

}
