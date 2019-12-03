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
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECT");
        //���������Ȼ����һ����¼����ִ��ѭ����
        while(resultSet.next()) {
            GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(resultSet.getInt("projectCategory_id"));
            GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(resultSet.getInt("projectType_id"));
            GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(resultSet.getInt("projectStatus_id"));
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //����GraduateProjectType���󣬸��ݱ�������е�id,description,no,remarksֵ
            GraduateProject GraduateProject = new GraduateProject(resultSet.getInt("id"), resultSet.getString("title"),
                    graduateProjectCategory, graduateProjectType, graduateProjectStatus,teacher);
            //��graduateProjectTypes���������GraduateProjectType����
            GraduateProjects.add(GraduateProject);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,statement,connection);
        return GraduateProjects;
    }

    public GraduateProject find(Integer id) throws SQLException{
        GraduateProject GraduateProject = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM PROJECT WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(resultSet.getInt("projectCategory_id"));
            GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(resultSet.getInt("projectType_id"));
            GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(resultSet.getInt("projectStatus_id"));
            Teacher teacher = TeacherService.getInstance().find(resultSet.getInt("teacher_id"));
            //����GraduateProjectType���󣬸��ݱ�������е�id,description,no,remarksֵ
            GraduateProject = new GraduateProject(resultSet.getInt("id"),resultSet.getString("title"),
                    graduateProjectCategory,graduateProjectType,graduateProjectStatus,teacher);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,pstmt,connection);
        return GraduateProject;
    }

    public boolean update(GraduateProject GraduateProject) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //дsql���
        String updateGraduateProject_sql = "UPDATE PROJECT SET TITLE=?, PROJECTCATEGORY_ID=?,PROJECTTYPE_ID=?, PROJECTSTATUS_ID=?,TEACHER_ID=? WHERE ID=?";
        //�ڸ������ϴ���Ԥ����������
        PreparedStatement preparedStatement = connection.prepareStatement(updateGraduateProject_sql);
        //ΪԤ���������ֵ
        preparedStatement.setString(1,GraduateProject.getTitle());
        preparedStatement.setInt(2,GraduateProject.getGraduateProjectCategory().getId());
        preparedStatement.setInt(3,GraduateProject.getGraduateProjectType().getId());
        preparedStatement.setInt(4,GraduateProject.getGraduateProjectStatus().getId());
        preparedStatement.setInt(5,GraduateProject.getTeacher().getId());
        preparedStatement.setInt(6,GraduateProject.getId());
        //ִ��Ԥ������䣬��ȡ�ı��¼��������ֵ��affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //�ر���Դ
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }

    public boolean add (GraduateProject GraduateProject) throws SQLException{
        //������������
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
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteGraduateProject_sql = "DELETE FROM PROJECT WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProject_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        //���school�����Ա��graduateProjectStatustoadd��school�����Ը�ֵ
        GraduateProjectCategory graduateProjectCategory = GraduateProjectCategoryService.getInstance().find(2);
        GraduateProjectType graduateProjectType = GraduateProjectTypeService.getInstance().find(4);
        //����graduateProjectStatusToAdd����
        GraduateProjectStatus graduateProjectStatus = GraduateProjectStatusService.getInstance().find(3);

        //����Dao����
        GraduateProjectDao GraduateProjectDao = new GraduateProjectDao();
//		domain.GraduateProject GraduateProject = new domain.GraduateProject("��ͬ",graduateProjectCategory,graduateProjectType,graduateProjectStatus);
        GraduateProject GraduateProject = GraduateProjectDao.find(8);
        System.out.println(GraduateProject);
        GraduateProject.setTitle("��ͬ");
        GraduateProjectDao.update(GraduateProject);
        //ִ��Dao����ķ���
        GraduateProject GraduateProject1 = GraduateProjectDao.find(8);
//        domain.GraduateProject addGraduateProject = GraduateProjectDao.addWithStoreProcedure(GraduateProject);
//		GraduateProjectDao.delete(2);
        //��ӡ��Ӻ󷵻صĶ���
        System.out.println(GraduateProject1);
        System.out.println("�޸�GraduateProject����ɹ���");

//		domain.GraduateProject GraduateProject2 = new domain.GraduateProject(5,"�ﾲ",graduateProjectCategory,graduateProjectType,graduateProjectStatus);
//		GraduateProjectDao.add(GraduateProject2);
    }

}
