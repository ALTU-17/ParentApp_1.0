package in.aceventura.evolvuschool;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.adapters.ChildAdapter;
import in.aceventura.evolvuschool.adapters.EvolvuRVAdapter;
import in.aceventura.evolvuschool.adapters.ImportantRVAdapter;
import in.aceventura.evolvuschool.adapters.ListModel;
import in.aceventura.evolvuschool.adapters.NewRVAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.utils.Config;
import in.aceventura.evolvuschool.utils.ConstantsFile;
import in.aceventura.evolvuschool.utils.FirebaseNotificationUtils;

import static in.aceventura.evolvuschool.utils.ConstantsFile.flagVersion;

public class ParentDashboard extends AppCompatActivity {
    public String Sname = "";
    public List<ListModel> mChildrenList = new ArrayList<>();
    public List<ListModel> mNewsList = new ArrayList<>();
    public List<ListModel> mImportantList = new ArrayList<>();
    public List<ListModel> mUpdateList = new ArrayList<>();
    TextView student_Name;
    TextView supportEmail, newsLabel, impLinksLabel, evolvuUpdateLabel;
    LinearLayout layout;
    ImageView tlogo;
    ImageButton notify, drawer;
    String newUrl, dUrl, parent_id;
    String check_sms_consent_status_url, save_sms_consent_url;
    DatabaseHelper mDatabaseHelper;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    String name;
    String androidversion, forced_update;
    String birth_day;
    String birth_month;
    String sys_day;
    String sys_month;
    String test;
    String test1;
    String academic_yr;
    ProgressBar pb;
    RecyclerView childRV, newRV, evolvuRV, impRV;
    public ChildAdapter mChildAdapter;
    public NewRVAdapter mNewRVAdapter;
    public EvolvuRVAdapter mEvolvuRVAdapter;
    public ImportantRVAdapter mImportantRVAdapter;
    Context context;
    CardView birthdaycard;
    TextView birthdaydayname, title, tv_academic_yr;
    Integer reg_id;
    String loginPwd, default_pwd;
    RelativeLayout student_not_available;


