package in.aceventura.evolvuschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.utils.Config;


public class ParentProfile extends AppCompatActivity implements View.OnClickListener {
    EditText et_fname, et_foccupation, et_faddress, et_ftelephone, et_fmobile, et_femail;
    EditText et_adharno, et_mname, et_moccupation, et_maddress, et_mtelephone, et_mmobile, et_memailid;
    Button btnUpdate, btnCancel;
    TextView textViewResult;
    RadioButton fRadioButton, mRadioButton;
    String Sname;
    String newUrl, dUrl;
    Integer reg_id;
    String phone;
    String num;
    DatabaseHelper mDatabaseHelper;
    String name;
    ProgressBar progressBar;
    String father_name = "";
    String mother_name = "";
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private int mPositionClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Parent Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        reg_id = (SharedPrefManager.getInstance(this).getRegId());

        et_fname = findViewById(R.id.et_fname);
        et_fname.setEnabled(false);
        et_foccupation = findViewById(R.id.et_foccupation);
        et_faddress = findViewById(R.id.et_faddress);
        et_fmobile = findViewById(R.id.et_fmobile);
        et_ftelephone = findViewById(R.id.et_ftelephone);
        et_femail = findViewById(R.id.et_femail);
        et_adharno = findViewById(R.id.et_adharno);
        et_mname = findViewById(R.id.et_mname);
        et_mname.setEnabled(false);
        et_moccupation = findViewById(R.id.et_moccupation);
        et_maddress = findViewById(R.id.et_maddress);
        et_mtelephone = findViewById(R.id.et_mtelephone);
        et_mmobile = findViewById(R.id.et_mmobile);
        et_memailid = findViewById(R.id.et_memailid);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        et_fmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fRadioButton.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_mmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRadioButton.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_adharno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fRadioButton = findViewById(R.id.fradioButton);
        mRadioButton = findViewById(R.id.mradioButton);
        mRadioButton.setOnClickListener(this);
        fRadioButton.setOnClickListener(this);

