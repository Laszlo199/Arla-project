package GUI.Model;

import bll.Facade;
import bll.IFacade;
import bll.exception.BLLException;

public class LoginModel {
    private IFacade iFacade;


    public LoginModel() {
        this.iFacade = Facade.getInstance();
    }

    public boolean validate(String password){
        try {
            iFacade.validate(password);
        }catch (BLLException e){
            e.printStackTrace();
        }
        return true;
    }
}
