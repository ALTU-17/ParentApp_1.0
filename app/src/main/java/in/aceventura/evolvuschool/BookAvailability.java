package in.aceventura.evolvuschool;


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Payment.PaymentWebview;
import in.aceventura.evolvuschool.Payment.ReceiptWebview;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class BookAvailability extends AppCompatActivity implements Spinner.OnItemSelectedListener, View.OnClickListener {

    ProgressBar progressBar;
    List<String> item = new ArrayList<>();
    EditText et_book_title, et_book_author;
    Button browse;
    String book_category_id;
    String selectedItem;
    String book_title, book_author;
    String newUrl, Sname, dUrl;
    DatabaseHelper mDatabaseHelper;
    String name;
    //Declaring an Spinner
    private Spinner spinner;
    //An ArrayList for Spinner Items
    private ArrayList<String> bookCategory;
    //JSON Array
    private JSONArray result;
    private ProgressDialog progressDialog;

    //TextViews to display details
//    private TextView textViewName;
//    private TextView textViewCourse;
//    private TextView textViewSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_availability);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Check Book Availability");
        //Initializing the ArrayList
        getSupportActionBar().hide();
        bookCategory = new ArrayList<>();
        et_book_title = findViewById(R.id.et_book_title);
        et_book_author = findViewById(R.id.et_book_author);
        browse = findViewById(R.id.browseBook);
        spinner = findViewById(R.id.sp_bookCategory);
        browse.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
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



        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_bookavailability);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Check Book Availability");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookAvailability.this, ParentDashboard.class);
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
            View view = findViewById(R.id.bb_bookavailability);
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
                        Intent intent = new Intent(BookAvailability.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(BookAvailability.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(BookAvailability.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(BookAvailability.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(BookAvailability.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///

//
//        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
//        FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Book Data...");
        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class itself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        //Initializing TextViews
//        textViewName = (TextView) findViewById(R.id.tv_accessionNo);
//        textViewCourse = (TextView) findViewById(R.id.tv_categoryNo);
//        textViewSession = (TextView) findViewById(R.id.tv_title);

        // bookCategory.add("Select Category");

        //This method will fetch the fetchData from the URL
        getData();
//        spinner.setSelection(0);
    }

    private void getData() {
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);


        //params.put(SharedPrefManager.getInstance(this).getRegId(), reg_id.toString());

        //Creating a string request
        //bookCategory.add("Select Category");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_bookCategory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(BookAvailability.this, ""+response, Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        JSONObject j = null;
                        try {
                            result = new JSONArray(response.replace("ï»¿", ""));
//                             result = new JSONArray(response.replace("<br",""));
                            //                         Toast.makeText(BookAvailability.this, ""+result, Toast.LENGTH_SHORT).show();

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                params.put("short_name", name);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        bookCategory.add(0, "Select Category");
        //Traversing through all the items in the json array
        for (int i = 1; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list

                bookCategory.add(json.getString("category_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<>(BookAvailability.this, android.R.layout.simple_spinner_dropdown_item, bookCategory));
    }

    //Method to get student name of a particular position
    private String getName(int position) {
        String name = "";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString("category_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    //Doing the same with this method as we did with getName()
    private String getCourse(int position) {
        String course = "";
        try {
            JSONObject json = result.getJSONObject(position);
            course = json.getString("category_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }

    //Doing the same with this method as we did with getName()
    private String getSession(int position) {
        String session = "";
        try {
            JSONObject json = result.getJSONObject(position);
            session = json.getString("call_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return session;
    }

    //this method will execute when we pick an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        selectedItem = getName(position);
        // Toast.makeText(BookAvailability.this,selectedItem,Toast.LENGTH_SHORT).show();
//        textViewCourse.setText(getCourse(position));
//        textViewSession.setText(getSession(position));
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        textViewName.setText("");
//        textViewCourse.setText("");
//        textViewSession.setText("");
    }

    @Override
    public void onClick(View v) {
        book_title = et_book_title.getText().toString().trim();
        book_author = et_book_author.getText().toString().trim();

        if (!book_author.equals("null") && !book_title.equals("null")){
            if ((book_author.length() == 0 && book_title.length() == 0) && selectedItem.equals("Select Category")) {
                Toast.makeText(BookAvailability.this, "Error: To search book select at least one field", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(BookAvailability.this, BookItemsActivity.class);
                intent.putExtra("BOOK_CAT_ID", selectedItem);
                intent.putExtra("BOOK_TITLE", book_title);
                intent.putExtra("BOOK_AUTHOR", book_author);
                startActivity(intent);
                spinner.setSelection(0);
                et_book_title.setText("");
                et_book_author.setText("");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}

