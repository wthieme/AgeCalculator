package nl.whitedove.agecalculator

import org.joda.time.DateTime

internal class Persoon {
    private var id = 0
    private var naam: String? = null
    private var gebdatum: DateTime? = null
    private var geselecteerd: Boolean? = null
    private var gevinkt: Boolean? = null
    private var getoond: Boolean? = null
    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getNaam(): String? {
        return naam
    }

    fun setNaam(naam: String?) {
        this.naam = naam
    }

    fun getGebdatum(): DateTime? {
        return gebdatum
    }

    fun setGebdatum(gebdatum: DateTime?) {
        this.gebdatum = gebdatum
    }

    fun getGeselecteerd(): Boolean? {
        return geselecteerd
    }

    fun setGeselecteerd(geselecteerd: Boolean?) {
        this.geselecteerd = geselecteerd
    }

    fun getGetoond(): Boolean? {
        return getoond
    }

    fun setGetoond(getoond: Boolean?) {
        this.getoond = getoond
    }

    fun getGevinkt(): Boolean? {
        return gevinkt
    }

    fun setGevinkt(gevinkt: Boolean?) {
        this.gevinkt = gevinkt
    }
}