package nl.whitedove.agecalculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class ListDatesActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dates_list_activity)
        val fabTerug: FloatingActionButton = findViewById(R.id.fabTerug)
        fabTerug.setOnClickListener { terug() }
        val extras = intent.extras!!
        val namen = extras.getString("namen")
        toonData(namen)
    }

    private fun terug() {
        val intent = Intent(this, MainActivity::class.java)
        Helper.dgLijst = ArrayList()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun toonData(namen: String?) {
        val tvNamen = findViewById<TextView>(R.id.tvNamen)
        tvNamen.text = namen
        if (Helper.dgLijst.size == 0) return
        val lvEigenMeldingen = findViewById<ListView>(R.id.lvDates)
        lvEigenMeldingen.adapter = CustomListAdapterDates(this, Helper.dgLijst)
    }
}