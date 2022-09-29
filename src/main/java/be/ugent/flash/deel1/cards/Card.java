package be.ugent.flash.deel1.cards;

import be.ugent.flash.db.*;

import java.util.ArrayList;

public abstract class Card {

    protected Question question;
    protected ArrayList<Part> parts;

    public Card(Question question){
        this.question = question;
    }

    public void setParts(ArrayList<Part> parts){
        this.parts = parts;
    }

    public String getVraag(){
        return question.text_part();
    }

    public String getCorrectAnswer() {
        return question.correct_answer();
    }
    public byte[] getImage(){
        return(question.image_part());
    }

    public ArrayList<byte[]> getAnswers(){
        ArrayList<byte[]> answers = new ArrayList<>();
        for(Part part : parts){
            answers.add(part.part());
        }
        return answers;
    }
}
