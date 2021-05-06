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
import javafx.stage.Stage;

import java.io.IOException;

/**
 *   controller for the screen preview that will be shown in the screens
 */
public class ScreenPreview {

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
    }

    public void refreshNow(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
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

    public Label getRefreshTime() {
        return refreshTime;
    }

    public Label getAttachment1() {
        return attachment1;
    }

    public Label getAttachment2() {
        return attachment2;
    }

    public Label getAttachment3() {
        return attachment3;
    }

    public Label getAttachment4() {
        return attachment4;
    }
}
