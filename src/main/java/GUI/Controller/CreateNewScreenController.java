package GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 *
 */
public class CreateNewScreenController implements Initializable {
    @FXML private JFXTextField nameField;
    @FXML private Label csvL;
    @FXML private Label pdfL;
    @FXML private Label pngL;
    @FXML private Label jpgL;
    @FXML private Label httpL;
    @FXML private JFXButton setButton;
    @FXML private TextField rowsFiled;
    @FXML private TextField colsField;
    @FXML private AnchorPane space;
    private GridPane gridPane;
    ContextMenu contextMenu = new ContextMenu();

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
            gridPane = new GridPane();
            gridPane.setGridLinesVisible(true);
            gridPane.prefHeightProperty().bind(space.heightProperty());
            gridPane.prefWidthProperty().bind(space.widthProperty());
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    gridPane.add(new AnchorPane(), j, i);
                }
            }
            for (int i = 0; i < rows; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / rows);
                gridPane.getRowConstraints().add(rowConst);

            }
            for (int i = 0; i < cols; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / cols);
                gridPane.getColumnConstraints().add(colConst);

            }
            for(Node node: gridPane.getChildren()){
                System.out.println("setting constraint for node");
                node.setOnMousePressed(event -> showContextMenu(event, node));
            }
            space.getChildren().add(gridPane);
            colsField.setDisable(true);
            rowsFiled.setDisable(true);
            setButton.setDisable(true);
        }
    }

    /**
     * we will provide functionality to connect two or more available spots.
     * We need to ensure that grid is set first before checking that stuff
     * @param event
     * @param node
     */
    private void showContextMenu(MouseEvent event, Node node) {
        contextMenu.getItems().clear();

        if (event.isSecondaryButtonDown()) {
           //down
            if(GridPane.getRowIndex(node) !=gridPane.getRowCount()-1){
                List<MenuItem> menuItems = new ArrayList<>();
                int noToConnect = gridPane.getRowCount() - 1 - GridPane.getRowIndex(node);
                for(int i=1; i<=noToConnect; i++){
                    System.out.println("in the loooop...:"+ i);
                    MenuItem menuItem = new MenuItem("connect with: "+ i+" bottom");
                    int finalI = i;
                    menuItem.setOnAction(actionEvent -> {
                        int span =0;
                       if (GridPane.getRowSpan(node) == null) span = 1;
                       else span = GridPane.getRowSpan(node);
                        GridPane.setRowSpan(node, span + finalI);
                        node.setStyle("-fx-background-color: GREEN");
                    });
                    menuItems.add(menuItem);
                }
                contextMenu.getItems().addAll(menuItems);
                contextMenu.show(node, event.getScreenX(), event.getScreenY());

            }
            //up
            if(GridPane.getRowIndex(node)!=0){
                Map<Integer, MenuItem> menuItems = new HashMap<>();
                for(int i=1; i<=GridPane.getRowIndex(node); i++){
                    MenuItem menuItem = new MenuItem("connect with: " + i + " top");
                    int finalI = i;
                    menuItem.setOnAction(actionEvent -> {
                        int goalRow = GridPane.getRowIndex(node) - finalI;
                        int goalCol = GridPane.getColumnIndex(node);
                        //int span = GridPane.getRowSpan(node) +finalI;
                        int span = 0;
                        if(GridPane.getRowSpan(node)==null) span =1 + finalI;
                        else span = GridPane.getRowSpan(node) + finalI;
                        GridPane.setRowIndex(node, goalRow);
                        GridPane.setRowSpan(node, span);
                        node.setStyle("-fx-background-color: orange");
                    });
                    menuItems.put(i, menuItem );
                }
                contextMenu.getItems().addAll(menuItems.values().
                        stream().collect(Collectors.toList()));
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }


        }

    }



    public void saveButton(ActionEvent actionEvent) {

    }
}
