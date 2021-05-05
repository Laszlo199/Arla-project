package GUI.Controller;

import GUI.util.CSVLoader;
import GUI.util.PDFLoader;
import GUI.util.WebsiteLoader;
import be.Screen;
import be.Section;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateNewController implements Initializable {
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
    private Screen thisScreen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        thisScreen = new Screen(0, null);
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
                btn.setOnAction(actionEvent -> loadWebsite(anchorPane, field.getText()));
                loadNodes(anchorPane, lbl, field, btn);
                success =true;
            }
            else if (fileType.equals("PDF")){
                Button button = new Button("load file");
                button.setOnAction(actionEvent ->{
                    PDFLoader.loadPDF(actionEvent, new FileChooser());
                    PDFLoader.loadPDFViewer(anchorPane);
                });
                loadNodes(anchorPane, lbl, button);
                success = true;
            }
            else if(fileType.equals("CSV")){
                Button button = new Button("load file");
                button.setOnAction(actionEvent -> CSVLoader.loadCSV(actionEvent,
                        new FileChooser(), anchorPane));
                loadNodes(anchorPane, lbl, button);
                success = true;
            }
                else {
                Button button = new Button("load file");
                button.setOnAction(actionEvent -> loadFiles(actionEvent, anchorPane,
                        fileType));
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

    private void loadFiles(ActionEvent event, AnchorPane anchorPane, String fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                "*." + fileType.toLowerCase()));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        Image image;

        if (!files.isEmpty()) {
            image = new Image(files.get(0).toURI().toString());

            anchorPane.getChildren().removeAll();
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            anchorPane.getChildren().add(imageView);
            imageView.setFitHeight(anchorPane.getHeight());
            imageView.setFitWidth(anchorPane.getWidth());
        }
    }

    private void saveName(ActionEvent event) {
        thisScreen.setName(screenNameTextField.getText());
    }

    private void saveAction(ActionEvent event) {
        if (!checkIfEmpty()) {

            List<Node> nodes = gridPane.getChildren();
            List<Section> sections = new ArrayList<>();

            int i = 0;

            for (Node node : nodes) {
                Integer width = GridPane.getColumnSpan(node);
                Integer height = GridPane.getRowSpan(node);

                //here
                if (width == null) width = 1;
                if (height == null) height = 1;

                Section section = new Section(thisScreen.getId(), i, width, height);
                i++;
                //here
                if (i != gridPane.getChildren().size()) {
                    sections.add(section);
                    System.out.println(section);
                }
            }
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
}
