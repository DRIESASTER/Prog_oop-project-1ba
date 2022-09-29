package be.ugent.flash.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class JDBCQuestionDAO extends JDBCAbstractDAO implements QuestionDAO {

    public JDBCQuestionDAO(Connection conn) {
        super(conn);
    }

    @Override
    public ResultSet allIds() throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            stmt = prepare("SELECT question_id FROM questions ORDER BY question_id");
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new DataAccessException("kon niet de id's van alle vragen ophalen", e);
        }
    }

    @Override
    public ArrayList<Question> allQuestions() throws DataAccessException {
        PreparedStatement stmt = null;
        ArrayList<Question> questions = new ArrayList<>();
        try {
            stmt = prepare("SELECT * FROM questions ORDER BY question_id");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String text = rs.getString(3);
                byte[] image = rs.getBytes(4);
                String type = rs.getString(5);
                String answer = rs.getString(6);
                Question question = new Question(id, title, text, image, type, answer);
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new DataAccessException("kon niet alle vragen ophalen", e);
        }
        return questions;
    }

    public void updateTitel(int id, String titel) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("UPDATE questions SET title= ? WHERE question_id = ?");
            stmt.setString(1, titel);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon titel niet updaten van vraag met id: "+id, e);
        }
    }

    public void updateText(int id, String text) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("UPDATE questions SET text_part= ? WHERE question_id = ?");
            stmt.setString(1, text);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon niet de tekst updaten van de vraag met id: "+id, e);
        }
    }

    public void deleteQuestion(int id) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("DELETE FROM questions WHERE question_id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("de vraag met id: "+id+" kon niet gedelete worden", e);
        }
    }

    @Override
    public void updateImage(int id, byte[] byteArray) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("UPDATE questions SET image_part= ? WHERE question_id = ?");
            stmt.setBytes(1, byteArray);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon de afbeelding van de vraag met id: "+id+" niet aanpassen", e);
        }
    }

    @Override
    public void updateCorrectAnswer(int id, String answer) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("UPDATE questions SET correct_answer= ? WHERE question_id = ?");
            stmt.setString(1, answer);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon het correct antwoord van de vraag met id: "+id+" niet updaten", e);
        }
    }

    public int addQuestion(Question question) throws DataAccessException {

            Map<String, String> correctAnswer = Map.of(
                "mcs", "0",
                "mci", "0",
                "mcc", "0",
                "mr", "F",
                "open", "",
                "openi", "0"
        );

        try {
            PreparedStatement stmt = prepare("INSERT INTO questions(title,text_part,image_part,question_type,correct_answer) VALUES (? , '', ?, ?, ?)");
            stmt.setString(1, question.title());
            stmt.setBytes(2, null);
            stmt.setString(3, question.question_type());
            stmt.setString(4, correctAnswer.get(question.question_type()));
            stmt.executeUpdate();
            return stmt.getGeneratedKeys().getInt(1);
        }catch(SQLException e){
            throw new DataAccessException("kon de vraag niet toevoegen", e);
        }
    }
}
