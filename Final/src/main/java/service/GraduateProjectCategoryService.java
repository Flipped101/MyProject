package service;

import dao.GraduateProjectCategoryDao;
import domain.GraduateProjectCategory;

import java.sql.SQLException;
import java.util.Collection;

public final class GraduateProjectCategoryService {
    private static GraduateProjectCategoryDao graduateProjectCategoryDao
            = GraduateProjectCategoryDao.getInstance();
    private static GraduateProjectCategoryService graduateProjectCategoryService
            =new GraduateProjectCategoryService();
    private GraduateProjectCategoryService(){}

    public static GraduateProjectCategoryService getInstance(){
        return graduateProjectCategoryService;
    }

    public Collection<GraduateProjectCategory> findAll() throws SQLException {
        return graduateProjectCategoryDao.findAll();
    }

    public GraduateProjectCategory find(Integer id) throws SQLException {
        return graduateProjectCategoryDao.find(id);
    }

    public boolean add(GraduateProjectCategory graduateProjectCategory) throws SQLException {
        return graduateProjectCategoryDao.add(graduateProjectCategory);
    }

    public boolean delete(Integer id) throws SQLException {
        return graduateProjectCategoryDao.delete(id);
    }

    public boolean update (GraduateProjectCategory graduateProjectCategory) throws SQLException {
        return graduateProjectCategoryDao.update(graduateProjectCategory);
    }

//    public boolean delete(domain.GraduateProjectCategory graduateProjectCategory){
//        return graduateProjectCategoryDao.delete(graduateProjectCategory);
//    }
}

