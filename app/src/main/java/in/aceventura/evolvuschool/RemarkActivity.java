package in.aceventura.evolvuschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.RemarkAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.utils.Config;

public class RemarkActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView test;//Important - This is hidden field, Do not delete this line
    RelativeLayout nodata;
    String pid, sid, Sname, newUrl, dUrl, name, classid, sectionid;
    DatabaseHelper mDatabaseHelper;
    private List<DataSet> list = new ArrayList<>();
    private RemarkAdapter remarkadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Student Remarks");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remark);
        getSupportActionBar().hide();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nodata = findViewById(R.id.nodata3);
        nodata.setVisibility(View.GONE);
        test = findViewById(R.id.test);
        test.setActivated(false);

        RecyclerView listView = findViewById(R.id.remark_listView);
        remarkadapter = new RemarkAdapter(this, list);
        listView.setAdapter(remarkadapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sid = bundle.getString("SID");
            sectionid = bundle.getString("SECTIONID");
            classid = bundle.getString("CLASSID");
            pid = bundle.getString("PID");
        }

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_remark);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Student Remarks");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RemarkActivity.this, StudentDashboard.class);
                i.putExtra("CLASSID", classid);
                i.putExtra("SECTIONID", sectionid);
                i.putExtra("SID", sid);
                startActivity(i);
                finish();
            }
        });


        //---------Code to change the logo dynamically based on urls (NEW) -----------------
        String logoUrl;
        if (name.equals("SACS")) {
            logoUrl = dUrl + "uploads/logo.png";
        } else {
            logoUrl = dUrl + "uploads/" + name + "/logo.png";
        }
        Log.e("LogoUrl", "Values:" + logoUrl);
        Glide.with(this).load(logoUrl)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(drawer);

        //-----------------------------------------------------------


        //bottomBar
        try {
            View view = findViewById(R.id.bb_remark);
            TextView supportEmail = view.findViewById(R.id.email);
            //---------------Support email--------------------------
            if (name != null) {
                String supportname = name.toLowerCase();

                supportEmail.setText("For app support email to : " + "support" + supportname + "@aceventura.in");
            } else {
                supportEmail.setText("For app support email to : " + "aceventuraservices@gmail.com");
                return;
            }
            //bottomBar
            BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
            //
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(RemarkActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(RemarkActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(RemarkActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(RemarkActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(RemarkActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        new Handler().postDelayed(() ->
        {

            final String academic_yr = (SharedPrefManager.getInstance(RemarkActivity.this).getAcademicYear());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_premark",
                    response ->
                    {

                        response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        if (response.equals("")) {

                            remarkadapter.showShimmer = false;
                            nodata.setVisibility(View.VISIBLE);

                        } else {
                            try {
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                                if (result.length() == 0) {

                                    remarkadapter.showShimmer = false;
                                    nodata.setVisibility(View.VISIBLE);

                                } else {

                                    remarkadapter.showShimmer = false;
                                    nodata.setVisibility(View.GONE);
                                    for (int i = 0; i < result.length(); i++) {

                                        JSONObject obj = result.getJSONObject(i);

                                        DataSet dataSet = new DataSet();
                                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                                        String inputDateStr = obj.getString("remark_date");

                                        Date date = inputFormat.parse(inputDateStr);
                                        String outputDateStr = outputFormat.format(date);

                                        dataSet.setrDate(outputDateStr);
                                        dataSet.setrRemarkSubject(obj.getString("remark_subject"));
                                        dataSet.setrDescription(obj.getString("remark_desc"));
                                        dataSet.setrTName(obj.getString("teachername"));
                                        dataSet.setrId(obj.getString("remark_id"));
                                        dataSet.setrAck(obj.getString("acknowledge"));
                                        dataSet.setRmkRead_status(obj.getString("read_status"));
                                        dataSet.setTnImagelist(obj.getString("image_list"));
                                        dataSet.setClass_id(classid);
                                        dataSet.setSection_id(sectionid);
                                        dataSet.setSid(sid);
                                        dataSet.setPid(pid);

                                        list.add(dataSet);


                                    }
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        remarkadapter.notifyDataSetChanged();
                    },
                    error -> {
                        remarkadapter.showShimmer = false;
                        Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(Config.STUD_ID, sid);
                    params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);
                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    params.put("short_name", name);
                    params.put("parent_id", pid);
                    System.out.println(params);
                    return params;
                }
            };
            RequestHandler.getInstance(RemarkActivity.this).addToRequestQueue(stringRequest);
            remarkadapter.notifyDataSetChanged();
        }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RemarkActivity.this, StudentDashboard.class);
        i.putExtra("CLASSID", classid);
        i.putExtra("SECTIONID", sectionid);
        i.putExtra("SID", sid);
        startActivity(i);
        finish();
    }
}
