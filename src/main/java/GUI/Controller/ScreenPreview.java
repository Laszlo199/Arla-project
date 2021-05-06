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

    private DefaultScreen ds;
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
    }

    public void refreshNow(ActionEvent actionEvent) {
    }

    /*
    method deletes the screen
     */
    public void delete(ActionEvent actionEvent) {

        ScreenModel.getInstance().deleteDefaultScreen(ds);

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


    public void setDefaultScreen(DefaultScreen defaultScreen) {
        this.ds = defaultScreen;
        initFields();
    }
    public Label getScreenNameLbl() {
        return ScreenName;
    }

    private void initFields() {
        id = ds.getId();
        String csv = ds.getDestinationPathCSV().toString();
        String pdf = ds.getDestinationPathPDF().toString();
        attachment1.setText(csv.substring(csv.lastIndexOf("/")+1));
        attachment2.setText(pdf.substring(csv.lastIndexOf("/")+1));
        attachment3.setText(ds.getInsertedWebsite());
        ScreenName.setText(ds.getName());
    }



}
