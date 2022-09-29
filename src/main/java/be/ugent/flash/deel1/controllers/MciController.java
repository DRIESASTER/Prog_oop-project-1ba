package be.ugent.flash.deel1.controllers;

import be.ugent.flash.deel1.CardManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


public class MciController extends Controller {

    public HBox images;

    public MciController(CardManager cardManager) {
        super(cardManager);
    }

    public void initialize() {
        baseInitialize();
        //voor elk antwoord een afbeelding toevoegen
        for (byte[] bytes : card.getAnswers()) {
            ImageView imageView = new ImageView();
            if(bytes != null) {
                Image image = byteToImage(bytes);
                imageView.setImage(image);
            }
            //laat next oproepen als op de afbeelding geclicked word
            imageView.setOnMouseClicked(mouseEvent -> {
                next(imageView);
            });
            images.getChildren().add(imageView);
        }
    }

    public void next(ImageView imageView) {
        //zet verbetering afhankelijk van gekozen antwoord = correct antwoord, roep volgende kaart op
        int gekozenAntwoord = images.getChildren().indexOf(imageView);
        int correctAntwoord = Integer.parseInt(card.getCorrectAnswer());

        super.cardManager.setVerbetering(gekozenAntwoord == correctAntwoord);
        super.cardManager.displayNext();
    }
}


