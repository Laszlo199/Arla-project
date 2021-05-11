package GUI.Controller;


import GUI.Model.LoginModel;
import GUI.util.Command.CommandManager;
import GUI.Controller.Interfaces.ILogIn;
import be.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

/**
 *
 */
public class LogInController implements ILogIn {
    private BorderPane borderPane;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @Override
    public boolean passwordIsCorrect() {
        return true;
    }

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

    public void confirm() {
        LoginModel model = new LoginModel();
        User user = model.getUser(usernameField.getText());
        System.out.println(user.getUserName() + user.getPassword());
        if(user==null)
            JOptionPane.showMessageDialog(null,"Wrong username");
        else if(!user.getPassword().equals(passwordField.getText()))
            JOptionPane.showMessageDialog(null,"Wrong Password");
        else if(user.isAdmin())
            CommandManager.getInstance().getPrevious().rollback(borderPane);
        else
            openClient(user);
    }

    private void openClient(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = loader.load();
            ClientViewController controller = loader.getController();
            controller.setUser(user, stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(user.getUserName());
    }
}
