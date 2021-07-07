package in.aceventura.evolvuschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.OnlineExamAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.models.ExamListModel;

public class OnlineExamActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    RelativeLayout nodata;
    OnlineExamAdapter onlineExamAdapter;
    LinearLayoutManager layoutManager;
    String classid, sectionid, newUrl, dUrl, name, stud_id, parent_id;
    private List<ExamListModel> examList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_exam_activty);
        getSupportActionBar().hide();
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

        nodata = findViewById(R.id.nodata1);
        nodata.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stud_id = bundle.getString("SID");
            parent_id = bundle.getString("PID");
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
        }


        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_onlinexam);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Online Exam");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnlineExamActivity.this, StudentDashboard.class);
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
            View view = findViewById(R.id.bb_onlinexam);
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
                        Intent intent = new Intent(OnlineExamActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(OnlineExamActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(OnlineExamActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(OnlineExamActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(OnlineExamActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        //Recyclerview settings
        RecyclerView rcv = findViewById(R.id.rv_online_exams);
        onlineExamAdapter = new OnlineExamAdapter(this, examList);
        rcv.setAdapter(onlineExamAdapter);
        rcv.setLayoutManager(layoutManager);

        new Handler().postDelayed(() -> {
            final String academic_yr = (SharedPrefManager.getInstance(OnlineExamActivity.this).getAcademicYear());
            System.out.println("NEW_URL" + newUrl);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                    "view_uploaded_QuestionbnkStudent", response -> {
                examList.clear();
                response = new String(response.getBytes(StandardCharsets.ISO_8859_1),
                        StandardCharsets.UTF_8);
                System.out.println(newUrl);
                try {
                    Log.i("ONLINE_EXAMS_RESPONSE ", String.valueOf(response));
                    JSONObject jsonObject = new JSONObject(response);
                    int len = jsonObject.length();
                    System.out.println("LENGTH" + len);
                    String download_url = jsonObject.getString("download_url");
                    for (int i = 0; i < jsonObject.length() - 1; i++) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(String.valueOf(i));
                        onlineExamAdapter.showShimmer = false;
                        nodata.setVisibility(View.GONE);
                        String exam_name = jsonObject1.getString("exam_name");
                        String class_name = jsonObject1.getString("class_name");
                        String subject_name = jsonObject1.getString("subject_name");
                        String question_bank_id = jsonObject1.getString("question_bank_id");
                        String exam_id = jsonObject1.getString("exam_id");
                        String class_id = jsonObject1.getString("class_id");
                        String subject_id = jsonObject1.getString("subject_id");
                        String qb_type = jsonObject1.getString("qb_type");
                        String create_date = jsonObject1.getString("create_date");
                        String teacher_id = jsonObject1.getString("teacher_id");
                        String complete = jsonObject1.getString("complete");
                        String acd_yr = jsonObject1.getString("academic_yr");
                        String section_id = jsonObject1.getString("section_id");
                        String status = jsonObject1.getString("status");
                        String weightage = jsonObject1.getString("weightage");
                        String image_list = jsonObject1.getString("image_list");
                        String qb_name = jsonObject1.getString("qb_name");
                        String student_attempt_status = jsonObject1.getString("student_attempt_status");
                        String instructions = jsonObject1.getString("instructions");

                        ExamListModel examListModel = new ExamListModel(exam_name, class_name, subject_name,
                                question_bank_id, exam_id, class_id, subject_id, qb_type, create_date, teacher_id,
                                complete, acd_yr, section_id, status, /*qp_id,*/ weightage,/* sm_id,*/ image_list,
                                qb_name, download_url, stud_id, student_attempt_status, instructions);
                        examList.add(examListModel);
                        onlineExamAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onlineExamAdapter.showShimmer = false;
                    nodata.setVisibility(View.VISIBLE);
                    Log.d("Error1", e.toString());
                    rcv.setVisibility(View.GONE);
                }
            }, error -> {
                onlineExamAdapter.showShimmer = false;
                nodata.setVisibility(View.VISIBLE);
                rcv.setVisibility(View.GONE);
                error.printStackTrace();
                Log.d("Error2", error.toString());
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
                    params.put("acd_yr", academic_yr);
                    params.put("student_id", stud_id);
                    System.out.println("ONLINE_EXAMS" + params);
                    return params;
                }
            };
            RequestHandler.getInstance(OnlineExamActivity.this).addToRequestQueue(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            onlineExamAdapter.notifyDataSetChanged();
        }, 1000);

    }
}
