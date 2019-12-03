package dao;

import domain.GraduateProjectType;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class GraduateProjectTypeDao {

    private static GraduateProjectTypeDao graduateProjectTypeDao= new GraduateProjectTypeDao();
    private GraduateProjectTypeDao(){};
    public static GraduateProjectTypeDao getInstance(){
        return graduateProjectTypeDao;
    }

    public Collection<GraduateProjectType> findAll() throws SQLException {
        Set<GraduateProjectType> graduateProjectTypeSet = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECTTYPE");
        //遍历集合中的元素加入到graduateProjectTypeSet集合中
        while (resultSet.next()){
            //创建GraduateProjectType对象
            GraduateProjectType graduateProjectType = new GraduateProjectType(resultSet.getInt("id"),
                    resultSet.getString("description"),resultSet.getString("no"),
                    resultSet.getString("remarks"));

            graduateProjectTypeSet.add(graduateProjectType);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return graduateProjectTypeSet;
    }

    public GraduateProjectType find(Integer id) throws SQLException{
        GraduateProjectType graduateProjectType = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM PROJECTTYPE WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            graduateProjectType = new GraduateProjectType(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return graduateProjectType;
    }

    public boolean add(GraduateProjectType graduateProjectType) throws SQLException{
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECTTYPE (NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?)");
        preparedStatement.setString(1,graduateProjectType.getNo());
        preparedStatement.setString(2,graduateProjectType.getDescription());
        preparedStatement.setString(3,graduateProjectType.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteGraduateProjectType_sql = "DELETE FROM PROJECTTYPE WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProjectType_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(GraduateProjectType graduateProjectType) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateDepartment_sql = "UPDATE PROJECTTYPE SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,graduateProjectType.getDescription());
        pstmt.setString(2,graduateProjectType.getNo());
        pstmt.setString(3,graduateProjectType.getRemarks());
        pstmt.setInt(4,graduateProjectType.getId());
        //执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        GraduateProjectTypeDao graduateProjectTypeDao = new GraduateProjectTypeDao();
        GraduateProjectType graduateProjectType = graduateProjectTypeDao.find(3);
        System.out.println(graduateProjectType);
        //执行Dao对象的方法
//        domain.GraduateProjectType addGraduateProjectType = graduateProjectTypeDao.addWithStoreProcedure(graduateProjectType);
//        graduateProjectTypeDao.delete(465);
        graduateProjectType.setDescription("硕士");
        graduateProjectTypeDao.update(graduateProjectType);
        GraduateProjectType graduateProjectType1 = graduateProjectTypeDao.find(3);
        //打印修改后返回的对象
        System.out.println(graduateProjectType1);
        System.out.println("修改GraduateProjectType对象成功！");
    }

}

