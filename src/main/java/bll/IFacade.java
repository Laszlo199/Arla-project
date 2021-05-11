package bll;

import be.ScreenElement;
import be.User;
import bll.exception.BLLException;

import java.util.List;

/**
 *
 */
// Users add, delete and all
public interface IFacade extends IScreen, IDefaultScreen{
    List<User> getAllUser()throws BLLException;
    void deleteUser(User user)throws BLLException;
    void updateUser(User oldUser, User newUser) throws BLLException;
    void createUser(User user) throws BLLException;
    boolean validate(String password) throws BLLException;
    List<ScreenElement> getScreenForUser(int userId) throws BLLException;

    User getUser(String username) throws BLLException;
}
