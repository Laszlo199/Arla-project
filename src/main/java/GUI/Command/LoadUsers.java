package GUI.Command;

import GUI.util.Animations;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 *
 */
public class LoadUsers extends Command{
    @Override
    protected void setChosenPath() {
        chosenPath = FILE_PATH_USER;
    }

    @Override
    public void load(BorderPane borderPane) {
        setChosenPath();
        loadWindow(borderPane);
    }
}
