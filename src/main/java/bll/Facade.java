package bll;

import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import be.User;
import bll.exception.BLLException;
import bll.util.DetectOtherScreens;
import dal.DALFacade;
import dal.File.WatchFiles.ChangesFiles;
import dal.IDALFacade;
import dal.exception.DALexception;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class Facade implements IFacade{
    private IDALFacade facade;
    private DiagramOperations diagramOperations;
    private static Facade facadeBLL;
    private DetectOtherScreens detectOtherScreens;



    public static IFacade getInstance() throws BLLException {
        if(facadeBLL==null)
            facadeBLL = new Facade();
        return facadeBLL;
    }



    private Facade() throws BLLException {
        try {
            facade = DALFacade.getInstance();
        } catch (DALexception daLexception) {
            throw new BLLException("Couldnt get Dal facade instance", daLexception);
        }
        diagramOperations =new DiagramOperations();
        detectOtherScreens = new DetectOtherScreens();
    }

    @Override
    public void saveFile(Path originPath, Path destinationPath) throws BLLException {
        try {
            facade.saveFile(originPath, destinationPath);
        } catch (DALexception daLexception) {
            throw new BLLException("couldnt save", daLexception);
        }
    }

    @Override
    public double[] getHistogramData(Path destinationPath) throws BLLException {
        String string = null;
        try {
            string = facade.getHistogramData(destinationPath);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldnt get data", daLexception);
        }
        return diagramOperations.getArray(string);
    }

    @Override
    public String getHTML(Path pdfPath) throws BLLException {
        try {
            return facade.getHTML(pdfPath);
        } catch (DALexception daLexception) {
            throw new BLLException("couldnt convert pdf to html", daLexception);
        }
    }

    @Override
    public void saveDefaultTemplate(DefaultScreen defaultTemplate) throws BLLException {
        try {
            facade.saveDefaultTemplate(defaultTemplate);
        } catch (DALexception daLexception) {
            throw new BLLException("Whoops...Couldn't save new default template", daLexception);
        }
    }

    @Override
    public void deletePDFfiles(Path destinationPathPDF) throws BLLException {
        try {
            facade.deletePDFfiles(destinationPathPDF);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't delete PDF and HTML file", daLexception);
        }
    }

    @Override
    public void deleteCSV(Path destinationPathCSV) throws BLLException {
        try {
            facade.deleteCSV(destinationPathCSV);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't delete CSV file", daLexception);
        }
    }

    @Override
    public void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) throws BLLException {
        try {
            facade.save(screen, screenElements, usersList);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't save added screen", daLexception);
        }
    }

    @Override
    public List<Screen> getMainScreens() throws BLLException {
        try {
            return facade.getMainScreens();
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't get all main screens", daLexception);

        }
    }

    @Override
    public Screen getScreenByID(int id) throws BLLException {
        try {
            return facade.getScreenByID(id);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't get all screens by ID", daLexception);
        }
    }

    @Override
    public List<Screen> getModifiedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens) {
        ChangesFiles modifiedFilepaths = facade.getModifiedFilePaths();
        return detectOtherScreens.getModifiedScreens(newScreens, mainScreens, modifiedFilepaths.getChangedFiles());
    }

    @Override
    public List<Screen> getDeletedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens) {
        return detectOtherScreens.getDeletedScreens(newScreens, mainScreens);
    }

    @Override
    public List<Screen> getNewScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens) {
        return detectOtherScreens.getNewScreens(newScreens, mainScreens);

    }

    @Override
    public void deletePuzzleScreen(Screen screen) throws BLLException {
        try {
            facade.deletePuzzleScreen(screen);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldnt delete puzzle screen", daLexception);
        }
    }

    @Override
    public List<DefaultScreen> getAllDefaultScreens() throws BLLException {
        try {
            return facade.getAllDefaultScreens();
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't get all  default screens", daLexception);
        }
    }

    @Override
    public void deleteScreen(DefaultScreen screen) throws BLLException {
        try {
            facade.deleteScreen(screen);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't delete screen", daLexception);
        }
    }

    @Override
    public void updateScreen(int id, DefaultScreen screen) throws BLLException {
        try {
            facade.updateScreen(id, screen);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldn't update screen", daLexception);
        }
    }

    // Users add, delete and all
    @Override
    public List<User> getAllUser() throws BLLException {
        try {
            return facade.getAllUser();
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't gat all Users");
        }
    }

    @Override
    public void deleteUser(User user) throws BLLException {
        try {
            facade.deleteUser(user);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't deleted User");
        }
    }

    @Override
    public void updateUser(User oldUser, User newUser) throws BLLException {
        try {
            facade.updateUser(oldUser,newUser);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't updated User");
        }
    }

    @Override
    public void createUser(User user) throws BLLException {
        try {
            facade.createUser(user);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't create User");
        }
    }

    @Override
    public boolean validate(String password) throws BLLException {
        try {
            return facade.validate(password);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops...");
        }
    }

    @Override
    public List<ScreenElement> getScreenForUser(int userId) throws BLLException {
        try {
            return facade.getScreenForUser(userId);
        } catch (DALexception daLexception) {
            daLexception.printStackTrace();
            throw new BLLException("Couldnt get a screen for user");
        }
    }

    @Override
    public void resetPassword(User oldUser,User reset) throws BLLException {
        try {
            facade.resetPassword(oldUser, reset);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't reset Password");
        }
    }

    @Override
    public void updatePassword(User oldUSer,String newPassword) throws BLLException {
        try {
            facade.updatePassword(oldUSer, newPassword);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't add new Password");
        }
    }

    @Override
    public List<Integer> screensOfUser(int userID) throws BLLException {
        try {
            return facade.screensOfUser(userID);
        } catch (DALexception daLexception) {
            daLexception.printStackTrace();
            throw new BLLException("Couldnt get a screen for user");
        }
    }

    @Override
    public User getUser(String username) throws BLLException {
        try {
            return facade.getUser(username);
        } catch (DALexception e) {
            e.printStackTrace();
            throw new BLLException("Couldn't check the login");
        }
    }

    @Override
    public void deleteDefaultScreen(DefaultScreen defaultScreen) {
        facade.deleteDefaultScreen(defaultScreen);
    }


}
