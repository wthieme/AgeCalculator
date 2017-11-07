package nl.whitedove.agecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class CustomListAdapterDates extends BaseAdapter {

    private List<DatumGeval> listData;
    private LayoutInflater layoutInflater;

    CustomListAdapterDates(Context context, List<DatumGeval> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dates_list_row, parent, false);
            holder = new ViewHolder();
            holder.tvWhat = convertView.findViewById(R.id.tvDrWhat);
            holder.tvWhenDate = convertView.findViewById(R.id.tvDrWhenDate);
            holder.tvWhenTime = convertView.findViewById(R.id.tvDrWhenTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DatumGeval dat = listData.get(position);
        String sAantal = Helper.NumToString(dat.getAantal());
        String s = String.format("%s %ss", sAantal, dat.getEenheid());
        holder.tvWhat.setText(s);
        Helper.eenheidType eenh = dat.getEenheid();
        holder.tvWhenDate.setText(Helper.dFormat.print(dat.getDatumTijd()));
        if (eenh == Helper.eenheidType.hour || eenh == Helper.eenheidType.minute || eenh == Helper.eenheidType.second) {
            holder.tvWhenTime.setText(Helper.tFormatHhMmSs.print(dat.getDatumTijd()));
            holder.tvWhenTime.setVisibility(View.VISIBLE);
        } else {
            holder.tvWhenTime.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvWhat;
        TextView tvWhenDate;
        TextView tvWhenTime;
    }
}