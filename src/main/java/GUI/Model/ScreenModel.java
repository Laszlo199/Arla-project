package gui.Model;

import gui.Model.exception.ModelException;
import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import be.User;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import gui.util.Observator.IObservable;
import gui.util.Observator.ObserverMany;

import gui.util.Observator.ObserverSingle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    List<ObserverMany> observersMany = new ArrayList<>();
    List<ObserverSingle> observersSingle= new ArrayList<>();
    private static ScreenModel screenModel;
    private ObservableList<Screen> mainScreens;
    private ScheduledExecutorService executorService;
    private ScheduledExecutorService ex2;

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

    public ScreenModel() {
        mainScreens = FXCollections.observableArrayList();
        loadMainScreens();
        StartObservatorThread();
    }
    List<Screen> forgetAbout = new ArrayList<>();

    private void StartObservatorThread() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        ex2 = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);
        ex2.scheduleWithFixedDelay(runnable2, 6, 6, TimeUnit.SECONDS);
    }

        Runnable runnable = () -> {
            try {
                List<Screen> newScreens = logic.getMainScreens(); //it can be changed so that we wont ask db all the time
                List<Screen> modifiedScreens = logic.getModifiedScreens(newScreens, mainScreens);
                List<Screen> deletedScreens = logic.getDeletedScreens(newScreens, mainScreens);
                List<Screen> addedScreens = logic.getNewScreens(newScreens, mainScreens);
                mainScreens.addAll(addedScreens);
                mainScreens.removeAll(deletedScreens);
                modifiedScreens.removeAll(forgetAbout);
                notifyManyObservers(addedScreens, deletedScreens, modifiedScreens);
                listenRefreshNow(modifiedScreens);

            } catch (BLLException e) {
                e.printStackTrace();
            }
        };

    Runnable runnable2 = () ->{
        try {
            logic.setRefreshes(); //sets to 0
        } catch (BLLException e) {
            e.printStackTrace();
        }
        forgetAbout.clear();
    };


    private void listenRefreshNow(List<Screen> modifiedScreens){
        //List<Screen> helper = new ArrayList<>();
        for(Screen m: modifiedScreens){
            if(m.isRefreshNow()){
                update(m);
                m.setRefreshNow(false);
            }
        }
       notifySingleObservers(modifiedScreens);
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





    @Override
    public void notifyManyObservers(List<Screen> added, List<Screen> deleted, List<Screen> modified) {
        for (ObserverMany observerMany : observersMany) {
            observerMany.update(added, deleted, modified);
        }
    }



    @Override
    public void notifySingleObservers(List<Screen> modified) {

        forgetAbout.addAll(modified);

       if(!modified.isEmpty()) {
           for (ObserverSingle observerSingle : observersSingle) {
               System.out.println("I love sushii");
               observerSingle.update();
           }
       }

        for(ObserverSingle observerSingle: observersSingle){
            System.out.println("ids for observers: "+ observerSingle.
                    getScreen().getId());
        }
        System.out.println("slut --------------------");

        for(Screen screen: modified){
            System.out.println(" ids for screns: "+ screen.getId());
        }

        for(ObserverSingle observerSingle : observersSingle){
            System.out.println("size of modified: "+ modified.size());
            for(Screen mofidScreen: modified){
                if(observerSingle.getScreen().getId() == mofidScreen.getId()) {
                    System.out.println("fuck yeahhhh");
                }}}
       /*for (Screen screen : modified){
           for (ObserverSingle observerSingle : observersSingle){
                if (screen.getId() == observerSingle.getScreen().getId()) {

                    observerSingle.update();
                }
            }
        }

       /*
       for (Screen screen : modified) {
           for (ObserverSingle observerSingle : observersSingle) {
               if (screen.getId() == observerSingle.getScreen().getId()) {
                   observerSingle.update();
                   System.out.println("we hit there");
               }
           }
       }

        */


        /*
        for(ObserverSingle observerSingle: observersSingle) {
            System.out.println("id: " + observerSingle.getScreen().getId());
        }

        System.out.println("end.");

        for (Screen screen : modified) {
            System.out.println("id mod:" + screen.getId());
        }

        for (Screen screen : modified){
            for (ObserverSingle observerSingle : observersSingle){
                if (screen.getId() == observerSingle.getScreen().getId()) {
                    observerSingle.update();
                    System.out.println("we hit there");
                }
            }
        }

         */

    }


    public void deletePuzzleScreen(Screen screen) {
        try {
            logic.deletePuzzleScreen(screen);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }


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

    public void update(Screen screen) {
        try {
            logic.update(screen);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public List<ScreenElement> getSections(Screen screen){
        try {
           return logic.getSections(screen);
        }catch (BLLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getUsersForScreen(int id) {
        try {
            return logic.getUsersForScreen(id);
        }catch (BLLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void saveToUsersAndScreens(int screenID, int userID) {
        try {
            logic.saveToUsersAndScreens(screenID,userID);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public int getScreenIDByName(String screenName){
        try {
            return logic.getScreenIDByName(screenName);
        }catch (BLLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public void updateSections(List<ScreenElement> sections) {
        try {
            logic.updateSections(sections);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void updateAssignedUsers(int screenID, List<User> selectedUsers) {
        try {
            logic.updateAssignedUsers(screenID, selectedUsers);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }
}
