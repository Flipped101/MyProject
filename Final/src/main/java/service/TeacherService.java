package service;

import dao.TeacherDao;
import domain.Teacher;

import java.sql.SQLException;
import java.util.Collection;

public final class TeacherService {
	private static TeacherDao teacherDao= TeacherDao.getInstance();
	private static TeacherService teacherService=new TeacherService();
	private TeacherService(){}
	
	public static TeacherService getInstance(){
		return teacherService;
	}
	
	public Collection<Teacher> findAll() throws SQLException {
		return teacherDao.findAll();
	}
	
	public Teacher find(Integer id) throws SQLException {
		return teacherDao.find(id);
	}

//	public Collection<Teacher> findAllByDepartment(Integer department_id) throws SQLException{
//		return teacherDao.findAllByDepartment(department_id);
//	}
	
	public boolean add(Teacher teacher) {
		return teacherDao.add(teacher);
	}

	public boolean delete(Integer id) {
		return teacherDao.delete(id);
	}

	public boolean update(Teacher teacher) throws SQLException {
		return teacherDao.update(teacher);
	}
	
//	public boolean delete(domain.Teacher teacher){
//		//如果该教师有关联的课题，不能删除
//		if(teacher.getProjects().size()>0){
//			return false;
//		}
//		return teacherDao.delete(teacher);
//	}
}
