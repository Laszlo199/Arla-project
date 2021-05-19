package dal;

import be.DefaultScreen;
import be.Screen;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IDefaultScreen {
    void saveDefaultTemplate(DefaultScreen defaultTemplate) throws DALexception;
    List<DefaultScreen> getAllDefaultScreens() throws DALexception;
    void deleteDefaultScreen(DefaultScreen defaultScreen);
    void deleteScreen(DefaultScreen screen) throws DALexception;
    void updateScreen(int id, DefaultScreen screen) throws DALexception;
}
