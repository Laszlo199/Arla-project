package be;

import java.util.List;

public class Screen {

    private int id;
    private String name;
    private int refreshTime;
    private int userID;
    private List<ScreenElement> screenElementList;

    public Screen(String name) {
        this.name = name;
    }

    public void addListElement(ScreenElement screenElement){
        screenElementList.add(screenElement);
    }

    /**
     * initially this constructor will be used.
     * @param id
     * @param name
     * @param screenElementList
     */
    public Screen(int id, String name, List<ScreenElement> screenElementList) {
        this.id = id;
        this.name = name;
        this.screenElementList = screenElementList;
    }

    public Screen(String name, int userID) {
        this.name = name;
        this.userID = userID;
    }

    public Screen(int id, String name, int refreshTime) {
        this.id = id;
        this.name = name;
        this.refreshTime = refreshTime;
    }

    public Screen(String name, int refreshTime, int userID) {
        this.name = name;
        this.refreshTime = refreshTime;
        this.userID = userID;
    }

    public Screen(int id, String name, int refreshTime, int userID) {
        this.id = id;
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
