package nl.whitedove.agecalculator

import nl.whitedove.agecalculator.Helper.animatie
import nl.whitedove.agecalculator.Helper.eenheidType
import org.joda.time.DateTime

internal class DatumGeval(eenheid: eenheidType?, datumTijd: DateTime?, aantal: Long) {
    private var Eenheid: eenheidType? = null
    private var DatumTijd: DateTime? = null
    private var Aantal: Long = 0
    private var Animatie: animatie? = null
    private var AnimatieStartTijd: DateTime? = null
    fun getAnimatie(): animatie? {
        return Animatie
    }

    fun setAnimatieStartTijd(animatieStartTijd: DateTime?) {
        AnimatieStartTijd = animatieStartTijd
    }

    fun getAnimatieStartTijd(): DateTime? {
        return AnimatieStartTijd
    }

    fun setAnimatie(animatie: animatie?) {
        Animatie = animatie
    }

    fun getEenheid(): eenheidType? {
        return Eenheid
    }

    private fun setEenheid(eenheid: eenheidType?) {
        Eenheid = eenheid
    }

    fun getDatumTijd(): DateTime? {
        return DatumTijd
    }

    private fun setDatumTijd(datumTijd: DateTime?) {
        DatumTijd = datumTijd
    }

    fun getAantal(): Long {
        return Aantal
    }

    private fun setAantal(aantal: Long) {
        Aantal = aantal
    }

    init {
        setEenheid(eenheid)
        setDatumTijd(datumTijd)
        setAantal(aantal)
        setAnimatie(animatie.waiting)
    }
}