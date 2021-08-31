package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.CirtificateAdapter;
import in.aceventura.evolvuschool.adapters.SubjectClassAdapter;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.utils.Config;

public class StudentProfile extends AppCompatActivity implements View.OnClickListener {
    static String sid;
    TextView t1;
    String classid, sectionid;
    String id;
    String sex;
    EditText et_firstname, et_middlename, et_lastname, et_dob, et_doa, et_sgrnno, et_stuaadhaarno, et_shouse, et_sclass, et_sdivision, et_srollno, et_snationality, et_saddress,
            et_scity, et_sstate, et_spincode, et_scaste, et_scategory, et_semername, et_semeradd,
            et_emercontact, et_birthplace, et_mothertongue, et_sreligion, et_vehicleno,
            et_sallergy, et_sheight, et_sweight, et_studentIdNo;
    Button supdate, sback;
    Spinner sgender, sbloodgroup, shouse, transportSpinner, admission_class;
    CheckBox eAddressChkBox;
    String Sname;
    String newUrl, dUrl;
    DatabaseHelper mDatabaseHelper;
    String name;
    ProgressBar progressBar;
    String religion = "";
    String first_name = "";
    String grnno = "";
    String category = "";
    RecyclerView rv_subject_class11;
    TextView tv_SelectedSubjests;
    LinearLayout ll_class;

