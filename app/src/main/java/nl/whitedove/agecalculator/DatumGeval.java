package nl.whitedove.agecalculator;

import org.joda.time.DateTime;

class DatumGeval {

    private Helper.eenheidType Eenheid;
    private DateTime DatumTijd;
    private long Aantal;
    private Helper.animatie Animatie;
    private DateTime AnimatieStartTijd;

    DatumGeval(Helper.eenheidType eenheid, DateTime datumTijd, long aantal) {
        this.setEenheid(eenheid);
        this.setDatumTijd(datumTijd);
        this.setAantal(aantal);
        this.setAnimatie(Helper.animatie.waiting);
    }

    Helper.animatie getAnimatie() {
        return Animatie;
    }

    void setAnimatieStartTijd(DateTime animatieStartTijd) {
        AnimatieStartTijd = animatieStartTijd;
    }

    DateTime getAnimatieStartTijd() {
        return AnimatieStartTijd;
    }

    void setAnimatie(Helper.animatie animatie) {
        Animatie = animatie;
    }

    Helper.eenheidType getEenheid() {
        return Eenheid;
    }

    private void setEenheid(Helper.eenheidType eenheid) {
        Eenheid = eenheid;
    }

    DateTime getDatumTijd() {
        return DatumTijd;
    }

    private void setDatumTijd(DateTime datumTijd) {
        DatumTijd = datumTijd;
    }

    long getAantal() {
        return Aantal;
    }

    private void setAantal(long aantal) {
        Aantal = aantal;
    }
}