package bll;

import be.User;
import bll.exception.BLLException;

import java.util.List;

/**
 *
 */
public interface IUser {
    List<User> getAllUser()throws BLLException;
    void deleteUser(User user)throws BLLException;
    void updateUser(User oldUser, User newUser) throws BLLException;
    void createUser(User user) throws BLLException;
    void resetPassword(User oldUser,User reset) throws BLLException;
    void updatePassword(User oldUser,String newPassword) throws BLLException;
    User getUser(String username) throws BLLException;
}
