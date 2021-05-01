package GUI.Model;

import GUI.Model.exception.ModelException;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;

import java.net.URL;
import java.nio.file.Path;

/**
 *
 */
public class ScreenModel {
    private IFacade logic = new Facade();
    public void saveFile(Path originPath, Path destinationPath) throws ModelException {
        try {
            logic.saveFile(originPath, destinationPath);
        } catch (BLLException e) {
            throw new ModelException("couldn't save", e);
        }
    }

    public double[] getHistogramData(Path destinationPath) throws ModelException {
        try {
            return logic.getHistogramData(destinationPath);
        } catch (BLLException e) {
            throw new ModelException("couldnt get data", e);
        }
    }

    public String getHTML(Path pdfPath) {
        try {
            return logic.getHTML(pdfPath);
        } catch (BLLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
