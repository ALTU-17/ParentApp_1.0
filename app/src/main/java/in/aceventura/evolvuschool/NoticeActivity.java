package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import in.aceventura.evolvuschool.adapters.NoticeAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration;
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView;
import xyz.sangcomz.stickytimelineview.model.SectionInfo;

public class NoticeActivity extends AppCompatActivity {
    private static final String tag = NoticeActivity.class.getSimpleName();
    private static final String url = "http://androidlearnings.com/school/services/";
    public static String nType;
    //    SearchView mSearchView;
    ArrayList<DataSet> list = new ArrayList<>();
    ProgressBar progressBar;
    EditText editText;
    RelativeLayout nodata;
    Context context;
    DatabaseHelper mDatabaseHelper;
    public String classid, pid, academic_yr, name, sid, sectionid, newUrl, dUrl, Sname;
    private ProgressDialog progressDialog;
    //    private ListView listView;
    private TimeLineRecyclerView listView;
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Notice/SMS");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        nodata = findViewById(R.id.nodata2);
        nodata.setVisibility(View.GONE);
        editText = findViewById(R.id.edittext1);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        listView = findViewById(R.id.notice_listView);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(getSectionCallback(list));


        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    NoticeActivity.this.adapter.getFilter().filter(s.toString());
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {


            }
        });

        Bundle bundle = getIntent().getExtras();
        academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
        if (bundle != null) {
            classid = bundle.getString("CLASSID");
            sid = bundle.getString("SID");
            sectionid = bundle.getString("SECTIONID");
            pid = bundle.getString("PID");
        }

        getNotice();


        //Top Bar
        View tb_main1 = findViewById(R.id.tb_main1);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear()+")");
        school_title.setText(name+" Evolvu Parent App");
        ht_Teachernote.setText("Notice/SMS");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoticeActivity.this, StudentDashboard.class);
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

        //-

        //bottomBar
        try {
            View view = findViewById(R.id.bb_notice);
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
                        Intent intent = new Intent(NoticeActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(NoticeActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(NoticeActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(NoticeActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(NoticeActivity.this, ParentDashboard.class);
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



    public void getNotice() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_notice_with_multiple_attachment",
                response -> {
                    response=new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    progressBar.setVisibility(View.GONE);
                    if (response.equals("")) {
                        nodata.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        editText.setVisibility(View.GONE);
                    } else
                        try {
                            progressBar.setVisibility(View.GONE);
                            nodata.setVisibility(View.GONE);
                            editText.setVisibility(View.VISIBLE);
                            JSONArray result = new JSONArray(response.replace("ï»¿", ""));

                            if (result.length() == 0) {
                                nodata.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                editText.setVisibility(View.GONE);

                            } else {
                                list = new ArrayList<>();
                                for (int i = 0; i < result.length(); i++) {

                                    JSONObject obj = result.getJSONObject(i);
                                    DataSet dataSet = new DataSet();
                                    dataSet.setnClass(obj.getString("classname"));
                                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String inputDateStr = obj.getString("notice_date");
                                    Date date = inputFormat.parse(inputDateStr);
                                    String outputDateStr = outputFormat.format(date);
                                    dataSet.setnDate(outputDateStr);
                                    dataSet.setnSubject(obj.getString("subject"));
                                    dataSet.setnDescription(obj.getString("notice_desc"));
                                    dataSet.setnId(obj.getString("notice_id"));
                                    dataSet.setnType(obj.getString("notice_type"));
                                    dataSet.setnTeacher(obj.getString("teachername"));
                                    dataSet.setTnImagelist(obj.getString("image_list"));
                                    dataSet.setNoticeRead(obj.getString("read_status"));
                                    dataSet.setSection_id(sectionid);
                                    dataSet.setClass_id(classid);
                                    dataSet.setSid(sid);
                                    dataSet.setPid(pid);
                                    list.add(dataSet);
                                }
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }
                    //pendingOrderAdapter.notifyDataSetChanged();
                    adapter = new NoticeAdapter(NoticeActivity.this, R.layout.notice_row, list);
                    listView.setAdapter(adapter);
                },
                error -> {
                    Log.i("error", "onErrorResponse: " + error.toString());
                    progressBar.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_STUDENT_CLASS, classid);
                params.put("parent_id", pid);
                params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);

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
        return true;
    }


    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<DataSet> singerList) {
        return new RecyclerSectionItemDecoration.SectionCallback() {

            @NonNull
            @SuppressLint("ResourceType")
            @Override
            public SectionInfo getSectionHeader(int position) {
                DataSet singer = list.get(position);
                Drawable dot;
                dot = NoticeActivity.this.getResources().getDrawable(R.drawable.ic_timeline_dot);
                return new SectionInfo(singer.getnDate(), "", dot);

            }

            @Override
            public boolean isSection(int position) {
                return !list.get(position).getnDate().equals(list.get(position - 1).getnDate());
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NoticeActivity.this, StudentDashboard.class);
        i.putExtra("CLASSID", classid);
        i.putExtra("SECTIONID", sectionid);
        i.putExtra("SID", sid);
        startActivity(i);
        finish();
    }
}