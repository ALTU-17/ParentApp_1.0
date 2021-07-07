package in.aceventura.evolvuschool;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

public class Forgotpassword extends AppCompatActivity {

    String newUrl, dUrl, name;
    DatabaseHelper mDatabaseHelper;
    Calendar calendar;
    private ProgressDialog progressDialog;
    private RequestQueue resetPwdRequestQueue;
    TextView acd_yr, instituteName, recieveNewPwd;
    EditText userid, mothername, dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgetpw);
        setTitle("Reset Password");
        TextView txt = findViewById(R.id.ftxt2); //footer text
        TextClock textClock = findViewById(R.id.textClock); //date and time
        textClock.setFormat12Hour("EEEE MMM d yyyy hh:mm:ss a");
        textClock.setTimeZone("Asia/Kolkata");
        calendar = Calendar.getInstance();
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        resetPwdRequestQueue = Volley.newRequestQueue(this);


        acd_yr = findViewById(R.id.acd_yr);
        instituteName = findViewById(R.id.instituteName);
        recieveNewPwd = findViewById(R.id.recieveNewPwd);

        userid = findViewById(R.id.et_userId);
        mothername = findViewById(R.id.et_motherName);
        dob = findViewById(R.id.et_DOB);

        Button resetpwd = findViewById(R.id.bt_Reset);
        Button back = findViewById(R.id.bt_LoginPage);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        getSchoolDetails(name);


        final DatePickerDialog.OnDateSetListener startDate1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

            private void updateLabel2() {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                //Current date
                DateFormat.getDateTimeInstance().format(new Date());
                String currentDateTimeString;

                // setting date to dob edittext
                currentDateTimeString = sdf.format(calendar.getTime());
                dob.setText(currentDateTimeString);

            }
        };
        dob.setFocusable(false);
        dob.setOnClickListener(view -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(Forgotpassword.this, startDate1,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            if (Build.VERSION.SDK_INT < 22) {

                datePickerDialog.getDatePicker().setCalendarViewShown(true);
                datePickerDialog.getDatePicker().setSpinnersShown(false);
                datePickerDialog.getDatePicker().getCalendarView().setShowWeekNumber(false);
            }

            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });


        //for opening Company website
        txt.setOnClickListener(v -> {
            Intent myWebLink = new Intent(Intent.ACTION_VIEW);
            myWebLink.setData(Uri.parse("http://aceventura.in"));
            startActivity(myWebLink);
        });


        //send back to newLoginPage
        back.setOnClickListener(v -> {
            clearApplicationData();
            Intent bck = new Intent(Forgotpassword.this, NewLoginPage.class);
            startActivity(bck);
        });


        //send back to Login Activity
        resetpwd.setOnClickListener(v -> {

            if (validate()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Resetting the password...");
                String ResetPwdUrl = newUrl + "reset_password";
                final String dateOfBirth = dob.getText().toString();            //date of birth
                final String userId = userid.getText().toString();             //userId
                final String motherName = mothername.getText().toString();    //motherName


                StringRequest stringRequest = new StringRequest(Request.Method.POST, ResetPwdUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Log.i("PWDRESPONSE", response);
                                try {

                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getString("status").equals("true")) {
                                        progressDialog.dismiss();
                                        String msg = obj.getString("message");
                                        AlertDialog.Builder dialog =
                                                new AlertDialog.Builder(Forgotpassword.this);
                                        dialog.setCancelable(false);
                                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                        dialog.setTitle("Password Reset Successful");
                                        dialog.setMessage(msg);

                                        dialog.setPositiveButton("Ok", (dialog1, id) -> {
                                            clearApplicationData();
                                            Intent i = new Intent(Forgotpassword.this,
                                                    NewLoginPage.class);
                                            startActivity(i);
                                        });

                                        final AlertDialog alert = dialog.create();
                                        alert.show();

                                    } else if (obj.getString("status").equals("false")) {
                                        progressDialog.dismiss();
                                        String msg = obj.getString("message");
                                        Toast.makeText(Forgotpassword.this, msg, Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Forgotpassword.this, "Please check the details", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            progressDialog.dismiss();
                            Toast.makeText(Forgotpassword.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> resetPasswordParams = new HashMap<>();
                        resetPasswordParams.put("short_name", name);
                        resetPasswordParams.put("user_id", userId);
                        resetPasswordParams.put("answer_one", motherName);
                        resetPasswordParams.put("dob", dateOfBirth);
                        resetPasswordParams.put("role_id", "P");
                        return resetPasswordParams;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                resetPwdRequestQueue.add(stringRequest);
            }

        });

        //api calling to get new password on registered emailId
        recieveNewPwd.setOnClickListener(v -> {
            final String userId = userid.getText().toString();//userId
            if (userId.trim().isEmpty()) {
                userid.setError("Enter userId");
                Toast.makeText(Forgotpassword.this, "Enter userId", Toast.LENGTH_SHORT).show();
            }
            else {
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Sending new password..");
                // TODO: 24-03-2020 change with actual url
                String ResetPwdUrl = dUrl + "index.php/LoginApi/receive_new_password";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ResetPwdUrl,
                        response -> {
                            progressDialog.dismiss();
                            Log.i("PWDRESPONSE", response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    progressDialog.dismiss();
                                    String msg = obj.getString("message");
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Forgotpassword.this);
                                    dialog.setCancelable(false);
                                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    dialog.setTitle("Password Sent !!");
                                    dialog.setMessage(msg);
                                    dialog.setPositiveButton("Ok", (dialog1, id) -> {
                                        clearApplicationData();
                                        Intent i = new Intent(Forgotpassword.this, NewLoginPage.class);
                                        startActivity(i);
                                    });

                                    final AlertDialog alert = dialog.create();
                                    alert.show();

                                } else if (obj.getString("status").equals("false")) {
                                    progressDialog.dismiss();
                                    String msg = obj.getString("message");
                                    Toast.makeText(Forgotpassword.this, "" + msg, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(Forgotpassword.this, "Please check the details", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            progressDialog.dismiss();
                            Toast.makeText(Forgotpassword.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> resetPasswordParams = new HashMap<>();
                        resetPasswordParams.put("short_name", name);
                        resetPasswordParams.put("user_id", userId);
                        return resetPasswordParams;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                resetPwdRequestQueue.add(stringRequest);
            }
        });

    }//onCreate

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

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
        assert dir != null;
        return dir.delete();
    }

    public boolean validate() {
        boolean b = true;

        if (userid.getText().toString().trim().equals("")) {
            userid.setError("Enter UserId");
            userid.requestFocus();
            b = false;
        } else if (mothername.getText().toString().trim().equals("")) {
            mothername.setError("Enter Mother's Name");
            mothername.requestFocus();
            b = false;
        } else if (dob.getText().toString().trim().equals("")) {
            dob.setError("Enter Date of birth");
            dob.requestFocus();
            b = false;
        }
        return b;
    }


    private void getSchoolDetails(String name) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_settings_data",
                response -> {
                    progressDialog.dismiss();
                    Log.i("DETAILS_RESPONSE", "onResponse: " + response);
                    if (response != null) {
                        System.out.println("SCHOOL_DETAILS - " + response);
                        try {
                            JSONObject re = new JSONObject(response);
                            if (re.getString("status").equals("true")) {
                                JSONObject dataObj = re.getJSONObject("data");
                                instituteName.setText(dataObj.getString("institute_name"));
                                acd_yr.setText(dataObj.getString("academic_yr"));
                            }
                        } catch (JSONException e) {
                            instituteName.setText("Evolvu Parent Application");
                            acd_yr.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    } else {
                        instituteName.setText("Evolvu Parent Application");
                        acd_yr.setVisibility(View.GONE);
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(Forgotpassword.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", name);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        resetPwdRequestQueue.add(stringRequest);
    }

}


