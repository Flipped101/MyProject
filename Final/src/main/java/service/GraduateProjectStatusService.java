package service;

import dao.GraduateProjectStatusDao;
import domain.GraduateProjectStatus;

import java.sql.SQLException;
import java.util.Collection;

public final class GraduateProjectStatusService {
    private static GraduateProjectStatusDao graduateProjectStatusDao
            = GraduateProjectStatusDao.getInstance();
    private static GraduateProjectStatusService graduateProjectStatusService
            =new GraduateProjectStatusService();
    private GraduateProjectStatusService(){}

    public static GraduateProjectStatusService getInstance(){
        return graduateProjectStatusService;
    }

    public Collection<GraduateProjectStatus> findAll() throws SQLException {
        return graduateProjectStatusDao.findAll();
    }

    public GraduateProjectStatus find(Integer id) throws SQLException {
        return graduateProjectStatusDao.find(id);
    }

    public boolean add(GraduateProjectStatus graduateProjectStatus) throws SQLException {
        return graduateProjectStatusDao.add(graduateProjectStatus);
    }

    public boolean delete(Integer id) throws SQLException {
        return graduateProjectStatusDao.delete(id);
    }

    public boolean update (GraduateProjectStatus graduateProjectStatus) throws SQLException {
        return graduateProjectStatusDao.update(graduateProjectStatus);
    }

//    public boolean delete(domain.GraduateProjectStatus graduateProjectStatus){
//        return graduateProjectStatusDao.delete(graduateProjectStatus);
//    }
}

