package GUI.util.Command;

import javafx.scene.layout.BorderPane;

/**
 *
 */
public class LoadScreens extends Command{
    @Override
    protected void setChosenPath() {
        chosenPath = FILE_PATH_SCREENS;
    }

    @Override
    public void load(BorderPane borderPane) {
        setChosenPath();
        loadWindow(borderPane);
    }
}
