package in.aceventura.evolvuschool;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.nex3z.notificationbadge.NotificationBadge;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.encryption.Encryption;
import in.aceventura.evolvuschool.utils.ConstantsFile;

public class StudentDashboard extends AppCompatActivity {
    private static final String TAG = "StudentDashboard";
    String sid;
    String classname;
    String classid;
    String sectionid;
    GridView androidGridView;
    String Sname;
    Context mContext;
    String newUrl, dUrl;
    DatabaseHelper mDatabaseHelper;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    String name;
    ImageButton drawer;
    RelativeLayout stud_name;
    ProgressBar myprogressbar;
    NotificationBadge mBadgeNotes, mBadgeHomework, mBadgeNoticeSMS, mBadgeRemark;
    String gen, fn, rn, cn, sn, tn, parent_id;
    CardView studCardView, noteCardView, homeworkcardView, ParentNoticeView, remarkCardView,
            TimeTable, StudentResult, StudentAttendance, OnlineExam, Cirtificate, cv_chart, cv_healthActivity;
    String filename = "";
    TextView student_Name, tv_roll, tv_cs1, tv_ct1, tv_academic_yr;
    ImageView tlogo, cardstud;
    DownloadManager dm;
    String[] gridViewString = {
            "STUDENT", "TEACHER NOTE", "HOMEWORK",
            "NOTICE/SMS", "REMARK", "TIMETABLE", "CERTIFICATE" /*"FEES SECTION", "RESULT",*/};

