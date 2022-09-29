package be.ugent.flash.db;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCDataAccessContext implements DataAccessContext {

    private Connection connection;

    public JDBCDataAccessContext(Connection connection) {
        this.connection = connection;
    }

    @Override
    public QuestionDAO getQuestionDAO() {
        return new JDBCQuestionDAO(connection);
    }

    @Override
    public PartDAO getPartDAO() {
        return new JDBCPartDAO(connection);
    }

    @Override
    public void close() throws DataAccessException{
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new DataAccessException("Kon context niet aflsuiten", ex);
        }
    }

}
