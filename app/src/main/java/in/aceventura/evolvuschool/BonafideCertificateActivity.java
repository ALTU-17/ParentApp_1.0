package in.aceventura.evolvuschool;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.adapters.CertificateTypeClassAdapter;
import in.aceventura.evolvuschool.adapters.CertificateTypeModelClass;
import in.aceventura.evolvuschool.adapters.OnItemClick;
import in.aceventura.evolvuschool.adapters.StudentNameAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.models.StudentClassModel;

public class BonafideCertificateActivity extends AppCompatActivity implements OnItemClick {

    Spinner sp_AcademicYear;
    String _AcademicYear;
    EditText et_mail;
    String email;
    Button bt_send;
    ArrayList<String> mAcademicYearList;
    boolean BonafideCertificate, feeBonafideCertificate;
    RecyclerView rv_StudentName, rv_CertificateType;

    String name, newUrl, dUrl;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    DatabaseHelper mDatabaseHelper;
    ArrayList<StudentClassModel> studentClassModels;
    ArrayList<StudentClassModel> setstudentClassModels;
    StudentNameAdapter nameAdapter;
    CertificateTypeClassAdapter certificateTypeClassAdapter;

    ArrayList<CertificateTypeModelClass> arrayList;
    ArrayList<CertificateTypeModelClass> setarrayList;
    String bonafifie = "", bnaafideIdarray = "";
    String studnetId = "";
    String studnetIdsend = "";
    String studnetIdsend11 = "";
    String bonafifiesend = "";
    LinearLayout ll_yer;
    String AcadamicYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonafide_certificate);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);
        studentClassModels = new ArrayList<>();
        arrayList = new ArrayList<>();

        mDatabaseHelper = new DatabaseHelper(this);
        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        initView();
        getStudentName();
        getBonafideCertificate();

        initValue();
        getadapter();

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_profile);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Bonafide Certificate");
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
            View view = findViewById(R.id.bb_sprofile);
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
                        Intent intent = new Intent(BonafideCertificateActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(BonafideCertificateActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(BonafideCertificateActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(BonafideCertificateActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(BonafideCertificateActivity.this, ParentDashboard.class);
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

    private void getadapter() {

    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private void getBonafideCertificate() {

        Log.e("BonafideCertificate", "url>>>" + newUrl + "get_bonafide_certificate_format");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_bonafide_certificate_format", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("BonafideCertificate", "response>>>" + response);
                try {
                    CertificateTypeModelClass modelClass = null;
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        modelClass = new CertificateTypeModelClass();
                        JSONObject object = jsonArray.getJSONObject(i);
                        modelClass.setBc_format_id(object.getString("bc_format_id"));
                        modelClass.setName(object.getString("name"));
                        arrayList.add(modelClass);
                    }

                    certificateTypeClassAdapter = new CertificateTypeClassAdapter(BonafideCertificateActivity.this, arrayList, setarrayList, BonafideCertificateActivity.this::onClick);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rv_CertificateType.setLayoutManager(mLayoutManager);
                    rv_CertificateType.setItemAnimator(new DefaultItemAnimator());
                    rv_CertificateType.setAdapter(certificateTypeClassAdapter);

                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<>();

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params1.put("short_name", name);
                Log.i("CHILD_PARAM", "Valus:" + params1.toString());
                Log.e("BonafideCertificate", "params1>>" + params1);

                return params1;

            }


        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ChildList", "Onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ChildList", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ChildList", "onRestart");
    }


    private void getStudentName() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_childs", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ChildList", "response>>>" + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    StudentClassModel model = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        model = new StudentClassModel();
                        model.setName(object.getString("first_name") + " " + object.getString("mid_name") + " " + object.getString("last_name"));//
                        model.setSid(object.getString("student_id"));
                        studentClassModels.add(model);


                        nameAdapter = new StudentNameAdapter(BonafideCertificateActivity.this, studentClassModels, setstudentClassModels);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rv_StudentName.setLayoutManager(mLayoutManager);
                        rv_StudentName.setItemAnimator(new DefaultItemAnimator());
                        rv_StudentName.setAdapter(nameAdapter);
                    }

                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<>();
                params1.put(SharedPrefManager.KEY_REG_ID, "" + SharedPrefManager.getInstance(getApplicationContext()).getRegId());
                params1.put(SharedPrefManager.KEY_ACADEMIC_YEAR, SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());

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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void initView() {
        rv_StudentName = findViewById(R.id.rv_StudentName);
        rv_CertificateType = findViewById(R.id.rv_CertificateType);
        sp_AcademicYear = findViewById(R.id.sp_AcademicYear);
        et_mail = findViewById(R.id.et_mail);
        bt_send = findViewById(R.id.bt_send);
        ll_yer = findViewById(R.id.ll_yer);
        mAcademicYearList = new ArrayList<>();
        mAcademicYearList.add("2020-2021");
        mAcademicYearList.add("2021-2022");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, mAcademicYearList);
        sp_AcademicYear.setAdapter(arrayAdapter);
        setarrayList = new ArrayList<>();
        setstudentClassModels = new ArrayList<>();
    }

    private void initValue() {


        _AcademicYear = SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear();


        sp_AcademicYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _AcademicYear = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                _AcademicYear = SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear();
            }
        });




       /* cb_BonafideCertificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {

                    // checked
                    BonafideCertificate = true;
                } else {
                    // not checked
                    BonafideCertificate = false;
                }
            }
        });
        cb_FeeBonafideCertificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    feeBonafideCertificate = true;
                    // checked
                } else {
                    feeBonafideCertificate = false;
                    // not checked
                }
            }
        });*/

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setarrayList.isEmpty()) {

                } else {
                    bonafifie = "";
                    for (int i = 0; i < setarrayList.size(); i++) {

                        if (setarrayList.get(i).isaBoolean()) {

                            bonafifie = bonafifie + setarrayList.get(i).getBc_format_id() + ",";
                            bnaafideIdarray = "" + setarrayList.get(i);


                        } else {

                        }


                    }

                }
                if (setstudentClassModels.isEmpty()) {


                } else {
                    studnetId = "";
                    for (int i = 0; i < setstudentClassModels.size(); i++) {


                        if (setstudentClassModels.get(i).isSnameFlag()) {

                            studnetId = studnetId + setstudentClassModels.get(i).getSid() + ",";


                        } else {

                        }
                    }

                }
                Log.e("ifashif", "validbefore>" + studnetId);
                if (isValidEmailId(et_mail.getText().toString())) {
                    if (validate() == true) {

                        email = et_mail.getText().toString();


                        if (studnetId.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please Select Student", Toast.LENGTH_SHORT).show();

                        } else if (bonafifie.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please Select Certificate Type", Toast.LENGTH_SHORT).show();

                        } else {
                            setMM();
                            studnetIdsend = studnetId;
                           /* if (studnetId.split(",").length == 1) {
                                studnetIdsend = studnetId.replace(",", "");
                            } else {
                                studnetIdsend = studnetId;
                            }*/
                            if (bonafifie.split(",").length == 1) {
                                bonafifiesend = bonafifie.replace(",", "");
                            } else {
                                bonafifiesend = bonafifie;
                            }

                            Log.e("requisition_data", "frmm" + (studnetIdsend.substring(studnetIdsend.length() - 1)).equals(","));

                            StringBuilder sb = new StringBuilder(studnetIdsend);
                            //todo
                            sb.deleteCharAt(studnetIdsend.length() - 1);
                            studnetIdsend11=String.valueOf(sb);
                            Log.e("requisition_data", "sb?" + sb);
                            Log.e("requisition_data", "studnetIdsend11?" + studnetIdsend11);
                            send_bonafide_requisition_data();

                        }
                    } else {
                        Log.e("ifashif", "elsevaliarray>" + studnetId);
                        validate();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Vaild Email", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void setMM() {
        Log.e("ifashif", "array>" + studnetId.split(",").length);

        Log.e("ifashif", "array>" + bonafifie);
    }

    private boolean validate() {
        boolean value = false;

        if (setstudentClassModels.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Select Student", Toast.LENGTH_SHORT).show();
            value = false;

        }
        if (setarrayList.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Select Certificate Type", Toast.LENGTH_SHORT).show();
            value = false;
        }
        if (setarrayList.isEmpty() || setstudentClassModels.isEmpty()) {
            //  Toast.makeText(getApplicationContext(), "Please Select Certificate Type and Student", Toast.LENGTH_SHORT).show();

            value = false;


        } else {
            value = true;
        }

        return value;

    }


    private void send_bonafide_requisition_data() {

        Log.e("requisition_data", "frmm" + studnetIdsend11);
        Log.e("requisition_data", "frmm" + bonafifiesend);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "send_bonafide_requisition_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("requisition_data", "response>>>" + response);
                try {
                    String msg = "";
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        msg = jsonObject.getString("success_msg");

                    }

                    startActivity(new Intent(getApplicationContext(), BonafideCertificateActivityList.class));
                    finish();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<>();
                params1.put(SharedPrefManager.KEY_REG_ID, "" + SharedPrefManager.getInstance(getApplicationContext()).getRegId());
                params1.put(SharedPrefManager.KEY_ACADEMIC_YEAR, SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params1.put("short_name", name);
                params1.put("student_id", studnetIdsend11);
                params1.put("bonafied_formate_id", bonafifiesend);
                params1.put("email_id", email);
                params1.put("academic_yr", _AcademicYear);
                params1.put("parent_id", "" + SharedPrefManager.getInstance(getApplicationContext()).getRegId());

                Log.e("requisition_data", "params1>>" + params1);

                return params1;

            }

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BonafideCertificateActivity.this, BonafideCertificateActivityList.class);

        startActivity(i);
        finish();
    }

    @Override
    public void onClick(boolean b) {
        Log.e("Values", "CCX?" + b);
        _AcademicYear = SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear();

        if (b) {
            ll_yer.setVisibility(View.VISIBLE);
            sp_AcademicYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    _AcademicYear = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {
            _AcademicYear = SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear();
            ll_yer.setVisibility(View.GONE);
            validate();
        }
    }

}