package gui.Model;

import be.ScreenElement;
import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;

import java.util.List;

public class ClientModel {

    private IFacade logic;

    {
        try {
            logic = Facade.getInstance();
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }

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
