package in.aceventura.evolvuschool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aceventura.evolvuschool.adapters.ListModel;

class Calendar_AbsentAdapter extends RecyclerView.Adapter<Calendar_AbsentAdapter.ViewHolder> {


    private List<ListModel> dateslist;
    private Context mContext;


    Calendar_AbsentAdapter(List<ListModel> mAbsentDatesArrayList) {
        dateslist =  mAbsentDatesArrayList;
    }

@NonNull
@Override
public Calendar_AbsentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_absent_dates, parent, false);
    Context mContext = parent.getContext();

        return new ViewHolder(view);

        }


    @Override
public void onBindViewHolder(ViewHolder holder, int position) {
        String absent = dateslist.get(position).getAbsent_dates();
        if (absent.isEmpty()){
            holder.tv_absent_dates.setText("");
        }else{
            holder.tv_absent_dates.setText(absent);
        }

        }

@Override
public int getItemCount() {
        return dateslist.size();
        }


    class ViewHolder extends RecyclerView.ViewHolder {
    TextView tv_absent_dates;

    ViewHolder(View itemView) {
        super(itemView);

        tv_absent_dates = itemView.findViewById(R.id.tv_absent_dates);

////        itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
//////                    int pos = getAdapterPosition();
//////                    if (pos != RecyclerView.NO_POSITION) {
////                Intent intent = new Intent(mContext, StudentDashboard.class);
////                intent.putExtra("SID", StudentID);
////                intent.putExtra("CLASSID", ClassID);
////                intent.putExtra("SECTIONID", SectionID);
////                mContext.startActivity(intent);
//
////                    }
//            }
//        });

    }
}
}

