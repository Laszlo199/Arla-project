package bll;

import be.DefaultTemplate;
import bll.exception.BLLException;

import java.net.URL;
import java.nio.file.Path;

/**
 *
 */
public interface IScreen {
    void saveFile(Path originPath, Path destinationPath) throws BLLException;

    double[] getHistogramData(Path destinationPath) throws BLLException;

    String getHTML(Path pdfPath) throws BLLException;

    void saveDefaultTemplate(DefaultTemplate defaultTemplate) throws BLLException;

    void deletePDFfiles(Path destinationPathPDF) throws BLLException;

    void deleteCSV(Path destinationPathCSV) throws BLLException;
}
