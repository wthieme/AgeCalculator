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
    static final int maxRijen = 3;
    static ArrayList<DatumGeval> dgLijst = new ArrayList<>();
    static DateTime peildatum;
    static ArrayList<Persoon> Personen;
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

    static void MakeDgList() {

        if (dgLijst != null && dgLijst.size() > 0) return;
        DateTime today = DateTime.now();
        DateTime thatDay = peildatum;
        dgLijst = new ArrayList<>();
        int factor = today.isBefore(thatDay) ? -1 : 1;
        DateTime d;
        DatumGeval dg;

        for (int y = 0; y < 107; y++) {
            d = thatDay.plusYears(factor * y);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.year, d, y);
            dgLijst.add(dg);
        }

        int[] maanden = {1, 10, 100, 200, 400, 500, 700, 800, 1000, 1100};
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
                5000, 5100, 5200, 5300, 5400, 5500};

        for (int week : weken) {
            d = thatDay.plusWeeks(factor * week);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.week, d, week);
            dgLijst.add(dg);
        }

        // Let op er ontbreken expres een aantal waarden (1000 weken = 7000 dagen etc)
        int[] dagen = {10, 100, 1000, 2000, 3000, 4000, 5000, 6000, 8000, 9000, 10000,
                11000, 12000, 13000, 15000, 16000, 17000, 18000, 19000, 20000,
                22000, 23000, 24000, 25000, 26000, 27000, 29000, 30000, 31000, 32000, 33000, 34000, 36000, 37000};
        for (int dag : dagen) {
            d = thatDay.plusDays(factor * dag);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.day, d, dag);
            dgLijst.add(dg);
        }

        int[] uren = {10, 100, 1000, 10000, 100000, 150000, 200000, 250000, 300000, 350000, 400000, 450000, 500000, 550000, 650000, 700000, 750000, 800000, 850000, 900000};
        for (int uur : uren) {
            d = thatDay.plusHours(factor * uur);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.hour, d, uur);
            dgLijst.add(dg);
        }

        int[] minuten = {100, 1000, 10000, 100000, 1000000, 10000000, 20000000, 40000000, 50000000};
        for (int minuut : minuten) {
            d = thatDay.plusMinutes(factor * minuut);
            if (today.isAfter(d)) continue;
            dg = new DatumGeval(Helper.eenheidType.minute, d, minuut);
            dgLijst.add(dg);
        }

        Long[] seconden = {1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 200000000L,
                300000000L, 400000000L, 500000000L, 700000000L, 800000000L, 1000000000L,
                1100000000L, 1300000000L, 1400000000L, 1500000000L, 1600000000L, 1700000000L,
                1900000000L, 2000000000L, 2100000000L, 2200000000L, 2300000000L, 2500000000L, 2600000000L, 2700000000L,
                2800000000L, 2900000000L, 3100000000L, 3200000000L};
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