    LinearLayout s;
    String class_id, section_id;
    View icd_parentDashboard;
    LinearLayout ll_term_conditions;
    String flag = "";
    final int time = 6000;
    String versionName;
    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Parent Dashboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_dashboard);
        context = this;

        versionName = BuildConfig.VERSION_NAME;
        Username = getIntent().getStringExtra("User_id");
        /*  icd_parentDashboard = findViewById(R.id.icd_parentDashboard);*/
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        try {
            Log.e("flags", "values??" + SharedPrefManager.getInstance(getApplicationContext()).getActivityName());
            if (SharedPrefManager.getInstance(getApplicationContext()).getActivityName().equals("remark")) {

                Intent intent = new Intent(getApplicationContext(), RemarkActivity.class);
                SharedPrefManager.getInstance(getApplicationContext()).setActivityName("");
                intent.putExtra("SID", FirebaseNotificationUtils.Remarkstud_id);
                intent.putExtra("CLASSID", FirebaseNotificationUtils.Remarkclass_id);
                intent.putExtra("SECTIONID", FirebaseNotificationUtils.Remarksection_id);
                intent.putExtra("PID", FirebaseNotificationUtils.Remarkparent_id);
                Log.e("flags", "Paravalues>RID<" + "SID>" + FirebaseNotificationUtils.Remarkstud_id);
                Log.e("flags", "Paravalues>RID<" + "CID>" + FirebaseNotificationUtils.Remarkclass_id);
                Log.e("flags", "Paravalues>RID<" + "SsID>" + FirebaseNotificationUtils.Remarksection_id);
                Log.e("flags", "Paravalues>RID<" + "PID>" + FirebaseNotificationUtils.Remarkparent_id);
                startActivity(intent);

            }
            if (SharedPrefManager.getInstance(getApplicationContext()).getActivityName().equals("homework")) {

                Intent intent = new Intent(getApplicationContext(), HomeworkActivity.class);
                intent.putExtra("SID", FirebaseNotificationUtils.HomeWorkstud_id);
                SharedPrefManager.getInstance(getApplicationContext()).setActivityName("");
                intent.putExtra("CLASSID", FirebaseNotificationUtils.HomeWorkclass_id);
                intent.putExtra("SECTIONID", FirebaseNotificationUtils.HomeWorksection_id);
                intent.putExtra("PID", FirebaseNotificationUtils.HomeWorkparent_id);
                Log.e("flags", "Paravalues>RID<" + "SID>" + FirebaseNotificationUtils.HomeWorkstud_id);
                Log.e("flags", "Paravalues>RID<" + "CID>" + FirebaseNotificationUtils.HomeWorkclass_id);
                Log.e("flags", "Paravalues>RID<" + "SsID>" + FirebaseNotificationUtils.HomeWorksection_id);
                Log.e("flags", "Paravalues>RID<" + "PID>" + FirebaseNotificationUtils.HomeWorkparent_id);
                startActivity(intent);

            }
            if (SharedPrefManager.getInstance(getApplicationContext()).getActivityName().equals("note")) {

                Intent intent = new Intent(getApplicationContext(), TeachernoteActivity.class);
                intent.putExtra("SID", FirebaseNotificationUtils.Noteclass_id);
                SharedPrefManager.getInstance(getApplicationContext()).setActivityName("");
                intent.putExtra("CLASSID", FirebaseNotificationUtils.Noteclass_id);
                intent.putExtra("SECTIONID", FirebaseNotificationUtils.Notesection_id);
                intent.putExtra("PID", FirebaseNotificationUtils.Noteparent_id);
                Log.e("flags", "Paravalues>RID<" + "SID>" + FirebaseNotificationUtils.Noteclass_id);
                Log.e("flags", "Paravalues>RID<" + "CID>" + FirebaseNotificationUtils.Noteclass_id);
                Log.e("flags", "Paravalues>RID<" + "SsID>" + FirebaseNotificationUtils.Notesection_id);
                Log.e("flags", "Paravalues>RID<" + "PID>" + FirebaseNotificationUtils.Noteparent_id);
                startActivity(intent);

            }
            if (SharedPrefManager.getInstance(getApplicationContext()).getActivityName().equals("notice")) {

                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                intent.putExtra("SID", FirebaseNotificationUtils.Noticestud_id);
                SharedPrefManager.getInstance(getApplicationContext()).setActivityName("");
                intent.putExtra("CLASSID", FirebaseNotificationUtils.Noticeclass_id);
                intent.putExtra("SECTIONID", FirebaseNotificationUtils.Noticesection_id);
                intent.putExtra("PID", FirebaseNotificationUtils.Noticeparent_id);
                Log.e("flags", "Paravalues>RID<" + "SID>" + FirebaseNotificationUtils.Noticeclass_id);
                Log.e("flags", "Paravalues>RID<" + "CID>" + FirebaseNotificationUtils.Noticeclass_id);
                Log.e("flags", "Paravalues>RID<" + "SsID>" + FirebaseNotificationUtils.Noticesection_id);
                Log.e("flags", "Paravalues>RID<" + "PID>" + FirebaseNotificationUtils.Noticeparent_id);
                startActivity(intent);

            }

        } catch (Exception e) {
            e.getMessage();
        }

        Log.e("JsonValue", "Vzasf>>" + SharedPrefManager.getInstance(getApplicationContext()).getNotiremark_ID());

        // it's the delay time for sliding between items in recyclerview

        mDatabaseHelper = new DatabaseHelper(this);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);

        name = mDatabaseHelper.getName(1);
        ll_term_conditions = findViewById(R.id.ll_term_conditions);
        ll_term_conditions.setVisibility(View.GONE);

        Log.e("SCode", "Url===>>" + name);

        newUrl = mDatabaseHelper.getURL(1);
        Log.e("ParentUrl", "Url===>>" + newUrl);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        System.out.println("URLTEST" + newUrl);
        //method to get the app version from the server
        getVersion();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("NotificationValue", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast


                        Log.e("NotificationValue", "TokanValue>" + token);

                    }
                });


        // TODO: 08-06-2020  
        //method to check whether the pwd is default of not


        newsLabel = findViewById(R.id.newsLabel);
        impLinksLabel = findViewById(R.id.impLinksLabel);
        evolvuUpdateLabel = findViewById(R.id.evolvuUpdateLabel);
        try {
            title = findViewById(R.id.title);

            tv_academic_yr = findViewById(R.id.tv_academic_yr);
            notify = findViewById(R.id.notify);
            drawer = findViewById(R.id.drawer);
        } catch (Exception e) {
            e.getMessage();
        }

        s = findViewById(R.id.s);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tlogo = findViewById(R.id.tlogo);
        student_Name = findViewById(R.id.student_Name);
        childRV = findViewById(R.id.rcview);
        newRV = findViewById(R.id.newsRV);
        impRV = findViewById(R.id.impRV);
        evolvuRV = findViewById(R.id.evolvuRV);
        birthdaycard = findViewById(R.id.birthdaycard);
        birthdaydayname = findViewById(R.id.birthdaydayname);
        pb = findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        birthdaycard.setVisibility(View.GONE);

        //no student record for current acd yr ,then showing this msg
        student_not_available = findViewById(R.id.student_not_available);
        student_not_available.setVisibility(View.GONE);
        check_sms_concern_status();
        //bottombar


        try {
            View icd_bottom = findViewById(R.id.icd_bottom);
            BottomBar bottomBar = (BottomBar) icd_bottom.findViewById(R.id.bottomBar);
            supportEmail = icd_bottom.findViewById(R.id.email);
            bottomBar.setDefaultTabPosition(0);

            try {
                bottomBar.setActiveTabColor(getResources().getColor(R.color.bottomactivateColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    if (tabId == R.id.tab_calendar) {

                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentDashboard.this, MyCalendar.class);
                        intent.putExtra("class_id", class_id);
                        intent.putExtra("section_id", section_id);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.

                        Intent intent = new Intent(ParentDashboard.this, ParentProfile.class);
                        intent.putExtra("class_id", class_id);
                        intent.putExtra("section_id", section_id);
                        startActivity(intent);
                    }


                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentDashboard.this, MyCalendar.class);
                        intent.putExtra("class_id", class_id);
                        intent.putExtra("section_id", section_id);
                        startActivity(intent);
                        bottomBar.findPositionForTabWithId(tabId);
                    }
                    if (tabId == R.id.tab_profile) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentDashboard.this, ParentProfile.class);
                        intent.putExtra("class_id", class_id);
                        intent.putExtra("section_id", section_id);
                        startActivity(intent);
                        bottomBar.findPositionForTabWithId(tabId);
                    }
                    if (tabId == R.id.tab_dashboard) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentDashboard.this, ParentDashboard.class);
                        intent.putExtra("class_id", class_id);
                        intent.putExtra("section_id", section_id);
                        startActivity(intent);
                        bottomBar.findPositionForTabWithId(tabId);
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

        closeDialog();


        Log.e("flags", "set before called" + ConstantsFile.flagN);
        Log.e("flags", "ConstantFile");
        Log.e("LIFECYCLE", "OnCreatere" + ConstantsFile.flagN);

        //  if (SharedPrefManager.getInstance(getApplicationContext()).getFlag().equals("")) {
        if (ConstantsFile.flagN.equals("")) {

            Log.e("flags", "set 1 called");

            // SharedPrefManager.getInstance(getApplicationContext()).setFlag("");
            ConstantsFile.flagN = "";
            getAcademicYear();
            getOnInit1();

        } else {
            openDialog();

            getAcademicYear();

            //  Toast.makeText(getApplicationContext(), "inside Old", Toast.LENGTH_SHORT).show();
        }

        title.setText(name + " EvolvU Smart Parent App" + " (" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
    } //Oncreate

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("flags", "values>>>" + ConstantsFile.flagN);
        Log.e("flags", "values>>>");

    }


    private void getOnInit1() {
        Log.e("LIFECYCLE", "OnVAlues" + ConstantsFile.flagN);

        academic_yr = (SharedPrefManager.getInstance(ParentDashboard.this).getAcademicYear());
        tv_academic_yr.setText("(" + academic_yr + ")");
        reg_id = (SharedPrefManager.getInstance(ParentDashboard.this).getRegId());

        default_pwd = (SharedPrefManager.getInstance(this).getPassword());

        Intent in = getIntent();
        loginPwd = in.getStringExtra("password");

        // TODO: setting flag to prevent this method calling api every time app is open
        if (flagVersion == 0) {
            sendVersionToServer(versionName, reg_id, Username);
            flagVersion = 1;
        }

        //nav
        drawer.setOnClickListener(v -> {
            Intent i = new Intent(ParentDashboard.this, NavigationDrawerActivity.class);
            startActivity(i);
        });

        //-----------------------------------------------------------------------------------------

        Toolbar mToolbar = findViewById(R.id.tb_main);
        if (name != null) {
            mToolbar.setTitle(name + " EvolvU");
            mToolbar.setSubtitle("Smart Schooling App");
            setSupportActionBar(mToolbar);

            if (name.equals("EVOLVU")) {
                mToolbar.setTitle("EvolvU");
            } else {
                mToolbar.setTitle(name + " EvolvU");
            }
            mToolbar.setSubtitle("Smart Schooling App");
            mToolbar.setSubtitleTextColor(Color.WHITE);
            mToolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(mToolbar);
        } else {
            mToolbar.setTitle(name + "EvolvU");
            mToolbar.setSubtitle("Smart Schooling App");
            setSupportActionBar(mToolbar);
        }

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            return;
        }

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, NewLoginPage.class));
        }


        final String ay = (SharedPrefManager.getInstance(this).getAcademicYear());
        parent_id = String.valueOf((SharedPrefManager.getInstance(this).getRegId()));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 2, 0, 0);

        //---------------Support email--------------------------
        if (name != null) {
            String supportname = name.toLowerCase();

            supportEmail.setText("For app support email to : " + "support" + supportname + "@aceventura.in");
        } else {
            supportEmail.setText("For app support email to : " + "aceventuraservices@gmail.com");
            return;
        }

        //------------------------------------------------------

        //TODO: SchoolNewsRV
        mNewRVAdapter = new NewRVAdapter(this, mNewsList);
        newRV.setAdapter(mNewRVAdapter);
        final LinearLayoutManager newsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newRV.setLayoutManager(newsLinearLayoutManager);

        //The LinearSnapHelper will snap the center of the target child view to the center of the attached RecyclerView , it's optional if you want , you can use it
        final LinearSnapHelper newslinearSnapHelper = new LinearSnapHelper();
        newslinearSnapHelper.attachToRecyclerView(newRV);

        final Timer newstimer = new Timer();
        newstimer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (newsLinearLayoutManager.findLastCompletelyVisibleItemPosition() < (mNewRVAdapter.getItemCount() - 1)) {

                    newsLinearLayoutManager.smoothScrollToPosition(newRV, new RecyclerView.State(), newsLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                } else if (newsLinearLayoutManager.findLastCompletelyVisibleItemPosition() == (mNewRVAdapter.getItemCount() - 1)) {

                    newsLinearLayoutManager.smoothScrollToPosition(newRV, new RecyclerView.State(), 0);
                }
            }
        }, 0, time);

        //--------------------------------------------------------------------------------------------------------------------------------------

        //TODO: EvolvuUpdateRV
        mEvolvuRVAdapter = new EvolvuRVAdapter(this, mUpdateList);
        evolvuRV.setAdapter(mEvolvuRVAdapter);
        final LinearLayoutManager evolvuLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        evolvuRV.setLayoutManager(evolvuLinearLayoutManager);

        //The LinearSnapHelper will snap the center of the target child view to the center of the attached RecyclerView , it's optional if you want , you can use it
        final LinearSnapHelper updatelinearSnapHelper = new LinearSnapHelper();
        updatelinearSnapHelper.attachToRecyclerView(evolvuRV);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (evolvuLinearLayoutManager.findLastCompletelyVisibleItemPosition() < (mEvolvuRVAdapter.getItemCount() - 1)) {

                    evolvuLinearLayoutManager.smoothScrollToPosition(evolvuRV, new RecyclerView.State(), evolvuLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                } else if (evolvuLinearLayoutManager.findLastCompletelyVisibleItemPosition() == (mEvolvuRVAdapter.getItemCount() - 1)) {

                    evolvuLinearLayoutManager.smoothScrollToPosition(evolvuRV, new RecyclerView.State(), 0);
                }
            }
        }, 0, time);

        //----------------------------------------------------------------------------------------------------------------------------------------------
        //TODO: ImpLinksRV
        mImportantRVAdapter = new ImportantRVAdapter(this, mImportantList);
        impRV.setAdapter(mImportantRVAdapter);
        final LinearLayoutManager implinkLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        impRV.setLayoutManager(implinkLinearLayoutManager);

        //The LinearSnapHelper will snap the center of the target child view to the center of the attached RecyclerView , it's optional if you want , you can use it
        final LinearSnapHelper evolvuLinearSnapHelper = new LinearSnapHelper();
        evolvuLinearSnapHelper.attachToRecyclerView(impRV);

        final Timer implinkTimer = new Timer();
        implinkTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (implinkLinearLayoutManager.findLastCompletelyVisibleItemPosition() < (mImportantRVAdapter.getItemCount() - 1)) {

                    implinkLinearLayoutManager.smoothScrollToPosition(impRV, new RecyclerView.State(), implinkLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                } else if (implinkLinearLayoutManager.findLastCompletelyVisibleItemPosition() == (mImportantRVAdapter.getItemCount() - 1)) {

                    implinkLinearLayoutManager.smoothScrollToPosition(impRV, new RecyclerView.State(), 0);
                }
            }
        }, 0, time);


        //----------------------------------------------------------------------------------------------------------------------------------------------

        //TODO: ChildList
        mChildAdapter = new ChildAdapter(this, mChildrenList);
        childRV.setAdapter(mChildAdapter);
        childRV.setLayoutManager(new LinearLayoutManager(this));

        new Handler().postDelayed(() -> {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_childs",
                    response -> {
                        Log.e("Parentdashoad000", "ValeResponce>>" + response);
                        System.out.println("MANOJWAGHMARE" + response);
                        if (response != null && !response.equals("") && !response.equals("null")) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                                int msg_count = 0;
                                int size = jsonArray.length();
                                int count = 0;
                                int i = 0;

                                while (i <= size - 1) {
                                    count++;
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    if (jsonObject != null) {
                                        mChildAdapter.showShimmer = false;
                                        Log.i("CHILD_DATA", "onResponse: " + jsonObject);


                                        //database values
                                        String studentName = jsonObject.optString("first_name");
                                        String studentGender = jsonObject.optString("gender");
                                        String studentRollNo = jsonObject.optString("roll_no");
                                        String studentClass = jsonObject.optString("class_name");
                                        String studentSection = jsonObject.optString("section_name");
                                        String studentTeacherName = jsonObject.optString("class_teacher");
                                        String student_Id = jsonObject.optString("student_id");
                                        class_id = jsonObject.optString("class_id");
                                        section_id = jsonObject.optString("section_id");

                                        SharedPrefManager.getInstance(ParentDashboard.this).setClassId(class_id);
                                        SharedPrefManager.getInstance(ParentDashboard.this).setSectionId(section_id);

                                        String student_dob = jsonObject.optString("dob");
                                        String gen = jsonObject.optString("gender");
                                        mStudentDatabaseHelper.saveStudentDetails(studentName, studentGender, studentRollNo, studentClass, studentSection, studentTeacherName, student_Id, class_id, section_id, student_dob);
                                        mStudentDatabaseHelper.close();
                                        SharedPrefManager.getInstance(getApplicationContext()).childGender(gen);


                                        ListModel listModel = new ListModel();
                                        listModel.setFirst_name(studentName);
                                        listModel.setGender(studentGender);
                                        listModel.setRoll_no(studentRollNo);
                                        listModel.setClass_name(studentClass);//studentSection
                                        listModel.setSection_name(studentSection);
                                        listModel.setTeacher_name(studentTeacherName);
                                        listModel.setStud_id(student_Id);
                                        listModel.setClass_id(class_id);
                                        listModel.setSection_id(section_id);
                                        listModel.setBirthday(student_dob);
                                        mChildrenList.add(listModel);
                                        mChildAdapter.notifyDataSetChanged();

                                        /*ListModel listModel = new ListModel();
                                        listModel.setFirst_name(mStudentDatabaseHelper.getStudentName(i + 1));
                                        listModel.setGender(mStudentDatabaseHelper.getGender(i + 1));
                                        listModel.setRoll_no(mStudentDatabaseHelper.getRollNo(i + 1));
                                        listModel.setClass_name(mStudentDatabaseHelper.getClass(i + 1));
                                        listModel.setSection_name(mStudentDatabaseHelper.getSection(i + 1));
                                        listModel.setTeacher_name(mStudentDatabaseHelper.getTeacherName(i + 1));
                                        listModel.setStud_id(mStudentDatabaseHelper.getStudentId(i + 1));
                                        listModel.setClass_id(mStudentDatabaseHelper.getClassId(i + 1));
                                        listModel.setSection_id(mStudentDatabaseHelper.getSectionId(i + 1));
                                        listModel.setBirthday(mStudentDatabaseHelper.getDob(i + 1));
                                        mChildrenList.add(listModel);*/

                                        /* dob of child*/
//                                        String dob = jsonObject.optString("dob");
                                        String dob = mStudentDatabaseHelper.getDob(i + 1);

                                        if (dob.equals("null") || dob.isEmpty()) {
                                            birthdaycard.setVisibility(View.GONE);
                                        } else {
                                            //student dob parsing
                                            Date date;
                                            try {
                                                date = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                                                dob = new SimpleDateFormat("dd-MM-yyyy").format(date);

                                                String[] birthday = dob.split("-");
                                                birth_day = birthday[0];
                                                birth_month = birthday[1];
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            /* System Date...*/
                                            Date c = Calendar.getInstance().getTime();
                                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                            String formattedDate = df.format(c);
                                            String[] system = formattedDate.split("-");
                                            sys_day = system[0];
                                            sys_month = system[1];

                                            /* Checking Condition to show the card for birthday wish..*/
                                            if (birth_day.equals(sys_day) && birth_month.equals(sys_month)) {
                                                msg_count++;
                                                if (msg_count == 1) {
                                                    test = jsonObject.optString("first_name");
                                                    birthdaydayname.setText(test);
                                                    birthdaycard.setVisibility(View.VISIBLE);
                                                } else if (msg_count == 2) {
                                                    test1 = test + " , " + jsonObject.optString("first_name");
                                                    test = test + " & " + jsonObject.optString("first_name");
                                                    birthdaydayname.setText(test);
                                                    birthdaycard.setVisibility(View.VISIBLE);
                                                } else {
                                                    birthdaydayname.setText(test1 + " & " + jsonObject.optString("first_name"));
                                                    birthdaycard.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                birthdaycard.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        Toast.makeText(ParentDashboard.this, "No Response from Server", Toast.LENGTH_SHORT).show();
                                        mChildAdapter.showShimmer = false;
                                    }
                                    i++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //uncomment for new release
                                mChildAdapter.showShimmer = false;
                                student_not_available.setVisibility(View.VISIBLE);
                            }
                            mChildAdapter.notifyDataSetChanged();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        mChildAdapter.showShimmer = false;
//                        student_not_available.setVisibility(View.VISIBLE);
                        Toast.makeText(ParentDashboard.this, "No Response from Server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("", "get_children: " + error.getMessage());
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params1 = new HashMap<>();
                    params1.put(SharedPrefManager.KEY_REG_ID, reg_id.toString());
                    params1.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);

                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    params1.put("short_name", name);
                    Log.i("CHILD_PARAM", "Valus:" + params1.toString());
                    Log.e("Parentdashoad000", "params1>>" + params1);

                    return params1;

                }
            };

            RequestHandler.getInstance(ParentDashboard.this).addToRequestQueue(stringRequest);

            mChildAdapter.notifyDataSetChanged();
        }, 1000);

        // TODO: New Modules

        newsLabel.setVisibility(View.GONE);
        impLinksLabel.setVisibility(View.GONE);
        evolvuUpdateLabel.setVisibility(View.GONE);

        newRV.setVisibility(View.GONE);
        impRV.setVisibility(View.GONE);
        evolvuRV.setVisibility(View.GONE);


        // TODO: Methods to call New Modules
        getSchoolNews();
        getImportantLinks();
        getEvolvuUpdate();
        checkPwd(loginPwd, default_pwd);

    }

    private void openDialog() {

        final Dialog dialog = new Dialog(context); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_demo);
        dialog.setTitle("You are in a previous academic year");
        Button button = dialog.findViewById(R.id.bt_ok);
        dialog.show();
        dialog.setCancelable(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.e("ParentDialog", "Clike");
                closeDialog();
            }

        });


    }

    private void closeDialog() {
        // Log.e("ParentDialog", "ParentDialog>>>" + SharedPrefManager.getInstance(this).getFlag());
        Log.e("ParentDialog", "ParentDialog>>>" + ConstantsFile.flagN);

        final Dialog dialog = new Dialog(context); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_demo);

        dialog.dismiss();
        dialog.cancel();

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("flags", "set 2 called");
        ConstantsFile.flagN = "";
        //SharedPrefManager.getInstance(getApplicationContext()).setFlag("");
        Log.e("flags", "set before  on destroy ncalled" + ConstantsFile.flagN);
        //  Log.e("flags","set before  on destroy ncalled"+SharedPrefManager.getInstance(getApplicationContext()).getFlag());


    }



    @Override
    protected void onResume() {
        super.onResume();
        // getVersion();

    }

    private String check_sms_concern_status() {
        ll_term_conditions.setVisibility(View.GONE);
        check_sms_consent_status_url = newUrl + "check_sms_consent_status".trim();//check_sms_concern_status"

        Log.e("smsconcern", "MainUrl>" + check_sms_consent_status_url);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, check_sms_consent_status_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("smsconcern", "Mainresponce>" + response);
                try {
                    JSONObject object = new JSONObject(response.toString());
                    flag = object.getString("flag");
                    if (flag.equals("0")) {
                        ll_term_conditions.setVisibility(View.VISIBLE);
                        View term_conditions = findViewById(R.id.term_conditions);
                        Button bt_Agree = term_conditions.findViewById(R.id.bt_Agree);
                        Button bt_Cancel = term_conditions.findViewById(R.id.bt_Cancel);
                        bt_Agree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                save_sms_concern();

                                /*Intent i = new Intent(ChangePasswordActivity.this, ParentDashboard.class);
                                startActivity(i);
                                finish();*/
                                //Toast.makeText(getApplicationContext(), "Agree", Toast.LENGTH_SHORT).show();

                            }
                        });
                        bt_Cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ll_term_conditions.setVisibility(View.GONE);
                                try {
                                    mStudentDatabaseHelper.clearData();//clearing Sqlite data => 4 July 2020
                                    // clearApplicationData();
                                    SharedPrefManager.getInstance(ParentDashboard.this).logout();
                                    finish();
                                    startActivity(new Intent(ParentDashboard.this, NewLoginPage.class));
                                } catch (Exception e) {
                                    //clearApplicationData();
                                    SharedPrefManager.getInstance(ParentDashboard.this).logout();
                                    finish();
                                    startActivity(new Intent(ParentDashboard.this, NewLoginPage.class));
                                    e.printStackTrace();
                                }

                                new Thread(() -> {
                                    try {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }).start();

                                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {
                        ll_term_conditions.setVisibility(View.GONE);
                        flag = flag;//1,2,3,...n

                        ll_term_conditions.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ll_term_conditions.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("smsconcern", "errror>" + error.getMessage());
                ll_term_conditions.setVisibility(View.GONE);
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

                params.put("parent_id", String.valueOf(SharedPrefManager.getInstance(ParentDashboard.this).getRegId()));////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);
                Log.e("smsconcern", "param>" + params);

                return params;
            }
        };
        requestQueue.add(request);
        return flag;
    }

    private void save_sms_concern() {

        save_sms_consent_url = newUrl + "save_sms_consent".trim();//save_sms_concern

        Log.e("smsconcernstatus", "MainUrl>" + save_sms_consent_url);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, save_sms_consent_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("smsconcernstatus", "Mainresponse>" + response);
                if (response.equals("true")) {
                    check_sms_concern_status();
                    ll_term_conditions.setVisibility(View.VISIBLE);
                } else {
                    ll_term_conditions.setVisibility(View.GONE);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("smsconcernstatus", "Mainerror>" + error.getMessage());
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

                params.put("parent_id", String.valueOf(SharedPrefManager.getInstance(ParentDashboard.this).getRegId()));////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);
                Log.e("smsconcernstatus", "param>" + params);

                return params;
            }
        };

        requestQueue.add(request);
    }


    //TODO:method to check if default and current pwd are same
    private void checkPwd(String loginPwd, String default_pwd) {
        if (loginPwd != null) {
            if (loginPwd.equals(default_pwd)) {
                Intent i = new Intent(ParentDashboard.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        }
    }

    // TODO: passing version to server
    private void sendVersionToServer(final String versionName, final Integer reg_id, final String username) {
        StringRequest stringRequestAppVersion = new StringRequest(Request.Method.POST, newUrl + "add_app_version",
                response -> {
            Log.e("SendVersion","responce?"+response);
            Log.e("SendVersion","url?"+newUrl + "add_app_version");
                    if (response != null && !response.equals("") && !response.equals("null") && response.equals("true")) {
                        System.out.println(response);
                    } else {
                        System.out.println("False");
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.i("", "get_app_version_error: " + error.getMessage());
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
                params.put("user_id", username);
                params.put("app_version", versionName);
                params.put("parent_id", String.valueOf(reg_id));
                Log.e("SendVersion","param?"+params );
                Log.e("","");

                return params;
            }
        };
        RequestHandler.getInstance(ParentDashboard.this).addToRequestQueue(stringRequestAppVersion);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        getVersion();
    }*/

    // TODO: EvolvU Update Api Method
    private void getEvolvuUpdate() {
        StringRequest stringRequestUpdate = new StringRequest(Request.Method.POST, newUrl + "get_evolvu_updates",
                response -> {

                    if (response != null && !response.equals("") && !response.equals("null")) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) {
                                    evolvuUpdateLabel.setVisibility(View.VISIBLE);
                                    evolvuRV.setVisibility(View.VISIBLE);
                                    Log.i("EvolvU_DATA", "Update: " + jsonObject);
                                    ListModel updateModel = new ListModel();
                                    updateModel.setEvolvuSubject(jsonObject.optString("title"));
                                    updateModel.setEvolvuDetails(jsonObject.optString("description"));
                                    updateModel.setEvolvuImgUrl(jsonObject.optString("image_list"));
                                    updateModel.setEvolvuUpdate_Id(jsonObject.optString("update_id"));
                                    mUpdateList.add(updateModel);
                                } else {
                                    evolvuUpdateLabel.setVisibility(View.GONE);
                                    evolvuRV.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mEvolvuRVAdapter.notifyDataSetChanged();
                    } else {
                        evolvuUpdateLabel.setVisibility(View.GONE);
                        evolvuRV.setVisibility(View.GONE);
                    }
                },
                error -> {
                    error.printStackTrace();
                    evolvuUpdateLabel.setVisibility(View.GONE);
                    evolvuRV.setVisibility(View.GONE);
                    Log.i("", "get_update: " + error.getMessage());
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
                return params;
            }
        };
        RequestHandler.getInstance(ParentDashboard.this).addToRequestQueue(stringRequestUpdate);
        mEvolvuRVAdapter.notifyDataSetChanged();
    }

    // TODO: Important Links Api Method
    private void getImportantLinks() {
        StringRequest stringRequestLinks = new StringRequest(Request.Method.POST, newUrl + "get_important_links",
                response -> {
                    if (response != null && !response.equals("") && !response.equals("null")) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) {
                                    impLinksLabel.setVisibility(View.VISIBLE);
                                    impRV.setVisibility(View.VISIBLE);

                                    Log.i("", "IMPORTANTLINKS: " + jsonObject);
                                    ListModel linkModel = new ListModel();
                                    linkModel.setImpLinksSubject(jsonObject.optString("title"));
                                    linkModel.setImpLinksDetails(jsonObject.optString("description"));
                                    linkModel.setImpLinksUrl(jsonObject.optString("url"));
                                    mImportantList.add(linkModel);
                                } else {
                                    impLinksLabel.setVisibility(View.GONE);
                                    impRV.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mImportantRVAdapter.notifyDataSetChanged();
                    } else {
                        impLinksLabel.setVisibility(View.GONE);
                        impRV.setVisibility(View.GONE);
                    }
                },
                error -> {
                    impLinksLabel.setVisibility(View.GONE);
                    impRV.setVisibility(View.GONE);
                    error.printStackTrace();
                    Log.i("", "get_links: " + error.getMessage());
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
                params.put("type_link", "private");
                return params;
            }
        };
        RequestHandler.getInstance(ParentDashboard.this).addToRequestQueue(stringRequestLinks);
        mImportantRVAdapter.notifyDataSetChanged();

    }

    // TODO: School News  Api Method
    private void getSchoolNews() {
        StringRequest stringRequestNews = new StringRequest(Request.Method.POST, newUrl + "get_news",
                response -> {
                    if (response != null && !response.equals("") && !response.equals("null")) {
                        try {
                            JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) {
                                    newsLabel.setVisibility(View.VISIBLE);
                                    newRV.setVisibility(View.VISIBLE);

                                    Log.i("NEWS_DATA", "NEWS: " + jsonObject);
                                    ListModel newsModel = new ListModel();
                                    newsModel.setNews_title(jsonObject.optString("title"));
                                    newsModel.setNews_description(jsonObject.optString("description"));
                                    newsModel.setNews_date(jsonObject.optString("date_posted"));
                                    newsModel.setNews_url(jsonObject.optString("url"));
                                    newsModel.setNews_id(jsonObject.optString("news_id"));
                                    newsModel.setNews_image_name(jsonObject.optString("image_name"));

                                    mNewsList.add(newsModel);
                                } else {
                                    newRV.setVisibility(View.GONE);
                                    newsLabel.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mNewRVAdapter.notifyDataSetChanged();
                    } else {
                        newRV.setVisibility(View.GONE);
                        newsLabel.setVisibility(View.GONE);
                    }
                },
                error -> {
                    error.printStackTrace();
                    newRV.setVisibility(View.GONE);
                    newsLabel.setVisibility(View.GONE);
                    Log.i("", "get_news: " + error.getMessage());
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
                return params;
            }
        };
        RequestHandler.getInstance(ParentDashboard.this).addToRequestQueue(stringRequestNews);
        mNewRVAdapter.notifyDataSetChanged();

    }

    // TODO: App Version  Api Method
    private void getVersion() {
        //live url only
        String url1 = Config.NEW_LOGIN + "lastest_version";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                response -> {

                    try {
                        Log.e("lastest_version", "lastest_version>>?" + response);
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        androidversion = jsonObject.getString("latest_version");
                        String release_notes = jsonObject.getString("release_notes");
                        forced_update = jsonObject.getString("forced_update");
                        if (androidversion != null) {
                            float androidversion_num = Float.parseFloat(androidversion);
                            float localandroidversion = Float.parseFloat(Config.LOCAL_ANDROID_VERSION);

                            if (localandroidversion < androidversion_num) {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(ParentDashboard.this, R.style.MaterialDrawerBaseTheme_AlertDialog);
                                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                dialog.setMessage(release_notes);
//                                dialog.setMessage(Config.LOCAL_ANDROID_VERSION_MESSAGE);//todo remove static values
                                dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=in.aceventura.evolvuschool"))));
                                dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
                                AlertDialog alert = dialog.create();
                                alert.show();

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.d("Error Response", error.toString()));

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    // TODO: 23-06-2020
    //update the academic_yr with the server side academic_yr
    private void getAcademicYear() {
        Log.e("LIFECYCLE", "ONACADAMIT" + ConstantsFile.flagN);
        //if (SharedPrefManager.getInstance(getApplicationContext()).getFlag().equals("S")) {
        if (ConstantsFile.flagN.equals("S")) {

            SharedPrefManager.getInstance(getApplicationContext())
                    .setAcademicYear(
                            SharedPrefManager.getInstance(getApplicationContext()).getChangeAcademicYear()
                    );
            getOnInit1();
            openDialog();
        } else {
            Log.e("LIFECYCLE", "inside else" + ConstantsFile.flagN);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_academic_year",
                    response -> {
                        try {
                            Log.e("GetAcademicYer", "Values>>??" + response);
                            Log.e("GetAcademicYer", "URL>>??" + newUrl + "get_academic_year");
                            if (response != null) {

                                JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                                JSONObject obj = jsonArray.getJSONObject(0);
                                try {

                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .setAcademicYear(
                                                    obj.getString("academic_yr")
                                            );


                                    closeDialog();
                                } catch (Exception ignored) {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    Throwable::printStackTrace
            ) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    params.put("short_name", name);
                    Log.e("GetAcademicYer", "paramiteriii" + params);

                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ConstantsFile.flagN.equals("")) {

            closeDialog();
        } else {
            openDialog();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

