package bll.util;

import be.User;
import bll.IFacade;
import bll.exception.BLLException;
import dal.DALFacade;
import dal.IDALFacade;
import dal.exception.DALexception;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Predicate;

public class UserViewUtils {
    private IDALFacade dal;

    {
        try {
            dal = DALFacade.getInstance();
        } catch (DALexception daLexception) {
            daLexception.printStackTrace();
        }
    }


    public Predicate<User> createSearch(String searchText) {
        return user -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchFindsUser(user, searchText);
        };
    }

    private boolean searchFindsUser(User user, String searchText) {
        return (user.getUserName().toLowerCase().contains(searchText.toLowerCase()));
    }



    public List<User> returnSelectedUsers(String selection) throws BLLException {
        try {
            List<User> listOfUsers = dal.getAllUser();

            if (selection == "Admins"){
                ObservableList<User> admins = FXCollections.observableArrayList();
                for (int i = 0; i<listOfUsers.size(); i++){
                    if (listOfUsers.get(i).isAdmin()){
                        admins.add(listOfUsers.get(i));
                    }
                }
                return admins;
            }
            else if (selection == "Users")
            {
                ObservableList<User> users = FXCollections.observableArrayList();
                for (int i = 0; i<listOfUsers.size(); i++){
                    if (!listOfUsers.get(i).isAdmin()){
                        users.add(listOfUsers.get(i));
                    }
                }
                return users;
            }
            return listOfUsers;
        }catch (DALexception daLexception){
            throw new BLLException("Couldnt get admins/users", daLexception);
        }

    }


}