    int[] gridViewImageId = {
            R.drawable.boy, R.drawable.teacher, R.drawable.books, R.drawable.chat,
            R.drawable.notepad, R.drawable.calendar, R.drawable.boy /*R.drawable.icon_fees, R.drawable
            .icon_result,*/};
    private String url = "";
    private int STORAGE_PERMISSION_CODE = 23;
    View tb_main1;
    TextView tv_studentDashboardHediing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Student Dashboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        //to hide the toolbar;
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabaseHelper = new DatabaseHelper(this);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);
        mContext = this;
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);
        tlogo = findViewById(R.id.tlogo);
        //bottomBar
        try {
            View view = findViewById(R.id.icd_bottom);
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
            bottomBar.setInActiveTabColor(Color.WHITE);
            bottomBar.setActiveTabColor(Color.WHITE);
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(StudentDashboard.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentDashboard.this, ParentProfile.class);
                        startActivity(intent);
                    }


                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(StudentDashboard.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentDashboard.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {
                        Intent intent = new Intent(StudentDashboard.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        //SHA1 Encryption
        try {
            System.out.println("SHA1ENCRYPTED" + Encryption.SHA1("manoj"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        tv_studentDashboardHediing = findViewById(R.id.tv_studentDashboardHediing);

        parent_id = String.valueOf((SharedPrefManager.getInstance(this).getRegId()));
        tv_studentDashboardHediing.setText(name + " " + getResources().getString(R.string.evolvu_smart_parent_app) + " (" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sid = bundle.getString("SID");
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
        }
        try {
            /* tb_main1 = findViewById(R.id.tb_main1);*/
            drawer = findViewById(R.id.drawer);
            tv_academic_yr = findViewById(R.id.tv_academic_yr);
            tv_academic_yr.setText(SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());
        } catch (Exception e) {
            e.getMessage();
        }
        student_Name = findViewById(R.id.student_Name);
        tv_cs1 = findViewById(R.id.tv_cs1);
        tv_ct1 = findViewById(R.id.tv_ct1);
        tv_roll = findViewById(R.id.tv_roll);
        studCardView = findViewById(R.id.studCardView);
        noteCardView = findViewById(R.id.noteCardView);
        homeworkcardView = findViewById(R.id.homeworkcardView);
        ParentNoticeView = findViewById(R.id.ParentNoticeView);
        remarkCardView = findViewById(R.id.remarkCardView);
        TimeTable = findViewById(R.id.TimeTable);
        StudentResult = findViewById(R.id.StudentResult);
        StudentAttendance = findViewById(R.id.StudentAttendance);
        OnlineExam = findViewById(R.id.online_exam);
        Cirtificate = findViewById(R.id.Cirtificate);
        cv_chart = findViewById(R.id.cv_chart);
        cv_healthActivity = findViewById(R.id.cv_healthActivity);
        cardstud = findViewById(R.id.cardstud);


        stud_name = findViewById(R.id.stud_name);
        myprogressbar = findViewById(R.id.myprogressbar);
        mBadgeNotes = findViewById(R.id.noti_badge_teachernotes);
        mBadgeHomework = findViewById(R.id.noti_badge_homeworks);
        mBadgeNoticeSMS = findViewById(R.id.noti_badge_noticesms);
        mBadgeRemark = findViewById(R.id.noti_badge_remarks);

        StudentResult.setVisibility(View.INVISIBLE);
        cv_healthActivity.setVisibility(View.GONE);
        Cirtificate.setVisibility(View.INVISIBLE);

        //student result module
      /*  if (name.equals("SACS")) {
            StudentResult.setVisibility(View.VISIBLE);
        } else if (name.equals("EVOLVU")) {
            StudentResult.setVisibility(View.VISIBLE);
        } else {
            StudentResult.setVisibility(View.INVISIBLE);
            StudentResult.setVisibility(View.VISIBLE);
        }
        if (name.equals("SACS")) {
            Cirtificate.setVisibility(View.VISIBLE);
            cv_healthActivity.setVisibility(View.VISIBLE);

        } else {
            Cirtificate.setVisibility(View.GONE);
            cv_healthActivity.setVisibility(View.GONE);
            //  Cirtificate.setVisibility(View.VISIBLE);
            //cv_healthActivity.setVisibility(View.VISIBLE);
        }

        cv_healthActivity.setVisibility(View.GONE);*/


        /// toototototo
        getStudent();
        show_icons_parentdashboard_apk();

        //Badeges count
        getNotesUnreadCount();
        getHomewrokUnreadCount();
        getNoticeSmsUnreadCount();
        getRemarkUnreadCount();


        drawer.setOnClickListener(v -> {
            Intent i = new Intent(StudentDashboard.this, NavigationDrawerActivity.class);
            startActivity(i);
        });

        stud_name.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentProfile.class);
            intent.putExtra("SID", sid);
            startActivity(intent);
        });


        studCardView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentProfile.class);
            intent.putExtra("SID", sid);
            Log.e("StudentDashboardPro", "Values>" + sid);
            startActivity(intent);
        });


        noteCardView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, TeachernoteActivity.class);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("SID", sid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });

        homeworkcardView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, HomeworkActivity.class);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SID", sid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });

        ParentNoticeView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, NoticeActivity.class);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SID", sid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });

        remarkCardView.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, RemarkActivity.class);
            intent.putExtra("SID", sid);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });

        TimeTable.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, ClassTimetable.class);
            intent.putExtra("SID", sid);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            startActivity(intent);
        });

        StudentResult.setOnClickListener(v ->
        {
            Intent intent = new Intent(StudentDashboard.this, StudentACademicResult.class);
            intent.putExtra("SID", sid);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            startActivity(intent);
        });


        StudentAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, StudentAttendanceActivity.class);
            intent.putExtra("SID", sid);
            startActivity(intent);
        });

        OnlineExam.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, OnlineExamActivity.class);
            intent.putExtra("SID", sid);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });

        Cirtificate.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, CirtificateActivity.class);
            intent.putExtra("SID", sid);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });
       /* cv_healthActivity.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboard.this, HealthActivity.class);
            intent.putExtra("SID", sid);
            intent.putExtra("CLASSID", classid);
            intent.putExtra("SECTIONID", sectionid);
            intent.putExtra("PID", parent_id);
            startActivity(intent);
        });*/


        String onlineExamCardShowed = SharedPrefManager.getInstance(this).getOnlineExamCardShown();
        if (onlineExamCardShowed.equals("No")) {
            //TODO COMMENT SHOWCASE VIEW
            /*//showing guideView
            ConstantsFile.showGuideView(this, OnlineExam, "Online Exam", "Click here to see the list of " +
                    "Exams. On listing page, on click of 'START' button exam will be started & on click of 'i' " +
                    "icon Exam instructions will be shown for that exam.");*/

            //setting that the guide view was shown "Yes"
            SharedPrefManager.getInstance(this).setOnlineExamCardShown();
        }

    }//oncreate


    private void show_icons_parentdashboard_apk() {
        Log.e("iconsboard", "Respo=url>" + dUrl + "show_icons_parentdashboard_apk");

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "show_icons_parentdashboard_apk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("iconsboard", "Respo=>" + response.toString());
                try {
                    if (response == null || response == "" || response.equals("")) {


                    } else {
                        try {

                            JSONObject object = new JSONObject(response);
                            Log.e("iconsboard", "?>>>>" + object.getString("academic_result"));

                            try {
                                if (object.getString("academic_result").equals("1")) {
                                    StudentResult.setVisibility(View.VISIBLE);
                                } else {
                                    StudentResult.setVisibility(View.INVISIBLE);


                                }


                            } catch (Exception e) {
                                Log.e("iconsboard", "academic_result=>" + e.getMessage());
                                StudentResult.setVisibility(View.INVISIBLE);
                                e.getMessage();
                            }


                            try {

                                if (object.getString("receipt_button").equals("1")) {

                                } else {

                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Log.e("iconsboard", "receipt_button=>" + e.getMessage());

                            }
                            try {

                                if (object.getString("graph").equals("1")) {
                                    cv_chart.setVisibility(View.VISIBLE);

                                    cv_chart.setOnClickListener(v -> {
                                        Intent intent = new Intent(StudentDashboard.this, ChartActivity.class);
                                        intent.putExtra("CLASSID", classid);
                                        intent.putExtra("SECTIONID", sectionid);
                                        intent.putExtra("SID", sid);
                                        startActivity(intent);
                                    });
                                } else {
                                    cv_chart.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Log.e("iconsboard", "receipt_button=>" + e.getMessage());
                                cv_chart.setVisibility(View.GONE);
                            }
                            try {
                                if (object.getString("certificate").equals("1")) {
                                    Cirtificate.setVisibility(View.VISIBLE);
                                } else {
                                    Cirtificate.setVisibility(View.INVISIBLE);

                                }

                            } catch (Exception e) {
                                e.getMessage();
                                Cirtificate.setVisibility(View.INVISIBLE);
                                Log.e("iconsboard", "certificate=>" + e.getMessage());

                            }
                            try {
                                if (object.getString("healthActivity_certificate").equals("1")) {
                                    //   cv_healthActivity.setVisibility(View.VISIBLE);
                                    getHealthActivity();
                                } else {
                                    cv_healthActivity.setVisibility(View.INVISIBLE);

                                }
                            } catch (Exception e) {
                                e.getMessage();
                                cv_healthActivity.setVisibility(View.INVISIBLE);
                                Log.e("iconsboard", "healthActivity_certificate=>" + e.getMessage());

                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    e.getMessage();

                    Log.e("show_academic_result", "erorro=>" + response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("iconsboard", "Respo=GetAcademicYer>" + error.getStackTrace());
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
                ////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);

                Log.e("iconsboard", "params=>" + params.toString());

                return params;
            }
        };

        requestQueue.add(request);
    }


    public void getStudent() {
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
        final String studentid = sid;
        String section = (SharedPrefManager.getInstance(this).getStudentSection().toString());
        //String sid = (SharedPrefManager.getInstance(this).getStudentId().toString());
        String Sname = (SharedPrefManager.getInstance(this).getUsername());
        String cid = (SharedPrefManager.getInstance(this).getStudentClass().toString());
        String pid = (SharedPrefManager.getInstance(this).getParentId().toString());
        String fname = (SharedPrefManager.getInstance(this).getStudentName());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_student",
                response -> {
                    try {
                        Log.e("StudentDashboardPro", "url>" + newUrl + "get_student");

                        Log.e("StudentDashboardPro", "response>" + response);

                        JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                        Log.i("StudentDashboard", "onResponse: " + result);
                        JSONObject sData = result.getJSONObject(0);
                        classname = sData.getString("classname");
                        String Sec = sData.getString("sectionname");
                        String FN = sData.getString("first_name");
                        String RN = sData.getString("roll_no");
                        String TN = sData.getString("class_teacher");
                        String gender = sData.getString("gender");

                        SharedPrefManager.getInstance(getApplicationContext()).childName(FN);
                        SharedPrefManager.getInstance(getApplicationContext()).childRollNo(RN);
                        SharedPrefManager.getInstance(getApplicationContext()).childClass(classname);
                        SharedPrefManager.getInstance(getApplicationContext()).childSection(Sec);
                        SharedPrefManager.getInstance(getApplicationContext()).childTeacher(TN);

                        gen = (SharedPrefManager.getInstance(StudentDashboard.this).getChildGender());
                        fn = (SharedPrefManager.getInstance(StudentDashboard.this).getChildName());
                        rn = (SharedPrefManager.getInstance(StudentDashboard.this).getChildRollNo());
                        cn = (SharedPrefManager.getInstance(StudentDashboard.this).getChildClass());
                        sn = (SharedPrefManager.getInstance(StudentDashboard.this).getChildSection());
                        tn = (SharedPrefManager.getInstance(StudentDashboard.this).getChildTeacher());

                        if (gender.equals("F") || gender.equals("Female") || gender.equals("female")) {
                            tlogo.setImageResource(R.drawable.girl);
                            cardstud.setImageResource(R.drawable.girl);
                        } else {
                            tlogo.setImageResource(R.drawable.boy);
                            cardstud.setImageResource(R.drawable.boy);
                        }

                        tv_cs1.setText(cn + " " + sn);
                        student_Name.setText(fn);

                        //Roll Check
                        if (rn.equals("null") || rn.equals("")) {
                            tv_roll.setText("");
                        } else {
                            tv_roll.setText(rn);
                        }

                        //Teacher name check
                        if (tn.equals("") || tn.equals("null")) {
                            tv_ct1.setText("");
                        } else {
                            tv_ct1.setText(tn);
                        }
                    } catch (JSONException | RuntimeException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentid);
                params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                Log.e("StudentDashboardPro", "response>" + params);

                return params;

            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    //Notes badge count
    private void getNotesUnreadCount() {
        final String studentid = sid;
        final String pid = (SharedPrefManager.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());

        StringRequest stringRequestNotes = new StringRequest(Request.Method.POST, newUrl + "get_count_of_unread_notes",
                response -> {
                    Log.i("CountOfNotes", "" + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String count = jsonObject.getString("unread_notes");

                        if (count != null) {
                            mBadgeNotes.setNumber(Integer.parseInt(count));
                        } else {
                            mBadgeNotes.setNumber(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", studentid);
                params.put("parent_id", pid);
                params.put("acd_yr", academic_yr);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequestNotes);
    }

    //HW badge count
    private void getHomewrokUnreadCount() {
        final String studentid = sid;
        final String pid = (SharedPrefManager.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
        StringRequest stringRequestHomework = new StringRequest(Request.Method.POST, newUrl + "get_count_of_unread_homeworks",
                response -> {
                    Log.i("CountOfHomeworks", "" + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String count = jsonObject.getString("unread_homeworks");

                        if (count != null) {
                            mBadgeHomework.setNumber(Integer.parseInt(count));
                        } else {
                            mBadgeHomework.setNumber(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", studentid);
                params.put("parent_id", pid);
                params.put("acd_yr", academic_yr);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequestHomework);
    }

    //Notices badge count
    private void getNoticeSmsUnreadCount() {
        final String studentid = sid;
        final String pid = (SharedPrefManager.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
        StringRequest stringRequestNoticeSms = new StringRequest(Request.Method.POST, newUrl + "get_count_of_unread_notices",
                response -> {
                    Log.i("CountOfNotices", "" + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String count = jsonObject.getString("unread_notices");


                        if (count != null) {
                            mBadgeNoticeSMS.setNumber(Integer.parseInt(count));
                        } else {
                            mBadgeNoticeSMS.setNumber(0);
                        }

//                            Toast.makeText(StudentDashboard.this, ""+count, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("student_id", studentid);
                params.put("parent_id", pid);
                params.put("acd_yr", academic_yr);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequestNoticeSms);
    }

    //health Activity
    private void getHealthActivity() {
        url = newUrl + "check_health_activity_data_exist_for_studentid";
        Log.e("HealthActivity", "MainUrl>" + url);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("HealthActivity", "Respo helth an activity=>" + response.toString());
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());

                        if (jsonObject.getString("flag").equals("0")) {
                            cv_healthActivity.setVisibility(View.GONE);

                        } else {

                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                    if (response == null || response == "" || response.equals("")) {


                    } else {

                    }
                    JSONObject jsonObject = new JSONObject(response.toString());

                    String first_name = jsonObject.getString("first_name");
                    String mid_name = jsonObject.getString("mid_name");
                    String last_name = jsonObject.getString("last_name");

                    if (jsonObject.getString("flag").equals("1")) {
                        cv_healthActivity.setVisibility(View.VISIBLE);
                        cv_healthActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (mid_name.equals("")) {
                                    filename = "Health_N_Activity_Card_" + first_name + ".pdf".trim();
                                } else {
                                    filename = "Health_N_Activity_Card_" + first_name + "_" + mid_name + ".pdf".trim();
                                }

                                if (last_name.equals("")) {
                                    filename = "Health_N_Activity_Card_" + first_name + "_" + mid_name + ".pdf".trim();
                                } else {
                                    filename = "Health_N_Activity_Card_" + first_name + "_" + mid_name + "_" + last_name + ".pdf".trim();
                                }
                                //todo pdf name as aparna mam said--30-12-2020



                                /* String url = dUrl + "index.php/admin/parent_certificate?operation=download&student_id=" + model.getCirstudent_id() + "&acd_yr=" + model.getCiracademic_yr().trim();*/

                                // String url = dUrl + "index.php/admin/parent_certificate?operation=download&student_id=" + model.getCirstudent_id() + "&acd_yr=" + model.getCiracademic_yr().trim();
                                String url = dUrl + "index.php/fitness/health_activity_card_download?operation=download&student_id=" + sid + "&login_type=P&academic_yr=" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear().trim();
                                //"index.php/fitness/download_health_card_report?student_id=" + sid + "&academic_yr=" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear().trim();

                                Log.e("puel", "url->" + url);
                                try {

                                    if (isReadStorageAllowed()) {
                                        String downloadUrl;

                       /* if (name.equals("SACS")) {
                            downloadUrl = dUrl + "uploads/notice/" + iNoticeId + "/" + itemNoticeValue;
                        } else {
                            downloadUrl = dUrl + "uploads/" + name + "/notice/" + iNoticeId + "/" + itemNoticeValue;
                        }*/

                                        dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                                        Uri uri = Uri.parse(url);
                                        System.out.println("NOTICEDOWNLOADURL - " + uri.toString());
                                        DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                        request.setMimeType("application/");
                                        request.allowScanningByMediaScanner();

                                        String folder = "/Download/Evolvuschool/Parent/HealthCard/";
                                        String StringFile = "/Evolvuschool/Parent/HealthCard/";
                                        File directory = new File(folder);
                                        File directory1 = new File(StringFile);
                                        try {

                                            request.setDestinationInExternalPublicDir(folder, filename);//v 28 allow to create and it deprecated method

                                        } catch (Exception e) {
                                            //v 28+
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + filename);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")"/KrishanImages/" +
                                        }


                                        Log.e("Download", "donpa" + filename);

                                        Toast.makeText(mContext, "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/HealthCard folder or Downloads folder", Toast.LENGTH_LONG).show();
                                        //If directory not exists create one....
                                        if (!directory.exists()) {
                                            directory.mkdirs();
                                        }
                                        if (!directory1.exists()) {
                                            directory1.mkdirs();
                                        }
                                        dm.enqueue(request);
                                        return;

                                    }
                                    requestStoragePermission();
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


                            }
                        });
                    } else {
                        cv_healthActivity.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {


                    e.getMessage();

                    Log.e(TAG, "error" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HealthActivity", "error=>" + error);
                cv_healthActivity.setVisibility(View.GONE);

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
                Log.e("HealthActivity", "paramiteriii" + params);

                return params;
            }
        };

        requestQueue.add(request);
    }

    private void requestStoragePermission() {

        try {


            if (ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                Toast.makeText(mContext, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(StudentDashboard.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(mContext, "Storage Permission" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isReadStorageAllowed() {


        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }


    //Remarks badge count
    private void getRemarkUnreadCount() {
        final String studentid = sid;
        final String pid = (SharedPrefManager.getInstance(this).getRegId().toString());
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
        StringRequest stringRequestRemark = new StringRequest(Request.Method.POST, newUrl + "get_count_of_unread_remarks",
                response -> {
                    Log.i("CountOfRemarks", "" + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String count = jsonObject.getString("unread_remarks");
                        if (count != null) {
                            mBadgeRemark.setNumber(Integer.parseInt(count));
                        } else {
                            mBadgeRemark.setNumber(0);
                        }
//                            Toast.makeText(StudentDashboard.this, ""+count, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentid);
                params.put("parent_id", pid);
                params.put("acd_yr", academic_yr);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequestRemark);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(StudentDashboard.this, ParentDashboard.class);
        startActivity(i);
        finish();
    }
}