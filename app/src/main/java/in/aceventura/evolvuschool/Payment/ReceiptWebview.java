package in.aceventura.evolvuschool.Payment;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import in.aceventura.evolvuschool.ParentDashboard;
import in.aceventura.evolvuschool.ParentProfile;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class ReceiptWebview extends AppCompatActivity {

    private static final String TAG = "ReceiptWebView";
    WebView receiptWebview;
    String newUserAgent, UserAgent, name, newUrl, dUrl;
    WebSettings webSettings, newWebSettings;
    Context context;
    Activity activity;
    Button btn_Back;
    DatabaseHelper mDatabaseHelper;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_webview);
        getSupportActionBar().hide();
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
        receiptWebview = findViewById(R.id.receiptWebview);
        btn_Back = findViewById(R.id.btn_Back);

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_receiptwebview);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);

        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);

        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Receipt");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReceiptWebview.this, ParentDashboard.class);
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
            View view = findViewById(R.id.bb_receiptwebview);
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
                        Intent intent = new Intent(ReceiptWebview.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ReceiptWebview.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ReceiptWebview.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ReceiptWebview.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ReceiptWebview.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        final ProgressDialog progDailog = ProgressDialog.show(ReceiptWebview.this, "Loading...", "Please wait.. ", true);
        progDailog.setCancelable(false);

        //Runtime External storage permission for saving download files
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }
        String receiptUrl = getIntent().getStringExtra("receiptUrl");
        Log.i(TAG, "receiptUrl" + receiptUrl);


        if (receiptUrl.isEmpty() || receiptUrl.equals("null")) {
            Toast.makeText(ReceiptWebview.this, "Something went wrong..Please try Again", Toast.LENGTH_SHORT).show();
        } else {
            receiptWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            receiptWebview.getSettings().setSupportMultipleWindows(true);
            receiptWebview.getSettings().setSaveFormData(false);
            receiptWebview.getSettings().setDomStorageEnabled(true);
            receiptWebview.getSettings().setAllowFileAccess(true);
            receiptWebview.getSettings().setUseWideViewPort(true);
            receiptWebview.getSettings().setLoadWithOverviewMode(true);
            receiptWebview.getSettings().setLoadsImagesAutomatically(true);
            receiptWebview.getSettings().setJavaScriptEnabled(true);
            receiptWebview.setWebViewClient(new MyBrowser());
            webSettings = receiptWebview.getSettings();
            newUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36";
            webSettings.setUserAgentString(newUserAgent);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            receiptWebview.setWebViewClient(new WebViewClient() {
                public void onLoadResource(WebView view, String url) {
                    if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
                        finish();
                    } else super.onLoadResource(view, url);
                }
            });

            //==================
            receiptWebview.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
                try {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));
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
                        request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS, "/Evolvuschool/Parent/Receipt/" + URLUtil.guessFileName(
                                        url, contentDisposition, mimeType));
                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //==================

            receiptWebview.setWebViewClient(new WebViewClient() {
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

            receiptWebview.setWebChromeClient(new WebChromeClient() {


                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog,
                                              boolean isUserGesture, Message resultMsg) {

                    WebView newWebView = new WebView(ReceiptWebview.this);
                    newWebView.getSettings().setJavaScriptEnabled(true);
                    newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    newWebView.getSettings().setSupportMultipleWindows(true);
                    newWebView.getSettings().setDomStorageEnabled(true);
                    newWebView.getSettings().setAllowFileAccess(true);
                    newWebView.getSettings().setUseWideViewPort(true);
                    newWebView.getSettings().setLoadWithOverviewMode(true);
                    newWebView.getSettings().setLoadsImagesAutomatically(true);
                    newWebSettings = receiptWebview.getSettings();
                    UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36";
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
                        try {
                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse(url));
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
                                request.setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS, "/Evolvuschool/Parent/Receipt/" + URLUtil.guessFileName(
                                                url, contentDisposition, mimeType));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //==================
                    return true;
                }
            });


            receiptWebview.loadUrl(receiptUrl);
        }

        btn_Back.setOnClickListener(v -> {
            Intent i = new Intent(ReceiptWebview.this, ParentDashboard.class);
            startActivity(i);
            finish();
        });

    }
}
