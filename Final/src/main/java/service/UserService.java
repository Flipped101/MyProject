package service;

import dao.UserDao;
import domain.User;

import java.sql.SQLException;

public final class UserService {
    private UserDao userDao = UserDao.getInstance();
    private static UserService userService = new UserService();

    public UserService() {
    }

    public static UserService getInstance(){
        return UserService.userService;
    }

    public User findUserById(Integer id) throws SQLException {
        return userDao.findUserById(id);
    }

    public User findUserByUserName(String userName) throws SQLException {
        return userDao.findUserByUserName(userName);
    }

    public boolean updateDate(String username) throws SQLException {
        return userDao.updateDate(username);
    }

    public boolean changePassWord(User user) throws SQLException {
        return userDao.changePassword(user);
    }

    public User login(String username, String password) throws SQLException {
        return userDao.login(username,password);
    }
}
