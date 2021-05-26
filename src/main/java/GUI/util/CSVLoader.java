package gui.util;

import be.CSVInfo;
import gui.Model.ScreenModel;
import gui.Model.exception.ModelException;
import gui.util.charts.CreateHistogramChart;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CSVLoader {
    private final static String DESTINATION_PATH_CSV = "src/../Data/CSVData/";
    private static Path destinationPathCSV;
    private static Map<Node, CSVInfo> nodes = new HashMap<>();

    public static Path loadCSV(FileChooser fileChooser, Node node) {

        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (ValidateExtension.validateCSV(files.get(0))) {
            destinationPathCSV = Path.of(DESTINATION_PATH_CSV + files.get(0).
                    getName());
            FileSaver.saveFile(files.get(0), destinationPathCSV);
            getInfo(node);
            //drawCanvas(destinationPathCSV, csvChart);
           // return files.get(0).getName();
        } else {
            //repeat operation
        }
        return destinationPathCSV;
    }

    private static void getInfo(Node node) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setSpacing(10);
        hBox.setSpacing(10);
        RadioButton radioButton = new RadioButton();
        Label label = new Label("first row is a header");
        hBox.getChildren().addAll(radioButton, label);
        ComboBox<String> comboBox = new ComboBox<>();
        List<String> options = new ArrayList<>();
        options.add("barchart");
        options.add("linechart");
        options.add("table");
        comboBox.setItems(FXCollections.observableList(options));
        TextField textField = new TextField("chart title");
        Button button = new Button("load");
        button.setOnAction(event -> createChart(radioButton, comboBox, textField, node));
        vBox.getChildren().addAll(hBox, comboBox, textField, button);
        Stage stage = new Stage();
        stage.setScene(new Scene(vBox));
        stage.show();

    }

    private static void createChart(RadioButton radioButton, ComboBox<String> comboBox, TextField textField, Node node) {
        boolean isHeader = false;
        if(radioButton.isSelected()) isHeader = true;
        String title;
        if(textField.getText().equals(null) || textField.getText().equals("chart title")) title = "";
        else title = textField.getText();
        String chartType = comboBox.getSelectionModel().getSelectedItem();
        AnchorPane pane = (AnchorPane) node;
        switch (chartType) {
            case "linechart" -> {
                createLinechart(isHeader, title, pane);
                nodes.put(node, new CSVInfo(isHeader, title, CSVInfo.CSVType.LINECHART));
            }
            case "table" -> {
                createTable(isHeader, pane);
                nodes.put(node, new CSVInfo(isHeader, title, CSVInfo.CSVType.TABLE));
            }
            case "barchart" -> {
                createBarchart(isHeader, title, pane);
                nodes.put(node, new CSVInfo(isHeader, title, CSVInfo.CSVType.BARCHART));
            }
        }
    }

    public static void createBarchart(boolean isHeader, String title, Pane pane) {

        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);

        Object[] columnnames;
        CSVReader CSVFileReader;
        List<XYChart.Series> series = new ArrayList<>();

        try {
            CSVFileReader = new CSVReader(new FileReader(destinationPathCSV.toString()));
            List myEntries = CSVFileReader.readAll();
            columnnames = (String[]) myEntries.get(0);
            boolean ffirst = true;
            for(String c : (String[])columnnames) {
                if(isHeader) {
                    if (ffirst) xAxis.setLabel(c);
                    else {
                        XYChart.Series serie = new XYChart.Series();
                        serie.setName(c);
                        series.add(serie);
                    }
                } else {
                    if (ffirst) xAxis.setLabel("");
                    else {
                        XYChart.Series serie = new XYChart.Series();
                        series.add(serie);
                    }
                }
                ffirst = false;
            }

            boolean first;
            int i = -1;
            if(isHeader) i=0;
            for (int x = 0; x<myEntries.size(); x++)
            {
                if (x>i)
                {
                    first = true;
                    String xValue = "";
                    int s = 0;
                    for (String thisCellValue : (String[])myEntries.get(x))
                    {
                        thisCellValue.replace(" ", "");
                        if(first) xValue = thisCellValue;
                        else {
                            double yValue = Double.parseDouble(thisCellValue);
                            series.get(s).getData().add(new XYChart.Data(xValue, yValue));
                            s++;
                        }
                        first = false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        for(XYChart.Series s : series) barChart.getData().add(s);
        barChart.prefHeightProperty().bind(pane.heightProperty());
        barChart.prefWidthProperty().bind(pane.widthProperty());
        pane.getChildren().clear();
        pane.getChildren().add(barChart);
        //barChart.setPrefSize(pane.getWidth(), pane.getHeight());
    }

    public static void createLinechart(boolean isHeader, String title, Pane pane) {

        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);

        Object[] columnnames;
        CSVReader CSVFileReader;
        List<XYChart.Series> series = new ArrayList<>();

        try {
            CSVFileReader = new CSVReader(new FileReader(destinationPathCSV.toString()));
            List myEntries = CSVFileReader.readAll();
            columnnames = (String[]) myEntries.get(0);
            boolean ffirst = true;
            for(String c : (String[])columnnames) {
                if(isHeader) {
                    if (ffirst) xAxis.setLabel(c);
                    else {
                        XYChart.Series serie = new XYChart.Series();
                        serie.setName(c);
                        series.add(serie);
                    }
                } else {
                    if (ffirst) xAxis.setLabel("");
                    else {
                        XYChart.Series serie = new XYChart.Series();
                        series.add(serie);
                    }
                }
                ffirst = false;
            }
            boolean first;
            int i = -1;
            if(isHeader) i=0;
            for (int x = 0; x<myEntries.size(); x++)
            {
                if (x>i)
                {
                    first = true;
                    String xValue = "";
                    int s = 0;
                    for (String thisCellValue : (String[])myEntries.get(x))
                    {
                        thisCellValue.replace(" ", "");
                        if(first) xValue = thisCellValue;
                        else {
                            double yValue = Double.parseDouble(thisCellValue);
                            series.get(s).getData().add(new XYChart.Data(xValue, yValue));
                            s++;
                        }
                        first = false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        for(XYChart.Series s : series) lineChart.getData().add(s);
        pane.getChildren().clear();
        pane.getChildren().add(lineChart);
        //lineChart.setPrefSize(pane.getWidth(), pane.getHeight());

        lineChart.prefHeightProperty().bind(pane.heightProperty());
        lineChart.prefWidthProperty().bind(pane.widthProperty());
    }

    public static void createTable(boolean isHeader, Pane pane) {
        String[] columnNames;
        CSVReader CSVFileReader;
        DefaultTableModel tableModel = null;
        try {
            CSVFileReader = new CSVReader(new FileReader(destinationPathCSV.toString()));
            List myEntries = CSVFileReader.readAll();
            columnNames = (String[]) myEntries.get(0);
            tableModel = new DefaultTableModel(columnNames, myEntries.size());
            int rowcount = tableModel.getRowCount();
            for (int x = 0; x<rowcount; x++)
            {
                int columnnumber = 0;

                    for (String thiscellvalue : (String[])myEntries.get(x))
                    {
                        tableModel.setValueAt(thiscellvalue, x, columnnumber);
                        columnnumber++;
                    }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        JTable myJTable = new JTable(tableModel);
        myJTable.setShowGrid(true);
        myJTable.setGridColor(Color.LIGHT_GRAY);

        SwingNode sn = new SwingNode();
        sn.setContent(myJTable);
        ScrollPane scrollPane = new ScrollPane();
        //scrollPane.setPrefSize(pane.getWidth(), pane.getHeight());
        scrollPane.prefHeightProperty().bind(pane.heightProperty());
        scrollPane.prefWidthProperty().bind(pane.widthProperty());
        scrollPane.setContent(sn);
        //sn.minWidth(pane.getWidth());
        //sn.prefWidth(pane.getWidth());
        pane.getChildren().add(scrollPane);
    }


    public static void drawCanvas( Path destinationPath, Pane csvChart) {
        CreateHistogramChart createHistogramChart =
                new CreateHistogramChart(getHistogramData(destinationPath));
        ChartCanvas canvas = new ChartCanvas(createHistogramChart.createChart());
        csvChart.getChildren().add(canvas);
        canvas.widthProperty().bind(csvChart.widthProperty());
        canvas.heightProperty().bind(csvChart.heightProperty());
    }

    public static Path getDestinationPathCSV(){
        return destinationPathCSV;
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

    public static void setDestinationPathCsv(Path path) {
        destinationPathCSV = path;
    }

    public static void clearMap() {
        nodes.clear();
    }

    public static Map<Node, CSVInfo> getMap() {
        return nodes;
    }
}
