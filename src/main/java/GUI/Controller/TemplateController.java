package GUI.Controller;
import GUI.Command.*;
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
    private BorderPane borderPane;
    
    public TemplateController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CommandManager.getInstance().addCommand(new LoadScreens());
        Command command = new LoadLogIn();
        command.load(borderPane);
    }

    public void loadScreens(ActionEvent actionEvent) {

    }

    public void loadUsers(ActionEvent actionEvent) {
        Command command = new LoadUsers();
        command.load(borderPane);
    }

    public void loadTemplates(ActionEvent actionEvent) {
    }

    public void loadCreateNew(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) {
    }
}
