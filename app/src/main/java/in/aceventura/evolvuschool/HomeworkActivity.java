package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import in.aceventura.evolvuschool.adapters.HomeworkAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class HomeworkActivity extends AppCompatActivity {
    String sid, pid, classid, sectionid, Sname, newUrl, dUrl, name;
    RelativeLayout nodata;
    DatabaseHelper mDatabaseHelper;
    ProgressBar progressBar;
    private List<DataSet> list = new ArrayList<>();
    private HomeworkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*setTitle("Student Homework");*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        nodata = findViewById(R.id.nodata4);
        nodata.setVisibility(View.GONE);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pid = bundle.getString("PID");
            sid = bundle.getString("SID");
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
        }

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_homework);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Student Homework");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            View view = findViewById(R.id.bb_homework);
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
                        Intent intent = new Intent(HomeworkActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomeworkActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(HomeworkActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomeworkActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(HomeworkActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        RecyclerView listView = findViewById(R.id.listView);
        adapter = new HomeworkAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        final String academic_yr = (SharedPrefManager.getInstance(HomeworkActivity.this).getAcademicYear());

        new Handler().postDelayed(() -> {

            //new code
            final String academic_yr1 = (SharedPrefManager.getInstance(HomeworkActivity.this).getAcademicYear());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_homework",
                    response -> {
                        response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        System.out.println("HW-" + response);
                        if (response.equals("") || response.equals("null")) {
                            adapter.showShimmer = false;
                            nodata.setVisibility(View.VISIBLE);
                        } else
                            try {
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                                if (result.length() == 0) {
                                    nodata.setVisibility(View.VISIBLE);
                                    adapter.showShimmer = false;
                                } else {
                                    adapter.showShimmer = false;
                                    nodata.setVisibility(View.GONE);
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject obj = result.optJSONObject(i);
                                        DataSet dataSet = new DataSet();
                                        dataSet.setsClass(obj.getString("class_name"));
                                        dataSet.setSection(obj.getString("division"));
                                        dataSet.setSubject(obj.getString("subject_name"));
                                        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        String inputDateStr = obj.getString("start_date");
                                        Date date = inputFormat.parse(inputDateStr);
                                        String outputDateStr = outputFormat.format(date);
                                        dataSet.setsDate(outputDateStr);
                                        String input_e_date = obj.getString("end_date");
                                        Date date1 = inputFormat.parse(input_e_date);
                                        String output_e_date = outputFormat.format(date1);
                                        dataSet.seteDate(output_e_date);
                                        dataSet.setDescription(obj.getString("description"));
                                        dataSet.setStatus(obj.getString("homework_status"));
                                        dataSet.setTcomment(obj.getString("comment"));
                                        dataSet.setPcomment(obj.getString("parent_comment"));
                                        dataSet.setHomeworkId(obj.getString("homework_id"));
                                        dataSet.setCommentId(obj.getString("comment_id"));
                                        dataSet.setSid(sid);
                                        dataSet.setPid(pid);
                                        dataSet.setClass_id(classid);
                                        dataSet.setSection_id(sectionid);
                                        dataSet.setHwRead_status(obj.getString("read_status"));
                                        dataSet.setTnImagelist(obj.getString("image_list"));
                                        list.add(dataSet);
                                    }
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        adapter.notifyDataSetChanged();
                    },
                    error -> {
                        nodata.setVisibility(View.VISIBLE);
                        adapter.showShimmer = false;
                        Log.i("error", "onErrorResponse: " + error.toString());
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("student_id", sid);
                    params.put("section_id", sectionid);
                    params.put("class_id", classid);
                    params.put("parent_id", pid);
                    params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr1);

                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    params.put("short_name", name);
                    return params;
                }
            };
            RequestHandler.getInstance(HomeworkActivity.this).addToRequestQueue(stringRequest);
            adapter.notifyDataSetChanged();

        }, 1000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HomeworkActivity.this, StudentDashboard.class);
        i.putExtra("CLASSID", classid);
        i.putExtra("SECTIONID", sectionid);
        i.putExtra("SID", sid);
        startActivity(i);
        finish();
    }
}
