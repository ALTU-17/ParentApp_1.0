package in.aceventura.evolvuschool.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.AboutUsActivity;
import in.aceventura.evolvuschool.BonafideCertificateActivityList;
import in.aceventura.evolvuschool.BookAvailability;
import in.aceventura.evolvuschool.ChangeAcademicYearActivity;
import in.aceventura.evolvuschool.ChangePasswordActivity;
import in.aceventura.evolvuschool.NavigationDrawer;
import in.aceventura.evolvuschool.NewLoginPage;
import in.aceventura.evolvuschool.ParentProfile;
import in.aceventura.evolvuschool.Payment.PaymentWebview;
import in.aceventura.evolvuschool.ProfilePhotoWebview;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.SharedPrefManager;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.Sqlite.StudentsDatabaseHelper;
import in.aceventura.evolvuschool.encryption.Encryption;
import in.aceventura.evolvuschool.models.NavigationDrawerModel;
import in.aceventura.evolvuschool.utils.ConstantsFile;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyHolder> {
    Activity activity;
    ArrayList<NavigationDrawerModel> navigationDrawerModels;
    FragmentManager fragmentManager1;
    DatabaseHelper mDatabaseHelper;
    StudentsDatabaseHelper mStudentDatabaseHelper;
    String name, dUrl, newUrl;
    String payment_url = "";

    public NavigationDrawerAdapter(Activity activity, ArrayList<NavigationDrawerModel> navigationDrawerModels, FragmentManager fragmentManager1) {
        this.activity = activity;
        this.navigationDrawerModels = navigationDrawerModels;
        this.fragmentManager1 = fragmentManager1;
        mDatabaseHelper = new DatabaseHelper(activity);
        mStudentDatabaseHelper = new StudentsDatabaseHelper(activity);
        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
        show_icons_parentdashboard_apk();
    }

    private void show_icons_parentdashboard_apk() {

        Log.e("iconsboard", "Respo=url>" + newUrl + "show_icons_parentdashboard_apk");

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "show_icons_parentdashboard_apk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("iconsboard", "Respo=>" + response);
                try {

                    JSONObject object = new JSONObject(response);
                    Log.e("iconsboard", "?>>>>" + object);
                    payment_url = object.getString("payment_url");


                } catch (Exception e) {
                    e.getMessage();
                    Log.e("iconsboard", "RespoinsideConditionmain=>" + e.getMessage());

                    Log.e("show_academic_result", "erorro=>" + e.getMessage());
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

                params.put("academic_yr", SharedPrefManager.getInstance(activity).getAcademicYear());

                Log.e("iconsboard", "params=>" + params.toString());

                return params;
            }
        };

        requestQueue.add(request);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_navigation_list, parent, false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        NavigationDrawerModel navigationDrawerModel = navigationDrawerModels.get(position);
        holder.tv_navigation_name.setText(navigationDrawerModel.getnName());


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.iv_navigation_img.setBackground(activity.getDrawable(navigationDrawerModel.getnImage()));
            } else {
                holder.iv_navigation_img.setBackground(ContextCompat.getDrawable(activity, navigationDrawerModel.getnImage()));

            }

        } catch (Exception e) {
            e.getMessage();
            Glide.with(activity).load("").fitCenter().error(navigationDrawerModel.getnImage()).into(holder.iv_navigation_img);


        }

        holder.iv_navigation_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.logout + "".trim())) {
                    Toast.makeText(activity, "logout", Toast.LENGTH_SHORT).show();
                    logout_user();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.parenting + "".trim())) {
                    //Toast.makeText(activity, "myProfiel", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, ParentProfile.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.ic_certificate + "".trim())) {
                    //Toast.makeText(activity, "myProfiel", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, BonafideCertificateActivityList.class));

                    activity.finish();
                }


                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.ic_profile_update + "".trim())) {
                    //Toast.makeText(activity, "myProfiel", Toast.LENGTH_SHORT).show();
                    navigateToProfileImgWebview();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.change_password + "".trim())) {
                    //Toast.makeText(activity, "myProfiel", Toast.LENGTH_SHORT).show();
                    navigateToChangePassword();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.payment + "".trim())) {
                    //Toast.makeText(activity, "myProfiel", Toast.LENGTH_SHORT).show();
                    navigateToWebView();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.update + "".trim())) {
                    //Toast.makeText(activity, "myProfiel", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(activity, AboutUsActivity.class);
                    activity.startActivity(intent1);
                    activity.finish();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.studying + "".trim())) {
                    Intent intent1 = new Intent(activity, BookAvailability.class);
                    activity.startActivity(intent1);
                    activity.finish();
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.ic_share + "".trim())) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = activity.getString(R.string.share_app_link);
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "EvolvU Smart Application");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                if (String.format("%d", navigationDrawerModel.getnImage()).trim().equalsIgnoreCase("" + R.drawable.calendar + "".trim())) {
                    activity.startActivity(new Intent(activity, ChangeAcademicYearActivity.class));
                    activity.finish();

                }

            }
        });


    }

    /*ONLINE PAYMENT*/
    private void navigateToWebView() {
        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String academic_yr, reg_id, username, username1, encryptedUsername = null, secretKey;
        academic_yr = (SharedPrefManager.getInstance(activity).getAcademicYear());
        reg_id = (String.valueOf((SharedPrefManager.getInstance(activity).getRegId())));

        //ARNOLDS
        if (name.equals("SACS")) {
            Log.e("paymentView", "SACS>>");
            username1 = (String.valueOf((SharedPrefManager.getInstance(activity).getUserId())));
            username = Uri.encode(username1, ALLOWED_URI_CHARS);
            secretKey = "aceventura@services";
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

          /*  String url = dUrl + "index.php/admin/online_payment_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;*/
            String url = payment_url + "?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;

            Log.e("paymentView", "SACS>>" + url);


            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(activity, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            activity.startActivity(intent);
        } else if (name.equals("SFSPUNE") || name.equals("SJSKW")) {

            secretKey = "evolvu@sfs";

            username = (String.valueOf((SharedPrefManager.getInstance(activity).getUserId())));
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            /*String url = dUrl + "index.php/Worldline/WL_online_payment_req_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;*/
            String url = payment_url + "?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;

            Log.e("paymentView", "SFSPUNE,SJSKW>>" + url);

            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(activity, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            activity.startActivity(intent);
        } else {

            //todo  sfsne and sfsnw school


            secretKey = "evolvu@sfs";
            username = (String.valueOf((SharedPrefManager.getInstance(activity).getUserId())));
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            String url = payment_url + "?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;

            /*String url = dUrl + "index.php/Billdesk/BD_online_payment_req_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;*/

            Log.e("paymentView", "sfsne and sfsnw>>" + url);
            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(activity, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            activity.startActivity(intent);
        }
    }

/*    private void navigateToWebView() {
        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String academic_yr, reg_id, username, username1, encryptedUsername = null, secretKey;
        academic_yr = (SharedPrefManager.getInstance(activity).getAcademicYear());
        reg_id = (String.valueOf((SharedPrefManager.getInstance(activity).getRegId())));

        //ARNOLDS
        if (name.equals("SACS")) {
            username1 = (String.valueOf((SharedPrefManager.getInstance(activity).getUserId())));
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

            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(activity, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            activity.startActivity(intent);
        }
        //SFS
        else {
            secretKey = "evolvu@sfs";
            username = (String.valueOf((SharedPrefManager.getInstance(activity).getUserId())));
            try {
                encryptedUsername = Encryption.SHA1(username + secretKey);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url = dUrl + "index.php/Worldline/WL_online_payment_req_apk/?reg_id=" + reg_id +
                    "&academic_yr=" + academic_yr + "&user_id=" + username +
                    "&encryptedUsername=" + encryptedUsername +
                    "&short_name=" + name;

            System.out.println("PAYMENT_URL => " + url);
            Intent intent = new Intent(activity, PaymentWebview.class);
            intent.putExtra("paymentUrl", url);
            intent.putExtra("academic_yr", academic_yr);
            intent.putExtra("reg_id", reg_id);
            activity.startActivity(intent);
        }
    }*/


    private void navigateToChangePassword() {
        String reg_id = (String.valueOf((SharedPrefManager.getInstance(activity).getRegId())));
        Intent intent1 = new Intent(activity, ChangePasswordActivity.class);
        intent1.putExtra("reg_id", reg_id);
        activity.startActivity(intent1);
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

        Intent intent = new Intent(activity, ProfilePhotoWebview.class);
        intent.putExtra("profilePhotoUpdateUrl", profilePhotoUpdateUrl);
        activity.startActivity(intent);
    }


    private void logout_user() {
        try {
            ConstantsFile.flagN = "";

            mStudentDatabaseHelper.clearData();//clearing Sqlite data => 4 July 2020
            clearApplicationData();

            SharedPrefManager.getInstance(activity).logout();
            activity.finish();
            activity.startActivity(new Intent(activity, NewLoginPage.class));
        } catch (Exception e) {
            ConstantsFile.flagN = "";
            clearApplicationData();
            SharedPrefManager.getInstance(activity).logout();
            activity.finish();
            activity.startActivity(new Intent(activity, NewLoginPage.class));
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
        File cache = activity.getCacheDir();
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

    @Override
    public int getItemCount() {
        return navigationDrawerModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_navigation_img;
        TextView tv_navigation_name;
        LinearLayout ll_navi;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_navigation_name = itemView.findViewById(R.id.tv_navigation_name);
            iv_navigation_img = itemView.findViewById(R.id.iv_navigation_img);
            ll_navi = itemView.findViewById(R.id.ll_navi);

        }
    }
}
