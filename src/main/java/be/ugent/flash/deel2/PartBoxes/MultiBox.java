package be.ugent.flash.deel2.PartBoxes;

import be.ugent.flash.db.DataAccessException;
import be.ugent.flash.db.Part;
import be.ugent.flash.db.PartDAO;
import be.ugent.flash.deel2.Box;
import be.ugent.flash.deel2.ErrorPopUp;
import be.ugent.flash.deel2.GeopendeDBController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.util.ArrayList;

public abstract class MultiBox extends Box {
    protected ArrayList<Part> answers = null;
    protected ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public MultiBox(GeopendeDBController controller) {
        super(controller);
        loadAnswersIn();
    }

    public void loadAnswersIn() {
        //haal huidige parts met question_id van de geselecteerde vraag
        ;
        try {
            answers = controller.getPartDAO().selectParts(question.question_id());
        } catch (DataAccessException e) {
            new ErrorPopUp(e.getMessage());
            Platform.exit();
        }
    }

    //voegt voor elke part zijn element, bijbehorende checkbox en een removebutton toe aan de gridpane
    protected void loadNewAnswers(ArrayList<? extends Node> newAnswers, ArrayList<CheckBox> checkBox){
        answerPane.getChildren().removeAll(answerPane.getChildren());
        int rowCounter = 0;
        for(Node current : newAnswers){
            Button removeButton = new Button("x");
            removeButton.setOnAction((e) -> removeAnswer(current, newAnswers));

            answerPane.addRow(rowCounter, checkBox.get(rowCounter), current, removeButton);
            rowCounter++;
        }
    }

    public abstract void removeAnswer(Node node, ArrayList<? extends Node> newAnswers);

    //geeft alle checkboxes terug die momenteel geselect
    protected ArrayList<CheckBox> getSelectedBoxes(){
        ArrayList<CheckBox> selectedCheckBoxes = new ArrayList<>();
        for(CheckBox checkBox : checkBoxes){
            if(checkBox.isSelected()){
                selectedCheckBoxes.add(checkBox);
            }
        }
        return selectedCheckBoxes;
    }

    public abstract void createBox();

    public abstract void boxOpslaan(PartDAO pDAO);

    public abstract void addAnswer();


    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public void setCorrectAnswer(String answer){
        correctAnswer = answer;
    }

}
