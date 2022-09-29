package be.ugent.flash.deel2;


import be.ugent.flash.db.Part;
import be.ugent.flash.db.Question;
import be.ugent.flash.deel2.PartBoxes.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

public class Selecter {

    Map<String, String> typeMap = Map.of(
            "mcs", "Meerkeuze (standaard)",
            "mcc", "Meerkeuze (compact)",
            "mci", "Meerkeuze (afbeelding)",
            "mr", "Meerantwoord",
            "open", "Open (tekst)",
            "openi", "Open (geheel)"
    );

    Map<String, Function<GeopendeDBController, Box>> typeCreatorMap = Map.of(
            "mcs", McsBox::new,
            "mcc", MccBox::new,
            "mci", MciBox::new,
            "mr", MrBox::new,
            "open", OpenBox::new,
            "openi", OpeniBox::new
    );

    private GeopendeDBController controller;
    private ImageProcessor imgProcessor;
    private Box currentBox;
    public VBox afbeeldingBox;


    public Selecter(GeopendeDBController controller, VBox afbeeldingBox, ImageProcessor imgProcessor){
        this.controller = controller;
        this.afbeeldingBox = afbeeldingBox;
        this.imgProcessor = imgProcessor;
    }

    public void selectedManager(ObservableValue<? extends Question> observable){
        if(observable.getValue() != null){
            select(observable);
        }
        else {
            controller.rechterhelftZichtbaar(false);
        }
    }
    public void select(ObservableValue<? extends Question> observable){
        //maak partbox leeg en maak nieuwe aan, afhankelijk van vraagsoort
        controller.emptyPartBox();
        currentBox = typeCreatorMap.get(controller.getSelected().question_type()).apply(controller);

        //maak de rechterhelft zichtbaar
        controller.rechterhelftZichtbaar(true);
        //display alle eigenschappen die voor elke kaart bestaan;
        Question selected = observable.getValue();
        controller.setTitel(selected.title());
        controller.setType(typeMap.get(selected.question_type()));
        controller.setTekst(selected.text_part());

        //zet huidige image in controller, evt null als er geen is
        controller.setCurrentImage(selected.image_part());

        //check of er evt afbeelding is en plaats deze dan, anders een knop voeg afbeelding toe
        afbeeldingBox.getChildren().removeAll(afbeeldingBox.getChildren());
        if (selected.image_part() != null) {
            imgProcessor.voegAfbeeldingToe(selected, selected.image_part());
        } else {
            imgProcessor.verwijderAfbeelding(selected);
        }
    }

    public ArrayList<Part> getPreviewQuestion(){
        return currentBox.getPreviewQuestion();
    }

    public Box getCurrentBox(){
        return currentBox;
    }
}
