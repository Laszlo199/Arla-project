package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersInAdminViewController implements Initializable {
    @FXML private TableView<?> UserTableView;
    @FXML private TextField searchField;
    @FXML private TableColumn<?, ?> UserColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void btnAddUser(ActionEvent actionEvent) {
    }

    public void btnEditUser(ActionEvent actionEvent) {
    }

    public void btnDeleteUser(ActionEvent actionEvent) {
    }
}
