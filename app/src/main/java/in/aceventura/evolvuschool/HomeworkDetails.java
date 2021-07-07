package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class HomeworkDetails extends AppCompatActivity implements View.OnClickListener {
    TextView tv_sclass, tv_subject, tv_sdate, tv_edate, tv_description, tv_status, tv_tcomment, et_pcomment;
    Button btn_hupdate;
    String sid;
    String pid;
    String classid;
    String sectionid;
    String pUpdatedComment;
    String Sname;
    String newUrl;
    String dUrl;
    String name;
    String iImagelist;
    String iFile;
    String HomeworkId;
    ProgressBar progressBar;
    DatabaseHelper mDatabaseHelper;
    LinearLayout homeworkDetails, showAttachmentsLayout;
    private List<DataSet> homeworkattachment = new ArrayList<>();
    DownloadManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Homework Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_details);
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
        et_pcomment = findViewById(R.id.pcomment);
        tv_sclass = findViewById(R.id.sclass);
        tv_subject = findViewById(R.id.subject);
        tv_sdate = findViewById(R.id.sdate);
        tv_edate = findViewById(R.id.edate);
        tv_description = findViewById(R.id.description);
        tv_status = findViewById(R.id.status);
        tv_tcomment = findViewById(R.id.tcomment);
        btn_hupdate = findViewById(R.id.hupdate);
        showAttachmentsLayout = findViewById(R.id.showAttachmentsLayout);
        btn_hupdate.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        homeworkDetails = findViewById(R.id.homeworkDetails);

        Bundle bundle = getIntent().getExtras();
        String iSection = null;
        if (bundle != null) {
            iSection = bundle.getString("SECTION");
        }
        String isClass = null;
        if (bundle != null) {
            isClass = bundle.getString("SCLASS");
        }
        tv_sclass.setText(isClass + " " + iSection);

        String iSubject = null;
        if (bundle != null) {
            iSubject = bundle.getString("SUBJECT");
        }
        tv_subject.setText(iSubject);

        String isDate = null;
        if (bundle != null) {
            isDate = bundle.getString("SDATE");
        }
        tv_sdate.setText(isDate);

        String ieDate = null;
        if (bundle != null) {
            ieDate = bundle.getString("EDATE");
        }
        tv_edate.setText(ieDate);

        String iDescription = null;
        if (bundle != null) {
            iDescription = bundle.getString("DESCRIPTION");
            //Using Html Encode
            tv_description.setText(TextUtils.htmlEncode(iDescription));

            pid = bundle.getString("PID");

            String iStatus = bundle.getString("STATUS");
            tv_status.setText(iStatus);

            String iTComment = bundle.getString("TCOMMENT");
            tv_tcomment.setText(iTComment);

            String iPComment = bundle.getString("PCOMMENT");

            if (iPComment != null) {
                if (iPComment.equals("null") || iPComment.equals("NULL")) {
                    et_pcomment.setText("");
                } else {
                    et_pcomment.setText(iPComment);
                }
            }


            pUpdatedComment = iPComment;

            HomeworkId = bundle.getString("HOMEWORKID");

            sid = bundle.getString("SID");
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
        }


        if (bundle != null) {
            iImagelist = bundle.getString("IMAGELIST");
        }
//Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_homeworkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Homework Details");
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
            View view = findViewById(R.id.bb_homeworkdetails);
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
                        Intent intent = new Intent(HomeworkDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomeworkDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(HomeworkDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(HomeworkDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(HomeworkDetails.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


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
                String image_name = obj.getString("image_name");
                String file_size = obj.getString("file_size");

                DataSet notes = new DataSet();
                notes.setnote_file(image_name);
                notes.setnote_size(file_size);
                homeworkattachment.add(notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (image_list.length() > 0) {
            homeworkDetails.setVisibility(View.VISIBLE);
            showAttachments();
        } else {
            homeworkDetails.setVisibility(View.GONE);
        }

        if (bundle != null) {
            iFile = bundle.getString("IMAGELIST");
        }

    }

    private void showAttachments() {
        if (homeworkattachment.size() > 0) {
            for (int l = 0; l < homeworkattachment.size(); l++) {
                setViewAttachments(l);
            }
        }
    }

    private void setViewAttachments(final int l) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseViewpdf = layoutInflater.inflate(R.layout.image_namelist_layout,
                null);

        final TextView note_file = baseViewpdf.findViewById(R.id.note_file);
        final TextView tv_error = baseViewpdf.findViewById(R.id.tv_error);
        final ImageButton btn_download = baseViewpdf.findViewById(R.id.btn_download);

        String filename = homeworkattachment.get(l).getnote_file();
        note_file.setText(filename);

        //Checking file size
//        int fileSize = Integer.parseInt(homeworkattachment.get(l).getnote_size());
        String fileSize = homeworkattachment.get(l).getnote_size();
//        if (fileSize>0){
        if (fileSize.equals("0.00") || fileSize.equals("0") || fileSize.equals("0.0")) {
            tv_error.setVisibility(View.VISIBLE);
            btn_download.setVisibility(View.GONE);
        } else {
            tv_error.setVisibility(View.GONE);
            btn_download.setVisibility(View.VISIBLE);
        }


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fName = note_file.getText().toString();
                    downloadNotice(fName);
                    Toast.makeText(getApplicationContext(), "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/Homework folder or Downloads folder.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            private void downloadNotice(String fName) {
                try {
                    if (isReadStorageAllowed()) {

                        String downloadUrl;
                        fName = fName.replaceAll(" ", "%20");
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String inputDateStr = tv_sdate.getText().toString();
                        Date date = null;

                        try {
                            date = inputFormat.parse(inputDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String outputDateStr = outputFormat.format(date);

                        if (name.equals("SACS")) {
                            downloadUrl = dUrl + "uploads/homework/" + outputDateStr + "/" + HomeworkId + "/" + fName;
                        } else {
                            downloadUrl = dUrl + "uploads/" + name + "/homework/" + outputDateStr + "/" + HomeworkId + "/" + fName;
                        }

                        dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(downloadUrl);
                        System.out.println("HOMEWORKDOWNLOADURL - " + uri.toString());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                        request.allowScanningByMediaScanner();
                        String folder = "/Download/Evolvuschool/Parent/Homework/";
                        String StringFile = "/Evolvuschool/Parent/Homework/";
                        File directory = new File(folder);
                        File directory1 = new File(StringFile);


                        try {
                            request.setDestinationInExternalPublicDir(folder, fName);//v 28 allow to create and it deprecated method

                        } catch (Exception e) {

                            //v 28+
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + fName);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")
                        }

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
                    e.getMessage();
                }
            }
        });

        showAttachmentsLayout.addView(baseViewpdf);
    }

    private void requestStoragePermission() {
        try {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                Toast.makeText(HomeworkDetails.this, "To Download Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            int STORAGE_PERMISSION_CODE = 23;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isReadStorageAllowed() {
        int result = 0;
        try {
            //Getting the permission status
            result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            //If permission is granted returning true

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    public void updateHomework() {
        Bundle bundle = getIntent().getExtras();
        final String HomeworkId = bundle.getString("HOMEWORKID");
        final String CommentId = bundle.getString("COMMENTID");


        final String pid = (SharedPrefManager.getInstance(this).getRegId().toString());

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "update_homework",
                response -> {

                    progressBar.setVisibility(View.GONE);
                    if (response.replace("ï»¿", "") == null) {
                        Toast.makeText(HomeworkDetails.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(HomeworkDetails.this, ParentDashboard.class);
//                            i.putExtra("SID", sid);
//                            i.putExtra("CLASSID", classid);
//                            i.putExtra("SECTIONID", sectionid);
                        startActivity(i);
                        Toast.makeText(HomeworkDetails.this, "Comment Saved Successfully", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> Log.i("error", "onErrorResponse: " + error.toString())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(SharedPrefManager.KEY_STUDENT_ID, sid);
                params.put(SharedPrefManager.KEY_PARENT_ID, pid);
                if (HomeworkId != null) {
                    params.put("homework_id", HomeworkId);
                }
                if (CommentId != null) {
                    params.put("comment_id", CommentId);
                }

                params.put("parent_comment", et_pcomment.getText().toString());

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
    public void onClick(View view) {
        if (view.getId() == R.id.hupdate) {
            if (et_pcomment.length() == 0) {
                Toast.makeText(this, "Parent comment field is empty", Toast.LENGTH_SHORT).show();
            } else {
                updateHomework();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeworkDetails.this, HomeworkActivity.class);
        intent.putExtra("CLASSID", classid);
        intent.putExtra("SECTIONID", sectionid);
        intent.putExtra("SID", sid);
        intent.putExtra("PID", pid);
        startActivity(intent);
        finish();
    }
}


