package be;

import dal.Database.dataAccess.UserDAO;
import dal.exception.DALexception;

import java.util.List;

public class User {

    private int id;
    private String userName;
    private String password;
    private boolean isAdmin;
    private boolean isReset;
    private List<Integer> screens;

    public User(int id, String userName, String password,boolean isAdmin, boolean isReset){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isReset = isReset;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setReset(boolean reset) {
        isReset = reset;
    }

    public boolean isReset() {
        return isReset;
    }

    public void setScreens(List<Integer> screens) {
        this.screens = screens;
    }

    public List<Integer> getScreens(){
        return screens;
    }
}
