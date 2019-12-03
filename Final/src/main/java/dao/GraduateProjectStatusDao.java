package dao;

import domain.GraduateProjectStatus;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class GraduateProjectStatusDao {

    private static GraduateProjectStatusDao graduateProjectStatusDao= new GraduateProjectStatusDao();
    private GraduateProjectStatusDao(){};
    public static GraduateProjectStatusDao getInstance(){
        return graduateProjectStatusDao;
    }

    public Collection<GraduateProjectStatus> findAll() throws SQLException {
        Set<GraduateProjectStatus> graduateProjectStatusSet = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECTSTATUS");
        //遍历集合中的元素加入到graduateProjectStatusSet集合中
        while (resultSet.next()){
            //创建GraduateProjectStatus对象
            GraduateProjectStatus graduateProjectStatus = new GraduateProjectStatus(resultSet.getInt("id"),
                    resultSet.getString("description"),resultSet.getString("no"),
                    resultSet.getString("remarks"));

            graduateProjectStatusSet.add(graduateProjectStatus);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return graduateProjectStatusSet;
    }

    public GraduateProjectStatus find(Integer id) throws SQLException{
        GraduateProjectStatus graduateProjectStatus = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM PROJECTSTATUS WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            graduateProjectStatus = new GraduateProjectStatus(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return graduateProjectStatus;
    }

    public boolean add(GraduateProjectStatus graduateProjectStatus) throws SQLException{
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECTSTATUS (NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?)");
        preparedStatement.setString(1,graduateProjectStatus.getNo());
        preparedStatement.setString(2,graduateProjectStatus.getDescription());
        preparedStatement.setString(3,graduateProjectStatus.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteGraduateProjectStatus_sql = "DELETE FROM PROJECTSTATUS WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProjectStatus_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(GraduateProjectStatus graduateProjectStatus) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateDepartment_sql = "UPDATE PROJECTSTATUS SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,graduateProjectStatus.getDescription());
        pstmt.setString(2,graduateProjectStatus.getNo());
        pstmt.setString(3,graduateProjectStatus.getRemarks());
        pstmt.setInt(4,graduateProjectStatus.getId());
        //执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        GraduateProjectStatusDao graduateProjectStatusDao = new GraduateProjectStatusDao();
        GraduateProjectStatus graduateProjectStatus = graduateProjectStatusDao.find(3);
        System.out.println(graduateProjectStatus);
        //执行Dao对象的方法
//        domain.GraduateProjectStatus addGraduateProjectStatus = graduateProjectStatusDao.addWithStoreProcedure(graduateProjectStatus);
//        graduateProjectStatusDao.delete(465);
        graduateProjectStatus.setDescription("硕士");
        graduateProjectStatusDao.update(graduateProjectStatus);
        GraduateProjectStatus graduateProjectStatus1 = graduateProjectStatusDao.find(3);
        //打印修改后返回的对象
        System.out.println(graduateProjectStatus1);
        System.out.println("修改GraduateProjectStatus对象成功！");
    }

}

