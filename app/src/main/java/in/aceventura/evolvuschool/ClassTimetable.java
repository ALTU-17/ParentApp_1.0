package in.aceventura.evolvuschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.ClassTimetableAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class ClassTimetable extends AppCompatActivity {
    private static final String tag = RemarkActivity.class.getSimpleName();
    private static final String url = "http://androidlearnings.com/school/services/";
    String classid;
    String sectionid,sid;
    String Sname;
    String newUrl, dUrl;
    TextView nodata;
    DatabaseHelper mDatabaseHelper;
    String name;
    ListView listView;
    private List<DataSet> list = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ClassTimetableAdapter timetableadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_class_timetable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        setTitle("Time Table");
//        nodata.setVisibility(View.GONE);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            classid = bundle.getString("CLASSID");
        }
        if (bundle != null) {
            sectionid = bundle.getString("SECTIONID");
            sid = bundle.getString("SID");
        }


        nodata = findViewById(R.id.nodata);
        listView = findViewById(R.id.timetable_listView);
        timetableadapter = new ClassTimetableAdapter(this, list);
        listView.setAdapter(timetableadapter);
        get_timetable();
        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_classtimetable);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear()+")");
        school_title.setText(name+" Evolvu Parent App");
        ht_Teachernote.setText("Time Table");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClassTimetable.this, StudentDashboard.class);
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
            View view = findViewById(R.id.bb_classtimetable);
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
                        Intent intent = new Intent(ClassTimetable.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ClassTimetable.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ClassTimetable.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ClassTimetable.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ClassTimetable.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///





    }


    public void get_timetable() {
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_timetable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        if (response.replace("ï»¿", "").length() < 1) {
//                            nodata.setVisibility(View.VISIBLE);
//                            listView.setVisibility(View.GONE);
                            Toast.makeText(ClassTimetable.this, "Timetable not available for this class", Toast.LENGTH_LONG).show();
                        } else
                            try {
//                                nodata.setVisibility(View.GONE);
//                                listView.setVisibility(View.VISIBLE);
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                                for (int i = 0; i < result.length(); i++) {

                                    JSONObject obj = result.getJSONObject(i);
                                    DataSet dataSet = new DataSet();
                                    dataSet.setPeriodNo(obj.getInt("period_no"));
                                    dataSet.setClassIn(obj.getString("time_in"));
                                    dataSet.setClassOut(obj.getString("time_out"));
                                    dataSet.setTtMonday(obj.getString("monday"));
                                    dataSet.setTtTuesday(obj.getString("tuesday"));
                                    dataSet.setTtWednesday(obj.getString("wednesday"));
                                    dataSet.setTtThursday(obj.getString("thursday"));
                                    dataSet.setTtFriday(obj.getString("friday"));
                                    String saturday_in = obj.getString("sat_in");
                                    String saturday_out = obj.getString("sat_out");
                                    if (saturday_in.equals("0") && saturday_out.equals("0")) {
                                        dataSet.setSatClassIn("");
                                        dataSet.setSatClassOut("");
                                    } else {
                                        dataSet.setSatClassIn(obj.getString("sat_in"));
                                        dataSet.setSatClassOut(obj.getString("sat_out"));
                                    }

                                    String saturday_period = obj.getString("saturday");
                                    if (saturday_period.equals("0")) {
                                        dataSet.setTtSaturday("--");
                                    } else
                                        dataSet.setTtSaturday(obj.getString("saturday"));
                                    list.add(dataSet);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        timetableadapter.notifyDataSetChanged();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);
                params.put("class_id", classid);
                params.put("section_id", sectionid);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);

                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        Intent intent = new Intent(ClassTimetable.this,StudentDashboard.class);
        intent.putExtra("SID", SharedPrefManager.getInstance(this).getStudentId().toString());
        intent.putExtra("CLASSID", SharedPrefManager.getInstance(this).getStudentClass().toString());
        intent.putExtra("SECTIONID", SharedPrefManager.getInstance(this).getStudentSection().toString());
        return true;
    }

}
