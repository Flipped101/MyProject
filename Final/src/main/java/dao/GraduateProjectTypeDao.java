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
        //����������
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PROJECTTYPE");
        //���������е�Ԫ�ؼ��뵽graduateProjectTypeSet������
        while (resultSet.next()){
            //����GraduateProjectType����
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
        //������Ӷ���
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM PROJECTTYPE WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
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
        //������������
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PROJECTTYPE (NO,DESCRIPTION,REMARKS) VALUES" +
                        "(?,?,?)");
        preparedStatement.setString(1,graduateProjectType.getNo());
        preparedStatement.setString(2,graduateProjectType.getDescription());
        preparedStatement.setString(3,graduateProjectType.getRemarks());
        int affectRowNum = preparedStatement.executeUpdate();
        System.out.println("������ " + affectRowNum + " �С�");
        JdbcHelper.close(preparedStatement,connection);
        return affectRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String deleteGraduateProjectType_sql = "DELETE FROM PROJECTTYPE WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(deleteGraduateProjectType_sql);
        //ΪԤ�������������ֵ
        pstmt.setInt(1,id);
        //ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("ɾ���� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean update(GraduateProjectType graduateProjectType) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String updateDepartment_sql = "UPDATE PROJECTTYPE SET DESCRIPTION = ?, NO = ? ,REMARKS = ? WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
        //ΪԤ�������������ֵ
        pstmt.setString(1,graduateProjectType.getDescription());
        pstmt.setString(2,graduateProjectType.getNo());
        pstmt.setString(3,graduateProjectType.getRemarks());
        pstmt.setInt(4,graduateProjectType.getId());
        //ִ��Ԥ��������executeUpdate()��������ȡ�޸ļ�¼������
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("�޸��� "+affectedRowNum+" ��");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public static void main(String[] args) throws SQLException {
        GraduateProjectTypeDao graduateProjectTypeDao = new GraduateProjectTypeDao();
        GraduateProjectType graduateProjectType = graduateProjectTypeDao.find(3);
        System.out.println(graduateProjectType);
        //ִ��Dao����ķ���
//        domain.GraduateProjectType addGraduateProjectType = graduateProjectTypeDao.addWithStoreProcedure(graduateProjectType);
//        graduateProjectTypeDao.delete(465);
        graduateProjectType.setDescription("˶ʿ");
        graduateProjectTypeDao.update(graduateProjectType);
        GraduateProjectType graduateProjectType1 = graduateProjectTypeDao.find(3);
        //��ӡ�޸ĺ󷵻صĶ���
        System.out.println(graduateProjectType1);
        System.out.println("�޸�GraduateProjectType����ɹ���");
    }

}

