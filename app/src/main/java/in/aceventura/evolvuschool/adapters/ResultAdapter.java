package in.aceventura.evolvuschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aceventura.evolvuschool.R;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.DataAdpater> {

    private List<ListModel> newDataList;

    public ResultAdapter(List<ListModel> list) {
        this.newDataList = list;
    }

    @Override
    public ResultAdapter.DataAdpater onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_result_list, parent, false);
        Context mContext = parent.getContext();
        return new DataAdpater(view);
    }

    @Override
    public void onBindViewHolder(DataAdpater holder, int position) {
        holder.sub.setText(newDataList.get(position).getResSubject());
        holder.heading.setText(newDataList.get(position).getResMarkHeading());
        holder.mark.setText(newDataList.get(position).getResMarkObtained());
    }

    @Override
    public int getItemCount() {
        return newDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class DataAdpater extends RecyclerView.ViewHolder {

        TextView sub, heading, mark;

        DataAdpater(View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.sub);
            heading = itemView.findViewById(R.id.heading);
            mark = itemView.findViewById(R.id.mark);
        }
    }
}
