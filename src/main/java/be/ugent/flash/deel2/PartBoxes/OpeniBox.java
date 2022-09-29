package be.ugent.flash.deel2.PartBoxes;

import be.ugent.flash.db.PartDAO;
import be.ugent.flash.deel2.GeopendeDBController;

public class OpeniBox extends OpenTypeBox {

    public OpeniBox(GeopendeDBController controller){
        super(controller);
    }

    @Override
    public void boxOpslaan(PartDAO pDAO) {
        //check of antwoord geheel getal is
        if (antwoordVeld.getText().matches("^-?[0-9]+$")) {
            setCorrectAnswer(antwoordVeld.getText());
        } else {
            throw new IllegalArgumentException("antwoord moet een geheel getal zijn!");
        }
    }

}
