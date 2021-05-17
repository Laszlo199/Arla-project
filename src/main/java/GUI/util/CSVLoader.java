package GUI.util;

import GUI.Model.ScreenModel;
import GUI.Model.exception.ModelException;
import GUI.util.charts.CreateHistogramChart;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CSVLoader {
    private final static String DESTINATION_PATH_CSV = "src/../Data/CSVData/";
    private static Path destinationPathCSV;

    /**
     * we need file chooser, maybe i should save that file
     * then we need double[] and then
     * we can use CreateHistogramChart
     *
     */
    public static String loadCSV(FileChooser fileChooser, Pane pane) {

        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (ValidateExtension.validateCSV(files.get(0))) {
            destinationPathCSV = Path.of(DESTINATION_PATH_CSV + files.get(0).
                    getName());
            FileSaver.saveFile(files.get(0), destinationPathCSV);
            getInfo(pane);
            //drawCanvas(destinationPathCSV, csvChart);
            return files.get(0).getName();
        } else {
            //repeat operation
        }
        return new String("");
    }

    private static void getInfo(Pane pane) {
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
        button.setOnAction(event -> createChart(radioButton, comboBox, textField, pane));
        vBox.getChildren().addAll(hBox, comboBox, textField, button);
        Stage stage = new Stage();
        stage.setScene(new Scene(vBox));
        stage.show();

    }

    private static void createChart(RadioButton radioButton, ComboBox<String> comboBox, TextField textField, Pane pane) {
        boolean isHeader = false;
        if(radioButton.isSelected()) isHeader = true;
        String title;
        if(textField.getText().equals(null) || textField.getText().equals("chart title")) title = "";
        else title = textField.getText();
        String chartType = comboBox.getSelectionModel().getSelectedItem();
        switch (chartType) {
            case "linechart" -> createLinechart(isHeader, title, pane);
            case "table" -> createTable(isHeader, pane);
            case "barchart" -> createBarchart(isHeader, title, pane);
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
        pane.getChildren().clear();
        pane.getChildren().add(barChart);
        barChart.setPrefSize(pane.getWidth(), pane.getHeight());
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
        lineChart.setPrefSize(pane.getWidth(), pane.getHeight());
    }

    public static void createTable(boolean isHeader, Pane pane) {
        System.out.println(isHeader);
        String[] columnNames;
        CSVReader CSVFileReader;
        DefaultTableModel tableModel = null;
        try {
            CSVFileReader = new CSVReader(new FileReader(destinationPathCSV.toString()));
            List myEntries = CSVFileReader.readAll();
            columnNames = (String[]) myEntries.get(0);
            if(!isHeader) for(String s : columnNames) s="";
            tableModel = new DefaultTableModel(columnNames, myEntries.size()-1);
            int rowcount = tableModel.getRowCount();
            for (int x = 0; x<rowcount+1; x++)
            {
                int columnnumber = 0;

                if (x>0)
                {
                    for (String thiscellvalue : (String[])myEntries.get(x))
                    {
                        tableModel.setValueAt(thiscellvalue, x-1, columnnumber);
                        columnnumber++;
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

        JTable myJTable = new JTable(tableModel);
        myJTable.setShowGrid(true);
        myJTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = myJTable.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);

        //pane.getChildren().add(myJTable);
        //myJTable.setSize(pane.getWidth(), pane.getHeight());

        JFrame frame = new JFrame("table");
        frame.add(new JScrollPane(myJTable));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

}
