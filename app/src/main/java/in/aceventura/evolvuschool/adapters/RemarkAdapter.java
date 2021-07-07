package in.aceventura.evolvuschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RemarkActivity;
import in.aceventura.evolvuschool.RemarksDetails;
import in.aceventura.evolvuschool.RequestHandler;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

/**
 * Created by hp on 7/28/2017.
 */

public class RemarkAdapter extends RecyclerView.Adapter<RemarkAdapter.ViewHolder> {

    //Context context;
    private String rem_id;
    private Context context;
    private LayoutInflater inflater;
    private List<DataSet> DataList;
    public boolean showShimmer = true;
    private DatabaseHelper mDatabaseHelper;
    private String name,newUrl,dUrl,Today,parentId;


    public RemarkAdapter(Context context, List<DataSet> remarkItems) {
        this.context = context;
        this.DataList = remarkItems;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.remark_row,parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (showShimmer){
            holder.shimmerFrameLayout3.startShimmer();
        }else{
            holder.shimmerFrameLayout3.startShimmer();
            holder.shimmerFrameLayout3.setShimmer(null);

            holder.remarkDate.setBackground(null);
            holder.remarkTeachername.setBackground(null);
            holder.remarkSSubject.setBackground(null);

            final DataSet m = DataList.get(position);

            String highlight = m.getRmkRead_status();
            if (highlight.equals("0")){
                holder.Rem_card.setBackgroundResource(R.drawable.highlighted_cardshape);
            }else{
                holder.Rem_card.setBackgroundResource(R.drawable.cardshape);
            }

            holder.remarkDate.setText(m.getrDate());
            holder.remarkTeachername.setText(m.getrTName());
            holder.remarkSSubject.setText(m.getrRemarkSubject());
            holder.remarkDescription.setText(m.getrDescription());

            if(m.getrAck().equals("N")){
                Drawable d = context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp);
                holder.remarkAck.setImageDrawable(d);
            }else{
                Drawable d = context.getResources().getDrawable(R.drawable.ic_visibility_24dp);
                holder.remarkAck.setImageDrawable(d);
            }

            String a = m.getrAck();
            Arrays.toString(a.getBytes());

            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DataSet m = DataList.get(position);

                    final String rAc = m.getrId();
                    final String desc = m.getrDescription();
                    final String ackn = m.getrAck();
                    rem_id = m.getrId();

                    StringRequest stringRequestRemark = new StringRequest(Request.Method.POST, newUrl + "remark_read_log_create", response -> {
                    }, error -> Log.i("TAdapter", "onErrorResponse: "+ error.getMessage())) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("parent_id", parentId);
                            params.put("read_date", Today);
                            params.put("remark_id", rem_id);
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
                    RequestHandler.getInstance(context).addToRequestQueue(stringRequestRemark);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "set_remarkAck",

                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    if (ackn.equals("N")) {
                                        Toast.makeText(context, "Acknowledge Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context,RemarkActivity.class);
                                        //changed 18-dec-19
                                        intent.putExtra("CLASSID", m.getClass_id());
                                        intent.putExtra("SECTIONID", m.getSection_id());
                                        intent.putExtra("SID", m.getSid());
                                        intent.putExtra("PID", m.getPid());
                                        context.startActivity(intent);
                                    }
                                    else if (ackn.equals("Y")) {
                                        Intent intent = new Intent(context, RemarksDetails.class);
                                        intent.putExtra("DESCRIPTION", m.getrDescription());
                                        intent.putExtra("CLASSID", m.getClass_id());
                                        intent.putExtra("SECTIONID", m.getSection_id());
                                        intent.putExtra("IMAGELIST", m.getTnImagelist());
                                        intent.putExtra("DATE", m.getrDate());
                                        intent.putExtra("REMID", m.getrId());
                                        intent.putExtra("SID",m.getSid());
                                        intent.putExtra("PID",m.getPid());
                                        context.startActivity(intent);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("remark_id", rem_id);

                            if (name == null || name.equals("")) {
                                name = mDatabaseHelper.getName(1);
                                newUrl = mDatabaseHelper.getURL(1);
                                dUrl = mDatabaseHelper.getPURL(1);
                            }
                            params.put("short_name", name);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        // number of shimmer item shown while loading
        int SHIMMER_ITEM_NUMBER = 6;
        return showShimmer ? SHIMMER_ITEM_NUMBER : this.DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView remarkDate,remarkTeachername,remarkSSubject,remarkDescription,test;
        ImageView remarkAck;
        private View parentView;
        ShimmerFrameLayout shimmerFrameLayout3;
        LinearLayout Rem_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentView = itemView;
            this.remarkDate = itemView.findViewById(R.id.remark_date);
            this.remarkTeachername = itemView.findViewById(R.id.remark_teachername);
            this.remarkSSubject = itemView.findViewById(R.id.remark_ssubject);
            this.remarkDescription = itemView.findViewById(R.id.remark_desc);
            this.remarkAck = itemView.findViewById(R.id.remaerkAck);
            this.test= itemView.findViewById(R.id.test);
            this.shimmerFrameLayout3 = itemView.findViewById(R.id.shimmer_layout3);
            this.Rem_card= itemView.findViewById(R.id.rem_card);
        }
    }
}

