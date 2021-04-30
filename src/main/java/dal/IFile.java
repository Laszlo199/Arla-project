package dal;

import dal.exception.DALexception;

import java.nio.file.Path;

/**
 *
 */
public interface IFile {
    void saveFile(Path originPath, Path destinationPath) throws DALexception;

    String getHistogramData(Path destinationPath) throws DALexception;
}
