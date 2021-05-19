package gui.Controller;

import gui.util.CSVLoader;
import gui.util.PDFLoader;
import gui.util.WebsiteLoader;
import be.DefaultScreen;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;

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
