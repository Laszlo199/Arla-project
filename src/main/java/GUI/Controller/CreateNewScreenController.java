package GUI.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 *
 */
public class CreateNewScreenController implements Initializable {
    @FXML private JFXButton setButton;
    @FXML private TextField rowsFiled;
    @FXML private TextField colsField;
    @FXML private AnchorPane space;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeFieldsNumeric();
    }

    private void makeFieldsNumeric() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        rowsFiled.setTextFormatter(textFormatter1);
        colsField.setTextFormatter(textFormatter2);
    }

    /**
     * method takes value from two fields:
     * - rows
     * - cols
     * and then based on that builds grid and adds that to
     * AnchorPane called space
     * @param actionEvent
     */
    public void setGrid(ActionEvent actionEvent) {
        if(!colsField.getText().isEmpty() && !rowsFiled.getText().isEmpty()){
            int cols = Integer.parseInt(colsField.getText());
            int rows = Integer.parseInt(rowsFiled.getText());
            GridPane gridPane = new GridPane();
            gridPane.setGridLinesVisible(true);
            gridPane.prefHeightProperty().bind(space.heightProperty());
            gridPane.prefWidthProperty().bind(space.widthProperty());
            for (int i = 0; i < cols; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / cols);
                gridPane.getColumnConstraints().add(colConst);
            }
            for (int i = 0; i < rows; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / rows);
                gridPane.getRowConstraints().add(rowConst);
            }
            space.getChildren().add(gridPane);
            colsField.setDisable(true);
            rowsFiled.setDisable(true);
            setButton.setDisable(true);
        }
    }
}
