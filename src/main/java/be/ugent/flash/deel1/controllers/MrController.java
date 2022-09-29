package be.ugent.flash.deel1.controllers;

import be.ugent.flash.deel1.CardManager;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.ArrayList;

public class MrController extends Controller {

    public GridPane choices;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public MrController(CardManager cardManager) {
        super(cardManager);
    }

    public void initialize() {
        baseInitialize();

        int gridRowCounter = 0;
        //voor elk mogelijk antwoord checkbox toevoegen
        for (byte[] answer : answers) {
            CheckBox box = new CheckBox();
            checkBoxes.add(box);

            //maak tekst van mogelijk antwoord
            Text t1 = new Text(byteToString(answer));
            t1.getStyleClass().add("textKleur");
            TextFlow tFlow = new TextFlow(t1);

            //voeg checkbox met antwoord toe aan gridpane
            choices.addRow(gridRowCounter,box,tFlow);
            gridRowCounter++;
        }
    }

    public void next() {
        //we overlopen elke voor elke knop of hij geselecteerd is, zoja voegen we T toe aan de string gekozenAntwoord en anders F
        String gekozenAntwoord = "";
        for (CheckBox box : checkBoxes) {
            gekozenAntwoord += (box.isSelected() ? "T" : "F");
        }
        //zet verbetering afhankelijk van gekozen antwoord = correct antwoord, roep volgende kaart op
        String correctAntwoord = card.getCorrectAnswer();
        super.cardManager.setVerbetering(gekozenAntwoord.equals(correctAntwoord));
        cardManager.displayNext();
    }
}
