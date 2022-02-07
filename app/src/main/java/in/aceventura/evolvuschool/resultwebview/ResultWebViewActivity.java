package in.aceventura.evolvuschool.resultwebview;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
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

import in.aceventura.evolvuschool.ParentDashboard;
import in.aceventura.evolvuschool.ParentProfile;
import in.aceventura.evolvuschool.Payment.MyBrowser;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.StudentACademicResult;
import in.aceventura.evolvuschool.StudentDashboard;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

import static in.aceventura.evolvuschool.R.id;
import static in.aceventura.evolvuschool.R.layout;

public class ResultWebViewActivity extends AppCompatActivity {

    WebView resultWebView;
    String newUserAgent, UserAgent, student_id, class_id, reg_id, academic_yr;
    WebSettings webSettings, newWebSettings;
    Context context;
    Activity activity;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl, pid,class_name="";
    String resultUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_result_web_view);
        getSupportActionBar().hide();
        context = this;
        activity = this;
        resultWebView = findViewById(id.resultWebView);
        mDatabaseHelper = new DatabaseHelper(this);
        // TODO: 12-10-2020 added
        student_id = getIntent().getStringExtra("student_id");
        // TODO: 12-10-2020 added
        class_id = getIntent().getStringExtra("class_id");
        class_name = getIntent().getStringExtra("class_name");
        // TODO: 12-10-2020 added
        reg_id = (SharedPrefManager.getInstance(this).getRegId().toString());
        // TODO: 12-10-2020 added
        academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());

        final ProgressDialog progDailog = ProgressDialog.show(ResultWebViewActivity.this, "Loading...", "Please "
                + "wait.. ", true);
        progDailog.setCancelable(false);
/*
        // TODO: 12-10-2020 url for showing result webview only arnolds
        String resultUrl = "https://www.aceventura.in/demo/SACSv4test/index.php/assessment/show_report_card" +
                "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr;
        Log.i("TAG", "resultUrl" + resultUrl);*/

        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        //Top Bar
        View tb_main1 = findViewById(id.icd_tb_resultwebview);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Result");
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
            View view = findViewById(id.bb_resultwebview);
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
                        Intent intent = new Intent(ResultWebViewActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ResultWebViewActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ResultWebViewActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ResultWebViewActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ResultWebViewActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        // TODO: 17-12-2020 url for showing result webview only arnolds-->>>From DB

        //
        Log.e("classname","classnamed=?"+class_name);
        if(class_name.equals("11") || class_name.equals("12")){
           // resultUrl = dUrl + "index.php/HSC/show_report_card" +
           //         "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr;
            resultUrl = dUrl + "index.php/HSC/show_report_card" +
                    "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr + "&short_name=" + name;
            Log.e("classname","classnamedURL?"+resultUrl);
        }else{
           // resultUrl = dUrl + "index.php/assessment/show_report_card" +
            //        "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr;
            resultUrl = dUrl + "index.php/assessment/show_report_card" +
                    "?student_id=" + student_id + "&class_id=" + class_id + "&login_type=P&" + "acd_yr=" + academic_yr + "&short_name=" + name;

            Log.e("classname","classnamedURL?"+resultUrl);
        }
        Log.e("classname","classnamedURLFinal?"+resultUrl);
        Log.i("TAG", "resultUrl" + resultUrl);
        Log.w("TAGpo", "resultUrl" + resultUrl);

        if (resultUrl.isEmpty() || resultUrl.equals("null")) {
            Toast.makeText(ResultWebViewActivity.this, "Something went wrong..Please try Again",
                    Toast.LENGTH_SHORT).show();
        } else {
            resultWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            resultWebView.getSettings().setSupportMultipleWindows(true);
            resultWebView.getSettings().setSaveFormData(false);
            resultWebView.getSettings().setDomStorageEnabled(true);
            resultWebView.getSettings().setAllowFileAccess(true);
            resultWebView.getSettings().setUseWideViewPort(true);
            resultWebView.getSettings().setLoadWithOverviewMode(true);
            resultWebView.getSettings().setLoadsImagesAutomatically(true);
            resultWebView.getSettings().setJavaScriptEnabled(true);
            resultWebView.clearCache(true);
            resultWebView.clearHistory();
            resultWebView.setWebViewClient(new MyBrowser());
            webSettings = resultWebView.getSettings();
            newUserAgent =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/78.0.3904.97 Safari/537.36";
            webSettings.setUserAgentString(newUserAgent);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            resultWebView.setWebViewClient(new WebViewClient() {
                public void onLoadResource(WebView view, String url) {
                    if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
                        finish();
                    } else super.onLoadResource(view, url);
                }
            });

            //==================
            resultWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
                try {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    System.out.println(url);
                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading Receipt...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    try {
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                "/Evolvuschool/Parent/ReportCard/" + URLUtil.guessFileName(url, contentDisposition, mimeType));

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/ReportCard folder or Downloads folder", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //==================

            resultWebView.setWebViewClient(new WebViewClient() {
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

            resultWebView.setWebChromeClient(new WebChromeClient() {


                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                              Message resultMsg) {

                    WebView newWebView = new WebView(ResultWebViewActivity.this);
                    newWebView.getSettings().setJavaScriptEnabled(true);
                    newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    newWebView.getSettings().setSupportMultipleWindows(true);
                    newWebView.getSettings().setDomStorageEnabled(true);
                    newWebView.getSettings().setAllowFileAccess(true);
                    newWebView.getSettings().setUseWideViewPort(true);
                    newWebView.getSettings().setLoadWithOverviewMode(true);
                    newWebView.getSettings().setLoadsImagesAutomatically(true);
                    newWebSettings = resultWebView.getSettings();
                    UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                            "Gecko) Chrome/78.0.3904.97 Safari/537.36";
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
                            } else super.onLoadResource(view, url);
                        }
                    });

                    //==================
                    newWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        System.out.println(url);
                        request.setMimeType(mimeType);
                        String cookies = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookies);
                        request.addRequestHeader("User-Agent", userAgent);
                        request.setDescription("Downloading Result...");
                        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        try {
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                    "/Evolvuschool/Parent/ReportCard/" + URLUtil.guessFileName(url, contentDisposition, mimeType));
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                    });
                    //==================
                    return true;
                }
            });

            resultWebView.loadUrl(resultUrl);
        }


    }
}