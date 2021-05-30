package bll;

import be.ScreenElement;
import be.User;
import bll.exception.BLLException;
import dal.exception.DALexception;

import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
// Users add, delete and all
public interface IFacade extends IScreen{
    List<User> getAllUser()throws BLLException;
    void deleteUser(User user)throws BLLException;
    void updateUser(User oldUser, User newUser) throws BLLException;
    void createUser(User user) throws BLLException;
    List<ScreenElement> getScreenForUser(int userId) throws BLLException;
    void resetPassword(User oldUser,User reset) throws BLLException;
    void updatePassword(User oldUser,String newPassword) throws BLLException;
    List<Integer> screensOfUser(int userID) throws BLLException;


    User getUser(String username) throws BLLException;

    void updateAssignedUsers(int screenID, List<User> selectedUsers) throws BLLException;

    //search function
    Predicate<User> createSearch(String searchText) throws BLLException;

    List<User> returnSelectedUsers(String selection) throws BLLException;
}
