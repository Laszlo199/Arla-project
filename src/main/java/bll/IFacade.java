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
public interface IFacade extends IScreen, IListener, IUser, ISearchUser, IFiles{
    List<ScreenElement> getScreenForUser(int userId) throws BLLException;
    List<Integer> screensOfUser(int userID) throws BLLException;
    void updateAssignedUsers(int screenID, List<User> selectedUsers) throws BLLException;
}
