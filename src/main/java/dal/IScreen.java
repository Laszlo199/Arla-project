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
}
