package be;

public class User {

    private int id;
    private String userName;
    private String password;
    private int screenId;
    private boolean isAdmin;


    public User(int id, String userName, String password, int screenId, boolean isAdmin ){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.screenId = screenId;
        this.isAdmin = isAdmin;
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

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
