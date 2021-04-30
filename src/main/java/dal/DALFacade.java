package dal;

import dal.File.ScreenOperations;
import dal.exception.DALexception;

import java.nio.file.Path;

/**
 *
 */
public class DALFacade implements IDALFacade{
    private ScreenOperations screenOperations = new ScreenOperations();
    @Override
    public void saveFile(Path originPath, Path destinationPath) throws DALexception {
        screenOperations.saveFile(originPath, destinationPath);
    }

    @Override
    public String getHistogramData(Path destinationPath) throws DALexception {
        return screenOperations.getHistogramData(destinationPath);
    }
}
