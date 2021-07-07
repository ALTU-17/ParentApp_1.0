package in.aceventura.evolvuschool;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.Calendar_EventsWishAdapter;
import in.aceventura.evolvuschool.adapters.CirtificateAdapter;
import in.aceventura.evolvuschool.adapters.TeachernoteAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class CirtificateActivity extends AppCompatActivity {
    RecyclerView rv_cirtificate;
    LinearLayoutManager layoutManager;
    CirtificateAdapter cirtificateAdapter;
    ArrayList<DataSet> list = new ArrayList<>();
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl, pid;
    String classid, sectionid, Sname, sid;

    String url;
    private final static String TAG = "Cirtificate";
    Date date1 = null;
    SimpleDateFormat sdf;

    LinearLayout ll_Nodata;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Certificate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cirtificate);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll_Nodata = findViewById(R.id.ll_Nodata);
        ll_Nodata.setVisibility(View.GONE);
        mContext = this;
        mDatabaseHelper = new DatabaseHelper(this);
        rv_cirtificate = findViewById(R.id.rv_cirtificate);
        layoutManager = new LinearLayoutManager(this);
        cirtificateAdapter = new CirtificateAdapter(this, list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
            sid = bundle.getString("SID");
            pid = bundle.getString("PID");
        }

        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        url = newUrl + "get_achivements_by_studid";
        Log.e("URL", "Values=>" + url);


        Log.e("NEwPara", "academic_yr" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());//name//"SACS"
        Log.e("NEwPara", "student_id" + sid);////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
        Log.e("NEwPara", "short_name" + name);
        Log.e("NEwPara", "Reg_id" + "1283===" + SharedPrefManager.getInstance(getApplicationContext()).getRegId());
        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_cirtificate);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Certificate");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CirtificateActivity.this, StudentDashboard.class);
                intent.putExtra("SID", SharedPrefManager.getInstance(getApplicationContext()).getStudentId().toString());
                intent.putExtra("CLASSID", SharedPrefManager.getInstance(getApplicationContext()).getStudentClass().toString());
                intent.putExtra("SECTIONID", SharedPrefManager.getInstance(getApplicationContext()).getStudentSection().toString());
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
            View view = findViewById(R.id.bb_cirtificate);
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
                        Intent intent = new Intent(CirtificateActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(CirtificateActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(CirtificateActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(CirtificateActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(CirtificateActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        getCirtificate();


    }

    private void getCirtificate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Respo=>" + response.toString());
                try {
                    if (response == null || response == "" || response.equals("")) {

                        ll_Nodata.setVisibility(View.VISIBLE);
                    } else {
                        ll_Nodata.setVisibility(View.GONE);
                        DataSet set = null;

                        JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                        if (result.length() == 0) {
                            ll_Nodata.setVisibility(View.VISIBLE);
                        } else {
                            ll_Nodata.setVisibility(View.GONE);
                        }
                        Log.e(TAG, "RespoResultJArray=>" + result.toString());
                        for (int i = 0; i < result.length(); i++) {
                            set = new DataSet();
                            JSONObject obj = result.getJSONObject(i);
                            set.setCirCevent(obj.getString("event"));
                            sdf = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-
                            ;
                            try {
                                date1 = sdf.parse(obj.getString("date"));
                                Log.e("Log0", "IMT=" + obj.getString("date"));
                                Log.e("Log1", "IMT=" + date1.getTime() + "daffafa" + date1.getDate());
                                set.setCirdate(date1.getTime());
                            } catch (ParseException e) {
                                Log.e("Tag", "errror" + e.getMessage());
                                e.printStackTrace();
                            }
                            set.setCirfirst_name(obj.getString("first_name"));
                            set.setCirlast_name(obj.getString("last_name"));
                            set.setCirposition(obj.getString("position"));
                            set.setCirstudent_id(obj.getString("student_id"));
                            set.setCiracademic_yr(obj.getString("academic_yr"));
                            set.setCirachievement_id(obj.getString("achievement_id"));
                            list.add(set);

                            cirtificateAdapter = new CirtificateAdapter(CirtificateActivity.this, list);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_cirtificate.setLayoutManager(mLayoutManager);
                            rv_cirtificate.setItemAnimator(new DefaultItemAnimator());
                            rv_cirtificate.setAdapter(cirtificateAdapter);

                        }
                    }
                } catch (Exception e) {
                    e.getMessage();

                    Log.e(TAG, "error" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error=>" + error);
                ll_Nodata.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, "Internet Connection Problem ", Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Toast.makeText(mContext, "Bad Request.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Toast.makeText(mContext, "Parse Error (because of invalid json or xml).",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof OutOfMemoryError) {
                    Toast.makeText(mContext, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext, "server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                    Toast.makeText(mContext, "Server is not responding.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {
                    Toast.makeText(mContext, "Connection timeout error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "An unknown error occurred.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }

                params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());//name//"SACS"
                params.put("student_id", sid);////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);

                Log.e(TAG, "paramiteriii" + params);

                return params;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CirtificateActivity.this, ParentDashboard.class);
        i.putExtra("CLASSID", classid);
        i.putExtra("SECTIONID", sectionid);
        i.putExtra("SID", sid);
        startActivity(i);
        finish();
    }
}