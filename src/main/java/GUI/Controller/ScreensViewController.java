package gui.Controller;

import gui.Model.ScreenModel;
import gui.util.Observator.ObserverMany;
import be.Screen;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * class should listen for changes and if there are new screens
 * it should show them too!
 */
public class ScreensViewController extends ObserverMany implements Initializable {
    private final HashMap<Integer, Node> nodes = new HashMap<>(); // then try with static

    private List<Screen> allScreens = new ArrayList<>();
    private List<Screen> activeScreens = new ArrayList<>();

    @FXML private ToggleButton activeBtn;
    @FXML private ToggleButton allBtn;

    @FXML
    private TilePane space;


    @Override
    public synchronized void initialize(URL url, ResourceBundle resourceBundle) {
        allScreens = ScreenModel.getInstance().getMainScreens();
        loadScreens(allScreens);
        for(Screen screen : allScreens) {
            if(!ScreenModel.getInstance().getUsersForScreen(screen.getId()).isEmpty()) activeScreens.add(screen);
        }
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(activeBtn, allBtn);
        activeBtn.setOnAction(event -> showActive(event));
        allBtn.setOnAction(event -> showAll(event));

    }

    private void showAll(ActionEvent event) {
        space.getChildren().clear();
        loadScreens(allScreens);
    }

    private void showActive(ActionEvent event) {
        space.getChildren().clear();
        loadScreens(activeScreens);
    }

    public void attachToObservers(){
        ScreenModel.getInstance().attachManyObserver(this);
    }


    private void loadScreens(List<Screen> screens) {
        try {
        for (Screen sc : screens) {
                FXMLLoader loader = new FXMLLoader(getClass().
                        getResource("/ScreenPreview" + ".fxml"));
                Parent screen = loader.load();

                    int screenID = ScreenModel.getInstance().getScreenIDByName(sc.getName());
                    nodes.put(screenID, screen);

                ScreenPreview screenPreview = loader.getController();
                screenPreview.setMainScreen(sc);
                space.getChildren().add(screen);
                System.out.println("we got over here.pddd..");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * just reload data in elements
     *
     * @param modified
     */
    private void modifyElements(List<Screen> modified) {
        for(Screen screen: modified){
            Node node = nodes.get(screen.getId());
            ScreenPreview screenPreview = (ScreenPreview) node.getUserData();
            screenPreview.setMainScreen(screen);

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
            System.out.println("we get over here and nothing happens ....");
        }
    }


    /**
     * //this method may be useful if we have more than one screens opened
     * @param addedScreens
     * @param deletedScreens
     */
    @Override
    public void update(List<Screen> addedScreens, List<Screen> deletedScreens) {
        Platform.runLater(() -> removeElements(deletedScreens));
        Platform.runLater(() -> loadScreens(addedScreens));

    }

    @Override
    public void updateModified(List<Screen> modifiedScreens) {
        Platform.runLater(() -> {
            modifyElements(modifiedScreens);
        });
    }

}
