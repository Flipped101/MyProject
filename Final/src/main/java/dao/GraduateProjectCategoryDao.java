package dao;

import domain.GraduateProjectCategory;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class GraduateProjectCategoryDao {

    private static GraduateProjectCategoryDao graduateProjectCategoryDao= new GraduateProjectCategoryDao();
    private GraduateProjectCategoryDao(){};
    public static GraduateProjectCategoryDao getInstance(){
        return graduateProjectCategoryDao;
    }

    public Collection<GraduateProjectCategory> findAll() throws SQLException {
        Set<GraduateProjectCategory> graduateProjectCategorySet = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        //创建语句盒子
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECTCATEGORY");
        //遍历集合中的元素加入到graduateProjectCategorySet集合中
        while (resultSet.next()){
            //创建GraduateProjectCategory对象
            GraduateProjectCategory graduateProjectCategory = new GraduateProjectCategory(resultSet.getInt("id"),
                    resultSet.getString("description"),resultSet.getString("no"),
                    resultSet.getString("remarks"));

            graduateProjectCategorySet.add(graduateProjectCategory);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return graduateProjectCategorySet;
    }

    public GraduateProjectCategory find(Integer id) throws SQLException{
        GraduateProjectCategory graduateProjectCategory = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM PROJECTCATEGORY WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            graduateProjectCategory = new GraduateProjectCategory(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"));
        }
        JdbcHelper.close(resultSet,pstmt,connection);
        return graduateProjectCategory;
    }

    public boolean add(GraduateProjectCategory graduateProjectCategory) throws SQLException{
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECTCATEGORY (NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?)");
        preparedStatement.setString(1,graduateProjectCategory.getNo());
        preparedStatement.setString(2,graduateProjectCategory.getDescription());
        preparedStatement.setString(3,graduateProjectCategory.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteGraduateProjectCategory_sql = "DELETE FROM PROJECTCATEGORY WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProjectCategory_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(GraduateProjectCategory graduateProjectCategory) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String updateDepartment_sql = "UPDATE PROJECTCATEGORY SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //为预编译的语句参数赋值
        pstmt.setString(1,graduateProjectCategory.getDescription());
        pstmt.setString(2,graduateProjectCategory.getNo());
        pstmt.setString(3,graduateProjectCategory.getRemarks());
        pstmt.setInt(4,graduateProjectCategory.getId());
        //执行预编译对象的executeUpdate()方法，获取修改记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        GraduateProjectCategoryDao graduateProjectCategoryDao = new GraduateProjectCategoryDao();
        GraduateProjectCategory graduateProjectCategory = graduateProjectCategoryDao.find(3);
        System.out.println(graduateProjectCategory);
        //执行Dao对象的方法
//        domain.GraduateProjectCategory addGraduateProjectCategory = graduateProjectCategoryDao.addWithStoreProcedure(graduateProjectCategory);
//        graduateProjectCategoryDao.delete(465);
        graduateProjectCategory.setDescription("硕士");
        graduateProjectCategoryDao.update(graduateProjectCategory);
        GraduateProjectCategory graduateProjectCategory1 = graduateProjectCategoryDao.find(3);
        //打印修改后返回的对象
        System.out.println(graduateProjectCategory1);
        System.out.println("修改GraduateProjectCategory对象成功！");
    }

}

