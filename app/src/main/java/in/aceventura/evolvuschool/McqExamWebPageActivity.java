package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.models.ExamListModel;


public class McqExamWebPageActivity extends AppCompatActivity {

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    WebView mcqExamWebview;
    String newUserAgent, UserAgent, name, newUrl, dUrl, queBankId, stud_id;
    WebSettings webSettings, newWebSettings;
    Context context;
    Activity activity;
    DatabaseHelper mDatabaseHelper;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_exam_page);
        getSupportActionBar().hide();
        final ProgressDialog progDailog = ProgressDialog.show(McqExamWebPageActivity.this, "Loading...", "Please" +
                " wait..", true);

        mcqExamWebview = findViewById(R.id.examWebview);
        context = this;
        activity = this;

        setUpWebViewDefaults(mcqExamWebview);


        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            mcqExamWebview.restoreState(savedInstanceState);
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            ExamListModel examListModel = (ExamListModel) bundle.getSerializable("user");
            if (examListModel != null) {
                queBankId = examListModel.getQuestion_bank_id();
                stud_id = examListModel.getStud_id();
            }
        }


        String reg_id = getIntent().getStringExtra("reg_id");
        String academic_yr = getIntent().getStringExtra("academic_yr");

        String examUrl = dUrl + "index.php/OnlineExam/view_online_exam_by_student?param1=edit&qb_type=mcq" +
                "&question_bank_id=" + queBankId + "&current_student_id=" + stud_id + "&short_name=" + name;

        //Demo page to for uploading file
        //String examUrl = "https://www.aceventura.in/demo/file_upload.php";

        System.out.println("MCQURL" + examUrl);

        if (examUrl.isEmpty() || examUrl.equals("null")) {
            Toast.makeText(McqExamWebPageActivity.this, "Something went wrong..Please try Again",
                    Toast.LENGTH_SHORT).show();
        }

        mcqExamWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final Uri uri = Uri.parse(url);
                return handleUri(uri);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return handleUri(uri);
            }

            private boolean handleUri(final Uri uri) {
                if (uri != null) {
                    Log.i("TAG", "Uri =" + uri);
                    // Based on some condition you need to determine if you are going to load the url
                    // in your web view itself or in a browser.
                    // You can use `host` or `scheme` or any part of the `uri` to decide.
                    if (uri.getLastPathSegment() != null) {
                        if (uri.getLastPathSegment().contains("p1_business_part1.php")) {
                            finish();
                            startActivity(new Intent(McqExamWebPageActivity.this, LoginActivity.class));
                            return false;
                        }
                    } else {
                        mcqExamWebview.loadUrl(uri.toString());
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progDailog.isShowing()) {
                    progDailog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(McqExamWebPageActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
                view.loadUrl("about:blank");
            }
        });

        //WebChromeClient for Image Chooser
        mcqExamWebview.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(McqExamWebPageActivity.this.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e("TAG", "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        });
        if (mcqExamWebview.getUrl() == null) {
            mcqExamWebview.loadUrl(examUrl);


        }

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_mcqexampage);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Online Exam");
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
            View view = findViewById(R.id.bb_mcqexampage);
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
                        Intent intent = new Intent(McqExamWebPageActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(McqExamWebPageActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(McqExamWebPageActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(McqExamWebPageActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(McqExamWebPageActivity.this, ParentDashboard.class);
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
        if (mcqExamWebview.canGoBack()) {
            mcqExamWebview.goBack();

        } else {
            //Getting Confirmation of user before closing this webview of exam
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes",
                    (dialog, id) -> finish()).setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, /*prefix*/

                ".jpg", /*suffix*/

                storageDir /*directory */

        );
        return imageFile;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // We set the WebViewClient to ensure links are consumed by the WebView rather
        // than passed to a browser if it can
        mcqExamWebview.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }


}


//Exam Url
//https://www.aceventura.in/demo/SACSv4test/index.php/evaluation/view_qb_by_student_questionwise/edit/91/9051


//Old Code
/*

public class McqExamWebPageActivity extends AppCompatActivity {

    WebView mcqExamWebview;
    String newUserAgent, UserAgent, name, newUrl, dUrl, queBankId, stud_id;
    WebSettings webSettings, newWebSettings;
    Context context;
    Activity activity;
    DatabaseHelper mDatabaseHelper;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_exam_page);
        mcqExamWebview = findViewById(R.id.examWebview);
        context = this;
        activity = this;

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore the previous URL and history stack
            mcqExamWebview.restoreState(savedInstanceState);
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            ExamListModel examListModel = (ExamListModel) bundle.getSerializable("user");
            if (examListModel != null) {
                queBankId = examListModel.getQuestion_bank_id();
                stud_id = examListModel.getStud_id();
            }
        }


        final ProgressDialog progDailog = ProgressDialog.show(McqExamWebPageActivity.this, "Loading...",
                "Please"
                        + " wait..", true);
//        progDailog.setCancelable(false);

        String reg_id = getIntent().getStringExtra("reg_id");
        String academic_yr = getIntent().getStringExtra("academic_yr");

        String examUrl = dUrl + "index.php/OnlineExam/view_online_exam_by_student?param1=edit&qb_type=mcq" +
                "&question_bank_id=" + queBankId + "&current_student_id=" + stud_id + "&short_name=" + name;

//        String examUrl = "https://www.aceventura.in/demo/file_upload.php";

        System.out.println("MCQURL" + examUrl);

        if (examUrl.isEmpty() || examUrl.equals("null")) {
            Toast.makeText(McqExamWebPageActivity.this, "Something went wrong..Please try Again",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mcqExamWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mcqExamWebview.getSettings().setSupportMultipleWindows(true);
            mcqExamWebview.getSettings().setSaveFormData(false);
            mcqExamWebview.getSettings().setDomStorageEnabled(true);
            mcqExamWebview.getSettings().setAllowFileAccess(true);
            mcqExamWebview.getSettings().setUseWideViewPort(true);
            mcqExamWebview.getSettings().setLoadWithOverviewMode(true);
            mcqExamWebview.getSettings().setLoadsImagesAutomatically(true);
            mcqExamWebview.getSettings().setJavaScriptEnabled(true);
            mcqExamWebview.setWebViewClient(new MyBrowser());
            mcqExamWebview.setWebChromeClient(new ChromeClient());

            if (Build.VERSION.SDK_INT >= 19) {
                mcqExamWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
            else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19) {
                mcqExamWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            webSettings = mcqExamWebview.getSettings();
            newUserAgent =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/78.0.3904.97 Safari/537.36";
            webSettings.setUserAgentString(newUserAgent);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


            mcqExamWebview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(PermissionRequest request) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        request.grant(request.getResources());
                    }
                }
            });

            mcqExamWebview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    progDailog.show();
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {
                    progDailog.dismiss();
                }
            });

            mcqExamWebview.setWebChromeClient(new WebChromeClient() {

                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                              Message resultMsg) {

                    WebView newWebView = new WebView(McqExamWebPageActivity.this);
                    newWebView.getSettings().setJavaScriptEnabled(true);
                    newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    newWebView.getSettings().setSupportMultipleWindows(true);
                    newWebView.getSettings().setDomStorageEnabled(true);
                    newWebView.getSettings().setAllowFileAccess(true);
                    newWebView.getSettings().setUseWideViewPort(true);
                    newWebView.getSettings().setLoadWithOverviewMode(true);
                    newWebView.getSettings().setLoadsImagesAutomatically(true);
                    newWebView.setWebChromeClient(new ChromeClient());

                    if (Build.VERSION.SDK_INT >= 19) {
                        newWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    }
                    else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19) {
                        newWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }

                    newWebSettings = mcqExamWebview.getSettings();
                    UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                            "Gecko) " + "Chrome/78.0.3904.97 Safari/537.36";
                    newWebSettings.setUserAgentString(UserAgent);
                    newWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

                    view.addView(newWebView);
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(newWebView);
                    resultMsg.sendToTarget();

                    newWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            progDailog.show();
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, final String url) {
                            progDailog.dismiss();
                        }
                    });

                    newWebView.setWebViewClient(new WebViewClient() {
                        public void onLoadResource(WebView view, String url) {
                            if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
                                finish();
                            }
                            else super.onLoadResource(view, url);
                        }
                    });
                    return true;
                }
            });

            mcqExamWebview.loadUrl(examUrl);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName, */
/*


                ".jpg", */
/*


        );
        return imageFile;
    }
    public class ChromeClient extends WebChromeClient {
        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient
        .FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("Error", "Unable to create Image File", ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
            return true;
        }
        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard
            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");
            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }
            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");
            mCapturedImageURI = Uri.fromFile(file);
            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });
            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
        }
        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }
        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {
            openFileChooser(uploadMsg, acceptType);
        }
    }

    @Override
    public void onBackPressed() {
        if (mcqExamWebview.canGoBack()) {
            mcqExamWebview.goBack();

        }
        else {
            //Getting Confirmation of user before closing this webview of exam
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes",
                    (dialog, id) -> finish()).setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


}


//Exam Url
//https://www.aceventura.in/demo/SACSv4test/index.php/evaluation/view_qb_by_student_questionwise/edit/91/9051
*/


//Code
///*
//package in.aceventura.evolvuschool;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Message;
//import android.os.Parcelable;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.webkit.ConsoleMessage;
//import android.webkit.JsResult;
//import android.webkit.PermissionRequest;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import in.aceventura.evolvuschool.Payment.MyBrowser;
//import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
//import in.aceventura.evolvuschool.models.ExamListModel;
//
//public class McqExamWebPageActivity extends AppCompatActivity {
//
//    WebView mcqExamWebview;
//    String newUserAgent, UserAgent, name, newUrl, dUrl, queBankId, stud_id;
//    WebSettings webSettings, newWebSettings;
//    Context context;
//    Activity activity;
//    DatabaseHelper mDatabaseHelper;
//
//    private static final int INPUT_FILE_REQUEST_CODE = 1;
//    private static final int FILECHOOSER_RESULTCODE = 1;
//    private ValueCallback<Uri> mUploadMessage;
//    private Uri mCapturedImageURI = null;
//    private ValueCallback<Uri[]> mFilePathCallback;
//    private String mCameraPhotoPath;
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//            Uri[] results = null;
//            // Check that the response is a good one
//            if (resultCode == Activity.RESULT_OK) {
//                if (data == null) {
//                    // If there is not data, then we may have taken a photo
//                    if (mCameraPhotoPath != null) {
//                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                    }
//                } else {
//                    String dataString = data.getDataString();
//                    if (dataString != null) {
//                        results = new Uri[]{Uri.parse(dataString)};
//                    }
//                }
//            }
//            mFilePathCallback.onReceiveValue(results);
//            mFilePathCallback = null;
//        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//            if (requestCode == FILECHOOSER_RESULTCODE) {
//                if (null == this.mUploadMessage) {
//                    return;
//                }
//                Uri result = null;
//                try {
//                    if (resultCode != RESULT_OK) {
//                        result = null;
//                    } else {
//                        // retrieve from the private variable if the intent is null
//                        result = data == null ? mCapturedImageURI : data.getData();
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "activity :" + e,
//                            Toast.LENGTH_LONG).show();
//                }
//                mUploadMessage.onReceiveValue(result);
//                mUploadMessage = null;
//            }
//        }
//    }
//
//
//    @SuppressLint("SetJavaScriptEnabled")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mcq_exam_page);
//        mcqExamWebview = findViewById(R.id.examWebview);
//        context = this;
//        activity = this;
//
//        mDatabaseHelper = new DatabaseHelper(this);
//        name = mDatabaseHelper.getName(1);
//        newUrl = mDatabaseHelper.getURL(1);
//        dUrl = mDatabaseHelper.getPURL(1);
//
//        if (name == null || name.equals("")) {
//            name = mDatabaseHelper.getName(1);
//            newUrl = mDatabaseHelper.getURL(1);
//            dUrl = mDatabaseHelper.getPURL(1);
//        }
//
//        // Check whether we're recreating a previously destroyed instance
//        if (savedInstanceState != null) {
//            // Restore the previous URL and history stack
//            mcqExamWebview.restoreState(savedInstanceState);
//        }
//
//        Intent i = getIntent();
//        Bundle bundle = i.getExtras();
//        if (bundle != null) {
//            ExamListModel examListModel = (ExamListModel) bundle.getSerializable("user");
//            if (examListModel != null) {
//                queBankId = examListModel.getQuestion_bank_id();
//                stud_id = examListModel.getStud_id();
//            }
//        }
//
//
//
//        final ProgressDialog progDailog = ProgressDialog.show(McqExamWebPageActivity.this, "Loading...",
//                "Please"
//                + " wait..", true);
////        progDailog.setCancelable(false);
//
//        String reg_id = getIntent().getStringExtra("reg_id");
//        String academic_yr = getIntent().getStringExtra("academic_yr");
//
//        String examUrl = dUrl + "index.php/OnlineExam/view_online_exam_by_student?param1=edit&qb_type=mcq" +
//                "&question_bank_id=" + queBankId + "&current_student_id=" + stud_id + "&short_name=" + name;
//
////        String examUrl = "https://www.aceventura.in/demo/file_upload.php";
//
//        System.out.println("MCQURL" + examUrl);
//
//        if (examUrl.isEmpty() || examUrl.equals("null")) {
//            Toast.makeText(McqExamWebPageActivity.this, "Something went wrong..Please try Again",
//                    Toast.LENGTH_SHORT).show();
//        }
//        else {
//            mcqExamWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            mcqExamWebview.getSettings().setSupportMultipleWindows(true);
//            mcqExamWebview.getSettings().setSaveFormData(false);
//            mcqExamWebview.getSettings().setDomStorageEnabled(true);
//            mcqExamWebview.getSettings().setAllowFileAccess(true);
//            mcqExamWebview.getSettings().setUseWideViewPort(true);
//            mcqExamWebview.getSettings().setLoadWithOverviewMode(true);
//            mcqExamWebview.getSettings().setLoadsImagesAutomatically(true);
//            mcqExamWebview.getSettings().setJavaScriptEnabled(true);
//            mcqExamWebview.setWebViewClient(new MyBrowser());
//            mcqExamWebview.setWebChromeClient(new ChromeClient());
//
//            if (Build.VERSION.SDK_INT >= 19) {
//                mcqExamWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            }
//            else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19) {
//                mcqExamWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            }
//
//            webSettings = mcqExamWebview.getSettings();
//            newUserAgent =
//                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
//                            "Chrome/78.0.3904.97 Safari/537.36";
//            webSettings.setUserAgentString(newUserAgent);
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//
//
//            mcqExamWebview.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public void onPermissionRequest(PermissionRequest request) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        request.grant(request.getResources());
//                    }
//                }
//            });
//
//
//            mcqExamWebview.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    progDailog.show();
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                @Override
//                public void onPageFinished(WebView view, final String url) {
//                    progDailog.dismiss();
//                }
//            });
//
//            mcqExamWebview.setWebChromeClient(new WebChromeClient() {
//
//
//                @Override
//                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
//                                              Message resultMsg) {
//
//                    WebView newWebView = new WebView(McqExamWebPageActivity.this);
//                    newWebView.getSettings().setJavaScriptEnabled(true);
//                    newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//                    newWebView.getSettings().setSupportMultipleWindows(true);
//                    newWebView.getSettings().setDomStorageEnabled(true);
//                    newWebView.getSettings().setAllowFileAccess(true);
//                    newWebView.getSettings().setUseWideViewPort(true);
//                    newWebView.getSettings().setLoadWithOverviewMode(true);
//                    newWebView.getSettings().setLoadsImagesAutomatically(true);
//                    newWebView.setWebChromeClient(new ChromeClient());
//
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        newWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//                    }
//                    else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19) {
//                        newWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//                    }
//
//                    newWebSettings = mcqExamWebview.getSettings();
//                    UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
//                            "Gecko) " + "Chrome/78.0.3904.97 Safari/537.36";
//                    newWebSettings.setUserAgentString(UserAgent);
//                    newWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//
//                    view.addView(newWebView);
//                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                    transport.setWebView(newWebView);
//                    resultMsg.sendToTarget();
//
//                    newWebView.setWebViewClient(new WebViewClient() {
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            progDailog.show();
//                            view.loadUrl(url);
//                            return true;
//                        }
//
//                        @Override
//                        public void onPageFinished(WebView view, final String url) {
//                            progDailog.dismiss();
//                        }
//                    });
//
//                    newWebView.setWebViewClient(new WebViewClient() {
//                        public void onLoadResource(WebView view, String url) {
//                            if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
//                                finish();
//                            }
//                            else super.onLoadResource(view, url);
//                        }
//                    });
//                    return true;
//                }
//            });
//
//            mcqExamWebview.loadUrl(examUrl);
//        }
//    }
//
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File imageFile = File.createTempFile(
//                imageFileName,  */
///* prefix *//*
//
//                ".jpg",         */
///* suffix *//*
//
//                storageDir      */
///* directory *//*
//
//        );
//        return imageFile;
//    }
//    public class ChromeClient extends WebChromeClient {
//        // For Android 5.0
//        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient
//        .FileChooserParams fileChooserParams) {
//            // Double check that we don't have any existing callbacks
//            if (mFilePathCallback != null) {
//                mFilePathCallback.onReceiveValue(null);
//            }
//            mFilePathCallback = filePath;
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                // Create the File where the photo should go
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
//                } catch (IOException ex) {
//                    // Error occurred while creating the File
//                    Log.e("Error", "Unable to create Image File", ex);
//                }
//                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photoFile));
//                } else {
//                    takePictureIntent = null;
//                }
//            }
//            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//            contentSelectionIntent.setType("image/*");
//            Intent[] intentArray;
//            if (takePictureIntent != null) {
//                intentArray = new Intent[]{takePictureIntent};
//            } else {
//                intentArray = new Intent[0];
//            }
//            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
//            return true;
//        }
//        // openFileChooser for Android 3.0+
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            mUploadMessage = uploadMsg;
//            // Create AndroidExampleFolder at sdcard
//            // Create AndroidExampleFolder at sdcard
//            File imageStorageDir = new File(
//                    Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES)
//                    , "AndroidExampleFolder");
//            if (!imageStorageDir.exists()) {
//                // Create AndroidExampleFolder at sdcard
//                imageStorageDir.mkdirs();
//            }
//            // Create camera captured image file path and name
//            File file = new File(
//                    imageStorageDir + File.separator + "IMG_"
//                            + System.currentTimeMillis()
//                            + ".jpg");
//            mCapturedImageURI = Uri.fromFile(file);
//            // Camera capture image intent
//            final Intent captureIntent = new Intent(
//                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//            // Create file chooser intent
//            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
//            // Set camera intent to file chooser
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
//                    , new Parcelable[] { captureIntent });
//            // On select image call onActivityResult method of activity
//            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
//        }
//        // openFileChooser for Android < 3.0
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            openFileChooser(uploadMsg, "");
//        }
//        //openFileChooser for other Android versions
//        public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                    String acceptType,
//                                    String capture) {
//            openFileChooser(uploadMsg, acceptType);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mcqExamWebview.canGoBack()) {
//            mcqExamWebview.goBack();
//
//        }
//        else {
//            //Getting Confirmation of user before closing this webview of exam
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes",
//                    (dialog, id) -> finish()).setNegativeButton("No", (dialog, id) -> dialog.cancel());
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//    }
//
//
//}
//
//*/

//Exam Url
//https://www.aceventura.in/demo/SACSv4test/index.php/evaluation/view_qb_by_student_questionwise/edit/91/9051