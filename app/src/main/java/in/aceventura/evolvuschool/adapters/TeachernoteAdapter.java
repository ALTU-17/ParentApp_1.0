package in.aceventura.evolvuschool.adapters;

/*
 * Created by hp on 7/27/2017
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RequestHandler;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.TeachernoteDetails;


public class TeachernoteAdapter extends RecyclerView.Adapter<TeachernoteAdapter.ViewHolder> {

    public boolean showShimmer = true;
    private List<DataSet> teachernoteItems;
    private Context context;
    private String name, newUrl, dUrl, Today, parentId;
    private DatabaseHelper mDatabaseHelper;


    public TeachernoteAdapter(Context context, List<DataSet> teachernoteItems) {
        this.context = context;
        this.teachernoteItems = teachernoteItems; // return 5 loading...
        mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        Today = currentDate.format(todayDate);
        parentId = (SharedPrefManager.getInstance(context).getRegId().toString());

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        }
        else {
            holder.shimmerFrameLayout.startShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.tndesc.setBackground(null);
            holder.tnDate.setBackground(null);
            holder.tnTName.setBackground(null);

            final DataSet m = teachernoteItems.get(position);

            String highlight = m.getRead_status();
            if (highlight.equals("0")) {
                holder.noteCard.setBackgroundResource(R.drawable.highlighted_cardshape);
            } else {
                holder.noteCard.setBackgroundResource(R.drawable.cardshape);
            }

            holder.tndesc.setText(m.getTnDescription());
            holder.tnDate.setText(m.getTnDate());
            holder.tnTName.setText(m.getTnTname());


            //on click of particular item...
            holder.parentView.setOnClickListener(v -> {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "note_read_log_create",
                        response -> {
                        },
                        error -> Log.i("TAdapter", "onErrorResponse: " + error.getMessage())) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("parent_id", parentId);
                        params.put("read_date", Today);
                        params.put("notes_id", m.getTnId());
                        if (name == null || name.equals("")) {
                            name = mDatabaseHelper.getName(1);
                            newUrl = mDatabaseHelper.getURL(1);
                            dUrl = mDatabaseHelper.getPURL(1);
                        }
                        params.put("short_name", name);
                        System.out.println(params);
                        return params;
                    }
                };
                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

                DataSet m1 = teachernoteItems.get(position);
                Intent intent = new Intent(context, TeachernoteDetails.class);
                intent.putExtra("SCLASS", m1.getTnClass());
                intent.putExtra("SUBJECT", m1.getTnSubject());
                intent.putExtra("SDATE", m1.getTnDate());
                intent.putExtra("DESCRIPTION", m1.getTnDescription());
                intent.putExtra("IMAGELIST", m1.getTnImagelist());
                intent.putExtra("NOTESID", m1.getTnId());
                intent.putExtra("SSECTION", m1.getTnSection());
                intent.putExtra("SSECTIONID", m1.getTnSectionId());
                intent.putExtra("CID", m1.getClass_id());
                intent.putExtra("SID", m1.getSection_id());
                intent.putExtra("PID", m1.getPid());
                intent.putExtra("StudID", m1.getSid());
                context.startActivity(intent);
            });
        }
    }


    @Override
    public int getItemCount() {
        // number of shimmer item shown while loading
        int SHIMMER_ITEM_NUMBER = 6;
        return showShimmer ? SHIMMER_ITEM_NUMBER : this.teachernoteItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.teachernote_row, parent, false)
        );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tndesc, tnDate, tnTName;
        ShimmerFrameLayout shimmerFrameLayout;
        private View parentView;
        RelativeLayout noteCard;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentView = itemView;
            this.tnDate = itemView.findViewById(R.id.note_date);
            this.tndesc = itemView.findViewById(R.id.note_subject);
            this.tnTName = itemView.findViewById(R.id.note_teachername);
            this.shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            this.noteCard = itemView.findViewById(R.id.note_card);
        }
    }
}
