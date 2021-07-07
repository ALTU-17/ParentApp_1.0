package in.aceventura.evolvuschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.StudentDashboard;

public class NewNoticeSMSAdapter extends RecyclerView.Adapter<NewNoticeSMSAdapter.ViewHolder> {

    private String StudentID, SectionID, ClassID;

    private List<ListModel> movieListFiltered;

    private Context mContext;

    public NewNoticeSMSAdapter(List<ListModel> mHomeDataModelArrayList) {
        movieListFiltered = mHomeDataModelArrayList;
    }

    @NonNull
    @Override
    public NewNoticeSMSAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news_rv, parent, false);
        mContext = parent.getContext();

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(NewNoticeSMSAdapter.ViewHolder holder, int position) {


//        holder.imp_title.setText(movieListFiltered.get(position).getNews_title());
//        holder.imp_details.setText(movieListFiltered.get(position).getNews_description());
//        holder.imp_url.setText(movieListFiltered.get(position).getNews_date());

    }

    @Override
    public int getItemCount() {
        return movieListFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView act_sub;
        TextView act_details;
        TextView act_date;


        ViewHolder(View itemView) {
            super(itemView);

            act_sub = itemView.findViewById(R.id.act_sub);
            act_details = itemView.findViewById(R.id.act_details);
            act_date = itemView.findViewById(R.id.act_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(mContext, StudentDashboard.class);
                    intent.putExtra("SID", StudentID);
                    intent.putExtra("CLASSID", ClassID);
                    intent.putExtra("SECTIONID", SectionID);
                    mContext.startActivity(intent);

//                    }
                }
            });

        }
    }
}

