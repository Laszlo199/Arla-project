package gui.util;

import gui.Model.ScreenModel;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

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
     * @param
     */
    public static  Path loadPDF(FileChooser fileChooser){
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if(!files.isEmpty()) {
            if (ValidateExtension.validatePDF(files.get(0))) {
                destinationPathPDF = Path.of(DESTINATION_PATH_PDF + files.get(0).getName());
                FileSaver.saveFile(files.get(0), destinationPathPDF);
            }
        }
        return destinationPathPDF;
    }

    public static void loadPDFPreview() {
        File selectedFile ;
    }

    public static  void loadPDFViewer(Pane spacePDF){
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
