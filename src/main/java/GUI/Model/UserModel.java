package GUI.Model;

import be.Users;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {

    private IFacade iFacade;
    private ObservableList<Users> obsUsers;


    public UserModel() {
       this.obsUsers = FXCollections.observableArrayList();
       this.iFacade = Facade.getInstance();
    }

    public ObservableList<Users> getAllUser(){
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

    public void delete(Users selectedUser){
        try{
            iFacade.deleteUser(selectedUser);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.remove(selectedUser);
    }

    public void saveUser(Users user){
        try{
            iFacade.createUser(user);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.add(user);
    }

    public void updateUser(Users oldUser, Users newUser){
        try{
            iFacade.updateUser(oldUser,newUser);
        }catch (BLLException blLexception){
            blLexception.printStackTrace();
        }
        obsUsers.clear();
        obsUsers.addAll();
    }

}
