package gui.Controller;

import gui.Model.ScreenModel;
import gui.util.Observator.ObserverMany;
import be.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * class should listen for changes and if there are new screens
 * it should show them too!
 */
public class ScreensViewController extends ObserverMany implements Initializable {
    //private final static String HTML_DIRECTORY = "src/../Data/HTMLData/";
    private final static String PDF_DIRECTORY = "src/../Data/PDFData/";
    private final static String CVS_DIRECTORY = "src/../Data/CSVData/";
    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ScreenModel.getInstance().attachManyObserver(this);
        loadScreens(ScreenModel.getInstance().getMainScreens().stream().toArray(Screen[]::new));
    }


    private void loadScreens(Screen... screens) {
        for (Screen sc : screens) {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/ScreenPreview" + ".fxml"));
            try {
                AnchorPane screen = (AnchorPane) loader.load();
                ScreenPreview screenPreview = loader.getController();
                screenPreview.setMainScreen(sc);
                vBox.getChildren().add(screen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeElements(Screen... deleted) {
        for (Screen screen : deleted) {
            List<Node> nodes = vBox.getChildren();
            //we need to use iterator cause we will get CurrentModificationException
            Iterator<Node> it = nodes.iterator();
            while (it.hasNext()) {
                Node currectN = it.next();
                if (currectN != null) {
                    ScreenPreview screenPreview = (ScreenPreview) currectN.getUserData();
                    if (screenPreview.getCurrentScreen().equals(screen))
                        it.remove();
                    vBox.getChildren().remove(currectN);
                }
            }
        }
    }

    /**
     * just reload data in elements
     *
     * @param modified
     */
    private void modifyElements(Screen... modified) {
        for (Screen screen : modified) {
            List<Node> nodes = vBox.getChildren();
            for (Node node : nodes)
                if (node != null) {
                    ScreenPreview screenPreview = (ScreenPreview) node.getUserData();
                    if (screenPreview.getScreen().getId() == screen.getId()) {
                        screenPreview.setMainScreen(screen);
                        screenPreview.initMainFields();
                    }
                }
        }
    }

    @Override
    public void update(List<Screen> addedScreens, List<Screen> deletedScreens, List<Screen> modifiedScreens) {
        loadScreens(addedScreens.stream().toArray(Screen[]::new));
        removeElements(deletedScreens.stream().toArray(Screen[]::new));
        modifyElements(modifiedScreens.stream().toArray(Screen[]::new));
    }


    /**
     * It shows elements created from default template
     * @param defaultScreens
     */
    /*
    private void addElements(DefaultScreen... defaultScreens){
            for (DefaultScreen ds : defaultScreens) {
                FXMLLoader loader = new FXMLLoader(getClass().
                        getResource("/ScreenPreview" + ".fxml"));
                try {
                    AnchorPane screen = (AnchorPane) loader.load();
                    ScreenPreview screenPreview = loader.getController();
                    screenPreview.setCurrentScreen(ds);
                    vBox.getChildren().add(screen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }



    private List<DefaultScreen> getDefaultScreens(){
        try {
            return ScreenModel.getInstance().getAllDefaultScreens();
        } catch (ModelException e) {
            e.printStackTrace();
            AlertDisplayer.displayInformationAlert("couldnt load..",
                    "please restart the program we cant load the list", "");
        }
        return null;
    }


    private void modifyElements(DefaultScreen... modified) {
            for (DefaultScreen ds : modified) {
                System.out.println("we shouldnt get there now");
            }
    }

     */
    /**
     * now we work here
     * we may go through all nodes and check which contains our Default screen
     * @param
     */
    /*
    private void removeElements(DefaultScreen... deleted) {
            for (DefaultScreen defaultScreen : deleted) {
                System.out.println("we got to the method remove elements");
                List<Node> nodes = vBox.getChildren();
                //we need to use iterator cause we will get CurrentModificationException
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()){
                    Node currectN = it.next();
                    if(currectN!=null){
                    ScreenPreview screenPreview = (ScreenPreview) currectN.getUserData();
                    if(screenPreview.getCurrentScreen().equals(defaultScreen))
                        it.remove();
                        vBox.getChildren().remove(currectN);
                    }
                }
            }
    }

     */
    /**
     * get all screens from database, add them to the list,
     * loop through the list and load dynamically load to db
     */
    /*
    private void loadAllScreens(){
        ///List<DefaultScreen> defaultScreens = getDefaultScreens();
        // addElements(ScreenModel.getInstance().getDefaultScreens().stream().toArray(DefaultScreen[]::new));
        loadMainScreens(ScreenModel.getInstance().getMainScreens().stream().toArray(Screen[]::new));
    }

     */

}
