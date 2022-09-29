package be.ugent.flash.deel2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorPopUp {

    private VBox vBox = new VBox();
    private Label errorMsgText = new Label();
    private Button ok = new Button("OK");

    public ErrorPopUp(String errorMsg){
        Stage popUp = new Stage();
        popUp.setResizable(false);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("foutmelding");
        vBox.setPrefSize(300,150);
        vBox.setSpacing(15);
        errorMsgText.setText(errorMsg);
        ok.setOnAction(e -> popUp.close());
        vBox.getChildren().addAll(errorMsgText, ok);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        popUp.setScene(scene);
        //zorgt ervoor dat je niet terug naar hoofdscherm kan zonder pop up te sluiten
        popUp.showAndWait();
    }
}
