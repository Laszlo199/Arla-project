package GUI.Controller;

import GUI.Model.ScreenModel;
import GUI.Model.UserModel;
import GUI.util.CSVLoader;
import GUI.util.ImageLoader;
import GUI.util.PDFLoader;
import GUI.util.WebsiteLoader;
import be.Screen;
import be.ScreenElement;
import be.User;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

public class CreateNewController implements Initializable {
    @FXML private Button bTop;
    @FXML private Button bLeft;
    @FXML private Button bBottom;
    @FXML private Button bRight;
    @FXML private Button saveNameBtn;
    @FXML private TextField screenNameTextField;
    @FXML private GridPane gridPane;
    @FXML private VBox vBox;
    @FXML private TextField searchField;
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> usersNames;
   // private UserModel userModel = new UserModel(); // does it have to be a singleton?

    public Label jpgLbl = new Label("JPG");
    public Label pngLbl = new Label("PNG");
    public Label pdf = new Label("PDF");
    public Label csv = new Label("CSV");
    public Label http = new Label("HTTP");
    @FXML
    private Button saveBtn;
    ContextMenu contextMenu = new ContextMenu();
    MenuItem right = new MenuItem("connect with right");
    MenuItem down = new MenuItem("connect with bottom");
    WebEngine webEngine = new WebEngine();
    WebEngine pdfViewerEngine = new WebEngine();
    Map<Node, String> nodeMap = new HashMap<>();
    private UserModel userModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userModel = new UserModel();
        fillVBox();
        gridPane.setGridLinesVisible(true);
        setOnDrags();
        gridPane.setOnDragDropped(event -> dragDropped(event));
        gridPane.setOnDragOver(event -> dragOver(event));
        saveBtn.setOnAction(event -> saveAction(event));
        saveNameBtn.setOnAction(event -> saveName(event));
        for(Node node : gridPane.getChildren()) {
            node.setOnMousePressed(event -> showContextMenu(event, node));
        }
        contextMenu.getItems().addAll(right, down);

