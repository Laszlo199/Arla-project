package gui.util.Command;

import gui.Controller.LogInController;
import gui.util.Animations;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 *
 */
public class LoadLogIn extends Command{
    @Override
    protected void setChosenPath() {
        chosenPath = FILE_PATH_LOG_IN;
    }

    @Override
    protected void loadWindow(BorderPane borderPane) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/"+ chosenPath +".fxml"));

        try{
            //later it may be other pane
            Pane view = (Pane) loader.load();
            LogInController logInController = loader.getController();
            logInController.setBorderPane(borderPane);
            Animations.fadeInTransition(view, 650);
            borderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CommandManager.getInstance().addCommand(this);
    }

    @Override
    public void load(BorderPane borderPane) {
        setChosenPath();
        loadWindow(borderPane);
    }
}
