package dal.Database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;


public class DBConnector {

    private SQLServerDataSource ds;



    private DBConnector()
    { ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.31");
        ds.setUser("CSe20B_8");
        ds.setPassword("soma2000");
        ds.setDatabaseName("ArlaProject");

    }


    public Connection getConnection()throws SQLServerException
    {
        return ds.getConnection();
    }

}
