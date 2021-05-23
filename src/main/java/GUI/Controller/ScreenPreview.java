package gui.Controller;

import be.ScreenElement;
import gui.Model.ScreenModel;
import be.DefaultScreen;
import be.Screen;
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

    private int id;
    private ScreenModel model = ScreenModel.getInstance();
   // private DefaultScreen currentScreen;
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
    private Screen screen;

    public void setMainScreen(Screen sc) {
        this.screen = sc;
        initMainFields();
    }

    public void initMainFields() {
        ScreenName.setText(screen.getName());
        Label[] array =  {attachment1, attachment2, attachment3, attachment4};
        //for(ScreenElement se: screen.getScreenElementList())

       /* for (int i = 0; i < 3; i++) {
            if(screen.getScreenElementList().size()!=0) {
                String str = screen.getScreenElementList().get(i).getFilepath();
                array[i].setText(str.substring(str.lastIndexOf("/") + 1));
            }
        }

        */
    }

    public Screen getScreen() {
        return screen;
    }

    public Label getScreenNameLbl() {
        return ScreenName;
    }


    public void refreshNow(ActionEvent actionEvent) {
        //TO-DO
    }

    public void delete(ActionEvent actionEvent) {
        model.deletePuzzleScreen(screen);
    }

    /**
     *
     * @param actionEvent
     */
    public void edit(ActionEvent actionEvent) {

    }

    /**
     * leave preview for now
     * @param actionEvent
     */
    public void openPreview(ActionEvent actionEvent) {

    }

/*
    public void setCurrentScreen(DefaultScreen ds) {
        this.currentScreen = ds;
        initFieldsDefault();
    }

    public DefaultScreen getCurrentScreen() {
        return currentScreen;
    }
     */

    /*
    method deletes the screen
     */
    /*
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

     */

    /*
    private void initFieldsDefault() {
        id = currentScreen.getId();
        String csv = currentScreen.getDestinationPathCSV().toString();
        String pdf = currentScreen.getDestinationPathPDF().toString();
        attachment1.setText(csv.substring(csv.lastIndexOf("/")+1));
        attachment2.setText(pdf.substring(pdf.lastIndexOf("/")+1));
        attachment3.setText(currentScreen.getInsertedWebsite());
        ScreenName.setText(currentScreen.getName());
    }
     */

}
