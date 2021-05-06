package GUI.Model;

import GUI.Model.exception.ModelException;
import GUI.util.Observator.IObserver;
import GUI.util.Observator.Observable;
import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class ScreenModel extends Observable<DefaultScreen> {
    private IFacade logic = new Facade();
    private static ScreenModel screenModel;
    private ObservableList<DefaultScreen> defaultScreens;

    private ScreenModel() {
        defaultScreens = FXCollections.observableArrayList();
        loadDefaultScreens();
    }

    public static ScreenModel getInstance(){
        if(screenModel==null)
            screenModel = new ScreenModel();
        return screenModel;
    }


    public void saveFile(Path originPath, Path destinationPath) throws ModelException {
        try {
            logic.saveFile(originPath, destinationPath);
        } catch (BLLException e) {
            throw new ModelException("couldn't save", e);
        }
    }

    public double[] getHistogramData(Path destinationPath) throws ModelException {
        try {
            return logic.getHistogramData(destinationPath);
        } catch (BLLException e) {
            throw new ModelException("couldnt get data", e);
        }
    }

    public String getHTML(Path pdfPath) {
        try {
            return logic.getHTML(pdfPath);
        } catch (BLLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDefaultTemplate(DefaultScreen defaultTemplate) {
        defaultScreens.add(defaultTemplate);
        try {
            logic.saveDefaultTemplate(defaultTemplate);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void deletePDFfiles(Path destinationPathPDF) {
        try {
            logic.deletePDFfiles(destinationPathPDF);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCSV(Path destinationPathCSV) {
        try {
            logic.deleteCSV(destinationPathCSV);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public List<DefaultScreen> getAllDefaultScreens() throws ModelException {
       return defaultScreens;
    }

    public void loadDefaultScreens(){
        try {
            List<DefaultScreen> list = logic.getAllDefaultScreens();
            defaultScreens.addAll(list);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDefaultScreen(DefaultScreen defaultScreen) {
        observers.remove(defaultScreen);
        logic.deleteDefaultScreen(defaultScreen);
    }

    @Override
    public void notifyObservers(DefaultScreen added, DefaultScreen deleted, DefaultScreen modified) {
        for(IObserver o: super.observers){
            o.update(added, deleted, modified);
        }
    }

    /**
     * method passes the data needed to save screen there are encapsulated daata for each section and
     * general info about screen
     * @param screen
     * @param screenElements
     */
    public void save(Screen screen, List<ScreenElement> screenElements) {
        logic.save(screen, screenElements);
    }
}
