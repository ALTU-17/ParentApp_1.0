package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.R;

public class ClassTimetableAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSet> DataList;


    public ClassTimetableAdapter(Activity activity, List<DataSet> timetableItems) {
        this.activity = activity;
        this.DataList = timetableItems;
    }

    @Override
    public int getCount() {
        return DataList.size();
    }

    @Override
    public Object getItem(int location) {
        return DataList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.timetable_row, null);

        //Integer period_no = 0;
        TextView period_no = convertView.findViewById(R.id.period_no);
        TextView time = convertView.findViewById(R.id.class_time);
        TextView timeM = convertView.findViewById(R.id.class_timeM);
        TextView timeT = convertView.findViewById(R.id.class_timeT);
        TextView timeW = convertView.findViewById(R.id.class_timeW);
        TextView timeTh = convertView.findViewById(R.id.class_timeTh);
        TextView timeF = convertView.findViewById(R.id.class_timeF);

        TextView sub_monday = convertView.findViewById(R.id.sub_monday);
        TextView sub_tuesday = convertView.findViewById(R.id.sub_tuesday);
        TextView sub_wednesday = convertView.findViewById(R.id.sub_wednesday);
        TextView sub_thrusday = convertView.findViewById(R.id.sub_thursday);
        TextView sub_friday = convertView.findViewById(R.id.sub_friday);
        TextView sub_sat_time = convertView.findViewById(R.id.sub_saturday_period);
        //TextView sub_sat_class_out = (TextView) convertView.findViewById(R.id.sub_saturday_period);
        TextView sub_saturday = convertView.findViewById(R.id.sub_saturday);
        DataSet m = DataList.get(position);

        period_no.setText(m.getPeriodNo().toString());
        time.setText(m.getClassIn() + "-" + m.getClassOut());
        timeM.setText(m.getClassIn() + "-" + m.getClassOut());
        timeT.setText(m.getClassIn() + "-" + m.getClassOut());
        timeW.setText(m.getClassIn() + "-" + m.getClassOut());
        timeTh.setText(m.getClassIn() + "-" + m.getClassOut());
        timeF.setText(m.getClassIn() + "-" + m.getClassOut());
        sub_monday.setText(m.getTtMonday());
        sub_tuesday.setText(m.getTtTuesday());
        sub_wednesday.setText(m.getTtWednesday());
        sub_thrusday.setText(m.getTtThursday());
        sub_friday.setText(m.getTtFriday());

        String sCI = m.getSatClassIn();
        String sCO = m.getSatClassOut();

        if (sCI.equals("null") && sCO.equals("null")){
            sub_sat_time.setText(" ");
        }
        else {
            sub_sat_time.setText(sCI + "-" + sCO);
        }




        if (m.getTtSaturday().isEmpty() || m.getTtSaturday().equals("null") ){
            sub_saturday.setText("");
        }else {
            sub_saturday.setText(m.getTtSaturday());
        }

        return convertView;
    }

}

