package gui.Controller;


import be.Screen;
import gui.Model.LoginModel;
import gui.util.AlertDisplayer;
import gui.util.Command.CommandManager;
import be.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dal.Database.dataAccess.UserDAO;
import dal.exception.DALexception;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 */
public class LogInController implements Initializable {
    private BorderPane borderPane;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXComboBox<String> screensComboBox;
    List<Integer> screens = new ArrayList<>();
    LoginModel model = new LoginModel();
    User user = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        screensComboBox.setVisible(false);
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void confirm() throws DALexception {
        user = model.getUser(usernameField.getText());

        if (user !=null){
            if (user.isReset()){
                String newPassword = passwordField.getText();
                model.updatePassword(user,newPassword);
            }
            else
            {
                if (user.getPassword().equals(passwordField.getText())){
                    if (user.isAdmin()){
                        CommandManager.getInstance().getPrevious().rollback(borderPane);
                    }
                    else {
                        //openClient(user);
                        selectScreen(user);
                    }
                }
                else
                {
                    AlertDisplayer.displayInformationAlert("Wrong password",
                            "Please insert  correct password password ", "");
                }
            }
        }
        else
        {
            AlertDisplayer.displayInformationAlert("Wrong username",
                    "Please insert  correct username ", "");
        }

        /*
       //System.out.println(user.getUserName() + user.getPassword());
        if(user ==null)
            JOptionPane.showMessageDialog(null,"Wrong username");
        else if(!user.getPassword().equals(passwordField.getText()))
            JOptionPane.showMessageDialog(null,"Wrong Password");
        else if(user.isAdmin())
            CommandManager.getInstance().getPrevious().rollback(borderPane);
        else
            openClient(user);

         */
    }

    private void selectScreen(User user) throws DALexception {
        screensComboBox.getItems().setAll((String) null);
        screens = model.screensOfUser(user.getID());

        if(screens.size()>0){
            for (int i = 0; i < screens.size(); i++){
                screensComboBox.getItems().add(model.getScreenByID(screens.get(i)).getName());
            }

            screensComboBox.setVisible(true);
        }
        else
        {
            //openClient(user,user.getScreens().get(0));
        }
    }

    public void loginWithComboBox(ActionEvent actionEvent) {
       if (screensComboBox.getSelectionModel().getSelectedItem() != null){
           Screen screen = model.getScreenByID( screens.get(screensComboBox.getSelectionModel().getSelectedIndex()-1) );
           openClient(screen);
       }
    }

    private void openClient(Screen screen) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = loader.load();
            ClientViewController controller = loader.getController();
            controller.setAsObserver(screen);
            controller.setScreen(screen, stage);

            controller.setAsObserver(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(screen.getName() + ", " + user.getUserName());
    }

    /* @Override
    public boolean passwordIsCorrect() {
        return true;
    }

    */

    /**
     * when button is invoked
     * @param
     */
    /*
    public void confirm() {
        String pw = passwordField.getText();
        LoginModel loginModel = new LoginModel();
        boolean flag = loginModel.validate(pw);
        if(!flag)
            JOptionPane.showMessageDialog(null,"Wrong Password");
        else
            CommandManager.getInstance().getPrevious().rollback(borderPane);

    }

     */
}

