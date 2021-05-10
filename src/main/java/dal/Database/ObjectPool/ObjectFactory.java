package dal.Database.ObjectPool;

import java.sql.Connection;

/**
 *
 */
public interface ObjectFactory {
    public abstract Connection createNew();
}
