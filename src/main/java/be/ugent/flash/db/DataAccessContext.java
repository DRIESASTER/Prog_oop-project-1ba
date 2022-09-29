package be.ugent.flash.db;

public interface DataAccessContext extends AutoCloseable{

    QuestionDAO getQuestionDAO ();
    PartDAO getPartDAO ();
    abstract void close() throws DataAccessException;
}
