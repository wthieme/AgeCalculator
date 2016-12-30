package nl.whitedove.agecalculator;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity {

    private ScheduledExecutorService executer = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture future;
    private boolean mAnimation = false;
    private boolean mInCalDiaglog = false;
    DatabaseHelper mDH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitDb();
        InitEdits();
        InitTvs();
        InitDateListeners();
        InitFabs();
        SetDefaults();
        SetBars();
        InitTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        StopTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        StopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        InitTimer();
    }

    private void InitFabs() {
        FloatingActionButton fabDgLijst = (FloatingActionButton) findViewById(R.id.fabDgLijst);
        fabDgLijst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToonLijst();
            }
        });
    }

    private void InitDateListeners() {
        findViewById(R.id.ivDate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker(1);
            }
        });
        findViewById(R.id.ivDate2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowDatePicker(2);
            }
        });
        findViewById(R.id.ivDate3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowDatePicker(3);
            }
        });
    }

    private void InitEdits() {
        final EditText etName1 = (EditText) findViewById(R.id.etName1);
        GetNaam(etName1, 1);

        etName1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName1, 1);
            }
        });
        etName1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Persoon persoon = Helper.Personen.get(0);
                    DateTime gebDat = persoon.getGebdatum();
                    TextView tvDate = (TextView) findViewById(R.id.tvDate1);
                    SetDate(tvDate, 1, gebDat);
                    GetNaam(etName1, 1);
                }
            }
        });


        final EditText etName2 = (EditText) findViewById(R.id.etName2);
        GetNaam(etName2, 2);

        etName2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName2, 2);
            }
        });

        etName2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Persoon persoon = Helper.Personen.get(1);
                    DateTime gebDat = persoon.getGebdatum();
                    TextView tvDate = (TextView) findViewById(R.id.tvDate2);
                    SetDate(tvDate, 2, gebDat);
                    GetNaam(etName2, 2);
                }
            }
        });

        final EditText etName3 = (EditText) findViewById(R.id.etName3);
        GetNaam(etName3, 3);

        etName3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName3, 3);
            }
        });

        etName3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Persoon persoon = Helper.Personen.get(2);
                    DateTime gebDat = persoon.getGebdatum();
                    TextView tvDate = (TextView) findViewById(R.id.tvDate3);
                    SetDate(tvDate, 3, gebDat);
                    GetNaam(etName3, 3);
                }
            }
        });
    }

    private void InitTvs() {
        final TextView tvDate1 = (TextView) findViewById(R.id.tvDate1);
        GetDate(tvDate1, 1);
        final TextView tvDate2 = (TextView) findViewById(R.id.tvDate2);
        GetDate(tvDate2, 2);
        final TextView tvDate3 = (TextView) findViewById(R.id.tvDate3);
        GetDate(tvDate3, 3);
    }

    private void GetNaam(EditText etNaam, int nr) {
        SetBars();
        Persoon persoon = Helper.Personen.get(nr - 1);
        String naam = persoon.getNaam();
        etNaam.setText(naam);
        TextView tvNaam = (TextView) findViewById(R.id.tvNaam);
        tvNaam.setText(naam);
    }

    private void SetNaam(EditText etNaam, int nr) {
        Helper.Nr = nr;
        SetBars();
        Persoon persoon = Helper.Personen.get(nr - 1);
        String naam = etNaam.getText().toString();
        persoon.setNaam(naam);
        mDH.UpdatePersoon(persoon);
        TextView tvNaam = (TextView) findViewById(R.id.tvNaam);
        tvNaam.setText(naam);
    }

    private void GetDate(TextView tvDate, int nr) {
        SetBars();
        Persoon persoon = Helper.Personen.get(nr - 1);
        String sDate = Helper.dtmFormat.print(persoon.getGebdatum());
        tvDate.setText(sDate);
    }

    private void SetDate(DateTime date) {
        int nr = Helper.Nr;
        String name = String.format("tvDate%s", nr);
        int id = getResources().getIdentifier(name, "id", this.getPackageName());
        final TextView tvDate = (TextView) findViewById(id);
        SetDate(tvDate, nr, date);
    }

    private void SetDate(TextView tvDate, int nr, DateTime date) {
        Helper.Nr = nr;
        Helper.peildatum = date;
        SetBars();
        String sDate = Helper.dtmFormat.print(date);
        tvDate.setText(sDate);
        Persoon persoon = Helper.Personen.get(nr - 1);
        persoon.setGebdatum(date);
        mDH.UpdatePersoon(persoon);
    }

    private void ToonLijst() {
        Intent intent = new Intent(this, ListDatesActivity.class);
        startActivity(intent);
    }

    private void ShowDatePicker(int nr) {
        mInCalDiaglog = true;
        Helper.Nr = nr;
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.year), Helper.peildatum.getYear());
        args.putInt(getString(R.string.month), Helper.peildatum.getMonthOfYear());
        args.putInt(getString(R.string.day), Helper.peildatum.getDayOfMonth());
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), getString(R.string.date_picker));
    }

    OnDateSetListener ondate = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int yyyy, int mm, int
                dd) {
            mInCalDiaglog = false;
            Context cxt = getApplicationContext();
            int hour = Helper.peildatum.getHourOfDay();
            int minute = Helper.peildatum.getMinuteOfHour();
            DateTime newDate = new DateTime(yyyy, mm + 1, dd, hour, minute);
            if (newDate.isAfterNow()) {
                Helper.ShowMessage(cxt, getString(R.string.DateNotInPast), true);
                return;
            }

            DateTime dAfter = DateTime.now().minusYears(100);
            if (newDate.isBefore(dAfter)) {
                String sAfter = Helper.dFormat.print(dAfter);
                Helper.ShowMessage(cxt, String.format(getString(R.string.DateMustBeAfter), sAfter), true);
                return;
            }
            SetDate(newDate);
            ShowTimePicker();
        }
    };

    private void ShowTimePicker() {
        mInCalDiaglog = true;
        TimePickerFragment time = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.hour), Helper.peildatum.getHourOfDay());
        args.putInt(getString(R.string.minute), Helper.peildatum.getMinuteOfHour());
        time.setArguments(args);
        time.setCallBack(ontime);
        time.show(getSupportFragmentManager(), getString(R.string.time_picker));
    }

    OnTimeSetListener ontime = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hh, int mi) {
            mInCalDiaglog = false;
            int year = Helper.peildatum.getYear();
            int month = Helper.peildatum.getMonthOfYear();
            int day = Helper.peildatum.getDayOfMonth();
            DateTime newDate = new DateTime(year, month, day, hh, mi, 0);
            SetDate(newDate);
            Restart(true);
        }
    };

    private int GetAnimationIndex() {
        for (int i = 0; i < Helper.dgLijst.size(); i++) {
            if (Helper.dgLijst.get(i).getAnimatie() != Helper.animatie.finished)
                return i;
        }
        return -1;
    }

    private void AnimateDatumGeval() {

        // Er loopt al een animatie
        if (mAnimation) return;

        //Kijk waar we gebleven waren
        final int idx = GetAnimationIndex();

        if (idx == -1) return;

        if (Helper.dgLijst.get(idx).getAnimatie() != Helper.animatie.waiting) return;

        // Na een animatie blijft het 10 seconden staan
        DateTime start = (idx >= 1) ? Helper.dgLijst.get(idx - 1).getAnimatieStartTijd() : null;

        DateTime eind = (start == null) ? null : start.plusMillis(Helper.Duration);

        DateTime nu = DateTime.now();

        if (eind != null && nu.isBefore(eind)) return;

        final LinearLayout ll = (LinearLayout) findViewById(R.id.llDatumGeval);
        TextView tvWhat = (TextView) findViewById(R.id.tvWhat);
        TextView tvWhen = (TextView) findViewById(R.id.tvWhen);
        String sAantal = Helper.NumToString(Helper.dgLijst.get(idx).getAantal());
        String s = String.format("%s %ss", sAantal, Helper.dgLijst.get(idx).getEenheid());
        tvWhat.setText(s);
        Helper.eenheidType eenh = Helper.dgLijst.get(idx).getEenheid();
        DateTimeFormatter fmt = (eenh == Helper.eenheidType.hour || eenh == Helper.eenheidType.minute || eenh == Helper.eenheidType.second) ? Helper.dtFormat : Helper.dFormat;
        tvWhen.setText(fmt.print(Helper.dgLijst.get(idx).getDatumTijd()));

        Helper.dgLijst.get(idx).setAnimatie(Helper.animatie.running);
        Helper.dgLijst.get(idx).setAnimatieStartTijd(DateTime.now());
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeIn.setStartOffset(1000);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Helper.dgLijst.get(idx).setAnimatie(Helper.animatie.finished);
                mAnimation = false;
            }
        });

        ll.startAnimation(animationFadeIn);
        mAnimation = true;
    }

    private void UpdateScreen() {

        DateTime today = DateTime.now();
        DateTime thatDay = Helper.peildatum;
        Period period = new Period(thatDay, today);

        TextView tvY = (TextView) findViewById(R.id.tvY);
        tvY.setText(String.format(Locale.getDefault(), "%dy ", Math.abs(period.getYears())));

        TextView tvM = (TextView) findViewById(R.id.tvM);
        tvM.setText(String.format(Locale.getDefault(), "%dm ", Math.abs(period.getMonths())));

        TextView tvW = (TextView) findViewById(R.id.tvW);
        tvW.setText(String.format(Locale.getDefault(), "%dw ", Math.abs(period.getWeeks())));

        TextView tvD = (TextView) findViewById(R.id.tvD);
        tvD.setText(String.format(Locale.getDefault(), "%dd ", Math.abs(period.getDays())));

        TextView tvH = (TextView) findViewById(R.id.tvH);
        tvH.setText(String.format(Locale.getDefault(), "%dh ", Math.abs(period.getHours())));

        TextView tvMi = (TextView) findViewById(R.id.tvMi);
        tvMi.setText(String.format(Locale.getDefault(), "%dm ", Math.abs(period.getMinutes())));

        TextView tvS = (TextView) findViewById(R.id.tvS);
        tvS.setText(String.format(Locale.getDefault(), "%ds", Math.abs(period.getSeconds())));

        Locale locale = Locale.getDefault();
        NumberFormat nFormat = NumberFormat.getNumberInstance(locale);

        long totalYears = Math.abs(Years.yearsBetween(today, thatDay).getYears());
        TextView tvYears = (TextView) findViewById(R.id.tvYears);
        tvYears.setText(nFormat.format(totalYears));

        long totalMonths = Math.abs(Months.monthsBetween(today, thatDay).getMonths());
        TextView tvMonths = (TextView) findViewById(R.id.tvMonths);
        tvMonths.setText(nFormat.format(totalMonths));

        long totalWeeks = Math.abs(Weeks.weeksBetween(today, thatDay).getWeeks());
        TextView tvWeeks = (TextView) findViewById(R.id.tvWeeks);
        tvWeeks.setText(nFormat.format(totalWeeks));

        long totalDays = Math.abs(Days.daysBetween(today, thatDay).getDays());
        TextView tvDays = (TextView) findViewById(R.id.tvDays);
        tvDays.setText(nFormat.format(totalDays));

        long totalHours = Math.abs(Hours.hoursBetween(today, thatDay).getHours());
        TextView tvHours = (TextView) findViewById(R.id.tvHours);
        tvHours.setText(nFormat.format(totalHours));

        long totalMinutes = Math.abs(Minutes.minutesBetween(today, thatDay).getMinutes());
        TextView tvMinutes = (TextView) findViewById(R.id.tvMinutes);
        tvMinutes.setText(nFormat.format(totalMinutes));

        Long totalSeconds = Math.abs((today.getMillis() - thatDay.getMillis()) / 1000);
        TextView tvSeconds = (TextView) findViewById(R.id.tvSeconds);
        tvSeconds.setText(nFormat.format(totalSeconds));

        Helper.MakeDgList();

        TextView tvNextParty = (TextView) findViewById(R.id.tvNextParty);
        DatumGeval dg = Helper.dgLijst.get(0);
        DateTimeFormatter fmt = (dg.getEenheid() == Helper.eenheidType.hour
                || dg.getEenheid() == Helper.eenheidType.minute
                || dg.getEenheid() == Helper.eenheidType.second)
                ? Helper.dtFormat : Helper.dFormat;

        String sDate = fmt.print(dg.getDatumTijd());
        tvNextParty.setText(String.format(getString(R.string.next_party), sDate));

        TextView tvWhenNextParty = (TextView) findViewById(R.id.tvWhenNextParty);
        String sAantal = Helper.NumToString(dg.getAantal());
        String s = String.format(getString(R.string.Age), sAantal, dg.getEenheid());
        tvWhenNextParty.setText(s);

        AnimateDatumGeval();
        Restart(false);
    }

    private void InitDb() {
        mDH = new DatabaseHelper(getApplicationContext());
        Helper.Personen = mDH.GetPersonen();
        if (Helper.Personen.size() == 0) {
            // Voeg max aantal rijen toe
            for (int i = 1; i <= Helper.maxRijen; i++) {
                Persoon persoon = new Persoon();
                persoon.setId(i);
                persoon.setNaam(String.format("Naam %s", i));
                persoon.setGebdatum(new DateTime(2000, 1, 1, 0, 0));
                persoon.setGeselecteerd(true);
                persoon.setGetoond(i == 1);
                mDH.UpdatePersoon(persoon);
                Helper.Personen.add(persoon);
            }
        }
    }

    private void StopTimer() {
        if (executer != null && future != null) {
            future.cancel(false);
        }
    }

    private void InitTimer() {
        if (executer == null) {
            executer = Executors.newSingleThreadScheduledExecutor();
        }

        final MyHandler mHandler = new MyHandler(this);

        Runnable task = new Runnable() {
            public void run() {
                Context cxt = getApplicationContext();
                try {
                    mHandler.obtainMessage(1).sendToTarget();
                } catch (Exception e) {
                    Helper.ShowMessage(cxt, e.getMessage(), false);
                }
            }
        };

        if (future != null) {
            future.cancel(false);
        }

        future = executer.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    //static inner class doesn't hold an implicit reference to the outer class
    private static class MyHandler extends Handler {
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<MainActivity> myClassWeakReference;

        MyHandler(MainActivity myClassInstance) {
            myClassWeakReference = new WeakReference<>(myClassInstance);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity ma = myClassWeakReference.get();
            if (ma != null) {
                ma.UpdateScreen();
            }
        }
    }

    private void SetDefaults() {
        Helper.peildatum = Helper.Personen.get(Helper.Nr - 1).getGebdatum();
        SetDate(Helper.peildatum);
        UpdateScreen();
    }

    private void Restart(Boolean force) {
        if (mInCalDiaglog) return;

        if (!force) {
            for (int i = 0; i < Helper.dgLijst.size(); i++)
                if (Helper.dgLijst.get(i).getAnimatie() != Helper.animatie.finished) return;
        }

        // Alle animaties zijn gefinished
        Helper.dgLijst = new ArrayList<>();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void SetBars() {
        TextView tvBarLeft1 = (TextView) findViewById(R.id.tvBarLeft1);
        TextView tvBarLeft2 = (TextView) findViewById(R.id.tvBarLeft2);
        TextView tvBarLeft3 = (TextView) findViewById(R.id.tvBarLeft3);
        TextView tvBarRight1 = (TextView) findViewById(R.id.tvBarRight1);
        TextView tvBarRight2 = (TextView) findViewById(R.id.tvBarRight2);
        TextView tvBarRight3 = (TextView) findViewById(R.id.tvBarRight3);

        tvBarLeft1.setVisibility(View.GONE);
        tvBarLeft2.setVisibility(View.GONE);
        tvBarLeft3.setVisibility(View.GONE);
        tvBarRight1.setVisibility(View.GONE);
        tvBarRight2.setVisibility(View.GONE);
        tvBarRight3.setVisibility(View.GONE);

        String nameLeft = String.format("tvBarLeft%s", Helper.Nr);
        int idLeft = getResources().getIdentifier(nameLeft, "id", this.getPackageName());
        View tvBarLeft = findViewById(idLeft);
        tvBarLeft.setVisibility(View.VISIBLE);

        String nameRight = String.format("tvBarRight%s", Helper.Nr);
        int idRight = getResources().getIdentifier(nameRight, "id", this.getPackageName());
        View tvBarRight = findViewById(idRight);
        tvBarRight.setVisibility(View.VISIBLE);
    }
}