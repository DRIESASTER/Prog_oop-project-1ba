package be.ugent.flash.deel2;

import be.ugent.flash.db.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Map;

public class GeopendeDBController extends Menu {

    Map<String, String> typeMap = Map.of(
            "mcs", "Meerkeuze (standaard)",
            "mcc", "Meerkeuze (compact)",
            "mci", "Meerkeuze (afbeelding)",
            "mr", "Meerantwoord",
            "open", "Open (tekst)",
            "openi", "Open (geheel)"
    );


    public TableColumn<Question, String> titelKolom;
    public TableColumn<Question, String> typeKolom;
    public TableView<Question> tabel;
    public TextField titel;
    public Label type;
    public TextArea tekst;
    public Button vraagVerwijderen;
    public Button herstelButton;
    public Button vraagToevoegenButton;
    public Button opslaanButton;
    public Button previewKnop;
    public VBox rechterhelft;
    public VBox partBox;

    public Label emptyIndicator;
    public VBox afbeeldingBox;
    public Button afbeeldingToevoegen = new Button();
    public RowConstraints afbeeldingRij;
    public GridPane rechterBovenkant;
    private byte[] currentImage;
    private ArrayList<Question> questions;
    private DBEditor editor;
    private QuestionDAO qDAO;
    private PartDAO pDAO;
    private Selecter selecter;


    private SimpleBooleanProperty disabledTabel = new SimpleBooleanProperty(false);

    public GeopendeDBController(DBEditor editor){
        super(editor.getStage());
        this.editor = editor;
        qDAO = editor.getQDAO();
        pDAO = editor.getPDAO();
        //afbeelding toevoegen knop instellen
        afbeeldingToevoegen.setText("voeg een afbeelding toe");
    }

    public void initialize(){
        //ImageProcessor aanmaken
        ImageProcessor imgProcessor = new ImageProcessor(this, afbeeldingBox, rechterBovenkant);
        //Selecter aanmaken
        selecter = new Selecter(this, afbeeldingBox, imgProcessor);
        //disable tabel als er iets is aangepast
        //tabel inladen
        tabel.disableProperty().bind(disabledTabel);
        vraagToevoegenButton.disableProperty().bind(disabledTabel);
        herstelButton.disableProperty().bind(disabledTabel.not());
        opslaanButton.disableProperty().bind(disabledTabel.not());
        previewKnop.disableProperty().bind(vraagVerwijderen.disableProperty());

        titel.setOnKeyTyped((e) -> disabledHasChanged(true));
        tekst.setOnKeyTyped((e) -> disabledHasChanged(true));
        laadTabel();
    }

    public void laadTabel(){

        rechterhelftZichtbaar(false);

        ObservableList<Question> model = FXCollections.observableArrayList();

        for ( int i = 0; i<tabel.getItems().size(); i++) {
            tabel.getItems().clear();
        }
        questions = editor.getQuestions();
        titelKolom.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().title())
        );
        typeKolom.setCellValueFactory(
                p -> new SimpleStringProperty(typeMap.get(p.getValue().question_type()))
        );
        model.addAll(questions);
        tabel.setItems(model);
        //voeg listener toe voor selected question in de tabel
        tabel.getSelectionModel().selectedItemProperty().addListener(this::select);

        //disable knop vraagverwijderen als er niks selected is
        vraagVerwijderen.disableProperty().bind(tabel.getSelectionModel().selectedItemProperty().isNull());
        //this.rechterhelftZichtbaar().bind(tabel.getSelectionModel().selectedItemProperty().isNull());
    }

    private void select(Observable observable) {
        //tabel.disableProperty().bind(new SimpleBooleanProperty(tabel.getSelectionModel().getSelectedItem() != null && !tabel.getSelectionModel().getSelectedItem().title().equals(titel.getText())));
        selecter.selectedManager(tabel.getSelectionModel().selectedItemProperty());
    }


    public void vraagToevoegen(){
        QuestionAdder qAdder = new QuestionAdder(qDAO, this);
    }

    public void vraagVerwijderen() {
        disabledHasChanged(false);
        int questionId = tabel.getSelectionModel().getSelectedItem().question_id();
        try {
            qDAO.deleteQuestion(questionId);
            pDAO.deleteAllParts(questionId);
        } catch (DataAccessException e) {
            new ErrorPopUp(e.getMessage());
            throw new RuntimeException(e);
        }
        laadTabel();
    }


    //slaat algemeen deel op, titel, text, image en laad tabel opniew in
    public void algemeenDeelOpslaan() {
        int questionId = tabel.getSelectionModel().getSelectedItem().question_id();
        try {
            qDAO.updateTitel(questionId, titel.getText());
            qDAO.updateText(questionId, tekst.getText());
            qDAO.updateImage(questionId, currentImage);
        } catch (DataAccessException e) {
            new ErrorPopUp(e.getMessage());
            Platform.exit();
        }

        laadTabel();
        disabledHasChanged(false);
    }

    public void opslaan(){
        int questionId = tabel.getSelectionModel().getSelectedItem().question_id();
        try{
            selecter.getCurrentBox().boxOpslaan(editor.getPDAO());
            qDAO.updateCorrectAnswer(questionId, selecter.getCurrentBox().getCorrectAnswer());
            algemeenDeelOpslaan();
        } catch(IllegalArgumentException | DataAccessException e){
            new ErrorPopUp(e.getMessage());
        }
    }


    public void herstellen(){
        //laad de huidge vraag opnieuw in
        disabledHasChanged(false);
        selecter.selectedManager(tabel.getSelectionModel().selectedItemProperty());
    }

    public void preview(){
        ArrayList<Part> previewParts = selecter.getPreviewQuestion();
        String correctAnswer = selecter.getCurrentBox().correctAnswer;
        Question q = new Question(getSelected().question_id(), titel.getText(),tekst.getText() , currentImage, getSelected().question_type(), correctAnswer);
        editor.preview(q, previewParts);
    }

    public void closePreview(){
        editor.closePreview();
    }

    public void rechterhelftZichtbaar(Boolean zichtbaar){
        //maak de rechterkant (on)zichtbaar
        rechterBovenkant.setPrefHeight(172);
        for(Node node : rechterhelft.getChildren()){
            node.setVisible(zichtbaar);
        }
        emptyIndicator.setVisible(!zichtbaar);
    }

    public void disabledHasChanged(boolean changed){
        disabledTabel.set(changed);
    }

    public void setTitel(String text){
        titel.setText(text);
    }

    public void setType(String text){
        type.setText(text);
    }

    public void setTekst(String text){
        tekst.setText(text);
    }

    public void addTopartBox(Node node){
        partBox.getChildren().add(node);
    }

    public void emptyPartBox(){
        partBox.getChildren().removeAll(partBox.getChildren());
    }

    public Question getSelected(){
        return tabel.getSelectionModel().getSelectedItem();
    }

    public PartDAO getPartDAO(){
        return editor.getPDAO();
    }

    public void setCurrentImage(byte[] byteArray){
        currentImage = byteArray;
    }
}
