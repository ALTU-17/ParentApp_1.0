package in.aceventura.evolvuschool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.Payment.PaymentWebview;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.encryption.Encryption;
import in.aceventura.evolvuschool.utils.ConstantsFile;


public class NavigationDrawer extends AppCompatActivity {

    ImageButton ham, imgbtn_myprofile, imgbtn_payment, imgbtn_profile_update, imgbtn_changePwd, imgbtn_shareapp, imgbtn_about_us, imgbtn_books, imgbtn_logout;
    ImageButton btn_close_menu, iv_AcademicYear, imgbtn_rv_bonafide;
    DatabaseHelper mDatabaseHelper;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    RelativeLayout rv_bonafide;
    String name, dUrl, newUrl;
    RelativeLayout library, IDCard, paymentLayout, changePwdMenu, myProfile, shareApp, aboutUs, logOut;
    LinearLayout extra1, extra2;
    TextView tv_AcademicYear, tv_imgbtn_payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Parent Dashboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_attach_menu);

        final RelativeLayout mRevealView = findViewById(R.id.reveal_items);
        mRevealView.setVisibility(View.VISIBLE);
        ham = findViewById(R.id.drawer);
        myProfile = findViewById(R.id.lay1);
        tv_AcademicYear = findViewById(R.id.tv_AcademicYear);
        tv_imgbtn_payment = findViewById(R.id.tv_imgbtn_payment);
        library = findViewById(R.id.library);
        IDCard = findViewById(R.id.IDCard);
        rv_bonafide = findViewById(R.id.rv_bonafide);
        shareApp = findViewById(R.id.lay4);
        aboutUs = findViewById(R.id.lay5);
        logOut = findViewById(R.id.lay6);
        iv_AcademicYear = findViewById(R.id.iv_AcademicYear);
        imgbtn_rv_bonafide = findViewById(R.id.imgbtn_rv_bonafide);
        iv_AcademicYear.setVisibility(View.GONE);

        imgbtn_rv_bonafide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BonafideCertificateActivityList.class));
            }
        });

        changePwdMenu = findViewById(R.id.changePwdMenu);
        btn_close_menu = findViewById(R.id.closenav);
        paymentLayout = findViewById(R.id.paymentLayout);

        imgbtn_myprofile = findViewById(R.id.imgbtn_myprofile);
        imgbtn_payment = findViewById(R.id.imgbtn_payment);
        imgbtn_profile_update = findViewById(R.id.imgbtn_profile_update);
        imgbtn_changePwd = findViewById(R.id.imgbtn_changePwd);
        imgbtn_shareapp = findViewById(R.id.imgbtn_shareapp);
        imgbtn_about_us = findViewById(R.id.imgbtn_about_us);
        imgbtn_books = findViewById(R.id.imgbtn_books);
        imgbtn_logout = findViewById(R.id.imgbtn_logout);


        extra1 = findViewById(R.id.extra1);
        extra2 = findViewById(R.id.extra2);

        mDatabaseHelper = new DatabaseHelper(this);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(this);

        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);


        library.setVisibility(View.GONE);
        //extra2.setVisibility(View.GONE);
        //extra1.setVisibility(View.GONE);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        show_icons_parentdashboard_apk();

        Log.e("nameninddn", "vailed" + name + "newUrl??" + newUrl + "dUrl" + dUrl);

        if (name.equals("SACS")) {
            library.setVisibility(View.VISIBLE);
            //  extra1.setVisibility(View.INVISIBLE);
            //  extra2.setVisibility(View.GONE);
        } else if (name.equals("EVOLVU")) {
            library.setVisibility(View.VISIBLE);
            // extra1.setVisibility(View.INVISIBLE);
            //extra2.setVisibility(View.GONE);
        } else {
            library.setVisibility(View.GONE);
            //  extra1.setVisibility(View.INVISIBLE);
            //  extra2.setVisibility(View.INVISIBLE);
        }

        //cross button to close the drawer...
        btn_close_menu.setOnClickListener(v -> finish());


        myProfile.setOnClickListener(v -> {
            Intent intent = new Intent(NavigationDrawer.this, ParentProfile.class);
            startActivity(intent);
            finish();

        });
        iv_AcademicYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationDrawer.this, ChangeAcademicYearActivity.class));
            }
        });
        imgbtn_myprofile.setOnClickListener(v -> {
            Intent intent = new Intent(NavigationDrawer.this, ParentProfile.class);
            startActivity(intent);
            finish();

        });


        library.setOnClickListener(v -> {
            Intent intent1 = new Intent(NavigationDrawer.this, BookAvailability.class);
            startActivity(intent1);
            finish();

        });

        imgbtn_books.setOnClickListener(v -> {
            Intent intent1 = new Intent(NavigationDrawer.this, BookAvailability.class);
            startActivity(intent1);
            finish();

        });

        paymentLayout.setOnClickListener(v -> navigateToWebView());
        imgbtn_payment.setOnClickListener(v -> navigateToWebView());

        changePwdMenu.setOnClickListener(v -> navigateToChangePassword());
        imgbtn_changePwd.setOnClickListener(v -> navigateToChangePassword());

        IDCard.setOnClickListener(v -> navigateToProfileImgWebview());
        imgbtn_profile_update.setOnClickListener(v -> navigateToProfileImgWebview());


        shareApp.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = getString(R.string.share_app_link);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "EvolvU Smart Application");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });

        imgbtn_shareapp.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = getString(R.string.share_app_link);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "EvolvU Smart Application");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });

        aboutUs.setOnClickListener(v -> {
            Intent intent1 = new Intent(NavigationDrawer.this, AboutUsActivity.class);
            startActivity(intent1);
            finish();

        });

        imgbtn_about_us.setOnClickListener(v -> {
            Intent intent1 = new Intent(NavigationDrawer.this, AboutUsActivity.class);
            startActivity(intent1);
            finish();

        });

        logOut.setOnClickListener(v -> logout_user());
        imgbtn_logout.setOnClickListener(v -> logout_user());
    }

    private void show_icons_parentdashboard_apk() {


        Log.e("iconsboard", "Respo=url>" + newUrl + "show_icons_parentdashboard_apk");

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "show_icons_parentdashboard_apk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("iconsboard", "Respo=>" + response);
                try {
                    Log.e("iconsboard", "Respoinsidetry=>" + response);


                    Log.e("iconsboard", "RespoinsideCondition=>" + response);


                    JSONObject object = new JSONObject(response);
                    Log.e("iconsboard", "?>>>>" + object);
                    try {
                        if (object.getString("change_academic_year").equals("1")) {
                            iv_AcademicYear.setVisibility(View.VISIBLE);
                            library.setVisibility(View.VISIBLE);
                            tv_AcademicYear.setVisibility(View.VISIBLE);
                        } else {
                            iv_AcademicYear.setVisibility(View.INVISIBLE);
                            library.setVisibility(View.INVISIBLE);
                            tv_AcademicYear.setVisibility(View.INVISIBLE);

                        }
                    } catch (Exception e) {
                        Log.e("iconsboard", "RespoinsideConditioninside=>" + e.getMessage());
                    }

                    try {
                        if (object.getString("bonafide_certificate").equals("1")) {
                            rv_bonafide.setVisibility(View.VISIBLE);

                        } else {
                            rv_bonafide.setVisibility(View.GONE);


                        }
                    } catch (Exception e) {
                        rv_bonafide.setVisibility(View.GONE);
                        Log.e("iconsboard", "RespoinsideConditioninside=>" + e.getMessage());
                    }


                    try {
                        if (object.getString("online_fees_payment").equals("1"))
                        {
                            paymentLayout.setVisibility(View.VISIBLE);
                            imgbtn_payment.setVisibility(View.VISIBLE);
                            tv_imgbtn_payment.setVisibility(View.VISIBLE);

                        }
                        else
                        {

                            imgbtn_payment.setVisibility(View.GONE);
                            tv_imgbtn_payment.setVisibility(View.GONE);
                            paymentLayout.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {
                        imgbtn_payment.setVisibility(View.GONE);
                        paymentLayout.setVisibility(View.GONE);
                        tv_imgbtn_payment.setVisibility(View.GONE);
                        Log.e("iconsboard", "RespoinsideConditioninside=>" + e.getMessage());
                    }

                } catch (Exception e) {
                    e.getMessage();
                    Log.e("iconsboard", "RespoinsideConditionmain=>" + e.getMessage());

                    Log.e("show_academic_result", "erorro=>" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iv_AcademicYear.setVisibility(View.INVISIBLE);
                library.setVisibility(View.INVISIBLE);
                tv_AcademicYear.setVisibility(View.INVISIBLE);


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
                params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());

                Log.e("iconsboard", "params=>" + params.toString());

                return params;
            }
        };

        requestQueue.add(request);
    }

    private void navigateToProfileImgWebview() {
        String profilePhotoUpdateUrl;
        if (name.equals("SACS")) {
            profilePhotoUpdateUrl = dUrl + "index.php/IdCard/upload_parentstudent_photos_apk";
            System.out.println("profilePhotoUpdateUrl" + profilePhotoUpdateUrl);

        } else {
            profilePhotoUpdateUrl = dUrl + "index.php/IdCard/upload_parentstudent_photos_apk";
            System.out.println("profilePhotoUpdateUrl" + profilePhotoUpdateUrl);
        }

        Intent intent = new Intent(NavigationDrawer.this, ProfilePhotoWebview.class);
        intent.putExtra("profilePhotoUpdateUrl", profilePhotoUpdateUrl);
        startActivity(intent);
    }

    private void navigateToChangePassword() {
        String reg_id = (String.valueOf((SharedPrefManager.getInstance(this).getRegId())));
        Intent intent1 = new Intent(NavigationDrawer.this, ChangePasswordActivity.class);
        intent1.putExtra("reg_id", reg_id);
        startActivity(intent1);
    }

    /*ONLINE PAYMENT*/
    private void navigateToWebView() {
        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String academic_yr, reg_id, username, username1, encryptedUsername = null, secretKey;
        academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());
        reg_id = (String.valueOf((SharedPrefManager.getInstance(this).getRegId())));

        //ARNOLDS
        if (name.equals("SACS")) {
            Log.e("paymentView","SACS>>");
            username1 = (String.valueOf((SharedPrefManager.getInstance(this).getUserId())));
            username = Uri.encode(username1, ALLOWED_URI_CHARS);
            secretKey = "aceventura@services";
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = dUrl + "index.php/admin/online_payment_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;

            Log.e("paymentView","SACS>>"+url);



            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(NavigationDrawer.this, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            startActivity(intent);
        }
        else if(name.equals("SFSPUNE") ||name.equals("SJSKW")) {

            secretKey = "evolvu@sfs";

            username = (String.valueOf((SharedPrefManager.getInstance(this).getUserId())));
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = dUrl + "index.php/Worldline/WL_online_payment_req_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;
            Log.e("paymentView","SFSPUNE,SJSKW>>"+url);

            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(NavigationDrawer.this, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            startActivity(intent);
        }else{

            //todo  sfsne and sfsnw school


            secretKey = "evolvu@sfs";
            username = (String.valueOf((SharedPrefManager.getInstance(this).getUserId())));
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            String url = dUrl + "index.php/Billdesk/BD_online_payment_req_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;

            Log.e("paymentView","sfsne and sfsnw>>"+url);
            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(NavigationDrawer.this, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            startActivity(intent);
        }
    }

    private void logout_user() {
        try {
            ConstantsFile.flagN = "";

            mStudentDatabaseHelper.clearData();//clearing Sqlite data => 4 July 2020
            clearApplicationData();

            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, NewLoginPage.class));
        } catch (Exception e) {
            ConstantsFile.flagN = "";
            clearApplicationData();
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, NewLoginPage.class));
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED ");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}