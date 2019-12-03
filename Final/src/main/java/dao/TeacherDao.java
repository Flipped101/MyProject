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
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("SELECT * FROM TEACHER");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()) {
			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("no"), resultSet.getString("name"),
					profTitle, degree, department);
			//向degrees集合中添加Degree对象
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}

    public Teacher find(Integer id) throws SQLException{
	    Teacher teacher = null;
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String str = "SELECT * FROM TEACHER WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(str);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()){
            ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
            Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
            //创建Degree对象，根据遍历结果中的id,description,no,remarks值
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("no"), resultSet.getString("name"),
                    profTitle,degree,department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,pstmt,connection);
        return teacher;
    }

//	public Collection<Teacher> findAllByDepartment(Integer department_id) throws SQLException {
//		Set<Teacher> teachers = new HashSet<>();
//		//获得连接对象
//		Connection connection = JdbcHelper.getConn();
//		Statement statement = connection.createStatement();
//		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
//		String str = "SELECT * FROM TEACHER WHERE DEPARTMENT_ID = ?";
//		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
//		PreparedStatement pstmt = connection.prepareStatement(str);
//		pstmt.setInt(1,department_id);
//		ResultSet resultSet = pstmt.executeQuery();
//		while(resultSet.next()) {
//			ProfTitle profTitle = ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id"));
//			Degree degree = DegreeService.getInstance().find(resultSet.getInt("degree_id"));
//			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
//			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
//			Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("no"), resultSet.getString("name"),
//					profTitle, degree, department);
//			//向degrees集合中添加Degree对象
//			teachers.add(teacher);
//		}
//		//关闭资源
//		JdbcHelper.close(resultSet,statement,connection);
//		return teachers;
//	}

	public boolean update(Teacher teacher) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateTeacher_sql = "UPDATE TEACHER SET NO = ?, NAME=?, PROFTITLE_ID=?,DEGREE_ID=?, DEPARTMENT_ID=? WHERE ID=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,teacher.getNo());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
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
			//加载驱动程序
			connection = JdbcHelper.getConn();
			//事务开始
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
			System.out.println("Teacher Table 增加了 " + affectTeacherRows + " 行。");

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
			System.out.println("User Table 增加了 " + affectUserRows + " 行。");

			//提交当前连接所做的操作
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage() + "\n errorCode = " + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null){
					//事务已回滚的方式结束
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try {
				//回复自动提交
				if (connection != null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
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
			//事务开始
			connection.setAutoCommit(false);

			String deleteUser_sql = "DELETE FROM USER WHERE TEACHER_ID = ?";
			//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
			preparedStatement = connection.prepareStatement(deleteUser_sql);
			//为预编译的语句参数赋值
			preparedStatement.setInt(1,id);
			//执行预编译对象的executeUpdate()方法，获取删除记录的行数
			affectUserRows = preparedStatement.executeUpdate();
			System.out.println("User Table 删除了 "+ affectUserRows +" 行");

			//创建sql语句，“？”作为占位符
			String deleteTeacher_sql = "DELETE FROM TEACHER WHERE ID = ?";
			//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
			preparedStatement = connection.prepareStatement(deleteTeacher_sql);
			//为预编译的语句参数赋值
			preparedStatement.setInt(1,id);
			//执行预编译对象的executeUpdate()方法，获取删除记录的行数
			affectTeacherRows = preparedStatement.executeUpdate();
			System.out.println("Teacher Table 删除了 "+ affectTeacherRows + " 行");

			//提交当前连接所做的操作
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage() + "\n errorCode = " + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null){
					//事务已回滚的方式结束
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try {
				//回复自动提交
				if (connection != null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement,connection);
		}
		return affectTeacherRows > 0 && affectUserRows > 0;
	}

	public static void main(String[] args) throws SQLException {
//		//获得school对象，以便给departmenttoadd的school的属性赋值
//        ProfTitle profTitle = ProfTitleService.getInstance().find(2);
//		Degree degree = DegreeService.getInstance().find(4);
//		//创建departmentToAdd对象
//		Department department = DepartmentService.getInstance().find(3);
//
//		//创建Dao对象
//		TeacherDao teacherDao = new TeacherDao();
////		domain.Teacher teacher = new domain.Teacher("李同",profTitle,degree,department);
//		Teacher teacher = teacherDao.find(8);
//        System.out.println(teacher);
//		teacher.setName("苏同");
//		teacherDao.update(teacher);
//		//执行Dao对象的方法
//		Teacher teacher1 = teacherDao.find(8);
////        domain.Teacher addTeacher = teacherDao.addWithStoreProcedure(teacher);
////		teacherDao.delete(2);
//		//打印添加后返回的对象
//		System.out.println(teacher1);
//		System.out.println("修改Teacher对象成功！");
//		System.out.println(teacherDao.findAllByDepartment(3));

	}

}
