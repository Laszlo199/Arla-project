package GUI.Controller;

import GUI.Model.UserModel;
import be.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AssignUsersViewController implements Initializable {
    @FXML private Button add;
    @FXML private Button cancel;
    @FXML private TableView<Users> tableview;

    @FXML private TableColumn<Users, String> usersNames;


    private UserModel userModel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userModel = new UserModel();
        initUserTableView();


    }

    private void initUserTableView(){
        usersNames.setCellValueFactory(new PropertyValueFactory<Users, String>("userName"));
        userModel.loadUsers();
        tableview.setItems(userModel.getAllUser());

    }

    public void btnAdd(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }


    public void btnCancel(ActionEvent actionEvent) {

        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

}
