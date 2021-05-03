package GUI.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *   controller for the screen preview that will be shown in the screens
 */
public class ScreenPreview {
    @FXML
    private Label ScreenName;
    @FXML
    private Label refreshTime;
    @FXML
    private Label attachment1;
    @FXML
    private Label attachment2;
    @FXML
    private Label attachment3;
    @FXML
    private Label attachment4;

    public void refreshNow(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }

    public void edit(ActionEvent actionEvent) {
    }

    public void openPreview(ActionEvent actionEvent) {
    }

    public Label getScreenName() {
        return ScreenName;
    }

    public Label getRefreshTime() {
        return refreshTime;
    }

    public Label getAttachment1() {
        return attachment1;
    }

    public Label getAttachment2() {
        return attachment2;
    }

    public Label getAttachment3() {
        return attachment3;
    }

    public Label getAttachment4() {
        return attachment4;
    }
}
