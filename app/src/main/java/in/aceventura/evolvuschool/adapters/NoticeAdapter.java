package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.NoticeDetails;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RequestHandler;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    ProgressDialog progressDialog;
    private Context context;
    private Activity activity;
    private ArrayList<DataSet> MainList;
    private ArrayList<DataSet> TempList;
    private NoticeAdapter.SubjectDataFilter subjectDataFilter;
    private String Today,parentId,name,newUrl,dUrl;
    public DatabaseHelper mDatabaseHelper;

    public NoticeAdapter(Context context, int id, ArrayList<DataSet> subjectArrayList) {

        this.context = context;
        this.TempList = new ArrayList<>();
        this.TempList.addAll(subjectArrayList);
        this.MainList = new ArrayList<>();
        this.MainList.addAll(subjectArrayList);

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
    public int getItemCount() {
        return TempList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    public Filter getFilter() {

        if (subjectDataFilter == null) {

            subjectDataFilter = new NoticeAdapter.SubjectDataFilter();
            notifyDataSetChanged();

        }

        return subjectDataFilter;
    }


    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).
                        inflate(R.layout.notice_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DataSet m = TempList.get(position);
        holder.noticeType.setText(m.getnType());
        holder.noticeSubject.setText(m.getnSubject());
        holder.noticeCreatedBy.setText(m.getnTeacher());


        String highlight = m.getNoticeRead();
        if (highlight.equals("0")){
            holder.noticeCard.setBackgroundResource(R.drawable.highlighted_cardshape);
        }else{
            holder.noticeCard.setBackgroundResource(R.drawable.cardshape);
        }

        //OnClick Of Item...
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "notice_read_log_create",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("NoticeAdapter", "onErrorResponse: "+ error.getMessage());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("parent_id", parentId);
                        params.put("read_date", Today);
                        params.put("notice_id", m.getnId());
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

                //to open details page
                Intent intent = new Intent(context, NoticeDetails.class);
                intent.putExtra("NOTICE_CLASS", m.getnClass());
                intent.putExtra("NOTICE_DATE", m.getnDate());
                intent.putExtra("FROM_DATE", m.getnFromDate());
                intent.putExtra("TO_DATE", m.getnToDate());
                intent.putExtra("START_TIME", m.getnStartTime());
                intent.putExtra("END_TIME", m.getnEndTime());
                intent.putExtra("NOTICE_SUBJECT", m.getnSubject());
                intent.putExtra("NOTICE_DESCRIPTION", m.getnDescription());
                intent.putExtra("NOTICE_FILE", m.getnFile());
                intent.putExtra("NOTICE_ID", m.getnId());
                intent.putExtra("NOTICE_TYPE", m.getnType());
                intent.putExtra("IMAGELIST", m.getTnImagelist());
                intent.putExtra("CID", m.getClass_id());
                intent.putExtra("SECTIONID", m.getSection_id());
                intent.putExtra("SID", m.getSid());
                intent.putExtra("PID",m.getPid());
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView noticeType,noticeSubject,noticeCreatedBy;
        RelativeLayout noticeCard;
        private View parentView;

        ViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
            noticeType = itemView.findViewById(R.id.notice_type);
            noticeSubject = itemView.findViewById(R.id.notice_subject);
            noticeCreatedBy = itemView.findViewById(R.id.created_by);
            noticeCard = itemView.findViewById(R.id.card1);

        }
    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if (charSequence.toString().length() == 0) {
                synchronized (this) {
                    filterResults.values = MainList;
                    filterResults.count = MainList.size();
                }
            } else {
                ArrayList<DataSet> arrayList1 = new ArrayList<>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                    DataSet subject = MainList.get(i);

                    if (subject.toString().toLowerCase().contains(charSequence))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;

            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            TempList.clear();
            TempList.addAll((ArrayList<DataSet>) filterResults.values);

            notifyDataSetChanged();
/*
            int i;
            for (i = 0; i < TempList.size(); i++) {
                TempList.add(TempList.get(i));
            }
*/
//            notifyDataSetChanged();
        }
    }
}
