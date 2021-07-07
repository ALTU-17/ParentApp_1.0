package in.aceventura.evolvuschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.aceventura.evolvuschool.R;

public class ImportantRVAdapter extends RecyclerView.Adapter<ImportantRVAdapter.ViewHolder> {


    private List<ListModel> importantLinks;
    private Context mContext;

    public ImportantRVAdapter(Context mContext, List<ListModel> importantLinks) {
        this.importantLinks = importantLinks;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ImportantRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_implinks_rv, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImportantRVAdapter.ViewHolder holder, int position) {
        holder.imp_title.setText(importantLinks.get(position).getImpLinksSubject());
        holder.imp_url.setText(importantLinks.get(position).getImpLinksUrl());

        String implinkDetails = importantLinks.get(position).getImpLinksDetails();

        if (implinkDetails.isEmpty() || implinkDetails.equals("null")) {
            holder.imp_details.setVisibility(View.GONE);
        } else {
            holder.imp_details.setVisibility(View.VISIBLE);
            holder.imp_details.setText(implinkDetails);
        }

        //to open url in browser
        holder.imp_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String impUrl = holder.imp_url.getText().toString();
                Uri uri = Uri.parse(impUrl); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return importantLinks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView imp_title,imp_details,imp_url;

        ViewHolder(View itemView) {
            super(itemView);

            imp_title = itemView.findViewById(R.id.act_subImp);
            imp_details = itemView.findViewById(R.id.act_Impdetails);
            imp_url = itemView.findViewById(R.id.act_ImpUrl);

        }
    }
}

