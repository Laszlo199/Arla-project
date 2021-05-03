package dal.File;

import dal.exception.DALexception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 *
 */
public class ScreenOperations {
    public void saveFile(Path originPath, Path destinationPath) throws DALexception {
        try {
            Files.copy(originPath, destinationPath, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DALexception("Couldn't save file ", e);
        }
    }

    public String getHistogramData(Path destinationPath) throws DALexception {
        try {
            return Files.readString(destinationPath);
        } catch (IOException e) {
            throw new DALexception("Couldn't get data ", e);
        }
    }


    public void deletePDFfiles(Path destinationPathPDF) throws DALexception {
        String html = destinationPathPDF.toString().replaceFirst("PDFData","HTMLData").
                replaceAll(".pdf", ".html");
        try {
            Files.delete(destinationPathPDF);
            Files.delete(Path.of(html));
        } catch (IOException e) {
            throw new DALexception("Couldn't delete PDF and HTML file ", e);
        }
    }

    public void deleteCSV(Path destinationPathCSV) throws DALexception {
        try {
            Files.delete(destinationPathCSV);
        } catch (IOException e) {
            throw new DALexception("Couldn't delete CSV file ", e);
        }
    }
}
