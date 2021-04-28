package GUI.Controller;

import GUI.Command.CommandManager;
import GUI.Controller.Interfaces.ILogIn;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

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
        if(passwordIsCorrect())
            CommandManager.getInstance().getPrevious().rollback(borderPane);
    }
}
