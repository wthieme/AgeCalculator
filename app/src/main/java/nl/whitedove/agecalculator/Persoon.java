package nl.whitedove.agecalculator;

import org.joda.time.DateTime;

class Persoon {

    private int id;
    private String naam;
    private DateTime gebdatum;
    private Boolean geselecteerd;
    private Boolean getoond;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getNaam() {
        return naam;
    }

    void setNaam(String naam) {
        this.naam = naam;
    }

    DateTime getGebdatum() {
        return gebdatum;
    }

    void setGebdatum(DateTime gebdatum) {
        this.gebdatum = gebdatum;
    }

    Boolean getGeselecteerd() {
        return geselecteerd;
    }

    void setGeselecteerd(Boolean geselecteerd) {
        this.geselecteerd = geselecteerd;
    }

    Boolean getGetoond() {
        return getoond;
    }

    void setGetoond(Boolean getoond) {
        this.getoond = getoond;
    }
}
