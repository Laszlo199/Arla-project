package bll;

import bll.exception.BLLException;

import java.nio.file.Path;

/**
 *
 */
public interface IScreen {
    void saveFile(Path originPath, Path destinationPath) throws BLLException;

    double[] getHistogramData(Path destinationPath) throws BLLException;
}
