package be.ugent.flash.deel2.PartBoxes;

import be.ugent.flash.db.Part;
import be.ugent.flash.db.PartDAO;
import be.ugent.flash.deel2.Box;
import be.ugent.flash.deel2.GeopendeDBController;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract
class OpenTypeBox extends Box {

    protected TextField antwoordVeld;

    public OpenTypeBox(GeopendeDBController controller) {
        super(controller);
        createBox();
    }

    public void createBox(){
        antwoordVeld = new TextField(question.correct_answer());
        antwoordVeld.setOnKeyTyped((e) -> controller.disabledHasChanged(true));
        Label label = new Label("Correct antwoord");
        controller.addTopartBox(new HBox(label, antwoordVeld));
    }

    @Override
    public abstract void boxOpslaan(PartDAO pDAO);

    @Override
    public ArrayList<Part> getPreviewQuestion() {
        ArrayList<Part> previewParts = new ArrayList<>();
        int partId = question.question_id();
        previewParts.add(new Part(question.question_id()+1, question.question_id(), antwoordVeld.getText().getBytes(StandardCharsets.UTF_8)));
        return previewParts;
    }

}
