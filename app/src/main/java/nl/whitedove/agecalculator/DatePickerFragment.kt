package nl.whitedove.agecalculator

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DatePickerFragment : DialogFragment() {
    private var ondateSet: OnDateSetListener? = null
    fun setCallBack(ondate: OnDateSetListener?) {
        ondateSet = ondate
    }

    private var year = 0
    private var month = 0
    private var day = 0
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        year = args!!.getInt("year")
        month = args.getInt("month")
        day = args.getInt("day")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, ondateSet, year, month - 1, day)
    }
}