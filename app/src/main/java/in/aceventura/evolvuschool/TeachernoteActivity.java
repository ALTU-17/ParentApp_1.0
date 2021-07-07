package in.aceventura.evolvuschool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import in.aceventura.evolvuschool.adapters.TeachernoteAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;


public class TeachernoteActivity extends AppCompatActivity {

    String classid, sectionid, Sname, newUrl, dUrl, name, sid, pid;
    DatabaseHelper mDatabaseHelper;
    RelativeLayout nodata;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    private List<DataSet> list = new ArrayList<>();
    private TeachernoteAdapter teachernoteAdapter;
    private String TAG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*   setTitle("Student Dashboard");*/
        setContentView(R.layout.activity_teachernote);
        /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        //  setFullScreen();
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(this);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        // TODO: 15-11-2019
        int REQUEST_CODE = 1;
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
            sid = bundle.getString("SID");
            pid = bundle.getString("PID");
            Log.e("valuefrombandel", "CID=" + classid + "=SID=" + sectionid + "=sid=" + sid + "=pid=" + pid);
        }


        //Top Bar
        View tb_main1 = findViewById(R.id.tb_main1);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Teacher Note");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent i = new Intent(TeachernoteActivity.this, StudentDashboard.class);
                i.putExtra("SID", SharedPrefManager.getInstance(getApplicationContext()).getStudentId().toString());
                i.putExtra("CLASSID", SharedPrefManager.getInstance(getApplicationContext()).getStudentClass().toString());
                i.putExtra("SECTIONID", SharedPrefManager.getInstance(getApplicationContext()).getStudentSection().toString());

                startActivity(i);*/
                onBackPressed();
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
            View view = findViewById(R.id.bb_tnote);
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

            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(TeachernoteActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(TeachernoteActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(TeachernoteActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(TeachernoteActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(TeachernoteActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        Log.i(TAG, "onCreate:" + newUrl);
        nodata = findViewById(R.id.nodata1);
        nodata.setVisibility(View.GONE);


        RecyclerView rcv = findViewById(R.id.teachernote_list);
        teachernoteAdapter = new TeachernoteAdapter(this, list);
        rcv.setAdapter(teachernoteAdapter);
        rcv.setLayoutManager(layoutManager);


        new Handler().postDelayed(() -> {

            final String academic_yr = (SharedPrefManager.getInstance(TeachernoteActivity.this).getAcademicYear());
            System.out.println("NEW_URL" + newUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_teachernote_with_multiple_attachment",
                    response -> {
                        response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        System.out.println(newUrl);
                        Log.e("respTe", "techer==>>" + response);
                        if (response == null) {
                            teachernoteAdapter.showShimmer = false;
                            nodata.setVisibility(View.VISIBLE);
                        } else
                            try {
                                teachernoteAdapter.showShimmer = false;
                                nodata.setVisibility(View.GONE);
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                                Log.i(TAG, "onResponseTNOTE: " + result);
                                if (result.length() == 0) {
                                    teachernoteAdapter.showShimmer = false;
                                    nodata.setVisibility(View.VISIBLE);
                                } else {
                                    teachernoteAdapter.showShimmer = false;
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject obj = result.getJSONObject(i);
                                        System.out.println(obj);
                                        DataSet dataSet = new DataSet();
                                        dataSet.setTnClass(obj.getString("classname"));
                                        dataSet.setTnSubject(obj.getString("subject_name"));
                                        dataSet.setTnDate(obj.getString("date"));
                                        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        String inputDateStr = obj.getString("date");
                                        Date date = inputFormat.parse(inputDateStr);
                                        String outputDateStr = outputFormat.format(date);
                                        dataSet.setTnDate(outputDateStr);
                                        dataSet.setTnDescription(obj.getString("description"));
                                        dataSet.setTnImagelist(obj.getString("image_list"));
                                        dataSet.setTnTname(obj.getString("name"));
                                        dataSet.setTnId(obj.getString("notes_id"));
                                        dataSet.setTnSectionId(obj.getString("section_id"));
                                        dataSet.setTnSection(obj.getString("sectionname"));
                                        dataSet.setClass_id(classid);
                                        dataSet.setSection_id(sectionid);
                                        dataSet.setPid(pid);
                                        dataSet.setSid(sid);
                                        dataSet.setRead_status(obj.getString("read_status"));
                                        list.add(dataSet);
                                    }
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                                teachernoteAdapter.showShimmer = false;
                                nodata.setVisibility(View.VISIBLE);

                            }
                        teachernoteAdapter.notifyDataSetChanged();
                    },
                    error -> {
                        teachernoteAdapter.showShimmer = false;
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    params.put("short_name", name);
                    params.put("class_id", classid);
                    params.put("section_id", sectionid);
                    params.put("parent_id", pid);
                    params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);
                    System.out.println(params);
                    return params;
                }
            };
            RequestHandler.getInstance(TeachernoteActivity.this).addToRequestQueue(stringRequest);
            teachernoteAdapter.notifyDataSetChanged();
        }, 1000);
    }

    @SuppressWarnings("deprecation")
    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        } else {
            //noinspection deprecation
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }


    /* @Override
     public boolean onOptionsItemSelected(MenuItem menuItem) {
         onBackPressed();
         return true;
     }
 */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TeachernoteActivity.this, StudentDashboard.class);
        i.putExtra("CLASSID", classid);
        i.putExtra("SECTIONID", sectionid);
        i.putExtra("SID", sid);
        startActivity(i);
        finish();
    }

}
