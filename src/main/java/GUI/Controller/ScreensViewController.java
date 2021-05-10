package GUI.Controller;

import GUI.Model.ScreenModel;
import GUI.Model.exception.ModelException;
import GUI.util.AlertDisplayer;
import GUI.util.Observator.IObserver;
import be.DefaultScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * class should listen for changes and if there are new screens
 * it should show them too!
 */
public class ScreensViewController implements Initializable, IObserver<DefaultScreen> {
    //private final static String HTML_DIRECTORY = "src/../Data/HTMLData/";
    private final static String PDF_DIRECTORY = "src/../Data/PDFData/";
    private final static String CVS_DIRECTORY = "src/../Data/CSVData/";
    @FXML private VBox vBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ScreenModel.getInstance().attachObserver(this);
        loadAllScreens();
    }

    /**
     * get all screens from database, add them to the list,
     * loop through the list and load dynamically load to db
     */
    private void loadAllScreens(){
        List<DefaultScreen> defaultScreens = getDefaultScreens();
        addElements(defaultScreens.stream().toArray(DefaultScreen[]::new));

    }

    /**
     * 
     * @param defaultScreens
     */
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

    @Override
    public void update(DefaultScreen added, DefaultScreen deleted, DefaultScreen modified) {
       if(added!=null) addElements(added);
       if(deleted!=null) removeElements(deleted);
       if(modified!=null) modifyElements(modified);
    }

    private void modifyElements(DefaultScreen... modified) {
            for (DefaultScreen ds : modified) {
                System.out.println("we shouldnt get there now");
            }
    }

    private void removeElements(DefaultScreen... deleted) {
            for (DefaultScreen defaultScreen : deleted) {
                System.out.println("we got to the method remove elements");
            }
    }

}
