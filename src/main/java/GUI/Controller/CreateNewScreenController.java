package GUI.Controller;

import GUI.Model.UserModel;
import GUI.util.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 *
 */
public class CreateNewScreenController implements Initializable {
    @FXML
    private Label xlxsL;
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
    private GridPane gridPane= new GridPane();
    ContextMenu contextMenu = new ContextMenu();
    private int[][] array;
    private int incrementedValue = 1;

    Map<Node, String> nodeMap = new HashMap<>();
    private UserModel userModel;
    WebEngine webEngine = new WebEngine();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableLabels();
        this.userModel = new UserModel();
        setOnDrags();
        gridPane.setOnDragOver(event -> dragOver(event));
        gridPane.setOnDragDropped(event -> dragDropped(event));
        makeFieldsNumeric();
    }

    private void disableLabels() {
        jpgL.setDisable(true);
        pngL.setDisable(true);
        pdfL.setDisable(true);
        csvL.setDisable(true);
        httpL.setDisable(true);
        xlxsL.setDisable(true);
    }

    private void dragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    private void setOnDrags() {
        jpgL.setOnDragDetected(event -> dragStart(event, jpgL));
        pngL.setOnDragDetected(event -> dragStart(event, pngL));
        pdfL.setOnDragDetected(event -> dragStart(event, pdfL));
        csvL.setOnDragDetected(event -> dragStart(event, csvL));
        httpL.setOnDragDetected(event -> dragStart(event, httpL));
        xlxsL.setOnDragDetected(event -> dragStart(event, xlxsL));
    }

    private void dragStart(MouseEvent event, Label source) {
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        db.setDragView(source.snapshot(null, null));
        ClipboardContent cb = new ClipboardContent();
       // source.setCursor(Cursor.DEFAULT);
        cb.putString(source.getText());
        db.setContent(cb);
        event.consume();
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
            setupGrid();
            initGrid(rows, cols);
            setConstraints(rows, cols);
            setOnActions();
            array = new int[rows][cols];
            space.getChildren().add(gridPane);
            disableFields();
        }
    }

    private void setupGrid() {
        //gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.prefHeightProperty().bind(space.heightProperty());
        gridPane.prefWidthProperty().bind(space.widthProperty());
        enableLabels();
    }

    private void enableLabels() {
        jpgL.setDisable(false);
        pngL.setDisable(false);
        pdfL.setDisable(false);
        csvL.setDisable(false);
        httpL.setDisable(false);
        xlxsL.setDisable(false);
    }

    private void disableFields() {
        colsField.setDisable(true);
        rowsFiled.setDisable(true);
        setButton.setDisable(true);
    }

    private void setOnActions() {
        for (Node node : gridPane.getChildren()) {
            node.setOnMousePressed(event -> showContextMenu(event, node));
        }
    }

    private void initGrid(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gridPane.add(new AnchorPane(), j, i);
            }
        }
    }

    private void setConstraints(int rows, int cols) {
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
            checkDown(node, event);
            checkUp(node, event);
            checkRight(node, event);
            checkLeft(node, event);
        }
    }

    private void dragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node node = event.getPickResult().getIntersectedNode();
        //db.getDragView().cancel();
        if (db.hasString()) {
            switch(db.getString()){
                case "HTTP" -> loadHTTP(node);
                case "PNG" ->  loadImage(node, event);
                case "JPG" -> loadImage(node, event);
                case "PDF" ->  loadPDF(node);
                case "CSV" -> loadCSV(node);
                case "XLSX" -> loadExcel(node);
            }
        }

    }

    private void loadExcel(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XLSX file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (.xlsx)",
                "*.xlsx"));
        ExcelLoader.loadXLSX(fileChooser, anchorPane);
    }

    private void loadCSV(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (.csv)",
                "*.csv"));
        CSVLoader.loadCSV(fileChooser, anchorPane);

    }

    private void loadPDF(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (.pdf)",
                "*.pdf"));
        PDFLoader.loadPDF(fileChooser);
        PDFLoader.loadPDFViewer(anchorPane);
        nodeMap.put(node, PDFLoader.getDestinationPathPDF().toString());
    }

    private void loadImage(Node node,DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
        AnchorPane anchorPane = (AnchorPane) node;
        ImageLoader.loadImage(anchorPane);
        nodeMap.put(node, ImageLoader.getDestinationPath().toString());
    }

    /**
     * method is used within switch statement
     */
    private void loadHTTP(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        Label lbl = new Label("HTTP");
        JFXTextField field = new JFXTextField();
        Button btn = new Button("load");
        btn.setOnAction(actionEvent -> {
            String que = field.getText();
            nodeMap.put(node, que);
            loadWebsite(anchorPane, que);
        });
        loadNodes(anchorPane, lbl, field, btn);
    }

    private void loadWebsite(AnchorPane anchorPane, String query) {
       WebsiteLoader websiteLoader = new WebsiteLoader(webEngine);
       websiteLoader.addWebView(anchorPane);
       websiteLoader.executeQuery(query);
    }

    private void loadNodes(AnchorPane anchorPane, Node... nodes){
        VBox vbox = new VBox();
        vbox.getChildren().addAll(nodes);
        vbox.setPrefWidth(anchorPane.getWidth());
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        anchorPane.getChildren().addAll(vbox);
    }

    private void checkLeft(Node node, MouseEvent event) {
        if (GridPane.getColumnIndex(node) != 0) {
            List<MenuItem> menuItems = new ArrayList<>();
            for (int i = 1; i <= GridPane.getColumnIndex(node); i++) {
                boolean check = getLeftOccupiedCheck(node, i);
                MenuItem menuItem = new MenuItem("connect with: " + i + " left");
                setLeftMenuItemOnAction(node, i, menuItem);
                if (check) {
                    menuItems.add(menuItem);
                }
            }
            contextMenu.getItems().addAll(menuItems);
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        }

    }

    private void setLeftMenuItemOnAction(Node node, final int finalI, MenuItem menuItem) {
        menuItem.setOnAction(actionEvent -> {
            int goalCol = GridPane.getColumnIndex(node) - finalI;
            int span = 0;
            if (GridPane.getColumnSpan(node) == null) span = 1 + finalI;
            else span = GridPane.getColumnSpan(node) + finalI;
            GridPane.setColumnIndex(node, goalCol);
            GridPane.setColumnSpan(node, span);
            numerateLeft(node);
            node.setStyle("-fx-background-color: #e01c81");
        });
    }

    /**
     * Numeration will be needed when checking if field is occupied
     *
     * @param node
     */
    private void numerateLeft(Node node) {
        for (int j = GridPane.getColumnIndex(node); j < GridPane.getColumnIndex(node) +
                GridPane.getColumnSpan(node); j++) {
            array[GridPane.getRowIndex(node)][j] = incrementedValue;
        }
        incrementedValue++;
    }

    /**
     * if we return false it is occupied.
     * we check along the way if there is an item somewhere where we could add need one
     * we want to exclude that items
     *
     * @param node
     * @param i
     * @return
     */
    private boolean getLeftOccupiedCheck(Node node, int i) {
        for (int k = GridPane.getColumnIndex(node) - i; k <= GridPane.getColumnIndex(node); k++) {
            if (array[GridPane.getRowIndex(node)][k] != 0 && array[GridPane.getRowIndex(node)][k]
                    != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)])
                return false;
        }
        return true;
    }

    private void checkRight(Node node, MouseEvent event) {
        if (GridPane.getColumnIndex(node) != gridPane.getColumnCount() - 1) {
            List<MenuItem> menuItems = new ArrayList<>();
            int noToConnect = gridPane.getColumnCount() - 1 - GridPane.getColumnIndex(node);
            for (int i = 1; i <= noToConnect; i++) {
                boolean check = checkRightIsOccupied(node, i);
                MenuItem menuItem = new MenuItem("connect with: " + i + " right");
                setRightOnAction(node, i, menuItem);
                if (check) {
                    menuItems.add(menuItem);
                }
            }
            contextMenu.getItems().addAll(menuItems);
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        }
    }

    private void setRightOnAction(Node node, final int finalI, MenuItem menuItem) {
        menuItem.setOnAction(actionEvent -> {
            int span = 0;
            if (GridPane.getColumnSpan(node) == null) span = 1;
            else span = GridPane.getColumnSpan(node);
            GridPane.setColumnSpan(node, span + finalI);
            setRightNumeration(node, finalI, span);
            node.setStyle("-fx-background-color: #641d97");
        });
    }

    private void setRightNumeration(Node node, int finalI, int span) {
        int rowIndex = GridPane.getRowIndex(node);
        for (int j = GridPane.getColumnIndex(node); j < GridPane.getColumnIndex(node) +
                span + finalI; j++) {
            System.out.println("we marked col: " + j + "with " + incrementedValue);
            array[rowIndex][j] = incrementedValue;
        }
        incrementedValue++;
    }

    private boolean checkRightIsOccupied(Node node, int i) {
        for (int k = GridPane.getColumnIndex(node); k <= GridPane.getColumnIndex(node) + i; k++) {
            System.out.println("col index: " + k);
            if (array[GridPane.getRowIndex(node)][k] != 0)
                return false;
        }
        return true;
    }

    private void checkUp(Node node, MouseEvent event) {
        if (GridPane.getRowIndex(node) != 0) {
            Map<Integer, MenuItem> menuItems = new HashMap<>();
            for (int i = 1; i <= GridPane.getRowIndex(node); i++) {
                boolean check = checkUpOccupied(node, i);
                MenuItem menuItem = new MenuItem("connect with: " + i + " top");
                int finalI = i;
                menuItem.setOnAction(actionEvent -> {
                    int goalRow = GridPane.getRowIndex(node) - finalI;
                    int span = 0;
                    if (GridPane.getRowSpan(node) == null) span = 1 + finalI;
                    else span = GridPane.getRowSpan(node) + finalI;
                    GridPane.setRowIndex(node, goalRow);
                    GridPane.setRowSpan(node, span);
                    node.setStyle("-fx-background-color: orange");
                    setUpNumeration(node);
                });
                if (check)
                    menuItems.put(i, menuItem);
            }
            contextMenu.getItems().addAll(menuItems.values().
                    stream().collect(Collectors.toList()));
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        }
    }

    private void setUpNumeration(Node node) {
        for (int j = GridPane.getRowIndex(node); j < GridPane.getRowIndex(node) +
                GridPane.getRowSpan(node); j++) {
            System.out.println("marking up idex: " + j);
            array[j][GridPane.getColumnIndex(node)] = incrementedValue;
        }
        incrementedValue++;
    }

    private boolean checkUpOccupied(Node node, int i) {
        int columnSpan1 = 0;
        if (GridPane.getColumnSpan(node) == null) columnSpan1 = 1;
        else columnSpan1 = GridPane.getColumnSpan(node);

        for (int k = GridPane.getRowIndex(node) - i; k <= GridPane.getRowIndex(node); k++) {
            for (int l = GridPane.getColumnIndex(node); l <= GridPane.getColumnIndex(node) + columnSpan1 - 1; l++) {
                if (array[k][l] != 0 && array[k][l]
                        != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkDown(Node node, MouseEvent event) {
        if (GridPane.getRowIndex(node) != gridPane.getRowCount() - 1) {
            List<MenuItem> menuItems = new ArrayList<>();
            int noToConnect = gridPane.getRowCount() - 1 - GridPane.getRowIndex(node);
            for (int i = 1; i <= noToConnect; i++) {
                int columnSpan = getColSpan(node);
                boolean check = checkDownIsOccupied(node, i, columnSpan);
                MenuItem menuItem = new MenuItem("connect with: " + i + " bottom");
                setDownOnAction(node, i, columnSpan, menuItem);
                if (check)
                    menuItems.add(menuItem);
            }
            contextMenu.getItems().addAll(menuItems);
            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        }
    }

    private void setDownOnAction(Node node, final int finalI, final int finalColumnSpan, MenuItem menuItem) {
        menuItem.setOnAction(actionEvent -> {
            int span = getRowSpan(node);
            int columnIndex = GridPane.getColumnIndex(node);
            GridPane.setRowSpan(node, span + finalI);
            setDownNumeration(node, finalI, span, finalColumnSpan, columnIndex);
            node.setStyle("-fx-background-color: GREEN");
        });
    }

    private void setDownNumeration(Node node, int finalI, int span,
                                   int finalColumnSpan, int columnIndex) {
        for (int j = GridPane.getRowIndex(node); j < GridPane.getRowIndex(node) +
                span + finalI; j++) {
            for (int k = columnIndex; k <= columnIndex + finalColumnSpan - 1; k++) {
                System.out.println("marked row: " + j + " and column: " + k +
                        " with value " + incrementedValue);
                array[j][k] = incrementedValue;
            }
        }
        incrementedValue++;
    }

    private int getRowSpan(Node node) {
        int span = 0;
        if (GridPane.getRowSpan(node) == null) span = 1;
        else span = GridPane.getRowSpan(node);
        return span;
    }


    private boolean checkDownIsOccupied(Node node, int i, int columnSpan) {
        for (int k = GridPane.getRowIndex(node); k <= GridPane.getRowIndex(node) + i; k++) {
            for (int l = GridPane.getColumnIndex(node); l <= GridPane.getColumnIndex(node) + columnSpan - 1; l++) {
                if (array[k][l] != 0 && array[k][l] != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getColSpan(Node node) {
        int columnSpan1 = 0;
        if (GridPane.getColumnSpan(node) == null) columnSpan1 = 1;
        else columnSpan1 = GridPane.getColumnSpan(node);
        return columnSpan1;
    }


    public void saveButton(ActionEvent actionEvent) {

    }

    /**
     * check if any of the nodes is not filled in
     * We need to implement it after meeting with jeppe
     * @return
     */
    private boolean checkIfAnyEmpty() {
        return false;
    }
}
