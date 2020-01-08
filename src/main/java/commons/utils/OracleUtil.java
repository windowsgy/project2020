package commons.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Oracle查询工具
 */


public class OracleUtil {

    private static Connection con;// 获取连接
    private static PreparedStatement pre;//
    private static ResultSet rs; //结果集

    public OracleUtil(String url, String username, String password) {
        try {
            Log.info("Load Oracle Driver");
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.error("Load Oracle Driver Fail:" + e.getMessage());
        }
        try {
            Log.info("Connect Oracle DB");
            con = DriverManager.getConnection(url, username, password);// 获取连接
            Log.info("Connect Oracle Success:");
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("Connect Oracle Fail:" + e.getMessage());
        }
    }

    /**
     * 查询数据库
     *
     * @param sql 查询语句
     * @return rs
     */

    public ResultSet select(String sql) {
        try {
            Log.info("SQL :" + sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            rs = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("Select Fail:" + e.getMessage());
            return null;
        }
        return rs;
    }

    /**
     * 删除数据
     *
     * @param sql SQL语句
     * @return rs
     */

    public ResultSet delete(String sql) {
        try {
            //Log.info("执行 delete 语句"+sql);
            pre = con.prepareStatement(sql);// 实例化预编译语句
            pre.executeUpdate();// 执行查询，注意括号中不需要再加参数
        } catch (Exception e) {
            Log.error("删除记录失败 :" + e.getMessage());
        }
        return rs;
    }

    /**
     * 关闭数据库连接
     */
    public void closeCon() {
        try {
            // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
            // 注意关闭的顺序，最后使用的最先关闭
            if (rs != null)
                rs.close();
            if (pre != null)
                pre.close();
            if (con != null)
                con.close();
            Log.info("Oracle Connect Close");
        } catch (Exception e) {
            Log.error("Oracle Close Error" + e.getMessage());
        }
    }

    /**
     * 插入数据
     *
     * @param sql sql
     * @return PreparedStatement
     */
    public PreparedStatement insert(String sql) {
        try {
            //Log.info("执行预编译:" + sql);
            pre = con.prepareStatement(sql);
        } catch (Exception e) {
            Log.error("Insert Fail:" + e.getMessage());
            return null;
        }
        return pre;
    }

    public PreparedStatement update(String sql) {
        try {
            Log.info("Update:" + sql);
            pre = con.prepareStatement(sql);
        } catch (Exception e) {
            Log.error("Update Fail:" + e.getMessage());
            return null;
        }
        return pre;
    }
}