package be;

public class Screen {

    private int id;
    private String name;
    private int refreshTime;
    private int userID;

    public Screen(String name) {
        this.name = name;
    }

    public Screen(String name, int userID) {
        this.name = name;
        this.userID = userID;
    }

    public Screen(String name, int refreshTime, int userID) {
        this.name = name;
        this.refreshTime = refreshTime;
        this.userID = userID;
    }

    public Screen() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
