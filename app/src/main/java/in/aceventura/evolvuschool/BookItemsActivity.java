package in.aceventura.evolvuschool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.BookItemsAdapter;

public class BookItemsActivity extends AppCompatActivity {
    private static final String tag = RemarkActivity.class.getSimpleName();
    private static final String url = "http://androidlearnings.com/school/services/";
    ProgressBar progressBar;
    String book_title, book_author;
    String book_category_id1;
    Integer book_category_id;
    String Sname;
    String newUrl, dUrl;
    DatabaseHelper mDatabaseHelper;
    String name;
    private List<DataSet> list = new ArrayList<DataSet>();
    private ProgressDialog progressDialog;
    private BookItemsAdapter bookitemadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Available Books");
//        Sname=(SharedPrefManager.getInstance(this).getShortname());
//        newUrl=(SharedPrefManager.getInstance(this).getUrl());
//
//        mDatabaseHelper = new DatabaseHelper(this);
//        name = mDatabaseHelper.getName(1);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        //Sname=NewLoginPage.sn;


//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        ListView listView = findViewById(R.id.book_listView);
        bookitemadapter = new BookItemsAdapter(this, list);
        listView.setAdapter(bookitemadapter);


        Bundle bundle = getIntent().getExtras();

        book_category_id1 = bundle.getString("BOOK_CAT_ID");

        if (book_category_id1 != null) {
            book_category_id = Integer.parseInt(book_category_id1);
        }

        book_title = bundle.getString("BOOK_TITLE");

        book_author = bundle.getString("BOOK_AUTHOR");


        if (book_author.equals("") && book_title.equals("")) {

            get_bookByCategory();

        } else if (book_category_id > 79 && book_title.length() > 0 && book_author.length() > 0) {
//                Toast.makeText(this, ""+book_category_id, Toast.LENGTH_SHORT).show();
            get_bookByAll();

        }
//            else if(book_category_id >= 79 && (book_title.length()>0 || book_author.length()>0) ){
//                Toast.makeText(this, ""+book_category_id, Toast.LENGTH_SHORT).show();
        else if (book_category_id > 79 && (book_title.length() > 0 || book_author.length() > 0)) {
            get_bookByAuthorAndTitle();
        }
    }


    public void get_bookByAll() {
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_bookByAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        if (response.replace("ï»¿", "").length() < 1) {
                            Toast.makeText(BookItemsActivity.this, "No Record Available", Toast.LENGTH_LONG).show();
                        } else
                            try {
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject obj = result.getJSONObject(i);

                                    DataSet dataSet = new DataSet();

                                    dataSet.setbTitle(obj.getString("book_title"));
                                    dataSet.setbAuthor(obj.getString("author"));
                                    dataSet.setbCategoryName(obj.getString("category_name"));
                                    list.add(dataSet);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        bookitemadapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", book_category_id.toString());
                params.put("book_title", book_title);
                params.put("author", book_author);
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


    public void get_bookByAuthorAndTitle() {
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_bookByAuthorAndTitle",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        if (response.length() < 1) {
                            Toast.makeText(BookItemsActivity.this, "No Record Available", Toast.LENGTH_LONG).show();
                        } else
                            try {
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject obj = result.getJSONObject(i);
                                    DataSet dataSet = new DataSet();
                                    dataSet.setbTitle(obj.getString("book_title"));
                                    dataSet.setbAuthor(obj.getString("author"));
                                    dataSet.setbCategoryName(obj.getString("category_name"));
                                    list.add(dataSet);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        bookitemadapter.notifyDataSetChanged();
                        //Toast.makeText(RemarkActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(RemarkActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("book_title", book_title);
                params.put("author", book_author);
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


    public void get_bookByCategory() {

//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_bookByCategory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        if (response.replace("ï»¿", "").length() < 1) {
                            Toast.makeText(BookItemsActivity.this, "No Record Available", Toast.LENGTH_LONG).show();
                        } else
                            try {
                                JSONArray result = new JSONArray(response.replace("ï»¿", ""));


                                for (int i = 0; i < result.length(); i++) {

                                    JSONObject obj = result.getJSONObject(i);

                                    DataSet dataSet = new DataSet();

                                    dataSet.setbTitle(obj.getString("book_title"));
                                    dataSet.setbAuthor(obj.getString("author"));
                                    dataSet.setbCategoryName(obj.getString("category_name"));
                                    list.add(dataSet);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        bookitemadapter.notifyDataSetChanged();
                        //Toast.makeText(RemarkActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(RemarkActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Error: Check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", book_category_id.toString());

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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

}
