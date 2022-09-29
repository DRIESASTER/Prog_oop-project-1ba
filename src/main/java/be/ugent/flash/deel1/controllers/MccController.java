package be.ugent.flash.deel1.controllers;

import be.ugent.flash.deel1.CardManager;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class MccController extends Controller {

    public HBox buttons;

    public MccController(CardManager cardManger) {
        super(cardManger);
    }

    public void initialize(){
        baseInitialize();

        //voor elk antwoord een button toevoegen
        for(byte[] answer : answers) {
            Button button = new Button();
            button.setText(byteToString(answer));
            //laat button next oproepen
            button.setOnAction(e -> next(button));
            buttons.getChildren().add(button);
        }
    }

    public void next(Button button) {
        //zet verbetering afhankelijk van gekozen antwoord = correct antwoord, roep volgende kaart op
        int gekozenAntwoord = buttons.getChildren().indexOf(button);
        int correctAntwoord = Integer.parseInt(card.getCorrectAnswer());

        cardManager.setVerbetering(gekozenAntwoord == correctAntwoord);
        cardManager.displayNext();
    }
}
