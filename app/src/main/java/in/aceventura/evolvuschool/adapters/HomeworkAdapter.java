package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.HomeworkActivity;
import in.aceventura.evolvuschool.HomeworkDetails;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RequestHandler;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;


/**
 * Created by Hatesh kumar on 11/07/2016.
 */

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder> {

    private List<DataSet> DataList;
    private Context context;
    public boolean showShimmer = true;
    private String sid;
    private String name,newUrl,dUrl,Today,parentId;
    private DatabaseHelper mDatabaseHelper;


    public HomeworkAdapter(Context context, List<DataSet> Datalist) {
        this.context = context;
        this.DataList = Datalist;

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
    public void onBindViewHolder(@NonNull HomeworkAdapter.ViewHolder holder, int position) {

        if (showShimmer){
            holder.shimmerFrameLayout1.startShimmer();
        }else {
            holder.shimmerFrameLayout1.startShimmer();
            holder.shimmerFrameLayout1.setShimmer(null);

            holder.mySubject.setBackground(null);
            holder.mySDate.setBackground(null);
            holder.myEdate.setBackground(null);
            holder.myStatus.setBackground(null);


            final DataSet m = DataList.get(position);

            String highlight = m.getHwRead_status();
            if (highlight.equals("0")){
                holder.hwCard.setBackgroundResource(R.drawable.highlighted_cardshape);
            }else{
                holder.hwCard.setBackgroundResource(R.drawable.cardshape);
            }

            holder.mySubject.setText(m.getSubject());
            holder.mySDate.setText(m.getsDate());
            holder.myEdate.setText(m.geteDate());
            holder.myStatus.setText(m.getStatus());


            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "homework_read_log_create", response -> {
                    }, error -> Log.i("HAdapter", "onErrorResponse: "+ error.getMessage())) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("parent_id", parentId);
                            params.put("read_date", Today);
                            params.put("homework_id", m.getHomeworkId());
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


                    Intent intent = new Intent(context, HomeworkDetails.class);
                    intent.putExtra("SCLASS", m.getsClass());
                    intent.putExtra("SUBJECT", m.getSubject());
                    intent.putExtra("SDATE", m.getsDate());
                    intent.putExtra("EDATE", m.geteDate());
                    intent.putExtra("DESCRIPTION", m.getDescription());
                    intent.putExtra("STATUS", m.getStatus());
                    intent.putExtra("TCOMMENT", m.getTcomment());
                    intent.putExtra("PCOMMENT", m.getPcomment());
                    intent.putExtra("SECTION", m.getSection());
                    intent.putExtra("HOMEWORKID", m.getHomeworkId());
                    intent.putExtra("COMMENTID", m.getCommentId());
                    intent.putExtra("CLASSID", m.getClass_id());
                    intent.putExtra("SECTIONID", m.getSection_id());
                    intent.putExtra("IMAGELIST", m.getTnImagelist());
                    intent.putExtra("SID",m.getSid());
                    intent.putExtra("PID",m.getPid());
                    context.startActivity(intent);
                }
            });

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.homework_row,parent, false)
        );
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 6;
        return showShimmer ? SHIMMER_ITEM_NUMBER : this.DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mySubject,mySDate,myEdate,myStatus;
        private View parentView;
        ShimmerFrameLayout shimmerFrameLayout1;
        LinearLayout hwCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentView = itemView;
            this.mySubject = itemView.findViewById(R.id.subject);
            this.mySDate = itemView.findViewById(R.id.sdate);
            this.myEdate = itemView.findViewById(R.id.edate);
            this.myStatus = itemView.findViewById(R.id.hstatus);
            this.shimmerFrameLayout1 = itemView.findViewById(R.id.shimmer_layout1);
            this.hwCard= itemView.findViewById(R.id.hw_card);
        }
    }
}
