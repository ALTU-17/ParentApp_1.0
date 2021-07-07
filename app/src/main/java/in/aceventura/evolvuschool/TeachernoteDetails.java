package in.aceventura.evolvuschool;

/*
 * Created by hp on 7/27/2017.
 */

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.Button;
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

public class TeachernoteDetails extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static TextView data;
    TextView tv_sclass, tv_subject, tv_sdate, tv_description, tv_image, tv_image_colon, note_file;
    String iFile, inotesId, isDate, outputDateStr, iSection, iSectionId, iImagelist, itemValue;
    DownloadManager dm;
    String name, newUrl, dUrl, pid, StudID;
    DatabaseHelper mDatabaseHelper;
    LinearLayout teachernoteDetails, showAttachmentsLayout;
    private List<DataSet> attachment = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 23;
    private String CID, SID;
    Button mBack;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Teacher Note Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachernote_details);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        // TODO: 15-11-2019
        //  Code for Security Error Exception
        int REQUEST_CODE = 1;
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        teachernoteDetails = findViewById(R.id.teachernoteDetails);
        tv_sclass = findViewById(R.id.sclass);
        tv_subject = findViewById(R.id.subject);
        tv_sdate = findViewById(R.id.sdate);
        tv_description = findViewById(R.id.description);
        tv_image = findViewById(R.id.tv_image);
        tv_image_colon = findViewById(R.id.tv_image_colon);
        note_file = findViewById(R.id.note_file);
        mBack = findViewById(R.id.bck);
        showAttachmentsLayout = findViewById(R.id.showAttachmentsLayout);

        Bundle bundle = getIntent().getExtras();

        String isClass = null;
        if (bundle != null) {
            isClass = bundle.getString("SCLASS");
            CID = bundle.getString("CID");
            SID = bundle.getString("SID");
            StudID = bundle.getString("StudID");
        }


        mBack.setOnClickListener(v -> {
            Intent intent = new Intent(TeachernoteDetails.this, TeachernoteActivity.class);
            intent.putExtra("CLASSID", CID);
            intent.putExtra("SECTIONID", SID);
            startActivity(intent);
        });

        String iSubject = null;
        if (bundle != null) {
            iSubject = bundle.getString("SUBJECT");
        }

        if (iSubject != null) {
            if (iSubject.equals("null")) {
                tv_subject.setText("");
            } else {
                tv_subject.setText(iSubject);
            }
        }

        if (bundle != null) {
            isDate = bundle.getString("SDATE");
        }
        tv_sdate.setText(isDate);

        String mjdate = tv_sdate.getText().toString();

        String iDescription = null;
        if (bundle != null) {
            iDescription = bundle.getString("DESCRIPTION");
        }
        if (iDescription != null) {
            tv_description.setText(iDescription);
        }

        if (bundle != null) {
            iSection = bundle.getString("SSECTION");
            pid = bundle.getString("PID");
        }

        if (bundle != null) {
            iSectionId = bundle.getString("SSECTIONID");
        }
        tv_sclass.setText(isClass + " " + iSection);


        if (bundle != null) {
            inotesId = bundle.getString("NOTESID");
        }

        if (bundle != null) {
            iImagelist = bundle.getString("IMAGELIST");
        }

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
                attachment.add(notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (image_list.length() > 0) {
            teachernoteDetails.setVisibility(View.VISIBLE);
            showAttachments();
        } else {
            teachernoteDetails.setVisibility(View.GONE);
        }

        if (bundle != null) {
            iFile = bundle.getString("IMAGELIST");
        }

        Log.d("file", iFile);

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_teachernotedetails);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Teacher Note Details");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            View view = findViewById(R.id.bb_teachernotedetails);
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
                        Intent intent = new Intent(TeachernoteDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(TeachernoteDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(TeachernoteDetails.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(TeachernoteDetails.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(TeachernoteDetails.this, ParentDashboard.class);
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

    private void showAttachments() {
        if (attachment.size() > 0) {
            for (int l = 0; l < attachment.size(); l++) {
                setViewAttachments(l);
            }
        }
    }

    private void setViewAttachments(int l) {
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("InflateParams") final View baseViewpdf = layoutInflater.inflate(R.layout.image_namelist_layout,
                null);

        final TextView note_file = baseViewpdf.findViewById(R.id.note_file);
        final TextView tv_error = baseViewpdf.findViewById(R.id.tv_error);
        final ImageButton btn_download = baseViewpdf.findViewById(R.id.btn_download);

        String filename = attachment.get(l).getnote_file();
        note_file.setText(filename);

        //Checking file size
        String fileSize = attachment.get(l).getnote_size();
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
                Toast.makeText(getApplicationContext(), "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/TeacherNote folder or Downloads folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.getMessage();
            }
        });
        showAttachmentsLayout.addView(baseViewpdf);
    }

    ///manoj waghmare - download attachments method...
    public void downloadNotice(String itemValue) {
        try {
            if (isReadStorageAllowed()) {
                String downloadUrl;
                itemValue = itemValue.replaceAll(" ", "%20");
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inputDateStr = tv_sdate.getText().toString();
                Date date = null;
                try {
                    date = inputFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                outputDateStr = outputFormat.format(date);

                if (name.equals("SACS")) {
                    downloadUrl = dUrl + "uploads/daily_notes/" + outputDateStr + "/" + inotesId + "/" + itemValue;
                } else {
                    downloadUrl = dUrl + "uploads/" + name + "/daily_notes/" + outputDateStr + "/" + inotesId + "/" + itemValue;
                }

                //original
                dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(downloadUrl);
                System.out.println("NOTEDOWNLOADURL - " + uri.toString());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                System.out.println(uri.toString());


                String folder = "/Download/Evolvuschool/Parent/TeacherNote/";
                String StringFile = "/Evolvuschool/Parent/TeacherNote/";
                request.allowScanningByMediaScanner();

                File directory = new File(folder);
                File directory1 = new File(StringFile);

           /*     try {
                    request.setDestinationInExternalPublicDir(folder, itemValue);
                } catch (Exception e) {
                    e.printStackTrace();

                }*/

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
        } catch (Exception e) {
            e.getMessage();
        }

        requestStoragePermission();
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = 0;
        try {
            result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //If permission is granted returning true
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
                Toast.makeText(TeachernoteDetails.this, "To Download Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                return;
                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops!! you just denied the permission", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(TeachernoteDetails.this, TeachernoteActivity.class);
        intent.putExtra("CLASSID", CID);
        intent.putExtra("SECTIONID", SID);
        intent.putExtra("PID", pid);
        intent.putExtra("SID", StudID);//17Dec19 changed
        startActivity(intent);
        finish();
    }
}


