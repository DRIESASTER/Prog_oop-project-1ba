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

public class MccBox extends MultiBox {
    //alle huidige mogelijke antwoorden (niet perse opgeslagen)
    private ArrayList<TextField> newAnswers = new ArrayList<>();

    public MccBox(GeopendeDBController controller) {
        super(controller);
        createBox();
    }

    @Override
    public void createBox() {
        ScrollPane scrollPane = new ScrollPane(answerPane);
        int rowCounter = 0;
        //voor elk antwoord in de db een textfield toevoegen
        for(Part answer : answers) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(rowCounter == Integer.parseInt(correctAnswer));
            checkBox.setOnAction((e) -> controller.disabledHasChanged(true));
            checkBoxes.add(checkBox);
            TextField tField = new TextField();
            try{
                tField.setText((new String(answer.part())));
            }catch(Exception e){
                //als part leeg is moet hij de tekst niet instellen
            }
            tField.setOnKeyTyped((e) -> controller.disabledHasChanged(true));
            newAnswers.add(tField);
            rowCounter++;
        }
        loadNewAnswers(newAnswers, checkBoxes);
        controller.addTopartBox(scrollPane);

        Button addButton = new Button("Optie toevoegen");
        addButton.setOnAction((e) -> addAnswer());
        controller.addTopartBox(addButton);
    }


    @Override
    public void boxOpslaan(PartDAO pDAO) {
        ArrayList<CheckBox> selectedBoxes = getSelectedBoxes();
        //checkt of er exact 1 correct antwoord aangeduid is
        if (selectedBoxes.size() != 1) {
            throw new IllegalArgumentException("er moet juist 1 correct antwoord aangeduid zijn");
        } else {
            //antwoord = string van index van geselecteerde checkbox
            correctAnswer = String.valueOf(checkBoxes.indexOf(selectedBoxes.get(0)));
            ArrayList<byte[]> bytes = new ArrayList<>();
            for (TextField tField : newAnswers) {
                bytes.add(tField.getText().getBytes(StandardCharsets.UTF_8));
            }
            try {
                controller.getPartDAO().updateParts(controller.getSelected().question_id(), bytes);
            } catch (DataAccessException e) {
                new ErrorPopUp(e.getMessage());
                Platform.exit();
            }
        }
    }

    //voegt nieuw antwoord toe zonder het op te slaan in de db
    public void addAnswer(){
        controller.disabledHasChanged(true);
        CheckBox checkBox = new CheckBox();
        checkBoxes.add(checkBox);

        TextField tField = new TextField();
        newAnswers.add(tField);
        loadNewAnswers(newAnswers, checkBoxes);
    }

    public void removeAnswer(Node node, ArrayList<? extends Node> newAnswers){
        controller.disabledHasChanged(true);
        answerPane.getChildren().removeAll(answerPane.getChildren());
        checkBoxes.remove(newAnswers.indexOf(node));
        newAnswers.remove(node);
        loadNewAnswers(newAnswers, checkBoxes);
    }

    @Override
    public ArrayList<Part> getPreviewQuestion() {
        ArrayList<Part> previewParts = new ArrayList<>();
        int partId = question.question_id();
        for(TextField tField : newAnswers) {
            partId++;
            previewParts.add(new Part(partId, question.question_id(), tField.getText().getBytes(StandardCharsets.UTF_8)));
        }
        return previewParts;
    }

}
