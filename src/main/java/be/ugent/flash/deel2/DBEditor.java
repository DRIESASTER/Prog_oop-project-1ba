package be.ugent.flash.deel2;

import be.ugent.flash.db.*;
import be.ugent.flash.deel1.CardManager;
import be.ugent.flash.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DBEditor {

    private Stage stage;
    private DataAccessContext DAO;

    private Stage previewStage = new Stage();

    public DBEditor(File file, Stage stage){
        this.stage = stage;
        //maak DAO aan van de geselecteerde db
        JDBCDataAccessProvider provider = new JDBCDataAccessProvider("jdbc:sqlite:" + file.getAbsolutePath());
        try {
            DAO = provider.getDataAccessContext();
        }catch(DataAccessException e){
            new ErrorPopUp(e.getMessage());
        }
        startGeopendeDB();
    }

    public Stage getStage(){
        return stage;
    }


    private void startGeopendeDB(){
        //start de effectieve editor op
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("/be/ugent/flash/deel2FXML/GeopendeDB.fxml"));
        fxmlLoader.setController(new GeopendeDBController(this));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 720,  520);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("DB-Editor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public ArrayList<Question> getQuestions(){
        //haalt alle question uit de db
        try {
            return DAO.getQuestionDAO().allQuestions();
        }catch(DataAccessException e){
            new ErrorPopUp(e.getMessage());
            Platform.exit();
        }
        return null;
    }
    public void preview(Question question, ArrayList<Part> parts) {
        //maakt preview van meegegeven vraag
        CardManager cardManager = new CardManager(DAO, new View(previewStage));
        cardManager.preview(question, parts);
    }

    public void closePreview(){
        previewStage.close();
    }

    public QuestionDAO getQDAO(){
        return DAO.getQuestionDAO();
    }
    public PartDAO getPDAO(){
        return DAO.getPartDAO();
    }

}
