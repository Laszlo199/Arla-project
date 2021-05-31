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
import java.util.ArrayList;
import java.util.List;

/**
 *   controller for the screen preview that will be shown in the screens
 */
public class ScreenPreview {

    private ScreenModel model = ScreenModel.getInstance();
    private Screen screen;

    @FXML private Label usernamesLbl;
    @FXML
    private Label ScreenName;
    @FXML
    private Label refreshTime;
    /*
    @FXML
    private Label attachment1;
    @FXML
    private Label attachment2;

     */
    @FXML
    private Label attachment3;
    /*
    @FXML
    private Label attachment4;

     */

    public void setMainScreen(Screen sc) {
        this.screen = sc;
        initMainFields();
    }

    public void initMainFields() {
        ScreenName.setText(screen.getName());

        List<ScreenElement> elements = model.getSections(screen);

        String attachments = "Attachment 1: "+elements.get(0).getFilepath();
        int x = 2;
        for (int i = 1; i<elements.size();i++){
            if(elements.get(i).getFilepath()!=null) {
                attachments=attachments+"\n"+"Attachment "+(x)+": "+elements.get(i).getFilepath();
                x++;
            }
        }
        attachment3.setText(attachments);

        String users = usernamesLbl.getText();
        List<String> usernames = model.getUsersForScreen(screen.getId());
        if(!usernames.isEmpty()) for(String u : usernames) users += "\n"+u;
        else users += "\n-";
        usernamesLbl.setText(users);
        
    }

    public Screen getScreen() {
        return screen;
    }

    public Label getScreenNameLbl() {
        return ScreenName;
    }


    public void refreshNow(ActionEvent actionEvent) {
        screen.setRefreshNow(true);
        model.update(screen);
        screen.setRefreshNow(false); //it shouldn't change anything.
    }

    public void delete(ActionEvent actionEvent) {
        model.deletePuzzleScreen(screen);
    }

    /**
     * Open new FXML window in which we need to
     * @param actionEvent
     */
    public void edit(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditView.fxml"));
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = loader.load();
            EditViewController controller = loader.getController();
            controller.setScreen(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setTitle("modifying screen: " + screen.getName());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * leave preview for now
     * @param actionEvent
     */
    public void openPreview(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = loader.load();
            ClientViewController controller = loader.getController();
            controller.setScreen(screen, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(screen.getName());
    }



}
