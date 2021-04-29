package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 *
 */
public class CreateDefaultTemplateController implements Initializable {
    @FXML
    private AnchorPane csvChart;
    @FXML
    private StackPane pdfSpace;
    @FXML
    private WebView webView;
    WebEngine engine;


    @Override
    public void initialize(URL url2, ResourceBundle resourceBundle) {

    }


    public void action(ActionEvent actionEvent) {

    }

    /**
     * save pane to all screens. we need to save which file corresponds to which corner
     * @param actionEvent
     */
    public void save(ActionEvent actionEvent) {
    }

    /**
     * not save anything.
     * Also delete files that have been storied over there
     * @param actionEvent
     */
    public void cancel(ActionEvent actionEvent) {
    }
}
