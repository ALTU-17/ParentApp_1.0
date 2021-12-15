package in.aceventura.evolvuschool.viewholders;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.StudentACademicResult;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ExamViewHolder extends GroupViewHolder {
    String first_name = "", mid_name = "", last_name = "", type = "";
    private TextView examTitle;
    private ImageView arrow;
    LinearLayout ll_list_Proficiency_Certificate, ll_list_item_genre_arrow, ll_main_item, ll_Certificate;
    String nameitem;
    DatabaseHelper mDatabaseHelper;

    public String name;
    public String termFinalId = "";
    String newUrl, dUrl;
    Context mContext1;
    Activity mActivity;
    String sid, term_id1, term_id2;
    String filename = "";
    DownloadManager dm;
    String Exam_Name_Term_Id;
    String ExamID = "";
    String class_name;
    String resurl = "";
    String Ids = "";

    public ExamViewHolder(View itemView, String class_name, String Exam_Name_Term_Id, String term_id1, String term_id2, Activity mActivity, Context mContext, String sid) {
        super(itemView);
        this.mContext1 = mContext;
        this.mActivity = mActivity;
        nameitem = name;
        this.sid = sid;
        this.class_name = class_name;
        this.term_id1 = term_id1;
        this.term_id2 = term_id2;
        this.Exam_Name_Term_Id = Exam_Name_Term_Id;
        Log.e("ExamViewHolder", "VALUES>" + "TERM 1>" + term_id1);
        Log.e("ExamViewHolder", "VALUES>" + "TERM 2>" + term_id2);
        Log.e("ExamViewHolder", "VALUES>" + "Finam EXAM>" + Exam_Name_Term_Id);
        Log.e("ExamViewHolder", "VALUES>" + "class_name>" + class_name);

        examTitle = itemView.findViewById(R.id.txtexamname);
        ll_list_Proficiency_Certificate = itemView.findViewById(R.id.ll_list_Proficiency_Certificate);
        ll_list_item_genre_arrow = itemView.findViewById(R.id.ll_list_item_genre_arrow);
        arrow = itemView.findViewById(R.id.list_item_genre_arrow);
        ll_main_item = itemView.findViewById(R.id.ll_main_item);
        ll_Certificate = itemView.findViewById(R.id.ll_Certificate);
        mDatabaseHelper = new DatabaseHelper(mContext1);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        Log.e("exholder", "name=" + name);
        if (name.equalsIgnoreCase("Term 1") || name.equalsIgnoreCase("Term 2") || name.equalsIgnoreCase("Final exam")) {
            Log.e("exholder", "name=" + name);

        } else {
            ll_list_Proficiency_Certificate.setVisibility(View.GONE);
            ll_Certificate.setVisibility(View.VISIBLE);


        }

        examTitle.setText(name);


        ll_list_Proficiency_Certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("EXAMHOLDER", "VALUES>IDS>" + nameitem);
                if (nameitem.equalsIgnoreCase("Term 1")) {
                    try {
                        // getDownload_ProficiencyCer(term_id22);


                        getDownload_ProficiencyCer(term_id1);
                    } catch (Exception e) {

                    }


                } else if (nameitem.equalsIgnoreCase("Term 2")) {
                    try {

                        //  getDownload_ProficiencyCer(term_id22);
                        getDownload_ProficiencyCer(term_id2);

                    } catch (Exception e) {

                    }
                } else if (nameitem.equalsIgnoreCase("Final exam")) {
                    try {

                        getDownload_ProficiencyCer(Exam_Name_Term_Id);

                        /// getDownload_ProficiencyCer(term_id22);
                    } catch (Exception e) {
                    }
                }


            }
        });

    }

    public void setExamTitle(String name) {
        Log.e("exholder", "name=" + name);
        if (name.equalsIgnoreCase("Term 1") || name.equalsIgnoreCase("Term 2") || name.equalsIgnoreCase("Final exam")) {
            //ll_Certificate.setVisibility(View.GONE);
            // ll_list_Proficiency_Certificate.setVisibility(View.VISIBLE);
            try {

                getDownload_Proficiency(term_id1);
                Log.e("term", "ids0=" + term_id1);
            } catch (Exception e) {

            }
            try {

                getDownload_Proficiency(term_id2);
                Log.e("term", "ids00=" + term_id2);
            } catch (Exception e) {

            }
            try {
                if (class_name.equalsIgnoreCase("9") || class_name.equalsIgnoreCase("11"))
                getDownload_Proficiency(Exam_Name_Term_Id);
                Log.e("term", "ids000=" + Exam_Name_Term_Id);

            } catch (Exception e) {

            }
        } else {
            ll_list_Proficiency_Certificate.setVisibility(View.GONE);
            ll_Certificate.setVisibility(View.VISIBLE);

        }
        nameitem = name;
        examTitle.setText(name);
    }


    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    public void setITEM(Context context) {


        if (nameitem.equalsIgnoreCase("Term 1") || nameitem.equalsIgnoreCase("Term 2") || nameitem.equalsIgnoreCase("Final exam")) {
            try {

                getDownload_Proficiency(term_id1);

            } catch (Exception e) {
            }
            try {

                getDownload_Proficiency(term_id2);

            } catch (Exception e) {
            }
            try {

                if (class_name.equalsIgnoreCase("9") || class_name.equalsIgnoreCase("11"))
                    getDownload_Proficiency(Exam_Name_Term_Id);


            } catch (Exception e) {
            }


            // ll_Certificate.setVisibility(View.GONE);
            //ll_list_Proficiency_Certificate.setVisibility(View.VISIBLE);
        } else {
            ll_Certificate.setVisibility(View.VISIBLE);
            ll_list_Proficiency_Certificate.setVisibility(View.GONE);

        }

/*
        ll_list_Proficiency_Certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("EXAMHOLDER", "VALUES>IDS>" + nameitem);
                if (nameitem.equalsIgnoreCase("Term 1")) {
                    getDownload_ProficiencyCer(term_id1);
                } else if (nameitem.equalsIgnoreCase("Term 2")) {
                    getDownload_ProficiencyCer(term_id2);
                } else if (nameitem.equalsIgnoreCase("Final exam")) {
                    getDownload_ProficiencyCer(Exam_Name_Term_Id);
                } else {

                }


            }
        });*/

    }

    private void getDownload_ProficiencyCer(String term_id) {
        try {
            Log.e("puelFile", "url->" + term_id);

            /*if (mid_name.equals("")) {
                filename = "Proficiency_Certificate_" + first_name + ".pdf".trim();
            } else {
                filename = "Proficiency_Certificate_" + first_name + "_" + mid_name + ".pdf".trim();
            }

            if (last_name.equals("")) {
                filename = "Proficiency_Certificate_" + first_name + "_" + mid_name + ".pdf".trim();
            } else {
                filename = "Proficiency_Certificate_" + first_name + "_" + mid_name + "_" + last_name + ".pdf".trim();
            }*/
            Log.e("puel009", "url->ddExamID>" + ExamID);
            Log.e("puel009", "url->nameitem>" + nameitem);
            Log.e("puel009", "url->class_name>" + class_name);
            Log.e("puel009", "url->term_id>" + term_id);
            if (class_name.equalsIgnoreCase("9") || class_name.equalsIgnoreCase("11")) {

                filename = "Proficiency_Certificate_" + nameitem + "_" + first_name + ".pdf".trim();

                resurl = dUrl + "index.php/assessment/download_proficiency_certificate?login_type=P&student_id=" + sid + "&term_id=" + ExamID + "&type=" + type + "&academic_yr=" + SharedPrefManager.getInstance(mContext1).getAcademicYear().trim();

            } else {
                filename = "Proficiency_Certificate_" + nameitem + "_" + first_name + ".pdf".trim();

                resurl = dUrl + "index.php/assessment/download_proficiency_certificate?login_type=P&student_id=" + sid + "&term_id=" + term_id + "&type=" + type + "&academic_yr=" + SharedPrefManager.getInstance(mContext1).getAcademicYear().trim();

            }


            Log.e("puel", "url->" + resurl);
            Log.e("puel", "filename->" + filename);
            try {

                if (isReadStorageAllowed()) {
                    dm = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(resurl);
                    System.out.println("NOTICEDOWNLOADURL - " + uri.toString());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    //            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setMimeType("application/");
                    request.allowScanningByMediaScanner();

                    String folder = "/Download/Evolvuschool/Parent/ProficiencyCertificate/";
                    String StringFile = "/Evolvuschool/Parent/ProficiencyCertificate/";
                    File directory = new File(folder);
                    File directory1 = new File(StringFile);
                    try {

                        request.setDestinationInExternalPublicDir(folder, filename);//v 28 allow to create and it deprecated method

                    } catch (Exception e) {
                        //v 28+
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + filename);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")"/KrishanImages/" +
                    }


                    Log.e("Download", "donpa" + filename);

                    Toast.makeText(mContext1, "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/ProficiencyCertificate folder or Downloads folder", Toast.LENGTH_LONG).show();
                    //If directory not exists create one....
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    if (!directory1.exists()) {
                        directory1.mkdirs();
                    }
                    dm.enqueue(request);
                    return;


                    // requestStoragePermission();
                }
                requestStoragePermission();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("", "" + e.getMessage());
        }
    }

    private void getDownload_Proficiency(String term_id) {
        try {
            termFinalId = term_id;

            Log.e("Download_Proficiency ", "term_id>" + term_id);
            Log.e("Download_Proficiency ", "termFinalId>" + termFinalId);
            String url = newUrl + "check_proficiency_data_exist_for_studentid";
            Log.e("Download_Proficiency", "MainUrl>" + url);
            RequestQueue requestQueue = Volley.newRequestQueue(mContext1);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Download_Proficiency", "Mainresponse>" + response);

                    try {
                        JSONObject objcet = new JSONObject(response.toString());
                        if (objcet.getString("flag").equals("1")) {
                            ll_Certificate.setVisibility(View.GONE);
                            ll_list_Proficiency_Certificate.setVisibility(View.VISIBLE);
                            first_name = objcet.getString("first_name");
                            mid_name = objcet.getString("mid_name");
                            last_name = objcet.getString("last_name");
                            try {
                                ExamID = objcet.getString("term_id");
                                // getDownload_ProficiencyCer(term_id22);
                            } catch (Exception e) {

                            }

                            type = objcet.getString("type");


                        } else {
                            ll_Certificate.setVisibility(View.VISIBLE);
                            ll_list_Proficiency_Certificate.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
                    params.put("student_id", sid);///
                    params.put("short_name", name);

                    params.put("term_id", termFinalId);
                    try {
                        Log.e("Download_Proficiency", "inside" + Exam_Name_Term_Id);

                       // params.put("term_id", Exam_Name_Term_Id);

                    } catch (Exception e) {

                    }


                    Log.e("Download_Proficiency", "paramiteriii" + params);
                    return params;
                }
            };
            requestQueue.add(request);
        } catch (Exception e) {
            e.getMessage();
            Log.e("", "" + e.getMessage());
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(mContext1, "To Download Attachment Please Allow the Storage " +
                    "Permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        int STORAGE_PERMISSION_CODE = 23;
        ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , STORAGE_PERMISSION_CODE);
    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }


    public void setWeightSum(Context context) {

    }
}
