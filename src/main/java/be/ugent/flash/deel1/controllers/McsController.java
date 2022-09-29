package be.ugent.flash.deel1.controllers;

import be.ugent.flash.deel1.CardManager;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.HashMap;
import java.util.Map;


public class McsController extends Controller {
    public GridPane choices;
    private int buttonText = (int) 'A';
    // map van knop aan antwoord
    private Map<Button, Integer> buttonToAnswer = new HashMap<>();

    public McsController(CardManager cardManager) {
        super(cardManager);
    }

    public void initialize() {
        baseInitialize();
        //voor elk mogelijk antwoord een knop en textflow aanmaken
        int gridRowCounter = 0;
        for(byte[] answer : answers) {
            Text t1 = new Text(byteToString(answer));
            t1.getStyleClass().add("keuzeText");
            TextFlow tFlow = new TextFlow();
            tFlow.getChildren().add(t1);
            tFlow.getStyleClass().add("keuzeText");

            Button button = new Button();
            //zet huidige ascii value buttonText om naar char en schuift ook char op daarna
            button.setText(Character.toString(buttonText));
            buttonText++;

            //map knop aan de index van tFlow/huidige antwoord in answers
            buttonToAnswer.put(button,gridRowCounter);

            button.setOnAction(e -> {
                //geef rij mee van gridpane choices waarop button zich bevind
                next(buttonToAnswer.get(button));
            });

            //we voegen knop en textflow toe aan nieuwe gridpane rij
            choices.addRow(gridRowCounter,button,tFlow);
            gridRowCounter++;
        }
    }

    public void next(int gekozenAntwoord) {
        //zet verbetering afhankelijk van gekozen antwoord = correct antwoord, roep volgende kaart op
        int correctAntwoord = Integer.parseInt(card.getCorrectAnswer());
        super.cardManager.setVerbetering(gekozenAntwoord == correctAntwoord);
        super.cardManager.displayNext();
    }
}


