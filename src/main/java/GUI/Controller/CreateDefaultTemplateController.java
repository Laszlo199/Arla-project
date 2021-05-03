package GUI.Controller;

import GUI.Model.ScreenModel;
import GUI.Model.exception.ModelException;
import GUI.util.AlertDisplayer;
import GUI.util.ChartCanvas;
import GUI.util.ValidateExtension;
import GUI.util.charts.CreateHistogramChart;
import be.DefaultScreen;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 *
 */
public class CreateDefaultTemplateController implements Initializable {
    @FXML
    private AnchorPane spacePDF;
    @FXML
    private JFXTextField nameField;
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
    private AnchorPane csvChart;
    private WebEngine webEngine;
    private WebEngine pdfViewerEngine;
    private FileChooser fileChooser = new FileChooser();
    private ScreenModel screenModel = ScreenModel.getInstance();
    private final static String DESTINATION_PATH_CSV = "src/../Data/CSVData/";
    private final static String DESTINATION_PATH_PDF = "src/../Data/PDFData/";
    private final static String GOOGLE = "http://www.google.com/search?q=";
    private final static String HOME = "https://www.google.com/webhp";

    //fields to save
    private Path destinationPathCSV;
    private String insertedWebsite;
    private Path destinationPathPDF;
    private String name;

    @Override
    public void initialize(URL url2, ResourceBundle resourceBundle) {

    }

    private String getHTMLForPDF(Path pdfPath) {
        return screenModel.getHTML(pdfPath);
    }


    /**
     * we need to save three paths:
     * - one to website
     * - one to csv
     * - one to PDF
     *
     * @param actionEvent
     */
    public void save(ActionEvent actionEvent) {
        name = nameField.getText();
        if (name != null && destinationPathCSV != null &&
                insertedWebsite != null && destinationPathPDF != null) {
            screenModel.saveDefaultTemplate(new DefaultScreen(name, destinationPathCSV,
                    destinationPathPDF, insertedWebsite));
        }
    }

    /**
     * not save anything.
     * Also delete files that have been stored over there
     * We need to delete PDF HTML, CSV
     *
     * @param actionEvent
     */
    public void cancel(ActionEvent actionEvent) {
        if(destinationPathPDF!=null)
            screenModel.deletePDFfiles(destinationPathPDF);
        if(destinationPathCSV!=null)
            screenModel.deleteCSV(destinationPathCSV);
    }

    /**
     * some actions to load website
     *
     * @param actionEvent
     */
    public void loadWebsite(ActionEvent actionEvent) {
        addWebView();
        executeQuery();
    }

    private void executeQuery() {
        insertedWebsite = websiteLink.getText();
        webEngine.load(GOOGLE + insertedWebsite);
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

    /**
     * loads website which will handle
     */
    private void loadPDFViewer(String htmlPath) {
        WebView webView = new WebView();
        pdfViewerEngine = webView.getEngine();
        webView.prefHeightProperty().bind(spacePDF.heightProperty());
        webView.prefWidthProperty().bind(spacePDF.widthProperty());
        spacePDF.getChildren().add(webView);
        File f = new File(htmlPath);
        pdfViewerEngine.load(f.toURI().toString());
    }

    /**
     * method at first opens file chooser then pdf is saved
     * we get html and display it in newly created Web view
     * that is dynamically added to the stage
     *
     * @param actionEvent
     */
    public void loadPDF(ActionEvent actionEvent) {
        File selectedFile = getSelectedFile(actionEvent, "Choose PDF file");
        if (ValidateExtension.validatePDF(selectedFile)) {
            attachment3.setText(selectedFile.getName());
            destinationPathPDF = Path.of(DESTINATION_PATH_PDF + selectedFile.getName());
            saveFile(selectedFile, destinationPathPDF);
            String htmlPath = getHTMLForPDF(destinationPathPDF);
            loadPDFViewer(htmlPath);
        }
    }

    /**
     * we need file chooser, maybe i should save that file
     * then we need double[] and then
     * we can use CreateHistogramChart
     *
     * @param actionEvent
     */
    public void loadCSV(ActionEvent actionEvent) {
        File selectedFile = getSelectedFile(actionEvent, "Choose csv file");
        if (ValidateExtension.validateCSV(selectedFile)) {
            attachment1.setText(selectedFile.getName());
            destinationPathCSV = Path.of(DESTINATION_PATH_CSV + selectedFile.
                    getName());
            saveFile(selectedFile, destinationPathCSV);
            drawCanvas(destinationPathCSV);
        } else {
            //repeat operation
        }
    }

    private void drawCanvas(Path destinationPath) {
        CreateHistogramChart createHistogramChart =
                new CreateHistogramChart(getHistogramData(destinationPath));
        ChartCanvas canvas = new ChartCanvas(createHistogramChart.createChart());
        csvChart.getChildren().add(canvas);
        canvas.widthProperty().bind(csvChart.widthProperty());
        canvas.heightProperty().bind(csvChart.heightProperty());
    }

    private File getSelectedFile(ActionEvent actionEvent, String information) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        fileChooser.setTitle(information);
        return fileChooser.showOpenDialog(stage);
    }


    private void saveFile(File selectedFile, Path destinationPath) {
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
     *
     * @return
     */
    private double[] getHistogramData(Path destinationPath) {
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
