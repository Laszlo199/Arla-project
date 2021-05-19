package gui.util.Command;

import javafx.scene.layout.BorderPane;

/**
 *
 */
public class LoadTemplates extends Command{
    @Override
    protected void setChosenPath() {
        chosenPath = FILE_PATH_TEMPLATES;
    }

    @Override
    public void load(BorderPane borderPane) {
        setChosenPath();
        loadWindow(borderPane);
    }

}
