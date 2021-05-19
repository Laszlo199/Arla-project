package GUI.util;

import javafx.embed.swing.SwingNode;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ExcelLoader {
    private final static String DESTINATION_PATH_XLSX = "src/../Data/XLSXData/";
    private static Path destinationPathXLSX;

    public static void loadXLSX(FileChooser fileChooser, AnchorPane anchorPane) {

        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (ValidateExtension.validateXLSX(files.get(0))) {
            destinationPathXLSX = Path.of(DESTINATION_PATH_XLSX + files.get(0).
                    getName());
            FileSaver.saveFile(files.get(0), destinationPathXLSX);

            showExcel(anchorPane);

        }

    }

    private static void showExcel(AnchorPane anchorPane) {
        DefaultTableModel tableModel = null;
        try {
            FileInputStream inputStream = new FileInputStream(destinationPathXLSX.toString());

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();

            String[] columnNames = new String[firstSheet.getRow(0).getPhysicalNumberOfCells()];
            for(String s : columnNames) s="";

            int row = 0;
            int col = 0;
            tableModel = new DefaultTableModel(columnNames, firstSheet.getPhysicalNumberOfRows());
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                col=0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            tableModel.setValueAt(cell.getStringCellValue(), row, col);
                            break;
                            case Cell.CELL_TYPE_NUMERIC:
                                tableModel.setValueAt(cell.getNumericCellValue(), row, col);
                                break;
                    }
                    col++;
                }
                row++;
            }

            workbook.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTable myJTable = new JTable(tableModel);
        myJTable.setShowGrid(true);
        myJTable.setGridColor(Color.LIGHT_GRAY);

        SwingNode sn = new SwingNode();
        sn.setContent(myJTable);
        javafx.scene.control.ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(anchorPane.getWidth(), anchorPane.getHeight());
        scrollPane.setContent(sn);
        anchorPane.getChildren().add(scrollPane);
    }
}
