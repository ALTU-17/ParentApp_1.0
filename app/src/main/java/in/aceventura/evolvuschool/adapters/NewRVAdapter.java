package in.aceventura.evolvuschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.SchoolNewsDetailPage;

public class NewRVAdapter extends RecyclerView.Adapter<NewRVAdapter.ViewHolder> {

    private List<ListModel> newsList;
    private Context mContext;

    public NewRVAdapter(Context mContext, List<ListModel> newsList) {
        this.newsList = newsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NewRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news_rv, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewRVAdapter.ViewHolder holder, final int position) {

        holder.news_title.setText(newsList.get(position).getNews_title());
        holder.news_description.setText(newsList.get(position).getNews_description());

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputDateStr = newsList.get(position).getNews_date();

        if (!inputDateStr.equals("null") || inputDateStr != null || !inputDateStr.isEmpty()) {
            Date date = null;
            try {
                date = inputFormat.parse(inputDateStr);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(date);
            holder.news_date.setText(outputDateStr + "  :  ");
        }
        else {
            holder.news_date.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SchoolNewsDetailPage.class);
            intent.putExtra("news_title", newsList.get(position).getNews_title());
            intent.putExtra("news_date", newsList.get(position).getNews_date());
            intent.putExtra("news_description", newsList.get(position).getNews_description());
            intent.putExtra("news_url", newsList.get(position).getNews_url());
            intent.putExtra("news_id", newsList.get(position).getNews_id());
            intent.putExtra("news_image_name", newsList.get(position).getNews_image_name());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView news_title, news_date, news_description;

        ViewHolder(View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.act_sub);
            news_description = itemView.findViewById(R.id.act_details);
            news_date = itemView.findViewById(R.id.act_date);
        }
    }
}

