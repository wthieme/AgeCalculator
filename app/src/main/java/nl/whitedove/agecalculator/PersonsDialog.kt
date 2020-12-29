package nl.whitedove.agecalculator

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox

internal class PersonsDialog(ctx: Context) : Dialog(ctx), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.persons_dialog)
        val btOk = findViewById<Button>(R.id.btOk)
        btOk.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val cb1 = findViewById<CheckBox>(R.id.cbShow1)
        val cb2 = findViewById<CheckBox>(R.id.cbShow2)
        val cb3 = findViewById<CheckBox>(R.id.cbShow3)
        val cb4 = findViewById<CheckBox>(R.id.cbShow4)
        val cb5 = findViewById<CheckBox>(R.id.cbShow5)
        val dh = DatabaseHelper.getInstance(context)
        val personen = dh.getAllePersonen()
        personen[0].setGetoond(cb1.isChecked)
        dh.updatePersoon(personen[0])
        personen[1].setGetoond(cb2.isChecked)
        dh.updatePersoon(personen[1])
        personen[2].setGetoond(cb3.isChecked)
        dh.updatePersoon(personen[2])
        personen[3].setGetoond(cb4.isChecked)
        dh.updatePersoon(personen[3])
        personen[4].setGetoond(cb5.isChecked)
        dh.updatePersoon(personen[4])
        dismiss()
    }

    fun setCheckboxes() {
        val cb1 = findViewById<CheckBox>(R.id.cbShow1)
        val cb2 = findViewById<CheckBox>(R.id.cbShow2)
        val cb3 = findViewById<CheckBox>(R.id.cbShow3)
        val cb4 = findViewById<CheckBox>(R.id.cbShow4)
        val cb5 = findViewById<CheckBox>(R.id.cbShow5)
        val dh = DatabaseHelper.getInstance(context)
        val personen = dh.getAllePersonen()
        cb1.text = personen[0].getNaam()
        cb1.isChecked = personen[0].getGetoond() == true
        cb2.text = personen[1].getNaam()
        cb2.isChecked = personen[1].getGetoond() == true
        cb3.text = personen[2].getNaam()
        cb3.isChecked = personen[2].getGetoond() == true
        cb4.text = personen[3].getNaam()
        cb4.isChecked = personen[3].getGetoond() == true
        cb5.text = personen[4].getNaam()
        cb5.isChecked = personen[4].getGetoond() == true
    }
}