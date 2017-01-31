package nl.whitedove.agecalculator;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
        InitCbs();
        InitTvs();
        InitDateListeners();
        InitFabs();
        SetPersons();
        SetNaam();
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
        findViewById(R.id.ivDate4).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowDatePicker(4);
            }
        });
        findViewById(R.id.ivDate5).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowDatePicker(5);
            }
        });
    }

    public void moreClick(View oView) {
        PopupMenu popup = new PopupMenu(this, oView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.cmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.show_persons_dialog:
                        ShowPersonsDialog();
                        return true;

                }
                return true;
            }
        });

        popup.show();
    }

    private void InitEdits() {
        final EditText etName1 = (EditText) findViewById(R.id.etName1);
        NaamToEdit(etName1, 1);

        etName1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName1, 1);
            }
        });

        final EditText etName2 = (EditText) findViewById(R.id.etName2);
        NaamToEdit(etName2, 2);

        etName2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName2, 2);
            }
        });

        final EditText etName3 = (EditText) findViewById(R.id.etName3);
        NaamToEdit(etName3, 3);

        etName3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName3, 3);
            }
        });

        final EditText etName4 = (EditText) findViewById(R.id.etName4);
        NaamToEdit(etName4, 4);

        etName4.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName4, 4);
            }
        });

        final EditText etName5 = (EditText) findViewById(R.id.etName5);
        NaamToEdit(etName5, 5);

        etName5.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                SetNaam(etName5, 5);
            }
        });
    }

    private void InitTvs() {
        final TextView tvDate1 = (TextView) findViewById(R.id.tvDate1);
        DateToTextView(tvDate1, 1);
        final TextView tvDate2 = (TextView) findViewById(R.id.tvDate2);
        DateToTextView(tvDate2, 2);
        final TextView tvDate3 = (TextView) findViewById(R.id.tvDate3);
        DateToTextView(tvDate3, 3);
        final TextView tvDate4 = (TextView) findViewById(R.id.tvDate4);
        DateToTextView(tvDate4, 4);
        final TextView tvDate5 = (TextView) findViewById(R.id.tvDate5);
        DateToTextView(tvDate5, 5);
    }

    private void InitCbs() {
        final CheckBox cb1 = (CheckBox) findViewById(R.id.cb1);
        CheckToCheckbox(cb1, 1);
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetCheck(cb1, 1);
            }
        });

        final CheckBox cb2 = (CheckBox) findViewById(R.id.cb2);
        CheckToCheckbox(cb2, 2);
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetCheck(cb2, 2);
            }
        });

        final CheckBox cb3 = (CheckBox) findViewById(R.id.cb3);
        CheckToCheckbox(cb3, 3);
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetCheck(cb3, 3);
            }
        });

        final CheckBox cb4 = (CheckBox) findViewById(R.id.cb4);
        CheckToCheckbox(cb4, 4);
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetCheck(cb4, 4);
            }
        });

        final CheckBox cb5 = (CheckBox) findViewById(R.id.cb5);
        CheckToCheckbox(cb5, 5);
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetCheck(cb5, 5);
            }
        });
    }

    private void CheckToCheckbox(CheckBox cb, int nr) {
        Persoon persoon = mDH.GetPersoon(nr);
        cb.setChecked(persoon.getGevinkt());
    }

    private void SetCheck(CheckBox cb, int nr) {
        Persoon persoon = mDH.GetPersoon(nr);
        Boolean checked = cb.isChecked();
        persoon.setGevinkt(checked);
        mDH.UpdatePersoon(persoon);
        // Update de samengestelde naam
        SetNaam();
    }

    private void NaamToEdit(EditText etNaam, int nr) {
        Persoon persoon = mDH.GetPersoon(nr);
        String naam = persoon.getNaam();
        etNaam.setText(naam);
        TextView tvNames = (TextView) findViewById(R.id.tvNames);
        tvNames.setText(naam);
    }

    private void SetNaam(EditText etNaam, int nr) {
        Persoon persoon = mDH.GetPersoon(nr);
        String naam = etNaam.getText().toString();
        persoon.setNaam(naam);
        mDH.UpdatePersoon(persoon);
        // Update de samengestelde naam
        SetNaam();
    }

    private void DateToTextView(TextView tvDate, int nr) {
        Persoon persoon = mDH.GetPersoon(nr);
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
        String sDate = Helper.dtmFormat.print(date);
        tvDate.setText(sDate);
        Persoon persoon = mDH.GetPersoon(nr);
        persoon.setGebdatum(date);
        mDH.UpdatePersoon(persoon);
    }

    private void ToonLijst() {
        Intent intent = new Intent(this, ListDatesActivity.class);
        String namen = BepaalNamen();
        intent.putExtra("namen", namen);
        startActivity(intent);
    }

    private void ShowDatePicker(int nr) {
        mInCalDiaglog = true;
        Helper.Nr = nr;
        Persoon persoon = mDH.GetPersoon(nr);
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.year), persoon.getGebdatum().getYear());
        args.putInt(getString(R.string.month), persoon.getGebdatum().getMonthOfYear());
        args.putInt(getString(R.string.day), persoon.getGebdatum().getDayOfMonth());
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
            Persoon persoon = mDH.GetPersoon(Helper.Nr);
            int hour = persoon.getGebdatum().getHourOfDay();
            int minute = persoon.getGebdatum().getMinuteOfHour();
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
        Persoon persoon = mDH.GetPersoon(Helper.Nr);
        TimePickerFragment time = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.hour), persoon.getGebdatum().getHourOfDay());
        args.putInt(getString(R.string.minute), persoon.getGebdatum().getMinuteOfHour());
        time.setArguments(args);
        time.setCallBack(ontime);
        time.show(getSupportFragmentManager(), getString(R.string.time_picker));
    }

    OnTimeSetListener ontime = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hh, int mi) {
            mInCalDiaglog = false;
            Persoon persoon = mDH.GetPersoon(Helper.Nr);
            int year = persoon.getGebdatum().getYear();
            int month = persoon.getGebdatum().getMonthOfYear();
            int day = persoon.getGebdatum().getDayOfMonth();
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
                mAnimation = false;
                if (Helper.dgLijst != null && Helper.dgLijst.size() > 0)
                    Helper.dgLijst.get(idx).setAnimatie(Helper.animatie.finished);
            }
        });

        ll.startAnimation(animationFadeIn);
        mAnimation = true;
    }

    private void UpdateScreen() {

        DateTime today = DateTime.now();
        DateTime thatDay = today;

        ArrayList<Persoon> personen = mDH.GetAangevinktePersonen();
        for (Persoon persoon : personen) {
            thatDay = thatDay.minus(today.getMillis() - persoon.getGebdatum().getMillis());
        }

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

        Helper.MakeDgList(personen);
        TextView tvNextParty = (TextView) findViewById(R.id.tvNextParty);
        TextView tvWhenNextParty = (TextView) findViewById(R.id.tvWhenNextParty);
        FloatingActionButton fabDgLijst = (FloatingActionButton) findViewById(R.id.fabDgLijst);

        if (personen.size()==0)
        {
            tvNextParty.setVisibility(View.GONE);
            tvWhenNextParty.setVisibility(View.GONE);
            fabDgLijst.setVisibility(View.GONE);
            return;
        }

        fabDgLijst.setVisibility(View.VISIBLE);
        tvNextParty.setVisibility(View.VISIBLE);
        tvWhenNextParty.setVisibility(View.VISIBLE);
        DatumGeval dg = Helper.dgLijst.get(0);
        DateTimeFormatter fmt = (dg.getEenheid() == Helper.eenheidType.hour
                || dg.getEenheid() == Helper.eenheidType.minute
                || dg.getEenheid() == Helper.eenheidType.second)
                ? Helper.dtFormat : Helper.dFormat;

        String sDate = fmt.print(dg.getDatumTijd());
        tvNextParty.setText(String.format(getString(R.string.next_party), sDate));

        String sAantal = Helper.NumToString(dg.getAantal());
        String s = String.format(getString(R.string.Age), sAantal, dg.getEenheid());
        tvWhenNextParty.setText(s);

        AnimateDatumGeval();
        Restart(false);
    }

    private void InitDb() {
        mDH = new DatabaseHelper(getApplicationContext());
        int aantal = mDH.GetAantalPersonen();
        if (aantal == 0) {
            // Voeg max aantal rijen toe
            for (int i = 1; i <= Helper.maxRijen; i++) {
                Persoon persoon = new Persoon();
                persoon.setId(i);
                persoon.setNaam(String.format("Name %s", i));
                persoon.setGebdatum(new DateTime(2000, 1, 1, 0, 0));
                persoon.setGeselecteerd(i == 1);
                persoon.setGetoond(i == 1);
                persoon.setGevinkt(i == 1);
                mDH.UpdatePersoon(persoon);
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

    private void SetPersons() {
        ArrayList<Persoon> personen = mDH.GetAllePersonen();
        for (Persoon persoon : personen) {
            SetViewVisible("etName", persoon.getId(), persoon.getGetoond());
            SetViewVisible("cb", persoon.getId(), persoon.getGetoond());
            SetViewVisible("ivDate", persoon.getId(), persoon.getGetoond());
            SetViewVisible("tvDate", persoon.getId(), persoon.getGetoond());
        }
    }

    private String BepaalNamen() {
        ArrayList<Persoon> personen = mDH.GetAangevinktePersonen();
        String namen = "";
        for (Persoon persoon : personen) {
            namen = namen + persoon.getNaam() + " + ";
        }
        if (namen.length() > 0) {
            namen = namen.substring(0, namen.length() - 3);
        }
        return namen;
    }

    private void SetNaam() {
        String namen = BepaalNamen();
        TextView tvNames = (TextView) findViewById(R.id.tvNames);
        tvNames.setText(namen);
        Helper.dgLijst = new ArrayList<>();
    }

    private void SetViewVisible(String name, int nr, boolean visible) {
        Context context = getApplicationContext();
        Resources res = context.getResources();
        String packname = context.getPackageName();
        String vName = name + Integer.toString(nr);
        int id = res.getIdentifier(vName, "id", packname);
        View vw = findViewById(id);
        vw.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void ShowPersonsDialog() {
        PersonsDialog rd = new PersonsDialog(this);
        rd.setCancelable(false);

        rd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SetPersons();
                SetNaam();
            }
        });

        rd.show();
        rd.SetCheckboxes(mDH);
    }
}
