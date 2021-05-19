package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 *
 */
public class ChooseFile {
    public static File getSelectedFile(ActionEvent actionEvent, String information,
                                       FileChooser fileChooser){
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        fileChooser.setTitle(information);
        return fileChooser.showOpenDialog(stage);
    }

}
