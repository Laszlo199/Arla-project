package bll;

import bll.exception.BLLException;

import java.nio.file.Path;

/**
 *
 */
public interface IFiles {
    void saveFile(Path originPath, Path destinationPath) throws BLLException;
    double[] getHistogramData(Path destinationPath) throws BLLException;
    String getHTML(Path pdfPath) throws BLLException;
    void deletePDFfiles(Path destinationPathPDF) throws BLLException;

    void deleteCSV(Path destinationPathCSV) throws BLLException;
}
