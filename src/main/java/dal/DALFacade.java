package dal;

import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import be.User;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.Database.ObjectPool.ConnectionPool;
import dal.Database.dataAccess.ScreenDAO;
import dal.Database.dataAccess.UserDAO;
import dal.File.PDFOperations;
import dal.File.ScreenOperations;
import dal.File.WatchFiles.ChangesFiles;
import dal.File.WatchFiles.FileWatcher;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class DALFacade implements IDALFacade{

    private ConnectionPool connectionPool;
    private UserDAO userDAO;
    private static DALFacade dalFacade;
    private ScreenOperations screenOperations;
    private PDFOperations pdfOperations;
    private ScreenDAO screenDAO;
    private FileWatcher fileWatcher;


    {
        try {
            fileWatcher = new FileWatcher();
        } catch (DALexception daLexception) {
            throw new DALexception("Couldnt create an instance of filewatcher", daLexception);
        }
    }

    private DALFacade() throws DALexception {
        userDAO = new UserDAO();
        screenOperations = new ScreenOperations();
        pdfOperations = new PDFOperations();
        screenDAO = new ScreenDAO();
        connectionPool = ConnectionPool.getInstance();
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
       // return userDAO.getAll();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return userDAO.getAll(connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void deleteUser(User user) throws DALexception {
      //  userDAO.delete(user);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            userDAO.delete(user, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void updateUser(User oldUser, User newUser) throws DALexception {
       // userDAO.update(oldUser,newUser);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            userDAO.update(oldUser,newUser, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void createUser(User user) throws DALexception {
       // userDAO.create(user);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            userDAO.create(user, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public String getHTML(Path pdfPath) throws DALexception {
        return pdfOperations.getHTML(pdfPath);
    }


    @Override
    public void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) throws DALexception {
       // screenDAO.save(screen, screenElements,usersList);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            screenDAO.save(screen, screenElements,usersList, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Screen> getMainScreens() throws DALexception {
        Connection connection = null;
        try{
            connection = connectionPool.getConnection();
            return screenDAO.getAllScreens(connection);

        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }





    @Override
    public List<ScreenElement> getScreenForUser(int userId) throws DALexception {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return userDAO.getScreenForUser(userId, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void resetPassword(User oldUser,User reset) throws DALexception {
      //  userDAO.resetPassword(oldUser, reset);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            userDAO.resetPassword(oldUser, reset, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void updatePassword(User oldUser, String newPassword) throws DALexception {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            userDAO.updatePassword(oldUser, newPassword, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Integer> screensOfUser(int userID) throws DALexception {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return userDAO.screensOfUser(userID, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Screen getScreenByID(int id) throws DALexception {
        //
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return screenDAO.getScreenByID(id, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public ChangesFiles getModifiedFilePaths() {
        return fileWatcher.getChanges();
    }

    @Override
    public void deletePuzzleScreen(Screen screen) throws DALexception {
       // screenDAO.deletePuzzleScreen(screen);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            screenDAO.deletePuzzleScreen(screen, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void update(Screen screen) throws DALexception {
       // screenDAO.update(screen);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            screenDAO.update(screen, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
}
    public List<ScreenElement> getSections(Screen screen) throws DALexception {
       // return screenDAO.getSections(screen);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return screenDAO.getSections(screen, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void clearChangedFiles() {
        fileWatcher.clear();
    }

    @Override

    public void setRefreshes() throws DALexception {
        //screenDAO.setRefreshes();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            screenDAO.setRefreshes(connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    public List<String> getUsersForScreen(int id) throws DALexception {
       // return userDAO.getUsersForScreen(id);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return userDAO.getUsersForScreen(id, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public User getUser(String username) throws DALexception {
       // return userDAO.getUser(username);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return userDAO.getUser(username, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void updateAssignedUsers(int screenID, List<User> selectedUsers) throws DALexception {
        //userDAO.updateAssignedUsers(screenID, selectedUsers);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            userDAO.updateAssignedUsers(screenID, selectedUsers, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }


    @Override
    public void saveToUsersAndScreens(int screenID, int userID) throws DALexception {
       // screenDAO.saveToUsersAndScreens(screenID,userID);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            screenDAO.saveToUsersAndScreens(screenID,userID, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public int getScreenIDByName(String screenName) throws DALexception {
       // return screenDAO.getScreenIDByName(screenName);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            return screenDAO.getScreenIDByName(screenName, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void updateSections(List<ScreenElement> sections) throws DALexception {
      //  screenDAO.updateSections(sections);
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            screenDAO.updateSections(sections, connection);
        }catch (SQLServerException throwables) {
            throw new DALexception("Couldn't establish connection");
        }
        finally {
            if(connectionPool.isValid(connection))
                connectionPool.releaseConnection(connection);
        }
    }

}
