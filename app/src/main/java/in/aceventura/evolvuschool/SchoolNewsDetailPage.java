package in.aceventura.evolvuschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

public class SchoolNewsDetailPage extends AppCompatActivity {

    TextView news_title,news_date,news_description,news_Url;
    ImageView news_image;
    String news_url,name,newUrl,dUrl,img_path,news_id,news_image_name;
    DatabaseHelper mDatabaseHelper;
    LinearLayout news_urlLayout,imglayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_news_detail_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIds();
        news_urlLayout.setVisibility(View.GONE);
        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            news_title.setText(bundle.getString("news_title"));
            news_description.setText(bundle.getString("news_description"));
            news_url= bundle.getString("news_url");
            news_id = bundle.getString("news_id");
            news_image_name = bundle.getString("news_image_name");



            String inputDateStr = bundle.getString("news_date");


            if (!inputDateStr.equals("null") || inputDateStr != null || !inputDateStr.isEmpty()) {
                try {
                    date = inputFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date);
                news_date.setText(outputDateStr);
            } else {
                news_date.setText("");
            }



            //News Url
            if (news_url.isEmpty()){
                news_urlLayout.setVisibility(View.GONE);
            }
            else {
                news_urlLayout.setVisibility(View.VISIBLE);
                news_Url.setText(news_url);
                news_Url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsUrl = news_Url.getText().toString();
                        Uri uri = Uri.parse(newsUrl); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
        }

        if (news_image_name.isEmpty()){
            imglayout.setVisibility(View.GONE);
        }else{
            imglayout.setVisibility(View.VISIBLE);

            if (name.equals("SACS")) {
                img_path = dUrl + "uploads/news/" + news_id+ "/" + news_image_name;
            } else {
                img_path = dUrl + "uploads/" + name + "/news/" + news_id + "/" + news_image_name;
            }

            ImageView ivBasicImage = findViewById(R.id.news_image);
            Picasso.get()
                    .load(img_path.replaceAll(" ","%20"))
                    .fit()
                    .placeholder( R.drawable.progress_animation)
                    .centerInside()
                    .into(ivBasicImage)
            ;
        }

    } //OnCreate

    private void getIds() {
        news_title = findViewById(R.id.news_title);
        news_date = findViewById(R.id.news_date);
        news_description = findViewById(R.id.news_description);
        news_image = findViewById(R.id.news_image);
        news_Url= findViewById(R.id.news_url);
        news_urlLayout = findViewById(R.id.news_urlLayout);
        imglayout = findViewById(R.id.imglayout);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SchoolNewsDetailPage.this, ParentDashboard.class);
        startActivity(i);
        finish();
    }
}
