package dal.Database.dataAccess;

import be.ScreenElement;
import be.User;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DBConnector dbConnector;

    public UserDAO() {
        dbConnector = new DBConnector();
    }


    public List<User> getAll() throws DALexception {
        List<User> users = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("Password");
                int screenId = resultSet.getInt("screenID");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                users.add(new User(id, userName, password, screenId, isAdmin));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops");
        }
        return users;
    }

    public void delete(User user) throws DALexception {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "DELETE FROM Users WHERE ID=?";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, user.getID());
            pStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops...Couldn't delete an User");
        }
    }

    public void update(User oldUser, User newUser) throws DALexception {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "UPDATE Users SET userName=?,Password=?,screenId=?, isAdmin=? WHERE ID=?";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, newUser.getUserName());
            pStatement.setString(2, newUser.getPassword());
            pStatement.setInt(3, newUser.getScreenId());
            pStatement.setBoolean(4, newUser.isAdmin());
            pStatement.setInt(5, oldUser.getID());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops..Couldn't update an User");
        }
    }

    public void create(User user) throws DALexception {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "INSERT INTO Users(userName,Password,screenId,isAdmin) VALUES(?,?,?,?)";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, user.getUserName());
            pStatement.setString(2, user.getPassword());
            pStatement.setInt(3, user.getScreenId());
            pStatement.setBoolean(4, user.isAdmin());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops...Couldn't create an User");
        }
    }

    public List<ScreenElement> getScreenForUser(int userId) throws DALexception {
        List<ScreenElement> sections = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT s.* " +
                    "FROM Sections s, Users u " +
                    "WHERE s.screenID = u.screenID AND u.ID = ?";
            PreparedStatement pstat = connection.prepareStatement(sql);
            pstat.setInt(1, userId);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
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

    public User getUser(String username) throws DALexception {
        User user = null;
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Users WHERE userName=?";
            PreparedStatement pstat = connection.prepareStatement(sql);
            pstat.setString(1, username);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("Password");
                int screenId = resultSet.getInt("screenID");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                user = new User(id, userName, password, screenId, isAdmin);
            }
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
}
