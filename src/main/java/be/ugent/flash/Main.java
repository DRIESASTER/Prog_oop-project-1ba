package be.ugent.flash;

import be.ugent.flash.db.DataAccessException;
import be.ugent.flash.db.JDBCDataAccessProvider;
import be.ugent.flash.deel1.CardManager;
import be.ugent.flash.deel2.ErrorPopUp;
import be.ugent.flash.deel2.InterfaceManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


import java.util.List;

/**
 * Hoofdprogramma
 */
public class Main extends Application {
    // Dit is een basis waarmee je je hoofdprogramma kan opbouwen. Maak gerust aanpassingen
    // die beter passen bij je eigen ontwerp.
    //
    // Je moet hoe dan ook de methode start nog aanpassen.



    private static String dbName; // null wanneer we de beheersinterface willen starten
    /**
     * Toont een foutbericht en stopt het programma
     */
    private void error(String bericht) {
        // Uit cursusbroncode (ArgsMain)
        System.err.println("*ERROR* -- " + bericht);
        Platform.exit(); // Opgelet! Platform.exit() is ook nodig als 'launch' niet wordt opgeroepen!
    }
    @Override
    public void init() {
        List<String> argList = getParameters().getRaw();
        int size = argList.size();
        if (size == 0) {
            this.dbName = null;
        } else if (size == 1) {
            this.dbName = argList.get(0);
        } else {
            error("maximum twee opdrachtlijnparameters toegelaten");
        }
    }

    @Override
    public void start(Stage stage) {
        if (dbName == null) {
            // start de beheersinterface
            InterfaceManager interfaceManager = new InterfaceManager(stage);
        } else {
            //maakt view aan waar ik de juiste scenes kan gaan instellen
            View view = new View(stage);
            //connect met DB en overloop alle kaarten
            JDBCDataAccessProvider provider = new JDBCDataAccessProvider("jdbc:sqlite:" + dbName);
            CardManager cardManager = null;
            try {
                cardManager = new CardManager(provider.getDataAccessContext(), view);
            } catch (DataAccessException e) {
                new ErrorPopUp(e.getMessage());
                throw new RuntimeException(e);
            }
            cardManager.displayNext();
        }
    }

    public static void main(String[] args) {
        if(args.length > 0) {
            dbName = args[0];
        }
        launch(args);
    }
}
