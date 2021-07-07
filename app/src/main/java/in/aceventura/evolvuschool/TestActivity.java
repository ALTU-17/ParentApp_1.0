package in.aceventura.evolvuschool;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
//    EditText et_fname, et_foccupation, et_faddress, et_ftelephone, et_fmobile, et_femail;
//    EditText et_adharno, et_mname, et_moccupation, et_maddress, et_mtelephone, et_mmobile, et_memail;
//    Button btnUpdate, btnCancel;
//    TextView textViewResult;

    private static final String TAG = TestActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        textViewResult = (TextView)findViewById(R.id.textView3);
//
//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => "+c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = df.format(c.getTime());
//        // formattedDate have current date/time
//        Toast.make SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
//        Date todayDate = new Date();
//        String thisDate = currentDate.format(todayDate);
//        textViewResult.setText(thisDate);Text(this, formattedDate, Toast.LENGTH_SHORT).show();
//
//
//       // test();
        // txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        //txtMessage = (TextView) findViewById(R.id.txt_push_message);

    }


}
