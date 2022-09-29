package be.ugent.flash.db;

import be.ugent.flash.db.DataAccessContext;
import be.ugent.flash.db.DataAccessException;

public interface DataAccessProvider {
    public DataAccessContext getDataAccessContext() throws DataAccessException;
}
