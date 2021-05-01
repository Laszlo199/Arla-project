package GUI.Controller;

import GUI.Model.ScreenModel;
import GUI.Model.exception.ModelException;
import GUI.util.AlertDisplayer;
import GUI.util.ChartCanvas;
import GUI.util.ValidateExtension;
import GUI.util.charts.CreateHistogramChart;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 *
 */
public class CreateDefaultTemplateController implements Initializable {
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
    @FXML
    private StackPane pdfSpace;
    private WebEngine webEngine ;
    private FileChooser fileChooser = new FileChooser();
    private ScreenModel screenModel = new ScreenModel();
    private final static String DESTINATION_PATH_CSV = "src/../Data/CSVData/";
    private final static String DESTINATION_PATH_PDF = "src/../Data/PDFData/";
    private final static String GOOGLE = "http://www.google.com/search?q=";
    private final static String HOME = "https://www.google.com/webhp";
    private Path destinationPath;

    @Override
    public void initialize(URL url2, ResourceBundle resourceBundle) {

    }


    public void action(ActionEvent actionEvent) {

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
     * method will open file chooser,
     * then we will create a pane and add pdf view with it
     * @param actionEvent
     */
    public void loadPDF(ActionEvent actionEvent) {



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
        String query = websiteLink.getText();
        webEngine.load(GOOGLE+query);
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
     * we need file chooser, maybe i should save that file
     * then we need double[] and then
     * we can use CreateHistogramChart
     * @param actionEvent
     */
    public void loadCSV(ActionEvent actionEvent) {
        File selectedFile = getSelectedFile(actionEvent);
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

    private File getSelectedFile(ActionEvent actionEvent){
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        fileChooser.setTitle("Choose csv file");
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
