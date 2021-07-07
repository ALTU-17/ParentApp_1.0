package in.aceventura.evolvuschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.ListModel;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

import static com.prolificinteractive.materialcalendarview.CalendarDay.from;

public class StudentAttendanceActivity extends AppCompatActivity {

    public List<ListModel> mAbsentDatesArrayList = new ArrayList<>();
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl, sid;
    String academic_yr_from, academic_yr_to, stud_id;
    MaterialCalendarView calendar_attendance;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    RecyclerView rv_absent_dates;
    Calendar_AbsentAdapter mCalendar_AbsentAdapter = null;
    int y, m, d, yt, mt, dt;
    private List<CalendarDay> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        setTitle("Attendance Calendar");
        getSupportActionBar().hide();
        calendar_attendance = findViewById(R.id.calendar_attendance);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
            stud_id = bundle.getString("SID");
        }
        rv_absent_dates = findViewById(R.id.rv_absent_dates);
        rv_absent_dates.setLayoutManager(new LinearLayoutManager(this));


        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_studentattendance);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Attendance Calendar");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentAttendanceActivity.this, StudentDashboard.class);
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
            View view = findViewById(R.id.bb_studentattendance);
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
                        Intent intent = new Intent(StudentAttendanceActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentAttendanceActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(StudentAttendanceActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, newUrl + "get_academic_yr_from_to",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.equals("")) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    academic_yr_from = jsonObject.getString("academic_yr_from");
                                    academic_yr_to = jsonObject.getString("academic_yr_to");

                                    /*academic_yr_from*/
                                    String[] dateParts = academic_yr_from.split("-");
                                    String dayFrom = dateParts[0];
                                    String monthFrom = dateParts[1];
                                    String yearFrom = dateParts[2];
                                    y = Integer.parseInt(yearFrom);
                                    m = Integer.parseInt(monthFrom) - 1;
                                    d = Integer.parseInt(dayFrom);


                                    /*academic_yr_to*/
                                    String[] dateParts1 = academic_yr_to.split("-");
                                    String dayFrom1 = dateParts1[0];
                                    String monthFrom1 = dateParts1[1];
                                    String yearFrom1 = dateParts1[2];
                                    yt = Integer.parseInt(yearFrom1);
                                    mt = Integer.parseInt(monthFrom1) - 1;
                                    dt = Integer.parseInt(dayFrom1);
                                }

                                calendar_attendance.state().edit()
                                        .setFirstDayOfWeek(Calendar.MONDAY)

                                        //set month -1 (want April set March)
                                        .setMinimumDate(CalendarDay.from(y, m, d))

                                        //Today's Date...
                                        .setMaximumDate(CalendarDay.from(yt, mt, dt))
                                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                                        .commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, error -> {
            VolleyLog.d("volley", "Error: " + error.getMessage());
            error.printStackTrace();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", name);
                return params;

            }
        };
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(stringRequest2);


        calendar_attendance.setDateSelected(CalendarDay.today(), true);

        String month = String.valueOf(calendar_attendance.getCurrentDate().getMonth() + 1);
        String yr = String.valueOf(calendar_attendance.getCurrentDate().getYear());
        setAttendance(month, yr);

        //call api when the Month/Year changes
        calendar_attendance.setOnMonthChangedListener((widget, date) -> {
            mAbsentDatesArrayList.clear();
            String month1 = String.valueOf(date.getMonth() + 1);
            String yr1 = String.valueOf(date.getYear());
            setAttendance(month1, yr1);
        });
    }

    //setting red dots to particular dates on which student is absent...
    private void setAttendance(final String month, final String yr) {
        mAbsentDatesArrayList.clear();
        // TODO: 17-10-2019 Api for getting the date on which student is absent for showing in  the calendar...
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_absent_dates",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.equals("")) {
                            mAbsentDatesArrayList.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String oldDOB = jsonObject.getString("absent_date");
                                    ListModel listModel = new ListModel();
                                    listModel.setAbsent_dates(oldDOB);
                                    mAbsentDatesArrayList.add(listModel);

                                    Date newdate;
                                    try {
                                        newdate = simpleDateFormat.parse(oldDOB);
                                        CalendarDay day = from(newdate);
                                        events.addAll(Collections.singleton(day));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    EventDecorator eventDecorator = new EventDecorator(Color.RED, events);
                                    calendar_attendance.addDecorator(eventDecorator);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            return;
                        }
                        mCalendar_AbsentAdapter = new Calendar_AbsentAdapter(mAbsentDatesArrayList);
                        rv_absent_dates.setAdapter(mCalendar_AbsentAdapter);
                    }
                }, error -> {
            VolleyLog.d("volley", "Error: " + error.getMessage());
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("year", yr);
                if (month.length() == 1) {
                    params.put("month", "0" + month);
                } else {
                    params.put("month", month);
                }
                params.put("student_id", stud_id);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                Log.e("SA", "param=" + params);

                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        Intent intent = new Intent(StudentAttendanceActivity.this, StudentDashboard.class);
        intent.putExtra("SID", SharedPrefManager.getInstance(this).getStudentId().toString());
        intent.putExtra("CLASSID", SharedPrefManager.getInstance(this).getStudentClass().toString());
        intent.putExtra("SECTIONID", SharedPrefManager.getInstance(this).getStudentSection().toString());
        return true;
    }
}
