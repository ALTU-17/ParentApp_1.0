package in.aceventura.evolvuschool;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.utils.ConstantsFile;

public class ChangeAcademicYearActivity extends AppCompatActivity {
    Spinner sp_AcademicYear;
    Button bt_AcademicYear;
    ArrayList<String> mAcademicYearList;
    String Values;
    String name, newUrl, dUrl;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    DatabaseHelper mDatabaseHelper;
    Context mContext;
    String currentacademic_yr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_academic_year);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        mContext = this;
        initView();
        initValue();
    }

    private void initView() {
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);
        mDatabaseHelper = new DatabaseHelper(this);

        mAcademicYearList = new ArrayList<>();
        sp_AcademicYear = findViewById(R.id.sp_AcademicYear);
        bt_AcademicYear = findViewById(R.id.bt_AcademicYear);
        bt_AcademicYear.setBackgroundColor(Color.GRAY);

        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_changeAcademicYear);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Change Academic Year");
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

        //-

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

            BottomBar bottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

           /* try {
                bottomBar.setActiveTabColor(getResources().getColor(R.color.bottomactivateColor));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    if (tabId == R.id.tab_calendar) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ChangeAcademicYearActivity.this, MyCalendar.class);
                  /*  intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {
//                        // The tab with id R.id.tab_favorites was selected,
//                        // change your content accordingly.
//                        Intent intent = new Intent(ChangeAcademicYearActivity.this, ParentDashboard.class);
//                  /*  intent.putExtra("class_id", class_id);
//                    intent.putExtra("section_id", section_id);*/
//                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        // The tab with id R.id.tab_favorites was selected,
                 /*   // change your content accordingly.
                    Intent intent = new Intent(ParentProfile.this, ParentProfile.class);
                  *//*  intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*//*
                    startActivity(intent);*/
                    }


                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ChangeAcademicYearActivity.this, MyCalendar.class);
                    /*intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ChangeAcademicYearActivity.this, ParentProfile.class);
                    /*intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ChangeAcademicYearActivity.this, ParentDashboard.class);
                /*    intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                }
            });
            mAcademicYearList.add(0, "Select Academic Year");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item, mAcademicYearList);
            sp_AcademicYear.setAdapter(arrayAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        getAcademicYearList();
        getAcademicYear();
    }

    private void getAcademicYear() {

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
                                currentacademic_yr = obj.getString("academic_yr");


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


    private void getAcademicYearList() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        Log.e("ChangeAcademicYear", "url" + dUrl + "get_academic_years_list");

        Log.e("GetAcademicYer", "Respo=url>" + dUrl + "get_academic_years_list");
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "get_academic_years_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("GetAcademicYer", "Respo=GetAcademicYer>" + response.toString());
                Log.e("ChangeAcademicYear", "response" + response);

                try {
                    if (response == null || response == "" || response.equals("")) {


                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.e("GetAcademicYer", "Respo=GetAcademicYer>" + jsonObject.getString("academic_yr"));


                                mAcademicYearList.add(jsonObject.getString("academic_yr"));

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item, mAcademicYearList);
                                sp_AcademicYear.setAdapter(arrayAdapter);
                                sp_AcademicYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (adapterView.getItemAtPosition(i).toString().equals("Select Academic Year")) {
                                            bt_AcademicYear.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                                            bt_AcademicYear.setEnabled(true);

                                        } else {
                                            bt_AcademicYear.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                                            bt_AcademicYear.setEnabled(true);

                                        }


                                        Values = adapterView.getItemAtPosition(i).toString();


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    e.getMessage();

                    Log.e("GetAcademicYer", "Respo=GetAcademicYer>" + response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GetAcademicYer", "Respo=GetAcademicYer>" + error.getStackTrace());
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
                Log.e("ChangeAcademicYear", "paramiteriii" + params);

                return params;
            }
        };

        requestQueue.add(request);
    }

    private void initValue() {
        bt_AcademicYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("GetAcademicYer", "Values>>??" + Values);
                if (Values.equals("Select Academic Year"))
                {
                    Toast.makeText(getApplicationContext(), "Please Select Academic Year", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.e("GetAcademicYer", "Values>>" + Values);
                        Log.e("flags", "set 3 called flag s set");

                        if (Values.equals(currentacademic_yr)) {
                            ConstantsFile.flagN = "";
                        } else {
                            ConstantsFile.flagN = "S";
                            SharedPrefManager.getInstance(mContext)
                                    .setChangeAcademicYear(
                                            Values);
                        }

                        Intent intent = new Intent(ChangeAcademicYearActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    } catch (Exception ignored) {
                        Log.e("BtnValues", "inside button error" + ignored.getMessage());
                    }
                }
            }
        });
    }
}