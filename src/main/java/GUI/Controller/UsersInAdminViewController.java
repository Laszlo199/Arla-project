package GUI.Controller;

import GUI.Model.UserModel;
import be.Users;
import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersInAdminViewController implements Initializable {
    @FXML private TableView<Users> userTableView;
    @FXML private TextField searchField;
    @FXML private TableColumn<Users, String> userColumn;
    @FXML private TableColumn<Users, String> passwordColumn;
    @FXML private JFXButton edit;
    @FXML private AnchorPane editTable;
    @FXML private AnchorPane addNewUser;
    @FXML private JFXButton add;
    @FXML private TextField editNameField;
    @FXML private TextField editPasswordField;
    @FXML private TextField newNameField;
    @FXML private TextField newPasswordField;
    @FXML private Button editCancel;
    @FXML private Button newCancel;


    private UserModel userModel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userModel = new UserModel();
        editTable.setVisible(false);
        addNewUser.setVisible(false);
        initUserTableView();
    }

    private void initUserTableView(){
        userColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("userName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Users,String>("Password"));

        userModel.loadUsers();
        userTableView.setItems(userModel.getAllUser());

    }


    public void btnAddUser(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        show.setDuration(Duration.seconds(0.4));
        show.setNode(addNewUser);
        show.setToX(0);
        show.setToY(-100);
        show.play();

        addNewUser.setTranslateX(0);
        addNewUser.setVisible(true);
        editTable.setVisible(false);
        add.setDisable(true);
        edit.setDisable(false);

        edit.setOnMouseClicked(event ->{
            show.setNode(addNewUser);
            show.setToX(0);
            show.setToY(100);
            show.play();

            addNewUser.setTranslateX(0);
        });
    }

    public void btnEditUser(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        show.setDuration(Duration.seconds(0.4));
        show.setNode(editTable);
        show.setToX(0);
        show.setToY(100);
        show.play();

        editTable.setTranslateX(0);
       editTable.setVisible(true);
       addNewUser.setVisible(false);
       edit.setDisable(true);
        add.setDisable(false);

       add.setOnMouseClicked(event ->{
           show.setNode(editTable);
           show.setToX(0);
           show.setToY(-100);
           show.play();

           addNewUser.setTranslateX(0);
       });
    }

    public void btnCancelEdit(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        editCancel.setOnMouseClicked(event ->{
            show.setNode(editTable);
            show.setToX(0);
            show.setToY(0);
            show.play();

            editTable.setTranslateX(0);
        });


        editTable.setTranslateX(0);
        editTable.setVisible(false);


    }

    public void btnCancelAdd(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        newCancel.setOnMouseClicked(event ->{
            show.setNode(addNewUser);
            show.setToX(0);
            show.setToY(0);
            show.play();

            addNewUser.setTranslateX(0);
        });

        addNewUser.setTranslateX(0);
        addNewUser.setVisible(false);
    }

    public void btnUpdate(ActionEvent actionEvent) {
        Users newUser = userTableView.getSelectionModel().getSelectedItem();
        newUser.setUserName(editNameField.getText());
        newUser.setPassword(editPasswordField.getText());

        userModel.updateUser(userTableView.getSelectionModel().getSelectedItem(),newUser);

    }

    public void btnCreate(ActionEvent actionEvent) {
        Users newUser = new Users(-1,
                newNameField.getText(),
                newPasswordField.getText());
        userModel.saveUser(newUser);

    }

    public void btnDeleteUser(ActionEvent actionEvent) {
        Users selectedUser = userTableView.getSelectionModel().getSelectedItem();
        userModel.delete(selectedUser);
        userModel.loadUsers();

    }

    public void readUser(MouseEvent event) {
        editNameField.setText(userTableView.getSelectionModel().getSelectedItem().getUserName());
        editPasswordField.setText(userTableView.getSelectionModel().getSelectedItem().getPassword());

    }
}
