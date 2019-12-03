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
        //����������
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECTSTATUS");
        //���������е�Ԫ�ؼ��뵽graduateProjectStatusSet������
        while (resultSet.next()){
            //����GraduateProjectStatus����
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
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM PROJECTSTATUS WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
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
        //������������
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECTSTATUS (NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?)");
        preparedStatement.setString(1,graduateProjectStatus.getNo());
        preparedStatement.setString(2,graduateProjectStatus.getDescription());
        preparedStatement.setString(3,graduateProjectStatus.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteGraduateProjectStatus_sql = "DELETE FROM PROJECTSTATUS WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProjectStatus_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(GraduateProjectStatus graduateProjectStatus) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateDepartment_sql = "UPDATE PROJECTSTATUS SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,graduateProjectStatus.getDescription());
        pstmt.setString(2,graduateProjectStatus.getNo());
        pstmt.setString(3,graduateProjectStatus.getRemarks());
        pstmt.setInt(4,graduateProjectStatus.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ�޸ļ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        GraduateProjectStatusDao graduateProjectStatusDao = new GraduateProjectStatusDao();
        GraduateProjectStatus graduateProjectStatus = graduateProjectStatusDao.find(3);
        System.out.println(graduateProjectStatus);
        //ִ��Dao����ķ���
//        domain.GraduateProjectStatus addGraduateProjectStatus = graduateProjectStatusDao.addWithStoreProcedure(graduateProjectStatus);
//        graduateProjectStatusDao.delete(465);
        graduateProjectStatus.setDescription("˶ʿ");
        graduateProjectStatusDao.update(graduateProjectStatus);
        GraduateProjectStatus graduateProjectStatus1 = graduateProjectStatusDao.find(3);
        //��ӡ�޸ĺ󷵻صĶ���
        System.out.println(graduateProjectStatus1);
        System.out.println("�޸�GraduateProjectStatus����ɹ���");
    }

}