    String[] houseValue = {"", "E", "R", "S", "D"};
    String[] houseName = {"Select House", "Emerald", "Ruby", "Sapphire", "Diamond"};
    String[] admissionClass = {"Select Class", "Nursery", "LKG", "UKG", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10"};
    String[] admissionClassesValues = {"", "Nursery", "LKG", "UKG", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10"};
    String[] bloodGroupValue = {"", "AB+", "AB-", "B+", "B-", "A+", "A-", "O+", "O-"};
    String[] bloodGroupName = {"Select Blood Group", "AB+", "AB-", "B+", "B-", "A+", "A-", "O+",
            "O-"};
    String[] genderValue = {"", "M", "F"};
    String[] genderName = {"Select Gender", "Male", "Female"};
    String[] transportModeValue = {"", "Bus", "Van", "Self"};
    String[] transportMode = {"Select Mode", "School Bus", "Private Van", "Self"};
    CircleImageView mIvProfileImage;
    Integer reg_id;
    String hasSpectacles;
    ArrayAdapter<String> admissionClassAdapter;
    private ProgressDialog progressDialog;
    private boolean isfound;
    private RadioButton rb_yes, rb_no;
    // RecyclerView rv_subject_class11;
    TextView tv_stream;
    LinearLayout ll_SelectedSubject, ll_Stream;
    SubjectClassAdapter subjectClassAdapter;
    TextView tv_admission_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*  setTitle("Student Profile");*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);
        //to hide the toolbar;
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reg_id = (SharedPrefManager.getInstance(this).getRegId()); //836
        String fname = (SharedPrefManager.getInstance(this).getStudentName());
        String pid = (SharedPrefManager.getInstance(this).getParentId().toString());

        progressBar = findViewById(R.id.progressBar);
        tv_admission_class = findViewById(R.id.tv_admission_class);
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

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*//Load image from Java file
        private CircleImageView mIvProfileImage;
        mIvProfileImage = findViewById(R.id.ivProfileImage);
        Picasso.with(getApplicationContext()).load("IMAGE URL")//download URL
                .placeholder(R.drawable.ic_user_class)//use default image
                .error(R.drawable.new_evolvu)//if failed
                .into(mIvProfileImage);//imageview
        */

        mIvProfileImage = findViewById(R.id.ivProfileImage);
        tv_stream = findViewById(R.id.tv_stream);
        ll_SelectedSubject = findViewById(R.id.ll_SelectedSubject);
        ll_Stream = findViewById(R.id.ll_Stream);

//        mIvProfileImage.setImageResource(R.drawable.girl);
        /*if (profile_pic.isEmpty()){
            mIvProfileImage.setImageResource(R.drawable.boy);
        }
        */

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
            sid = bundle.getString("SID");
            pid = bundle.getString("PID");
        }
        rv_subject_class11 = findViewById(R.id.rv_subject_class11);
        tv_SelectedSubjests = findViewById(R.id.tv_SelectedSubjests);
        ll_class = findViewById(R.id.ll_class);

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_profile);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Student Profile");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentProfile.this, StudentDashboard.class);
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
                        Intent intent = new Intent(StudentProfile.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentProfile.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(StudentProfile.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(StudentProfile.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(StudentProfile.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        Log.e("valuefrombandel", "CID=" + classid + "=SID=" + sectionid + "=sid=" + sid + "=pid=" + pid);
        Picasso.get()
                .load("http://hdqwalls.com/wallpapers/green-arrow-minimalism-4k-ln.jpg")
                .into(mIvProfileImage);


        t1 = findViewById(R.id.textViewName);
        eAddressChkBox = findViewById(R.id.eAddressCheckbox);
        et_firstname = findViewById(R.id.et_firstname);
        et_firstname.setEnabled(false);
        et_middlename = findViewById(R.id.et_middlename);
        et_middlename.setEnabled(false);
        et_lastname = findViewById(R.id.et_lastname);
        et_lastname.setEnabled(false);
        et_dob = findViewById(R.id.et_dob);
        et_dob.setEnabled(false);
        et_doa = findViewById(R.id.et_doa);
        et_doa.setEnabled(false);
        et_sgrnno = findViewById(R.id.et_grnno);
        et_sgrnno.setEnabled(false);
        et_stuaadhaarno = findViewById(R.id.et_stuaadhaarno);
        et_sclass = findViewById(R.id.et_class);
        et_sclass.setEnabled(false);
        et_sdivision = findViewById(R.id.et_division);
        et_sdivision.setEnabled(false);
        et_srollno = findViewById(R.id.et_rollno);
        et_srollno.setEnabled(false);
        et_snationality = findViewById(R.id.et_nationality);
        et_saddress = findViewById(R.id.et_address);
        et_scity = findViewById(R.id.et_city);
        et_sstate = findViewById(R.id.et_state);
        et_spincode = findViewById(R.id.et_pincode);
        et_scategory = findViewById(R.id.et_category);
        et_scategory.setEnabled(false);
        et_scaste = findViewById(R.id.et_caste);
        et_scaste.setEnabled(false);
        et_emercontact = findViewById(R.id.et_emercontact);
        et_semername = findViewById(R.id.et_emername);
        et_semeradd = findViewById(R.id.et_emeradd);
        et_sreligion = findViewById(R.id.et_religion);
        et_sreligion.setEnabled(false);
        et_vehicleno = findViewById(R.id.et_vehicleno);
        et_sallergy = findViewById(R.id.et_allergy);
        et_sheight = findViewById(R.id.et_height);
        et_sweight = findViewById(R.id.et_weight);

        // TODO: 11-08-2020
        RadioGroup rg_spectacles = findViewById(R.id.rg_spectacles);
        rb_yes = findViewById(R.id.rb_yes);
        rb_no = findViewById(R.id.rb_no);
        et_birthplace = findViewById(R.id.et_birthplace);
        et_mothertongue = findViewById(R.id.et_mothertongue);
        et_studentIdNo = findViewById(R.id.et_studentIdNo);
        et_studentIdNo.setEnabled(false);

        get_selcted_subject_class11();
        rg_spectacles.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton radioButton;
                    radioButton = findViewById(checkedId);
                    hasSpectacles = radioButton.getText().toString();
                }
        );

        transportSpinner = findViewById(R.id.transportSpinner);
        ArrayAdapter<String> transportSpinnerAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, transportMode);
        transportSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportSpinner.setAdapter(transportSpinnerAdapter);

        transportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {   //String option;
                String selectedItem = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        shouse = findViewById(R.id.shouse);

        ArrayAdapter<String> houseAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, houseName);
        houseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shouse.setAdapter(houseAdapter);


        shouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {   //String option;
                String selectedItem = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        admission_class = findViewById(R.id.admission_class);
        admissionClassAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, admissionClass);
        admissionClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admission_class.setAdapter(admissionClassAdapter);

        admission_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {   //String option;
                String selectedItem = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        admission_class.setEnabled(false);
        sbloodgroup = findViewById(R.id.sbloodgroup);
        ArrayAdapter<String> bgroupAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, bloodGroupName);
        bgroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sbloodgroup.setAdapter(bgroupAdapter);

        sbloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {   //String option;
                String selectedItem = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sgender = findViewById(R.id.sgender);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, genderName);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sgender.setAdapter(dataAdapter);
        sgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        supdate = findViewById(R.id.supdate);
        sback = findViewById(R.id.sback);
        eAddressChkBox.setOnCheckedChangeListener((buttonView, isChecked) -> et_semeradd.setText(et_saddress.getText().toString())
        );
        supdate.setOnClickListener(this);
        sback.setOnClickListener(this);

        // Bundle bundle = getIntent().getExtras();
        sid = bundle.getString("SID");
        getStudent();

    }

    private void get_selcted_subject_class11() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "get_selcted_subject_class11", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("subject_class11", "Respo=>" + response.toString());
                try {
                    if (response == null || response == "" || response.equals("")) {

                    } else {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getString("data").equals("1")) {
                            ll_class.setVisibility(View.VISIBLE);
                            tv_stream.setText(jsonObject.getString("stream"));
                            JSONArray jsonArray = jsonObject.getJSONArray("selected_subject");
                            ArrayList<String> mlist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mlist.add(jsonArray.getString(i));
                                Log.e("subject_class11", "ArrayValues" + jsonArray.getString(i));
                            }
                            subjectClassAdapter = new SubjectClassAdapter(StudentProfile.this, mlist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_subject_class11.setLayoutManager(mLayoutManager);
                            rv_subject_class11.setItemAnimator(new DefaultItemAnimator());
                            rv_subject_class11.setAdapter(subjectClassAdapter);
                        } else {
                            tv_stream.setVisibility(View.GONE);
                            tv_SelectedSubjests.setVisibility(View.GONE);
                            ll_class.setVisibility(View.GONE);
                            rv_subject_class11.setVisibility(View.GONE);
                            ll_SelectedSubject.setVisibility(View.GONE);
                            ll_Stream.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("subject_class11", "error=>" + error.getMessage());
                tv_stream.setVisibility(View.GONE);
                tv_SelectedSubjests.setVisibility(View.GONE);
                rv_subject_class11.setVisibility(View.GONE);
                ll_SelectedSubject.setVisibility(View.GONE);
                ll_Stream.setVisibility(View.GONE);
                ll_class.setVisibility(View.GONE);
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

                // params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());//name//"SACS"
                params.put("student_id", sid);////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);
                Log.e("subject_class11", "Respo=>" + params);
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
        String sname = (SharedPrefManager.getInstance(this).getUsername());
        String cid = (SharedPrefManager.getInstance(this).getStudentClass().toString());
        String pid = (SharedPrefManager.getInstance(this).getParentId().toString());
        String fname = (SharedPrefManager.getInstance(this).getStudentName());
        progressBar.setVisibility(View.VISIBLE);
        Log.e("StudentDashboardPro", "url>" + newUrl + "get_student");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_student",
                response -> {
                    Log.e("StudentDashboardPro", "response>" + response);

                    progressBar.setVisibility(View.GONE);
                    showJSON1(response);
                    Log.i("CHILD_RESPONSE", "onResponse: " + response);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error: Check Internet Connection",
                            Toast.LENGTH_LONG).show();
                }) {
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

    public void showJSON1(String response) {

        String middle_name = "";
        String last_name = "";
        String dob = "";
        String doa = "";
        String stuaadhaarno = "";
        int house = 0;
        String sclass = "";
        String division = "";
        String rollno = "";
        int gender = 0;
        int bloodgroup = 0;
        String nationality = "";
        String address = "";
        String city = "";
        String state = "";
        String pincode = "";
        String caste = "";
        String emername = "";
        String emeradd = "";
        String emercontact = "";
        String allergy = "";
        String height = "";
        String weight = "";
        int transport = 0;
        String vehicleno = "";
        String stud_id_no = "", mother_tongue = "", birth_place = "";
        int admissionClassValue;


        try {
            JSONArray result = new JSONArray(response.replace("ï»¿", ""));
            JSONObject sData = result.getJSONObject(0);

            first_name = sData.getString(Config.FIRST_NAME);
            middle_name = sData.getString(Config.MIDDLE_NAME);
            last_name = sData.getString(Config.LAST_NAME);

            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy" +
                    "-MM-dd");
            @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd" +
                    "-MM-yyyy");

            String oldDOB = sData.getString(Config.DOB);
            Date DOBdate = inputFormat.parse(oldDOB);
            String newDOB = outputFormat.format(DOBdate);
            dob = newDOB;

            String oldDOA = sData.getString(Config.DOA);
            if (!oldDOA.equals("null")) {
                Date DOAdate = inputFormat.parse(oldDOA);
                doa = outputFormat.format(DOAdate);
            } else {
                et_doa.setText("");
            }

            grnno = sData.getString(Config.GRNNO);
            stuaadhaarno = sData.getString(Config.SADHAR_NO);
            house = Arrays.asList(houseValue).indexOf(sData.getString(Config.HOUSE));

            sclass = sData.getString("classname");
            division = sData.getString("sectionname");
            rollno = sData.getString(Config.ROLLNO);
            gender = Arrays.asList(genderValue).indexOf(sData.getString(Config.GENDER));
            bloodgroup = Arrays.asList(bloodGroupValue).indexOf(sData.getString(Config.BLOODGROUP));
            nationality = sData.getString(Config.NATIONALITY);
            address = sData.getString(Config.ADDRESS);
            city = sData.getString(Config.CITY);
            state = sData.getString(Config.STATE);
            pincode = sData.getString(Config.PINCODE);
            caste = sData.getString(Config.CASTE);
            category = sData.getString(Config.CATEGORY);
            emername = sData.getString(Config.EMERNAME);
            emeradd = sData.getString(Config.EMERADD);
            emercontact = sData.getString(Config.EMERCONTACT);
            religion = sData.getString(Config.RELIGION);
            allergy = sData.getString(Config.ALLERGY);
            weight = sData.getString(Config.WEIGHT);
            height = sData.getString(Config.HEIGHT);
            // TODO: 11-08-2020
            hasSpectacles = sData.getString("has_specs");
            Log.e("CHECKBOX", "VALUE>>>>" + sData.getString("has_specs"));
//            if (hasSpectacles.equals("null") || hasSpectacles.equals(null)) {
//                hasSpectacles = null;
//            }

            birth_place = sData.getString("birth_place");
            mother_tongue = sData.getString("mother_tongue");
            stud_id_no = sData.getString("stud_id_no");
            admissionClassValue = Arrays.asList(admissionClassesValues).indexOf(sData.getString(
                    "admission_class"));
            admission_class.setSelection(admissionClassValue);
//            admission_class.setSelection(admission_class.getPosition("item2"));

            transport =
                    Arrays.asList(transportModeValue).indexOf(sData.getString(Config.TRANSPORT_MODE));
            vehicleno = sData.getString(Config.VEHICLE_NO);

            if (address.equals(emeradd)) {
                eAddressChkBox.setChecked(true);
            } else {
                eAddressChkBox.setChecked(false);
            }


        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        if (first_name.equals("null")) {
            et_firstname.setText("");
        } else {
            et_firstname.setText(first_name);
        }

        // TODO: 11-08-2020
        if (birth_place.equals("null")) {
            et_birthplace.setText("");
        } else {
            et_birthplace.setText(birth_place);
        }

        if (mother_tongue.equals("null")) {
            et_mothertongue.setText("");
        } else {
            et_mothertongue.setText(mother_tongue);
        }

        if (stud_id_no.equals("null")) {
            et_studentIdNo.setText("");
        } else {
            et_studentIdNo.setText(stud_id_no);
        }
        try {
            switch (hasSpectacles) {
                case "Y":
                    rb_yes.setChecked(true);
                    hasSpectacles = "Y";
                    break;
                case "N":
                    rb_no.setChecked(true);
                    hasSpectacles = "N";
                    break;
                case "null":
                    rb_yes.setChecked(false);
                    rb_no.setChecked(false);
                    break;
                default:
                    rb_yes.setChecked(false);
                    rb_no.setChecked(false);
                    break;
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("ERRRORhasSpectacles", "VALUE==" + e.getMessage());
        }

        /* Method to show Profile Image*/
        getStudentPic(first_name, reg_id);


        if (middle_name.equals("null")) {
            et_middlename.setText("");
        } else {
            et_middlename.setText(middle_name);
        }


        if (last_name.equals("null")) {
            et_lastname.setText("");
        } else {
            et_lastname.setText(last_name);
        }

        if (dob.equals("null")) {
            et_dob.setText("");
        } else {
            et_dob.setText(dob);
        }


        if (doa.equals("null")) {
            et_doa.setText("");
        } else {
            et_doa.setText(doa);
        }


        if (grnno.equals("null")) {
            et_sgrnno.setText("");
        } else {
            et_sgrnno.setText(grnno);
        }


        if (stuaadhaarno != null) {
            String aadhar = stuaadhaarno.replace(" ", "");
            et_stuaadhaarno.setText(aadhar);
        } else {
            et_stuaadhaarno.setText("");
        }

        et_sclass.setText(sclass);
        tv_admission_class.setText(sclass);
        et_sdivision.setText(division);

        if (rollno.equals("null")) {
            et_srollno.setText("");
        } else {
            et_srollno.setText(rollno);
        }
        et_snationality.setText(nationality);
        et_saddress.setText(address);
        et_scity.setText(city);
        et_sstate.setText(state);

        if (pincode.equals("null") || pincode.equals("0")) {
            et_spincode.setText("");
        } else {
            et_spincode.setText(pincode);
        }

        if (caste.equals("null")) {
            et_scaste.setText("");
        } else {
            et_scaste.setText(caste);
        }

        et_scategory.setText(category);

        if (emername.equals("null")) {
            et_semername.setText("");
        } else {
            et_semername.setText(emername);
        }

        if (emeradd.equals("null")) {
            et_semeradd.setText("");

        } else {
            et_semeradd.setText(emeradd);
        }

        if (emercontact.equals("null")) {
            et_emercontact.setText("");
        } else {
            et_emercontact.setText(emercontact);
        }

        if (religion.equals("null")) {
            et_sallergy.setText("");
        } else {
            et_sreligion.setText(religion);
        }

        if (allergy.equals("null")) {
            et_sallergy.setText("");
        } else {
            et_sallergy.setText(allergy);
        }


        if (weight.equals("null") || weight.equals("0.0")) {
            et_sweight.setText("");
        } else {
            et_sweight.setText(weight);
        }

        if (height.equals("null") || height.equals("0.0")) {
            et_sheight.setText("");
        } else {
            et_sheight.setText(height);
        }


        shouse.setSelection(house);
        sbloodgroup.setSelection(bloodgroup);

        sgender.setSelection(gender);
        transportSpinner.setSelection(transport);

        if (vehicleno.equals("null")) {
            et_vehicleno.setText("");
        } else {
            et_vehicleno.setText(vehicleno);
        }

        if (!isFinishing()) {
            showdialog();
        }
    }


    //displaying the profile pic of student
    private void getStudentPic(final String f_name, final Integer reg_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                "get_student_profile_image",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
                        String temp = "1";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            System.out.println(jsonObject);
                            id = jsonObject.getString("student_id");

                            //https://www.evolvu.in/TEST_SFS/uploads/student_image/2751.jpg

                            String path = dUrl + "uploads/student_image/" + id + ".jpg";

                            Picasso.get()
                                    .load(path)
                                    .into(mIvProfileImage);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("first_name", f_name);
                params.put("parent_id", String.valueOf(reg_id));
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }
        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest);
    }


    private void showdialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(StudentProfile.this);
        if (first_name.length() == 0 || grnno.length() == 0 || religion.length() == 0 || category.length() == 0) {
            builder1.setMessage("Please send an email to School Admin to update GRN No, Religion " +
                    "or Category.");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    (dialog, id) -> dialog.cancel());
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void updateStudent() {

        final String studentid = sid;
        final String firstname = et_firstname.getText().toString().trim();
        final String middlename = et_middlename.getText().toString().trim();
        final String lastname = et_lastname.getText().toString().trim();
        final String dob = et_dob.getText().toString().trim();
        final String doa = et_doa.getText().toString().trim();
        final String sgrnno = et_sgrnno.getText().toString().trim();
        final String stuaadhaarno = et_stuaadhaarno.getText().toString().trim();
        final String sclass = et_sclass.getText().toString().trim();
        final String sdivision = et_sdivision.getText().toString().trim();
        final String srollno = et_srollno.getText().toString().trim();
        final String snationality = et_snationality.getText().toString().trim();
        final String saddress = et_saddress.getText().toString().trim();
        final String scity = et_scity.getText().toString().trim();
        final String sstate = et_sstate.getText().toString().trim();
        final String spincode = et_spincode.getText().toString().trim();
        final String scategory = et_scategory.getText().toString().trim();
        final String scaste = et_scaste.getText().toString().trim();
        final String emercontact = et_emercontact.getText().toString().trim();
        final String semername = et_semername.getText().toString().trim();
        final String semeradd = et_semeradd.getText().toString().trim();
        final String sreligion = et_sreligion.getText().toString().trim();
        final String sallergy = et_sallergy.getText().toString().trim();
        final String sheight = et_sheight.getText().toString().trim();
        final String sweight = et_sweight.getText().toString().trim();
        final String svehicleno = et_vehicleno.getText().toString().trim();

        // TODO: 11-08-2020
        final String studentIdNo = et_studentIdNo.getText().toString().trim();
        final String birthPlace = et_birthplace.getText().toString().trim();
        final String motherTongue = et_mothertongue.getText().toString().trim();

        //Admitted In Class
        int classAdmitted = admission_class.getSelectedItemPosition();
        final String admittedInClass = admissionClassesValues[classAdmitted];

        int selectHouse = shouse.getSelectedItemPosition();
        final String selectHouseValue = houseValue[selectHouse];

        int selectBloodGroup = sbloodgroup.getSelectedItemPosition();
        final String selectBloodGroupValue = bloodGroupValue[selectBloodGroup];

        int selectGender = sgender.getSelectedItemPosition();
        final String selectGenderValue = genderValue[selectGender];

        int selectTransportMode = transportSpinner.getSelectedItemPosition();
        final String selectTransportValue = transportModeValue[selectTransportMode];

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                "update_student",
                response -> {
                    progressBar.setVisibility(View.GONE);
                    if (response == null) {
                        Toast.makeText(StudentProfile.this, "Failed to Update",
                                Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(StudentProfile.this, ParentDashboard.class);
                    startActivity(i);
                    Toast.makeText(StudentProfile.this, "Profile Updated Successfully",
                            Toast.LENGTH_LONG).show();
                },
                error -> Toast.makeText(getApplicationContext(), "Error: Check Internet " +
                        "Connection", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_STUDENT_ID, studentid);
                params.put(Config.SADHAR_NO, stuaadhaarno);
                params.put(Config.NATIONALITY, snationality);
                params.put(Config.ADDRESS, saddress);
                params.put(Config.CITY, scity);
                params.put(Config.STATE, sstate);
                params.put(Config.PINCODE, spincode);
                params.put(Config.CATEGORY, scategory);
                params.put(Config.EMERCONTACT, emercontact);
                params.put(Config.EMERNAME, semername);
                params.put(Config.EMERADD, semeradd);
                params.put(Config.RELIGION, sreligion);
                params.put(Config.ALLERGY, sallergy);
                params.put(Config.HEIGHT, sheight);
                params.put(Config.WEIGHT, sweight);
                params.put(Config.HOUSE, selectHouseValue);
                params.put(Config.BLOODGROUP, selectBloodGroupValue);
                params.put(Config.GENDER, selectGenderValue);
                params.put(Config.TRANSPORT_MODE, selectTransportValue);
                params.put(Config.VEHICLE_NO, svehicleno);
                // TODO: 11-08-2020
                params.put("has_specs", hasSpectacles);
                params.put("birth_place", birthPlace.equals("") ? "" : birthPlace);
                params.put("mother_tongue", motherTongue);
                params.put("stud_id_no", studentIdNo);
                params.put("admission_class", ""+tv_admission_class.getText().toString());


                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                Log.i("STUDENTPROFILE", "getParams: " + params);
                Log.e("STUDENTPROFILEUPdate", "getParams: " + params);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.supdate) {
            if (et_stuaadhaarno.getText().toString().length() <= 11 && et_stuaadhaarno.getText().toString().length() >= 1) {
                Toast.makeText(getApplicationContext(), "Invalid Aadhar number",
                        Toast.LENGTH_SHORT).show();
                et_stuaadhaarno.setError("Aadhar No Must be 12 Digit");
                return;

            } else if (et_snationality.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Nationality Field cannot be empty",
                        Toast.LENGTH_SHORT).show();
                et_snationality.setError("Field cannot be Blank");
                return;
            } else if (et_saddress.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Address Field cannot be empty",
                        Toast.LENGTH_SHORT).show();
                et_saddress.setError("Field cannot be Blank");
                return;
            } else if (et_saddress.getText().toString().contains("'")) {
                et_saddress.setError("Address can't contain Apostrophe symbol ' ");
                return;
            } else if (et_scity.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "City Field cannot be empty",
                        Toast.LENGTH_SHORT).show();
                et_scity.setError("Field cannot be Blank");
                return;
            } else if (et_sstate.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "State Field cannot be empty",
                        Toast.LENGTH_SHORT).show();
                et_sstate.setError("Field cannot be Blank");
                return;
            } else if (et_mothertongue.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Mother Tongue Field cannot be empty",
                        Toast.LENGTH_SHORT).show();
                et_mothertongue.setError("Field cannot be Blank");
                return;
            } /*else if (admission_class.getSelectedItem() == "Select Class") {
                Toast.makeText(getApplicationContext(), "Select Admitted Class",
                        Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (et_emercontact.getText().toString().length() < 10) {
                Toast.makeText(getApplicationContext(), "Invalid Contact No", Toast.LENGTH_SHORT).show();
                et_emercontact.setError("Invalid Contact No");

                if (et_emercontact.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Emergency Number Field cannot be " +
                            "empty", Toast.LENGTH_SHORT).show();
                    et_emercontact.setError("Field cannot be Blank");

                }
                return;
            } else {
                updateStudent();
            }
        }
        if (view.getId() == R.id.sback) {
            finish();
        }
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        Intent intent = new Intent(StudentProfile.this, StudentDashboard.class);
        intent.putExtra("SID", sid);
        intent.putExtra("CLASSID", SharedPrefManager.getInstance(this).getStudentClass().toString());
        intent.putExtra("SECTIONID", SharedPrefManager.getInstance(this).getStudentSection().toString());
        return true;
    }*/
}
