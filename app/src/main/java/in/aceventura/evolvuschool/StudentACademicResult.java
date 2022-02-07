package in.aceventura.evolvuschool;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.adapters.ExamAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.models.Detail_result;
import in.aceventura.evolvuschool.models.Exam_list;
import in.aceventura.evolvuschool.resultwebview.ResultShowReportCardActivity;
import in.aceventura.evolvuschool.resultwebview.ResultWebViewActivity;
import in.aceventura.evolvuschool.utils.AcademicResultPojo;

public class StudentACademicResult extends AppCompatActivity {

    String sid;
    String classid;
    String filename = "";
    String sectionid;
    ProgressBar progressBar;
    Context mContext;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    ArrayList<AcademicResultPojo> resultPojoArrayList;
    String term_id1 = "", term_id2 = "", class_name = "", Exam_Name_Term_Id = "";
    String  student_id, class_id, academic_yr,childName;
    String mCBSC = "";
    String CBSEFLAG = "";
    AcademicResultPojo vpojo;// ll_Blank.setVisibility(View.VISIBLE);
    String Sname;
    String resultUrl="";
    CardView llshowchart;
    DatabaseHelper mDatabaseHelper;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    public String name;
    String newUrl, dUrl;
    ExamAdapter examAdapter;
    ArrayList eName = new ArrayList<String>();
    RelativeLayout nodata;
    DownloadManager dm;
    private List<Exam_list> exam_list;
    LinearLayout ll_Download_Proficiency_Certificate;
    Activity mActivity;
    View tb_main1;
    TextView tv_result;
    LinearLayout ll_result, ll_CBSE, ll_Blank;
    TextView tv_pay_errormsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Result");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_academic_result);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nodata = findViewById(R.id.nodata4);
        nodata.setVisibility(View.GONE);
        mContext = this;
        mActivity = this;
        llshowchart = findViewById(R.id.llshowchart);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ll_Download_Proficiency_Certificate = findViewById(R.id.ll_Download_Proficiency_Certificate);
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
            sid = bundle.getString("SID");
        }
        if (bundle != null) {
            classid = bundle.getString("CLASSID");
        }
        if (bundle != null) {
            sectionid = bundle.getString("SECTIONID");
        }
        tb_main1 = findViewById(R.id.icd_tb_studentacademicresult);
        //  getDownload_Proficiency();
        //Top Bar
        student_id=sid;
        class_id=classid;
        academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
       childName=(SharedPrefManager.getInstance(this).getChildName());
       // mStudentDatabaseHelper= new StudentsDatabaseHelper(this);
       // childName=mStudentDatabaseHelper.g(1);
        Log.e("ChildName", "ChildName:" + childName);

        getResult();
        getReport_Card();
        //   show_icons_parentdashboard_apk();
        ll_result = findViewById(R.id.ll_resultsr);
        ll_CBSE = findViewById(R.id.ll_CBSEsr);
        tv_result = findViewById(R.id.tv_resultsr);
        tv_result.setText("Download Report Card");

        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        tv_pay_errormsg = tb_main1.findViewById(R.id.tv_pay_errormsg);


        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);

        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Result");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentACademicResult.this, StudentDashboard.class);
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
            View view = findViewById(R.id.bb_studentacademicresult);
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
                        Intent intent = new Intent(StudentACademicResult.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentACademicResult.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(StudentACademicResult.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentACademicResult.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {
                        Intent intent = new Intent(StudentACademicResult.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///

        recyclerView = findViewById(R.id.recyclerExamlist);
        //examAdapter = new ExamAdapter(mActivity, class_name,mContext, mCBSC, Exam_Name_Term_Id, term_id1, term_id2, sid, exam_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentACademicResult.this));

        if(class_name.equals("11") || class_name.equals("12")){
            // resultUrl = dUrl + "index.php/HSC/show_report_card" +
            //         "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr;
            resultUrl = dUrl + "index.php/HSC/pdf_download" +
                    "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr + "&short_name=" + name;
            Log.e("classname","classnamedURL?"+resultUrl);
        }else{
            // resultUrl = dUrl + "index.php/assessment/show_report_card" +
            //        "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr;
            resultUrl = dUrl + "index.php/assessment/pdf_download" +
                    "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr + "&short_name=" + name ;

            Log.e("classname","classnamedURL?"+resultUrl);
        }


        tv_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getApplicationContext(), "Downloading the result...", Toast.LENGTH_SHORT).show();

                Uri url = Uri.parse(resultUrl);
                Log.e("classname","Finalvariable?"+resultUrl);
                        //("https://sfs.evolvu.in/test/msfs_test/index.php/Assessment/pdf_download?student_id=8249&class_id=43&acd_yr=2021-2022");
                Set<String> args = url.getQueryParameterNames();
                // String fileName = url.getQueryParameter("student_id") + "_" + url.getQueryParameter("class_id") + "_" + url.getQueryParameter("acd_yr") + ".pdf";
                String folder = "/Download/Evolvuschool/Parent/";
                String StringFile = "/Evolvuschool/Parent/";
                DownloadManager.Request request=new DownloadManager.Request(url);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDescription("Downloading Result...");




                try {
                    request.setDestinationInExternalPublicDir(folder, "RC_"+childName+".pdf");//v 28 allow to create and it deprecated method

                } catch (Exception e) {

                    //v 28+
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + "RC_"+childName+".pdf");//(Environment.DIRECTORY_PICTURES,"parag.jpeg")
                }



                //  request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, folder);
                request.allowScanningByMediaScanner();

                DownloadManager dm=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                //Intent i = new Intent(StudentACademicResult.this, ResultWebViewActivity.class);
                //i.putExtra("student_id", sid);// TODO: 09-02-2020 added
                //i.putExtra("class_id", classid);// TODO: 09-02-2020 added
               // i.putExtra("class_name", class_name);// TODO: 09-02-2020 added
               // startActivity(i);
            }
        });


    }


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
                                if (object.getString("graph").equals("1")) {
                                    llshowchart.setVisibility(View.VISIBLE);
                                    llshowchart.setOnClickListener(v -> {
                                        Intent intent = new Intent(StudentACademicResult.this, ChartActivity.class);
                                        intent.putExtra("CLASSID", classid);
                                        intent.putExtra("SECTIONID", sectionid);
                                        intent.putExtra("SID", sid);
                                        startActivity(intent);
                                    });
                                } else {
                                    llshowchart.setVisibility(View.GONE);
                                    Log.e("Graphite", "?>>>>" + object.getString("graph"));

                                }
                                if (object.getString("cbse_reportcard").equals("1")) {
                                    //ll_CBSE.setVisibility(View.VISIBLE);
                                    getResult();
                                } else {
                                    ll_CBSE.setVisibility(View.GONE);
                                    ll_Blank.setVisibility(View.VISIBLE);

                                }



                            } catch (Exception e) {
                                e.getMessage();
                                ll_CBSE.setVisibility(View.GONE);
                                ll_Blank.setVisibility(View.VISIBLE);
                                Log.e("iconsboard", "receipt_button=>" + e.getMessage());
                                 llshowchart.setVisibility(View.GONE);

                            }


                         //   try {


                           // } catch (Exception e) {
                             //   e.getMessage();
                              //  Log.e("iconsboard", "receipt_button=>" + e.getMessage());
                              //  llshowchart.setVisibility(View.GONE);
                           // }

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


    private void getReport_Card() {
        String url = newUrl + "check_report_card";
        Log.e("check_report_card", "Url>" + url);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("check_report_card", "Responce>" + response);
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String Msg = object.getString("error_msg");
                    // tv_pay_errormsg
                    LinearLayout ll_main_result = findViewById(R.id.ll_main_result);
                    if (object.getString("flag").equals("1")) {
                        ll_result.setVisibility(View.VISIBLE);
                        tv_result.setVisibility(View.VISIBLE);
                        //    llshowchart.setVisibility(View.VISIBLE);
                        ll_main_result.setVisibility(View.VISIBLE);
                        show_icons_parentdashboard_apk();
                    } else {
                        tv_result.setVisibility(View.GONE);
                        ll_result.setVisibility(View.GONE);
                        ll_main_result.setVisibility(View.GONE);

                        if (Msg.equalsIgnoreCase("") || Msg.equals("")) {
                            tv_pay_errormsg.setVisibility(View.GONE);
                        } else {
                            tv_pay_errormsg.setVisibility(View.VISIBLE);
                            tv_pay_errormsg.setText("" + Msg);
                            llshowchart.setVisibility(View.GONE);

                        }


                    }

                } catch (Exception r) {
                    r.printStackTrace();
                    tv_result.setVisibility(View.GONE);
                    ll_result.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_result.setVisibility(View.GONE);
                ll_result.setVisibility(View.GONE);
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
                params.put("student_id", sid);///
                params.put("short_name", name);
                params.put("acd_yr", SharedPrefManager.getInstance(mContext).getAcademicYear());
                Log.e("check_report_card", "paramiteriii" + params);
                return params;
            }

        };
        requestQueue.add(request);
    }


    public void getResult() {
        final String academic_yr = (SharedPrefManager.getInstance(StudentACademicResult.this).getAcademicYear());
        exam_list = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        Log.e("AcadamicResult", "URL>>" + newUrl + "student_marks");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "student_marks",
                response -> {
                    if (response != null) {
                        System.out.println("STUDENTRESULT" + response);
                        nodata.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        try {
                            Log.e("AcadamicResult", "Responce>>" + response);
                            JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                            Log.e("AcadamicResult", "jsonArrayCONVEYR>>" + jsonArray);

                            int i;
                            exam_list = new ArrayList<>(jsonArray.length());
                            String ename = null;
                            ArrayList<Detail_result> detail_results = null;
                            String sub, mh, mo, hm;
                            for (i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ename = jsonObject.getString("Exam_name");
                                    try {
                                        class_name = jsonObject.getString("class_name");

                                        if (ename.equalsIgnoreCase("Final exam") || ename.equalsIgnoreCase("Term 1") || ename.equalsIgnoreCase("Term 2")) {
                                            Log.e("AcadamicResult", "within" + jsonObject.getString("term_id"));

                                            try {
                                                Log.e("AcadamicResult", "within" + jsonObject.getString("term_id"));
                                                term_id1 = jsonObject.getString("term_id");
                                            } catch (Exception e) {
                                                e.getMessage();
                                               // term_id1 = "";
                                                Log.e("CHECKING", "VALUERROR>" + e.getMessage());
                                            }

                                         try {
                                                term_id2 = jsonObject.getString("term_id");

                                            } catch (Exception e) {
                                                Log.e("CHECKING", "VALUERROR>" + e.getMessage());
                                                term_id2 = "";

                                            }

                                            try
                                            {
                                                //todo cbse for 9 n 11 we callet examId other than we take term id
                                                Log.e("Exam_Name_Term_Id", "AcadamicResult" + jsonObject.getString("term_id"));
                                                if (class_name.equals("9") || class_name.equals("11")) {
                                                    mCBSC = "T";
                                                   // Exam_Name_Term_Id = jsonObject.getString("term_id");
                                                    Exam_Name_Term_Id = jsonObject.getString("Exam_id");

                                                    getcheck_cbseformat_report_card();
                                                    if (getcheck_cbseformat_report_card().equals("1")) {

                                                    } else {
                                                        ll_CBSE.setVisibility(View.GONE);
                                                        ll_Blank.setVisibility(View.VISIBLE);


                                                    }
                                                } else {
                                                    mCBSC = "F";
                                                    ll_CBSE.setVisibility(View.GONE);
                                                    ll_Blank.setVisibility(View.VISIBLE);
                                                }

                                            } catch (Exception e) {
                                                e.getMessage();

                                                Log.e("CHECKING", "VALUERROR>" + e.getMessage());

                                            }
                                        } else {
                                            mCBSC = "F";

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("Details");
                                    detail_results = new ArrayList<>();
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = new JSONObject(jsonArray1.get(j).toString());
                                        sub = jsonObject1.getString("Subject");
                                        mh = jsonObject1.getString("Mark_headings");
                                        mo = jsonObject1.getString("Marks_obtained");
                                        hm = jsonObject1.getString("Highest_marks");
                                        detail_results.add(new Detail_result(sub, mh, mo, hm));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("HighestMS", "errr" + e.getMessage());
                                }

                                exam_list.add(new Exam_list(ename, detail_results));
                            }
                            //viwe

                            examAdapter = new ExamAdapter(mActivity,class_name, mContext, mCBSC, Exam_Name_Term_Id, term_id1, term_id2, sid, exam_list);
                            recyclerView.setAdapter(examAdapter);
                            examAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        nodata.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            progressBar.setVisibility(View.GONE);
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
                params.put("student_id", sid);
                params.put("section_id", sectionid);
                params.put("academic_yr", academic_yr);
                Log.e("AcadamicResult", "Paramiter>>" + params);
                return params;
            }
        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(stringRequest);
    }

    private String getcheck_cbseformat_report_card() {

        String url = newUrl + "check_cbseformat_report_card";

        Log.e("cbseformat_report_card", "MainUrl" + url);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("cbseformat_report_card", "response>>" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    CBSEFLAG = object.getString("flag");
                    if (object.getString("flag").equals("1")) {
                        ll_CBSE.setVisibility(View.VISIBLE);
                        //ll_Blank.setVisibility(View.GONE);
                        ll_CBSE.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                                       /* String url = dUrl + "index.php/assessment/show_reportcard_class9_cbseformat?student_id="+sid+"&class_id="+classid+"&login_type=P&acd_yr="+ SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear().trim();
                                                        Log.e("show_reportcard_class9","url>"+url);
*/
                                Intent i = new Intent(StudentACademicResult.this, ResultShowReportCardActivity.class);
                                i.putExtra("student_id", sid);// TODO: 09-02-2020 added
                                i.putExtra("class_id", classid);// TODO: 09-02-2020 added
                                i.putExtra("class_name", class_name);// TODO: 09-02-2020 added
                                startActivity(i);

                            }

                        });
                    } else {
                        ll_CBSE.setVisibility(View.GONE);
                        //ll_Blank.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.getMessage();
                    Log.e("cbseformat_report_card", "rJSON>>" + e.getMessage());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("cbseformat_report_card", "responseERROR>>" + error.getMessage());

                ll_CBSE.setVisibility(View.GONE);
                // ll_Blank.setVisibility(View.VISIBLE);

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
                params.put("student_id", sid);///
                params.put("short_name", name);

                Log.e("cbseformat_report_card", "paramiteriii" + params);
                return params;
            }
        };
        requestQueue.add(request);

        return CBSEFLAG;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.downloadResult) {
//            downloadResult();
            //open a webview here for viewing & downloading result
           // Intent i = new Intent(this, ResultWebViewActivity.class);
            //i.putExtra("student_id", sid);// TODO: 12-10-2020 added
           // i.putExtra("class_id", classid);// TODO: 12-10-2020 added
           // startActivity(i);
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    //old
    private void downloadResult() {
        Toast.makeText(this, "Downloading the result...", Toast.LENGTH_SHORT).show();

        String downloadUrl = "https://www.aceventura.in/demo/SACSv4test/index" + ".php/assessment/pdf_download" +
                "?student_id=9306&class_id=59&acd_yr=2020-2021";

        //opening the url in browser to download it
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
        //startActivity(browserIntent);

        if (isReadStorageAllowed()) {
            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadUrl);
            System.out.println("ResultPdf - " + uri.toString());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String folder = "/Download/Evolvuschool/Parent/";
            String StringFile = "/Evolvuschool/Parent/";
            File directory = new File(folder);
            File directory1 = new File(StringFile);
            request.allowScanningByMediaScanner();

            try {
                request.setDestinationInExternalPublicDir(folder, "resultPdf");//v 28 allow to create and it deprecated method

            } catch (Exception e) {

                //v 28+
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + "resultPdf");//(Environment.DIRECTORY_PICTURES,"parag.jpeg")
            }


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
    }

    //old
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(StudentACademicResult.this, "To Download Attachment Please Allow the Storage " +
                    "Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , STORAGE_PERMISSION_CODE);
    }

    //old
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }
}
