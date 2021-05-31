package bll;

import be.User;
import bll.exception.BLLException;

import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public interface ISearchUser {
    //search function
    Predicate<User> createSearch(String searchText) throws BLLException;
    List<User> returnSelectedUsers(String selection) throws BLLException;
}
