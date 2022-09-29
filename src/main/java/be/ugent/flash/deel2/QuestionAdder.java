package be.ugent.flash.deel2;

import be.ugent.flash.db.DataAccessException;
import be.ugent.flash.db.Question;
import be.ugent.flash.db.QuestionDAO;
import be.ugent.flash.deel2.ErrorPopUp;
import be.ugent.flash.deel2.GeopendeDBController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuestionAdder {

    private String[] types = {"Meerkeuze (standaard)", "Meerkeuze (compact)", "Meerkeuze (afbeelding)", "Meerantwoord", "Open (tekst)", "Open (geheel)"};
    private String[] indexToCode = {"mcs", "mcc", "mci", "mr", "open", "openi"};

    private VBox mainPane = new VBox();
    private GridPane addPane = new GridPane();
    private TextField titel = new TextField();
    private ComboBox typeSelecter = new ComboBox(FXCollections.observableArrayList(types));
    private QuestionDAO qDAO;
    private Stage stage = new Stage();
    private GeopendeDBController controller;

    public QuestionAdder(QuestionDAO qDAO, GeopendeDBController controller){
        //startpopup om vraagtype te laten selecteren
        this.qDAO = qDAO;
        this.controller = controller;
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        mainPane.setPrefSize(300,140);
        mainPane.setSpacing(15);
        mainPane.setAlignment(Pos.CENTER);

        Label vraagTitel = new Label("VraagTitel");

        Button ok = new Button("OK");
        ok.setOnAction((e) -> voegVraagToe());

        Button annuleren = new Button("annuleren");
        annuleren.setOnAction((e) -> stage.close());

        HBox buttons = new HBox(ok, annuleren);

        addPane.addRow(0, new Label("VraagTitel:"), titel);
        addPane.addRow(1, new Label("VraagType:"), typeSelecter);
        //empty label om hbox enkel in 2e kolom te krijgen
        addPane.addRow(2, new Label(""), buttons);
        addPane.setHgap(10);
        addPane.setVgap(5);

        Label header = new Label("Vraag Toevoegen");
        header.setFont(Font.font(18));

        mainPane.getChildren().addAll(header, addPane);
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);

        typeSelecter.getSelectionModel().select(0);

        stage.showAndWait();
    }


    private void voegVraagToe(){
        //gekozen vraagtype
        String newType = indexToCode[typeSelecter.getSelectionModel().getSelectedIndex()];

        Question newQuestion = new Question(-1, titel.getText(), null, null, newType, null);

        int qId = -1;
        //voeg vraag toe aan db
        try {
            qId = qDAO.addQuestion(newQuestion);
            controller.getPartDAO().addEmptyPart(qId);
        } catch (DataAccessException e) {
            new ErrorPopUp(e.getMessage());
            Platform.exit();
        }

        //laad tabel opniew, selecter laatste vraag meteen, popup mag weg
        controller.laadTabel();
        controller.tabel.getSelectionModel().select(controller.tabel.getItems().size()-1);
        stage.close();
    }

}



