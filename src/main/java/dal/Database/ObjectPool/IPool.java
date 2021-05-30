package dal.Database.ObjectPool;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.exception.DALexception;

import java.sql.Connection;

/**
 *
 */
public interface IPool{
    /**
     * method is used to release object from the pool
     * @return
     */
    Connection getConnection() throws SQLServerException;

    /**
     * Object that is passed as a parameter is put back
     * to the pool
     * @param connection
     */
    void releaseConnection(Connection connection);

    //comment later. for now, not fully understand
    void shutdown();

    public static interface Validator
    {
        /**
         * Checks whether the object is valid.
         */
        public boolean isValid(Connection connection) throws DALexception;

        /**
         * Performs any cleanup activities
         * before discarding the object.
         * @param connection the object to cleanup
         */

        public void invalidate(Connection connection);
    }

}
