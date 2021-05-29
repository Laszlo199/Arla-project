package dal;

import be.Screen;
import be.ScreenElement;
import be.User;
import dal.File.WatchFiles.ChangesFiles;
import dal.exception.DALexception;

import java.util.HashSet;
import java.util.List;

/**
 *
 */
public interface IScreen {
    void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) throws DALexception;

    List<Screen> getMainScreens() throws DALexception;
    Screen getScreenByID(int id) throws DALexception;

    ChangesFiles getModifiedFilePaths();

    void deletePuzzleScreen(Screen screen) throws DALexception;

    void update(Screen screen) throws DALexception;

    List<ScreenElement> getSections(Screen screen) throws DALexception;

    void clearChangedFiles();


    void setRefreshes() throws DALexception;

    List<String> getUsersForScreen(int id) throws DALexception;

    void saveToUsersAndScreens(int screenID, int userID)throws DALexception;
    int getScreenIDByName(String screenName) throws DALexception;

    void updateSections(List<ScreenElement> sections) throws DALexception;
}
