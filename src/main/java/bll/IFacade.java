package bll;

import be.ScreenElement;
import be.Users;
import bll.exception.BLLException;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
// Users add, delete and all
public interface IFacade extends IScreen, IDefaultScreen{
    List<Users> getAllUser()throws BLLException;
    void deleteUser(Users user)throws BLLException;
    void updateUser(Users oldUser, Users newUser) throws BLLException;
    void createUser(Users user) throws BLLException;
    boolean validate(String password) throws BLLException;
    List<ScreenElement> getScreenForUser(int userId) throws BLLException;
}
