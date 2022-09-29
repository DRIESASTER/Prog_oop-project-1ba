package be.ugent.flash.deel2;

import be.ugent.flash.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InterfaceManager {

    private Stage stage;
    public InterfaceManager(Stage stage){
        this.stage = stage;
        startBeheersInterface(stage);
    }

    //start het startvenster
    private void startBeheersInterface(Stage stage){
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("/be/ugent/flash/deel2FXML/StartVenster.fxml"));
        fxmlLoader.setController(new Menu(stage));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 520);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("quizlet 2.0");
        stage.setScene(scene);
        stage.show();
    }
}

