package dal;

import be.DefaultScreen;
import be.Users;
import dal.Database.DBConnector;
import dal.Database.dataAccess.ScreenDAO;
import dal.Database.dataAccess.UserDAO;
import dal.File.PDFOperations;
import dal.File.ScreenOperations;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class DALFacade implements IDALFacade{

    private DBConnector dbConnector = new DBConnector();
    private UserDAO userDAO = new UserDAO();
    private static DALFacade dalFacade;
    private ScreenOperations screenOperations = new ScreenOperations();
    private PDFOperations pdfOperations = new PDFOperations();
    private ScreenDAO screenDAO = new ScreenDAO();


    public static DALFacade getInstance(){
        if(dalFacade==null)
            dalFacade = new DALFacade();
        return dalFacade;
    }

    @Override
    public void saveFile(Path originPath, Path destinationPath) throws DALexception {
        screenOperations.saveFile(originPath, destinationPath);
    }

    @Override
    public String getHistogramData(Path destinationPath) throws DALexception {
        return screenOperations.getHistogramData(destinationPath);
    }

    @Override
    public void deletePDFfiles(Path destinationPathPDF) throws DALexception {
        screenOperations.deletePDFfiles(destinationPathPDF);
    }

    @Override
    public void deleteCSV(Path destinationPathCSV) throws DALexception {
        screenOperations.deleteCSV(destinationPathCSV);
    }


    //Users Delete, Add, Edit, all
    @Override
    public List<Users> getAllUser() throws DALexception {
        return userDAO.getAll();
    }

    @Override
    public void deleteUser(Users user) throws DALexception {
        userDAO.delete(user);
    }

    @Override
    public void updateUser(Users oldUser, Users newUser) throws DALexception {
        userDAO.update(oldUser,newUser);
    }

    @Override
    public void createUser(Users user) throws DALexception {
        userDAO.create(user);
    }


    @Override
    public String getHTML(Path pdfPath) throws DALexception {
        return pdfOperations.getHTML(pdfPath);
    }

    @Override
    public void saveDefaultTemplate(DefaultScreen defaultTemplate) throws DALexception {
        screenDAO.saveDefaultTemplate(defaultTemplate);
    }

    @Override
    public List<DefaultScreen> getAllDefaultScreens() throws DALexception {
        return screenDAO.getAllDefaultScreens();
    }

    @Override
    public void deleteDefaultScreen(DefaultScreen defaultScreen) {
      //  screenDAO.deleteDefaultScreen
    }
}
