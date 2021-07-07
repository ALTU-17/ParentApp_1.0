package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class NoticeDetails extends AppCompatActivity {
    private static final String TAGERROR = "NoticeDetails";
    public String dwnldurl = "www.evolvu.in/";
    TextView tv_sclass, tv_notice_date,
            tv_notice_subject, tv_notice_description, notice_attachment, tv_notice_attachment_lable, tv_attachment_colon;
    DownloadManager dm;
    String iNoticeId, iNoticeType, iImagelist, itemValue, image_name, newUrl, name, dUrl, CID, SEC_ID, SID, pid;
    DatabaseHelper mDatabaseHelper;
    LinearLayout attachmentLayout, showAttachmentsLayout;
    private int STORAGE_PERMISSION_CODE = 23;
    private List<DataSet> noteven = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Notice/SMS Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        getSupportActionBar().hide();
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            tv_sclass = findViewById(R.id.sclass);
            tv_notice_date = findViewById(R.id.noticeDate);
            tv_notice_subject = findViewById(R.id.noticeSubject);
            tv_notice_description = findViewById(R.id.noticeDescription);
            notice_attachment = findViewById(R.id.notice_attachment);
            tv_notice_attachment_lable = findViewById(R.id.notice_attachment_lable);
            try {
                /*      tv_attachment_colon = findViewById(R.id.tv_attachment_colon);*/

            } catch (Exception e) {

            }
            attachmentLayout = findViewById(R.id.attachmentLayout);
            showAttachmentsLayout = findViewById(R.id.showAttachmentsLayout);

        } catch (Exception e) {
            e.getMessage();
        }


        /*ListView listView = findViewById(R.id.attachment_notice_file);
        NotEvenAdapter notEvenAdapter = new NotEvenAdapter(this, noteven);
        listView.setAdapter(notEvenAdapter);*/

        Bundle bundle = getIntent().getExtras();

        iNoticeType = bundle.getString("NOTICE_TYPE");

        if (iNoticeType != null) {
            if (iNoticeType.equals("Notice")) {
                attachmentLayout.setVisibility(View.VISIBLE);
            } else {
                attachmentLayout.setVisibility(View.GONE);
            }
        }

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_noticedetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Notice/SMS Details");
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

        //-

        //bottomBar
        try {
            View view = findViewById(R.id.bb_noticedetails);
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
                        Intent intent = new Intent(NoticeDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(NoticeDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(NoticeDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(NoticeDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(NoticeDetails.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        String isClass = bundle.getString("NOTICE_CLASS");
        tv_sclass.setText(isClass);

        String iNoticeDate = bundle.getString("NOTICE_DATE");
        tv_notice_date.setText(iNoticeDate);

        String iSubject = bundle.getString("NOTICE_SUBJECT");
        tv_notice_subject.setText(iSubject);

        String iDescription = bundle.getString("NOTICE_DESCRIPTION");
        tv_notice_description.setText(iDescription);

        iNoticeId = bundle.getString("NOTICE_ID");
        CID = bundle.getString("CID");
        SEC_ID = bundle.getString("SECTIONID");
        SID = bundle.getString("SID");
        pid = bundle.getString("PID");


        iImagelist = bundle.getString("IMAGELIST");

        JSONArray image_list = null;
        try {
            image_list = new JSONArray(iImagelist);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert image_list != null;
        for (int i = 0; i < image_list.length(); i++) {
            try {
                JSONObject obj = image_list.getJSONObject(i);
                image_name = obj.getString("image_name");
                String file_size = obj.getString("file_size");


                DataSet notes = new DataSet();
                notes.setnotice_attachment(obj.getString("image_name"));
                notes.setnote_size(file_size);

                noteven.add(notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (image_list.length() > 0) {
            attachmentLayout.setVisibility(View.VISIBLE);
            showAttachments();
        } else {
            attachmentLayout.setVisibility(View.GONE);
        }
    }

    private void showAttachments() {
        if (noteven.size() > 0) {
            for (int l = 0; l < noteven.size(); l++) {
                setViewAttachments(l);
            }
        }
    }

    private void setViewAttachments(final int l) {
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseViewpdf = layoutInflater.inflate(R.layout.image_namelist_layout,
                null);

        final TextView note_file = baseViewpdf.findViewById(R.id.note_file);
        final TextView tv_error = baseViewpdf.findViewById(R.id.tv_error);
        final ImageButton btn_download = baseViewpdf.findViewById(R.id.btn_download);

        String filename = noteven.get(l).getnotice_attachment();
        note_file.setText(filename);

        //Checking file size
        String fileSize = noteven.get(l).getnote_size();
        if (fileSize.equals("0.00") || fileSize.equals("0") || fileSize.equals("0.0")) {
            tv_error.setVisibility(View.VISIBLE);
            btn_download.setVisibility(View.GONE);
        } else {
            tv_error.setVisibility(View.GONE);
            btn_download.setVisibility(View.VISIBLE);

        }

        btn_download.setOnClickListener(v -> {
            try {

                String fName = note_file.getText().toString();


                downloadNotice(fName);


            } catch (Exception e) {
                e.getMessage();

            }

        });

        showAttachmentsLayout.addView(baseViewpdf);
    }

    private void downloadNotice(String itemNoticeValue) {
        try {

            if (isReadStorageAllowed()) {
                String downloadUrl;

                if (name.equals("SACS")) {
                    downloadUrl = dUrl + "uploads/notice/" + iNoticeId + "/" + itemNoticeValue;
                } else {
                    downloadUrl = dUrl + "uploads/" + name + "/notice/" + iNoticeId + "/" + itemNoticeValue;
                }

                dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(downloadUrl);
                System.out.println("NOTICEDOWNLOADURL - " + uri.toString());
                DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setMimeType("application/");
                request.allowScanningByMediaScanner();


                String folder = "/Download/Evolvuschool/Parent/Notice/";
                String StringFile = "/Evolvuschool/Parent/Notice/";
                File directory = new File(folder);
                File directory1 = new File(StringFile);

                try {
                    request.setDestinationInExternalPublicDir(folder, itemNoticeValue);//v 28 allow to create and it deprecated method

                } catch (Exception e) {
                    //v 28+
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + itemNoticeValue);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")
                }
                Toast.makeText(getApplicationContext(), "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/Notice folder or Downloads folder ", Toast.LENGTH_LONG).show();

                if (!directory.exists()) {
                    directory.mkdirs();
                }
                if (!directory1.exists()) {
                    directory1.mkdirs();
                }


                dm.enqueue(request);
                return;

            }
            requestStoragePermission();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = 0;
        try {
            result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            //If permission is granted returning true
            return result == PackageManager.PERMISSION_GRANTED;

        } catch (Exception e) {
            e.getMessage();
        }
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                Toast.makeText(NoticeDetails.this, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            getErrorTrace(TAGERROR, e.getMessage(), SharedPrefManager.getInstance(getApplicationContext()).getUserId());

        }
    }

    private void getErrorTrace(String activityName, String message, String userId) {

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getErrorTrace(TAGERROR, error.getMessage(), "");

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
                params.put("student_id", "");////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", "");

                Log.e("", "paramiteriii" + params);

                return params;
            }
        };
        requestQueue.add(request);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {

            //Checking the request code of our request
            if (requestCode == STORAGE_PERMISSION_CODE) {

                //If permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Displaying a toast
                    // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();

                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NoticeDetails.this, NoticeActivity.class);
        i.putExtra("CLASSID", CID);
        i.putExtra("SECTIONID", SEC_ID);
        i.putExtra("SID", SID);
        i.putExtra("PID", pid);

        startActivity(i);
        finish();
    }
}


