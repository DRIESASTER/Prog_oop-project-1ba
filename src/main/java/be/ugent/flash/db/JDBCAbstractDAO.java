package be.ugent.flash.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class JDBCAbstractDAO {
    protected final Connection connection;

    public JDBCAbstractDAO(Connection connection) {
        this.connection = connection;
    }
    protected PreparedStatement prepare (String sql) throws SQLException { return connection.prepareStatement(sql);
    }
}
