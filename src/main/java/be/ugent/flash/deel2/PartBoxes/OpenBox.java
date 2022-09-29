package be.ugent.flash.deel2.PartBoxes;

import be.ugent.flash.db.PartDAO;
import be.ugent.flash.deel2.GeopendeDBController;

public class OpenBox extends OpenTypeBox {

    public OpenBox(GeopendeDBController controller){
        super(controller);
    }

    @Override
    public void boxOpslaan(PartDAO pDAO) {
        //check of antwoord veld nie leeg is, anders popup
        if(!antwoordVeld.getText().isBlank()) {
            setCorrectAnswer(antwoordVeld.getText());
        }
        else{
            throw new IllegalArgumentException("antwoord mag niet leeg zijn!");
        }
    }

}
