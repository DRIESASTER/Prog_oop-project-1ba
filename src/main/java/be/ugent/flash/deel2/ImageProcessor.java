package be.ugent.flash.deel2;

import be.ugent.flash.db.Question;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageProcessor {

    private VBox afbeeldingBox;
    private GridPane rechterBovenkant;
    private GeopendeDBController controller;

    public ImageProcessor(GeopendeDBController controller, VBox afbeeldingBox, GridPane rechterBovenkant){
        this.controller = controller;
        this.afbeeldingBox = afbeeldingBox;
        this.rechterBovenkant = rechterBovenkant;
    }

    public void nieuweAfbeelding(Question selected){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("afbeeldingen","*.jpeg", "*.png", "*.jpg"));
        byte[] byteArray = null;
        File file =chooser.showOpenDialog(afbeeldingBox.getScene().getWindow()); if ( file != null) {
            try {
                controller.disabledHasChanged(true);
                byteArray = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            voegAfbeeldingToe(selected, byteArray);
        }

    }

    public void voegAfbeeldingToe(Question selected, byte[] byteArray){
        //opmaak van image in vbox -> gridpane
        afbeeldingBox.getChildren().removeAll(afbeeldingBox.getChildren());
        ImageView view = new ImageView();

        //sla orginele byte[] op in currentImage
        controller.setCurrentImage(byteArray);

        view.setImage(new Image(new ByteArrayInputStream(byteArray)));
        view.setPreserveRatio(true);
        rechterBovenkant.setPrefHeight(272);
        view.setFitHeight(90);
        afbeeldingBox.getChildren().add(view);


        //voeg knoppen toe in hbox
        HBox afbeeldingKnoppen = new HBox();
        Button wijzig = new Button("wijzig...");
        Button verwijder = new Button("verwijder");
        wijzig.setOnAction(e -> nieuweAfbeelding(selected));
        //laat verwijder button tabel disablen en methode verwijder afbeelding oproepen
        verwijder.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                controller.disabledHasChanged(true);
                verwijderAfbeelding(selected);
            }
        });

        afbeeldingKnoppen.getChildren().add(wijzig);
        afbeeldingKnoppen.getChildren().add(verwijder);
        afbeeldingBox.getChildren().add(afbeeldingKnoppen);
    }

    public void verwijderAfbeelding(Question selected){
        rechterBovenkant.setPrefHeight(172);
        afbeeldingBox.getChildren().removeAll(afbeeldingBox.getChildren());
        Button toevoegen = new Button("voeg afbeelding toe");
        toevoegen.setOnAction(e -> nieuweAfbeelding(selected));
        afbeeldingBox.getChildren().add(toevoegen);
        controller.setCurrentImage(null);
    }
}
