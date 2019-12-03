package service;

import dao.GraduateProjectDao;
import domain.GraduateProject;

import java.sql.SQLException;
import java.util.Collection;

public final class GraduateProjectService {
    private static GraduateProjectDao graduateProjectDao
            = GraduateProjectDao.getInstance();
    private static GraduateProjectService graduateProjectService
            =new GraduateProjectService();
    private GraduateProjectService(){}

    public static GraduateProjectService getInstance(){
        return graduateProjectService;
    }

    public Collection<GraduateProject> findAll() throws SQLException {
        return graduateProjectDao.findAll();
    }

    public GraduateProject find(Integer id) throws SQLException {
        return graduateProjectDao.find(id);
    }

    public boolean add(GraduateProject graduateProject) throws SQLException {
        return graduateProjectDao.add(graduateProject);
    }

    public boolean delete(Integer id) throws SQLException {
        return graduateProjectDao.delete(id);
    }

    public boolean update (GraduateProject graduateProject) throws SQLException {
        return graduateProjectDao.update(graduateProject);
    }

//    public boolean delete(domain.GraduateProject graduateProject){
//        return graduateProjectDao.delete(graduateProject);
//    }
}

