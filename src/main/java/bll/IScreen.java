package bll;

import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import be.User;
import bll.exception.BLLException;
import dal.exception.DALexception;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public interface IScreen {
    void saveFile(Path originPath, Path destinationPath) throws BLLException;

    double[] getHistogramData(Path destinationPath) throws BLLException;

    String getHTML(Path pdfPath) throws BLLException;

    void saveDefaultTemplate(DefaultScreen defaultTemplate) throws BLLException;

    void deletePDFfiles(Path destinationPathPDF) throws BLLException;

    void deleteCSV(Path destinationPathCSV) throws BLLException;
    void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) throws BLLException;

    List<Screen> getMainScreens() throws BLLException;
    Screen getScreenByID(int id) throws BLLException;

    List<Screen> getModifiedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens) throws BLLException;

    List<Screen> getDeletedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens);

    List<Screen> getNewScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens);

    void deletePuzzleScreen(Screen screen) throws BLLException;


    void update(Screen screen) throws BLLException;

    List<ScreenElement> getSections(Screen screen) throws BLLException;

    void setRefreshes() throws BLLException;
}
