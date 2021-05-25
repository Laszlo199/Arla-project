package gui.Controller;

import be.CSVInfo;
import be.Screen;
import gui.Model.ClientModel;
import gui.Model.ScreenModel;
import gui.util.CSVLoader;
import gui.util.ExcelLoader;
import gui.util.Observator.ObserverSingle;
import gui.util.PDFLoader;
import gui.util.WebsiteLoader;
import be.ScreenElement;
import be.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientViewController extends ObserverSingle implements Initializable{

    public AnchorPane pane;
    private List<ScreenElement> sections;
    private ClientModel model;
    private User user;
    private GridPane gridPane = new GridPane();
    private WebEngine webEngine;
    private Stage stageToSet;

    //when logging as a client
    public void setUser(User user, Stage stage) {
        this.stageToSet =stage;
        this.user = user;
        model = ClientModel.getInstance();

        //sections = model.getSections(user.getID());

      //  sections = new ArrayList<>();
      //  ScreenElement s1 = new ScreenElement(0, 0, 1, 1, "dog");
      //  ScreenElement s2 = new ScreenElement(0, 1, 1, 1, "src/../Data/PDFData/Assignment 1 - Consultative Solutions.pdf");
      //  ScreenElement s3 = new ScreenElement(1, 0, 1, 2, "src/../Data/CSVData/kilograms.csv", new CSVInfo(true, "TITLE", CSVInfo.CSVType.LINECHART));
      //  sections.add(s1);
       // sections.add(s2);
       // sections.add(s3);

        sections = model.getSections(user.getID());


        loadScreen(stage);
    }

    //when showing a preview
    public void setScreen(Screen screen, Stage stage) {
        model = ClientModel.getInstance();
        sections = model.getSections(screen);

        for(ScreenElement s : sections) System.out.println(s);

        loadScreen(stage);
    }

    private void loadScreen(Stage stage) {
        for(ScreenElement section : sections) {

            String filePath = section.getFilepath();
            AnchorPane anchorPane = null;
            if(filePath!=null) {
                String fileType = "";
                if (filePath.length() > 4) fileType = filePath.substring(filePath.length() - 4);
                else fileType = filePath;
                System.out.println(filePath);
                switch (fileType) {
                    case ".png", ".jpg":
                        anchorPane = loadImage(filePath);
                        break;
                    case ".pdf":
                        anchorPane = loadPDF(filePath);
                        break;
                    case ".csv":
                        anchorPane = loadCSV(filePath, section.getCsvInfo());
                        break;
                    case "xlsx":
                        anchorPane = loadExcel(filePath);
                        break;
                    default:
                        anchorPane = loadWebsite(filePath);
                        break;
                }
            }

            gridPane.add(anchorPane, section.getColIndex(),
                    section.getRowIndex(), section.getColSpan(), section.getRowSpan());
            GridPane.setHgrow(anchorPane, Priority.SOMETIMES);
            GridPane.setVgrow(anchorPane, Priority.SOMETIMES);
        }

        loadZoomable();

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();

    }

    private void loadZoomable() {
        for(Node node : gridPane.getChildren()) {
            node.setOnScroll(event -> zoomAction(event, node));
            node.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
                @Override public void changed(ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) {
                    node.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
                }
            });
        }
    }


    private AnchorPane loadWebsite(String filepath) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        WebsiteLoader websiteLoader = new WebsiteLoader(webEngine);
        websiteLoader.addWebView(anchorPane);
        websiteLoader.executeQuery(filepath);
        System.out.println("loaded website");
        return anchorPane;
    }


    private AnchorPane loadPDF(String filepath) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        PDFLoader.setDestinationPathPDF(Path.of(filepath));
        PDFLoader.loadPDFViewer(anchorPane);
        System.out.println("loaded pdf");
        return anchorPane;
    }

    private AnchorPane loadCSV(String filepath, CSVInfo csvInfo) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        CSVLoader.setDestinationPathCsv(Path.of(filepath));
        switch (csvInfo.getType()) {
            case TABLE -> CSVLoader.createTable(csvInfo.isHeader(), anchorPane);
            case BARCHART -> CSVLoader.createBarchart(csvInfo.isHeader(), csvInfo.getTitle(), anchorPane);
            case LINECHART -> CSVLoader.createLinechart(csvInfo.isHeader(), csvInfo.getTitle(), anchorPane);
        }
        System.out.println("loaded csv");
        return anchorPane;
    }

    private AnchorPane loadExcel(String filePath) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        ExcelLoader.setDestinationPathXLSX(Path.of(filePath));
        ExcelLoader.showExcel(anchorPane);
        return anchorPane;
    }

    private AnchorPane loadImage(String filepath) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        Image image = new Image(filepath);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        anchorPane.getChildren().add(imageView);
        imageView.setFitHeight(anchorPane.getHeight());
        imageView.setFitWidth(anchorPane.getWidth());
        System.out.println("loaded image");
        return anchorPane;
    }

    private void zoomAction(ScrollEvent event, Node node) {
        final double SCALE_DELTA = 1.1;
        AnchorPane anchorPane = (AnchorPane) node;
        Node child = anchorPane.getChildren().get(0);
        event.consume();

        if (event.getDeltaY() == 0) {
            return;
        }

        double scaleFactor =
                (event.getDeltaY() > 0)
                        ? SCALE_DELTA
                        : 1/SCALE_DELTA;

        if((child.getScaleX() * scaleFactor) >=1.0 ) {
            child.setScaleX(child.getScaleX() * scaleFactor);
            child.setScaleY(child.getScaleY() * scaleFactor);
        }


        /*
        Scale newScale = new Scale();
        newScale.setPivotX(event.getX());
        newScale.setPivotY(event.getY());
        newScale.setX(child.getScaleX() * scaleFactor);
        newScale.setY(child.getScaleY() * scaleFactor);
        child.getTransforms().add(newScale);


         */
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ScreenModel.observersSingle.add(this);
        ScreenModel.getInstance().attachSingleObserver(this);
    }

    public void setScreenObs(Screen screen){
        setScreen(screen);
    }

    /**
     * if update reload all screen
     */
    @Override
    public void update() {
        pane.getChildren().clear();
        loadScreen(stageToSet);
    }
}
