package nl.whitedove.agecalculator

import android.content.Context
import android.widget.Toast
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

internal object Helper {
    private const val DEBUG = false
    val dtFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")
    val dtmFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm")
    val dFormat = DateTimeFormat.forPattern("dd-MM-yyyy")
    val tFormatHhMmSs = DateTimeFormat.forPattern("HH:mm:ss")
    const val Duration = 10000
    const val maxRijen = 5
    var dgLijst: ArrayList<DatumGeval> = ArrayList()
    var Nr = 1
    private fun Log(log: String?) {
        if (DEBUG) {
            println(log)
        }
    }

    fun showMessage(cxt: Context?, melding: String?, isLong: Boolean) {
        Log(melding)
        val duration = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        val toast = Toast.makeText(cxt, melding, duration)
        toast.show()
    }

    fun numToString(aantal: Long): String? {
        if (aantal < 1000) return String.format("%s", aantal)
        if (aantal < 1000000 && aantal % 1000 != 0L) return String.format("%s", aantal)
        if (aantal < 1000000 && aantal % 1000 == 0L) return String.format("%sk", aantal / 1000)
        if (aantal < 1000000000 && aantal % 1000000 != 0L) return String.format("%sk", aantal / 1000)
        if (aantal < 1000000000 && aantal % 1000000 == 0L) return String.format("%sM", aantal / 1000000)
        return if (aantal < 1000000000000L && aantal % 1000000000 != 0L) String.format("%sM", aantal / 1000000) else String.format("%sG", aantal / 1000000000)
    }

