package GUI.Model;

import be.User;
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

    public User getUser(String username) {
        try {
            return iFacade.getUser(username);
        } catch (BLLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePassword(User oldUser,String newPassword){
        try {
            iFacade.updatePassword(oldUser, newPassword);
        } catch (BLLException e) {
            e.printStackTrace();
        }
    }
}
