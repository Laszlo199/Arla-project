package GUI.util.Command;

import javafx.scene.layout.BorderPane;

public class LoadCreateNew extends Command {
    @Override
    protected void setChosenPath() {
        chosenPath = FILE_PATH_CREATE_NEW;
    }

    @Override
    public void load(BorderPane borderPane) {
        setChosenPath();
        loadWindow(borderPane);
    }
}