        if (fRadioButton.isChecked()) {
            mRadioButton.setChecked(false);
            Toast.makeText(getApplicationContext(), "Father no selected", Toast.LENGTH_SHORT).show();

        }
        if (mRadioButton.isChecked()) {
            fRadioButton.setChecked(false);
            Toast.makeText(getApplicationContext(), "Mother no selected", Toast.LENGTH_SHORT).show();
        }

        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        getParentDetails();
        //todo Bottom Bar

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_profile);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Parent Profile");
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
            bottomBar.setDefaultTabPosition(2);
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
                        Intent intent = new Intent(ParentProfile.this, MyCalendar.class);
                  /*  intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentProfile.this, ParentDashboard.class);
                  /*  intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
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
                        Intent intent = new Intent(ParentProfile.this, MyCalendar.class);
                    /*intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentProfile.this, ParentProfile.class);
                    /*intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {
                        // The tab with id R.id.tab_favorites was selected,
                        // change your content accordingly.
                        Intent intent = new Intent(ParentProfile.this, ParentDashboard.class);
                /*    intent.putExtra("class_id", class_id);
                    intent.putExtra("section_id", section_id);*/
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //getData();
    }

    public void updateParentDetails() {
        final Integer reg_id = (SharedPrefManager.getInstance(this).getRegId());
        final String father_name = et_fname.getText().toString().trim();
        final String father_occupation = et_foccupation.getText().toString().trim();
        final String f_office_add = et_faddress.getText().toString().trim();
        final String f_mobile = et_fmobile.getText().toString().trim();
        final String f_office_tel = et_ftelephone.getText().toString().trim();
        final String f_email = et_femail.getText().toString().trim();
        final String parent_adhar_no = et_adharno.getText().toString().trim();
        final String mother_name = et_mname.getText().toString().trim();
        final String mother_occupation = et_moccupation.getText().toString().trim();
        final String m_office_add = et_maddress.getText().toString().trim();
        final String m_office_tel = et_mtelephone.getText().toString().trim();
        final String m_mobile = et_mmobile.getText().toString().trim();
        final String m_emailid = et_memailid.getText().toString().trim();

//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "update_parent",
                response -> {
                    progressBar.setVisibility(View.GONE);
                    if (response == null) {
                        Toast.makeText(ParentProfile.this, "Failed to Update", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(ParentProfile.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ParentProfile.this, ParentDashboard.class);
                    startActivity(intent);
                    //Toast.makeText(ParentProfile.this,response,Toast.LENGTH_LONG).show();
                },
                error -> {
                    Log.i("error", "onErrorResponse: " + error.toString());

//                        progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(ParentProfile.this, error.toString(), Toast.LENGTH_LONG).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_REG_ID, reg_id.toString());
                //params.put(Config.FATHER_NAME,father_name);
                params.put(Config.FATHER_OCCUPATION, father_occupation);
                params.put(Config.F_OFFICE_ADD, f_office_add);
                params.put(Config.F_MOBILE, f_mobile);
                params.put(Config.F_OFFICE_TEL, f_office_tel);
                params.put(Config.F_EMAIL, f_email);
                params.put(Config.PARENT_ADHAR_NO, parent_adhar_no);
                //params.put(Config.MOTHER_NAME,mother_name);
                params.put(Config.MOTHER_OCCUPATION, mother_occupation);
                params.put(Config.M_OFFICE_ADD, m_office_add);
                params.put(Config.M_OFFICE_TEL, m_office_tel);
                params.put(Config.M_MOBILE, m_mobile);
                params.put(Config.M_EMAILID, m_emailid);
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


        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
    }

    public void getParentDetails() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_parent",
                response -> {
                    progressBar.setVisibility(View.GONE);
                    showJSON(response);
                },
                error -> Log.i("error", "onErrorResponse: " + error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_REG_ID, reg_id.toString());

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

    public void showJSON(String response) {
        String father_occupation = "";
        String f_office_add = "";
        String f_office_tel = "";
        String f_mobile = "";
        String f_email = "";
        String mother_occupation = "";
        String m_office_add = "";
        String m_office_tel = "";
        String m_mobile = "";
        String m_emailid = "";
        String parent_adhar_no = "";
        phone = "";


        //getting data from API
        try {
            JSONArray result = new JSONArray(response.replace("ï»¿", ""));
            JSONObject collegeData = result.getJSONObject(0);
            father_name = collegeData.getString(Config.FATHER_NAME);
            father_occupation = collegeData.getString(Config.FATHER_OCCUPATION);
            f_office_add = collegeData.getString(Config.F_OFFICE_ADD);
            f_mobile = collegeData.getString(Config.F_MOBILE);
            f_office_tel = collegeData.getString(Config.F_OFFICE_TEL);
            f_email = collegeData.getString(Config.F_EMAIL);
            parent_adhar_no = collegeData.getString(Config.PARENT_ADHAR_NO);
            mother_name = collegeData.getString(Config.MOTHER_NAME);
            mother_occupation = collegeData.getString(Config.MOTHER_OCCUPATION);
            m_office_add = collegeData.getString(Config.M_OFFICE_ADD);
            m_office_tel = collegeData.getString(Config.M_OFFICE_TEL);
            m_mobile = collegeData.getString(Config.M_MOBILE);
            m_emailid = collegeData.getString(Config.M_EMAILID);
            phone = collegeData.getString("active_phone_no");

            //save data to db...
            //get data from db...

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isFinishing()) {
            showdialog();
        }

        if (father_name.equals("null")) {
            et_fname.setText("");
        } else {
            et_fname.setText(father_name);
        }

        if (father_occupation.equals("null")) {
            et_foccupation.setText("");
        } else {
            et_foccupation.setText(father_occupation);
        }


        if (f_office_add.equals("null")) {
            et_faddress.setText("");
        } else {
            et_faddress.setText(f_office_add);
        }


        if (f_office_tel.equals("null")) {
            et_ftelephone.setText("");
        } else {
            et_ftelephone.setText(f_office_tel);
        }


        if (f_mobile.equals("null")) {
            et_fmobile.setText("");
        } else {
            et_fmobile.setText(f_mobile);

        }

        if (f_email.equals("null")) {
            et_femail.setText("");
        } else {
            et_femail.setText(f_email);
        }

        if (parent_adhar_no.equals("null")) {
            et_adharno.setText("");
        } else {
            et_adharno.setText(parent_adhar_no);
        }


        if (mother_name.equals("null")) {
            et_mname.setText("");
        } else {
            et_mname.setText(mother_name);
        }

        if (mother_occupation.equals("null")) {
            et_moccupation.setText("");
        } else {
            et_moccupation.setText(mother_occupation);
        }

        if (m_office_add.equals("null")) {
            et_maddress.setText("");
        } else {
            et_maddress.setText(m_office_add);
        }


        if (m_office_tel.equals("null")) {
            et_mtelephone.setText("");
        } else {
            et_mtelephone.setText(m_office_tel);
        }

        if (m_emailid.equals("null")) {
            et_memailid.setText("");
        } else {
            et_memailid.setText(m_emailid);
        }

        if (m_mobile.equals("null")) {
            et_mmobile.setText("");
        } else {
            et_mmobile.setText(m_mobile);
        }

        if (!et_fmobile.getText().toString().equals("") && !et_mmobile.getText().toString().equals("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_active_phone_no",
                    response1 -> {
//                            Toast.makeText(ParentProfile.this, ""+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray result = new JSONArray(response1.replace("ï»¿", ""));
                            JSONObject obj = result.getJSONObject(0);
                            num = obj.getString("active_phone_no");

                            if (et_fmobile.getText().toString().equals(num)) {
                                fRadioButton.setChecked(true);

                            } else if (et_mmobile.getText().toString().equals(num)) {
                                mRadioButton.setChecked(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    Throwable::printStackTrace) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(SharedPrefManager.KEY_REG_ID, reg_id.toString());

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


       /*
        if(et_fmobile.getText().toString().equals(phone)){
            fRadioButton.setChecked(true);

        }else if(et_mmobile.getText().toString().equals(phone)){
            mRadioButton.setChecked(true);
        }
*/
    }

    private void showdialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ParentProfile.this);

        if (father_name.length() == 0 || mother_name.length() == 0) {
            builder1.setMessage("Please send an email to School Admin to update Father or Mother Name.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    (dialog, id) -> dialog.cancel());
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public void setFatherNo() {

        final Integer regid = (SharedPrefManager.getInstance(this).getRegId());
        final String phoneno = et_fmobile.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "update_ContactDetails",
                response -> {
                    Toast.makeText(ParentProfile.this, "Father no. Selected", Toast.LENGTH_SHORT).show();
                    //new
                    fRadioButton.setChecked(true);

                },
                error -> {
                    //Toast.makeText(ParentProfile.this, error.toString(), Toast.LENGTH_LONG).show();
                    Log.i("error", "onErrorResponse: " + error.toString());

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_REG_ID, regid.toString());
                params.put("phone_no", phoneno);

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

    public void setMotherNo() {

        final Integer regid = (SharedPrefManager.getInstance(this).getRegId());
        final String phoneno = et_mmobile.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "update_ContactDetails",
                response -> {
//                        mRadioButton.setChecked(true);
                    Toast.makeText(ParentProfile.this, "Mother no. Selected", Toast.LENGTH_SHORT).show();
                    //new
                    mRadioButton.setChecked(true);
                    //Toast.makeText(StudentProfile.this,response,Toast.LENGTH_LONG).show();

                },
                error -> {
                    //Toast.makeText(ParentProfile.this, error.toString(), Toast.LENGTH_LONG).show();
                    Log.i("error", "onErrorResponse: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_REG_ID, regid.toString());
                params.put("phone_no", phoneno);
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
    public void onClick(View v) {
        if (v == findViewById(R.id.btnUpdate)) {

            if (et_foccupation.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Fill all  Fields", Toast.LENGTH_SHORT).show();
                et_foccupation.setError("Field cannot be Blank");
                return;
            } else if (et_foccupation.getText().toString().contains("'")) {
                et_foccupation.setError("Occupation can't contain Apostrophe symbol ' ");
                return;

            } else if (et_faddress.getText().toString().length() == 0) {
                //Toast.makeText(getApplicationContext(), "Unable to Update: Fill all  Fields", Toast.LENGTH_SHORT).show();
                et_faddress.setError("Field cannot be Blank");
                return;
            } else if (et_faddress.getText().toString().contains("'")) {
                et_faddress.setError("Address can't contain Apostrophe symbol ' ");
                return;

            } else if (et_adharno.getText().toString().length() <= 11 && et_adharno.getText().toString().length() >= 1) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_adharno.setError("Aadhar No Must be 12 Digit");
                return;
            } else if (et_ftelephone.getText().toString().length() < 11 && et_ftelephone.getText().toString().length() >= 1) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_ftelephone.setError("Telephone no must be atleast 11 digit long");
                return;
            } else if (et_fmobile.getText().toString().length() == 0 || et_fmobile.getText().toString().length() < 10) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_fmobile.setError("Invalid Mobile No");
                return;
            } else if (et_femail.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_femail.setError("Email address can't be empty");
                return;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_femail.getText().toString()).matches()) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_femail.setError("Invalid email address");
                return;

            } else if (et_maddress.getText().toString().contains("'")) {
                et_maddress.setError("Address can't contain Apostophe symbol ' ");
                return;
            } else if (et_mtelephone.getText().toString().length() < 11 && et_mtelephone.getText().toString().length() >= 1) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_mtelephone.setError("Telephone no must be atleast 10 digit long");
                return;
            } else if (et_mmobile.getText().toString().length() == 0 || et_mmobile.getText().toString().length() < 10) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_mmobile.setError("Invalid Mobile No");
                return;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_memailid.getText().toString()).matches()) {
                Toast.makeText(getApplicationContext(), "Unable to Update: Invalid Info", Toast.LENGTH_SHORT).show();
                et_memailid.setError("Invalid email address");
                return;

            } else {
                updateParentDetails();
            }
        }
        if (v == findViewById(R.id.btnCancel)) {
            finish();
        }
        if (v == findViewById(R.id.fradioButton)) {
            if (et_fmobile.getText().toString().length() == 0 || et_fmobile.getText().toString().length() < 10) {
                et_fmobile.setError("Selected Mobile no is not Valid Mobile No.");
                // fRadioButton.setChecked(false);
                mRadioButton.setChecked(false);
                return;
            } else
                mRadioButton.setChecked(false);
            setFatherNo();

        }
        if (v == findViewById(R.id.mradioButton)) {
            if (et_mmobile.getText().toString().length() == 0 || et_mmobile.getText().toString().length() < 10) {
                et_mmobile.setError("Selected Mobile no is not Valid Mobile No.");
                fRadioButton.setChecked(false);
                return;
            } else
                fRadioButton.setChecked(false);
            setMotherNo();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ParentProfile.this, ParentDashboard.class);
        startActivity(i);
        finish();
    }
}
