package in.aceventura.evolvuschool;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.ListModel;
import in.aceventura.evolvuschool.adapters.ResultAdapter;

public class ExamDetailsActivity extends AppCompatActivity {
    RecyclerView result_records;
    ResultAdapter mResultAdapter;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl;
    private List<ListModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_details);
        setTitle("Result Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        result_records = findViewById(R.id.rv_records);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        mResultAdapter = new ResultAdapter(list);
        result_records.setAdapter(mResultAdapter);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(ExamDetailsActivity.this,
                LinearLayoutManager.VERTICAL, false);
        result_records.setLayoutManager(LayoutManager);

        getResultData();

    }

    public void getResultData() {

        final Integer reg_id = (SharedPrefManager.getInstance(this).getRegId());
        final String academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());

        // TODO: 16-10-2019 change the "get_childs" from below line...
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_childs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        list = new ArrayList<>();
                        if (response != null && !response.equals("")) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ListModel listModel = new ListModel();

                                    //set values from Api...
                                    listModel.setResSubject(jsonObject.optString("res_subject"));
                                    listModel.setResMarkHeading(jsonObject.optString("res_markheading"));
                                    listModel.setResMarkObtained(jsonObject.optString("res_markobtained"));
                                    list.add(listModel);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        mResultAdapter = new ResultAdapter(list);
                        result_records.setAdapter(mResultAdapter);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        Log.i("error", "onErrorResponse: "+error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_REG_ID, reg_id.toString());
                params.put(SharedPrefManager.KEY_ACADEMIC_YEAR, academic_yr);

                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);

                Log.i("REGID", "getParams: " + reg_id.toString());
                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

}
