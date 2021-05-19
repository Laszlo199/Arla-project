package gui.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class TemplatesViewController implements Initializable {
    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDefaultTemplate();
    }

    private void loadDefaultTemplate() {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/Templates/"+ "DefaultTemplate" +".fxml"));
        try{
            //later it may be other pane
            Pane template = (Pane) loader.load();
            vBox.getChildren().add(template);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
