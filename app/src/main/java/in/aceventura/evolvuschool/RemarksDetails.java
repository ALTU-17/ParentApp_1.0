package in.aceventura.evolvuschool;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.List;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class RemarksDetails extends AppCompatActivity {

    TextView remdesc;
    String iFile, inotesId, isDate, outputDateStr, iSection, iSectionId, iImagelist, itemValue;
    DownloadManager dm;
    String name, newUrl, dUrl, pid;
    DatabaseHelper mDatabaseHelper;
    LinearLayout Remarkattachments, showAttachmentsLayout;
    private List<DataSet> rem_attachment = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 23;
    private String CID, SID, rem_date, studID, attachments, secID, classID, REMID, description;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


        Remarkattachments = findViewById(R.id.homeworkDetails);
        showAttachmentsLayout = findViewById(R.id.showAttachmentsLayout);
        Remarkattachments.setVisibility(View.GONE);
        remdesc = findViewById(R.id.description);
        Bundle bundle = getIntent().getExtras();

        description = bundle.getString("DESCRIPTION");
        classID = bundle.getString("CLASSID");
        secID = bundle.getString("SECTIONID");
        studID = bundle.getString("SID");
        rem_date = bundle.getString("DATE");
        REMID = bundle.getString("REMID");
        pid = bundle.getString("PID");

        if (bundle != null) {
            iImagelist = bundle.getString("IMAGELIST");
        }

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_remarkdetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("("+SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear()+")");
        school_title.setText(name+" Evolvu Parent App");
        ht_Teachernote.setText("Remarks Details");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RemarksDetails.this, RemarkActivity.class);
                i.putExtra("CLASSID", classID);
                i.putExtra("SECTIONID", secID);
                i.putExtra("SID", studID);
                i.putExtra("PID", pid);
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

        //-

        //bottomBar
        try {
            View view = findViewById(R.id.bb_remarkdetails);
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
                        Intent intent = new Intent(RemarksDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(RemarksDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(RemarksDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(RemarksDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(RemarksDetails.this, ParentDashboard.class);
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
                notes.setnote_file(obj.getString("image_name"));
                notes.setnote_size(file_size);

                rem_attachment.add(notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (image_list.length() > 0) {
            Remarkattachments.setVisibility(View.VISIBLE);
            showAttachments();
        } else {
            Remarkattachments.setVisibility(View.GONE);
        }

        remdesc.setText(description);
    }

    private void showAttachments() {
        if (rem_attachment.size() > 0) {
            for (int l = 0; l < rem_attachment.size(); l++) {
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

        String filename = rem_attachment.get(l).getnote_file();
        note_file.setText(filename);

        //Checking file size
        String fileSize = rem_attachment.get(l).getnote_size();
//        String fileSize = "0";
        if (fileSize.equals("0.00") || fileSize.equals("0") || fileSize.equals("0.0")) {
            tv_error.setVisibility(View.VISIBLE);
            btn_download.setVisibility(View.GONE);
        } else {
            tv_error.setVisibility(View.GONE);
            btn_download.setVisibility(View.VISIBLE);

        }

        btn_download.setOnClickListener(v -> {
            String fName = note_file.getText().toString();
            downloadNotice(fName);
            Toast.makeText(getApplicationContext(), "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/Remarks folder or Downloads folder", Toast.LENGTH_LONG).show();
        });

        showAttachmentsLayout.addView(baseViewpdf);
    }

    public void downloadNotice(String itemValue) {
        try {
            if (isReadStorageAllowed()) {
                String downloadUrl;
                itemValue = itemValue.replaceAll(" ", "%20");
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inputFormat.parse(rem_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                outputDateStr = outputFormat.format(date);

                if (name.equals("SACS")) {
                    downloadUrl = dUrl + "uploads/remark/" + outputDateStr + "/" + REMID + "/" + itemValue;
                } else {
                    downloadUrl = dUrl + "uploads/" + name + "/remark/" + outputDateStr + "/" + REMID + "/" + itemValue;
                }

                dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(downloadUrl);
                System.out.println("REMARKDOWNLOADURL - " + uri.toString());
                DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.allowScanningByMediaScanner();


                String folder = "/Download/Evolvuschool/Parent/Remarks/";
                String StringFile = "/Evolvuschool/Parent/Remarks/";
                File directory = new File(folder);
                File directory1 = new File(StringFile);

                try {
                    request.setDestinationInExternalPublicDir(folder, itemValue);//v 28 allow to create and it deprecated method

                } catch (Exception e) {

                    //v 28+
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + itemValue);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")
                }


                //If directory not exists create one....
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                if (!directory1.exists()) {
                    directory1.mkdirs();
                }
                dm.enqueue(request);
                return;
            }
        } catch (Exception r) {
            r.printStackTrace();
        }
        requestStoragePermission();
    }

    private boolean isReadStorageAllowed() {

        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
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
                Toast.makeText(RemarksDetails.this, "To Download Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RemarksDetails.this, RemarkActivity.class);
        intent.putExtra("CLASSID", classID);
        intent.putExtra("SECTIONID", secID);
        intent.putExtra("SID", studID);
        intent.putExtra("PID", pid);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }
}
