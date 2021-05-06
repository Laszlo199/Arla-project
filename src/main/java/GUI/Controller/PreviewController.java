package GUI.Controller;

import GUI.util.CSVLoader;
import GUI.util.PDFLoader;
import GUI.util.WebsiteLoader;
import be.DefaultScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import org.jfree.data.io.CSV;

public class PreviewController {
    @FXML private AnchorPane websiteSpace;
    @FXML private AnchorPane csvChart;
    @FXML private AnchorPane spacePDF;
    private WebEngine webEngine;
    private DefaultScreen thisScreen;

    public void setScreen(DefaultScreen screen) {
        thisScreen = screen;
        loadWebsite();
        loadCSV();
        loadPDF();
    }

    public void loadWebsite() {
        WebsiteLoader websiteLoader = new WebsiteLoader(webEngine);
        websiteLoader.addWebView(websiteSpace);
        websiteLoader.executeQuery(thisScreen.getInsertedWebsite());
    }


    public void loadPDF() {
        PDFLoader.setDestinationPathPDF(thisScreen.getDestinationPathPDF());
        PDFLoader.loadPDFViewer(spacePDF);
    }

    public void loadCSV() {
        CSVLoader.setDestinationPathCsv(thisScreen.getDestinationPathCSV());
        CSVLoader.drawCanvas(thisScreen.getDestinationPathCSV(), csvChart);
    }
}
