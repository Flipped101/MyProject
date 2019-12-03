package dao;

import domain.*;
import service.GraduateProjectTypeService;
import service.GraduateProjectStatusService;
import service.GraduateProjectCategoryService;
import service.TeacherService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class GraduateProjectDao {
    private static GraduateProjectDao GraduateProjectDao=new GraduateProjectDao();
    private GraduateProjectDao(){}
    public static GraduateProjectDao getInstance(){
        return GraduateProjectDao;
    }
    public Collection<GraduateProject> findAll() throws SQLException {
        Set<GraduateProject> GraduateProjects = new HashSet<>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECT");
        //若结果集仍然有下一条记录，则执行循环体
        while(resultSet.next()) {
            GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(resultSet.getInt("projectCategory_id"));
            GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(resultSet.getInt("projectType_id"));
            GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(resultSet.getInt("projectStatus_id"));
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //创建GraduateProjectType对象，根据遍历结果中的id,description,no,remarks值
            GraduateProject GraduateProject = new GraduateProject(resultSet.getInt("id"), resultSet.getString("title"),
                    graduateProjectCategory, graduateProjectType, graduateProjectStatus,teacher);
            //向graduateProjectTypes集合中添加GraduateProjectType对象
            GraduateProjects.add(GraduateProject);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return GraduateProjects;
    }

    public GraduateProject find(Integer id) throws SQLException{
        GraduateProject GraduateProject = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM PROJECT WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(resultSet.getInt("projectCategory_id"));
            GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(resultSet.getInt("projectType_id"));
            GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(resultSet.getInt("projectStatus_id"));
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //创建GraduateProjectType对象，根据遍历结果中的id,description,no,remarks值
            GraduateProject = new GraduateProject(resultSet.getInt("id"),resultSet.getString("title"),
                    graduateProjectCategory,graduateProjectType,graduateProjectStatus,teacher);
        }
        //关闭资源
        JdbcHelper.close(resultSet,pstmt,connection);
        return GraduateProject;
    }

    public boolean update(GraduateProject GraduateProject) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateGraduateProject_sql = "UPDATE PROJECT SET TITLE=?, PROJECTCATEGORY_ID=?,PROJECTTYPE_ID=?, PROJECTSTATUS_ID=?,TEACHER_ID=? WHERE ID=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateGraduateProject_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,GraduateProject.getTitle());
        preparedStatement.setInt(2,GraduateProject.getGraduateProjectCategory().getId());
        preparedStatement.setInt(3,GraduateProject.getGraduateProjectType().getId());
        preparedStatement.setInt(4,GraduateProject.getGraduateProjectStatus().getId());
        preparedStatement.setInt(5,GraduateProject.getTeacher().getId());
        preparedStatement.setInt(6,GraduateProject.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }

    public boolean add (GraduateProject GraduateProject) throws SQLException{
        //加载驱动程序
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECT (TITLE,PROJECTCATEGORY_ID,PROJECTTYPE_ID,PROJECTSTATUS_ID,TEACHER_ID) VALUES" +
                        "(?,?,?,?,?)");
        preparedStatement.setString(1,GraduateProject.getTitle());
        preparedStatement.setInt(2,GraduateProject.getGraduateProjectCategory().getId());
        preparedStatement.setInt(3,GraduateProject.getGraduateProjectType().getId());
        preparedStatement.setInt(4,GraduateProject.getGraduateProjectStatus().getId());
        preparedStatement.setInt(5,GraduateProject.getTeacher().getId());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("增加了 " + affectRowNum + " 行。");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteGraduateProject_sql = "DELETE FROM PROJECT WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProject_sql);
        //为预编译的语句参数赋值
        pstmt.setInt(1,id);
        //执行预编译对象的executeUpdate()方法，获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 "+affectedRowNum+" 行");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        //获得school对象，以便给graduateProjectStatustoadd的school的属性赋值
        GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(2);
        GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(4);
        //创建graduateProjectStatusToAdd对象
        GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(3);

        //创建Dao对象
        GraduateProjectDao GraduateProjectDao = new GraduateProjectDao();
//		domain.GraduateProject GraduateProject = new domain.GraduateProject("李同",graduateProjectCategory,graduateProjectType,graduateProjectStatus);
        GraduateProject GraduateProject = GraduateProjectDao.find(8);
        System.out.println(GraduateProject);
        GraduateProject.setTitle("苏同");
        GraduateProjectDao.update(GraduateProject);
        //执行Dao对象的方法
        GraduateProject GraduateProject1 = GraduateProjectDao.find(8);
//        domain.GraduateProject addGraduateProject = GraduateProjectDao.addWithStoreProcedure(GraduateProject);
//		GraduateProjectDao.delete(2);
        //打印添加后返回的对象
        System.out.println(GraduateProject1);
        System.out.println("修改GraduateProject对象成功！");

//		domain.GraduateProject GraduateProject2 = new domain.GraduateProject(5,"田静",graduateProjectCategory,graduateProjectType,graduateProjectStatus);
//		GraduateProjectDao.add(GraduateProject2);
    }

}
