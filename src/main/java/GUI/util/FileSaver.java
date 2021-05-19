package gui.util;

import gui.Model.ScreenModel;
import gui.Model.exception.ModelException;

import java.io.File;
import java.nio.file.Path;

/**
 *
 */
public class FileSaver {
    public static void saveFile(File selectedFile, Path destinationPath){
        try {
            ScreenModel.getInstance().saveFile(Path.of(selectedFile.getAbsolutePath()),
                    destinationPath);
        } catch (ModelException e) {
            e.printStackTrace();
            AlertDisplayer.displayInformationAlert("saving",
                    "couldn't save", "");
        }
    }

}
