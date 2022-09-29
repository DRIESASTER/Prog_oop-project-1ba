package be.ugent.flash.deel1.controllers;

import be.ugent.flash.deel1.CardManager;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class OpeniController extends Controller {

    public TextField textField;
    public Label check;

    public OpeniController(CardManager cardManager) {
        super(cardManager);
    }

    public void initialize(){
        baseInitialize();
        //check voor enter in textfield en roep dan next() op

        //check wanneer enter ingeduwd word -> volgende kaart oproepen
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().toString().equals("ENTER")){
                    next();
                }
            }
        });
    }


    public void next() {
        //check of ingetypte String leeg is;
        if(!textField.getText().matches("^-?[0-9]+$")){
            check.getStyleClass().add("rodeText");
            check.setText("Je antwoord moet een geheel getal zijn!");
        }
        else {
            //zet verbetering afhankelijk van gekozen antwoord = correct antwoord, roep volgende kaart op
            String gekozenAntwoord = textField.getText();
            String correctAntwoord = card.getCorrectAnswer();
            cardManager.setVerbetering(gekozenAntwoord.equals(correctAntwoord));
            cardManager.displayNext();
        }
    }
}