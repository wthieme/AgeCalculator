package nl.whitedove.agecalculator

import nl.whitedove.agecalculator.Helper.Animatie
import nl.whitedove.agecalculator.Helper.EenheidType
import org.joda.time.DateTime

internal class DatumGeval(eenheid: EenheidType? = null,
                          datumTijd: DateTime?,
                          aantal: Long = 0,
                          kind: Helper.KindType = Helper.KindType.Absolute,
                          textToShow: String = "") {
    private var _eenheid: EenheidType? = null
    private var _datumTijd: DateTime? = null
    private var _aantal: Long = 0
    private var _animatie: Animatie? = null
    private var _animatieStartTijd: DateTime? = null
    private var _kind: Helper.KindType = Helper.KindType.Absolute
    private var _textToShow: String = ""

    fun setAnimatieStartTijd(animatieStartTijd: DateTime?) {
        _animatieStartTijd = animatieStartTijd
    }

    fun getAnimatieStartTijd(): DateTime? {
        return _animatieStartTijd
    }

    fun getAnimatie(): Animatie? {
        return _animatie
    }

    fun setAnimatie(animatie: Animatie?) {
        _animatie = animatie
    }

    fun getEenheid(): EenheidType? {
        return _eenheid
    }

    private fun setEenheid(eenheid: EenheidType?) {
        _eenheid = eenheid
    }

    fun getDatumTijd(): DateTime? {
        return _datumTijd
    }

    private fun setDatumTijd(datumTijd: DateTime?) {
        _datumTijd = datumTijd
    }

    fun getAantal(): Long {
        return _aantal
    }

    private fun setAantal(aantal: Long) {
        _aantal = aantal
    }

    fun getKind(): Helper.KindType {
        return _kind
    }

    private fun setKind(kind: Helper.KindType) {
        _kind = kind
    }

    fun getTextToShow(): String {
        return _textToShow
    }

    private fun setTextToShow(textToShow: String) {
        _textToShow = textToShow
    }

    init {
        setEenheid(eenheid)
        setDatumTijd(datumTijd)
        setAantal(aantal)
        setAnimatie(Animatie.Waiting)
        setKind(kind)
        setTextToShow(textToShow)
    }
}