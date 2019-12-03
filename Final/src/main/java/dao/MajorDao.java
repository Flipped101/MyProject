package dao;

import domain.Major;
import domain.Department;
import service.DepartmentService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class MajorDao {
	private static MajorDao majorDao = new MajorDao();
	private MajorDao(){}
	public static MajorDao getInstance(){
		return majorDao;
	}

	public Collection<Major> findAll() throws SQLException {
		Set<Major> majorss = new HashSet<>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("SELECT * FROM MAJOR");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			Major majors = new Major(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"),department);
			//向departemnts集合中添加Degree对象
			majorss.add(majors);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return majorss;
	}

	public Major find(Integer id) throws SQLException{
		Major majors = null;
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String str = "SELECT * FROM MAJOR WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(str);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		if(resultSet.next()){
			Department department = DepartmentService.getInstance().find(resultSet.getInt("department_id"));
			//创建Degree对象，根据遍历结果中的id,description,no,remarks值
			majors = new Major(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"),department);

		}
		//若结果集仍然有下一条记录，则执行循环体
		JdbcHelper.close(resultSet,pstmt,connection);
		return majors;
	}

	public boolean add(Major majors) throws SQLException {
		//加载驱动程序
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO MAJOR (NO,DESCRIPTION,REMARKS,DEPARTMENT_ID) VALUES" +
						"(?,?,?,?)");
		preparedStatement.setString(1,majors.getNo());
		preparedStatement.setString(2,majors.getDescription());
		preparedStatement.setString(3,majors.getRemarks());
		preparedStatement.setInt(4,majors.getDepartment().getId());
		int affectRowNum = preparedStatement.executeUpdate();
		System.out.println("增加了 " + affectRowNum + " 行。");
		JdbcHelper.close(preparedStatement,connection);
		return affectRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String deleteMajor_sql = "DELETE FROM MAJOR WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteMajor_sql);
		//为预编译的语句参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate()方法，获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 "+affectedRowNum+" 行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean update(Major majors) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String updateMajor_sql = "UPDATE MAJOR SET DESCRIPTION = ?, NO = ? ,REMARKS = ?, DEPARTMENT_ID = ? WHERE ID = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(updateMajor_sql);
		//为预编译的语句参数赋值
		pstmt.setString(1,majors.getDescription());
		pstmt.setString(2,majors.getNo());
		pstmt.setString(3,majors.getRemarks());
		pstmt.setInt(4,majors.getDepartment().getId());
		pstmt.setInt(5,majors.getId());
		//执行预编译对象的executeUpdate()方法，获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public static void main(String[] args) throws SQLException {
		MajorDao majorDao = new MajorDao();
		Major majors = majorDao.find(3);
		System.out.println(majors);
		majors.setDescription("环境工程");
		majorDao.update(majors);
		//打印添加后返回的对象
		Major majors2 = majorDao.find(3);
		System.out.println(majors2);
		System.out.println("修改Major对象成功！");
	}
}


	