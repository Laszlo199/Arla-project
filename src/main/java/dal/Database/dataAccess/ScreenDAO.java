package dal.Database.dataAccess;

import be.*;
import com.microsoft.sqlserver.jdbc.SQLServerException;
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


    public List<Screen> getAllScreens(Connection connection) throws DALexception {
        Map<Integer, Screen> screens = new HashMap<>();
        String queScreens  ="SELECT * from Screens;";
        String queSections = "SELECT screenID, colIndex, rowIndex, columnSpan," +
                " rowSpan, filepath, isHeader, title, CSVType from Sections;";


       try (Statement statement = connection.createStatement()){


            ResultSet resultSet = statement.executeQuery(queScreens);
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int refreshTime = resultSet.getInt(3);
                boolean refreshNow = resultSet.getBoolean(4);
                if(refreshTime==0)
                    refreshTime = 5;
                    screens.put(id, new Screen(id, name, refreshTime, refreshNow));
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
           // statement.executeUpdate(clean);

            return screens.values().stream().toList();
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't get all screens", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't get all screens", throwables);
        }


    }

    public Screen getScreenByID(int id, Connection connection) throws DALexception {
       Screen temp = null;
        String sql = "SELECT * FROM Screens WHERE id=?";

        try (PreparedStatement pstat = connection.prepareStatement(sql)) {

            pstat.setInt(1, id);

            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
                int id1 = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int refreshTime = resultSet.getInt("refreshTime");
                temp = new Screen(id1, name, refreshTime);

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

    public void save(Screen screen, List<ScreenElement> screenElements, List<User> usersList, Connection connection) throws DALexception {
        int screenID = -1;
        String query1 = "INSERT INTO Screens([name], refreshTime) Values(?, ?);";
        String query2 = "INSERT INTO Sections(screenID, colIndex, rowIndex " +
                " , columnSpan, rowSpan, filepath, isHeader, title, CSVType) Values(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement preparedStat1 = connection.prepareStatement(query1,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement = connection.prepareStatement(query2)

        )
        {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            preparedStat1.setString(1, screen.getName());
            preparedStat1.setInt(2, 5);
            preparedStat1.executeUpdate();


            //set proper id for that screen
            try(ResultSet generatedKey = preparedStat1.getGeneratedKeys()) {
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
                if(element.getCsvInfo()==null) {
                    preparedStatement.setString(7, null);
                    preparedStatement.setString(8, null);
                    preparedStatement.setString(9, null);
                } else {
                    preparedStatement.setBoolean(7, element.getCsvInfo().isHeader());
                    preparedStatement.setString(8, element.getCsvInfo().getTitle());
                    preparedStatement.setString(9, element.getCsvInfo().getType().toString());
                }
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
    




    public void update(Screen screen, Connection connection) throws DALexception {
        String sql = "UPDATE Screens SET [name]=?, [refreshTime]=?, [refreshNow]=? " +
                "WHERE id = ?";
        try(PreparedStatement pstat = connection.prepareStatement(sql)) {

            pstat.setString(1, screen.getName());
            pstat.setInt(2, screen.getRefreshTime());
            pstat.setBoolean(3, screen.isRefreshNow());
            pstat.setInt(4, screen.getId());
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
    public void deletePuzzleScreen(Screen screen, Connection connection) throws DALexception {
        String sql = "DELETE FROM Screens WHERE id=?";
        try(PreparedStatement pstat = connection.prepareStatement(sql)) {

            pstat.setInt(1, screen.getId());
            pstat.executeUpdate();
        } catch (SQLServerException throwables) {
            throw new DALexception("Whoops...Couldn't delete screen");
        } catch (SQLException throwables) {
            throw new DALexception("Whoops...Couldn't delete screen");
        }
    }


    public List<ScreenElement> getSections(Screen screen, Connection connection) throws DALexception {
        String sql = "SELECT * FROM Sections WHERE screenID = ?";
        List<ScreenElement> sections = new ArrayList<>();

        try(PreparedStatement pstat = connection.prepareStatement(sql)) {
            pstat.setInt(1, screen.getId());
            System.out.println("SCREEN ID: " + screen.getId());
            ResultSet resultSet = pstat.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int colIndex = resultSet.getInt(3);
                int rowIndex = resultSet.getInt(4);
                int columnSpan = resultSet.getInt(5);
                int rowSpan = resultSet.getInt(6);
                String filepath = resultSet.getString(7);
                boolean isHeader = resultSet.getBoolean(8);
                String title = resultSet.getString(9);
                String type = resultSet.getString(10);

                if(title==null || type==null)
                    sections.add(
                            new ScreenElement(id, colIndex, rowIndex, columnSpan, rowSpan, filepath));
                else {
                    switch (type) {
                        case "LINECHART" -> sections.add(
                                new ScreenElement(id, colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                        new CSVInfo(isHeader, title, CSVInfo.CSVType.LINECHART)));
                        case "BARCHART" ->  sections.add(
                                new ScreenElement(id, colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                        new CSVInfo(isHeader, title, CSVInfo.CSVType.BARCHART)));
                        case "TABLE" -> sections.add(
                                new ScreenElement(id, colIndex, rowIndex, columnSpan, rowSpan, filepath,
                                        new CSVInfo(isHeader, title, CSVInfo.CSVType.TABLE)));
                    }
                }

            }
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't get sections for screen", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't get sections for screen", throwables);
        }
        return sections;
    }

    public void setRefreshes(Connection connection) throws DALexception {
        String clean  = "UPDATE Screens Set refreshNow=0;";
        try( Statement statement = connection.createStatement()) {
            statement.executeUpdate(clean);
        } catch (SQLServerException throwables) {
            throw new DALexception("Couldn't set refreshes", throwables);
        } catch (SQLException throwables) {
            throw new DALexception("Couldn't set refreshes", throwables);
        }
    }

    public void saveToUsersAndScreens(int screenID, int userID, Connection connection) throws DALexception {
        String sql = "INSERT INTO UsersAndScreens(UserID,ScreenID) Values(?, ?)";
        try (PreparedStatement pStatement = connection.prepareStatement(sql)) {
            pStatement.setInt(1, userID);
            pStatement.setInt(2, screenID);
            pStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DALexception("Whoops...2");
        }
    }

    public int getScreenIDByName(String screenName, Connection connection) throws DALexception {
        int id= -1;
        String sql = "SELECT id FROM Screens WHERE name=?";
        try (PreparedStatement pstat = connection.prepareStatement(sql)) {
            pstat.setString(1, screenName);
            ResultSet resultSet = pstat.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("id");

            }
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public void updateSections(List<ScreenElement> sections, Connection connection) {
        String sql = "UPDATE Sections SET filepath=?, isHeader=?, title=?, CSVType=? WHERE id=?";
        try (PreparedStatement pstat = connection.prepareStatement(sql)) {
            for(ScreenElement section : sections) {
                pstat.setString(1, section.getFilepath());
                if(section.getCsvInfo()!=null) {
                    pstat.setBoolean(2, section.getCsvInfo().isHeader());
                    pstat.setString(3, section.getCsvInfo().getTitle());
                    pstat.setString(4, section.getCsvInfo().getType().toString());
                } else {
                    pstat.setString(2, null);
                    pstat.setString(3, null);
                    pstat.setString(4, null);
                }
                pstat.setInt(5, section.getId());
                pstat.executeUpdate();
            }
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
