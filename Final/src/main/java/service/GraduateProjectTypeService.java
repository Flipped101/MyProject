package service;

import dao.GraduateProjectTypeDao;
import domain.GraduateProjectType;

import java.sql.SQLException;
import java.util.Collection;

public final class GraduateProjectTypeService {
    private static GraduateProjectTypeDao graduateProjectTypeDao
            = GraduateProjectTypeDao.getInstance();
    private static GraduateProjectTypeService graduateProjectTypeService
            =new GraduateProjectTypeService();
    private GraduateProjectTypeService(){}

    public static GraduateProjectTypeService getInstance(){
        return graduateProjectTypeService;
    }

    public Collection<GraduateProjectType> findAll() throws SQLException {
        return graduateProjectTypeDao.findAll();
    }

    public GraduateProjectType find(Integer id) throws SQLException {
        return graduateProjectTypeDao.find(id);
    }

    public boolean add(GraduateProjectType graduateProjectType) throws SQLException {
        return graduateProjectTypeDao.add(graduateProjectType);
    }

    public boolean delete(Integer id) throws SQLException {
        return graduateProjectTypeDao.delete(id);
    }

    public boolean update (GraduateProjectType graduateProjectType) throws SQLException {
        return graduateProjectTypeDao.update(graduateProjectType);
    }

//    public boolean delete(domain.GraduateProjectType graduateProjectType){
//        return graduateProjectTypeDao.delete(graduateProjectType);
//    }
}

