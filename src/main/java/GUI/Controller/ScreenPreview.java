package GUI.Controller;

import GUI.Model.ScreenModel;
import be.DefaultScreen;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *   controller for the screen preview that will be shown in the screens
 */
public class ScreenPreview {

    private int id;
    private ScreenModel model = ScreenModel.getInstance();
    private DefaultScreen currentScreen;
    @FXML
    private Label ScreenName;
    @FXML
    private Label refreshTime;
    @FXML
    private Label attachment1;
    @FXML
    private Label attachment2;
    @FXML
    private Label attachment3;
    @FXML
    private Label attachment4;

    public void setCurrentScreen(DefaultScreen ds) {
        this.currentScreen = ds;
        initFields();
    }

    public DefaultScreen getCurrentScreen() {
        return currentScreen;
    }

    public void refreshNow(ActionEvent actionEvent) {
    }

    /*
    method deletes the screen
     */
    public void delete(ActionEvent actionEvent) {
      //  ScreenModel.getInstance().deleteDefaultScreen(currentScreen);
        model.deleteScreen(currentScreen);
    }

    public void edit(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Templates/CreateDefaultTemplate.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            CreateDefaultTemplateController controller = loader.getController();
            controller.setEditMode(currentScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Production");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openPreview(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/preview.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            PreviewController controller = loader.getController();
            controller.setScreen(currentScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Preview");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public Label getScreenNameLbl() {
        return ScreenName;
    }

    private void initFields() {
        id = currentScreen.getId();
        String csv = currentScreen.getDestinationPathCSV().toString();
        String pdf = currentScreen.getDestinationPathPDF().toString();
        attachment1.setText(csv.substring(csv.lastIndexOf("/")+1));
        attachment2.setText(pdf.substring(csv.lastIndexOf("/")+1));
        attachment3.setText(currentScreen.getInsertedWebsite());
        ScreenName.setText(currentScreen.getName());
    }



}
