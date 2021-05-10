package GUI.Model;

import be.DefaultScreen;
import be.ScreenElement;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClientModel {

    private IFacade logic = new Facade();
    private static ClientModel instance;

    private ClientModel() {
    }

    public static ClientModel getInstance(){
        if(instance==null)
            instance = new ClientModel();
        return instance;
    }

    public List<ScreenElement> getSections(int userId) {
        try {
            return logic.getScreenForUser(userId);
        } catch (BLLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
