package dal.Database.dataAccess;

import be.DefaultScreen;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ScreenDAO {
    private DBConnector dbConnector;

    public ScreenDAO() {
        dbConnector = new DBConnector();
    }

    /**
     * method adds template to database
     * @param defaultTemplate
     */
    public void saveDefaultTemplate(DefaultScreen defaultTemplate) throws DALexception {
        String sql =  "INSERT INTO DefaultTemplates( [name], destinationPathCSV, " +
                "destinationPathPDF, insertedWebsite) VALUES(?, ?, ?, ?);";
        try(Connection connection = dbConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, defaultTemplate.getName());
            preparedStatement.setString(2, defaultTemplate.getDestinationPathCSV().toString());
            preparedStatement.setString(3, defaultTemplate.getDestinationPathPDF().toString());
            preparedStatement.setString(4, defaultTemplate.getInsertedWebsite());
            preparedStatement.executeUpdate();
        } catch (SQLServerException throwables) {
            throw new DALexception("Whoops...Couldn't save new default template");
        } catch (SQLException throwables) {
            throw new DALexception("Whoops...Couldn't save new default template");
        }
    }

    /**
     * method retrieves all screens from Default templates table
     * @return
     */
    public List<DefaultScreen> getAllDefaultScreens() throws DALexception {
        List<DefaultScreen> defaultScreens= new ArrayList<>();
        String sql = "SELECT * FROM DefaultTemplates";
        try(Connection connection = dbConnector.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String destinationPathCSV = resultSet.getString(3);
                String destinationPathPDF = resultSet.getString(4);
                String insertedWebsite = resultSet.getString(5);
                defaultScreens.add(new DefaultScreen(id, name, Path.of(destinationPathCSV),
                        Path.of(destinationPathPDF), insertedWebsite));
            }
            return defaultScreens;
        } catch (SQLServerException throwables) {
            throw new DALexception("Whoops...Couldn't get all screens");
        } catch (SQLException throwables) {
            throw new DALexception("Whoops...Couldn't get all screens");
        }
    }

    public void deleteScreen(DefaultScreen screen) throws DALexception {
        String sql = "DELETE FROM DefaultTemplates WHERE id=?";
        try(Connection con = dbConnector.getConnection()) {
            PreparedStatement pstat = con.prepareStatement(sql);
            pstat.setInt(1, screen.getId());
            pstat.executeUpdate();
        } catch (SQLServerException throwables) {
            throw new DALexception("Whoops...Couldn't delete screen");
        } catch (SQLException throwables) {
            throw new DALexception("Whoops...Couldn't delete screen");
        }
    }

    public void updateScreen(int id, DefaultScreen screen) throws DALexception {
        String sql = "UPDATE DefaultTemplates SET [name]=?, destinationPathCSV=?, " +
                "destinationPathPDF=?, insertedWebsite=? WHERE id=?";
        try(Connection con = dbConnector.getConnection()) {
            PreparedStatement pstat = con.prepareStatement(sql);
            pstat.setString(1, screen.getName());
            pstat.setString(2, screen.getDestinationPathCSV().toString());
            pstat.setString(3, screen.getDestinationPathPDF().toString());
            pstat.setString(4, screen.getInsertedWebsite());
            pstat.setInt(5, id);
            pstat.executeUpdate();
        } catch (SQLServerException throwables) {
            throw new DALexception("Whoops...Couldn't update screen");
        } catch (SQLException throwables) {
            throw new DALexception("Whoops...Couldn't update screen");
        }
    }
}
