package nl.whitedove.agecalculator

import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nl.whitedove.agecalculator.Helper.Animatie
import nl.whitedove.agecalculator.Helper.EenheidType
import org.joda.time.*
import java.text.NumberFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs

class MainActivity : FragmentActivity() {
    private var mTimer: Timer? = null
    private var mAnimation = false
    private var mInCalDiaglog = false
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDb()
        initEdits()
        initCbs()
        initTvs()
        initDateListeners()
        initFabs()
        setPersons()
        setNaam()
        initTimer()
    }

    public override fun onPause() {
        super.onPause()
        stopTimer()
    }

    public override fun onStop() {
        super.onStop()
        stopTimer()
    }

    public override fun onResume() {
        super.onResume()
        initTimer()
    }

    private fun initFabs() {
        val fabDgLijst: FloatingActionButton = findViewById(R.id.fabDgLijst)
        fabDgLijst.setOnClickListener { toonLijst() }
    }

    private fun initDateListeners() {
        findViewById<View>(R.id.ivDate1).setOnClickListener { showDatePicker(1) }
        findViewById<View>(R.id.ivDate2).setOnClickListener { showDatePicker(2) }
        findViewById<View>(R.id.ivDate3).setOnClickListener { showDatePicker(3) }
        findViewById<View>(R.id.ivDate4).setOnClickListener { showDatePicker(4) }
        findViewById<View>(R.id.ivDate5).setOnClickListener { showDatePicker(5) }
    }

    fun moreClick(oView: View) {
        val popup = PopupMenu(this, oView)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.cmenu, popup.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.show_persons_dialog -> {
                    showPersonsDialog()
                    return@OnMenuItemClickListener true
                }
            }
            true
        })
        popup.show()
    }

    private fun initEdits() {
        val etName1 = findViewById<EditText>(R.id.etName1)
        naamToEdit(etName1, 1)
        etName1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setNaam(etName1, 1)
            }
        })
        val etName2 = findViewById<EditText>(R.id.etName2)
        naamToEdit(etName2, 2)
        etName2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setNaam(etName2, 2)
            }
        })
        val etName3 = findViewById<EditText>(R.id.etName3)
        naamToEdit(etName3, 3)
        etName3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setNaam(etName3, 3)
            }
        })
        val etName4 = findViewById<EditText>(R.id.etName4)
        naamToEdit(etName4, 4)
        etName4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setNaam(etName4, 4)
            }
        })
        val etName5 = findViewById<EditText>(R.id.etName5)
        naamToEdit(etName5, 5)
        etName5.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setNaam(etName5, 5)
            }
        })
    }

    private fun initTvs() {
        val tvDate1 = findViewById<TextView>(R.id.tvDate1)
        dateToTextView(tvDate1, 1)
        val tvDate2 = findViewById<TextView>(R.id.tvDate2)
        dateToTextView(tvDate2, 2)
        val tvDate3 = findViewById<TextView>(R.id.tvDate3)
        dateToTextView(tvDate3, 3)
        val tvDate4 = findViewById<TextView>(R.id.tvDate4)
        dateToTextView(tvDate4, 4)
        val tvDate5 = findViewById<TextView>(R.id.tvDate5)
        dateToTextView(tvDate5, 5)
    }

    private fun initCbs() {
        val cb1 = findViewById<CheckBox>(R.id.cb1)
        checkToCheckbox(cb1, 1)
        cb1.setOnCheckedChangeListener { _, _ -> setCheck(cb1, 1) }
        val cb2 = findViewById<CheckBox>(R.id.cb2)
        checkToCheckbox(cb2, 2)
        cb2.setOnCheckedChangeListener { _, _ -> setCheck(cb2, 2) }
        val cb3 = findViewById<CheckBox>(R.id.cb3)
        checkToCheckbox(cb3, 3)
        cb3.setOnCheckedChangeListener { _, _ -> setCheck(cb3, 3) }
        val cb4 = findViewById<CheckBox>(R.id.cb4)
        checkToCheckbox(cb4, 4)
        cb4.setOnCheckedChangeListener { _, _ -> setCheck(cb4, 4) }
        val cb5 = findViewById<CheckBox>(R.id.cb5)
        checkToCheckbox(cb5, 5)
        cb5.setOnCheckedChangeListener { _, _ -> setCheck(cb5, 5) }
    }

    private fun checkToCheckbox(cb: CheckBox, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(nr)
        cb.isChecked = persoon.getGevinkt() == true
    }

    private fun setCheck(cb: CheckBox, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(nr)
        val checked = cb.isChecked
        persoon.setGevinkt(checked)
        dh.updatePersoon(persoon)
        // Update de samengestelde naam
        setNaam()
    }

    private fun naamToEdit(etNaam: EditText, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(nr)
        val naam = persoon.getNaam()
        etNaam.setText(naam)
        val tvNames = findViewById<TextView>(R.id.tvNames)
        tvNames.text = naam
    }

    private fun setNaam(etNaam: EditText, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(nr)
        val naam = etNaam.text.toString()
        persoon.setNaam(naam)
        dh.updatePersoon(persoon)
        // Update de samengestelde naam
        setNaam()
    }

    private fun dateToTextView(tvDate: TextView, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(nr)
        val sDate = Helper.dtmFormat.print(persoon.getGebdatum())
        tvDate.text = sDate
    }

    private fun setDate(date: DateTime) {
        val nr = Helper.Nr
        val name = String.format("tvDate%s", nr)
        val id = resources.getIdentifier(name, "id", this.packageName)
        val tvDate = findViewById<TextView>(id)
        setDate(tvDate, nr, date)
    }

    private fun setDate(tvDate: TextView, nr: Int, date: DateTime?) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        Helper.Nr = nr
        val sDate = Helper.dtmFormat.print(date)
        tvDate.text = sDate
        val persoon = dh.getPersoon(nr)
        persoon.setGebdatum(date)
        dh.updatePersoon(persoon)
    }

    private fun toonLijst() {
        val intent = Intent(this, ListDatesActivity::class.java)
        val namen = bepaalNamen()
        intent.putExtra("namen", namen)
        startActivity(intent)
    }

    private fun showDatePicker(nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        mInCalDiaglog = true
        Helper.Nr = nr
        val persoon = dh.getPersoon(nr)
        val date = DatePickerFragment()
        val args = Bundle()
        args.putInt(getString(R.string.year), persoon.getGebdatum()!!.year)
        args.putInt(getString(R.string.month), persoon.getGebdatum()!!.monthOfYear)
        args.putInt(getString(R.string.day), persoon.getGebdatum()!!.dayOfMonth)
        date.arguments = args
        date.setCallBack(ondate)
        date.show(supportFragmentManager, getString(R.string.date_picker))
    }

    private var ondate: OnDateSetListener = OnDateSetListener { _, yyyy, mm, dd ->
        mInCalDiaglog = false
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(Helper.Nr)
        val hour = persoon.getGebdatum()!!.hourOfDay
        val minute = persoon.getGebdatum()!!.minuteOfHour
        val newDate = DateTime(yyyy, mm + 1, dd, hour, minute)
        if (newDate.isAfterNow) {
            Helper.showMessage(context, getString(R.string.DateNotInPast), true)
            return@OnDateSetListener
        }
        val dAfter = DateTime.now().minusYears(100)
        if (newDate.isBefore(dAfter)) {
            val sAfter = Helper.dFormat.print(dAfter)
            Helper.showMessage(context, String.format(getString(R.string.DateMustBeAfter), sAfter), true)
            return@OnDateSetListener
        }
        setDate(newDate)
        showTimePicker()
    }

    private fun showTimePicker() {
        mInCalDiaglog = true
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(Helper.Nr)
        val time = TimePickerFragment()
        val args = Bundle()
        args.putInt(getString(R.string.hour), persoon.getGebdatum()!!.hourOfDay)
        args.putInt(getString(R.string.minute), persoon.getGebdatum()!!.minuteOfHour)
        time.arguments = args
        time.setCallBack(ontime)
        time.show(supportFragmentManager, getString(R.string.time_picker))
    }

    private var ontime: OnTimeSetListener = OnTimeSetListener { _, hh, mi ->
        mInCalDiaglog = false
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.getPersoon(Helper.Nr)
        val year = persoon.getGebdatum()!!.year
        val month = persoon.getGebdatum()!!.monthOfYear
        val day = persoon.getGebdatum()!!.dayOfMonth
        val newDate = DateTime(year, month, day, hh, mi, 0)
        setDate(newDate)
        restart(true)
    }

    private fun getAnimationIndex(): Int {
        for (i in Helper.dgLijst.indices) {
            if (Helper.dgLijst[i].getAnimatie() != Animatie.Finished) return i
        }
        return -1
    }

    private fun animateDatumGeval() { // Er loopt al een animatie
        if (mAnimation) return
        //Kijk waar we gebleven waren
        val idx = getAnimationIndex()
        if (idx == -1) return
        if (Helper.dgLijst[idx].getAnimatie() != Animatie.Waiting) return
        // Na een animatie blijft het 10 seconden staan
        val start = if (idx >= 1) Helper.dgLijst[idx - 1].getAnimatieStartTijd() else null
        val eind = start?.plusMillis(Helper.Duration)
        val nu = DateTime.now()
        if (eind != null && nu.isBefore(eind)) return
        val ll = findViewById<LinearLayout>(R.id.llDatumGeval)
        val tvWhat = findViewById<TextView>(R.id.tvWhat)
        val tvWhen = findViewById<TextView>(R.id.tvWhen)
        val kind = Helper.dgLijst[idx].getKind()
        if (kind == Helper.KindType.Absolute) {
            val sAantal = Helper.numToString(Helper.dgLijst[idx].getAantal())
            val s = String.format("%s %ss", sAantal, Helper.dgLijst[idx].getEenheid())
            tvWhat.text = s
        } else {
            val s = Helper.dgLijst[idx].getTextToShow()
            tvWhat.text = s
        }
        val eenh = Helper.dgLijst[idx].getEenheid()
        val fmt = if (eenh == EenheidType.Hour || eenh == EenheidType.Minute || eenh == EenheidType.Second) Helper.dtFormat else Helper.dFormat
        tvWhen.text = fmt.print(Helper.dgLijst[idx].getDatumTijd())
        Helper.dgLijst[idx].setAnimatie(Animatie.Running)
        Helper.dgLijst[idx].setAnimatieStartTijd(DateTime.now())
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        animationFadeIn.startOffset = 1000
        animationFadeIn.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                ll.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                mAnimation = false
                if (Helper.dgLijst.size > 0) Helper.dgLijst[idx].setAnimatie(Animatie.Finished)
            }
        })
        ll.startAnimation(animationFadeIn)
        mAnimation = true
    }

    private fun updateScreen() {
        val today = DateTime.now()
        var thatDay = today
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val personen = dh.getAangevinktePersonen()
        for (persoon in personen) {
            thatDay = thatDay.minus(today.millis - persoon.getGebdatum()!!.millis)
        }
        val period = Period(thatDay, today)
        val tvY = findViewById<TextView>(R.id.tvY)
        tvY.text = String.format(Locale.getDefault(), "%dy ", abs(period.years))
        val tvM = findViewById<TextView>(R.id.tvM)
        tvM.text = String.format(Locale.getDefault(), "%dm ", abs(period.months))
        val tvW = findViewById<TextView>(R.id.tvW)
        tvW.text = String.format(Locale.getDefault(), "%dw ", abs(period.weeks))
        val tvD = findViewById<TextView>(R.id.tvD)
        tvD.text = String.format(Locale.getDefault(), "%dd ", abs(period.days))
        val tvH = findViewById<TextView>(R.id.tvH)
        tvH.text = String.format(Locale.getDefault(), "%dh ", abs(period.hours))
        val tvMi = findViewById<TextView>(R.id.tvMi)
        tvMi.text = String.format(Locale.getDefault(), "%dm ", abs(period.minutes))
        val tvS = findViewById<TextView>(R.id.tvS)
        tvS.text = String.format(Locale.getDefault(), "%ds", abs(period.seconds))
        val locale = Locale.getDefault()
        val nFormat = NumberFormat.getNumberInstance(locale)
        val totalYears = abs(Years.yearsBetween(today, thatDay).years).toLong()
        val tvYears = findViewById<TextView>(R.id.tvYears)
        tvYears.text = nFormat.format(totalYears)
        val totalMonths = abs(Months.monthsBetween(today, thatDay).months).toLong()
        val tvMonths = findViewById<TextView>(R.id.tvMonths)
        tvMonths.text = nFormat.format(totalMonths)
        val totalWeeks = abs(Weeks.weeksBetween(today, thatDay).weeks).toLong()
        val tvWeeks = findViewById<TextView>(R.id.tvWeeks)
        tvWeeks.text = nFormat.format(totalWeeks)
        val totalDays = abs(Days.daysBetween(today, thatDay).days).toLong()
        val tvDays = findViewById<TextView>(R.id.tvDays)
        tvDays.text = nFormat.format(totalDays)
        val totalHours = abs(Hours.hoursBetween(today, thatDay).hours).toLong()
        val tvHours = findViewById<TextView>(R.id.tvHours)
        tvHours.text = nFormat.format(totalHours)
        val totalMinutes = abs(Minutes.minutesBetween(today, thatDay).minutes).toLong()
        val tvMinutes = findViewById<TextView>(R.id.tvMinutes)
        tvMinutes.text = nFormat.format(totalMinutes)
        val totalSeconds = abs((today.millis - thatDay.millis) / 1000)
        val tvSeconds = findViewById<TextView>(R.id.tvSeconds)
        tvSeconds.text = nFormat.format(totalSeconds)
        Helper.makeDgList(personen)
        val tvNextParty = findViewById<TextView>(R.id.tvNextParty)
        val tvWhenNextParty = findViewById<TextView>(R.id.tvWhenNextParty)
        val fabDgLijst = findViewById<FloatingActionButton>(R.id.fabDgLijst)
        if (personen.size == 0) {
            tvNextParty.visibility = View.GONE
            tvWhenNextParty.visibility = View.GONE
            fabDgLijst.hide()
            return
        }
        fabDgLijst.show()
        tvNextParty.visibility = View.VISIBLE
        tvWhenNextParty.visibility = View.VISIBLE
        val dg = Helper.dgLijst[0]
        val fmt = if (dg.getEenheid() == EenheidType.Hour || dg.getEenheid() == EenheidType.Minute || dg.getEenheid() == EenheidType.Second) Helper.dtFormat else Helper.dFormat
        val sDate = fmt.print(dg.getDatumTijd())
        tvNextParty.text = String.format(getString(R.string.next_party), sDate)
        if (dg.getKind() == Helper.KindType.Absolute) {
            val sAantal = Helper.numToString(dg.getAantal())
            val s = String.format(getString(R.string.Age), sAantal, dg.getEenheid())
            tvWhenNextParty.text = s
        } else {
            val s = dg.getTextToShow()
            tvWhenNextParty.text = s
        }

        animateDatumGeval()
        restart(false)
    }

    private fun initDb() {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val aantal = dh.getAantalPersonen()
        if (aantal == 0L) { // Voeg max aantal rijen toe
            for (i in 1..Helper.maxRijen) {
                val persoon = Persoon()
                persoon.setId(i)
                persoon.setNaam(String.format("Name %s", i))
                persoon.setGebdatum(DateTime(2000, 1, 1, 0, 0))
                persoon.setGeselecteerd(i == 1)
                persoon.setGetoond(i == 1)
                persoon.setGevinkt(i == 1)
                dh.updatePersoon(persoon)
            }
        }
    }

    private fun stopTimer() {
        mTimer?.cancel()
    }

    private fun initTimer() {
        mTimer = fixedRateTimer(initialDelay = 1000, period = 1000, daemon = true) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                updateScreen()
            }
        }
    }

    private fun restart(force: Boolean) {
        if (mInCalDiaglog) return
        if (!force) {
            for (i in Helper.dgLijst.indices) if (Helper.dgLijst[i].getAnimatie() != Animatie.Finished) return
        }
        // Alle animaties zijn gefinished
        Helper.dgLijst = ArrayList()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun setPersons() {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val personen = dh.getAllePersonen()
        for (persoon in personen) {
            setViewVisible("etName", persoon.getId(), persoon.getGetoond() == true)
            setViewVisible("cb", persoon.getId(), persoon.getGetoond() == true)
            setViewVisible("ivDate", persoon.getId(), persoon.getGetoond() == true)
            setViewVisible("tvDate", persoon.getId(), persoon.getGetoond() == true)
        }
    }

    private fun bepaalNamen(): String {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val personen = dh.getAangevinktePersonen()
        val namenBuilder = StringBuilder()
        for (persoon in personen) {
            namenBuilder.append(persoon.getNaam()).append(" + ")
        }
        var namen = namenBuilder.toString()
        if (namen.isNotEmpty()) {
            namen = namen.substring(0, namen.length - 3)
        }
        return namen
    }

    private fun setNaam() {
        val namen = bepaalNamen()
        val tvNames = findViewById<TextView>(R.id.tvNames)
        tvNames.text = namen
        Helper.dgLijst = ArrayList()
    }

    private fun setViewVisible(name: String?, nr: Int, visible: Boolean) {
        val context = applicationContext
        val res = context.resources
        val packname = context.packageName
        val vName = name + nr.toString()
        val id = res.getIdentifier(vName, "id", packname)
        val vw = findViewById<View>(id)
        vw.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showPersonsDialog() {
        val rd = PersonsDialog(this)
        rd.setCancelable(false)
        rd.setOnDismissListener {
            setPersons()
            setNaam()
        }
        rd.show()
        rd.setCheckboxes()
    }
}