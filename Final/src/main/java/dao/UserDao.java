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

        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM USER WHERE USERNAME = ? AND PASSWORD = ?";

        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1,username);
        pstmt.setString(2,password);

        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),teacher, resultSet.getTimestamp("loginTime"));
        }

        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public boolean updateDate (String username) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "UPDATE USER SET LOGINTIME = CURRENT_TIMESTAMP WHERE USERNAME = ?";
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1,username);
        int affectedRowNum = pstmt.executeUpdate();
        return affectedRowNum > 0;
    }

    public User findUserById(Integer id) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM USER WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),teacher, resultSet.getTimestamp("loginTime"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public User findUserByUserName(String username) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM USER WHERE USERNAME = ? ";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setString(1,username);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),teacher, resultSet.getTimestamp("loginTime"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return user;
    }

    public boolean changePassword(User user) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateUser_sql = "UPDATE USER SET PASSWORD = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,user.getPassword());
        pstmt.setInt(2,user.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ�޸ļ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) {
    }
}
