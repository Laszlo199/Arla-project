package gui.util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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

    /**
     * animation moves node only horizontally
     * it can be used for example if the access was denied
     * and in one of the fields there was provided wrong information
     * @param node
     */
    public static void shakeNodeAnimation(Node node) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(50), node);
        translateTransition.setFromX(0f);
        translateTransition.setByX(10f);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);
        translateTransition.playFromStart();
    }
}
