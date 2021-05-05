package GUI.util;

import GUI.Model.ScreenModel;
import GUI.Model.exception.ModelException;
import GUI.util.charts.CreateHistogramChart;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;

/**
 *
 */
public class CSVLoader {
    private final static String DESTINATION_PATH_CSV = "src/../Data/CSVData/";
    //private static Path destinationPathCSV;

    /**
     * we need file chooser, maybe i should save that file
     * then we need double[] and then
     * we can use CreateHistogramChart
     *
     * @param actionEvent
     */
    public static String loadCSV(ActionEvent actionEvent, FileChooser fileChooser,
                                 Pane csvChart) {
        File selectedFile = ChooseFile.getSelectedFile(actionEvent, "Choose csv file",
                fileChooser);
        if (ValidateExtension.validateCSV(selectedFile)) {
           // attachment1.setText(selectedFile.getName());
           Path destinationPathCSV = Path.of(DESTINATION_PATH_CSV + selectedFile.
                    getName());
            FileSaver.saveFile(selectedFile, destinationPathCSV);
            drawCanvas(destinationPathCSV, csvChart);
            return selectedFile.getName();
        } else {
            //repeat operation
        }
        return new String("");
    }



    private static void drawCanvas( Path destinationPath, Pane csvChart) {
        CreateHistogramChart createHistogramChart =
                new CreateHistogramChart(getHistogramData(destinationPath));
        ChartCanvas canvas = new ChartCanvas(createHistogramChart.createChart());
        csvChart.getChildren().add(canvas);
        canvas.widthProperty().bind(csvChart.widthProperty());
        canvas.heightProperty().bind(csvChart.heightProperty());
    }

    /**
     * get data from selected file
     *
     * @return
     */
    private static double[] getHistogramData(Path destinationPath) {
        try {
            return ScreenModel.getInstance().getHistogramData(destinationPath);
        } catch (ModelException e) {
            e.printStackTrace();
            AlertDisplayer.displayInformationAlert("getting data..",
                    "Couldnt get data", "");
        }
        return new double[0];
    }

}
