package be.ugent.flash.deel2.PartBoxes;

import be.ugent.flash.db.DataAccessException;
import be.ugent.flash.db.Part;
import be.ugent.flash.db.PartDAO;
import be.ugent.flash.deel2.ErrorPopUp;
import be.ugent.flash.deel2.GeopendeDBController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MrBox extends MultiBox {

    private ArrayList<TextArea> newAnswers = new ArrayList<>();
    public MrBox(GeopendeDBController controller) {
        super(controller);
        createBox();
    }

    @Override
    public void createBox() {
        ScrollPane scrollPane = new ScrollPane(answerPane);

        int rowCounter = 0;
        //voor elk part een antwoord toevoegen
        for(Part answer : answers) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(correctAnswer.charAt(rowCounter) == 'T');
            checkBoxes.add(checkBox);
            checkBox.setOnAction((e) -> controller.disabledHasChanged(true));

            TextArea tArea = new TextArea();
            tArea.setPrefHeight(60);
            tArea.setPrefWidth(250);
            tArea.setOnKeyTyped((e) -> controller.disabledHasChanged(true));
            try{
                tArea.setText(new String(answer.part()));
            } catch(Exception e){
                //part is null, tekst mag leeg blijven
            }

            newAnswers.add(tArea);
            rowCounter++;
        }
        loadNewAnswers(newAnswers, checkBoxes);
        controller.addTopartBox(scrollPane);

        Button addButton = new Button("Optie toevoegen");
        addButton.setOnAction((e) -> addAnswer());
        controller.addTopartBox(addButton);
    }

    public void generateCorrectAnswer(){
        //correct antwoord genereren op basis van een box geselect is +T anders +F
        String correctAnswer = "";
        for (CheckBox box : checkBoxes) {
            correctAnswer += (box.isSelected() ? "T" : "F");
        }
        setCorrectAnswer(correctAnswer);
    }

    public void removeAnswer(Node node, ArrayList<? extends Node> newAnswers) {
        controller.disabledHasChanged(true);
        answerPane.getChildren().removeAll(answerPane.getChildren());
        checkBoxes.remove(newAnswers.indexOf(node));
        newAnswers.remove(node);
        loadNewAnswers(newAnswers, checkBoxes);
    }

    @Override
    public void boxOpslaan(PartDAO pDAO) {
        generateCorrectAnswer();
        //check of er minstens 1 antwoord is
        if(correctAnswer.length() > 0) {
            ArrayList<byte[]> bytes = new ArrayList<>();
            for (TextArea tArea : newAnswers) {
                bytes.add(tArea.getText().getBytes(StandardCharsets.UTF_8));
            }
            try {
                controller.getPartDAO().updateParts(controller.getSelected().question_id(), bytes);
            } catch (DataAccessException e) {
                new ErrorPopUp(e.getMessage());
                Platform.exit();
            }
        }
        else{
            throw new IllegalArgumentException("er moet minstens 1 antwoord zijn");
        }
    }

    @Override
    public ArrayList<Part> getPreviewQuestion() {
        ArrayList<Part> previewParts = new ArrayList<>();
        int partId = question.question_id();
        for(TextArea tArea : newAnswers) {
            partId++;
            previewParts.add(new Part(partId, question.question_id(), tArea.getText().getBytes(StandardCharsets.UTF_8)));
        }
        return previewParts;
    }

    @Override
    public void addAnswer(){
        controller.disabledHasChanged(true);
        CheckBox checkBox = new CheckBox();
        checkBoxes.add(checkBox);

        TextArea tArea = new TextArea();
        tArea.setPrefHeight(60);
        tArea.setPrefWidth(250);
        newAnswers.add(tArea);

        loadNewAnswers(newAnswers, checkBoxes);
    }
}
