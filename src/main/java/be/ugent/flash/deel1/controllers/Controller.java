package be.ugent.flash.deel1.controllers;

import be.ugent.flash.deel1.cards.Card;
import be.ugent.flash.deel1.CardManager;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public abstract class Controller {

    protected CardManager cardManager;
    protected Card card;
    protected ArrayList<byte[]> answers;
    public Label correctie;
    public ImageView afbeelding;
    public TextFlow vraag;
    public BorderPane mainPane;

    public Controller(CardManager cardManager){
        this.cardManager = cardManager;
        card = cardManager.getCard();
        answers = card.getAnswers();
    }

    //algemene initialisatie methode voor alle kaarten
    public void baseInitialize(){
        //verbeter de vorige vraag
        verbeter();

        //stel vraag in
        Text t1 = new Text(card.getVraag());
        t1.getStyleClass().add("textKleur");
        vraag.getChildren().add(t1);

        //stel afbeelding in
        setImage();

    }


    public void verbeter(){
        //plaats de verbetering van de vorige vraag bovenaan
        correctie.setText("");
        if(cardManager.getVerbetering() != null && !cardManager.getVerbetering()) {
            correctie.setText("De vorige vraag was fout.");
            correctie.getStyleClass().add("rodeText");
        }
    }

    public void setImage(){
        //als er een afbeelding is, stel deze dan in.
        try{
            Image img = new Image(new ByteArrayInputStream(card.getImage()));
            afbeelding.setImage(img);
        }
        catch (NullPointerException e){
            //zet geen afbeelding
        }
    }

    //simpele methode om byte[] naar string om te zetten
    public String byteToString(byte[] bytes){
        try{
            return new String(bytes);
        }catch (NullPointerException e){
            //return lege string als bytes null is
            return "";
        }
    }

    public void disableInput(){
        mainPane.getChildren().forEach((x) -> x.setDisable(true));
    }

    //simpele methode om byte[] naar een afbeelding om te zetten
    public Image byteToImage(byte[] bytes){
        try {
            return new Image(new ByteArrayInputStream(bytes));
        }catch (NullPointerException e){
            return null;
        }
    }
}
