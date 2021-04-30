package GUI.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersInAdminViewController implements Initializable {
    @FXML private TableView<?> UserTableView;
    @FXML private TextField searchField;
    @FXML private TableColumn<?, ?> UserColumn;
    @FXML private JFXButton edit;
    @FXML private AnchorPane editTable;
    @FXML private AnchorPane addNewUser;
    @FXML private JFXButton add;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editTable.setVisible(false);
        addNewUser.setVisible(false);
    }


    public void btnAddUser(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        show.setDuration(Duration.seconds(0.4));
        show.setNode(addNewUser);
        show.setToX(0);
        show.setByY(-100);
        show.play();

        addNewUser.setTranslateX(0);
        addNewUser.setVisible(true);
        editTable.setVisible(false);
        add.setDisable(true);
        edit.setDisable(false);
        
        edit.setOnMouseClicked(event ->{
            show.setNode(addNewUser);
            show.setToX(0);
            show.setByY(100);
            show.play();

            addNewUser.setTranslateX(0);
        });
    }

    public void btnEditUser(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        show.setDuration(Duration.seconds(0.4));
        show.setNode(editTable);
        show.setToX(0);
        show.setByY(100);
        show.play();

        editTable.setTranslateX(0);
       editTable.setVisible(true);
       addNewUser.setVisible(false);
       edit.setDisable(true);
        add.setDisable(false);

       add.setOnMouseClicked(event ->{
           show.setNode(editTable);
           show.setToX(0);
           show.setByY(-100);
           show.play();

           addNewUser.setTranslateX(0);
       });
    }

    public void btnDeleteUser(ActionEvent actionEvent) {

    }


    public void btnCancleEdit(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        show.setNode(editTable);
        show.setToX(0);
        show.setByY(-100);
        show.play();
        editTable.setTranslateX(0);
        editTable.setVisible(false);


    }

    public void btnCancleAdd(ActionEvent actionEvent) {
        TranslateTransition show= new TranslateTransition();
        show.setNode(addNewUser);
        show.setToX(0);
        show.setByY(100);
        show.play();
        addNewUser.setTranslateX(0);
        addNewUser.setVisible(false);
    }
}
