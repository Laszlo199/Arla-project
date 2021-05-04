package dal.Database.dataAccess;

import be.Users;
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


}
