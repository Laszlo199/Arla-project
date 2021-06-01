package be;

import java.util.ArrayList;
import java.util.List;

public class Screen {

    private int id;
    private String name;
    private int refreshTime;
    private List<Integer> userIDs; //it wont be included in hashcode
    private List<ScreenElement> screenElementList;
    private boolean refreshNow = false;

    {
        screenElementList = new ArrayList<>();
    }

    public boolean isRefreshNow() {
        return refreshNow;
    }

    /**
     * after refreshing we need to set it back to false
     * @param refreshNow
     */
    public void setRefreshNow(boolean refreshNow) {
        this.refreshNow = refreshNow;
    }

    public Screen(String name) {
        this.name = name;
    }

    public void addListElement(ScreenElement screenElement) {
        screenElementList.add(screenElement);
    }

    public List<ScreenElement> getScreenElementList() {
        return screenElementList;
    }

    /**
     * initially this constructor will be used.
     *
     * @param id
     * @param name
     * @param screenElementList
     */
    public Screen(int id, String name, List<ScreenElement> screenElementList) {
        this.id = id;
        this.name = name;
        this.screenElementList = screenElementList;
    }


    public Screen(int id, String name, int refreshTime) {
        this.id = id;
        this.name = name;
        this.refreshTime = refreshTime;
    }

    public Screen(int id, String name, int refreshTime, boolean refreshNow) {
        this.id = id;
        this.name = name;
        this.refreshTime = refreshTime;
        this.refreshNow = refreshNow;
    }

    public Screen() {
    }

    public void setUserIDs(List<Integer> userIDs) {
        this.userIDs = userIDs;
    }

    public void setScreenElementList(List<ScreenElement> screenElementList) {
        this.screenElementList = screenElementList;
    }

    public int getId() {
        return id;
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


    @Override
    public String toString() {
        return "Screen{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", refreshTime=" + refreshTime +
                ", userIDs=" + userIDs +
                ", screenElementList=" + screenElementList +
                ", refreshNow=" + refreshNow +
                '}';
    }



}
