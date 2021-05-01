package dal;

import be.Users;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IDALFacade extends IFile , IPDF{



    // Users add delete edit and all
    List<Users> getAllUser()throws DALexception;
    void deleteUser(Users user)throws DALexception;
    void updateUser(Users oldUser, Users newUser) throws DALexception;
    void createUser(Users user) throws DALexception;
}
