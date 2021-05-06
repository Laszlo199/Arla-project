package dal.Database.dataAccess;

import dal.Database.DBConnector;
import dal.exception.DALexception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminsDAO {
    private DBConnector dbconnector;

    public AdminsDAO(){
        dbconnector = new DBConnector();
    }


    public boolean validate(String password) throws DALexception {
        try (Connection connection = dbconnector.getConnection()) {
            String sql = "SELECT * FROM Admins WHERE Password=?";
            PreparedStatement pstat = connection.prepareStatement(sql);
            pstat.setString(1, password);
            ResultSet rs = pstat.executeQuery();

            while(rs.next()) {
                return true;
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
            throw new DALexception("Whoops");
        }
        return false;
    }
}

