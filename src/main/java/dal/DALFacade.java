package dal;

import be.Users;
import dal.Database.DBConnector;
import dal.Database.dataAccess.UserDAO;
import dal.File.ScreenOperations;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public class DALFacade implements IDALFacade{

    private DBConnector dbConnector = new DBConnector();
    private UserDAO userDAO = new UserDAO();
    private static DALFacade dalFacade;


    public static DALFacade getInstance(){
        if(dalFacade==null)
            dalFacade = new DALFacade();
        return dalFacade;
    }

    private ScreenOperations screenOperations = new ScreenOperations();
    @Override
    public void saveFile(Path originPath, Path destinationPath) throws DALexception {
        screenOperations.saveFile(originPath, destinationPath);
    }

    @Override
    public String getHistogramData(Path destinationPath) throws DALexception {
        return screenOperations.getHistogramData(destinationPath);
    }


    //Users Delete, Add, Edit, all
    @Override
    public List<Users> getAllUser() throws DALexception {
        return userDAO.getAll();
    }

    @Override
    public void deleteUser(Users user) throws DALexception {
        userDAO.delete(user);
    }

    @Override
    public void updateUser(Users oldUser, Users newUser) throws DALexception {
        userDAO.update(oldUser,newUser);
    }

    @Override
    public void createUser(Users user) throws DALexception {
        userDAO.create(user);
    }




}
