package gui.util.Command;

import com.jfoenix.controls.JFXButton;
import gui.Controller.ScreensViewController;
import gui.Controller.TemplateController;
import gui.util.Animations;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class LoadScreens extends Command{
    private JFXButton a;
    private JFXButton b;
    private JFXButton c;
    private JFXButton d;

    public LoadScreens() {

    }

    public LoadScreens(JFXButton screensB, JFXButton usersB, JFXButton createNewB, JFXButton logOutB) {
        a = screensB;
        b = usersB;
        c = createNewB;
        d = logOutB;
    }

    private void action(){
        a.setDisable(false);
        b.setDisable(false);
        c.setDisable(false);
        d.setDisable(false);
    }


    @Override
    protected void setChosenPath() {
        chosenPath = FILE_PATH_SCREENS;
    }

    @Override
    public void load(BorderPane borderPane) {
        action();
        setChosenPath();
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
