package dal.Database.dataAccess;

import be.ScreenElement;
import be.Users;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DBConnector dbConnector;

    public UserDAO(){
        dbConnector = new DBConnector();
    }



    public List<Users> getAll()throws DALexception {
        List<Users> users = new ArrayList<>();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("Password");
                users.add(new Users(id, userName, password));
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new DALexception("Whoops");
        }
        return users;
    }

    public void delete(Users user)throws DALexception{
        try (Connection connection = dbConnector.getConnection()){
            String sql = "DELETE FROM Users WHERE ID=?";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1,user.getID());
            pStatement.executeUpdate();

        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw  new DALexception("Whoops...Couldn't delete an User");
        }
    }

    public void update(Users oldUser, Users newUser) throws DALexception{
        try(Connection connection = dbConnector.getConnection()){
            String sql = "UPDATE Users SET userName=?,Password=? WHERE ID=?";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1,newUser.getUserName());
            pStatement.setString(2,newUser.getPassword());
            pStatement.setInt(3,oldUser.getID());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops..Couldn't update an User");
        }
    }

    public void create(Users user) throws DALexception{
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "INSERT INTO Users(userName,Password) VALUES(?,?)";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, user.getUserName());
            pStatement.setString(2,user.getPassword());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops...Couldn't create an User");
        }
    }

    public List<ScreenElement> getScreenForUser(int userId) throws DALexception {
        List<ScreenElement> sections = new ArrayList<>();
        try(Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT s.* " +
                    "FROM Sections s, Screens sc " +
                    "WHERE s.screenID = sc.id AND sc.userID = ?";
            PreparedStatement pstat = connection.prepareStatement(sql);
            pstat.setInt(1, userId);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()){
                int colIndex = resultSet.getInt("colIndex");
                int rowIndex = resultSet.getInt("rowIndex");
                int colSpan = resultSet.getInt("columnSpan");
                int rowSpan = resultSet.getInt("rowSpan");
                String filepath = resultSet.getString("filepath");
                sections.add(new ScreenElement(colIndex, rowIndex, colSpan, rowSpan, filepath));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Couldn't get screen sections for a user");
        }

        return sections;
    }


}
