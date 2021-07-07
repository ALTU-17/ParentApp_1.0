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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.adapters.CertificateTypeClassAdapter;
import in.aceventura.evolvuschool.adapters.CertificateTypeModelClass;
import in.aceventura.evolvuschool.adapters.SendBonafideRequisitionData;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.models.SendBonafideRequisitionDataModel;

public class BonafideCertificateActivityList extends AppCompatActivity {
    RecyclerView rv_send_bonafide_requisition_data;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl;
    SendBonafideRequisitionData msendBonafideRequisitionData;
    ArrayList<SendBonafideRequisitionDataModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonafide_certificate_list);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);
        mDatabaseHelper = new DatabaseHelper(this);
        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        rv_send_bonafide_requisition_data = findViewById(R.id.rv_send_bonafide_requisition_data);
        FloatingActionButton fabremark = findViewById(R.id.fab_createBonaided);
        fabremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BonafideCertificateActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initView();
        initValues();
        get_bonafide_certificate_requsition_data();

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_profile);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Bonafide Certificate List");
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
                        Intent intent = new Intent(BonafideCertificateActivityList.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(BonafideCertificateActivityList.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(BonafideCertificateActivityList.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(BonafideCertificateActivityList.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(BonafideCertificateActivityList.this, ParentDashboard.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BonafideCertificateActivityList.this, ParentDashboard.class);
        startActivity(intent);
    }

    private void get_bonafide_certificate_requsition_data() {
        Log.e("certificate_requsition", "url>>>" + newUrl + "get_bonafide_certificate_requsition_data");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_bonafide_certificate_requsition_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("certificate_requsition", "response>>>" + response);
                try {
                    SendBonafideRequisitionDataModel sendBonafideRequisitionDataModel = null;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        sendBonafideRequisitionDataModel = new SendBonafideRequisitionDataModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        try {
                            if (jsonObject.getString("student_id").equalsIgnoreCase("0")) {

                            } else {
                                sendBonafideRequisitionDataModel.setStudent_id(jsonObject.getString("student_id"));
                                sendBonafideRequisitionDataModel.setFullname(jsonObject.getString("first_name") + " " + jsonObject.getString("mid_name") + " " + jsonObject.getString("last_name"));
                                sendBonafideRequisitionDataModel.setBonafied_req_id(jsonObject.getString("bonafide_req_id"));
                                sendBonafideRequisitionDataModel.setClass_name(jsonObject.getString("class_name")+"-"+jsonObject.getString("section_name"));
                                sendBonafideRequisitionDataModel.setBonafied_formate_id(jsonObject.getString("bonafide_format_id"));
                                sendBonafideRequisitionDataModel.setEmail_id(jsonObject.getString("email_id"));
                                sendBonafideRequisitionDataModel.setIsGenerated(jsonObject.getString("IsGenerated"));
                                sendBonafideRequisitionDataModel.setParent_id(jsonObject.getString("parent_id"));
                                sendBonafideRequisitionDataModel.setAcademic_yr(jsonObject.getString("academic_yr"));
                                sendBonafideRequisitionDataModel.setReq_acd_yr(jsonObject.getString("r_acd_yr"));
                                sendBonafideRequisitionDataModel.setBc_name(jsonObject.getString("bc_name"));
                                sendBonafideRequisitionDataModel.setDate(jsonObject.getString("reg_date"));
                                arrayList.add(sendBonafideRequisitionDataModel);
                            }
                            Log.e("certificate_requsition", "id>>>" + jsonObject.getString("student_id"));
                            Log.e("certificate_requsition", "reg_date>>>" + jsonObject.getString("reg_date"));
                            Log.e("certificate_requsition", "bonafide_req_id>>>" + jsonObject.getString("bonafide_req_id"));

                            msendBonafideRequisitionData = new SendBonafideRequisitionData(BonafideCertificateActivityList.this, arrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rv_send_bonafide_requisition_data.setLayoutManager(mLayoutManager);
                            rv_send_bonafide_requisition_data.setItemAnimator(new DefaultItemAnimator());
                            rv_send_bonafide_requisition_data.setAdapter(msendBonafideRequisitionData);
                        } catch (Exception e) {
                            e.getMessage();
                            Log.e("certificate_requsition", "err>>>" + e.getMessage());
                        }

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


                params1.put("short_name", name);
                params1.put("parent_id", "" + SharedPrefManager.getInstance(getApplicationContext()).getRegId());

                Log.i("CHILD_PARAM", "Valus:" + params1.toString());
                Log.e("certificate_requsition", "params1>>" + params1);

                return params1;

            }


        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void initValues() {


    }

    private void initView() {
        arrayList = new ArrayList<>();


    }

}