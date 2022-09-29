package be.ugent.flash.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDataAccessProvider implements DataAccessProvider {

    private String source;

    public JDBCDataAccessProvider(String source) {
        this.source=source;
    }

    public DataAccessContext getDataAccessContext() throws DataAccessException {
        return new JDBCDataAccessContext(getConnection());
    }

    private Connection getConnection() throws DataAccessException {
        try {
            return DriverManager.getConnection(source);
        } catch (SQLException e) {
            throw new DataAccessException("kon geen verbinding maken met de driverManager", e);
        }
    }
}
