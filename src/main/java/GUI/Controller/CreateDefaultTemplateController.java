package GUI.Controller;

import GUI.Model.ScreenModel;
import GUI.Model.exception.ModelException;
import GUI.util.AlertDisplayer;
import GUI.util.ChartCanvas;
import GUI.util.ValidateExtension;
import GUI.util.charts.CreateHistogramChart;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 *
 */
public class CreateDefaultTemplateController implements Initializable {
    public AnchorPane spacePDF;
    @FXML
    private AnchorPane websiteSpace;
    @FXML
    private JFXTextField websiteLink;
    @FXML
    private Label attachment1;
    @FXML
    private Label attachment2;
    @FXML
    private Label attachment3;
    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane csvChart;
    private WebView pdfViewer;
    private WebEngine webEngine ;
    private WebEngine pdfViewerEngine;
    private FileChooser fileChooser = new FileChooser();
    private ScreenModel screenModel = new ScreenModel();
    private final static String DESTINATION_PATH_CSV = "src/../Data/CSVData/";
    private final static String DESTINATION_PATH_PDF = "src/../Data/PDFData/";
    private final static String GOOGLE = "http://www.google.com/search?q=";
    private final static String HOME = "https://www.google.com/webhp";
    private final static String PDF_VIEWER = "https://www.docfly.com/pdf-viewer";
    private Path destinationPath;
    private String insertedWebsite;

    @Override
    public void initialize(URL url2, ResourceBundle resourceBundle) {

    }

    private String getHTMLForPDF(Path pdfPath){
        return screenModel.getHTML(pdfPath);
    }


    /**
     * provided our own implementation that does nothing when file
     * is dropped
     */
    private void disableDrag() {
        pdfViewer.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
            }
        });
        pdfViewer.setOnDragDropped((DragEvent event) -> {
        });
    }


    /**
     * save pane to all screens. we need to save which file corresponds to which corner
     * @param actionEvent
     */
    public void save(ActionEvent actionEvent) {
    }

    /**
     * not save anything.
     * Also delete files that have been storied over there
     * @param actionEvent
     */
    public void cancel(ActionEvent actionEvent) {
    }

    /**
     * some actions to load website
     * @param actionEvent
     */
    public void loadWebsite(ActionEvent actionEvent) {
        addWebView();
        executeQuery();
    }

    private void executeQuery() {
        insertedWebsite = websiteLink.getText();
        webEngine.load(GOOGLE+insertedWebsite);
        attachment2.setText(insertedWebsite);
    }

    private void addWebView() {
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webView.setZoom(0.6);
        webView.prefHeightProperty().bind(websiteSpace.heightProperty());
        webView.prefWidthProperty().bind(websiteSpace.widthProperty());
        websiteSpace.getChildren().add(webView);
    }
    //pdfViewerEngine  = pdfViewer.getEngine();
    // pdfViewerEngine.load(PDF_VIEWER);
    //disableDrag();
    /**
     * loads website which will handle
     */
    private void loadPDFViewer(String htmlPath) {
       WebView webView = new WebView();
       pdfViewerEngine = webView.getEngine();
       spacePDF.getChildren().add(webView);
      File f = new File(htmlPath);
      pdfViewerEngine.load(f.toURI().toString());
    }

    /**
     * method at first opens file chooser then pdf is saved
     * we get html and display it in newly created Web view
     * that is dynamically added to the stage
     * @param actionEvent
     */
    public void loadPDF(ActionEvent actionEvent) {
        File selectedFile = getSelectedFile(actionEvent, "Choose PDF file");
        if(ValidateExtension.validatePDF(selectedFile)){
            attachment3.setText(selectedFile.getName());
            Path destinationPath = Path.of(DESTINATION_PATH_PDF+selectedFile.getName());
            saveFile(selectedFile, destinationPath);
            String htmlPath = getHTMLForPDF(destinationPath);
            loadPDFViewer(htmlPath);
        }
    }


    /**
     * we need file chooser, maybe i should save that file
     * then we need double[] and then
     * we can use CreateHistogramChart
     * @param actionEvent
     */
    public void loadCSV(ActionEvent actionEvent) {
        File selectedFile = getSelectedFile(actionEvent, "Choose csv file");
        if(ValidateExtension.validateCSV(selectedFile)){
            attachment1.setText(selectedFile.getName());
            destinationPath =Path.of(DESTINATION_PATH_CSV+selectedFile.
                    getName());
           saveFile(selectedFile, destinationPath);
           drawCanvas(destinationPath);
        }else {
            //repeat operation
        }
    }

    private void drawCanvas(Path destinationPath){
        CreateHistogramChart createHistogramChart =
                new CreateHistogramChart(getHistogramData(destinationPath));
        ChartCanvas canvas = new ChartCanvas(createHistogramChart.createChart());
        csvChart.getChildren().add(canvas);
        canvas.widthProperty().bind( csvChart.widthProperty());
        canvas.heightProperty().bind( csvChart.heightProperty());
    }

    private File getSelectedFile(ActionEvent actionEvent, String information){
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        fileChooser.setTitle(information);
        return fileChooser.showOpenDialog(stage);
    }


    private void saveFile(File selectedFile, Path destinationPath){
        try {
            screenModel.saveFile(Path.of(selectedFile.getAbsolutePath()),
                    destinationPath);
        } catch (ModelException e) {
            e.printStackTrace();
            AlertDisplayer.displayInformationAlert("saving",
                    "couldn't save", "");
        }
    }
        /**
         * get data from selected file
         * @return
         */
        private double[] getHistogramData(Path destinationPath){
            try {
                return screenModel.getHistogramData(destinationPath);
            } catch (ModelException e) {
                e.printStackTrace();
                AlertDisplayer.displayInformationAlert("getting data..",
                        "Couldnt get data", "");
            }
            return new double[0];
        }

}
