package gui.Controller;

import be.CSVInfo;
import be.Screen;
import be.ScreenElement;
import gui.Model.ScreenModel;
import gui.Model.UserModel;
import gui.util.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import gui.util.Command.Command;
import gui.util.Command.CommandManager;
import gui.util.Command.LoadCreateNew;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 *
 */
public class CreateNewScreenController implements Initializable {
    @FXML
    private JFXButton resetButton;
    @FXML
    private Label videoL;
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
    private BorderPane borderPane;

    private GridPane gridPane;
    ContextMenu contextMenu;
    private int[][] array;
    private int incrementedValue = 1;
    private UserModel userModel;
    private ScreenModel screenModel;
    WebEngine webEngine;


    public CreateNewScreenController() {
        gridPane = new GridPane();
        contextMenu = new ContextMenu();
        webEngine = new WebEngine();
        this.userModel = new UserModel();
        this.screenModel = new ScreenModel();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLay();
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private void initLay() {
        disableLabels();
        setOnDrags();
        gridPane.setOnDragOver(event -> dragOver(event));
        gridPane.setOnDragDropped(event -> dragDropped(event));
        makeFieldsNumeric();
        CSVLoader.clearMap();
    }

    private void disableLabels() {
        jpgL.setDisable(true);
        pngL.setDisable(true);
        pdfL.setDisable(true);
        csvL.setDisable(true);
        httpL.setDisable(true);
        xlxsL.setDisable(true);
        videoL.setDisable(true);
        resetButton.setDisable(true);
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
        videoL.setOnDragDetected(event -> dragStart(event, videoL));
    }

    private void dragStart(MouseEvent event, Label source) {
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        db.setDragView(source.snapshot(null, null));
        ClipboardContent cb = new ClipboardContent();
        cb.putString(source.getText());
        db.setContent(cb);
        event.consume();
    }

    private UnaryOperator<TextFormatter.Change> getFilter1(){
        UnaryOperator<TextFormatter.Change> filter1 = change -> {
            String text = change.getText();
            int lengR = rowsFiled.getText().length();
            if(text.equals(""))
                return change;

            if (text.matches("[1-9]") && lengR<1 ) {
                return change;
            }
            return null;
        };
        return filter1;
    }

    private UnaryOperator<TextFormatter.Change> getFilter2(){
        UnaryOperator<TextFormatter.Change> filter2 = change -> {
            String text = change.getText();
            int lengC = colsField.getText().length();
            if(text.equals(""))
                return change;

            if (text.matches("[1-9]") && lengC<1 ) {
                return change;
            }
            return null;
        };
        return filter2;
    }


    private void makeFieldsNumeric() {
        TextFormatter<String> textFormatter1 = new TextFormatter<>(getFilter1());
        TextFormatter<String> textFormatter2 = new TextFormatter<>(getFilter2());
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
            resetButton.setDisable(false);
        }
    }

    private void setupGrid() {
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
        videoL.setDisable(false);
    }

    private void disableFields() {
        colsField.setDisable(true);
        rowsFiled.setDisable(true);
        setButton.setDisable(true);
    }

    private void setOnActions() {
        for (Node node : gridPane.getChildren()) {
            Information information = new Information();
            information.setNode(node);
            node.setUserData(information);
            node.setOnMousePressed(event -> showContextMenu(event, node));
        }
    }


