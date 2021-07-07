package in.aceventura.evolvuschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.adapters.NavigationDrawerAdapter;
import in.aceventura.evolvuschool.models.NavigationDrawerModel;


public class NavigationDrawerActivity extends AppCompatActivity {
    RecyclerView rv_navigation;
    ArrayList<NavigationDrawerModel> navigationDrawerModels;
    Activity activity;
    NavigationDrawerAdapter navigationDrawerAdapter;
    DatabaseHelper mDatabaseHelper;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    String name, dUrl, newUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        activity = this;
        initView();
        initValue();
        show_icons_parentdashboard_apk();

    }

    private void show_icons_parentdashboard_apk() {

        Log.e("iconsboard", "Respo=url>" + newUrl + "show_icons_parentdashboard_apk");

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "show_icons_parentdashboard_apk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("iconsboard", "Respo=>" + response);
                try {

                    JSONObject object = new JSONObject(response);
                    Log.e("iconsboard", "?>>>>" + object);

                    try {
                        if (object.getString("online_fees_payment").equals("1")) {
                            navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.fee_payment), R.drawable.payment));

                        } else {

                        }
                    } catch (Exception e) {
                    }

                    try {
                        if (object.getString("bonafide_certificate").equals("1")) {
                            navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.bonafide_requisition), R.drawable.ic_certificate));


                        } else {


                        }
                    } catch (Exception e) {

                        Log.e("iconsboard", "RespoinsideConditioninside=>" + e.getMessage());
                    }

                    try {
                        if (object.getString("change_academic_year").equals("1")) {
                            navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.change_academic_year), R.drawable.calendar));

                        } else {

                        }
                    } catch (Exception e) {
                        Log.e("iconsboard", "RespoinsideConditioninside=>" + e.getMessage());
                    }


                } catch (Exception e) {
                    e.getMessage();
                    Log.e("iconsboard", "RespoinsideConditionmain=>" + e.getMessage());

                    Log.e("show_academic_result", "erorro=>" + e.getMessage());
                }

                navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.id_card_photo), R.drawable.ic_profile_update));

                navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.share_app), R.drawable.ic_share));
                navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.change_password), R.drawable.change_password));
                navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.about_us), R.drawable.update));
                navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.books), R.drawable.studying));

                navigationDrawerModels.add(new NavigationDrawerModel("", getString(R.string.log_out), R.drawable.logout));

                navigationDrawerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.e("iconsboard", "Respo=GetAcademicYer>" + error.getStackTrace());
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
                ////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);

                params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());

                Log.e("iconsboard", "params=>" + params.toString());

                return params;
            }
        };

        requestQueue.add(request);
    }


    private void initView() {
        rv_navigation = findViewById(R.id.rv_navigation);
        navigationDrawerModels = new ArrayList<>();
        mDatabaseHelper = new DatabaseHelper(this);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        navigationDrawerModels.add(0, new NavigationDrawerModel("", getString(R.string.my_profile), R.drawable.parenting));


        GridLayoutManager manager1 = new GridLayoutManager(activity, 3
                , GridLayoutManager.VERTICAL, false);
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        navigationDrawerAdapter = new NavigationDrawerAdapter(activity, navigationDrawerModels, fragmentManager1);
        rv_navigation.setLayoutManager(manager1);
        rv_navigation.setAdapter(navigationDrawerAdapter);

    }

    private void initValue() {
        // closenav
        findViewById(R.id.closenav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}