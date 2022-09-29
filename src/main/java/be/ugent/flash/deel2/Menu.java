package be.ugent.flash.deel2;

import be.ugent.flash.db.CreateTables;
import be.ugent.flash.deel2.DBEditor;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Menu {

    //regelt de knoppen van het startInterface, als de menu bar van elk scherm
    private Stage stage;


    public Menu(Stage stage){
        this.stage = stage;
    }

    public void open() {
        FileChooser chooser = new FileChooser();
        File file =chooser.showOpenDialog(stage.getScene().getWindow()); if ( file != null) {
            new DBEditor(file, stage);
        }
    }

    public void exitProgram(){
        Platform.exit();
    }

    public void nieuw() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("example" + ".sqlite");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("sqlite database","*.sqlite"));
        File file = chooser.showSaveDialog(stage.getScene().getWindow());
        if (file != null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
                CreateTables.createQuestionsTable(connection);
                CreateTables.createPartsTable(connection);
                new DBEditor(file,stage);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
