package nl.whitedove.agecalculator

import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import nl.whitedove.agecalculator.Helper.animatie
import nl.whitedove.agecalculator.Helper.eenheidType
import org.joda.time.*
import java.lang.ref.WeakReference
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class MainActivity : FragmentActivity() {
    private var mExecuter = Executors.newSingleThreadScheduledExecutor()
    private var mFuture: ScheduledFuture<*>? = null
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
        findViewById<View>(R.id.ivDate1).setOnClickListener({ showDatePicker(1) })
        findViewById<View>(R.id.ivDate2).setOnClickListener({ showDatePicker(2) })
        findViewById<View>(R.id.ivDate3).setOnClickListener({ showDatePicker(3) })
        findViewById<View>(R.id.ivDate4).setOnClickListener({ showDatePicker(4) })
        findViewById<View>(R.id.ivDate5).setOnClickListener({ showDatePicker(5) })
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
        cb1.setOnCheckedChangeListener({ buttonView, isChecked -> setCheck(cb1, 1) })
        val cb2 = findViewById<CheckBox>(R.id.cb2)
        checkToCheckbox(cb2, 2)
        cb2.setOnCheckedChangeListener({ buttonView, isChecked -> setCheck(cb2, 2) })
        val cb3 = findViewById<CheckBox>(R.id.cb3)
        checkToCheckbox(cb3, 3)
        cb3.setOnCheckedChangeListener({ buttonView, isChecked -> setCheck(cb3, 3) })
        val cb4 = findViewById<CheckBox>(R.id.cb4)
        checkToCheckbox(cb4, 4)
        cb4.setOnCheckedChangeListener({ buttonView, isChecked -> setCheck(cb4, 4) })
        val cb5 = findViewById<CheckBox>(R.id.cb5)
        checkToCheckbox(cb5, 5)
        cb5.setOnCheckedChangeListener({ buttonView, isChecked -> setCheck(cb5, 5) })
    }

    private fun checkToCheckbox(cb: CheckBox, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(nr)
        cb.setChecked(persoon.getGevinkt() == true)
    }

    private fun setCheck(cb: CheckBox, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(nr)
        val checked = cb.isChecked()
        persoon.setGevinkt(checked)
        dh.UpdatePersoon(persoon)
        // Update de samengestelde naam
        setNaam()
    }

    private fun naamToEdit(etNaam: EditText, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(nr)
        val naam = persoon.getNaam()
        etNaam.setText(naam)
        val tvNames = findViewById<TextView>(R.id.tvNames)
        tvNames.setText(naam)
    }

    private fun setNaam(etNaam: EditText, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(nr)
        val naam = etNaam.getText().toString()
        persoon.setNaam(naam)
        dh.UpdatePersoon(persoon)
        // Update de samengestelde naam
        setNaam()
    }

    private fun dateToTextView(tvDate: TextView, nr: Int) {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(nr)
        val sDate = Helper.dtmFormat.print(persoon.getGebdatum())
        tvDate.setText(sDate)
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
        tvDate.setText(sDate)
        val persoon = dh.GetPersoon(nr)
        persoon.setGebdatum(date)
        dh.UpdatePersoon(persoon)
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
        val persoon = dh.GetPersoon(nr)
        val date = DatePickerFragment()
        val args = Bundle()
        args.putInt(getString(R.string.year), persoon.getGebdatum()!!.year)
        args.putInt(getString(R.string.month), persoon.getGebdatum()!!.monthOfYear)
        args.putInt(getString(R.string.day), persoon.getGebdatum()!!.dayOfMonth)
        date.arguments = args
        date.setCallBack(ondate)
        date.show(supportFragmentManager, getString(R.string.date_picker))
    }

    var ondate: OnDateSetListener = OnDateSetListener { view, yyyy, mm, dd ->
        mInCalDiaglog = false
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(Helper.Nr)
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
        val persoon = dh.GetPersoon(Helper.Nr)
        val time = TimePickerFragment()
        val args = Bundle()
        args.putInt(getString(R.string.hour), persoon.getGebdatum()!!.hourOfDay)
        args.putInt(getString(R.string.minute), persoon.getGebdatum()!!.minuteOfHour)
        time.arguments = args
        time.setCallBack(ontime)
        time.show(supportFragmentManager, getString(R.string.time_picker))
    }

    var ontime: OnTimeSetListener = OnTimeSetListener { view, hh, mi ->
        mInCalDiaglog = false
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val persoon = dh.GetPersoon(Helper.Nr)
        val year = persoon.getGebdatum()!!.year
        val month = persoon.getGebdatum()!!.monthOfYear
        val day = persoon.getGebdatum()!!.dayOfMonth
        val newDate = DateTime(year, month, day, hh, mi, 0)
        setDate(newDate)
        restart(true)
    }

    private fun getAnimationIndex(): Int {
        for (i in Helper.dgLijst.indices) {
            if (Helper.dgLijst[i].getAnimatie() != animatie.finished) return i
        }
        return -1
    }

    private fun animateDatumGeval() { // Er loopt al een animatie
        if (mAnimation) return
        //Kijk waar we gebleven waren
        val idx = getAnimationIndex()
        if (idx == -1) return
        if (Helper.dgLijst[idx].getAnimatie() != animatie.waiting) return
        // Na een animatie blijft het 10 seconden staan
        val start = if (idx >= 1) Helper.dgLijst[idx - 1].getAnimatieStartTijd() else null
        val eind = start?.plusMillis(Helper.Duration)
        val nu = DateTime.now()
        if (eind != null && nu.isBefore(eind)) return
        val ll = findViewById<LinearLayout>(R.id.llDatumGeval)
        val tvWhat = findViewById<TextView>(R.id.tvWhat)
        val tvWhen = findViewById<TextView>(R.id.tvWhen)
        val kind = Helper.dgLijst[idx].getKind()
        if (kind == Helper.kindType.absolute) {
            val sAantal = Helper.numToString(Helper.dgLijst[idx].getAantal())
            val s = String.format("%s %ss", sAantal, Helper.dgLijst[idx].getEenheid())
            tvWhat.setText(s)
        }
        else
        {
            val s = Helper.dgLijst[idx].getTextToShow()
            tvWhat.setText(s)
        }
        val eenh = Helper.dgLijst[idx].getEenheid()
        val fmt = if (eenh == eenheidType.hour || eenh == eenheidType.minute || eenh == eenheidType.second) Helper.dtFormat else Helper.dFormat
        tvWhen.setText(fmt.print(Helper.dgLijst[idx].getDatumTijd()))
        Helper.dgLijst[idx].setAnimatie(animatie.running)
        Helper.dgLijst[idx].setAnimatieStartTijd(DateTime.now())
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        animationFadeIn.startOffset = 1000
        animationFadeIn.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                ll.setVisibility(View.VISIBLE)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                mAnimation = false
                if (Helper.dgLijst.size > 0) Helper.dgLijst[idx].setAnimatie(animatie.finished)
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
        val personen = dh.GetAangevinktePersonen()
        for (persoon in personen) {
            thatDay = thatDay.minus(today.millis - persoon.getGebdatum()!!.millis)
        }
        val period = Period(thatDay, today)
        val tvY = findViewById<TextView>(R.id.tvY)
        tvY.setText(String.format(Locale.getDefault(), "%dy ", Math.abs(period.years)))
        val tvM = findViewById<TextView>(R.id.tvM)
        tvM.setText(String.format(Locale.getDefault(), "%dm ", Math.abs(period.months)))
        val tvW = findViewById<TextView>(R.id.tvW)
        tvW.setText(String.format(Locale.getDefault(), "%dw ", Math.abs(period.weeks)))
        val tvD = findViewById<TextView>(R.id.tvD)
        tvD.setText(String.format(Locale.getDefault(), "%dd ", Math.abs(period.days)))
        val tvH = findViewById<TextView>(R.id.tvH)
        tvH.setText(String.format(Locale.getDefault(), "%dh ", Math.abs(period.hours)))
        val tvMi = findViewById<TextView>(R.id.tvMi)
        tvMi.setText(String.format(Locale.getDefault(), "%dm ", Math.abs(period.minutes)))
        val tvS = findViewById<TextView>(R.id.tvS)
        tvS.setText(String.format(Locale.getDefault(), "%ds", Math.abs(period.seconds)))
        val locale = Locale.getDefault()
        val nFormat = NumberFormat.getNumberInstance(locale)
        val totalYears = Math.abs(Years.yearsBetween(today, thatDay).years).toLong()
        val tvYears = findViewById<TextView>(R.id.tvYears)
        tvYears.setText(nFormat.format(totalYears))
        val totalMonths = Math.abs(Months.monthsBetween(today, thatDay).months).toLong()
        val tvMonths = findViewById<TextView>(R.id.tvMonths)
        tvMonths.setText(nFormat.format(totalMonths))
        val totalWeeks = Math.abs(Weeks.weeksBetween(today, thatDay).weeks).toLong()
        val tvWeeks = findViewById<TextView>(R.id.tvWeeks)
        tvWeeks.setText(nFormat.format(totalWeeks))
        val totalDays = Math.abs(Days.daysBetween(today, thatDay).days).toLong()
        val tvDays = findViewById<TextView>(R.id.tvDays)
        tvDays.setText(nFormat.format(totalDays))
        val totalHours = Math.abs(Hours.hoursBetween(today, thatDay).hours).toLong()
        val tvHours = findViewById<TextView>(R.id.tvHours)
        tvHours.setText(nFormat.format(totalHours))
        val totalMinutes = Math.abs(Minutes.minutesBetween(today, thatDay).minutes).toLong()
        val tvMinutes = findViewById<TextView>(R.id.tvMinutes)
        tvMinutes.setText(nFormat.format(totalMinutes))
        val totalSeconds = Math.abs((today.millis - thatDay.millis) / 1000)
        val tvSeconds = findViewById<TextView>(R.id.tvSeconds)
        tvSeconds.setText(nFormat.format(totalSeconds))
        Helper.makeDgList(personen)
        val tvNextParty = findViewById<TextView>(R.id.tvNextParty)
        val tvWhenNextParty = findViewById<TextView>(R.id.tvWhenNextParty)
        val fabDgLijst = findViewById<FloatingActionButton>(R.id.fabDgLijst)
        if (personen.size == 0) {
            tvNextParty.setVisibility(View.GONE)
            tvWhenNextParty.setVisibility(View.GONE)
            fabDgLijst.hide()
            return
        }
        fabDgLijst.show()
        tvNextParty.setVisibility(View.VISIBLE)
        tvWhenNextParty.setVisibility(View.VISIBLE)
        val dg = Helper.dgLijst[0]
        val fmt = if (dg.getEenheid() == eenheidType.hour || dg.getEenheid() == eenheidType.minute || dg.getEenheid() == eenheidType.second) Helper.dtFormat else Helper.dFormat
        val sDate = fmt.print(dg.getDatumTijd())
        tvNextParty.setText(String.format(getString(R.string.next_party), sDate))
        if (dg.getKind()== Helper.kindType.absolute) {
            val sAantal = Helper.numToString(dg.getAantal())
            val s = String.format(getString(R.string.Age), sAantal, dg.getEenheid())
            tvWhenNextParty.setText(s)
        }
        else {
            val s = dg.getTextToShow()
            tvWhenNextParty.setText(s)
        }

        animateDatumGeval()
        restart(false)
    }

    private fun initDb() {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val aantal = dh.GetAantalPersonen()
        if (aantal == 0L) { // Voeg max aantal rijen toe
            for (i in 1..Helper.maxRijen) {
                val persoon = Persoon()
                persoon.setId(i)
                persoon.setNaam(String.format("Name %s", i))
                persoon.setGebdatum(DateTime(2000, 1, 1, 0, 0))
                persoon.setGeselecteerd(i == 1)
                persoon.setGetoond(i == 1)
                persoon.setGevinkt(i == 1)
                dh.UpdatePersoon(persoon)
            }
        }
    }

    private fun stopTimer() {
        val future = mFuture
        if (mFuture != null) {
            future!!.cancel(false)
        }
    }

    private fun initTimer() {
        val mHandler = MyHandler(this)
        val task = Runnable {
            val cxt = applicationContext
            try {
                mHandler.obtainMessage(1).sendToTarget()
            } catch (e: Exception) {
                Helper.showMessage(cxt, e.message, false)
            }
        }
        val future = mFuture
        if (mFuture != null) {
            future!!.cancel(false)
        }
        mFuture = mExecuter.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS)
    }

    //static inner class doesn't hold an implicit reference to the outer class
    private class MyHandler internal constructor(myClassInstance: MainActivity) : Handler() {
        //Using a weak reference means you won't prevent garbage collection
        private val myClassWeakReference: WeakReference<MainActivity>

        override fun handleMessage(msg: Message?) {
            val ma = myClassWeakReference.get()
            ma?.updateScreen()
        }

        init {
            myClassWeakReference = WeakReference(myClassInstance)
        }
    }

    private fun restart(force: Boolean) {
        if (mInCalDiaglog) return
        if (!force) {
            for (i in Helper.dgLijst.indices) if (Helper.dgLijst[i].getAnimatie() != animatie.finished) return
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
        val personen = dh.GetAllePersonen()
        for (persoon in personen) {
            setViewVisible("etName", persoon.getId(), persoon.getGetoond() == true)
            setViewVisible("cb", persoon.getId(), persoon.getGetoond() == true)
            setViewVisible("ivDate", persoon.getId(), persoon.getGetoond() == true)
            setViewVisible("tvDate", persoon.getId(), persoon.getGetoond() == true)
        }
    }

    private fun bepaalNamen(): String? {
        val context = applicationContext
        val dh = DatabaseHelper.getInstance(context)
        val personen = dh.GetAangevinktePersonen()
        val namenBuilder = StringBuilder()
        for (persoon in personen) {
            namenBuilder.append(persoon.getNaam()).append(" + ")
        }
        var namen = namenBuilder.toString()
        if (namen.length > 0) {
            namen = namen.substring(0, namen.length - 3)
        }
        return namen
    }

    private fun setNaam() {
        val namen = bepaalNamen()
        val tvNames = findViewById<TextView>(R.id.tvNames)
        tvNames.setText(namen)
        Helper.dgLijst = ArrayList()
    }

    private fun setViewVisible(name: String?, nr: Int, visible: Boolean) {
        val context = applicationContext
        val res = context.resources
        val packname = context.packageName
        val vName = name + Integer.toString(nr)
        val id = res.getIdentifier(vName, "id", packname)
        val vw = findViewById<View>(id)
        vw.setVisibility(if (visible) View.VISIBLE else View.GONE)
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