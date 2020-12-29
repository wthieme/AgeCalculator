package nl.whitedove.agecalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import nl.whitedove.agecalculator.Helper.EenheidType

internal class CustomListAdapterDates(context: Context, private val listData: ArrayList<DatumGeval>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return listData[position]
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
        if (kind == Helper.KindType.Absolute) {

            val sAantal = Helper.numToString(dat.getAantal())
            val s = String.format("%s %ss", sAantal, dat.getEenheid())
            holder.tvWhat!!.text = s
        } else {
            val s = dat.getTextToShow()
            holder.tvWhat!!.text = s
        }

        val eenh = dat.getEenheid()
        holder.tvWhenDate!!.text = Helper.dFormat.print(dat.getDatumTijd())
        if (eenh == EenheidType.Hour || eenh == EenheidType.Minute || eenh == EenheidType.Second) {
            holder.tvWhenTime!!.text = Helper.tFormatHhMmSs.print(dat.getDatumTijd())
            holder.tvWhenTime!!.visibility = View.VISIBLE
        } else {
            holder.tvWhenTime!!.visibility = View.GONE
        }
        return cv
    }

    private class ViewHolder {
        var tvWhat: TextView? = null
        var tvWhenDate: TextView? = null
        var tvWhenTime: TextView? = null
    }

}