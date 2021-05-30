package dal.Database.ObjectPool;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.exception.DALexception;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 */
public class ConnectionPool implements  IPool,IPool.Validator{
    private Hashtable<Connection, Long> locked, unlocked;
    private long expirationTime;
    private SQLServerDataSource ds;
    private static ConnectionPool connectionPool;

    public static ConnectionPool getInstance(){
        if(connectionPool==null)
            connectionPool= new ConnectionPool();
        return connectionPool;
    }

    private ConnectionPool() {
        initDataSource();
        locked = new Hashtable<Connection, Long>();
        unlocked = new Hashtable<Connection, Long>();
        expirationTime = 30_000;

    }

    private void initDataSource() {
        ds = new SQLServerDataSource();
        ds.setServerName("10.176.111.31");
        ds.setUser("CSe20B_8");
        ds.setPassword("soma2000");
        ds.setDatabaseName("ArlaProject");
    }

    @Override
    public synchronized Connection getConnection() throws SQLServerException {
        long now = System.currentTimeMillis();
        Connection connection;
        if (unlocked.size() > 0) {
            Enumeration<Connection> e = unlocked.keys();
            while (e.hasMoreElements()) {
                connection = e.nextElement();
                if ((now - unlocked.get(connection)) > expirationTime) {
                    // object has expired
                    unlocked.remove(connection);
                    invalidate(connection);
                    connection = null;
                } else {
                        if (isValid(connection)) {
                            unlocked.remove(connection);
                            locked.put(connection, now);
                            return (connection);
                        } else {
                            // object failed validation
                            unlocked.remove(connection);
                            invalidate(connection);
                            connection = null;
                        }

                }
            }
        }
        // no objects available, create a new one
        connection = createNew();
        locked.put(connection, now);
        return connection;
    }

    @Override
    public synchronized void releaseConnection(Connection connection) {
        locked.remove(connection);
        unlocked.put(connection, System.currentTimeMillis());
    }

    @Override
    public void shutdown() {

    }

    private Connection createNew() throws SQLServerException {
       return ds.getConnection();
    }

    @Override
    public boolean isValid(Connection connection)  {
        if(connection == null) {
            return false;
        }
        try {
            return !connection.isClosed();
        }
        catch(SQLException se) {
            return false;
        }
    }

    @Override
    public void invalidate(Connection connection) {
        try
        {
            connection.close();
        }
        catch(SQLException se)
        {
        }
    }
}
