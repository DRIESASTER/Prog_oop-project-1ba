package be.ugent.flash.db;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PartDAO {
    ArrayList<Part> selectParts(int question_id) throws DataAccessException;

    void updateParts(int question_id, ArrayList<byte[]> parts) throws DataAccessException;

    void removePart(int part_id) throws DataAccessException;

    void addEmptyPart(int question_id) throws DataAccessException;

    void deleteAllParts(int question_id) throws DataAccessException;

}
