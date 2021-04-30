package bll;

import bll.exception.BLLException;
import dal.DALFacade;
import dal.IDALFacade;
import dal.exception.DALexception;

import java.nio.file.Path;

/**
 *
 */
public class Facade implements IFacade{
    private IDALFacade facade = new DALFacade();
    private DiagramOperations diagramOperations = new DiagramOperations();
    

    @Override
    public void saveFile(Path originPath, Path destinationPath) throws BLLException {
        try {
            facade.saveFile(originPath, destinationPath);
        } catch (DALexception daLexception) {
            throw new BLLException("couldnt save", daLexception);
        }
    }

    @Override
    public double[] getHistogramData(Path destinationPath) throws BLLException {
        String string = null;
        try {
            string = facade.getHistogramData(destinationPath);
        } catch (DALexception daLexception) {
            throw new BLLException("Couldnt get data", daLexception);
        }
        return diagramOperations.getArray(string);
    }
}
