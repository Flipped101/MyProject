package dao;

import domain.Degree;
import domain.Department;
import domain.ProfTitle;
import domain.Teacher;
import service.DegreeService;
import service.DepartmentService;
import service.ProfTitleService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		Set<Teacher> teachers = new HashSet<>();
		//������Ӷ���
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
		ResultSet resultSet = statement.executeQuery("SELECT * FROM TEACHER");
		//���������Ȼ����һ����¼����ִ��ѭ����
		while(resultSet.next()) {
			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
			Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("no"), resultSet.getString("name"),
					profTitle, degree, department);
			//��degrees���������Degree����
			teachers.add(teacher);
		}
		//�ر���Դ
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}

    public Teacher find(Integer id) throws SQLException{
	    Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //����sql��䣬��������Ϊռλ��
        String str = "SELECT * FROM TEACHER WHERE ID = ?";
        //����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
            Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
            //����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("no"), resultSet.getString("name"),
                    profTitle,degree,department);
        }
        //�ر���Դ
        JdbcHelper.close(resultSet,pstmt,connection);
        return teacher;
    }

//	public Collection<Teacher> findAllByDepartment(Integer department_id) throws SQLException {
//		Set<Teacher> teachers = new HashSet<>();
//		//������Ӷ���
//		Connection connection = JdbcHelper.getConn();
//		Statement statement = connection.createStatement();
//		//ִ��SQL��ѯ��䲢��ý���������α�ָ�������Ŀ�ͷ��
//		String str = "SELECT * FROM TEACHER WHERE DEPARTMENT_ID = ?";
//		//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
//		PreparedStatement pstmt = connection.prepareStatement(str);
//		pstmt.setInt(1,department_id);
//		ResultSet resultSet = pstmt.executeQuery();
//		while(resultSet.next()) {
//			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
//			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
//			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
//			//����Degree���󣬸��ݱ�������е�id,description,no,remarksֵ
//			Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("no"), resultSet.getString("name"),
//					profTitle, degree, department);
//			//��degrees���������Degree����
//			teachers.add(teacher);
//		}
//		//�ر���Դ
//		JdbcHelper.close(resultSet,statement,connection);
//		return teachers;
//	}

	public boolean update(Teacher teacher) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//дsql���
		String updateTeacher_sql = "UPDATE TEACHER SET NO = ?, NAME=?, PROFTITLE_ID=?,DEGREE_ID=?, DEPARTMENT_ID=? WHERE ID=?";
		//�ڸ������ϴ���Ԥ����������
		PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
		//ΪԤ���������ֵ
		preparedStatement.setString(1,teacher.getNo());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		//ִ��Ԥ������䣬��ȡ�ı��¼��������ֵ��affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//�ر���Դ
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean add (Teacher teacher) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int affectUserRows = 0;
		int affectTeacherRows = 0;
		int teacher_id = 0;
		try{
			//������������
			connection = JdbcHelper.getConn();
			//����ʼ
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(
					"INSERT INTO TEACHER (NO,NAME,PROFTITLE_ID,DEGREE_ID,DEPARTMENT_ID) VALUES" +
							"(?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,teacher.getNo());
			preparedStatement.setString(2,teacher.getName());
			preparedStatement.setInt(3,teacher.getTitle().getId());
			preparedStatement.setInt(4,teacher.getDegree().getId());
			preparedStatement.setInt(5,teacher.getDepartment().getId());
			affectTeacherRows = preparedStatement.executeUpdate();
			System.out.println("Teacher Table ������ " + affectTeacherRows + " �С�");

//			ResultSet resultSet = preparedStatement.executeQuery("SELECT LAST_INSERT_ID()");
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()){
				teacher_id = resultSet.getInt(1);
			}

			preparedStatement = connection.prepareStatement(
					"INSERT INTO USER (USERNAME,PASSWORD,TEACHER_ID) VALUES" +
							"(?,?,?)");
			preparedStatement.setString(1,teacher.getNo());
			preparedStatement.setString(2,teacher.getNo());
			preparedStatement.setInt(3,teacher_id);
			affectUserRows = preparedStatement.executeUpdate();
			System.out.println("User Table ������ " + affectUserRows + " �С�");

			//�ύ��ǰ���������Ĳ���
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage() + "\n errorCode = " + e.getErrorCode());
			try {
				//�ع���ǰ���������Ĳ���
				if (connection != null){
					//�����ѻع��ķ�ʽ����
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try {
				//�ظ��Զ��ύ
				if (connection != null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//�ر���Դ
			JdbcHelper.close(preparedStatement,connection);
		}
		return affectTeacherRows > 0 && affectUserRows > 0;
	}

	public boolean delete(Integer id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int affectUserRows = 0;
		int affectTeacherRows = 0;
		try{
			connection = JdbcHelper.getConn();
			//����ʼ
			connection.setAutoCommit(false);

			String deleteUser_sql = "DELETE FROM USER WHERE TEACHER_ID = ?";
			//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
			preparedStatement = connection.prepareStatement(deleteUser_sql);
			//ΪԤ�������������ֵ
			preparedStatement.setInt(1,id);
			//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
			affectUserRows = preparedStatement.executeUpdate();
			System.out.println("User Table ɾ���� "+ affectUserRows +" ��");

			//����sql��䣬��������Ϊռλ��
			String deleteTeacher_sql = "DELETE FROM TEACHER WHERE ID = ?";
			//����PreparedStatement�ӿڶ��󣬰�װ������Ŀ����루�������ò�������ȫ�Ըߣ�
			preparedStatement = connection.prepareStatement(deleteTeacher_sql);
			//ΪԤ�������������ֵ
			preparedStatement.setInt(1,id);
			//ִ��Ԥ��������executeUpdate()��������ȡɾ����¼������
			affectTeacherRows = preparedStatement.executeUpdate();
			System.out.println("Teacher Table ɾ���� "+ affectTeacherRows + " ��");

			//�ύ��ǰ���������Ĳ���
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage() + "\n errorCode = " + e.getErrorCode());
			try {
				//�ع���ǰ���������Ĳ���
				if (connection != null){
					//�����ѻع��ķ�ʽ����
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try {
				//�ظ��Զ��ύ
				if (connection != null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//�ر���Դ
			JdbcHelper.close(preparedStatement,connection);
		}
		return affectTeacherRows > 0 && affectUserRows > 0;
	}

	public static void main(String[] args) throws SQLException {
//		//���school�����Ա��departmenttoadd��school�����Ը�ֵ
//        ProfTitle profTitle = ProfTitleService.getInstance().find(2);
//		Degree degree = DegreeService.getInstance().find(4);
//		//����departmentToAdd����
//		Department department = DepartmentService.getInstance().find(3);
//
//		//����Dao����
//		TeacherDao teacherDao = new TeacherDao();
////		domain.Teacher teacher = new domain.Teacher("��ͬ",profTitle,degree,department);
//		Teacher teacher = teacherDao.find(8);
//        System.out.println(teacher);
//		teacher.setName("��ͬ");
//		teacherDao.update(teacher);
//		//ִ��Dao����ķ���
//		Teacher teacher1 = teacherDao.find(8);
////        domain.Teacher addTeacher = teacherDao.addWithStoreProcedure(teacher);
////		teacherDao.delete(2);
//		//��ӡ��Ӻ󷵻صĶ���
//		System.out.println(teacher1);
//		System.out.println("�޸�Teacher����ɹ���");
//		System.out.println(teacherDao.findAllByDepartment(3));

	}

}
