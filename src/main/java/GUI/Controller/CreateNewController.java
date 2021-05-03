package GUI.Controller;

import be.Section;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateNewController implements Initializable {
    @FXML private GridPane gridPane;
    @FXML private VBox vBox;
    public Label jpgLbl = new Label("JPG");
    public Label pngLbl = new Label("PNG");
    @FXML private Button saveBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillVBox();
        gridPane.setGridLinesVisible(true);
        jpgLbl.setOnDragDetected(event -> dragStart(event, jpgLbl));
        pngLbl.setOnDragDetected(event -> dragStart(event, pngLbl));
        gridPane.setOnDragDropped(event -> dragDropped(event));
        gridPane.setOnDragOver(event -> dragOver(event));
        saveBtn.setOnAction(event -> saveAction(event));

    }

    private void saveAction(ActionEvent event) {
        if(!checkIfEmpty()) {

            List<Node> nodes = gridPane.getChildren();
            List<Section> sections = new ArrayList<>();

            int i = 0;

            for(Node node : nodes) {
                Integer width = GridPane.getColumnSpan(node);
                Integer height = GridPane.getRowSpan(node);

                if(width==null) width = 1;
                if(height==null) height = 1;

                Section section = new Section(i, width, height);
                i++;
                if(i!=gridPane.getChildren().size()) {
                    sections.add(section);
                    System.out.println(section);
                }
            }
        }
    }

    private boolean checkIfEmpty() {
        if(gridPane.getChildren().size()-1 != gridPane.getRowCount()*gridPane.getColumnCount())
            return true;
        else return false;
    }

    private void fillVBox() {
        vBox.getChildren().addAll(jpgLbl, pngLbl);
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
        if(dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void dragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        Node node = event.getPickResult().getIntersectedNode();
        if(db.hasString()){

            /*
            Integer col = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            System.out.println(row + ", " + col);
            if(row == null) row = 0;
            if(col == null) col = 0;
            gridPane.add(lbl, col, row);
             */

            AnchorPane anchorPane = (AnchorPane) node;
            Label lbl = new Label(db.getString());
            Button button = new Button("load file");
            button.setOnAction(actionEvent -> loadFiles(actionEvent, anchorPane, db.getString()));
            VBox vbox = new VBox();
            vbox.getChildren().addAll(lbl, button);
            vbox.setPrefWidth(anchorPane.getWidth());
            vbox.setSpacing(20);
            vbox.setPadding(new Insets(10, 10, 10, 10));
            vbox.setAlignment(Pos.CENTER);
            anchorPane.getChildren().addAll(vbox);


            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void loadFiles(ActionEvent event, AnchorPane anchorPane, String fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                "*."+fileType.toLowerCase()));
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
