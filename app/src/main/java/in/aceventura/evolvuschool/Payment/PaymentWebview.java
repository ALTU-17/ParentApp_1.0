package in.aceventura.evolvuschool.Payment;

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
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import in.aceventura.evolvuschool.AboutUsActivity;
import in.aceventura.evolvuschool.ParentDashboard;
import in.aceventura.evolvuschool.ParentProfile;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;

public class PaymentWebview extends AppCompatActivity {

    WebView paymentWebview;
    String newUserAgent, UserAgent, name, newUrl, dUrl;
    WebSettings webSettings, newWebSettings;
    Context context;
    Activity activity;
    Button btn_Back, btn_Receipt;
    DatabaseHelper mDatabaseHelper;
    TextView tv_result;
    LinearLayout ll_result, ll_Blank;
    String reg_id;
    String academic_yr;
    String paymentUrl;
    String payment_url = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_payemt_webview);
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
        paymentUrl = getIntent().getStringExtra("paymentUrl");
        reg_id = getIntent().getStringExtra("reg_id");
        academic_yr = getIntent().getStringExtra("academic_yr");

        paymentWebview = findViewById(R.id.paymentWebview);
        btn_Back = findViewById(R.id.btn_Back);
        btn_Receipt = findViewById(R.id.btn_Receipt);

        try {
            if (Build.VERSION.SDK_INT >= 19) {
                paymentWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                paymentWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {

        }


        show_icons_parentdashboard_apk();

        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_paymentwebview);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        tv_result = tb_main1.findViewById(R.id.tv_result);
        ll_result = tb_main1.findViewById(R.id.ll_result);
        ll_Blank = tb_main1.findViewById(R.id.ll_Blank);

        tv_result.setVisibility(View.GONE);
        ll_result.setVisibility(View.GONE);


        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);

        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Payment");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentWebview.this, ParentDashboard.class);
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
            View view = findViewById(R.id.bb_paymentwebview);
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
                        Intent intent = new Intent(PaymentWebview.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(PaymentWebview.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(PaymentWebview.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(PaymentWebview.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(PaymentWebview.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///


        //receipt button visibility
        if (name.equals("SACS")) {
            btn_Receipt.setVisibility(View.GONE);
        } else {
            btn_Receipt.setVisibility(View.VISIBLE);
        }

        final ProgressDialog progDailog = ProgressDialog.show(PaymentWebview.this, "Loading...",
                "Please wait...", true);
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


        if (paymentUrl.isEmpty() || paymentUrl.equals("null") || paymentUrl == null) {
            Toast.makeText(PaymentWebview.this, "Something went wrong..Please try Again", Toast.LENGTH_SHORT).show();
        } else {
            paymentWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            paymentWebview.getSettings().setSupportMultipleWindows(true);
            paymentWebview.getSettings().setSaveFormData(false);
            paymentWebview.getSettings().setDomStorageEnabled(true);
            paymentWebview.getSettings().setAllowFileAccess(true);
            paymentWebview.getSettings().setUseWideViewPort(true);
            paymentWebview.getSettings().setLoadWithOverviewMode(true);
            paymentWebview.getSettings().setLoadsImagesAutomatically(true);
            paymentWebview.getSettings().setJavaScriptEnabled(true);
            paymentWebview.setWebViewClient(new MyBrowser());
            webSettings = paymentWebview.getSettings();
            newUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
                    "like Gecko) Chrome/78.0.3904.97 Safari/537.36";
            webSettings.setUserAgentString(newUserAgent);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            paymentWebview.setWebViewClient(new WebViewClient() {
                public void onLoadResource(WebView view, String url) {
                    if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
                        finish();
                    } else super.onLoadResource(view, url);
                }
            });

            //==================
            paymentWebview.setDownloadListener((url, userAgent, contentDisposition, mimeType,
                                                contentLength) -> {

                try {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url.trim()));
                    System.out.println("Receipt_URL" + url);
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
                                Environment.DIRECTORY_DOWNLOADS, "/Evolvuschool/Parent/Receipts/" + URLUtil.guessFileName(
                                        url, contentDisposition, mimeType));
                        Toast.makeText(context, "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/Receipts folder or Downloads folder", Toast.LENGTH_LONG).show();
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

            paymentWebview.setWebViewClient(new WebViewClient() {
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

            paymentWebview.setWebChromeClient(new WebChromeClient() {

                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog,
                                              boolean isUserGesture, Message resultMsg) {

                    WebView newWebView = new WebView(PaymentWebview.this);
                    newWebView.getSettings().setJavaScriptEnabled(true);
                    newWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    newWebView.getSettings().setSupportMultipleWindows(true);
                    newWebView.getSettings().setDomStorageEnabled(true);
                    newWebView.getSettings().setAllowFileAccess(true);
                    newWebView.getSettings().setUseWideViewPort(true);
                    newWebView.getSettings().setLoadWithOverviewMode(true);
                    newWebView.getSettings().setLoadsImagesAutomatically(true);
                    newWebSettings = paymentWebview.getSettings();

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

                    // TODO: 08-09-2020
                   /* newWebView.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onCloseWindow(WebView window) {
                            super.onCloseWindow(window);
                            if (newWebView != null) {
                                paymentWebview.removeView(newWebView);
                            }
                        }

                    });*/

                    newWebView.setWebViewClient(new WebViewClient() {
                        public void onLoadResource(WebView view, String url) {
                            if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
                                finish();
                            } else super.onLoadResource(view, url);
                        }
                    });

                    //==================
                    newWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType,
                                                    contentLength) -> {
                        try {
                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse("RECEIPT_URL" + url));
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
                                        Environment.DIRECTORY_DOWNLOADS, "Evolvuschool/Parent/Payment/" + URLUtil.guessFileName(
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

                // TODO: 08-09-2020
                @Override
                public void onCloseWindow(WebView window) {
                    super.onCloseWindow(window);
                }
            });
            Log.e("paymentLoadUrl", "Values??" + paymentUrl);
            paymentWebview.loadUrl(paymentUrl);
        }

        btn_Back.setOnClickListener(v -> {
            Intent i = new Intent(PaymentWebview.this, ParentDashboard.class);
            startActivity(i);
            finish();
        });


        btn_Receipt.setOnClickListener(v -> {
            //only for sfs
            Intent i = new Intent(PaymentWebview.this, ReceiptWebview.class);
            //s

            // i.putExtra("receiptUrl", dUrl + "index.php/Worldline/WL_online_payment_receipts_apk?reg_id=" + reg_id + "&academic_yr=" + academic_yr + "&short_name=" + name);
            i.putExtra("receiptUrl", payment_url + "?reg_id=" + reg_id + "&academic_yr=" + academic_yr + "&short_name=" + name);
            Log.e("", "");

            startActivity(i);
            finish();
        });
    }

    private void show_icons_parentdashboard_apk() {
        Log.e("iconsboard", "Respo=url>" + dUrl + "show_icons_parentdashboard_apk");

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "show_icons_parentdashboard_apk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("iconsboard", "Respo=>" + response.toString());
                try {
                    if (response == null || response == "" || response.equals("")) {


                    } else {
                        try {

                            JSONObject object = new JSONObject(response);
                            Log.e("iconsboard", "?>>>>" + object.getString("academic_result"));

                            try {
                                if (object.getString("academic_result").equals("1")) {

                                } else {


                                }


                            } catch (Exception e) {
                                Log.e("iconsboard", "academic_result=>" + e.getMessage());

                                e.getMessage();
                            }
                            try {


                                Log.e("iconsboard", "?>>>>" + object);
                                payment_url = object.getString("receipt_url");


                            } catch (Exception e) {
                                e.getMessage();
                                Log.e("iconsboard", "RespoinsideConditionmain=>" + e.getMessage());

                                Log.e("show_academic_result", "erorro=>" + e.getMessage());
                            }

                            try {

                                if (object.getString("receipt_button").equals("1")) {
                                    tv_result.setVisibility(View.VISIBLE);
                                    ll_result.setVisibility(View.VISIBLE);
                                    ll_Blank.setVisibility(View.VISIBLE);

                                    tv_result.setText("" + getResources().getString(R.string.receipt));
                                    // btn_Receipt.setVisibility(View.VISIBLE);

                                    tv_result.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //only for sfs
                                            //
                                            Intent i = new Intent(PaymentWebview.this, ReceiptWebview.class);
                                            Log.e("RecUrl", "Urfl==>" + payment_url + "?reg_id=" + reg_id + "&academic_yr=" + academic_yr + "&short_name=" + name);
                                            i.putExtra("receiptUrl", payment_url + "?reg_id=" + reg_id + "&academic_yr=" + academic_yr + "&short_name=" + name);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                } else {
                                    tv_result.setVisibility(View.GONE);
                                    ll_result.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Log.e("iconsboard", "receipt_button=>" + e.getMessage());

                            }
                            try {
                                if (object.getString("certificate").equals("1")) {
                                } else {

                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Log.e("iconsboard", "certificate=>" + e.getMessage());

                            }
                            try {
                                if (object.getString("healthActivity_certificate").equals("1")) {
                                } else {

                                }
                            } catch (Exception e) {
                                e.getMessage();
                                Log.e("iconsboard", "healthActivity_certificate=>" + e.getMessage());

                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    e.getMessage();

                    Log.e("show_academic_result", "erorro=>" + response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("iconsboard", "Respo=GetAcademicYer>" + error.getStackTrace());
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
                ////9405"2020-2021"//SharedPrefManager.getInstance(MyCalendar.this).getAcademicYear()
                params.put("short_name", name);

                Log.e("iconsboard", "params=>" + params.toString());

                return params;
            }
        };

        requestQueue.add(request);
    }

}

