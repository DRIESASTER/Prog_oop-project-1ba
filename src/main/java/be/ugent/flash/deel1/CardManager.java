package be.ugent.flash.deel1;

import be.ugent.flash.View;
import be.ugent.flash.deel2.ErrorPopUp;
import be.ugent.flash.deel1.cards.*;
import be.ugent.flash.db.*;
import be.ugent.flash.deel1.controllers.*;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class CardManager {

    private QuestionDAO questionDAO;
    private PartDAO partDAO;
    private ArrayList<Question> questions;
    private Card card;
    private View view;
    private Boolean verbetering = null;
    private Question currentQuestion;


    //map die nieuwe kaarten aanmaakt overeenkomstig met het question_type
    Map<String, Function<Question, Card>> cards = Map.of(
            "mcs" , McsCard::new,
            "mcc", MccCard::new,
            "mci", MciCard::new,
            "mr", MrCard::new,
            "open", OpenCard::new,
            "openi", OpeniCard::new
    );

    //map met controllers
    Map<String, Function<CardManager, Controller>> controllers = Map.of(
            "mcs", McsController::new,
            "mcc", MccController::new,
            "mci", MciController::new,
            "mr", MrController::new,
            "open", OpenController::new,
            "openi", OpeniController::new
    );

    //map met question_types gelinkt aan de overeenkomstige fxml files
    Map<String, String> fxmlMap = Map.of(
            "mcs", "/be/ugent/flash/deel1FXML/Mcs.fxml",
            "mcc", "/be/ugent/flash/deel1FXML/Mcc.fxml",
            "mci", "/be/ugent/flash/deel1FXML/Mci.fxml",
            "mr", "/be/ugent/flash/deel1FXML/Mr.fxml",
            "open", "/be/ugent/flash/deel1FXML/OpenType.fxml",
            "openi", "/be/ugent/flash/deel1FXML/OpenType.fxml"
    );


    public CardManager(DataAccessContext DAC, View view){
        questionDAO = DAC.getQuestionDAO();
        partDAO = DAC.getPartDAO();
        this.view = view;

        //maakt arraylist van alle questions
        try {
            questions = questionDAO.allQuestions();
        } catch (DataAccessException e) {
            new ErrorPopUp(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void displayNext() {
        if(questions.size() == 0){
            Platform.exit();
        }
        else {
            //vraag kaartType en huidige vraag
            currentQuestion = questions.get(0);

            String type = currentQuestion.question_type();
            int question_id = currentQuestion.question_id();

            //haalt de correctie factory voor de volgende kaart uit de map en maakt nieuwe kaart van dat type aan met als constructor de eerstvolgende kaart in de ArrayList(questions)
            card = cards.get(type).apply(currentQuestion);
            //geef PartDAO door aan card
            try {
                card.setParts(partDAO.selectParts(question_id));
            } catch (DataAccessException e) {
                new ErrorPopUp(e.getMessage());
                Platform.exit();
            }

            //maak de correcte controller aan
            Controller controller = controllers.get(type).apply(this);

            //roep de juiste scene-zetter op in view
            view.changeScene(fxmlMap.get(type), controller);

            questions.remove(0);
        }
    }

    public Card getCard(){
        return card;
    }

    public Boolean getVerbetering(){
        return verbetering;
    }

    public void setVerbetering(Boolean correct){
        verbetering = correct;
        if(!verbetering){
            questions.add(currentQuestion);
        }
    }

    public void preview(Question previewQuestion, ArrayList<Part> previewParts){
        questions = new ArrayList<>(Arrays.asList(previewQuestion));

        String type = previewQuestion.question_type();
        int question_id = previewQuestion.question_id();

        //haalt de correctie factory voor de volgende kaart uit de map en maakt nieuwe kaart van dat type aan met als constructor de eerstvolgende kaart in de ArrayList(questions)
        card = cards.get(type).apply(previewQuestion);
        //geef PartDAO door aan card
        card.setParts(previewParts);

        //maak de correcte controller aan
        Controller controller = controllers.get(type).apply(this);

        //roep de juiste scene-zetter op in view
        view.changeScene(fxmlMap.get(type), controller);

        controller.disableInput();
    }

}
