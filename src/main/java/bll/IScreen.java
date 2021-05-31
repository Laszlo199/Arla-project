package bll;

import be.Screen;
import be.ScreenElement;
import be.User;
import bll.exception.BLLException;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public interface IScreen {
    void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList)
            throws BLLException;
    List<Screen> getMainScreens() throws BLLException;
    Screen getScreenByID(int id) throws BLLException;
    void deletePuzzleScreen(Screen screen) throws BLLException;
    void update(Screen screen) throws BLLException;
    List<ScreenElement> getSections(Screen screen) throws BLLException;
    void setRefreshes() throws BLLException;
    List<String> getUsersForScreen(int id) throws BLLException;
    void saveToUsersAndScreens(int screenID, int userID) throws BLLException;
    int getScreenIDByName(String screenName) throws BLLException;
    void updateSections(List<ScreenElement> sections) throws BLLException;
}
