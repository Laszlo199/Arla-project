package GUI.util;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

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
        try {
            FileInputStream inputStream = new FileInputStream(destinationPathXLSX.toString());

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();

            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue());
                            break;
                    }
                    System.out.print(" - ");
                }
                System.out.println();
            }

            workbook.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
