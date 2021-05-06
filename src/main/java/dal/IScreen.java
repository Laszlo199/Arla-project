package dal;

import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IScreen {

    void save(Screen screen, List<ScreenElement> screenElements) throws DALexception;

    void saveDefaultTemplate(DefaultScreen defaultTemplate) throws DALexception;

    List<DefaultScreen> getAllDefaultScreens() throws DALexception;

    void deleteScreen(DefaultScreen screen) throws DALexception;

    void updateScreen(int id, DefaultScreen screen) throws DALexception;

}
