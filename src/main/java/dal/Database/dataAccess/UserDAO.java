package dal.Database.dataAccess;

import be.CSVInfo;
import be.ScreenElement;
import be.User;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

   // private DBConnector dbConnector;

    public UserDAO() {
       // dbConnector = new DBConnector();
    }


    public List<User> getAll(Connection connection) throws DALexception {
        List<User> users = new ArrayList<>();
        try ( Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM Users";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("Password");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                boolean isReset = resultSet.getBoolean("isReset");
                users.add(new User(id, userName, password,isAdmin,isReset));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops");
        }
        return users;
    }

    public void delete(User user, Connection connection) throws DALexception {
        String sql = "DELETE FROM Users WHERE ID=?";
        try (PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, user.getID());
            pStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops...Couldn't delete an User");
        }
    }

    public void update(User oldUser, User newUser, Connection connection) throws DALexception {
        String sql = "UPDATE Users SET userName=?, isAdmin=? WHERE ID=?";
        try (PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, newUser.getUserName());
            //pStatement.setString(2, newUser.getPassword());
            pStatement.setBoolean(2, newUser.isAdmin());
            pStatement.setInt(3, oldUser.getID());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops..Couldn't update an User");
        }
    }

    public void create(User user, Connection connection) throws DALexception {
        String sql = "INSERT INTO Users(userName, Password, isAdmin, isReset) VALUES(?,?,?,?)";
        try ( PreparedStatement pStatement = connection.prepareStatement(sql);) {
            pStatement.setString(1, user.getUserName());
            pStatement.setString(2, user.getPassword());
            pStatement.setBoolean(3, user.isAdmin());
            pStatement.setBoolean(4, user.isReset());
            //pStatement.setString(4, user.getPassword());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops...Couldn't create an User");
        }
    }


    // I updated: laszlo
    public List<ScreenElement> getScreenForUser(int userId, Connection connection) throws DALexception {
        List<ScreenElement> sections = new ArrayList<>();
        String sql = "SELECT s.* " +
                "FROM Sections s, UsersAndScreens u " +
                "WHERE s.screenID = u.ScreenID AND u.UserID = ?";
        try ( PreparedStatement pstat = connection.prepareStatement(sql)) {
            pstat.setInt(1, userId);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
                int colIndex = resultSet.getInt("colIndex");
                int rowIndex = resultSet.getInt("rowIndex");
                int columnSpan = resultSet.getInt("columnSpan");
                int rowSpan = resultSet.getInt("rowSpan");
                String filepath = resultSet.getString("filepath");
                boolean isHeader = resultSet.getBoolean("isHeader");
                String title = resultSet.getString("title");
                String type = resultSet.getString("CSVType");

                if(title==null || type==null)
                    sections.add(
                            new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath));
                else {
                    switch (type) {
                        case "LINECHART" -> sections.add(
                                new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                        new CSVInfo(isHeader, title, CSVInfo.CSVType.LINECHART)));
                        case "BARCHART" ->  sections.add(
                                new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                        new CSVInfo(isHeader, title, CSVInfo.CSVType.BARCHART)));
                        case "TABLE" -> sections.add(
                                new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                        new CSVInfo(isHeader, title, CSVInfo.CSVType.TABLE)));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Couldn't get screen sections for a user");
        }

        return sections;
    }

    //Had to update, database has changed
    //updated: removed screenID and added a method to get all screens for userID. getUSer works the same.
    public User getUser(String username, Connection connection) throws DALexception {
        User user = null;
        String sql = "SELECT * FROM Users WHERE userName=?";
        try (PreparedStatement pstat = connection.prepareStatement(sql)) {
            pstat.setString(1, username);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("Password");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                boolean isReset = resultSet.getBoolean("isReset");
                user = new User(id, userName, password, isAdmin, isReset);

            }
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }


    public List<Integer> screensOfUser(int userID, Connection connection) throws DALexception{
        List<Integer> screens = new ArrayList<Integer>();
        String sql = "SELECT screenID FROM UsersAndScreens WHERE userID=?";
        try (PreparedStatement pstat = connection.prepareStatement(sql)) {

            pstat.setInt(1, userID);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
                screens.add(resultSet.getInt("screenID"));
            }
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return screens;
    }


    public void resetPassword(User oldUser,User reset, Connection connection) throws DALexception {
        String sql = "UPDATE Users SET Password =?, isReset = ? WHERE ID=?";
        try ( PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, reset.getPassword());
            pStatement.setBoolean(2, reset.isReset());
            pStatement.setInt(3, oldUser.getID());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops..Couldn't reset an User");
        }
    }

    public void updatePassword(User oldUser,String newPassword, Connection connection) throws DALexception{
        String sql = "UPDATE Users SET Password=?, isReset=? WHERE ID=?";
        try (PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setString(1, newPassword);
            pStatement.setBoolean(2, false);
            pStatement.setInt(3, oldUser.getID());
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops..Couldn't reset an User");
        }
    }

    public List<String> getUsersForScreen(int screenId, Connection connection) throws DALexception {
        String sql = "SELECT u.userName " +
                "FROM Users u, UsersAndScreens s " +
                "WHERE s.UserID = u.ID AND s.ScreenID = ?";
        List<String> usernames = new ArrayList<>();
        try(PreparedStatement pstat = connection.prepareStatement(sql)){

            pstat.setInt(1, screenId);
            ResultSet rs = pstat.executeQuery();
            while(rs.next()) {
                String username = rs.getString(1);
                usernames.add(username);
            }
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
            throw new DALexception("couldn't get users for screen");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usernames;
    }

    public void updateAssignedUsers(int screenID, List<User> selectedUsers, Connection connection) throws DALexception {
        String sql1 = "DELETE FROM UsersAndScreens WHERE ScreenID=?";
        try (PreparedStatement pstat = connection.prepareStatement(sql1)) {
            pstat.setInt(1, screenID);
            pstat.executeUpdate();

            String sql = "INSERT INTO UsersAndScreens(UserID,ScreenID) Values(?, ?)";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            for(User user : selectedUsers) {
                pStatement.setInt(1, user.getID());
                pStatement.setInt(2, screenID);
                pStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("could not update assigned users");
        }
    }
}
