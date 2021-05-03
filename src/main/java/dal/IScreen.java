package dal;

import be.DefaultScreen;
import dal.exception.DALexception;

import java.util.List;

/**
 *
 */
public interface IScreen {
    void saveDefaultTemplate(DefaultScreen defaultTemplate) throws DALexception;

    List<DefaultScreen> getAllDefaultScreens() throws DALexception;
}