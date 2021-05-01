package bll;

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
}
