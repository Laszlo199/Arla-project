package dal.Database.dataAccess;

import GUI.util.Command.Command;
import be.DefaultScreen;
import be.Screen;
import be.ScreenElement;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.TRANSACTION_NONE;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

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
        try(Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, defaultTemplate.getName());
            preparedStatement.setString(2, defaultTemplate.getDestinationPathCSV().toString());
            preparedStatement.setString(3, defaultTemplate.getDestinationPathPDF().toString());
            preparedStatement.setString(4, defaultTemplate.getInsertedWebsite());
            preparedStatement.executeUpdate();

            //set proper id for that movie
            try(ResultSet generatedKey = preparedStatement.getGeneratedKeys()) {
                if(generatedKey.next())
                {
                    defaultTemplate.setId(generatedKey.getInt(1));
                }
                else{
                    throw new DALexception("Couldn't get generated key");
                }
            }

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

    /**
     * here will happen all the magic. At first we save the screen, get its id and
     * then save sections adding screen ids
     * @param screen
     * @param screenElements
     */
    public void save(Screen screen, List<ScreenElement> screenElements) throws DALexception {
        int screenID = -1;
        String query1 = "INSERT INTO Screens(?, ?, ?) Values(?, ?, ?);";
        String query2 = "INSERT INTO Sections(screenID, colIndex, rowIndex" +
                " , columnSpan, rowSpan, filepath) Values(?, ?, ?, ?, ?, ?);";

        try(Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStat1 = connection.prepareStatement(query1,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement = connection.prepareStatement(query2,
                    Statement.RETURN_GENERATED_KEYS))
        {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(TRANSACTION_REPEATABLE_READ);
            preparedStat1.setString(1, screen.getName());
            preparedStat1.executeUpdate();

            //set proper id for that movie
            try(ResultSet generatedKey = preparedStatement.getGeneratedKeys()) {
                if(generatedKey.next())
                    screenID = generatedKey.getInt(1);
                else
                    throw new DALexception("Couldn't get generated key");
            }

            for(ScreenElement element: screenElements){
                preparedStatement.setInt(1, screenID);
                preparedStatement.setInt(2, element.getColIndex());
                preparedStatement.setInt(3, element.getRowIndex());
                preparedStatement.setInt(4, element.getColSpan());
                preparedStatement.setInt(5, element.getRowSpan());
                preparedStatement.setString(6, element.getFilepath());
                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
            connection.setTransactionIsolation(TRANSACTION_NONE);
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't insert to db added screen ", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't insert to db added screen ", throwables);
        }
    }
}
