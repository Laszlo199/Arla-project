package gui.util;

import javafx.scene.control.Alert;

/**
 *
 */
public class AlertDisplayer {

    /**
     * Alert suggests that the program is missing some data
     * data needs to be inserted by the user
     */
    public static void displayInformationAlert(String title, String information, String header)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.setContentText(information);
        alert.show();
    }
}
