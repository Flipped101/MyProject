package service;

import dao.MajorDao;
import domain.Major;

import java.sql.SQLException;
import java.util.Collection;

public final class MajorService {
    private static MajorDao majorDao
            = MajorDao.getInstance();
    private static MajorService majorService
            =new MajorService();
    private MajorService(){}

    public static MajorService getInstance(){
        return majorService;
    }

    public Collection<Major> findAll() throws SQLException {
        return majorDao.findAll();
    }

    public Major find(Integer id) throws SQLException {
        return majorDao.find(id);
    }

    public boolean add(Major major) throws SQLException {
        return majorDao.add(major);
    }

    public boolean delete(Integer id) throws SQLException {
        return majorDao.delete(id);
    }

    public boolean update (Major major) throws SQLException {
        return majorDao.update(major);
    }

//    public boolean delete(domain.Major major){
//        return majorDao.delete(major);
//    }
}

