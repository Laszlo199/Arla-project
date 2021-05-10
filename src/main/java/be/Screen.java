package be;

import java.util.ArrayList;
import java.util.List;

public class Screen {

    private int id;
    private String name;
    private int refreshTime;
    private List<Integer> userIDs;
    private List<ScreenElement> screenElementList = new ArrayList<>();

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
}
