package dal;

import be.Screen;
import be.ScreenElement;
import be.User;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IDALFacade extends IFile , IPDF, IScreen{



    // Users add delete edit and all
    List<User> getAllUser()throws DALexception;
    void deleteUser(User user)throws DALexception;
    void updateUser(User oldUser, User newUser) throws DALexception;
    void createUser(User user) throws DALexception;

    List<ScreenElement> getScreenForUser(int userId) throws DALexception;
    void resetPassword(User oldUser,User reset) throws DALexception;
    void updatePassword(User oldUser,String newPassword) throws DALexception;
    List<Integer> screensOfUser(int userID) throws DALexception;

    User getUser(String username) throws DALexception;

    void updateAssignedUsers(int screenID, List<User> selectedUsers) throws DALexception;
}
