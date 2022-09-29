package be.ugent.flash.deel2.PartBoxes;

import be.ugent.flash.db.DataAccessException;
import be.ugent.flash.db.Part;
import be.ugent.flash.db.PartDAO;
import be.ugent.flash.deel2.ErrorPopUp;
import be.ugent.flash.deel2.GeopendeDBController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class MciBox extends MultiBox {

    private ArrayList<ImageView> newAnswers = new ArrayList<>();
    private ArrayList<byte []> imageBytes = new ArrayList<>();

    private static Image textToImage(String text) {
        Label label = new Label(text);

        label.setFont(Font.font(35));

        label.setWrapText(true);
        Scene scene = new Scene(new Group(label));
        WritableImage img = new WritableImage(80, 50);
        scene.snapshot(img);
        return img ;
    }

    public MciBox(GeopendeDBController controller) {
        super(controller);
        createBox();
    }

    @Override
    public void createBox() {
        answerPane = new GridPane();
        ScrollPane scrollPane = new ScrollPane(answerPane);
        int rowCounter = 0;
        for(Part answer : answers) {
            CheckBox checkBox = new CheckBox();
            checkBox.setOnAction((e) -> controller.disabledHasChanged(true));
            checkBox.setSelected(rowCounter == Integer.parseInt(correctAnswer));
            checkBoxes.add(checkBox);

            ImageView view = new ImageView();
            view.setPreserveRatio(true);
            view.setFitHeight(60);
            Image img = null;
            try{
                img = new Image(new ByteArrayInputStream(answer.part()));
            }catch (Exception e){
                //part is null dus kan image niet aanmaken, zet lege afbeelding
                img = textToImage("(leeg)");
            }
            view.setImage(img);

            view.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                    if(mouseEvent.getButton() == MouseButton.PRIMARY){
                        if(mouseEvent.getClickCount() == 2) {
                            changeImage(view);
                        }
                    }
                }
            });

            newAnswers.add(view);
            imageBytes.add(answer.part());

            rowCounter++;
        }
        loadNewAnswers(newAnswers, checkBoxes);
        controller.addTopartBox(scrollPane);

        Button addButton = new Button("Optie toevoegen");
        addButton.setOnAction((e) -> addAnswer());
        controller.addTopartBox(addButton);
    }


    public void boxOpslaan(PartDAO pDAO) {
        ArrayList<CheckBox> selectedBoxes = getSelectedBoxes();

        if (selectedBoxes.size() != 1) {
            throw new IllegalArgumentException("er moet juist 1 correct antwoord aangeduid zijn");
        } else {
            //antwoord = string van index van geselecteerde checkbox
            correctAnswer = String.valueOf(checkBoxes.indexOf(selectedBoxes.get(0)));
            ArrayList<byte[]> bytes = new ArrayList<>();
            for (byte[] byteImg : imageBytes) {
                bytes.add(byteImg);
            }
            try {
                controller.getPartDAO().updateParts(controller.getSelected().question_id(), bytes);
            } catch (DataAccessException e) {
                new ErrorPopUp(e.getMessage());
                Platform.exit();
            }
        }
    }

    @Override
    public ArrayList<Part> getPreviewQuestion() {
        ArrayList<Part> previewParts = new ArrayList<>();
        int partId = question.question_id();
        for(byte[] bytes : imageBytes) {
            partId++;
            previewParts.add(new Part(partId, question.question_id(), bytes));
        }
        return previewParts;
    }


    public void addAnswer(){
        controller.disabledHasChanged(true);
        CheckBox checkBox = new CheckBox();
        checkBoxes.add(checkBox);

        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.setFitHeight(60);
        view.setImage(textToImage("(leeg)"));

        view.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.PRIMARY){
                    if(mouseEvent.getClickCount() == 2) {
                        changeImage(view);
                    }
                }
            }
        });

        newAnswers.add(view);
        imageBytes.add(null);

        loadNewAnswers(newAnswers, checkBoxes);
    }

    private void changeImage(ImageView view){
        byte[] newImgBytes = null;
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("afbeeldingen","*.jpeg", "*.png", "*.jpg"));
        File file = chooser.showOpenDialog(view.getScene().getWindow()); if (file != null){
            try{
                controller.disabledHasChanged(true);
                newImgBytes = Files.readAllBytes(file.toPath());
                imageBytes.remove(newAnswers.indexOf(view));
                imageBytes.add(newImgBytes);
                view.setImage(new Image(new ByteArrayInputStream(newImgBytes)));
            } catch(IOException e){
                //geen image
            }
        }
    }

    public void removeAnswer(Node node, ArrayList<? extends Node> newAnswers){
        controller.disabledHasChanged(true);
        answerPane.getChildren().removeAll(answerPane.getChildren());
        checkBoxes.remove(newAnswers.indexOf(node));
        imageBytes.remove(newAnswers.indexOf(node));
        newAnswers.remove(node);
        loadNewAnswers(newAnswers, checkBoxes);
    }
}
