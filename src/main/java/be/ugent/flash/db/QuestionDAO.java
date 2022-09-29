package be.ugent.flash.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface QuestionDAO {

    ResultSet allIds() throws DataAccessException;

    ArrayList<Question> allQuestions() throws DataAccessException;

    void updateTitel(int id, String titel) throws DataAccessException;

    void updateText(int id, String text) throws DataAccessException;

    void deleteQuestion(int id) throws DataAccessException;

    void updateImage(int id, byte[] byteArray) throws DataAccessException;

    void updateCorrectAnswer(int id, String answer) throws DataAccessException;

    int addQuestion(Question question) throws DataAccessException;
}
