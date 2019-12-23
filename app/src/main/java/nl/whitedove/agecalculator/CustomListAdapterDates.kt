package nl.whitedove.agecalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import nl.whitedove.agecalculator.Helper.eenheidType

internal class CustomListAdapterDates(context: Context, private val listData: ArrayList<DatumGeval>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater
    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return listData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val holder: ViewHolder
        if (cv == null) {
            cv = layoutInflater.inflate(R.layout.dates_list_row, parent, false)
            holder = ViewHolder()
            holder.tvWhat = cv!!.findViewById(R.id.tvDrWhat)
            holder.tvWhenDate = cv.findViewById(R.id.tvDrWhenDate)
            holder.tvWhenTime = cv.findViewById(R.id.tvDrWhenTime)
            cv.tag = holder
        } else {
            holder = cv.tag as ViewHolder
        }
        val dat = listData[position]
        val kind = dat.getKind()
        if (kind == Helper.kindType.absolute) {

            val sAantal = Helper.numToString(dat.getAantal())
            val s = String.format("%s %ss", sAantal, dat.getEenheid())
            holder.tvWhat!!.setText(s)
        } else {
            val s = dat.getTextToShow()
            holder.tvWhat!!.setText(s)
        }

        val eenh = dat.getEenheid()
        holder.tvWhenDate!!.setText(Helper.dFormat.print(dat.getDatumTijd()))
        if (eenh == eenheidType.hour || eenh == eenheidType.minute || eenh == eenheidType.second) {
            holder.tvWhenTime!!.setText(Helper.tFormatHhMmSs.print(dat.getDatumTijd()))
            holder.tvWhenTime!!.setVisibility(View.VISIBLE)
        } else {
            holder.tvWhenTime!!.setVisibility(View.GONE)
        }
        return cv
    }

    private class ViewHolder {
        var tvWhat: TextView? = null
        var tvWhenDate: TextView? = null
        var tvWhenTime: TextView? = null
    }

    init {
        layoutInflater = LayoutInflater.from(context)
    }
}