package in.aceventura.evolvuschool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.utils.ConstantsFile;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView forgetPassword, editTextUsername, schoolname;
    ImageView imageLOGO, buttonLogin;
    ProgressBar progressBar;
    String Uname;
    String schURL;
    RequestQueue mQueue;
    String Sname;
    String newUrl;
    String dUrl;
    DatabaseHelper mDatabaseHelper;
    String name, token;
    int errorColor;
    SpannableStringBuilder spannableStringBuilder;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Uname = getIntent().getStringExtra("user_id");
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
        schoolname = findViewById(R.id.schoolname);

        //Android Device Id...
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        //New Code for token(Manoj)...
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                System.out.println("TOKEN -" + token);
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("NotificationValue", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                     //   String token = task.getResult();
                        // Log and toast
                        token = task.getResult();


                        Log.e("NotificationValue", "TokanValue>" + token);

                    }
                });


        final int version = Build.VERSION.SDK_INT;
        //Get the defined errorColor from color resource.
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.md_white_1000);
        } else {
            errorColor = getResources().getColor(R.color.md_white_1000);
        }

        String errorString = "Password cannot be empty";  // Your custom error message.
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
        spannableStringBuilder = new SpannableStringBuilder(errorString);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mQueue = Volley.newRequestQueue(this);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);


        Log.e("","");
        System.out.println("URL - "+newUrl);


        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        Toolbar mToolbar = findViewById(R.id.tb_main);
        //-------------------------------------
        if (name != null) {
            schoolname.setText(name + "  EvolvU Smart Parent App");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            return;
        }

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            ConstantsFile.flagN = "";
            startActivity(new Intent(this, ParentDashboard.class));

            return;
        }

        forgetPassword = findViewById(R.id.tv_forget);
        editTextUsername = findViewById(R.id.et_Username);
        editTextPassword = findViewById(R.id.et_Password);
        imageLOGO = findViewById(R.id.imageView2);
        mQueue = Volley.newRequestQueue(this);

        //---------Code to change the logo dynamically based on urls (NEW) -----------------
        String logoUrl;
        if (name.equals("SACS")) {
            logoUrl = dUrl + "uploads/logo.png";
        } else {
            logoUrl = dUrl + "uploads/" + name + "/logo.png";
        }

        Glide.with(this).load(logoUrl)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageLOGO);

        //-----------------------------------------------------------

        editTextUsername.setText(Uname);
        buttonLogin = findViewById(R.id.btnLogin);
        final CheckBox showPasswordCheckBox = findViewById(R.id.cbShowPwd);

        showPasswordCheckBox.setOnClickListener(view -> {
            if (showPasswordCheckBox.isChecked()) {
                editTextPassword.setTransformationMethod(null);
            } else {
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        buttonLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    private void userLogin() {
        final String password = editTextPassword.getText().toString().trim();

        if (password.matches("")) {

            editTextPassword.setError(spannableStringBuilder);

        }
        if (!isValidPassword(password)) {
            editTextPassword.setError(spannableStringBuilder);
        }
        if (isValidPassword(password)) {

            progressBar.setVisibility(View.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_login",
                    response -> {
                        System.out.println("LOGINACT" + response);
                        Log.e("LOGIN_PARAMS", "response: " + response);
                        Log.e("LOGIN_PARAMS", "URL: " + newUrl + "get_login");
                        try {
                            if (response != null) {
                                JSONObject obj = new JSONObject(response.replace("ï»¿", ""));

                                if (!obj.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                                    obj.getString("user_id"),
                                                    obj.getInt("reg_id"),
                                                    obj.getString("name"),
                                                    obj.getString("academic_yr")
                                            );
                                    progressBar.setVisibility(View.GONE);
                                    ConstantsFile.flagN = "";
                                    Intent intent = new Intent(getApplicationContext(), ParentDashboard.class);
                                    intent.putExtra("User_id", Uname);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    System.out.println(response);
                                    Toast.makeText(getApplicationContext(), "Unable to Login", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> System.out.println("ERROR@LoginPage" + error.getMessage())
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", Uname);
                    params.put("password", password);
                    params.put("token", token);
                    params.put("device_id", android_id);

                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    params.put("short_name", name);

                    Log.e("LOGIN_PARAMS", "LOGIN_PARAMS: " + params);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() > 0;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            userLogin();
        } else if (view == forgetPassword) {

            // TODO: 20-11-2019 New Activity for forgot password from mobile itself...

            if (name != null) {

                //going to forgot password activity
                Intent i = new Intent(LoginActivity.this, Forgotpassword.class);
                startActivity(i);

                //old code do not delete
                /*String url = dUrl + "index.php/login/forgot_password";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

            } else {
                String TO = "aceventuraservices@gmail.com";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setDataAndType(Uri.parse(TO), "message/rfc822");
                startActivity(Intent.createChooser(email, "Support Mail :"));
            }
        }
    }

    //old
    /*@Override
    public void onBackPressed() {
        clearApplicationData();
        finish();
    }*/

    //new
    @Override
    public void onBackPressed() {
        clearApplicationData();
        Intent i = new Intent(LoginActivity.this, NewLoginPage.class);
        startActivity(i);
        finish();
    }

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


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        clearApplicationData();//new
        finish();
        startActivity(new Intent(this, NewLoginPage.class));
        onBackPressed();
        return true;
    }
}
