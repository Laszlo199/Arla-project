package dal;

import be.DefaultScreen;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IDefaultScreen {
    void saveDefaultTemplate(DefaultScreen defaultTemplate) throws DALexception;
    List<DefaultScreen> getAllDefaultScreens() throws DALexception;
    void deleteDefaultScreen(DefaultScreen defaultScreen);
}
