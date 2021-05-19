package be;

public class UsersAndScreens {
    private int userID;
    private int screenID;

    public UsersAndScreens(int userID, int screenID ){
        this.userID = userID;
        this.screenID = screenID;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setScreenID(int screenID) {
        this.screenID = screenID;
    }

    public int getScreenID() {
        return screenID;
    }
}
