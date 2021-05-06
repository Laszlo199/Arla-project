package bll;

import be.DefaultScreen;
import bll.exception.BLLException;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public interface IScreen {
    void saveFile(Path originPath, Path destinationPath) throws BLLException;

    double[] getHistogramData(Path destinationPath) throws BLLException;

    String getHTML(Path pdfPath) throws BLLException;

    void saveDefaultTemplate(DefaultScreen defaultTemplate) throws BLLException;

    void deletePDFfiles(Path destinationPathPDF) throws BLLException;

    void deleteCSV(Path destinationPathCSV) throws BLLException;

    List<DefaultScreen> getAllDefaultScreens() throws BLLException;

    void deleteScreen(DefaultScreen currentScreen) throws BLLException;
}