    fun makeDgList(personen: ArrayList<Persoon>) {
        if (dgLijst.size > 0) return
        dgLijst = ArrayList()
        val today = DateTime.now()
        var thatDay = today
        var d: DateTime?
        var dg: DatumGeval
        for (persoon in personen) {
            thatDay = thatDay.minus(today.millis - persoon.getGebdatum()!!.millis)
        }
        val factor = personen.size
        if (factor == 0) {
            return
        }
        for (y in 0..504) {
            d = thatDay.plusYears(y)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.year, d, y.toLong())
            dgLijst.add(dg)
        }
        val maanden = intArrayOf(1, 10, 100, 200, 400, 500, 700, 800,
                1000, 1100, 1300, 1400, 1600, 1700, 1900,
                2000, 2200, 2300, 2500, 2600, 2800, 2900,
                3100, 3200, 3400, 3500, 3700, 3800,
                4000, 4100, 4300, 4400, 4600, 4700, 4900, 5000)
        for (maand in maanden) {
            d = thatDay.plusMonths(maand)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.month, d, maand.toLong())
            dgLijst.add(dg)
        }
        val weken = intArrayOf(1, 10, 100, 200, 300, 400, 500, 600, 700, 800, 900,
                1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900,
                2000, 2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800, 2900,
                3000, 3100, 3200, 3300, 3400, 3500, 3600, 3700, 3800, 3900,
                4000, 4100, 4200, 4300, 4400, 4500, 4600, 4700, 4800, 4900,
                5000, 5100, 5200, 5300, 5400, 5500, 5600, 5700, 5800, 5900,
                6000, 6100, 6200, 6300, 6400, 6500, 6600, 6700, 6800, 6900,
                7000, 7100, 7200, 7300, 7400, 7500, 7600, 7700, 7800, 7900,
                8000, 8100, 8200, 8300, 8400, 8500, 8600, 8700, 8800, 8900,
                9000, 9100, 9200, 9300, 9400, 9500, 9600, 9700, 9800, 9900,
                10000, 11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000, 19000,
                20000, 21000, 22000, 23000, 24000, 25000, 26000, 27000, 28000, 29000)
        for (week in weken) {
            d = thatDay.plusWeeks(week)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.week, d, week.toLong())
            dgLijst.add(dg)
        }
        // Let op er ontbreken expres een aantal waarden (1000 weken = 7000 dagen etc)
        val dagen = intArrayOf(10, 100, 1000, 2000, 3000, 4000, 5000, 6000, 8000, 9000, 10000,
                11000, 12000, 13000, 15000, 16000, 17000, 18000, 19000, 20000,
                22000, 23000, 24000, 25000, 26000, 27000, 29000, 30000, 31000, 32000, 33000, 34000, 36000, 37000, 38000, 39000, 40000,
                50000, 60000, 80000, 90000, 100000, 110000, 120000, 130000, 150000, 160000, 170000, 180000, 190000, 200000)
        for (dag in dagen) {
            d = thatDay.plusDays(dag)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.day, d, dag.toLong())
            dgLijst.add(dg)
        }
        val uren = intArrayOf(10, 100, 1000, 10000, 100000, 150000, 200000, 250000, 300000, 350000, 400000, 450000,
                500000, 550000, 650000, 700000, 750000, 800000, 850000, 900000,
                1000000, 1500000, 2000000, 2500000, 3000000, 3500000, 4000000, 4500000, 5000000)
        for (uur in uren) {
            d = thatDay.plusHours(uur)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.hour, d, uur.toLong())
            dgLijst.add(dg)
        }
        val minuten = intArrayOf(100, 1000, 10000, 100000, 1000000, 10000000, 20000000, 40000000, 50000000, 70000000, 80000000,
                100000000, 110000000, 130000000, 140000000, 160000000, 170000000, 190000000,
                200000000, 210000000, 220000000, 230000000, 250000000, 260000000, 280000000, 290000000)
        for (minuut in minuten) {
            d = thatDay.plusMinutes(minuut)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.minute, d, minuut.toLong())
            dgLijst.add(dg)
        }
        val seconden = arrayOf(1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 200000000L,
                300000000L, 400000000L, 500000000L, 700000000L, 800000000L, 1000000000L,
                1100000000L, 1300000000L, 1400000000L, 1500000000L, 1600000000L, 1700000000L, 1900000000L,
                2000000000L, 2100000000L, 2200000000L, 2300000000L, 2500000000L, 2600000000L, 2700000000L, 2800000000L, 2900000000L,
                3100000000L, 3200000000L, 3300000000L, 3400000000L, 3500000000L, 3700000000L, 3800000000L, 3900000000L,
                4000000000L, 4100000000L, 4300000000L, 4400000000L, 4500000000L, 4600000000L, 4700000000L, 4900000000L,
                5000000000L, 5100000000L, 5200000000L, 5300000000L, 5500000000L, 5600000000L, 5700000000L, 5800000000L, 5900000000L,
                6100000000L, 6200000000L, 6300000000L, 6400000000L, 6500000000L, 6700000000L, 6800000000L, 6900000000L,
                7000000000L, 7100000000L, 7300000000L, 7400000000L, 7500000000L, 7600000000L, 7700000000L, 7900000000L,
                8000000000L, 8100000000L, 8200000000L, 8300000000L, 8500000000L, 8600000000L, 8700000000L, 8800000000L, 8900000000L,
                9100000000L, 9200000000L, 9300000000L, 9400000000L, 9500000000L, 9700000000L, 9800000000L, 9900000000L,
                10000000000L, 10100000000L, 10200000000L, 10300000000L, 10400000000L, 10500000000L, 10600000000L, 10700000000L, 10800000000L, 10900000000L,
                11000000000L, 11100000000L, 11200000000L, 11300000000L, 11400000000L, 11500000000L, 11600000000L, 11700000000L, 11800000000L, 11900000000L,
                12000000000L, 12100000000L, 12200000000L, 12300000000L, 12400000000L, 12500000000L, 12600000000L, 12700000000L, 12800000000L, 12900000000L,
                13000000000L, 13100000000L, 13200000000L, 13300000000L, 13400000000L, 13500000000L, 13600000000L, 13700000000L, 13800000000L, 13900000000L,
                14000000000L, 14100000000L, 14200000000L, 14300000000L, 14400000000L, 14500000000L, 14600000000L, 14700000000L, 14800000000L, 14900000000L)
        for (seconde in seconden) {
            d = thatDay.plus(1000 * seconde)
            if (today.isAfter(d)) continue
            val verschil = (d.millis - today.millis) / factor
            d = today.plus(verschil)
            dg = DatumGeval(eenheidType.second, d, seconde)
            dgLijst.add(dg)
        }

        val timesList = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10)

        val aantal = personen.count()
        for (i in 0 until aantal) {
            val p1 = personen[i]
            for (j in i + 1 until aantal) {
                val p2 = personen[j]

                for (times in timesList) {
                    var oudsteDat: DateTime
                    var jongsteDat: DateTime
                    var txtToShow = ""

                    if (p1.getGebdatum()!! < p2.getGebdatum()) {
                        oudsteDat = p1.getGebdatum()!!
                        jongsteDat = p2.getGebdatum()!!
                        txtToShow = p1.getNaam() + " " + times.toString() + "x age " + p2.getNaam()
                    } else {
                        oudsteDat = p2.getGebdatum()!!
                        jongsteDat = p1.getGebdatum()!!
                        txtToShow = p2.getNaam() + " " + times.toString() + "x age " + p1.getNaam()
                    }
                    val datVerschil = berekenVerschil(oudsteDat, jongsteDat, times)
                    if (datVerschil.isAfter(today)) {
                        dg = DatumGeval(eenheid = eenheidType.second,
                                datumTijd = datVerschil,
                                kind = Helper.kindType.relative,
                                textToShow = txtToShow)
                        dgLijst.add(dg)

                    }
                }

                Collections.sort(dgLijst) { lhs, rhs -> lhs.getDatumTijd()!!.compareTo(rhs.getDatumTijd()) }
            }
        }
    }

    fun berekenVerschil(oudsteDat: DateTime, jongsteDat: DateTime, aantal: Int): DateTime {
        val oudsteMs = oudsteDat.millis
        val jongsteMs = jongsteDat.millis
        val resultMs = oudsteMs + ((aantal / (aantal - 1.0)) * (jongsteMs - oudsteMs))
        val resultL = resultMs.toLong()
        val result = DateTime(resultL)
        return result
    }


    internal enum class eenheidType {
        year, month, week, day, hour, minute, second
    }

    internal enum class kindType {
        absolute, relative
    }

    internal enum class animatie {
        waiting, running, finished
    }
}