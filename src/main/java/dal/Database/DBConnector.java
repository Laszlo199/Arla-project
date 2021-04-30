package dal.Database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;


public class DBConnector {

    private SQLServerDataSource dataSource;


/*
//(Jakub Database) in my opinion we should use your Database when we creat the finale database. And one more database :D

    public DBConnector()
    {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("10.176.111.31");
        dataSource.setUser("CSe20B_8");
        dataSource.setPassword("potatoe2021");
        dataSource.setDatabaseName("AttendanceProject");
    }

 */

    /**
     * Test
     */
    public DBConnector()
    {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("10.176.111.31");
        dataSource.setUser("CSe20B_13");
        dataSource.setPassword("CSe20B_13");
        dataSource.setDatabaseName("ArlaMilk");
    }

    public Connection getConnection()throws SQLServerException
    {
        return dataSource.getConnection();
    }

}