    private void initGrid(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setMinSize(0, 0);
                gridPane.add(anchorPane, j, i);
            }
        }
    }

    private void setConstraints(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setVgrow(Priority.NEVER);
            rowConst.setPercentHeight(100.0 / rows);
            gridPane.getRowConstraints().add(rowConst);

        }
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.NEVER);
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
        Node node1 = event.getPickResult().getIntersectedNode();
        Node node = getNodeToUse(node1);
        if (db.hasString()) {
            switch (db.getString()) {
                case "HTTP" -> loadHTTP(node);
                case "PNG" -> loadImage(node, event);
                case "JPG" -> loadImage(node, event);
                case "PDF" -> loadPDF(node);
                case "CSV" -> loadCSV(node);
                case "XLSX" -> loadExcel(node);
                case "VIDEO" -> loadVideo(node);
            }
        }

    }


    private Node getNodeByCoordinate(Integer row, Integer column) {
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    /**
     * at first we need to select video then copy it
     * and run inside a program
     *
     * @param node
     */
    private void loadVideo(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video files",
                "*.mp4", "*.WEBM"));
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource("/" + "MoviePlayer" + ".fxml"));
        try {
            AnchorPane view = (AnchorPane) loader.load();
            MoviePlayerController controller = loader.getController();
            getInformation(node).setFilepath(controller.passFileChooser(fileChooser).toString());
            getInformation(node).setFilled(true);
            view.setPrefSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
            anchorPane.getChildren().add(view);
            view.prefHeightProperty().bind(anchorPane.heightProperty());
            view.prefWidthProperty().bind(anchorPane.widthProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExcel(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XLSX file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (.xlsx)",
                "*.xlsx"));
        String destPath = ExcelLoader.loadXLSX(fileChooser, anchorPane).toString();
        getInformation(node).setFilepath(destPath);
        getInformation(node).setFilled(true);
    }

    private void loadCSV(Node node) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (.csv)",
                "*.csv"));
        String destPath = CSVLoader.loadCSV(fileChooser, node).toString();
        getInformation(node).setFilepath(destPath);
        getInformation(node).setFilled(true);
    }



    private void loadPDF(Node node) {
        AnchorPane anchorPane = (AnchorPane) node;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (.pdf)",
                "*.pdf"));
        String destPath = PDFLoader.loadPDF(fileChooser).toString();
        getInformation(node).setFilepath(destPath);
        getInformation(node).setFilled(true);
        PDFLoader.loadPDFViewer(anchorPane);
    }

    private void loadImage(Node node, DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
        AnchorPane anchorPane = (AnchorPane) node;
        String destPath = ImageLoader.loadImage(anchorPane).toString();
        getInformation(node).setFilepath(destPath);
        getInformation(node).setFilled(true);
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
            getInformation(node).setFilepath(que);
            getInformation(node).setFilled(true);
            loadWebsite(anchorPane, que);
        });
        loadNodes(anchorPane, lbl, field, btn);
    }

    private void loadWebsite(AnchorPane anchorPane, String query) {
        WebsiteLoader websiteLoader = new WebsiteLoader(webEngine);
        websiteLoader.addWebView(anchorPane);
        websiteLoader.executeQuery(query);
    }

    private void loadNodes(AnchorPane anchorPane, Node... nodes) {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(nodes);
        vbox.setPrefWidth(anchorPane.getWidth());
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        anchorPane.getChildren().addAll(vbox);
    }

    private void checkLeft(Node node, MouseEvent event) {
        Node parentNode = getNodeToUse(node);
        if (GridPane.getColumnIndex(parentNode) != 0) {
            List<MenuItem> menuItems = new ArrayList<>();
            for (int i = 1; i <= GridPane.getColumnIndex(parentNode); i++) {
                boolean check = getLeftOccupiedCheck(parentNode, i);
                MenuItem menuItem = new MenuItem("connect with: " + i + " left");
                setLeftMenuItemOnAction(parentNode, i, menuItem);
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
            Node useThisNode = getNodeByCoordinate(GridPane.getRowIndex(node), goalCol);
            gridPane.getChildren().remove(node);
            for (int m = GridPane.getRowIndex(node); m < GridPane.getRowIndex(node) + getRowSpan(node); m++)
                for (int n = GridPane.getColumnIndex(node); n < GridPane.getColumnIndex(node) + getColSpan(node); n++) {
                    AnchorPane node1 = new AnchorPane();
                    node1.setMinSize(0, 0);
                    Information information = new Information();
                    information.setNode(useThisNode);
                    node1.setUserData(information);
                    node1.setOnMousePressed(event1 -> showContextMenu(event1, node1));
                    gridPane.add(node1, n, m);
                }
            GridPane.setRowSpan(useThisNode, getRowSpan(node));
            GridPane.setColumnSpan(useThisNode, getColSpan(node) + finalI);
            numerateLeft(useThisNode);
            useThisNode.setStyle("-fx-background-color: #e01c81");
        });
    }

    private Information getInformation(Node node){
        return (Information) node.getUserData();
    }



    private void checkRight(Node node, MouseEvent event) {
        Node parentNode = getNodeToUse(node);
        if (GridPane.getColumnIndex(parentNode) != gridPane.getColumnCount() - 1) {
            List<MenuItem> menuItems = new ArrayList<>();
            int noToConnect = gridPane.getColumnCount() - 1 - GridPane.getColumnIndex(parentNode) - getColSpan(parentNode) + 1;
            for (int i = 1; i <= noToConnect; i++) {
                boolean check = checkRightIsOccupied(parentNode, i);
                MenuItem menuItem = new MenuItem("connect with: " + i + " right");
                setRightOnAction(parentNode, i, menuItem);
                if (check) {
                    menuItems.add(menuItem);
                }
            }
            contextMenu.getItems().addAll(menuItems);
            contextMenu.show(parentNode, event.getScreenX(), event.getScreenY());
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


    private Node getNodeToUse(Node node) {
        if(getInformation(node).getNode()==null)
            return node;
        else
            return getInformation(node).getNode();
    }

    private void checkUp(final Node node, MouseEvent event) {
        Node usedNode = getNodeToUse(node);
        if (GridPane.getRowIndex(usedNode) != 0) {
            Map<Integer, MenuItem> menuItems = new HashMap<>();
            for (int i = 1; i <= GridPane.getRowIndex(usedNode); i++) {
                boolean check = checkUpOccupied(usedNode, i);
                MenuItem menuItem = new MenuItem("connect with: " + i + " top");
                int finalI = i;
                setUpOnAction(menuItem, usedNode, finalI);
                if (check)
                    menuItems.put(i, menuItem);
            }
            contextMenu.getItems().addAll(menuItems.values().
                    stream().collect(Collectors.toList()));
            contextMenu.show(usedNode, event.getScreenX(), event.getScreenY());
        }
    }

    private void setUpOnAction(MenuItem menuItem, Node usedNode, int finalI) {
        menuItem.setOnAction(actionEvent -> {
            int goalRow = GridPane.getRowIndex(usedNode) - finalI;
            Node useThisNow = getNodeByCoordinate(goalRow, GridPane.getColumnIndex(usedNode));
            gridPane.getChildren().remove(usedNode);
            for (int m = GridPane.getRowIndex(usedNode); m < GridPane.getRowIndex(usedNode) + getRowSpan(usedNode); m++)
                for (int n = GridPane.getColumnIndex(usedNode); n < GridPane.getColumnIndex(usedNode) + getColSpan(usedNode); n++) {
                    AnchorPane node1 = new AnchorPane();
                    node1.setMinSize(0, 0); //
                    Information information = new Information();
                    information.setNode(useThisNow);
                    node1.setUserData(information);
                    node1.setOnMousePressed(event1 -> showContextMenu(event1, node1));
                    gridPane.add(node1, n, m);
                }
            GridPane.setColumnSpan(useThisNow, getColSpan(usedNode));
            GridPane.setRowSpan(useThisNow, getRowSpan(usedNode) + finalI);
            useThisNow.setStyle("-fx-background-color: orange");
            setUpNumeration(useThisNow);
        });
    }

    private void checkDown(Node node, MouseEvent event) {
        Node parentNode = getNodeToUse(node);
        if (GridPane.getRowIndex(parentNode) != gridPane.getRowCount() - 1) {
            List<MenuItem> menuItems = new ArrayList<>();
            int noToConnect = gridPane.getRowCount() - 1 - GridPane.getRowIndex(parentNode) - getRowSpan(parentNode) + 1;
            for (int i = 1; i <= noToConnect; i++) {
                int columnSpan = getColSpan(parentNode);
                boolean check = checkDownIsOccupied(parentNode, i, columnSpan);
                MenuItem menuItem = new MenuItem("connect with: " + i + " bottom");
                setDownOnAction(parentNode, i, columnSpan, menuItem);
                if (check)
                    menuItems.add(menuItem);
            }
            contextMenu.getItems().addAll(menuItems);
            contextMenu.show(parentNode, event.getScreenX(), event.getScreenY());
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

    /**
     * if it sends false it is occupied
     *
     * @param node
     * @param i
     * @param columnSpan
     * @return
     */
    private boolean checkDownIsOccupied(Node node, int i, int columnSpan) {
        for (int k = GridPane.getRowIndex(node); k <= GridPane.getRowIndex(node) + i; k++) {
            for (int l = GridPane.getColumnIndex(node); l <= GridPane.getColumnIndex(node) + columnSpan - 1; l++) {
                if (array[k][l] != 0 && array[k][l] != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)])
                    return false;
            }
        }
        return true;
    }

    private boolean checkUpOccupied(Node node, int i) {
        for (int k = GridPane.getRowIndex(node) - i; k <= GridPane.getRowIndex(node); k++) {
            for (int l = GridPane.getColumnIndex(node); l <= GridPane.getColumnIndex(node) + getColSpan(node) - 1; l++) {
                if (array[k][l] != 0 && array[k][l] != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * method checks if on the right from the created box / empty node
     * there is anything along its row span. If yes we shouldn't
     * enable functionality to connect from that point.
     *
     * @param node
     * @param i
     * @return
     */
    private boolean checkRightIsOccupied(Node node, int i) {
        for (int k = GridPane.getColumnIndex(node); k <= GridPane.getColumnIndex(node) + i; k++) {
            for (int j = GridPane.getRowIndex(node); j < GridPane.getRowIndex(node) + getRowSpan(node); j++)
                if (array[j][k] != 0 && array[j][k] != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)])
                    return false;
        }
        return true;
    }

    /**
     * if we return false it is occupied.
     * we check along the way if there is an item somewhere where we could add need one
     * we want to exclude that items. We need to check in two dimensions:
     * - along the row span + in depth in the direction in which move will be done
     *
     * @param node
     * @param i
     * @return
     */
    private boolean getLeftOccupiedCheck(Node node, int i) {
        for (int k = GridPane.getColumnIndex(node) - i; k <= GridPane.getColumnIndex(node); k++) {
            for (int j = GridPane.getRowIndex(node); j < GridPane.getRowIndex(node) + getRowSpan(node); j++) {
                if (array[j][k] != 0 && array[j][k] != array[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)])
                    return false;
            }
        }
        return true;
    }

    private void setDownNumeration(Node node, int finalI, int span,
                                   int finalColumnSpan, int columnIndex) {
        for (int j = GridPane.getRowIndex(node); j < GridPane.getRowIndex(node) +
                span + finalI; j++) {
            for (int k = columnIndex; k <= columnIndex + finalColumnSpan - 1; k++) {
                array[j][k] = incrementedValue;
                setParentNode(j, k, node);
            }
        }
        incrementedValue++;
    }

    private void setUpNumeration(Node node) {
        for (int j = GridPane.getRowIndex(node); j < GridPane.getRowIndex(node) + getRowSpan(node); j++) {
            for (int l = GridPane.getColumnIndex(node); l < GridPane.getColumnIndex(node) + getColSpan(node); l++) {
                array[j][l] = incrementedValue;
                setParentNode(j, l, node);
            }
        }
        incrementedValue++;
    }

    /**
     * it has to be improved
     *
     * @param node
     * @param finalI
     * @param span
     */
    private void setRightNumeration(Node node, int finalI, int span) {
        for (int j = GridPane.getColumnIndex(node); j < GridPane.getColumnIndex(node) + span + finalI; j++) {
            for (int k = GridPane.getRowIndex(node); k < GridPane.getRowIndex(node) + span; k++) {
                array[k][j] = incrementedValue;
                setParentNode(k, j, node);
            }
        }
        incrementedValue++;
    }

    /**
     * Numeration will be needed when checking if field is occupied
     *
     * @param node
     */
    private void numerateLeft(Node node) {
        for (int j = GridPane.getColumnIndex(node); j < GridPane.getColumnIndex(node) + GridPane.getColumnSpan(node); j++) {
            for (int k = GridPane.getRowIndex(node); k < GridPane.getRowIndex(node) + getRowSpan(node); k++) {
                array[GridPane.getRowIndex(node)][j] = incrementedValue;
                setParentNode(k, j, node);
            }
        }
        incrementedValue++;
    }

    private void setParentNode(int row, int column, Node parentNode) {
        getInformation(getNodeByCoordinate(row, column)).setNode(parentNode);
    }

    private int getRowSpan(Node node) {
        int span = 0;
        if (GridPane.getRowSpan(node) == null) span = 1;
        else span = GridPane.getRowSpan(node);
        return span;
    }


    private int getColSpan(Node node) {
        int columnSpan1 = 0;
        if (GridPane.getColumnSpan(node) == null) columnSpan1 = 1;
        else columnSpan1 = GridPane.getColumnSpan(node);
        return columnSpan1;
    }


    /**
     * save to the database
     *  now we forgot about refresh rate <-
     *  cause we didnt set it anywhere
     * @param actionEvent
     */
    public void saveButton(ActionEvent actionEvent) {
            List<Node> nodes = gridPane.getChildren();
            nodes.remove(0);
            String name = nameField.getText();
            Screen screen = new Screen(name);
            List<ScreenElement> screenElements = new ArrayList<>();

            System.out.println("number of nodes: "+ nodes.size());
            for (Node node : nodes)
                if (node == null)
                    System.out.println("node is null");

            boolean fine = true;
            for (Node node : nodes) {
                if (node != null) {
                    Integer colIndex = getColIndex(node);
                    Integer rowIndex = getRowIndex(node);
                    Integer columnSpan = getColSpan(node);
                    Integer rowSpan = getRowSpan(node);

                    if(!getInformation(getInformation(node).getNode()).isFilled()){
                        System.out.println("not filled");
                        fine=false;
                        break;
                    }
                    else System.out.println("filled");

                    CSVInfo csv = CSVLoader.getMap().get(node);
                    if(csv!=null) {
                        System.out.println(csv);
                        screenElements.add(new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, getInformation(node).filepath, csv));
                    } else
                    screenElements.add(new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, getInformation(node).filepath));
                }
            }
            System.out.println(screenElements);
            if(fine){
                openAssignUsers(screen, screenElements);
            }
    }

    private void openAssignUsers(Screen screen, List<ScreenElement> screenElements) {
        ScreenModel.getInstance().save(screen, screenElements, new ArrayList<>());
        int screenID = screenModel.getScreenIDByName(screen.getName());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AssignUser.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            AssignUserController controller = fxmlLoader.getController();
            controller.setScreenName(screenID);
            stage.setTitle("Assign User");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Icons/arla.png")));
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRowIndex(Node node){
        if(GridPane.getRowIndex(node)==null)
            return 0;
        else
            return GridPane.getRowIndex(node);
    }

    private int getColIndex(Node node){
        if(GridPane.getColumnIndex(node)==null)
            return 0;
        else
            return GridPane.getColumnIndex(node);
    }

    /**
     * button resets
     * @param actionEvent
     */
    public void resetAction(ActionEvent actionEvent) {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        initLay();
        setButton.setDisable(false);
        resetButton.setDisable(false);
        disableLabels();
        clearFields();
        enableFields();
    }

    private void clearFields(){
        colsField.clear();
        rowsFiled.clear();
    }

    private void enableFields() {
        colsField.setDisable(false);
        rowsFiled.setDisable(false);
    }

    public void discardButton(ActionEvent actionEvent) {
        CommandManager.getInstance().getPrevious().rollback(borderPane);
    }


    /**
     * Class encapsulates data that is stored in Node
     * using setUserData
     */
    public class Information {
        private Node node;
        private boolean filled;
        private String filepath; // or query. later url

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }

        public boolean isFilled() {
            return filled;
        }

        public void setFilled(boolean filled) {
            this.filled = filled;
        }

    }
}
