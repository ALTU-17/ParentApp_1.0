package in.aceventura.evolvuschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.CirtificateAdapter;

public class HealthActivity extends AppCompatActivity {
    static String TAG = "HealthActivity";
    Context mContext;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl, pid;
    String classid, sectionid, Sname, sid;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Health Activity");
        setContentView(R.layout.activity_health);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        getHealthActivity();
        try {
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
                classid = bundle.getString("CLASSID");
                sectionid = bundle.getString("SECTIONID");
                sid = bundle.getString("SID");
                pid = bundle.getString("PID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        url = newUrl + "check_health_activity_data_exist_for_studentid";
        Log.e("HealthActivity", "MainUrl>" + url);
    }

    private void getHealthActivity() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://www.aceventura.in/demo/Test_ParentAppService/check_health_activity_data_exist_for_studentid", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Respo=>" + response.toString());
                try {
                    if (response == null || response == "" || response.equals("")) {


                    } else {

                    }
                } catch (Exception e) {
                    e.getMessage();

                    Log.e(TAG, "error" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error=>" + error);

                Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext, "Internet Connection Problem ", Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Toast.makeText(mContext, "Bad Request.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Toast.makeText(mContext, "Parse Error (because of invalid json or xml).",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof OutOfMemoryError) {
                    Toast.makeText(mContext, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext, "server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                    Toast.makeText(mContext, "Server is not responding.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {
                    Toast.makeText(mContext, "Connection timeout error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "An unknown error occurred.",
                            Toast.LENGTH_SHORT).show();
                }
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

                params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());//name//"SACS"
                params.put("student_id", sid);////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);
                Log.e(TAG, "paramiteriii" + params);

                return params;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HealthActivity.this, StudentDashboard.class);
        i.putExtra("CLASSID", classid);
        i.putExtra("SECTIONID", sectionid);
        i.putExtra("SID", sid);
        startActivity(i);
        finish();
    }
}