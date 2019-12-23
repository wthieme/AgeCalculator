package nl.whitedove.agecalculator

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class TimePickerFragment : DialogFragment() {
    var ontimeSet: OnTimeSetListener? = null
    fun setCallBack(ontime: OnTimeSetListener?) {
        ontimeSet = ontime
    }

    private var hour = 0
    private var minute = 0
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        hour = args!!.getInt("hour")
        minute = args.getInt("minute")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, ontimeSet, hour, minute, true)
    }
}