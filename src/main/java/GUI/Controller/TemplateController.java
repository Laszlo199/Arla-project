package GUI.Controller;
import GUI.util.LoadWindows;
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
    private LoadWindows loadWindows;

    public TemplateController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadWindows= new LoadWindows(borderPane);
    }

    public void loadScreens(ActionEvent actionEvent) {

    }

    public void loadUsers(ActionEvent actionEvent) {
        loadWindows.loadWindow("test");
    }

    public void loadTemplates(ActionEvent actionEvent) {
    }

    public void loadCreateNew(ActionEvent actionEvent) {
    }

    public void logOut(ActionEvent actionEvent) {
    }
}
