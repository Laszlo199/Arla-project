package gui.Controller;

import gui.Model.ScreenModel;
import gui.util.Observator.ObserverMany;
import be.Screen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * class should listen for changes and if there are new screens
 * it should show them too!
 */
public class ScreensViewController extends ObserverMany implements Initializable {
    private final static String PDF_DIRECTORY = "src/../Data/PDFData/";
    private final static String CVS_DIRECTORY = "src/../Data/CSVData/";
    private static HashMap<Integer, Node> nodes = new HashMap<>();

    @FXML
    private TilePane space;


    @Override
    public synchronized void initialize(URL url, ResourceBundle resourceBundle) {
      loadScreens(ScreenModel.getInstance().getMainScreens());
    }

    public void attachToObservers(){
        ScreenModel.getInstance().attachManyObserver(this);
    }


    private void loadScreens(List<Screen> screens) {
        try {
       // System.out.println("we are in load screens");
        for (Screen sc : screens) {
                FXMLLoader loader = new FXMLLoader(getClass().
                        getResource("/ScreenPreview" + ".fxml"));
                Parent screen = loader.load();
                nodes.put(sc.getId(), screen);
                ScreenPreview screenPreview = loader.getController();
                screenPreview.setMainScreen(sc);
                space.getChildren().add(screen);
               // System.out.println("we got over here.pddd..");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  we need to use iterator cause we will get CurrentModificationException
     * @param deleted
     */
    private void removeElements(List<Screen> deleted) {
        for(Screen screen: deleted){
            if(nodes.get(screen.getId()) ==null){
                System.out.println("we dont have such node");
            }

            Node node = nodes.get(screen.getId());
            space.getChildren().remove(node);
            nodes.remove(screen.getId());
            //System.out.println("we get over here and nothing happens ....");
        }
    }



    /**
     * just reload data in elements
     *
     * @param modified
     */
    private void modifyElements(List<Screen> modified) {
        /*
        for (Screen screen : modified) {
            List<Node> nodes = space.getChildren();
            for (Node node : nodes)
                if (node != null) {
                    ScreenPreview screenPreview = (ScreenPreview) node.getUserData();
                    if (screenPreview.getScreen().getId() == screen.getId()) {
                        screenPreview.setMainScreen(screen);
                        screenPreview.initMainFields();
                    }
                }

        }

         */
       // System.out.println("we are over ....");
    }

    /**
     * //this method may be useful if we have more than one screens opened
     * @param addedScreens
     * @param deletedScreens
     * @param modifiedScreens
     */
    @Override
    public void update(List<Screen> addedScreens, List<Screen> deletedScreens, List<Screen> modifiedScreens) {
       // System.out.println("update in ScreensViewController");
        Platform.runLater(() -> removeElements(deletedScreens));
        Platform.runLater(() -> loadScreens(addedScreens));
        Platform.runLater(() -> modifyElements(modifiedScreens));
    }

}
