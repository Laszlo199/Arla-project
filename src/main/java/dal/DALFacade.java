package dal;

import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import be.User;
import dal.Database.DBConnector;
import dal.Database.dataAccess.ScreenDAO;
import dal.Database.dataAccess.UserDAO;
import dal.File.PDFOperations;
import dal.File.ScreenOperations;
import dal.File.WatchFiles.ChangesFiles;
import dal.File.WatchFiles.FileWatcher;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.util.HashSet;
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
    private FileWatcher fileWatcher;

    {
        try {
            fileWatcher = new FileWatcher();
        } catch (DALexception daLexception) {
            throw new DALexception("Couldnt create an instance of filewatcher", daLexception);
        }
    }

    private DALFacade() throws DALexception {
    }

    public static DALFacade getInstance() throws DALexception {
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
    public List<User> getAllUser() throws DALexception {
        return userDAO.getAll();
    }

    @Override
    public void deleteUser(User user) throws DALexception {
        userDAO.delete(user);
    }

    @Override
    public void updateUser(User oldUser, User newUser) throws DALexception {
        userDAO.update(oldUser,newUser);
    }

    @Override
    public void createUser(User user) throws DALexception {
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

    @Override
    public void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) throws DALexception {
        screenDAO.save(screen, screenElements,usersList);
    }

    @Override
    public List<Screen> getMainScreens() throws DALexception {
        return screenDAO.getAllScreens();
    }

    public void deleteScreen(DefaultScreen screen) throws DALexception {
        screenDAO.deleteScreen(screen);
    }

    @Override
    public void updateScreen(int id, DefaultScreen screen) throws DALexception {
        screenDAO.updateScreen(id, screen);

    }

    @Override
    public List<ScreenElement> getScreenForUser(int userId) throws DALexception {
        return userDAO.getScreenForUser(userId);
    }

    @Override
    public void resetPassword(User oldUser,User reset) throws DALexception {
        userDAO.resetPassword(oldUser, reset);
    }

    @Override
    public void updatePassword(User oldUser, String newPassword) throws DALexception {
        userDAO.updatePassword(oldUser, newPassword);
    }

    @Override
    public List<Integer> screensOfUser(int userID) throws DALexception {
        return userDAO.screensOfUser(userID);
    }

    @Override
    public Screen getScreenByID(int id) throws DALexception {
        return screenDAO.getScreenByID(id);
    }

    @Override
    public ChangesFiles getModifiedFilePaths() {
        return fileWatcher.getChanges();
    }

    @Override
    public void deletePuzzleScreen(Screen screen) throws DALexception {
        screenDAO.deletePuzzleScreen(screen);
    }

    @Override
    public void update(Screen screen) throws DALexception {
        screenDAO.update(screen);
    }

    @Override
    public User getUser(String username) throws DALexception {
        return userDAO.getUser(username);
    }
}
