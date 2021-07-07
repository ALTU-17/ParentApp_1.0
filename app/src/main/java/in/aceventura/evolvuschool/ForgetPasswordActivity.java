package in.aceventura.evolvuschool;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

public class ForgetPasswordActivity extends AppCompatActivity {
    String Sname;
    DatabaseHelper mDatabaseHelper;
    String name, newUrl, dUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Forget Password");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
//        Sname = (SharedPrefManager.getInstance(this).getShortname());
//        mDatabaseHelper = new DatabaseHelper(this);
//        name = mDatabaseHelper.getName(1);

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);


        if (name != null) {
            switch (name) {
                case "SACS":
                    WebView mywebview = findViewById(R.id.webView);
//                    mywebview.loadUrl("https://aceventura.in/SACS/index.php/login/forgot_password");
                    mywebview.loadUrl("https://aceventura.in/demo/SACSv4test/index.php/login/forgot_password");
                    mywebview.setWebChromeClient(new WebChromeClient());
                    mywebview.getSettings().setJavaScriptEnabled(true);
                    mywebview.setHorizontalScrollBarEnabled(false);
                    mywebview.setVerticalScrollBarEnabled(false);
                    break;

                case "SFSNE":
                    WebView mywebview1 = findViewById(R.id.webView);
//                    mywebview1.loadUrl("https://www.evolvu.in/sfsne/index.php/login/forgot_password");
                    mywebview1.loadUrl("https://www.evolvu.in/TEST_SFS/index.php/login/forgot_password");
                    mywebview1.setWebChromeClient(new WebChromeClient());
                    mywebview1.getSettings().setJavaScriptEnabled(true);
                    mywebview1.setHorizontalScrollBarEnabled(false);
                    mywebview1.setVerticalScrollBarEnabled(false);
                    break;

                case "SFSNW":
                    WebView mywebview2 = findViewById(R.id.webView);
//                    mywebview2.loadUrl("https://www.evolvu.in/sfsnw/index.php/login/forgot_password");
                    mywebview2.loadUrl("https://www.evolvu.in/TEST_SFS/index.php/login/forgot_password");
                    mywebview2.setWebChromeClient(new WebChromeClient());
                    mywebview2.getSettings().setJavaScriptEnabled(true);
                    mywebview2.setHorizontalScrollBarEnabled(false);
                    mywebview2.setVerticalScrollBarEnabled(false);
                    break;

                case "SJSKW":
                    WebView mywebview3 = findViewById(R.id.webView);
//                    mywebview3.loadUrl("https://www.evolvu.in/sjskw/index.php/login/forgot_password");
                    mywebview3.loadUrl("https://www.evolvu.in/TEST_SFS/index.php/login/forgot_password");
                    mywebview3.setWebChromeClient(new WebChromeClient());
                    mywebview3.getSettings().setJavaScriptEnabled(true);
                    mywebview3.setHorizontalScrollBarEnabled(false);
                    mywebview3.setVerticalScrollBarEnabled(false);
                    break;
            }
        }
    }
}
