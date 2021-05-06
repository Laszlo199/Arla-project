package GUI.Controller;

import GUI.Model.ScreenModel;
import be.DefaultScreen;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *   controller for the screen preview that will be shown in the screens
 */
public class ScreenPreview {
    private DefaultScreen ds;
    private int id;
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

    /*
    method deletes the screen
     */
    public void delete(ActionEvent actionEvent) {
        ScreenModel.getInstance().deleteDefaultScreen(ds);
    }

    public void edit(ActionEvent actionEvent) {
    }

    public void openPreview(ActionEvent actionEvent) {
    }

    public void setDefaultScreen(DefaultScreen defaultScreen) {
        this.ds = defaultScreen;
        initFields();
    }

    private void initFields() {
        id = ds.getId();
        String csv = ds.getDestinationPathCSV().toString();
        String pdf = ds.getDestinationPathPDF().toString();
        attachment1.setText(csv.substring(csv.lastIndexOf("/")+1));
        attachment2.setText(pdf.substring(csv.lastIndexOf("/")+1));
        attachment3.setText(ds.getInsertedWebsite());
        ScreenName.setText(ds.getName());
    }



}
