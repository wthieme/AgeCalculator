package nl.whitedove.agecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Helper {

    enum eenheidType {
        year, month, week, day, hour, minute, second
    }

    enum animatie {waiting, running, finished}

    private static final boolean DEBUG = false;
    static final DateTimeFormatter dtFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    static final DateTimeFormatter dtmFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
    static final DateTimeFormatter dFormat = DateTimeFormat.forPattern("dd-MM-yyyy");
    static final DateTimeFormatter tFormatHhMmSs = DateTimeFormat.forPattern("HH:mm:ss");
    static final int Duration = 10000;
    static final int maxRijen = 5;
    static ArrayList<DatumGeval> dgLijst = new ArrayList<>();
    static int Nr = 1;

    private static void Log(String log) {
        if (Helper.DEBUG) {
            System.out.println(log);
        }
    }

    static void ShowMessage(Context cxt, String melding, boolean isLong) {
        Helper.Log(melding);
        int duration = isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(cxt, melding, duration);
        toast.show();
    }

    static String NumToString(long aantal) {

        if (aantal < 1000) return String.format("%s", aantal);

        if (aantal < 1000000 && aantal % 1000 != 0) return String.format("%s", aantal);

        if (aantal < 1000000 && aantal % 1000 == 0) return String.format("%sk", aantal / 1000);

        if (aantal < 1000000000 && aantal % 1000000 != 0)
            return String.format("%sk", aantal / 1000);

        if (aantal < 1000000000 && aantal % 1000000 == 0)
            return String.format("%sM", aantal / 1000000);

        if (aantal < 1000000000000L && aantal % 1000000000 != 0)
            return String.format("%sM", aantal / 1000000);

        return String.format("%sG", aantal / 1000000000);
    }

    static void MakeDgList(ArrayList<Persoon> personen) {

        if (dgLijst != null && dgLijst.size() > 0) return;
        DateTime today = DateTime.now();
        DateTime thatDay = today;
        for (Persoon persoon : personen) {
            thatDay = thatDay.minus(today.getMillis() - persoon.getGebdatum().getMillis());
        }
        dgLijst = new ArrayList<>();
        int factor = today.isBefore(thatDay) ? -1 : 1;
        DateTime d;
        DatumGeval dg;

        for (int y = 0; y < 505; y++) {
            d = thatDay.plusYears(factor * y);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.year, d, y);
            dgLijst.add(dg);
        }

        int[] maanden = {1, 10, 100, 200, 400, 500, 700, 800,
                1000, 1100, 1300, 1400, 1600, 1700, 1900,
                2000, 2200, 2300, 2500, 2600, 2800, 2900,
                3100, 3200, 3400, 3500, 3700, 3800,
                4000, 4100, 4300, 4400, 4600, 4700, 4900, 5000};

        for (int maand : maanden) {
            d = thatDay.plusMonths(factor * maand);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.month, d, maand);
            dgLijst.add(dg);
        }

        int[] weken = {1, 10, 100, 200, 300, 400, 500, 600, 700, 800, 900,
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
                20000, 21000, 22000, 23000, 24000, 25000, 26000, 27000, 28000, 29000};

        for (int week : weken) {
            d = thatDay.plusWeeks(factor * week);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.week, d, week);
            dgLijst.add(dg);
        }

        // Let op er ontbreken expres een aantal waarden (1000 weken = 7000 dagen etc)
        int[] dagen = {10, 100, 1000, 2000, 3000, 4000, 5000, 6000, 8000, 9000, 10000,
                11000, 12000, 13000, 15000, 16000, 17000, 18000, 19000, 20000,
                22000, 23000, 24000, 25000, 26000, 27000, 29000, 30000, 31000, 32000, 33000, 34000, 36000, 37000, 38000, 39000, 40000,
                50000, 60000, 80000, 90000, 100000, 110000, 120000, 130000, 150000, 160000, 170000, 180000, 190000, 200000};

        for (int dag : dagen) {
            d = thatDay.plusDays(factor * dag);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.day, d, dag);
            dgLijst.add(dg);
        }

        int[] uren = {10, 100, 1000, 10000, 100000, 150000, 200000, 250000, 300000, 350000, 400000, 450000,
                500000, 550000, 650000, 700000, 750000, 800000, 850000, 900000,
                1000000, 1500000, 2000000, 2500000, 3000000, 3500000, 4000000, 4500000, 5000000};

        for (int uur : uren) {
            d = thatDay.plusHours(factor * uur);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.hour, d, uur);
            dgLijst.add(dg);
        }

        int[] minuten = {100, 1000, 10000, 100000, 1000000, 10000000, 20000000, 40000000, 50000000, 70000000, 80000000,
                100000000, 110000000, 130000000, 140000000, 160000000, 170000000, 190000000,
                200000000, 210000000, 220000000, 230000000, 250000000, 260000000, 280000000, 290000000};

        for (int minuut : minuten) {
            d = thatDay.plusMinutes(factor * minuut);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.minute, d, minuut);
            dgLijst.add(dg);
        }

        Long[] seconden = {1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 200000000L,
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
                14000000000L, 14100000000L, 14200000000L, 14300000000L, 14400000000L, 14500000000L, 14600000000L, 14700000000L, 14800000000L, 14900000000L};

        for (Long seconde : seconden) {
            d = thatDay.plus(1000 * factor * seconde);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.second, d, seconde);
            dgLijst.add(dg);
        }

        Collections.sort(dgLijst, new Comparator<DatumGeval>() {
            @Override
            public int compare(DatumGeval lhs, DatumGeval rhs) {
                return lhs.getDatumTijd().compareTo(rhs.getDatumTijd());
            }
        });
    }
}