package dal.Database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;


public class DBConnector {

    private SQLServerDataSource ds;



//(Jakub Database) in my opinion we should use your Database when we creat the finale database. And one more database :D

    public DBConnector()
    { ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.31");
        ds.setUser("CSe20B_8");
        ds.setPassword("soma2000");
        ds.setDatabaseName("ArlaProject");

    }



    /**
     * Test
     */
/*
    public DBConnector()
    {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("10.176.111.31");
        dataSource.setUser("CSe20B_13");
        dataSource.setPassword("CSe20B_13");
        dataSource.setDatabaseName("ArlaMilk");
    }

 */

    public Connection getConnection()throws SQLServerException
    {
        return ds.getConnection();
    }

}
