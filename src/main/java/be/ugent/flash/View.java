package be.ugent.flash;

import be.ugent.flash.deel1.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class View {

    private Stage stage;

    public View(Stage stage){
        this.stage = stage;
    }


    //veranderd/zet scene a.h.v. een string met als waarde de naam van een fxml bestand en een meegegeven controller
    public void changeScene(String fxml, Controller controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        fxmlLoader.setController(controller);
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
