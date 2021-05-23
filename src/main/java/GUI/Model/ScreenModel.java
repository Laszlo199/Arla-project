package gui.Model;

import gui.Controller.ClientViewController;
import gui.Model.exception.ModelException;
import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import be.User;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import gui.util.Observator.IObservable;
import gui.util.Observator.Observer;
import gui.util.Observator.ObserverMany;

import gui.util.Observator.ObserverSingle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ScreenModel implements IObservable {
    private IFacade logic;


    private static ScreenModel screenModel;
    private ObservableList<DefaultScreen> defaultScreens;
    private ObservableList<Screen> mainScreens;
    private ScheduledExecutorService executorService;

    public ObservableList<Screen> getMainScreens() {
        return mainScreens;
    }

    {
        try {
            logic = Facade.getInstance();
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    private ScreenModel() {
        defaultScreens = FXCollections.observableArrayList();
        mainScreens = FXCollections.observableArrayList();
        loadDefaultScreens();
        loadMainScreens();
        StartObservatorThread();
    }

    private void StartObservatorThread() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
    }

        Runnable runnable = () -> {
            try {
                List<Screen> newScreens = logic.getMainScreens(); //it can be changed so that we wont ask db all the time
                List<Screen> modifiedScreens = logic.getModifiedScreens(newScreens, mainScreens);
                List<Screen> deletedScreens = logic.getDeletedScreens(newScreens, mainScreens);
                mainScreens.removeAll(deletedScreens);
                List<Screen> addedScreens = logic.getNewScreens(newScreens, mainScreens);
                mainScreens.addAll(addedScreens);
                System.out.println("Deleted screens 1: "+deletedScreens);
                System.out.println("Added screens 1: "+ addedScreens);
                notifyManyObservers(addedScreens, deletedScreens, modifiedScreens);
                notifySingleObservers(modifiedScreens);
            } catch (BLLException e) {
                e.printStackTrace();
            }
        };



    public ObservableList<DefaultScreen> getDefaultScreens() {
        return defaultScreens;
    }

    public void loadMainScreens() {
        try {
            List<Screen> screens = logic.getMainScreens();
            mainScreens.clear();
            mainScreens.addAll(screens);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public static ScreenModel getInstance() {
        if (screenModel == null)
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

    public void loadDefaultScreens() {
        try {
            List<DefaultScreen> list = logic.getAllDefaultScreens();
            defaultScreens.addAll(list);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    /**
     * method passes the data needed to save screen there are encapsulated daata for each section and
     * general info about screen
     *
     * @param screen
     * @param screenElements
     */
    public void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) {
        try {
            logic.save(screen, screenElements, usersList);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is not called now cause we have two this same methods and
     * other one is called
     *
     * @param currentScreen
     */
    public void deleteScreen(DefaultScreen currentScreen) {
        try {
            defaultScreens.remove(currentScreen);
            logic.deleteScreen(currentScreen);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void updateScreen(int id, DefaultScreen screen) {
        try {
            logic.updateScreen(id, screen);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void notifyManyObservers(List<Screen> added, List<Screen> deleted, List<Screen> modified) {
        System.out.println(" Added in notify:"+ added);
        System.out.println(" Deleted in notify:"+ deleted);
        for (ObserverMany observerMany : observersMany) {
            observerMany.update(added, deleted, modified);
        }
    }



    @Override
    public void notifySingleObservers(List<Screen> modified) {
        if(observersSingle.isEmpty())
            System.out.println("they are empty");

        for (Screen screen : modified)
            for (ObserverSingle observerSingle : observersSingle)
                if (screen.getId() == observerSingle.getScreen().getId()){
                    observerSingle.update();
                    System.out.println("we hit there");
                }
    }

    public void deletePuzzleScreen(Screen screen) {
        try {
            logic.deletePuzzleScreen(screen);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    List<ObserverMany> observersMany = new ArrayList<>();
    List<ObserverSingle> observersSingle= new ArrayList<>();

     public void attachManyObserver( ObserverMany observer) {
        observersMany.add(observer);
    }
     void detachManyObserver(ObserverMany observer) {
        observersMany.remove(observer);
    }

    public void attachSingleObserver( ObserverSingle observer) {
        observersSingle.add(observer);
    }
     void detachSingleObserver(ObserverSingle observer) {
        observersSingle.remove(observer);
    }
}
