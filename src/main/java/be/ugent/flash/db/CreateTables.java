package be.ugent.flash.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class CreateTables {

    /**
     * Maak de tabel 'questions' aan. (Enkel SQLite.)
     */
    public static void createQuestionsTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE questions (
                       question_id INTEGER PRIMARY KEY,
                       title TEXT,
                       text_part TEXT,
                       image_part BLOB,
                       question_type TEXT NOT NULL,
                       correct_answer TEXT
                    );
                    """
            );
        }
    }

    /**
     * Maak de tabel 'parts' aan. (Enkel SQLite.)
     */
    public static void createPartsTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE parts (
                       part_id INTEGER PRIMARY KEY,
                       question_id INTEGER REFERENCES questions,
                       part TEXT
                    );
                    """
            );
        }
        // merk op dat ON DELETE CASCADE niet werkt bij de standaardinstellingen van sqlite...
    }

}
