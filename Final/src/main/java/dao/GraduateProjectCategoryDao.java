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
        //����������
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECTCATEGORY");
        //���������е�Ԫ�ؼ��뵽graduateProjectCategorySet������
        while (resultSet.next()){
            //����GraduateProjectCategory����
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
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM PROJECTCATEGORY WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
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
        //������������
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECTCATEGORY (NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?)");
        preparedStatement.setString(1,graduateProjectCategory.getNo());
        preparedStatement.setString(2,graduateProjectCategory.getDescription());
        preparedStatement.setString(3,graduateProjectCategory.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteGraduateProjectCategory_sql = "DELETE FROM PROJECTCATEGORY WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProjectCategory_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(GraduateProjectCategory graduateProjectCategory) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateDepartment_sql = "UPDATE PROJECTCATEGORY SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,graduateProjectCategory.getDescription());
        pstmt.setString(2,graduateProjectCategory.getNo());
        pstmt.setString(3,graduateProjectCategory.getRemarks());
        pstmt.setInt(4,graduateProjectCategory.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ�޸ļ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        GraduateProjectCategoryDao graduateProjectCategoryDao = new GraduateProjectCategoryDao();
        GraduateProjectCategory graduateProjectCategory = graduateProjectCategoryDao.find(3);
        System.out.println(graduateProjectCategory);
        //ִ��Dao����ķ���
//        domain.GraduateProjectCategory addGraduateProjectCategory = graduateProjectCategoryDao.addWithStoreProcedure(graduateProjectCategory);
//        graduateProjectCategoryDao.delete(465);
        graduateProjectCategory.setDescription("˶ʿ");
        graduateProjectCategoryDao.update(graduateProjectCategory);
        GraduateProjectCategory graduateProjectCategory1 = graduateProjectCategoryDao.find(3);
        //��ӡ�޸ĺ󷵻صĶ���
        System.out.println(graduateProjectCategory1);
        System.out.println("�޸�GraduateProjectCategory����ɹ���");
    }

}

