package httpapi.utils.dbutils;

import com.alibaba.fastjson.JSONObject;
import org.testng.collections.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JdbcUtil {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    /**
     * 读取资源文件获取值，只需要读取一次即可
     */
    static {
        try {
            //创建Propreties
            Properties pro = new Properties();

            InputStream inputStream = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            pro.load(inputStream);

            //获取数据、赋值
            url = pro.getProperty("url");
            user = pro.getProperty("username");
            password = pro.getProperty("password");
            driver = pro.getProperty("driverClassName");
            //注册驱动
            Class.forName(driver);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    /**
     * 获取链接
     * @return 连接对象
     */
    public static Connection getConnection(){
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }


    /**
     * 查询数据库操作
     * @param sql
     * @return
     */
    public static ResultSet getResultSetByQuery(String sql){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //调用方法获取连接
            connection = getConnection();
            //获取执行sql的对象 Statement
            preparedStatement = connection.prepareStatement(sql);
            //执行sql
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;

    }


    /**
     * 查询指定参数的数据
     * @param sql
     * @param para
     * @return
     */
    public static ResultSet getResultSetByTransPara(String sql,String para){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //调用方法获取连接
            connection = getConnection();
            //获取执行sql的对象 Statement
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,para);
            //执行sql
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;

    }

    /**
     * 更新数据库操作
     * @param sql  删除 更新数据
     * @return
     */
    public static int executeUpdate(String sql){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
      //  ResultSet resultSet = null;
        int count = 0;

        try {
            //调用方法获取连接
            connection = getConnection();
            //获取执行sql的对象 Statement
            preparedStatement = connection.prepareStatement(sql);
            //执行sql
            count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //
            JdbcUtil.close(preparedStatement,connection);
        }
        
        return count;
        
    }


    /**
     * 把ResultSet集合转换成String
     * @param rs
     * @return
     * @throws Exception
     */
    public static String formatRsToString(ResultSet rs) throws Exception {
        // 获取表结构
        ResultSetMetaData md = rs.getMetaData();
        String content = null;
        while (rs.next()){
            JSONObject json = new JSONObject();
            json.put(md.getColumnName(1),rs.getObject(1));
            content = json.getString("intention_content");
        }

        return content;

    }

    /**
     * 把结果集写入到List当中
     * @param resultSet
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> handleResultSet(ResultSet resultSet) throws Exception {
        List<Map<String, Object>> list = new ArrayList();
        if (resultSet != null && resultSet.last()) {
            resultSet.beforeFirst();
            ResultSetMetaData rsmd = resultSet.getMetaData();

            while(resultSet.next()) {
                Map<String, Object> map = Maps.newHashMap();

                for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
                    map.put(rsmd.getColumnLabel(i), resultSet.getString(i));
                }
                list.add(map);
            }
        }

        return list;
    }




    /**
     * 释放资源
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection){

        //避免空指针异常
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        if(statement != null){

            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * 释放资源
     * @param statement
     * @param connection
     */
    public static void close(Statement statement,Connection connection){

        if(statement != null){

            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }




}
