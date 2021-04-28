package GUI.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 *
 */
public class LoadWindows {
    private BorderPane borderPane;

    public LoadWindows(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void loadWindow(String fxml_name){
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/"+ fxml_name +".fxml"));
        try{
            //later it may be other pane
            Pane view = (Pane) loader.load();
            Animations.fadeInTransition(view, 650);
            borderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
