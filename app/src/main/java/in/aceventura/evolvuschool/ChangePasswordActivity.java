package in.aceventura.evolvuschool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;


public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edt_motherName, edt_currentPwd, edt_newPwd, edt_renewPwd;
    private String password_new;
    RequestQueue requestQueue;
    Context context;
    private ProgressDialog progressDialog;
    DatabaseHelper mDatabaseHelper;
    String newUrl, name, dUrl, reg_id, user_id;
    String url, url1;
    String flag = "";
    LinearLayout ll_term_conditions;
    StudentsDatabaseHelper mStudentDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ll_term_conditions = findViewById(R.id.ll_term_conditions);
        ll_term_conditions.setVisibility(View.GONE);
        getId();
        getSupportActionBar().hide();


        context = this;
        requestQueue = Volley.newRequestQueue(getBaseContext());
        progressDialog = new ProgressDialog(context);

        mDatabaseHelper = new DatabaseHelper(this);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        check_sms_concern_status();
        // getting reg_id
        Intent intent = getIntent();
        reg_id = intent.getStringExtra("reg_id");//reg_id
        user_id = SharedPrefManager.getInstance(this).getUserId();//user_id


//Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_changepassword);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Change Password");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_sms_concern_status().equals("0")) {
                    try {
                        mStudentDatabaseHelper.clearData();//clearing Sqlite data => 4 July 2020
                        clearApplicationData();
                        SharedPrefManager.getInstance(ChangePasswordActivity.this).logout();
                        finish();
                        startActivity(new Intent(ChangePasswordActivity.this, NewLoginPage.class));
                    } catch (Exception e) {
                        clearApplicationData();
                        SharedPrefManager.getInstance(ChangePasswordActivity.this).logout();
                        finish();
                        startActivity(new Intent(ChangePasswordActivity.this, NewLoginPage.class));
                        e.printStackTrace();
                    }

                    new Thread(() -> {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                } else {
                    Intent i = new Intent(ChangePasswordActivity.this, ParentDashboard.class);
                    startActivity(i);
                    finish();

                }
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
            View view = findViewById(R.id.bb_changepassword);
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
                        Intent intent = new Intent(ChangePasswordActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ChangePasswordActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ChangePasswordActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ChangePasswordActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ChangePasswordActivity.this, ParentDashboard.class);
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

    private String check_sms_concern_status() {
        ll_term_conditions.setVisibility(View.GONE);
        url = newUrl + "check_sms_consent_status".trim();//check_sms_concern_status"

        Log.e("smsconcern", "MainUrl>" + url);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                               /* Intent i = new Intent(ChangePasswordActivity.this, ParentDashboard.class);
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
                                    clearApplicationData();
                                    SharedPrefManager.getInstance(ChangePasswordActivity.this).logout();
                                    finish();
                                    startActivity(new Intent(ChangePasswordActivity.this, NewLoginPage.class));
                                } catch (Exception e) {
                                    clearApplicationData();
                                    SharedPrefManager.getInstance(ChangePasswordActivity.this).logout();
                                    finish();
                                    startActivity(new Intent(ChangePasswordActivity.this, NewLoginPage.class));
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

                params.put("parent_id", String.valueOf(SharedPrefManager.getInstance(ChangePasswordActivity.this).getRegId()));////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);
                Log.e("smsconcern", "param>" + params);

                return params;
            }
        };
        requestQueue.add(request);
        return flag;
    }

    private void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED ");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private void save_sms_concern() {

        url1 = newUrl + "save_sms_consent".trim();//save_sms_concern

        Log.e("smsconcernstatus", "MainUrl>" + url1);
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("smsconcernstatus", "Mainresponse>" + response);
                check_sms_concern_status();
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

                params.put("parent_id", String.valueOf(SharedPrefManager.getInstance(ChangePasswordActivity.this).getRegId()));////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);
                Log.e("smsconcernstatus", "param>" + params);

                return params;
            }
        };

        requestQueue.add(request);
    }


    private void getId() {
        edt_motherName = findViewById(R.id.edt_motherName);
        edt_currentPwd = findViewById(R.id.edt_currentPwd);
        edt_newPwd = findViewById(R.id.edt_newPwd);
        edt_renewPwd = findViewById(R.id.edt_renewPwd);
        Button update = findViewById(R.id.btn_updatePwd);
        update.setOnClickListener(view -> {
            String rePwd = edt_renewPwd.getText().toString();
            String currentPwd = edt_currentPwd.getText().toString();
            String newPwd = edt_newPwd.getText().toString();
            password_new = edt_newPwd.getText().toString();
            if (edt_motherName.getText().toString().equals("")) {
                edt_motherName.setError("Enter Mother's Name");
                edt_motherName.requestFocus();
            } else if (edt_currentPwd.getText().toString().equals("")) {
                edt_currentPwd.setError("Enter Current Password");
                edt_currentPwd.requestFocus();
            } else if (edt_newPwd.getText().toString().equals("")) {
                edt_newPwd.setError("Enter New Password");
                edt_newPwd.requestFocus();
            } else if (edt_renewPwd.getText().toString().equals("")) {
                edt_renewPwd.setError("Enter Re-enter Password");
                edt_renewPwd.requestFocus();
            } else if (edt_newPwd.length() < 8) {
                Toast.makeText(context, "New Password must be between 8 - 20 characters ", Toast.LENGTH_SHORT).show();
                edt_newPwd.requestFocus();
            } else if (edt_newPwd.length() > 20) {
                Toast.makeText(context, "New Password must be between 8 - 20 characters ", Toast.LENGTH_SHORT).show();
                edt_newPwd.requestFocus();
            } else if (currentPwd.equals(newPwd)) {
                Toast.makeText(context, "Old Password & New Password cannot be same.", Toast.LENGTH_SHORT).show();
            } else if (!newPwd.equals(rePwd)) {
                Toast.makeText(context, "New Password & ReEnter Password must be same.", Toast.LENGTH_SHORT).show();
            } else if (!validatePwd(password_new)) {
                edt_newPwd.requestFocus();
                edt_newPwd.setError("Password must contain 8 to 20 characters,number and one special character i.e !@#$%");
            } else {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String motherName = edt_motherName.getText().toString();
        String password_old = edt_currentPwd.getText().toString();
        password_new = edt_newPwd.getText().toString();
        String password_re_enter = edt_renewPwd.getText().toString();
        progressDialog.setMessage("Changing Password...");

        progressDialog.show();
        StringRequest stringRequestLinks = new StringRequest(Request.Method.POST, newUrl + "change_password",
                response -> {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("true")) {
                            progressDialog.dismiss();
                            String success_msg = jsonObject.getString("message");
                            Toast.makeText(ChangePasswordActivity.this, success_msg, Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(ChangePasswordActivity.this, ParentDashboard.class);
                            startActivity(in);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            String error_msg = jsonObject.getString("message");
                            Toast.makeText(ChangePasswordActivity.this, error_msg + "  Try Again...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    error.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("user_id", user_id);
                params.put("answerone", motherName);
                params.put("password_old", password_old);
                params.put("password_new", password_new);
                params.put("password_re", password_re_enter);
                return params;
            }
        };
        RequestHandler.getInstance(ChangePasswordActivity.this).addToRequestQueue(stringRequestLinks);

    }

    public boolean validatePwd(final String pass) {
        String PASSWORD_PATTERN;
        PASSWORD_PATTERN = "((?=.*\\d)(?=.*[@#$%]).{8,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ChangePasswordActivity.this, ParentDashboard.class);
        startActivity(i);
        finish();
       /* try {
            mStudentDatabaseHelper.clearData();//clearing Sqlite data => 4 July 2020
            clearApplicationData();
            SharedPrefManager.getInstance(ChangePasswordActivity.this).logout();
            finish();
            startActivity(new Intent(ChangePasswordActivity.this, NewLoginPage.class));
        } catch (Exception e) {
            clearApplicationData();
            SharedPrefManager.getInstance(ChangePasswordActivity.this).logout();
            finish();
            startActivity(new Intent(ChangePasswordActivity.this, NewLoginPage.class));
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();*/

    }


}
