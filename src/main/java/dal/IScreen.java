package dal;

import be.DefaultTemplate;
import dal.exception.DALexception;

/**
 *
 */
public interface IScreen {
    void saveDefaultTemplate(DefaultTemplate defaultTemplate) throws DALexception;
}
