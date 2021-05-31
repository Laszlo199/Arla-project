package gui.Controller;


import be.Screen;
import com.jfoenix.validation.RequiredFieldValidator;
import gui.Model.LoginModel;
import gui.util.Animations;
import gui.util.Command.CommandManager;
import be.User;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
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
        checkIfEmpty(usernameField, passwordField);
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    Runnable runnable = () -> {
        user = model.getUser(usernameField.getText());

        if (user != null) {
            if (user.isReset()) {
                String newPassword = passwordField.getText();
                model.updatePassword(user, newPassword);
            } else {
                if (user.getPassword().equals(passwordField.getText())) {
                    if (user.isAdmin()) {
                        Platform.runLater(()->CommandManager.getInstance().getPrevious().rollback(borderPane));

                    } else {
                        Platform.runLater(()->selectScreen(user));
                    }
                } else {
                    Platform.runLater(()-> {
                        Animations.shakeNodeAnimation(passwordField);
                        Animations.shakeNodeAnimation(usernameField);
                    });
                }
            }
        } else {
            Platform.runLater(()-> {
                Animations.shakeNodeAnimation(usernameField);
            });
        }


    };

    public void confirm() {
      Thread thread = new Thread(runnable);
      thread.start();
    }

    /**
     * If one of the fields is empty user is notified
     * @param emailField
     * @param passwordField
     */
    private void checkIfEmpty(JFXTextField emailField, JFXPasswordField passwordField) {
        RequiredFieldValidator noInputVal = new RequiredFieldValidator();
        noInputVal.setMessage("Input Required");
        RequiredFieldValidator noInputVal1 = new RequiredFieldValidator();
        noInputVal1.setMessage("Input Required");
        emailField.focusedProperty().addListener((observableValue, aBoolean, newVal) -> {
            if (!newVal) {
                emailField.getValidators().add(noInputVal);
                emailField.validate();
            }
        });

        passwordField.focusedProperty().addListener((observableValue, aBoolean, newVal) -> {
            if (!newVal) {
                passwordField.getValidators().add(noInputVal1);
                passwordField.validate();
            }
        });
    }

    private void selectScreen(User user){
        screensComboBox.getItems().setAll((String) null);
        screens = model.screensOfUser(user.getID());

        if(screens.size()>0){
            for (int i = 0; i < screens.size(); i++){
                screensComboBox.getItems().add(model.getScreenByID(screens.get(i)).getName());
            }

            screensComboBox.setVisible(true);
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

            //controller.setAsObserver(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(screen.getName() + ", " + user.getUserName());
    }

}

