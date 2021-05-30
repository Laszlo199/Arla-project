package gui.Controller;
import com.jfoenix.controls.JFXButton;
import gui.util.Command.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class is responsible for changing the scenes within the center
 * of border pane
 */
public class TemplateController implements Initializable {
    @FXML
    private JFXButton screensB;
    @FXML
    private JFXButton usersB;
    @FXML
    private JFXButton createNewB;
    @FXML
    private JFXButton logOutB;
    @FXML
    private BorderPane borderPane;

    public TemplateController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Command commandO = new LoadScreens(screensB, usersB, createNewB, logOutB);
        CommandManager.getInstance().addCommand(commandO);
        Command command = new LoadLogIn(screensB, usersB, createNewB, logOutB);
        command.load(borderPane);
    }

    public void loadScreens(ActionEvent actionEvent) {
        Command command = new LoadScreens();
        command.load(borderPane);
    }

    public void loadUsers(ActionEvent actionEvent) {
        Command command = new LoadUsers();
        command.load(borderPane);
    }


    public void loadCreateNew(ActionEvent actionEvent) {
        Command command = new LoadCreateNew();
        command.load(borderPane);
    }

    public void logOut(ActionEvent actionEvent) {
        Command command = new LoadLogIn(screensB, usersB, createNewB, logOutB);
        command.load(borderPane);
    }
}
