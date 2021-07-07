package in.aceventura.evolvuschool;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

public class EvolvuUpdateDetailPage extends AppCompatActivity {

    TextView update_title, update_description;
    String update_img_url,name,newUrl,dUrl,update_id;
    private List<DataSet> updateImages = new ArrayList<>();
    DatabaseHelper mDatabaseHelper;
    CardView cardUpdate;
    ImageSlider image_slider;
    List <SlideModel> slideModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolvu_update_detail_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIds();


        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            update_title.setText(bundle.getString("update_title"));
            update_description.setText(bundle.getString("update_description"));
            update_img_url = bundle.getString("update_img_url");
            update_id = bundle.getString("update_id");
        }


        JSONArray image_list = null;
        try {
            image_list = new JSONArray(update_img_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert image_list != null;
        for (int i = 0; i < image_list.length(); i++) {
            try {
                JSONObject obj = image_list.getJSONObject(i);
                String image_name = obj.getString("image_name");
                DataSet updateImageName = new DataSet();
                updateImageName.setUpdateImages(image_name);
                updateImages.add(updateImageName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (image_list.length() > 0) {
            cardUpdate.setVisibility(View.VISIBLE);
            showImages();
        } else {
            cardUpdate.setVisibility(View.GONE);
        }
    }//OnCreate


    private void showImages() {
            if (updateImages.size() > 0) {
                String img_path;
                for (int l = 0; l < updateImages.size(); l++) {
                    String update_img_name = updateImages.get(l).getUpdateImages();
                    if (name.equals("SACS")) {
                        img_path = dUrl + "uploads/evolvu_updates/" + update_id+ "/" + update_img_name;
                    } else {
                        img_path = dUrl + "uploads/" + name + "/evolvu_updates/" + update_id+ "/" + update_img_name;
                    }
                    slideModels.add(new SlideModel(img_path));
                }
                image_slider.setImageList(slideModels,true);

            }
    }

    private void getIds() {
        update_title = findViewById(R.id.update_title);
        update_description = findViewById(R.id.update_description);
        cardUpdate= findViewById(R.id.cardUpdate);
        image_slider= findViewById(R.id.image_slider);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EvolvuUpdateDetailPage.this, ParentDashboard.class);
        startActivity(i);
        finish();
    }
}
