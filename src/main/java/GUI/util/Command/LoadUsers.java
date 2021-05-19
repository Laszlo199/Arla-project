package gui.util.Command;

import javafx.scene.layout.BorderPane;

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
