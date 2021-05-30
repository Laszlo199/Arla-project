package dal.Database.ObjectPool;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

/**
 *
 */
public interface ObjectFactory {
     Connection createNew() throws SQLServerException;
}
