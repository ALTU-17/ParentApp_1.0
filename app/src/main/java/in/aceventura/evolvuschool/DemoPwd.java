package in.aceventura.evolvuschool;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aceventura.evolvuschool.adapters.ListModel;

public class DemoPwd extends AppCompatActivity {
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_pwd);
        requestQueue = Volley.newRequestQueue(getBaseContext());

        StringRequest stringRequestLinks = new StringRequest(Request.Method.POST, "https://www.aceventura.in/demo/Test_ParentAppService/change_password",
                System.out::println,
                error -> {

                    error.printStackTrace();
                    Log.i("", "get_links: " + error.getMessage());
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", "SACS");
                params.put("user_id", "dsouza.francis@gmail.com");
                params.put("answerone", "liz");
                params.put("password_old", "arnolds");
                params.put("password_new", "arnolds@123");
                params.put("password_re", "arnolds@123");
                return params;
            }
        };
        RequestHandler.getInstance(DemoPwd.this).addToRequestQueue(stringRequestLinks);


    }
}
