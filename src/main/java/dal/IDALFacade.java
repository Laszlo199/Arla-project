package dal;

import be.ScreenElement;
import be.User;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IDALFacade extends IFile , IPDF, IScreen, IDefaultScreen{



    // Users add delete edit and all
    List<User> getAllUser()throws DALexception;
    void deleteUser(User user)throws DALexception;
    void updateUser(User oldUser, User newUser) throws DALexception;
    void createUser(User user) throws DALexception;
    boolean validate(String password) throws DALexception;

    List<ScreenElement> getScreenForUser(int userId) throws DALexception;

    User getUser(String username) throws DALexception;
}
