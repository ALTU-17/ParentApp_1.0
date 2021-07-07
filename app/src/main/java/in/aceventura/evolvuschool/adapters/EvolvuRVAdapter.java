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

import in.aceventura.evolvuschool.EvolvuUpdateDetailPage;
import in.aceventura.evolvuschool.R;

public class EvolvuRVAdapter extends RecyclerView.Adapter<EvolvuRVAdapter.ViewHolder> {


    private List<ListModel> updateList;
    private Context mContext;


    public EvolvuRVAdapter(Context mContext, List<ListModel> updateList) {
        this.updateList = updateList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EvolvuRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_evolvu_rv, parent, false);
        mContext = parent.getContext();

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(EvolvuRVAdapter.ViewHolder holder, final int position) {

        holder.eUpdateTitle.setText(updateList.get(position).getEvolvuSubject());
        holder.eUpdateDescription.setText(updateList.get(position).getEvolvuDetails());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EvolvuUpdateDetailPage.class);
                intent.putExtra("update_title",updateList.get(position).getEvolvuSubject());
                intent.putExtra("update_description", updateList.get(position).getEvolvuDetails());
                intent.putExtra("update_img_url", updateList.get(position).getEvolvuImgUrl());
                intent.putExtra("update_id", updateList.get(position).getEvolvuUpdate_Id());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView eUpdateTitle,eUpdateDescription;


        ViewHolder(View itemView) {
            super(itemView);
            eUpdateTitle = itemView.findViewById(R.id.act_subEvolvu);
            eUpdateDescription = itemView.findViewById(R.id.act_Evolvudetails);
        }
    }
}

