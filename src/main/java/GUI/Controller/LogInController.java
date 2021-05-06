package GUI.Controller;


import GUI.Model.LoginModel;
import GUI.util.Command.CommandManager;
import GUI.Controller.Interfaces.ILogIn;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import javax.swing.*;

/**
 *
 */
public class LogInController implements ILogIn {
    private BorderPane borderPane;

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    @FXML
    private JFXPasswordField passwordField;

    @Override
    public boolean passwordIsCorrect() {
        return true;
    }

    /**
     * when button is invoked
     * @param
     */
    public void confirm() {
        String pw = passwordField.getText();
        LoginModel loginModel = new LoginModel();
        boolean flag = loginModel.validate(pw);
        if(!flag)
            JOptionPane.showMessageDialog(null,"Wrong Password");
        else
            CommandManager.getInstance().getPrevious().rollback(borderPane);

    }
}
