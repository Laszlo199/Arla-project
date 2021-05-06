package GUI.util;

import GUI.Model.ScreenModel;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

/**
 *
 */
public class PDFLoader {
    private static Path destinationPathPDF;
    private final static String DESTINATION_PATH_PDF = "src/../Data/PDFData/";

    /**
     * method at first opens file chooser then pdf is saved
     * we get html and display it in newly created Web view
     * that is dynamically added to the stage
     *
     * @param actionEvent
     */
    public static String loadPDF(ActionEvent actionEvent, FileChooser fileChooser){
        File selectedFile = ChooseFile.getSelectedFile(actionEvent, "Choose PDF file",
                fileChooser);
        if (ValidateExtension.validatePDF(selectedFile)) {
           //attachment3.setText(selectedFile.getName());
            destinationPathPDF = Path.of(DESTINATION_PATH_PDF + selectedFile.getName());
            FileSaver.saveFile(selectedFile, destinationPathPDF);
            return selectedFile.getName();
        }
        return new String("");
    }

    public static void loadPDFPreview() {
        File selectedFile ;

    }
    public static void loadPDFViewer(Pane spacePDF){
        String htmlPath = ScreenModel.getInstance().getHTML(destinationPathPDF);
        WebView webView = new WebView();
        WebEngine pdfViewerEngine = webView.getEngine();
        webView.prefHeightProperty().bind(spacePDF.heightProperty());
        webView.prefWidthProperty().bind(spacePDF.widthProperty());
        spacePDF.getChildren().add(webView);
        File f = new File(htmlPath);
        pdfViewerEngine.load(f.toURI().toString());
    }
    public static Path getDestinationPathPDF(){
        return destinationPathPDF;
    }

    public static void setDestinationPathPDF(Path path) {
        destinationPathPDF = path;
    }


}
