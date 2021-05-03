package GUI.Model;

import GUI.Model.exception.ModelException;
import be.DefaultScreen;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class ScreenModel {
    private IFacade logic = new Facade();
    private static ScreenModel screenModel;

    private ScreenModel() {
    }

    public static ScreenModel getInstance(){
        if(screenModel==null)
            screenModel = new ScreenModel();
        return screenModel;
    }


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

    public void saveDefaultTemplate(DefaultScreen defaultTemplate) {
        try {
            logic.saveDefaultTemplate(defaultTemplate);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void deletePDFfiles(Path destinationPathPDF) {
        try {
            logic.deletePDFfiles(destinationPathPDF);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCSV(Path destinationPathCSV) {
        try {
            logic.deleteCSV(destinationPathCSV);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

    public List<DefaultScreen> getAllDefaultScreens() throws ModelException {
        try {
            return logic.getAllDefaultScreens();
        } catch (BLLException e) {
            throw new ModelException("Couldnt get all default screens", e);
        }
    }
}
