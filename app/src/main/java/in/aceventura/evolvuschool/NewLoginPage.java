package in.aceventura.evolvuschool;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.utils.Config;
import in.aceventura.evolvuschool.utils.ConstantsFile;
import in.aceventura.evolvuschool.utils.FirebaseNotificationUtils;

public class NewLoginPage extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static String sn = "";
    public static String url;
    public static String dUrl;
    public static String email;
    String androidversion, forced_update;

    ProgressBar progressBar;
    String versionName = BuildConfig.VERSION_NAME;
    TextView tvVersion;
    DatabaseHelper mDatabaseHelper;
    List<String> sNames = new ArrayList<>();
    private EditText Uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_login_page);
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
        progressBar.setVisibility(View.GONE);
        tvVersion = findViewById(R.id.tvVersion);
        mDatabaseHelper = new DatabaseHelper(this);
        Uname = findViewById(R.id.et_uid);
        ImageView next = findViewById(R.id.bt_Next);
        try {

            Bundle bundle1 = new Bundle();
            bundle1 = getIntent().getExtras();
            if (bundle1 != null) {
                ;
                Log.e("flags", "values>0" + bundle1.toString());
                String value = bundle1.getString("activity");
                Log.e("flags", "actiity name>>" + value);
                SharedPrefManager.getInstance(getApplicationContext()).setActivityName(value);
                if (value.equals("remark")) {
                    FirebaseNotificationUtils.Remarkremark_id= bundle1.getString("remark_id");
                    FirebaseNotificationUtils.Remarkstud_id= bundle1.getString("stud_id");
                    FirebaseNotificationUtils.Remarksection_id= bundle1.getString("section_id");
                    FirebaseNotificationUtils.Remarkclass_id= bundle1.getString("class_id");
                    FirebaseNotificationUtils.Remarkparent_id= bundle1.getString("parent_id");
                    //{ activity=remark, }
                }
                if (value.equals("homework")) {
                    FirebaseNotificationUtils.HomeWorkhomework_id= bundle1.getString("homework_id");
                    FirebaseNotificationUtils.HomeWorkstud_id= bundle1.getString("stud_id");
                    FirebaseNotificationUtils.HomeWorksection_id= bundle1.getString("section_id");
                    FirebaseNotificationUtils.HomeWorkclass_id= bundle1.getString("class_id");
                    FirebaseNotificationUtils.HomeWorkparent_id= bundle1.getString("parent_id");

                }if(value.equals("note")){
                    FirebaseNotificationUtils.Notenotes_id= bundle1.getString("notes_id");
                    FirebaseNotificationUtils.Notestud_id= bundle1.getString("stud_id");
                    FirebaseNotificationUtils.Notesection_id= bundle1.getString("section_id");
                    FirebaseNotificationUtils.Noteclass_id= bundle1.getString("class_id");
                    FirebaseNotificationUtils.Noteparent_id= bundle1.getString("parent_id");

                }
                if (value.equals("notice")) {
                    //  {stud_id=12511, activity=note,
                    //  section_id=302, class_id=77, notes_id=4620, parent_id=480}
                    FirebaseNotificationUtils.Noticenotice_id = bundle1.getString("notice_id");
                    FirebaseNotificationUtils.Noticestud_id = bundle1.getString("stud_id");
                    FirebaseNotificationUtils.Noticesection_id = bundle1.getString("section_id");
                    FirebaseNotificationUtils.Noticeclass_id = bundle1.getString("class_id");
                    FirebaseNotificationUtils.Noticeparent_id = bundle1.getString("parent_id");

                }



            }


        } catch (Exception e) {
            Log.e("flags", "error><" + e.getMessage());
        }


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            ConstantsFile.flagN = "";
            startActivity(new Intent(this, ParentDashboard.class));
            return;
        }

        //getting Version Name
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText("v " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPrefManager.getInstance(getApplicationContext()).newAppVersion(versionName);


        //Next Button
        next.setOnClickListener(v -> {
            email = Uname.getText().toString().trim();

            if (email.matches("")) {
                Uname.setError("UserId cannot be empty");
            } else {
                Log.e("NewLogin","url?"+Config.NEW_LOGIN + "validate_user");
                progressBar.setVisibility(View.VISIBLE);
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.NEW_LOGIN + "validate_user",
                        response -> {

                            progressBar.setVisibility(View.GONE);
                            try {
                                Log.e("NewLogin","vales?"+response);
                                if (response != null) {
                                    final JSONArray jsonArray = new JSONArray(response);
                                    System.out.println(jsonArray.toString());
                                    int size = jsonArray.length();
                                    Log.i(TAG, "ArraySize:" + size);
                                    if (size > 1) {
                                        String schoolName;
                                        sNames.clear();
                                        sNames.add(0, "Select School");
                                        JSONObject obj;
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            obj = jsonArray.getJSONObject(i);
                                            schoolName = obj.getString("name");
                                            sNames.add(schoolName);
                                        }
                                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewLoginPage.this);
                                        View mView = getLayoutInflater().inflate(R.layout.dialog_school_spinner, null);
                                        mBuilder.setTitle("Select School");

                                        final Spinner mSpinner = mView.findViewById(R.id.spinner);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewLoginPage.this, android.R.layout.simple_spinner_dropdown_item, sNames);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        adapter.notifyDataSetChanged();
                                        mSpinner.setAdapter(adapter);

                                        mBuilder.setPositiveButton("Ok", (dialog, which) -> {
                                            String schoolName1 = mSpinner.getSelectedItem().toString();
                                            if (schoolName1.equals("Select School")) {
                                                return;
                                            } else {
                                                mDatabaseHelper.addDetails(sn, url, dUrl);
                                                String shortname = mDatabaseHelper.getName(1);
                                                if (shortname == null || shortname.equals("")) {
                                                    return;
                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    ConstantsFile.flagN = "";
                                                    Intent i = new Intent(NewLoginPage.this, LoginActivity.class);
                                                    i.putExtra("user_id", email);
                                                    i.putExtra("short_name", sn);
                                                    startActivity(i);
                                                }
                                                dialog.dismiss();
                                            }
                                        });

                                        mBuilder.setNegativeButton("Dismiss", (dialog, which) -> dialog.dismiss());
                                        mBuilder.setView(mView);

                                        AlertDialog dialog = mBuilder.create();
                                        dialog.show();
                                        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                String schnm = mSpinner.getSelectedItem().toString();
                                                int pos = mSpinner.getSelectedItemPosition();

                                                if (pos > 0) {
                                                    JSONObject obj1 = null;
                                                    try {
                                                        JSONArray jsonArray1 = new JSONArray(response);
                                                        obj1 = jsonArray1.getJSONObject(pos - 1);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        if (obj1 != null) {
                                                            sn = obj1.getString("short_name");
                                                            url = obj1.getString("url");
                                                            dUrl = obj1.getString("project_url");
                                                            String default_pwd = obj1.getString("default_password");
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.VISIBLE);
                                        JSONArray jsonArray2 = new JSONArray(response.replace("ï»¿<html>", ""));
                                        JSONObject jo = jsonArray2.getJSONObject(0);
                                        sn = jo.getString("short_name");
                                        url = jo.getString("url");
                                        dUrl = jo.getString("project_url");
                                        String default_pwd = jo.getString("default_password");

                                        SharedPrefManager.getInstance(getApplicationContext())
                                                .setPassword(default_pwd);


                                        mDatabaseHelper.addDetails(sn, url, dUrl);

                                        String shortname = mDatabaseHelper.getName(1);
                                        if (shortname == null || shortname.equals("")) {
                                            return;
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Intent i = new Intent(NewLoginPage.this, LoginActivity.class);
                                            i.putExtra("user_id", email);
                                            i.putExtra("short_name", sn);
                                            startActivity(i);
                                        }
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Uname.setError("Invalid UserId");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                    progressBar.setVisibility(View.GONE);
                    Uname.setError("Invalid UserId");
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", email);
                        Log.e("NewLogin","user?"+params);
                        return params;

                    }
                };

                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
            }
        });

        //getting version from API for Update
        getVersion();
    }

    // TODO: uncomment for forcefully update
    @Override
    protected void onResume() {
        super.onResume();

    }

    //Version Check Method
    private void getVersion() {
        //live url
        String url1 = Config.NEW_LOGIN + "lastest_version";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                response -> {
                    Log.e("VersionCon", "VerSionRes>>" + response);
                    Log.e("lastest_version", "lastest_version>>?" + response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        androidversion = jsonObject.getString("latest_version");
                        String release_notes = jsonObject.getString("release_notes");

                        if (androidversion != null) {
                            float androidversion_num = Float.parseFloat(androidversion);
                            float localandroidversion = Float.parseFloat(Config.LOCAL_ANDROID_VERSION);
                            forced_update = jsonObject.getString("forced_update");
                            if (localandroidversion < androidversion_num) {
                                if (forced_update.equals("N")) {
                                    //for clearing the data
                                    clearApplicationData();
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(NewLoginPage.this, R.style.MaterialDrawerBaseTheme_AlertDialog);
                                    dialog.setCancelable(false);
                                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setMessage(release_notes);
//                                dialog.setMessage(Config.LOCAL_ANDROID_VERSION_MESSAGE); //todo remove static values
                                    dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=in.aceventura.evolvuschool"))));

                                    dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
                                    AlertDialog alert = dialog.create();
                                    alert.show();
                                } else if (forced_update.equals("Y")) {

                                    clearApplicationData();
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(NewLoginPage.this, R.style.MaterialDrawerBaseTheme_AlertDialog);
                                    dialog.setCancelable(false);
                                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    dialog.setTitle(Config.LOCAL_ANDROID_VERSION_DAILOG_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setMessage(release_notes);
//                                dialog.setMessage(Config.LOCAL_ANDROID_VERSION_MESSAGE);//todo remove static values
                                    dialog.setPositiveButton("Update", (dialog1, id) -> startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=in.aceventura.evolvuschool"))));

                                    dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());
                                    AlertDialog alert = dialog.create();
                                    alert.show();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.d("Error Response >->", error.toString()));

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    //Clear application data
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

    //Back Pressed
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}


