package be.ugent.flash.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCPartDAO extends JDBCAbstractDAO implements PartDAO {

    public JDBCPartDAO(Connection connection) {
        super(connection);
    }

    @Override
    public ArrayList<Part> selectParts(int question_id) throws DataAccessException {
        ArrayList<Part> parts = new ArrayList<>();
        try {
            PreparedStatement stmt = prepare("SELECT * FROM parts WHERE question_id=?");
            stmt.setString(1, String.valueOf(question_id));
            ResultSet rs = stmt.executeQuery();
            parts = new ArrayList<>();
            while (rs.next()) {
                int p_id = rs.getInt(1);
                int q_id = rs.getInt(2);
                byte[] p = rs.getBytes(3);
                Part part = new Part(p_id, q_id, p);
                parts.add(part);
            }
        }catch(SQLException e){
            throw new DataAccessException("kon parts van de vraag met id: "+question_id+" niet selecteren", e);
        }
        return parts;
    }


    public void updateParts(int question_id, ArrayList<byte[]> parts) throws DataAccessException {
        //verwijder eerst alle vorige parts
        deleteAllParts(question_id);
        //voeg de nieuwe parts toe aan de db
        try {
            PreparedStatement stmt2 = prepare("INSERT INTO parts (part_id, question_id, part) VALUES (?, ? ,?)");
            for (byte[] bytes : parts) {
                stmt2.setInt(2, question_id);
                stmt2.setBytes(3, bytes);
                stmt2.executeUpdate();
            }
        }catch(SQLException e){
            throw new DataAccessException("kon niet alle parts toevoegen voor de vraag met id: "+question_id, e);
        }
    }

    @Override
    public void removePart(int part_id) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("DELETE FROM parts WHERE part_id = ?");
            stmt.setInt(1, part_id);
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon niet het part met id: "+part_id+" verwijderen", e);
        }
    }

    public void addEmptyPart(int question_id) throws DataAccessException {
        try {
            PreparedStatement stmt = prepare("INSERT INTO parts(question_id,part) VALUES(?, ?)");
            stmt.setInt(1, question_id);
            stmt.setBytes(2, null);

            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon geen leeg part toevoegen voor de vraag met id: "+question_id, e);
        }
    }

    public void deleteAllParts(int question_id) throws DataAccessException {
        try {
            PreparedStatement stmt1 = prepare("DELETE FROM parts WHERE question_id = ?");
            stmt1.setInt(1, question_id);
            stmt1.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("kon niet alle parts van de vraag met id: "+question_id+" verwijderen", e);
        }
    }
}
