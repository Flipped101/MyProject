package dao;

import domain.Teacher;
import domain.User;
import service.TeacherService;
import service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class UserDao {
    private static UserDao userDao = new UserDao();

    private UserDao() { }

    public static UserDao getInstance() {
        return userDao;
    }

    public User login(String username, String password) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();

        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM USER WHERE USERNAME = ? AND PASSWORD = ?";

        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1,username);
        pstmt.setString(2,password);

        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),teacher, resultSet.getTimestamp("loginTime"));
        }

        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public boolean updateDate (String username) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "UPDATE USER SET LOGINTIME = CURRENT_TIMESTAMP WHERE USERNAME = ?";
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1,username);
        int affectedRowNum = pstmt.executeUpdate();
        return affectedRowNum > 0;
    }

    public User findUserById(Integer id) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM USER WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),teacher, resultSet.getTimestamp("loginTime"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public User findUserByUserName(String username) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM USER WHERE USERNAME = ? ";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1,username);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),teacher, resultSet.getTimestamp("loginTime"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public boolean changePassword(User user) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateUser_sql = "UPDATE USER SET PASSWORD = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,user.getPassword());
        pstmt.setInt(2,user.getId());
        //执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) {
    }
}
