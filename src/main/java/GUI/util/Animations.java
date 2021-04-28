package GUI.util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 *
 */
public class Animations {
    public static void fadeInTransition(Node node, int timeInMillis){
        FadeTransition ft = new FadeTransition(Duration.millis(timeInMillis), node);
        ft.setFromValue(0.4);
        ft.setToValue(1.0);
        ft.play();
    }
}
