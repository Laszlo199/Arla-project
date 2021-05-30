package bll.util;

import be.User;

import java.util.function.Predicate;

public class UserViewUtils {

    public Predicate<User> createSearch(String searchText) {
        return user -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsUser(user, searchText);
        };
    }

    private boolean searchFindsUser(User user, String searchText) {
        return (user.getUserName().toLowerCase().contains(searchText.toLowerCase()));
    }


}
