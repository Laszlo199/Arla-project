package gui.util.Command;

import gui.Controller.ScreensViewController;
import gui.util.Animations;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

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
        //loadWindow(borderPane);
        loadW(borderPane);
    }
    private void loadW(BorderPane borderPane) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/"+ chosenPath +".fxml"));
        try{
            //later it may be other pane
            Pane view = (Pane) loader.load();
            ScreensViewController screensViewController = loader.getController();
            screensViewController.attachToObservers();
            Animations.fadeInTransition(view, 650);
            borderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CommandManager.getInstance().addCommand(this);
    }
}
