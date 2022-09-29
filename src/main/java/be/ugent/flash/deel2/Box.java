package be.ugent.flash.deel2;

import be.ugent.flash.db.Part;
import be.ugent.flash.db.PartDAO;
import be.ugent.flash.db.Question;
import be.ugent.flash.deel2.GeopendeDBController;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public abstract class Box {

    protected GeopendeDBController controller;

    protected Question question;
    protected GridPane answerPane = new GridPane();
    protected String correctAnswer;

    public Box(GeopendeDBController controller) {
        this.controller = controller;
        this.question = controller.getSelected();
        setCorrectAnswer(question.correct_answer());
        answerPane.setVgap(5);
    }

    public abstract void createBox();

    public abstract void boxOpslaan(PartDAO pDAO);

    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public void setCorrectAnswer(String answer){
        correctAnswer = answer;
    }

    public abstract ArrayList<Part> getPreviewQuestion();

}