        borderListeners();
        initUserTableView();
        search();

    }

    private void borderListeners() {
        bBottom.setVisible(false);
        bTop.setVisible(false);
        bRight.setVisible(false);
        bLeft.setVisible(false);
        gridPane.setOnMouseMoved(mouseEvent -> {
            if(mouseEvent.getX() >= gridPane.getWidth()-15){
                bRight.setVisible(true);
                System.out.println("right line");
            }


            if(mouseEvent.getX() <= 15){
                bLeft.setVisible(true);
                System.out.println("left line");
            }


            if(mouseEvent.getY()>=gridPane.getHeight()-15){
                bBottom.setVisible(true);
                System.out.println("bottom line");
            }


            if(mouseEvent.getY() <= 15){
                bTop.setVisible(true);
                System.out.println("top line");
            }


            if(mouseEvent.getY()>15 && mouseEvent.getY()< gridPane.getHeight()-15
                    && mouseEvent.getX()>15 && mouseEvent.getX()< gridPane.getWidth()-15){
                bRight.setVisible(false);
                bTop.setVisible(false);
                bLeft.setVisible(false);
                bBottom.setVisible(false);
            }
        });

    }

    private void setOnDrags() {
        jpgLbl.setOnDragDetected(event -> dragStart(event, jpgLbl));
        pngLbl.setOnDragDetected(event -> dragStart(event, pngLbl));
        pdf.setOnDragDetected(event -> dragStart(event, pdf));
        csv.setOnDragDetected(event -> dragStart(event, csv));
        http.setOnDragDetected(event -> dragStart(event, http));
    }

    //how do we know that this is empty. i dont get that method
    private boolean checkIfEmpty() {
        if (gridPane.getChildren().size() - 1 != gridPane.getRowCount() * gridPane.getColumnCount())
            return true;
        else return false;
    }

    private void fillVBox() {
        vBox.getChildren().addAll(jpgLbl, pngLbl, pdf, csv, http);
        vBox.setSpacing(10);
    }

    public void dragStart(MouseEvent event, Label source) {
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent cb = new ClipboardContent();
        source.setCursor(Cursor.CLOSED_HAND);
        cb.putString(source.getText());
        db.setContent(cb);
        event.consume();
    }

    public void dragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void dragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node node = event.getPickResult().getIntersectedNode();
        if (db.hasString()) {

            AnchorPane anchorPane = (AnchorPane) node;
            String fileType = db.getString();
            Label lbl = new Label(fileType);
            if(fileType.equals("HTTP")){
                JFXTextField field = new JFXTextField();
                Button btn = new Button("load");
                btn.setOnAction(actionEvent -> {
                        String que = field.getText();
                        nodeMap.put(node, que);
                        loadWebsite(anchorPane, que);
                        });
                loadNodes(anchorPane, lbl, field, btn);
                success =true;
            }
            else if (fileType.equals("PDF")){
                Button button = new Button("load file");
                button.setOnAction(actionEvent ->{
                    PDFLoader.loadPDF( new FileChooser());
                    PDFLoader.loadPDFViewer(anchorPane);
                    nodeMap.put(node, PDFLoader.getDestinationPathPDF().toString());
                });
                loadNodes(anchorPane, lbl, button);
                success = true;
            }
            else if(fileType.equals("CSV")){
                Button button = new Button("load file");
                button.setOnAction(actionEvent -> {
                    CSVLoader.loadCSV(actionEvent, new FileChooser(), anchorPane);
                    nodeMap.put(node, CSVLoader.getDestinationPathCSV().toString());
                });
                loadNodes(anchorPane, lbl, button);
                success = true;
            }
                else {
                Button button = new Button("load file");
                button.setOnAction(actionEvent -> {
                   ImageLoader.loadImage(anchorPane);
                   nodeMap.put(node, ImageLoader.getDestinationPath().toString());
                });
                loadNodes(anchorPane, lbl, button);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     *
     * @param anchorPane
     */
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

    private String loadFiles(ActionEvent event, AnchorPane anchorPane, String fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                "*." + fileType.toLowerCase()));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        Image image;

        if (!files.isEmpty()) {
            String fileP = files.get(0).toURI().toString();
            image = new Image(fileP);

            anchorPane.getChildren().removeAll();
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            anchorPane.getChildren().add(imageView);
            imageView.setFitHeight(anchorPane.getHeight());
            imageView.setFitWidth(anchorPane.getWidth());
            return fileP;
        }
        return new String("");
    }

    private void saveName(ActionEvent event) {
        //thisScreen.setName(screenNameTextField.getText());
    }

    private void saveAction(ActionEvent event) {
        if (!checkIfEmpty()) {
            List<Node> nodes = gridPane.getChildren();
            nodes.remove(nodes.size()-1);
            String name = screenNameTextField.getText();
            Screen screen = new Screen(name);
             //later here we will add userID and refresh rate
            List<ScreenElement> screenElements = new ArrayList<>();
            List<User> usersList = userTableView.getSelectionModel().getSelectedItems();

            for(Node node: nodes)
                if(node==null)
                    System.out.println("node is null");

            for (Node node : nodes) {
                if(node!=null) {
                    Integer colIndex = GridPane.getColumnIndex(node);
                    System.out.println("col Idex:" + colIndex);
                    Integer rowIndex = GridPane.getRowIndex(node);
                    System.out.println("row Idex:" + rowIndex);
                    Integer columnSpan = GridPane.getColumnSpan(node);
                    Integer rowSpan = GridPane.getRowSpan(node);

                    if (colIndex == null) colIndex = 0;
                    if (rowIndex == null) rowIndex = 0;
                    if (columnSpan == null) columnSpan = 1;
                    if (rowSpan == null) rowSpan = 1;
                    //now we are missing file path
                    screenElements.add(new ScreenElement(colIndex, rowIndex,
                            columnSpan, rowSpan, nodeMap.get(node)));
                }
            }
            ScreenModel.getInstance().save(screen, screenElements, usersList);
        }
    }

    private void showContextMenu(MouseEvent mouseEvent, Node node) {
        if (mouseEvent.isSecondaryButtonDown()) {

            right.setDisable(false);
            down.setDisable(false);

            if(GridPane.getRowIndex(node)!=null) {
                if (GridPane.getRowIndex(node) == gridPane.getRowCount() - 1) down.setDisable(true);
            }
            if(GridPane.getColumnIndex(node)!=null) {
                if (GridPane.getColumnIndex(node) == gridPane.getColumnCount() - 1) right.setDisable(true);
            }
            contextMenu.show(node, mouseEvent.getScreenX(), mouseEvent.getScreenY());

            right.setOnAction((event) -> {
                int span = 0;
                if (GridPane.getColumnSpan(node) == null) span = 1;
                GridPane.setColumnSpan(node, span + 1);
                node.setStyle("-fx-background-color: GREEN"); //to check if worked
            });

            down.setOnAction((event) -> {
                int span = 0;
                if (GridPane.getRowSpan(node) == null) span = 1;
                GridPane.setRowSpan(node, span + 1);
                node.setStyle("-fx-background-color: BLUE"); //to check if worked
            });
        }
    }



    private void initUserTableView(){
        usersNames.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        userModel.loadUsers();
        userTableView.setItems(userModel.getAllUser());


    }
    public void search(){

        FilteredList<User> filteredList = new FilteredList<>(userModel.getAllUser(), b->true);
        searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredList.setPredicate(users -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (users.getUserName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else
                    return false;
            });
        });
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(userTableView.comparatorProperty());
        userTableView.setItems(sortedList);

    }

    public void selectUser(MouseEvent event) {
        userTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //lblUserName.setText("Selected User: "+(userTableView.getSelectionModel().getSelectedItem().getUserName()));


    }

    /**
     * add row at the top
     * @param actionEvent
     */
    public void topButtonClicked(ActionEvent actionEvent) {

    }

    /**
     * add col on the left
     * @param actionEvent
     */
    public void leftButtonClicked(ActionEvent actionEvent) {
    }

    public void bottomButtonClicked(ActionEvent actionEvent) {
    }

    public void rightButtonClicked(ActionEvent actionEvent) {
    }
}
