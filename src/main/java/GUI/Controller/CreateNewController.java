package GUI.Controller;

import GUI.util.CSVLoader;
import GUI.util.PDFLoader;
import GUI.util.WebsiteLoader;
import be.Section;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateNewController implements Initializable {
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
    WebEngine webEngine = new WebEngine();
    WebEngine pdfViewerEngine = new WebEngine();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillVBox();
        gridPane.setGridLinesVisible(true);
        setOnDrags();
        gridPane.setOnDragDropped(event -> dragDropped(event));
        gridPane.setOnDragOver(event -> dragOver(event));
        saveBtn.setOnAction(event -> saveAction(event));
    }

    private void setOnDrags() {
        jpgLbl.setOnDragDetected(event -> dragStart(event, jpgLbl));
        pngLbl.setOnDragDetected(event -> dragStart(event, pngLbl));
        pdf.setOnDragDetected(event -> dragStart(event, pdf));
        csv.setOnDragDetected(event -> dragStart(event, csv));
        http.setOnDragDetected(event -> dragStart(event, http));
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

                Section section = new Section(i, width, height);
                i++;
                //here
                if (i != gridPane.getChildren().size()) {
                    sections.add(section);
                    System.out.println(section);
                }
            }
        }
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

            /*
            Integer col = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            System.out.println(row + ", " + col);
            if(row == null) row = 0;
            if(col == null) col = 0;
            gridPane.add(lbl, col, row);
             */

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
}
