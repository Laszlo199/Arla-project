package gui.Model;

import be.User;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {

    private IFacade iFacade;
    private ObservableList<User> obsUsers;


    public UserModel() {
       this.obsUsers = FXCollections.observableArrayList();
        try {
            this.iFacade = Facade.getInstance();
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<User> getAllUser(){
        return obsUsers;
    }

    public void loadUsers(){
        obsUsers.clear();
        try{
            obsUsers.addAll(iFacade.getAllUser());
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
    }

    public void delete(User selectedUser){
        try{
            iFacade.deleteUser(selectedUser);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.remove(selectedUser);
    }

    public void saveUser(User user){
        try{
            iFacade.createUser(user);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.add(user);
    }

    public void updateUser(User oldUser, User newUser){
        try{
            iFacade.updateUser(oldUser,newUser);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.clear();
        obsUsers.addAll();
    }

    public void resetPassword(User oldUser,User reset){
        try{
            iFacade.resetPassword(oldUser, reset);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.clear();
        obsUsers.addAll();
    }


}
