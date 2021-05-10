package GUI.Controller;

import GUI.Model.ClientModel;
import GUI.util.CSVLoader;
import GUI.util.PDFLoader;
import GUI.util.WebsiteLoader;
import be.ScreenElement;
import be.Users;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientViewController {

    public AnchorPane pane;
    private List<ScreenElement> sections;
    private ClientModel model;
    private Users user;
    private GridPane gridPane = new GridPane();
    private WebEngine webEngine;

    public void setUser(Users user) {
        this.user = user;
        model = ClientModel.getInstance();
        //sections = model.getSections(user.getID());

        sections = new ArrayList<>();
        ScreenElement s1 = new ScreenElement(0, 0, 1, 1, "dog");
        ScreenElement s2 = new ScreenElement(0, 1, 1, 1, "src/../Data/PDFData/Assignment 1 - Consultative Solutions.pdf");
        ScreenElement s3 = new ScreenElement(1, 0, 1, 2, "src/../Data/CSVData/test.csv");
        sections.add(s1);
        sections.add(s2);
        sections.add(s3);

        loadScreen();
    }

    private void loadScreen() {
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for(ScreenElement section : sections) {

            String filePath = section.getFilepath();
            AnchorPane anchorPane = null;
            if(section.getFilepath()!=null) {
                String fileType = filePath.substring(filePath.length() - 3);
                switch (fileType) {
                    case "png", "jpg":
                        anchorPane = loadImage(filePath);
                        break;
                    case "pdf":
                        anchorPane = loadPDF(filePath);
                        break;
                    case "csv":
                        anchorPane = loadCSV(filePath);
                        break;
                    default:
                        anchorPane = loadWebsite(filePath);
                        break;
                }
            }

            gridPane.add(anchorPane, section.getColIndex(),
                    section.getRowIndex(), section.getColSpan(), section.getRowSpan());

        }

        //gridPane.setPrefHeight(pane.getHeight());
        //gridPane.setPrefWidth(pane.getWidth());

        //gridPane.setPrefSize(pane.getWidth(), pane.getHeight());
        //gridPane.setMinSize(pane.getWidth(), pane.getHeight());

        /*
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        column1.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(column1);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        gridPane.getRowConstraints().add(row1);

         */



        gridPane.setGridLinesVisible(true);
        pane.getChildren().add(gridPane);

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

    private AnchorPane loadCSV(String filepath) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(300, 300);
        CSVLoader.setDestinationPathCsv(Path.of(filepath));
        CSVLoader.drawCanvas(Path.of(filepath), anchorPane);
        System.out.println("loaded csv");
        return anchorPane;
    }

    private AnchorPane loadImage(String filepath) {
        AnchorPane anchorPane = new AnchorPane();
        System.out.println("loaded image");
        return anchorPane;
    }

}
