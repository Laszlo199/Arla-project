package GUI.Controller;

import GUI.Model.ScreenModel;
import GUI.util.CSVLoader;
import GUI.util.ImageLoader;
import GUI.util.PDFLoader;
import GUI.util.WebsiteLoader;
import be.Screen;
import be.ScreenElement;
import be.Section;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CreateNewController implements Initializable {
    @FXML private Button bTop;
    @FXML private Button bLeft;
    @FXML private Button bBottom;
    @FXML private Button bRight;
    @FXML private Button saveNameBtn;
    @FXML private TextField screenNameTextField;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox vBox;
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
    Button addColButton = new Button("add col");
    Button addRowButton = new Button("add row");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }

    private void borderListeners() {
        bBottom.setVisible(false);
        bTop.setVisible(false);
        bRight.setVisible(false);
        bLeft.setVisible(false);
        gridPane.setOnMouseMoved(mouseEvent -> {
            if(mouseEvent.getX() >= gridPane.getWidth()-50){
                bRight.setVisible(true);
                System.out.println("right line");
            }


            if(mouseEvent.getX() <= 50)
                System.out.println("left line");

            if(mouseEvent.getY()>=gridPane.getHeight()-50)
                System.out.println("bottom line");

            if(mouseEvent.getY() <= 50)
                System.out.println("top line");

            if(mouseEvent.getY()>50 && mouseEvent.getY()< gridPane.getHeight()-50
                    && mouseEvent.getY()>50 && mouseEvent.getX()< gridPane.getWidth()-50){
                bRight.setVisible(false);
                // gridPane.getChildren().remove(addColButton);
                // gridPane.getChildren().remove(addRowButton);
            }
        });
/*
        gridPane.setOnMouseMoved(mouseEvent -> {
            if(mouseEvent.getY()>50 && mouseEvent.getY()< gridPane.getHeight()-50
            && mouseEvent.getY()>50 && mouseEvent.getX()< gridPane.getWidth()-50){
                bRight.setVisible(false);
               // gridPane.getChildren().remove(addColButton);
               // gridPane.getChildren().remove(addRowButton);
            }

        });

 */
    }

    enum HORIZONTAL{
        TOP,
        BOTTOM
    }
    enum VERTICAL{
        LEFT,
        RIGHT
    }


    /**
     * add button with functionality for adding column
     * @param vertical
     */
    private void colButton( VERTICAL vertical){

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
                    PDFLoader.loadPDF(actionEvent, new FileChooser());
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
            ScreenModel.getInstance().save(screen, screenElements);
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


    public void btnAssignUser(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assignUsersView.fxml"));
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/Icons/arla.png"));
            stage.setTitle("Assign user");
            //stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
