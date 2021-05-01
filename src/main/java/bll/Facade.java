package bll;

import be.Users;
import bll.exception.BLLException;
import dal.DALFacade;
import dal.Database.dataAccess.UserDAO;
import dal.IDALFacade;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class Facade implements IFacade{
    private IDALFacade facade = new DALFacade();
    private DiagramOperations diagramOperations = new DiagramOperations();
    private static Facade facadeBLL;



    public static IFacade getInstance(){
        if(facadeBLL==null)
            facadeBLL = new Facade();
        return facadeBLL;
    }
    

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

        // Users add, delete and all
    @Override
    public List<Users> getAllUser() throws BLLException {
        try {
            return facade.getAllUser();
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't gat all Users");
        }
    }

    @Override
    public void deleteUser(Users user) throws BLLException {
        try {
            facade.deleteUser(user);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't deleted User");
        }
    }

    @Override
    public void updateUser(Users oldUser, Users newUser) throws BLLException {
        try {
            facade.updateUser(oldUser,newUser);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't updated User");
        }
    }

    @Override
    public void createUser(Users user) throws BLLException {
        try {
            facade.createUser(user);
        }catch (DALexception daLexception){
            daLexception.printStackTrace();
            throw new BLLException("Whoops..Couldn't create User");
        }
    }
}
