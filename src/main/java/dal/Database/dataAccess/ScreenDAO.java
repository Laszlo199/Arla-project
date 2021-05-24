package dal.Database.dataAccess;

import be.*;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Connection.*;

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

    public List<Screen> getAllScreens() throws DALexception {
        Map<Integer, Screen> screens = new HashMap<>();
        String queScreens  ="SELECT * from Screens;";
        String queSections = "SELECT screenID, colIndex, rowIndex, columnSpan," +
                " rowSpan, filepath, isHeader, title, CSVType from Sections;";

        try(Connection connection = dbConnector.getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(queScreens);
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int refreshTime = resultSet.getInt(3);
                if(refreshTime==0)
                    refreshTime = 5;
                    screens.put(id, new Screen(id, name, refreshTime));
            }

            resultSet = statement.executeQuery(queSections);
            while(resultSet.next()){
                int screenID = resultSet.getInt(1);
                int colIndex = resultSet.getInt(2);
                int rowIndex = resultSet.getInt(3);
                int columnSpan = resultSet.getInt(4);
                int rowSpan = resultSet.getInt(5);
                String filepath = resultSet.getString(6);
                Boolean isHeader = resultSet.getBoolean(7);
                String title = resultSet.getString(8);
                String type = resultSet.getString(9);
                if(resultSet.wasNull()) {
                    //do nothing
                }
                else {
                    if(isHeader==null && title==null && type==null) screens.get(screenID).addListElement(new ScreenElement(
                            colIndex, rowIndex, columnSpan, rowSpan, filepath));
                    else {
                        switch (type) {
                            case "LINECHART" -> screens.get(screenID).addListElement(
                                    new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                            new CSVInfo(isHeader, title, CSVInfo.CSVType.LINECHART)));
                            case "BARCHART" ->  screens.get(screenID).addListElement(
                                    new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                            new CSVInfo(isHeader, title, CSVInfo.CSVType.BARCHART)));
                            case "TABLE" -> screens.get(screenID).addListElement(
                                    new ScreenElement(colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                            new CSVInfo(isHeader, title, CSVInfo.CSVType.TABLE)));
                        }
                    }
                }

            }
            return screens.values().stream().toList();
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't get all screens", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't get all screens", throwables);
        }

    }

    public Screen getScreenByID(int id) throws DALexception {
        Screen temp = new Screen();
        String sql = "SELECT * FROM Screens WHERE id=?";

        try (Connection connection = dbConnector.getConnection()) {
            PreparedStatement pstat = connection.prepareStatement(sql);
            pstat.setInt(1, id);

            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
               temp.setId(resultSet.getInt("id"));
               temp.setName(resultSet.getString("name"));
               temp.setRefreshTime(resultSet.getInt("refreshTime"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops");
        }
        return temp;

    }



    /**
     * here will happen all the magic. At first we save the screen, get its id and
     * then save sections adding screen ids
     * @param screen
     * @param screenElements
     */

    public void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList) throws DALexception {
        int screenID = -1;
        String query1 = "INSERT INTO Screens([name], refreshTime) Values(?, ?);";
        String query2 = "INSERT INTO Sections(screenID, colIndex, rowIndex " +
                " , columnSpan, rowSpan, filepath, isHeader, title, CSVType) Values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String query3 = "UPDATE Users SET screenID = ? WHERE ID = ?";

        try(Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStat1 = connection.prepareStatement(query1,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement = connection.prepareStatement(query2)
           // PreparedStatement preparedStatement2 = connection.prepareStatement(query3)
        )
        {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            preparedStat1.setString(1, screen.getName());
            preparedStat1.setInt(2, 5);
            preparedStat1.executeUpdate();
            //connection.commit();

            //set proper id for that screen
            try(ResultSet generatedKey = preparedStat1.getGeneratedKeys()) {
                if(generatedKey.next())
                    screenID = generatedKey.getInt(1);
                else
                    throw new DALexception("Couldn't get generated key");
            }

            for(ScreenElement element: screenElements){
                preparedStatement.setInt(2, screenID);
                preparedStatement.setInt(3, element.getColIndex());
                preparedStatement.setInt(4, element.getRowIndex());
                preparedStatement.setInt(5, element.getColSpan());
                preparedStatement.setInt(6, element.getRowSpan());
                preparedStatement.setString(7, element.getFilepath());
                if(element.getCsvInfo()==null) {
                    preparedStatement.setString(8, null);
                    preparedStatement.setString(9, null);
                    preparedStatement.setString(10, null);
                } else {
                    preparedStatement.setBoolean(8, element.getCsvInfo().isHeader());
                    preparedStatement.setString(9, element.getCsvInfo().getTitle());
                    preparedStatement.setString(10, element.getCsvInfo().getType().toString());
                }
                preparedStatement.executeUpdate();
            }
        /*
            for (User user : usersList) {
                preparedStatement2.setInt(1, screenID);
                preparedStatement2.setInt(2, user.getID());
                preparedStatement2.executeUpdate();
            }

            */
            connection.commit();
            connection.setAutoCommit(true);
            connection.setTransactionIsolation(TRANSACTION_NONE);
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't insert to db added screen ", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't insert to db added screen ", throwables);
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

    /**
     * we need to delete this and all associcated rows
     * @param screen
     */
    public void deletePuzzleScreen(Screen screen) throws DALexception {
        String sql = "DELETE FROM Screens WHERE id=?";
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

    public List<ScreenElement> getSections(Screen screen) throws DALexception {
        String sql = "SELECT * FROM Sections WHERE screenID = ?";
        List<ScreenElement> sections = new ArrayList<>();

        try(Connection connection = dbConnector.getConnection()) {
            PreparedStatement pstat = connection.prepareStatement(sql);
            pstat.setInt(1, screen.getId());
            System.out.println("SCREEN ID: " + screen.getId());
            ResultSet resultSet = pstat.executeQuery();
            while(resultSet.next()){
                int colIndex = resultSet.getInt(3);
                int rowIndex = resultSet.getInt(4);
                int columnSpan = resultSet.getInt(5);
                int rowSpan = resultSet.getInt(6);
                String filepath = resultSet.getString(7);
                boolean isHeader = resultSet.getBoolean(8);
                String title = resultSet.getString(9);
                String type = resultSet.getString(10);

                System.out.println(isHeader + ", " + title + ", " + type);
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
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't get all screens", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't get all screens", throwables);
        }
        System.out.println("SECTIONS: " + sections.size());
        return sections;
    }
}
