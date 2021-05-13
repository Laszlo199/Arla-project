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
    @FXML
    private JFXTextField nameField;
    @FXML
    private Label csvL;
    @FXML
    private Label pdfL;
    @FXML
    private Label pngL;
    @FXML
    private Label jpgL;
    @FXML
    private Label httpL;
    @FXML
    private JFXButton setButton;
    @FXML
    private TextField rowsFiled;
    @FXML
    private TextField colsField;
    @FXML
    private AnchorPane space;
    private GridPane gridPane;
    ContextMenu contextMenu = new ContextMenu();
    private int[][] array;
    private int incrementedValue =1;

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
     *
     * @param actionEvent
     */
    public void setGrid(ActionEvent actionEvent) {
        if (!colsField.getText().isEmpty() && !rowsFiled.getText().isEmpty()) {
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
            for (Node node : gridPane.getChildren()) {
                node.setOnMousePressed(event -> showContextMenu(event, node));
            }
            //set array
            array = new int[rows][cols];
            space.getChildren().add(gridPane);
            colsField.setDisable(true);
            rowsFiled.setDisable(true);
            setButton.setDisable(true);
        }
    }

    /**
     * we will provide functionality to connect two or more available spots.
     * We need to ensure that grid is set first before checking that stuff
     *
     * @param event
     * @param node
     */
    private void showContextMenu(MouseEvent event, Node node) {
        contextMenu.getItems().clear();

        if (event.isSecondaryButtonDown()) {
            //down
            if (GridPane.getRowIndex(node) != gridPane.getRowCount() - 1) {
                List<MenuItem> menuItems = new ArrayList<>();
                int noToConnect = gridPane.getRowCount() - 1 - GridPane.getRowIndex(node);
                for (int i = 1; i <= noToConnect; i++) {
                    //check if it is not occupied all along the way
                    boolean check= true;
                    int spann = 0;
                    if(GridPane.getRowSpan(node) ==null) spann =1;
                    else  spann = GridPane.getRowSpan(node);

                    for(int k = GridPane.getRowIndex(node); k<= GridPane.getRowIndex(node)+i; k++) {
                        System.out.println("row index: "+ k);
                        if (array[k][GridPane.getColumnIndex(node)] != 0 && array[k][GridPane.getColumnIndex(node)]
                                !=array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)])
                            check = false;
                    }

                    MenuItem menuItem = new MenuItem("connect with: " + i + " bottom");
                    int finalI = i;
                    menuItem.setOnAction(actionEvent -> {
                        int span = 0;
                        if (GridPane.getRowSpan(node) == null) span = 1;
                        else span = GridPane.getRowSpan(node);
                        GridPane.setRowSpan(node, span + finalI);
                        //that numeration
                        int columnIndex = GridPane.getColumnIndex(node);
                        for(int j= GridPane.getRowIndex(node); j< GridPane.getRowIndex(node) +
                                span + finalI; j++){
                            System.out.println("we marked row: " + j +"with "+ incrementedValue);
                            array[j][columnIndex] = incrementedValue;
                        }

                        incrementedValue++;
                        node.setStyle("-fx-background-color: GREEN");
                    });
                    if(check){
                    menuItems.add(menuItem);
                }}
                contextMenu.getItems().addAll(menuItems);
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }
            }
            //up
            if (GridPane.getRowIndex(node) != 0) {
                Map<Integer, MenuItem> menuItems = new HashMap<>();
                for (int i = 1; i <= GridPane.getRowIndex(node); i++) {
                    MenuItem menuItem = new MenuItem("connect with: " + i + " top");
                    int finalI = i;
                    menuItem.setOnAction(actionEvent -> {
                        int goalRow = GridPane.getRowIndex(node) - finalI;
                        int span = 0;
                        if (GridPane.getRowSpan(node) == null) span = 1 + finalI;
                        else span = GridPane.getRowSpan(node) + finalI;
                        GridPane.setRowIndex(node, goalRow);
                        System.out.println("Row index in question: " +GridPane.getRowIndex(node));
                        GridPane.setRowSpan(node, span);
                        node.setStyle("-fx-background-color: orange");

                        //reevaluate that marking part

                        //go down it will appear only in some cases
                        for(int j = GridPane.getRowIndex(node); j<GridPane.getRowIndex(node) +
                        GridPane.getRowSpan(node); j++){
                            System.out.println("marking up idex: "+j);
                            array[j][GridPane.getColumnIndex(node)] = incrementedValue;
                        }

                        //go up
                        /*
                        for(int j= GridPane.getRowIndex(node) - finalI; j< GridPane.getRowIndex(node); j++) {
                            System.out.println("marking up idex (here??): "+j);
                            array[j][GridPane.getColumnIndex(node)] = incrementedValue;
                        }

                         */

                        incrementedValue++;
                    });
                    menuItems.put(i, menuItem);
                }
                contextMenu.getItems().addAll(menuItems.values().
                        stream().collect(Collectors.toList()));
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }

            //right
            if(GridPane.getColumnIndex(node) != gridPane.getColumnCount()-1){
                List<MenuItem> menuItems = new ArrayList<>();
                int noToConnect = gridPane.getColumnCount() - 1 - GridPane.getColumnIndex(node);
                for (int i = 1; i <= noToConnect; i++) {
                    //check if it is not occupied all along the way
                    boolean check= true;
                    int spann = 0;
                    if(GridPane.getColumnSpan(node) ==null) spann =1;
                    else  spann = GridPane.getColumnSpan(node);
                    for(int k = GridPane.getColumnIndex(node); k<= GridPane.getColumnIndex(node)+i; k++) {
                        System.out.println("col index: "+ k);
                        if (array[GridPane.getRowIndex(node)][k] != 0)
                            check = false;
                    }

                    MenuItem menuItem = new MenuItem("connect with: " + i + " right");
                    int finalI = i;
                    menuItem.setOnAction(actionEvent -> {
                        int span = 0;
                        if (GridPane.getColumnSpan(node) == null) span = 1;
                        else span = GridPane.getColumnSpan(node);
                        GridPane.setColumnSpan(node, span + finalI);
                        System.out.println("column starts here: ..."+ GridPane.getColumnIndex(node));
                        //that numeration
                        int rowIndex = GridPane.getRowIndex(node);
                        for(int j= GridPane.getColumnIndex(node); j< GridPane.getColumnIndex(node) +
                                span + finalI; j++){
                            System.out.println("we marked col: " + j +"with "+ incrementedValue);
                            array[rowIndex][j] = incrementedValue;
                        }

                        incrementedValue++;
                        node.setStyle("-fx-background-color: #641d97");
                    });
                    if(check) {
                        menuItems.add(menuItem);
                    }

                }
                contextMenu.getItems().addAll(menuItems);
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }


        }




    public void saveButton(ActionEvent actionEvent) {

    }
}
