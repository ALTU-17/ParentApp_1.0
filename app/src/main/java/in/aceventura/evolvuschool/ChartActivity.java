package in.aceventura.evolvuschool;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.bottombar.MyCalendar;
import in.aceventura.evolvuschool.models.Detail_result;
import in.aceventura.evolvuschool.models.LineChartArray;
import in.aceventura.evolvuschool.models.LineChartModel;

/**
       Todo Created BY Ashif  - 01/10/2021
      imp Notes: All Chart Contain only Floting Values
      Name :Shaikh Ashif Shabbir
      Files Contain: 1) Pie Chart
                     2) Line Chart
                     3) Bar Chart
      Desc :1) Entery Is Pointing Plot
            2) Data set is Carry All Entery
      Desc: 1)Pie Chart Alwalys showing 100% :1.1)CerterText
                                              1.2)Point Entery Data lebale and Pecentage
            2) Line Chart :Postion with X and Y axis
                           Data plot with X value for String ValeFormater
            3)Bar Char : Contain two type of data like 1.Leable and Is 2.Value
*/

public class ChartActivity extends AppCompatActivity {
    private LineChart mChart;
    // ArrayList<ILineDataSet> dataSets1;

    /**
       String PiechartData = "{\n" +
            "  \"pie_center_text\": \"Score of Term 1\",\n" +
            "  \"dataname\": \"Subject List\",\n" +
            "  \"piechartdata\": {\n" +
            "    \"data\": [\n" +
            "      {\n" +
            "        \"Subject_Marks\": \"5\",\n" +
            "        \"Subject_Name\": \"English\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Subject_Marks\": \"15\",\n" +
            "        \"Subject_Name\": \"Marathi\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Subject_Marks\": \"20\",\n" +
            "        \"Subject_Name\": \"Hindi\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Subject_Marks\": \"40\",\n" +
            "        \"Subject_Name\": \"Geography\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"Subject_Marks\": \"20\",\n" +
            "        \"Subject_Name\": \"Math\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
    String values = "[\n" +
            "  {\n" +
            "    \"Exam_name\": \"Periodic 1\",\n" +
            "    \"Details\": [\n" +
            "      \"{\\\"Subject\\\":\\\"English\\\",\\\"Percentage\\\":\\\"60\\\" ,\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"Hindi\\\",\\\"Percentage\\\":\\\"50\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"Maths\\\",\\\"Percentage\\\":\\\"70\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"EVS\\\",\\\"Percentage\\\":\\\"66\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"Marathi_rcs\\\",\\\"Percentage\\\":\\\"59\\\",\\\"Color\\\":\\\"#212121\\\"}\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"Exam_name\": \"Test 1\",\n" +
            "    \"Details\": [\n" +
            "      \"{\\\"Subject\\\":\\\"English\\\",\\\"Percentage\\\":\\\"47\\\" ,\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"Hindi\\\",\\\"Percentage\\\":\\\"70\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"Maths\\\",\\\"Percentage\\\":\\\"60\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"EVS\\\",\\\"Percentage\\\":\\\"56\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\",\n" +
            "      \"{\\\"Subject\\\":\\\"Marathi_rcs\\\",\\\"Percentage\\\":\\\"54\\\",\\\"Color\\\":\\\"#3F51B5\\\"}\"\n" +
            "    ]\n" +
            "  }\n" +
            "]";*/

    /**
      String values = "[\n" +
             "  {\n" +
             "    \"Exam_name\": \"Peri1\",\n" +
             "    \"Details\": [\n" +
             "      \"{\\\"Subject\\\":\\\"English\\\",\\\"Percentage\\\":\\\"60\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Hindi\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Maths\\\",\\\"Percentage\\\":\\\"70\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"EVS\\\",\\\"Percentage\\\":\\\"66\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Marathi_rcs\\\",\\\"Percentage\\\":\\\"59\\\"}\"\n" +
             "    ]\n" +
             "  },\n" +
             "  {\n" +
             "    \"Exam_name\": \"Test1\",\n" +
             "    \"Details\": [\n" +
             "      \"{\\\"Subject\\\":\\\"English\\\",\\\"Percentage\\\":\\\"65\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Hindi\\\",\\\"Percentage\\\":\\\"40\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Maths\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"EVS\\\",\\\"Percentage\\\":\\\"99\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Marathi_rcs\\\",\\\"Percentage\\\":\\\"69\\\"}\"\n" +
             "    ]\n" +
             "  },\n" +
             "  {\n" +
             "    \"Exam_name\": \"Test2\",\n" +
             "    \"Details\": [\n" +
             "      \"{\\\"Subject\\\":\\\"English\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Hindi\\\",\\\"Percentage\\\":\\\"60\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Maths\\\",\\\"Percentage\\\":\\\"75\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"EVS\\\",\\\"Percentage\\\":\\\"68\\\"}\",\n" +
             "      \"{\\\"Subject\\\":\\\"Marathi_rcs\\\",\\\"Percentage\\\":\\\"80\\\"}\"\n" +
             "    ]\n" +
             "  }\n" +
             "]";
 */


    LineDataSet set2;
    LineDataSet set3;
    String classid, sectionid, Sname, sid, pid;

    //Pie Chart
    private PieChart pieChart;

    //bar chart

    BarChart barChart;
    BarData data;
    BarDataSet barDataSet;
    String newUrl, dUrl, name;
    DatabaseHelper mDatabaseHelper;
    String response = "[\n" +
            "  {\n" +
            "    \"Details\": [\n" +
            "      \"{\\\"Academic Year\\\":\\\"2009-2010\\\",\\\"Percentage\\\":\\\"75.2\\\"}\",\n" +

           "      \"{\\\"Academic Year\\\":\\\"2009-2010\\\",\\\"Percentage\\\":\\\"75\\\"}\",\n" +

            "      \"{\\\"Academic Year\\\":\\\"2010-2011\\\",\\\"Percentage\\\":\\\"75.2\\\"}\"\n" +
           /* "      \"{\\\"Academic Year\\\":\\\"2011-2012\\\",\\\"Percentage\\\":\\\"40\\\"}\"\n" +*/
           /* "      \"{\\\"Academic Year\\\":\\\"2012-2013\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
            "      \"{\\\"Academic Year\\\":\\\"2013-2014\\\",\\\"Percentage\\\":\\\"60\\\"}\"\n" +*/
            /*"      \"{\\\"Academic Year\\\":\\\"2014-2015\\\",\\\"Percentage\\\":\\\"70\\\"}\",\n" +
            "      \"{\\\"Academic Year\\\":\\\"2015-2016\\\",\\\"Percentage\\\":\\\"80\\\"}\",\n" +
            "      \"{\\\"Academic Year\\\":\\\"2016-2017\\\",\\\"Percentage\\\":\\\"85\\\"}\"\n" +*/
            "    ]\n" +
            "  }\n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mDatabaseHelper = new DatabaseHelper(this);

        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            classid = bundle.getString("CLASSID");
            sectionid = bundle.getString("SECTIONID");
            sid = bundle.getString("SID");
            pid = bundle.getString("PID");
        }
        mChart = findViewById(R.id.chart1);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        ChartMarkerView mv = new ChartMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        renderData();
        pieChart = findViewById(R.id.activity_main_piechart);
        barChart = findViewById(R.id.severityBarChart);
   //     getBarChartData(response);

        setupPieChart();
        // loadPieChartData(PiechartData);
        //  loadPieChartData();
        getChartMarksPieChart();
        showBarChart();
        // getChartMarksLineChart1(values);

        String values = "[\n" +
                "  {\n" +
                "    \"Exam_name\": \"Term 1\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"54\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"51\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"56\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"57\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"58\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Marathi\\\",\\\"Percentage\\\":\\\"39\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Sanskrit\\\",\\\"Percentage\\\":\\\"37\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"42\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Exam_name\": \"Periodic Test 2\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"49\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"49\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"49\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Exam_name\": \"Term 2\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"86\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"85\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"80\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"84\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"88\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"41\\\"}\"\n" +
                "    ]\n" +
                "  }\n" +
                "]";


        String lll = "[\n" +
             "  {\n" +
                "    \"Exam_name\": \"Term 1\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"76\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"78\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"81\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"85\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"70\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Marathi\\\",\\\"Percentage\\\":\\\"40\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Sanskrit\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"35\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Exam_name\": \"Periodic Test 2\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"55\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"58\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"60\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"65\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
              "  {\n" +
                "    \"Exam_name\": \"Term 2\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"40\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"45\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"33\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"35\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"61\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"47\\\"}\"\n" +
                "    ]\n" +
                "  }" +
                "]";


     //getChartMarksLineChart24(lll);
        String valueLineChart = "[\n" +
                "  {\n" +
                "    \"Exam_name\": \"Term 1\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"33\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"44\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"55\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"66\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"55\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Marathi\\\",\\\"Percentage\\\":\\\"44\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Sanskrit\\\",\\\"Percentage\\\":\\\"33\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"22\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Exam_name\": \"Periodic Test 2\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"24\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"34\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"49\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"34\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"24\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Exam_name\": \"Term 2\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"19\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"29\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"39\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"49\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"59\\\"}\",\n" +
                "      \"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"69\\\"}\"\n" +
                "    ]\n" +
                "  }\n" +
                "]";
        String Valuesfrom = "[\n" +
                "  {\n" +
                "    \"Subject\": \"English\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"66\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Perodic Test 2\\\",\\\"Percentage\\\":\\\"76\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 2\\\",\\\"Percentage\\\":\\\"45.3\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Subject\": \"Hindi\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"15.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Perodic Test 2\\\",\\\"Percentage\\\":\\\"55.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 2\\\",\\\"Percentage\\\":\\\"42.3\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Subject\": \"Maths\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"15.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Perodic Test 2\\\",\\\"Percentage\\\":\\\"56.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 2\\\",\\\"Percentage\\\":\\\"65.3\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Subject\": \"Science\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"70.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Perodic Test 2\\\",\\\"Percentage\\\":\\\"59.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 2\\\",\\\"Percentage\\\":\\\"45.3\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Subject\": \"Social Studies\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"19.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Perodic Test 2\\\",\\\"Percentage\\\":\\\"65.3\\\"}\",\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 2\\\",\\\"Percentage\\\":\\\"70\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Subject\": \"Marathi\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"15.3\\\"}\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Subject\": \"Sanskrit\",\n" +
                "    \"Details\": [\n" +
                "      \"{\\\"Exam_name\\\":\\\"Term 1\\\",\\\"Percentage\\\":\\\"35.3\\\"}\"\n" +
                "    ]\n" +
                "  }\n" +
                "]";

       //  getChartMarksLineChart24(valueLineChart);


        //Top Bar
        View tb_main1 = findViewById(R.id.icd_tb_chart);
        TextView school_title = tb_main1.findViewById(R.id.school_title);
        TextView ht_Teachernote = tb_main1.findViewById(R.id.ht_Teachernote);
        TextView tv_academic_yr = tb_main1.findViewById(R.id.tv_academic_yr);
        ImageView ic_back = tb_main1.findViewById(R.id.ic_back);
        ImageView drawer = tb_main1.findViewById(R.id.drawer);
        tv_academic_yr.setText("(" + SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear() + ")");
        school_title.setText(name + " Evolvu Parent App");
        ht_Teachernote.setText("Chart");
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChartActivity.this, StudentDashboard.class);
                i.putExtra("CLASSID", classid);
                i.putExtra("SECTIONID", sectionid);
                i.putExtra("SID", sid);
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
            View view = findViewById(R.id.bb_remark);
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
                        Intent intent = new Intent(ChartActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }

                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ChartActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }

                }
            });
            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(int tabId) {

                    if (tabId == R.id.tab_calendar) {
                        Intent intent = new Intent(ChartActivity.this, MyCalendar.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_profile) {
                        Intent intent = new Intent(ChartActivity.this, ParentProfile.class);
                        startActivity(intent);
                    }
                    if (tabId == R.id.tab_dashboard) {

                        Intent intent = new Intent(ChartActivity.this, ParentDashboard.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bottomErrr", "wee" + e.getMessage());
        }
        ///

    }

    private void loadPieChartData(String piechartData) {
        try {
            JSONObject jsonObject1 = new JSONObject(piechartData);
            Log.e("pie_chart", "Json<>" + piechartData);
            String dataname = jsonObject1.getString("dataname");
            String pie_center_text = jsonObject1.getString("pie_center_text");
            Log.e("PieChartData", "markd<>" + jsonObject1.getString("pie_center_text"));
            JSONObject piechartdata = jsonObject1.getJSONObject("piechartdata");
            Log.e("PieChartData", "piechartdata<>" + piechartdata);
            JSONArray jsonArray = piechartdata.getJSONArray("data");
            ArrayList<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Log.e("PieChartData", "markd<>" + object.getString("Subject_Marks"));
                entries.add(new PieEntry(Float.parseFloat(object.getString("Subject_Marks")), object.getString("Subject_Name")));
            }
            ArrayList<Integer> colors = new ArrayList<>();
            for (int color : ColorTemplate.MATERIAL_COLORS) {
                colors.add(color);
            }

            for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color);
            }
            PieDataSet dataSet = new PieDataSet(entries, dataname);
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter(pieChart));
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);
            pieChart.setCenterText(pie_center_text);
            pieChart.setData(data);
            pieChart.invalidate();
            pieChart.animateY(1400, Easing.EaseInOutQuad);
        } catch (Exception e) {
            e.getMessage();
            Log.e("PieChartData", "ERIRIRI<>" + e.getMessage());
        }
    }

    private void showBarChart() {
      /*  ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = " Avg Obtain Marks";
        ArrayList<String> xAxisValues = new ArrayList<String>();
        xAxisValues.add("2001");
        xAxisValues.add("2002");
        xAxisValues.add("2003");
        xAxisValues.add("2004");
        xAxisValues.add("2005");
        xAxisValues.add("2006");
        xAxisValues.add("2007");
        xAxisValues.add("2008");
        xAxisValues.add("2009");
        xAxisValues.add("2010");
        xAxisValues.add("2011");
        xAxisValues.add("2012");
        xAxisValues.add("2013");
        xAxisValues.add("2014");
        xAxisValues.add("2015");

        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        //input data
       *//* for(int i = 0; i < 6; i++){
            valueList.add(i * 100.1);
        } */
        /*
        valueList.add(20.1);
        valueList.add(30.1);
        valueList.add(50.1);
        valueList.add(30.1);
        valueList.add(60.1);
        valueList.add(60.1);
        valueList.add(60.1);
        valueList.add(60.1);
        valueList.add(65.1);
        valueList.add(60.1);
        valueList.add(55.1);
        valueList.add(60.1);
        valueList.add(50.1);

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);

        }
        barDataSet = new BarDataSet(entries, title);
        data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
        // barChart.animateXY(2000, 2000);
        // llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1400, Easing.EaseInOutSine);

*/
        getChartMarksBarChart();
    }

    private void getChartMarksBarChart() {
        Log.e("BarChart", "URL>" + newUrl + "student_marks_for_bar_chart");
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "student_marks_for_bar_chart", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("BarChart", "response><>" + response);
                try {

                    try {
                          getBarChartData(response);

                    } catch (Exception e) {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("BarChart", "error message>>><>" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BarChart", "error><>" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", sid);
                //params.put("section_id", sectionid);
                //  params.put("class_id", classid);
                //params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                Log.e("BarChart", "params>" + params);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }

    private void getBarChartData(String response) {
        try {
            Log.e("BarChart", "00000");

            JSONArray jsonArray = new JSONArray(response);
            String dataname = "Percentage Academic Year Wise";
            ArrayList<String> academicyear = new ArrayList<>();
            ArrayList<String> percentage = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                int m = 0;
                JSONObject object = new JSONObject(jsonArray.get(i).toString());
                JSONArray Details = object.getJSONArray("Details");
                Log.e("BarChart", "idex?" + Details.length());
                for (int k = 0; k < Details.length(); k++) {
                    m++;
                    Log.e("BarChart", "idex?" + m + "<>M<>+" + k + "K" + Details.length());

                    JSONObject jsonObjectdetails = new JSONObject(Details.get(k).toString());
                    String name = jsonObjectdetails.getString("Academic Year");
                    String aa[] = name.split("-");
                    ;
                    Log.e("BarChart", "AcademicSplit?" + aa[0] + aa[1]);
                    String acadamicYr = aa[0].substring(2) + "-" + aa[1].substring(2);

                    if (k == 0) {
                        Log.e("BarChart", "AcademicSplitKM?" + aa[0]);
                    }
                    if (m == Details.length()) {
                        Log.e("BarChart", "AcademicSplitKM?" + aa[1]);

                    }
                    academicyear.add(acadamicYr);

                    percentage.add(jsonObjectdetails.getString("Percentage"));
                    Log.e("BarChart", "Academic?" + jsonObjectdetails.getString("Academic Year"));
                    Log.e("BarChart", "Percentage?" + jsonObjectdetails.getString("Percentage"));

                }

            }
            databarchart(academicyear, percentage, dataname);
        } catch (Exception e) {

        }
    }

    private void databarchart(ArrayList<String> academicyear, ArrayList<String> percentage, String dataname) {
        Log.e("BarChart", "11111");
        ArrayList<String> xAxisValues = new ArrayList<String>();
        int m = 0;
        int xmax = academicyear.size();
        for (int i = 0; i < academicyear.size(); i++) {
            ;
            Log.e("BarChart", "22222" + academicyear.get(i));
            xAxisValues.add(academicyear.get(i));
            m++;

        }

        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(academicyear));

        // barChart.setVisibleXRange(0, xmax);
        Log.e("BarChart", "size" + xmax);

        ArrayList<Double> valueList = new ArrayList<Double>();

        for (int k = 0; k < percentage.size(); k++) {
            Log.e("BarChart", "33333" + Double.valueOf(percentage.get(k)));
            valueList.add(Double.valueOf(percentage.get(k)));
            Log.e("abcd", "percentage=" + percentage.get(k));

        }
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = dataname;
        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            Log.e("abcd", "data into a bar=" + i + "" + valueList.get(i).floatValue());
            Log.e("BarChart", "44444" + valueList.get(i).floatValue());

            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);

        }
        XAxis xAxis = barChart.getXAxis();
      //  barChart.getXAxis().setCenterAxisLabels(true);
       /* barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(xmax);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(xmax);
*/
        barDataSet = new BarDataSet(entries, title);
        xAxis.setGranularityEnabled(true);
        data = new BarData(barDataSet);
        //  data.setBarWidth(0.5f);
       // barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getDescription().setText("");

        barChart.setData(data);
        barChart.invalidate();
        // barDataSet.setColors(getColors());
        // barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // barChart.animateXY(2000, 2000);
        // llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        //  barDataSet.setColor(Color.rgb(193  ,143 ,160));


        /*int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
        int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
        int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
        int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
        int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
        int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
        int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
        int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
        int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
        int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);


       ArrayList<Integer> integers=new ArrayList<>();
        integers.add(startColor1);
        integers.add(startColor2);
        integers.add(startColor3);
        integers.add(startColor4);
        integers.add(startColor5);
        integers.add(endColor1);
        integers.add(endColor2);
        integers.add(endColor3);
        integers.add(endColor4);
        integers.add(endColor5);


        barDataSet.setColors(integers);*/


        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(1400, Easing.EaseInOutSine);
        barChart.notifyDataSetChanged();

    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Score of previous Year");
        pieChart.setCenterTextSize(8);
        pieChart.getDescription().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(5f, "English"));
        entries.add(new PieEntry(15f, "Marathi"));
        entries.add(new PieEntry(20f, "Hindi"));
        entries.add(new PieEntry(40f, "Geography"));
        entries.add(new PieEntry(20f, "Math"));
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }
        PieDataSet dataSet = new PieDataSet(entries, "Acadamic Year");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void renderData() {

        LimitLine llXAxis = new LimitLine(20f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(15f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLimitLinesBehindData(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();

        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        mChart.getAxisRight().setEnabled(false);
        setData1();
    }

    private void setData1() {
        getChartMarksLineChart();


      /*  ArrayList<String> xAxisValues = new ArrayList<String>();
        xAxisValues.add("Term1");
        xAxisValues.add("Term2");
        xAxisValues.add("Term3");
        xAxisValues.add("Term4");
        xAxisValues.add("Term5");
        xAxisValues.add("Term6");

        mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(0, 60));//20    2018f  (2018f, 20))
        entries.add(new Entry(1, 57));//40    2019f   (2019f, 40))
        entries.add(new Entry(2, 65));//60    2020f (2020f, 60))
        entries.add(new Entry(3, 70));//80    2021f (2021f, 80))
        entries.add(new Entry(4, 80));//80    2021f (2021f, 80))
        entries.add(new Entry(5, 70));//80    2021f (2021f, 80))


        ArrayList<Entry> entry = new ArrayList<>();
        entry.add(new Entry(0, 70));
        entry.add(new Entry(1, 50));
        entry.add(new Entry(2, 60));
        entry.add(new Entry(3, 65));
        entry.add(new Entry(4, 75));
        entry.add(new Entry(5, 80));

        ArrayList<Entry> marathi = new ArrayList<>();
        marathi.add(new Entry(0, 80));
        marathi.add(new Entry(1, 70));
        marathi.add(new Entry(2, 50));
        marathi.add(new Entry(3, 68));
        marathi.add(new Entry(4, 55));
        marathi.add(new Entry(5, 36));


        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();

        LineDataSet set1 = new LineDataSet(entries, "English");
        set1.setDrawFilled(true);
        set1.setFillColor(Color.WHITE);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.DKGRAY);
        lines.add(set1);

        set2 = new LineDataSet(entry, "Hindi");
        set2.setDrawFilled(true);
        set2.setFillColor(Color.WHITE);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.RED);
        lines.add(set2);

        set3 = new LineDataSet(marathi, "Marathi");
        set3.setDrawFilled(true);
        set3.setFillColor(Color.WHITE);
        set3.setColor(Color.YELLOW);
        set3.setCircleColor(Color.parseColor("#8B0000"));
        lines.add(set3);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        mChart.setData(new LineData(dataSets));

        mChart.getDescription().setText("");

        mChart.getDescription().setTextColor(Color.RED);


        mChart.animateY(1400, Easing.EaseInOutBounce);

*/
        //getChartMarksLineChart1(values);
    }

    @SuppressLint("Range")
    private void getChartMarksLineChart1(String s) {
        try {
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(s.replace("ï»¿", ""));
            Log.e("linecart", "jsonArrayCONVEYR>>" + jsonArray);
            ArrayList<String> xAxisValues = new ArrayList<String>();
            String mExam = "";

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.e("linecart", "Exam_name" + jsonObject.getString("Exam_name"));
                    //       mExam = jsonObject.getString("Exam_name");
                    Log.e("linecart", "EXA><" + mExam);
                    xAxisValues.add(mExam);
                    mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                    JSONArray jsonArray1 = jsonObject.getJSONArray("Details");
                    Log.e("linecart", "DetailsjsonArray1><" + jsonArray1);
                    ArrayList<Entry> values = new ArrayList<>();
                    LineDataSet set1 = null;
                    String Colorm = "";
                    int jj = 0;
                    ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject1 = new JSONObject(jsonArray1.get(j).toString());
                        values.add(new Entry(j, Float.parseFloat(jsonObject1.getString("Percentage"))));
                        jj++;
                        set1 = new LineDataSet(values, jsonObject1.getString("Subject"));
                        Log.e("linecart", "Colorm><" + Colorm);
                        lines.add(set1);
                        dataSets.add(set1);
                        mChart.setData(new LineData(dataSets));
                        mChart.invalidate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("linecart", "ERRRRe>values<" + e.getMessage());
                } catch (Exception e) {
                    Log.e("linecart", "allerr>values<" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("linecart", "allerr>values<" + e.getMessage());
        }
    }

    private void getChartMarksLineChart() {
        Log.e("linecart", "URL>" + newUrl + "student_marks_details_for_line_chart");
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "student_marks_details_for_line_chart", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("linecart", "response><>" + response);
                try {
                     getChartMarksLineChart24(response);
                    //getChartMarksLineChart1(response);
                    //getChartMarksLineChart23(response);

                    //
                   /* String Valuesfrom="[{\"Exam_name\":\"Term 1\",\"Details\":[\"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"54\\\"}\",\"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"51\\\"}\",\"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"56\\\"}\",\"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"57\\\"}\",\"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"58\\\"}\",\"{\\\"Subject\\\":\\\"Marathi\\\",\\\"Percentage\\\":\\\"39\\\"}\",\"{\\\"Subject\\\":\\\"Sanskrit\\\",\\\"Percentage\\\":\\\"37\\\"}\",\"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"42\\\"}\"]},{\"Exam_name\":\"Periodic Test 2\",\"Details\":[\"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"49\\\"}\",\"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"49\\\"}\",\"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"49\\\"}\"]},{\"Exam_name\":\"Term 2\",\"Details\":[\"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"86\\\"}\",\"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"85\\\"}\",\"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"80\\\"}\",\"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"84\\\"}\",\"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"88\\\"}\",\"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"41\\\"}\"]}]";

                     getChartMarksLineChart24(Valuesfrom);*/
                    // newLineChart(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("linecart", "error message>>><>" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("linecart", "error><>" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", sid);
                params.put("class_id", classid);
             //   params.put("section_id", sectionid);
                params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                Log.e("linecart", "params>" + params);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void newLineChart(String response) {


    }

    private void getChartMarksLineChart24(String response) {
        Log.e("abcd", "getChartMarksLineChart24 caaled");

        try {
            JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
            ArrayList<LineChartArray> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LineChartArray lineChartArray = new LineChartArray();
                lineChartArray.setExamName(jsonObject.getString("Exam_name"));
                ArrayList<LineChartModel> lineChartModels = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject.getJSONArray("Details");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = new JSONObject(jsonArray1.get(j).toString());
                    String subjectLst = jsonObject1.getString("Subject");
                    String Percentage = jsonObject1.getString("Percentage");
                    LineChartModel lineChartModel = new LineChartModel();
                    lineChartModel.setmPercentage(Percentage);
                    lineChartModel.setmSubject(subjectLst);
                    lineChartModels.add(lineChartModel);
                }
                lineChartArray.setLineChartModels(lineChartModels);
                list.add(lineChartArray);
            }

            String buffer = new String();
            HashSet hashSet = new HashSet();
            for (LineChartArray ll : list) {
                Log.e("abcd", "exam name=" + ll.getExamName());
                for (LineChartModel lm : ll.getLineChartModels()) {
                    buffer = lm.getmSubject();
                    Log.e("abcd", "subject name=" + lm.getmSubject() + " percentage" + lm.getmPercentage());
                    hashSet.add(buffer.toString());
                }
            }
            setData2(list);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("abcd", "Exception caaled");

        }

    }

    private void setData2(ArrayList<LineChartArray> list, HashSet<String> lll) {
        ArrayList<String> subjectList = new ArrayList<>();
        try {
            ArrayList<String> substrings = new ArrayList<>();
            substrings.addAll(lll);
            for (String sss : lll) {
                Log.e("abbcd", "subject List?" + sss);
                subjectList.add(sss);
            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("abbcd", "subject List?" + e.getMessage());
        }


        ArrayList<String> examList = new ArrayList<>();


        ArrayList<Entry> entriesEnglish_rc = new ArrayList<>();
        ArrayList<Entry> entriesHindi_rc = new ArrayList<>();
        ArrayList<Entry> entriesMaths_rc = new ArrayList<>();
        ArrayList<Entry> entriesScience = new ArrayList<>();
        ArrayList<Entry> entriesSocialScience = new ArrayList<>();
        ArrayList<Entry> entriesMarathi = new ArrayList<>();
        ArrayList<Entry> entriesSanskrit = new ArrayList<>();
        ArrayList<Entry> entriesComputerApplications = new ArrayList<>();


        for (int k = 0; k < list.size(); k++) {
            LineChartArray ll = list.get(k);
            Log.e("abcd", "exam name=" + ll.getExamName());
            //  Log.e("abcd", "SubjectName=inloop?=" + ll.getLineChartModels().get(k).getmSubject());
            examList.add(ll.getExamName());
            Log.e("abcd", "values><?" + ll.getExamName());
            Log.e("abcd", "SizeOFK><?" + k);

            for (LineChartModel lm : ll.getLineChartModels()) {
                Log.e("abcd", "subject name=" + lm.getmSubject() + " percentage" + lm.getmPercentage());
                if (lm.getmSubject().equalsIgnoreCase("English_rc") || lm.getmSubject().equalsIgnoreCase("English")) {
                    entriesEnglish_rc.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Hindi_rc") || lm.getmSubject().equalsIgnoreCase("Hindi")) {
                    entriesHindi_rc.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Maths_rc") || lm.getmSubject().equalsIgnoreCase("Maths")) {
                    entriesMaths_rc.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Science")) {
                    entriesScience.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Social Science")) {
                    entriesSocialScience.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Marathi")) {
                    entriesMarathi.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Sanskrit")) {
                    entriesSanskrit.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Computer Applications")) {
                    entriesComputerApplications.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }

            }
        }

        mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(examList));
        mChart.getLegend().setWordWrapEnabled(true);

        mChart.fitScreen();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.TOP;
        mChart.getXAxis().setPosition(position);
        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int m = 0; m < subjectList.size(); m++) {
            String sub = subjectList.get(m);
            LineDataSet set1 = null;
            if (sub.equalsIgnoreCase("English_rc") || sub.equalsIgnoreCase("English")) {
                set1 = new LineDataSet(entriesEnglish_rc, sub);
            }
            if (sub.equalsIgnoreCase("Hindi_rc") || sub.equalsIgnoreCase("Hindi")) {
                set1 = new LineDataSet(entriesHindi_rc, sub);
            }
            if (sub.equalsIgnoreCase("Maths_rc") || sub.equalsIgnoreCase("Maths")) {
                set1 = new LineDataSet(entriesMaths_rc, sub);
            }
            if (sub.equalsIgnoreCase("Science")) {
                set1 = new LineDataSet(entriesScience, sub);
            }
            if (sub.equalsIgnoreCase("Social Science")) {
                set1 = new LineDataSet(entriesSocialScience, sub);
            }
            if (sub.equalsIgnoreCase("Marathi")) {
                set1 = new LineDataSet(entriesMarathi, sub);
            }
            if (sub.equalsIgnoreCase("Sanskrit")) {
                set1 = new LineDataSet(entriesSanskrit, sub);
            }
            if (sub.equalsIgnoreCase("Computer Applications")) {
                set1 = new LineDataSet(entriesComputerApplications, sub);
            }
           /*if(sub.equalsIgnoreCase(sub)){
                set1 = new LineDataSet(entriesComputerApplications, sub);

            }*/
            set1.setDrawFilled(true);
            set1.setFillColor(Color.WHITE);//todo background color
            if (m == 0) {
                set1.setColor(Color.RED);//1//todo line color
                set1.setCircleColor(Color.RED);//todo circle color
            } else if (m == 1) {
                set1.setCircleColor(Color.GREEN);
                set1.setColor(Color.GREEN);//2
            } else if (m == 2) {
                set1.setCircleColor(Color.BLACK);
                set1.setColor(Color.BLACK);//3
            } else if (m == 3) {
                set1.setCircleColor(Color.BLUE);
                set1.setColor(Color.BLUE);//4
            } else if (m == 4) {
                set1.setCircleColor(Color.YELLOW);
                set1.setColor(Color.YELLOW);
            } else if (m == 5) {
                set1.setCircleColor(Color.parseColor("#800080"));
                set1.setColor(Color.parseColor("#800080"));
                // purple
            } else if (m == 6) {
                set1.setCircleColor(Color.parseColor("#FF00FF"));
                set1.setColor(Color.parseColor("#FF00FF"));
                //fuchsia
            } else if (m == 7) {
                //set1.setCircleColor(Color.parseColor("##f0e68c"));
                //set1.setColor(Color.parseColor("##f0e68c"));
                //  khaki
            } else if (m == 8) {
                //set1.setCircleColor(Color.parseColor("#c71585"));
                //set1.setColor(Color.parseColor("#c71585"));
                //mediumvioletred
            } else if (m == 9) {
            } else if (m == 10) {
            }
            //set1.setCircleColor(Color.DKGRAY);
            lines.add(set1);
            lineDataSets.add(set1);
            dataSets.add(set1);
            Log.e("abcd", "mValues=" + m);
        }

        mChart.setData(new LineData(dataSets));

        mChart.getDescription().setText("");

        mChart.getDescription().setTextColor(Color.RED);

        mChart.setVisibleXRangeMaximum(100);
        mChart.animateY(1400, Easing.EaseInOutBounce);
        try {
            mChart.invalidate();
            mChart.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
       /* //

            TextView tv_tv1 = findViewById(R.id.tv_tv1), tv_tv2 = findViewById(R.id.tv_tv2),
                    tv_tv3 = findViewById(R.id.tv_tv3), tv_tv4 = findViewById(R.id.tv_tv4);
            StringBuffer stringBuffer = new StringBuffer();

            for (int t = 0; t < examList.size(); t++) {

                Log.e("abcd", "sujcetname==?>" + examList.get(t));
                stringBuffer.append(examList.get(t) + "             ");
                tv_tv1.setVisibility(View.VISIBLE);


            }
            tv_tv1.setText(stringBuffer);
*/
            // get_all_exams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//todo ashif comment

    private void setData2(ArrayList<LineChartArray> list) {


        Map<String, ArrayList<Entry>> map = new HashMap<>();


        for (LineChartArray ll : list) {
            Log.e("abcd", "exam name=" + ll.getExamName());
            for (LineChartModel lm : ll.getLineChartModels()) {
                map.put(lm.getmSubject(), new ArrayList<>());
                Log.e("abcd", "subject name=" + lm.getmSubject() + " percentage" + lm.getmPercentage());
            }
        }
        ArrayList<String> examList = new ArrayList<>();
        //HashSet<String> hashSet =new HashSet<>();
        ArrayList<String> integers=new ArrayList<>();

        for (Map.Entry<String, ArrayList<Entry>> e : map.entrySet()) {
            Log.e("abcd", "Key: " + e.getKey()
                    + " Value: " + e.getValue());
            String stringBuffer = new String();
            for (int k = 0; k < list.size(); k++) {

                LineChartArray ll = list.get(k);
                Log.e("abcd", "exam name=" + ll.getExamName());
                if(ll.getExamName().equalsIgnoreCase("Periodic Test 1")){
                    integers.add("Per.Test 1");
                }
                else if(ll.getExamName().equalsIgnoreCase("Periodic Test 2")){
                    integers.add("Per.Test 2");
                }else {
                    integers.add(ll.getExamName());
                }




                stringBuffer = ll.getExamName();
                //  examList.add(k,ll.getExamName());
                //hashSet.add(stringBuffer);
                for (LineChartModel lm : ll.getLineChartModels()) {

                    Log.e("abcd", "subject name=" + lm.getmSubject() + " percentage" + lm.getmPercentage());
                    if (lm.getmSubject().equalsIgnoreCase(e.getKey())) {
                        e.getValue().add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                    }

                }
            }

        }


        Log.e("abcd", "sujcetname==?>");


        //examList.addAll(hashSet);
        for (String dd : examList) {
            Log.e("ddddd00", "Vaalew>>" + dd);
        }


       /* integers.add("1");
        integers.add("2");
        integers.add("3");
        integers.add("4");
        integers.add("5");
        integers.add("6");*/
         mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(integers));


        XAxis.XAxisPosition position = XAxis.XAxisPosition.TOP;
        mChart.getXAxis().setPosition(position);


        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
       // get_all_exams();
        int m = 0;
        for (Map.Entry<String, ArrayList<Entry>> e : map.entrySet()) {
            Log.e("abcd", "Key2: " + e.getKey()
                    + " Value2: " + e.getValue());

            String sub = e.getKey();
            LineDataSet set1 = null;

            set1 = new LineDataSet(e.getValue(), sub);
            set1.setDrawFilled(true);
            set1.setFillColor(Color.parseColor("#00ffffff"));//todo background color
            if (m == 0) {
                set1.setColor(Color.RED);//1//todo line color
                set1.setCircleColor(Color.RED);//todo circle color
            } else if (m == 1) {
                set1.setCircleColor(Color.GREEN);
                set1.setColor(Color.GREEN);//2
            } else if (m == 2) {
                set1.setCircleColor(Color.BLACK);
                set1.setColor(Color.BLACK);//3
            } else if (m == 3) {
                set1.setCircleColor(Color.BLUE);
                set1.setColor(Color.BLUE);//4
            } else if (m == 4) {
                set1.setCircleColor(Color.YELLOW);
                set1.setColor(Color.YELLOW);
            } else if (m == 5) {
                set1.setCircleColor(Color.parseColor("#800080"));
                set1.setColor(Color.parseColor("#800080"));
                // purple
            } else if (m == 6) {
                set1.setCircleColor(Color.parseColor("#FF00FF"));
                set1.setColor(Color.parseColor("#FF00FF"));
                //fuchsia
            } else if (m == 7) {
                try {
                    set1.setCircleColor(Color.parseColor("##f0e68c"));
                    set1.setColor(Color.parseColor("##f0e68c"));
                    //  khaki


                } catch (Exception ee) {

                }

            } else if (m == 8) {
                //set1.setCircleColor(Color.parseColor("#c71585"));
                //set1.setColor(Color.parseColor("#c71585"));
                //mediumvioletred

            } else if (m == 9) {

            } else if (m == 10) {

            }
            //set1.setCircleColor(Color.DKGRAY);
            lines.add(set1);
            lineDataSets.add(set1);
            dataSets.add(set1);


            m++;
        }


        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularityEnabled(true);

        mChart.getLegend().setWordWrapEnabled(true);
        mChart.fitScreen();

        mChart.setData(new LineData(dataSets));

        mChart.getDescription().setText("");

        mChart.getDescription().setTextColor(Color.RED);


        mChart.animateY(1400, Easing.EaseInOutBounce);


    }

   /* private void setData3(ArrayList<LineChartArray> list, HashSet<String> lll) {
        ArrayList<String> subjectList = new ArrayList<>();
        try {
            ArrayList<String> substrings = new ArrayList<>();
            substrings.addAll(lll);
            for (String sss : lll) {
                Log.e("abbcd", "subject List?" + sss);
                subjectList.add(sss);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("abbcd", "subject List?" + e.getMessage());
        }
        ArrayList<String> examList = new ArrayList<>();
        int subl;
        ArrayList<Entry> subdyanic = new ArrayList<>();
        for (subl = 0; subl < subjectList.size(); subl++) {
            ArrayList<Entry> arrayList = new ArrayList();
            subdyanic.addAll(arrayList);
        }

        ArrayList<Entry> entriesEnglish_rc = new ArrayList<>();
        ArrayList<Entry> entriesHindi_rc = new ArrayList<>();
        ArrayList<Entry> entriesMaths_rc = new ArrayList<>();
        ArrayList<Entry> entriesScience = new ArrayList<>();

        ArrayList<Entry> entriesSocialScience = new ArrayList<>();
        ArrayList<Entry> entriesMarathi = new ArrayList<>();
        ArrayList<Entry> entriesSanskrit = new ArrayList<>();
        ArrayList<Entry> entriesComputerApplications = new ArrayList<>();
        for (int k = 0; k < list.size(); k++) {
            LineChartArray ll = list.get(k);
            Log.e("abcd", "exam name=" + ll.getExamName());
            //  Log.e("abcd", "SubjectName=inloop?=" + ll.getLineChartModels().get(k).getmSubject());
            examList.add(ll.getExamName());
            Log.e("abcd", "values><?" + ll.getExamName());
            Log.e("abcd", "SizeOFK><?" + k);
            for (LineChartModel lm : ll.getLineChartModels()) {
                Log.e("abcd", "subject name=" + lm.getmSubject() + " percentage" + lm.getmPercentage());

                if (lm.getmSubject().equalsIgnoreCase("English_rc") || lm.getmSubject().equalsIgnoreCase("English")) {
                    entriesEnglish_rc.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Hindi_rc") || lm.getmSubject().equalsIgnoreCase("Hindi")) {
                    entriesHindi_rc.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Maths_rc") || lm.getmSubject().equalsIgnoreCase("Maths")) {
                    entriesMaths_rc.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Science")) {
                    entriesScience.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Social Science")) {
                    entriesSocialScience.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Marathi")) {
                    entriesMarathi.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Sanskrit")) {
                    entriesSanskrit.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }
                if (lm.getmSubject().equalsIgnoreCase("Computer Applications")) {
                    entriesComputerApplications.add(new Entry(k, Float.valueOf(lm.getmPercentage())));
                }

            }
        }
        mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(examList));
        mChart.getLegend().setWordWrapEnabled(true);
        mChart.fitScreen();
        XAxis.XAxisPosition position = XAxis.XAxisPosition.TOP;
        mChart.getXAxis().setPosition(position);
        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int m = 0; m < subjectList.size(); m++) {
            String sub = subjectList.get(m);
            LineDataSet set1 = null;
            if (sub.equalsIgnoreCase("English_rc") || sub.equalsIgnoreCase("English")) {
                set1 = new LineDataSet(entriesEnglish_rc, sub);
            }
            if (sub.equalsIgnoreCase("Hindi_rc") || sub.equalsIgnoreCase("Hindi")) {
                set1 = new LineDataSet(entriesHindi_rc, sub);
            }
            if (sub.equalsIgnoreCase("Maths_rc") || sub.equalsIgnoreCase("Maths")) {
                set1 = new LineDataSet(entriesMaths_rc, sub);
            }

            if (sub.equalsIgnoreCase("Science")) {
                set1 = new LineDataSet(entriesScience, sub);
            }
            if (sub.equalsIgnoreCase("Social Science")) {
                set1 = new LineDataSet(entriesSocialScience, sub);
            }
            if (sub.equalsIgnoreCase("Marathi")) {
                set1 = new LineDataSet(entriesMarathi, sub);
            }
            if (sub.equalsIgnoreCase("Sanskrit")) {
                set1 = new LineDataSet(entriesSanskrit, sub);
            }
            if (sub.equalsIgnoreCase("Computer Applications")) {
                set1 = new LineDataSet(entriesComputerApplications, sub);
            }

            *//*if(sub.equalsIgnoreCase(sub)){
                set1 = new LineDataSet(entriesComputerApplications, sub);

            }*//*
            set1.setDrawFilled(true);
            set1.setFillColor(Color.parseColor("#00ffffff"));//todo background color
            if (m == 0) {
                set1.setColor(Color.RED);//1//todo line color
                set1.setCircleColor(Color.RED);//todo circle color
            } else if (m == 1) {
                set1.setCircleColor(Color.GREEN);
                set1.setColor(Color.GREEN);//2
            } else if (m == 2) {
                set1.setCircleColor(Color.BLACK);
                set1.setColor(Color.BLACK);//3
            } else if (m == 3) {
                set1.setCircleColor(Color.BLUE);
                set1.setColor(Color.BLUE);//4
            } else if (m == 4) {
                set1.setCircleColor(Color.YELLOW);
                set1.setColor(Color.YELLOW);
            } else if (m == 5) {
                set1.setCircleColor(Color.parseColor("#800080"));
                set1.setColor(Color.parseColor("#800080"));
                // purple
            } else if (m == 6) {
                set1.setCircleColor(Color.parseColor("#FF00FF"));
                set1.setColor(Color.parseColor("#FF00FF"));
                //fuchsia
            } else if (m == 7) {
                //set1.setCircleColor(Color.parseColor("##f0e68c"));
                //set1.setColor(Color.parseColor("##f0e68c"));
                //  khaki
                try {

                } catch (Exception e) {

                }
            } else if (m == 8) {
                //set1.setCircleColor(Color.parseColor("#c71585"));
                //set1.setColor(Color.parseColor("#c71585"));
                //mediumvioletred
                try {

                } catch (Exception e) {

                }
            } else if (m == 9) {
                try {

                } catch (Exception e) {

                }
            } else if (m == 10) {
                try {

                } catch (Exception e) {

                }
            }
            //set1.setCircleColor(Color.DKGRAY);
            lines.add(set1);
            lineDataSets.add(set1);
            dataSets.add(set1);
            Log.e("abcd", "mValues=" + m);
        }

        mChart.setData(new LineData(dataSets));

        mChart.getDescription().setText("");

        mChart.getDescription().setTextColor(Color.RED);

        mChart.setVisibleXRangeMaximum(100);
        mChart.animateY(1400, Easing.EaseInOutBounce);
        try {
            mChart.invalidate();
            mChart.notifyDataSetChanged();
            mChart.getXAxis().setDrawGridLines(true); // disable grid lines for the XAxis
            mChart.getAxisLeft().setDrawGridLines(true); // disable grid lines for the left YAxis
            mChart.getAxisRight().setDrawGridLines(true);
            mChart.setDrawGridBackground(true);


            mChart.setDragEnabled(true);
            mChart.setPinchZoom(true);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setHorizontalScrollBarEnabled(true);
            mChart.getViewPortHandler().setMaximumScaleX(5f);
            mChart.getViewPortHandler().setMaximumScaleY(5f);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        try {
            TextView tv_tv1 = findViewById(R.id.tv_tv1), tv_tv2 = findViewById(R.id.tv_tv2),
                    tv_tv3 = findViewById(R.id.tv_tv3), tv_tv4 = findViewById(R.id.tv_tv4);
            StringBuffer stringBuffer = new StringBuffer();

            for (int t = 0; t < examList.size(); t++) {

                Log.e("abcd", "sujcetname==?>" + examList.get(t));
                stringBuffer.append(examList.get(t) + "             ");
                //  tv_tv1.setVisibility(View.VISIBLE);
            }
            tv_tv1.setText(stringBuffer);

            get_all_exams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private void get_all_exams() {
        try {
            Log.e("get_current_exams", "url><>" + newUrl + "get_current_exams");
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            StringRequest request = new StringRequest(Request.Method.POST, newUrl + "get_current_exams", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("get_current_exams", "response><>" + response);
                    try {
                        ArrayList<String> strings = new ArrayList<>();
                        ;

                        JSONArray array = new JSONArray(response);
                        Log.e("get_current_exams", "array.length()><>" + array.length());
                        StringBuffer stringBuffer = new StringBuffer();

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = new JSONObject(array.get(i).toString());

                            stringBuffer.append(object.getString("name") + "              ");


                        }
                        String[] iii = new String[]{
                                stringBuffer.toString()
                        };

                        TextView tv_tv1 = findViewById(R.id.tv_tv1);

                        if (stringBuffer.length() > 0) {
                            try {

                                tv_tv1.setVisibility(View.VISIBLE);
                                tv_tv1.setText(stringBuffer);
                            } catch (Exception e) {
                                tv_tv1.setVisibility(View.GONE);

                                e.printStackTrace();
                            }

                        } else {
                            tv_tv1.setVisibility(View.GONE);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("get_current_exams", "error message>>><>" + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("get_current_exams", "error><>" + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("short_name", name);
                    params.put("student_id", sid);
                    params.put("class_id", classid);
                    /*      params.put("section_id", sectionid);*/
                    params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());
                    if (name == null || name.equals("")) {
                        name = mDatabaseHelper.getName(1);
                        newUrl = mDatabaseHelper.getURL(1);
                        dUrl = mDatabaseHelper.getPURL(1);
                    }
                    Log.e("get_current_exams", "params>" + params);
                    return params;
                }
            };
            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getChartMarksLineChart23(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response.replace("ï»¿", ""));
            Log.e("linecart", "jsonArrayCONVEYR>>" + jsonArray);
            String mExam;

            ArrayList<String> xAxisValues = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mExam = jsonObject.getString("Exam_name");
                    HashSet<String> examname = new HashSet<>();
                    JSONArray jsonArray1 = jsonObject.getJSONArray("Details");
                    List<String> colorcode = new ArrayList<>();
                    colorcode.add("#3DDC84");
                    colorcode.add("#FF0000");
                    colorcode.add("#4B53BC");
                    colorcode.add("#FFE866");
                    colorcode.add("#B73136");
                    xAxisValues.add(mExam);
                    mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
                    LineDataSet set1 = null;
                    ArrayList<String> strings = new ArrayList<>();


                    for (int j = 0; j < jsonArray1.length(); j++) {

                        JSONObject jsonObject1 = new JSONObject(jsonArray1.get(j).toString());
                        String subjectLst = jsonObject1.getString("Subject");
                        String Percentage = jsonObject1.getString("Percentage");
                        strings.add(subjectLst);
                        entries.add(new Entry(j, Float.parseFloat(Percentage)));


                    }
                    ArrayList<ILineDataSet> dataSets;
                    for (int kk = 0; kk < entries.size(); kk++) {
                        dataSets = new ArrayList<>();
                        set1 = new LineDataSet(entries, strings.get(kk));
                        lines.add(set1);
                        dataSets.add(set1);
                        mChart.setData(new LineData(dataSets));
                    }

                    //  mChart.invalidate();
                    // mChart.setVisibleXRangeMaximum(100);
                    mChart.moveViewToX(100);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("linecart", "ERRRRe>values<" + e.getMessage());
                } catch (Exception e) {
                    Log.e("linecart", "allerr>values<" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("linecart", "allerr>values<" + e.getMessage());
        }
    }

    private void getChartMarksPieChart() {
        Log.e("pie_chart", "url><>" + newUrl + "student_marks_details_for_pie_chart");
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "student_marks_details_for_pie_chart", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("pie_chart", "response><>" + response);
                try {
                    loadPieChartData1(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("pie_chart", "error message>>><>" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("pie_chart", "error><>" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("student_id", sid);
                params.put("class_id", classid);
                /*      params.put("section_id", sectionid);*/
                params.put("academic_yr", SharedPrefManager.getInstance(getApplicationContext()).getAcademicYear());
                if (name == null || name.equals("")) {
                    name = mDatabaseHelper.getName(1);
                    newUrl = mDatabaseHelper.getURL(1);
                    dUrl = mDatabaseHelper.getPURL(1);
                }
                Log.e("pie_chart", "params>" + params);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

    private void loadPieChartData1(String response) {
        try {
            ArrayList<PieEntry> entries = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            String pie_center_text = "";
            String dataname = "";
            Log.e("pie_chart", "jsonArray><>" + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                pie_center_text = object.getString("pie_center_text").toString();
                dataname = object.getString("dataname");
                JSONArray DetailsArray = object.getJSONArray("Details");
                for (int j = 0; j < DetailsArray.length(); j++) {//String.valueOf(Math.floor(value)).replace(".0","")
                    JSONObject detailjsonObject = new JSONObject(DetailsArray.get(j).toString());
                    Log.e("pie_chart", "Subject_Name><>" + detailjsonObject.getString("Subject_Name"));
                    entries.add(new PieEntry(Float.parseFloat(detailjsonObject.getString("Subject_Marks")), detailjsonObject.getString("Subject_Name")));
                }
                ArrayList<Integer> colors = new ArrayList<>();
                for (int color : ColorTemplate.MATERIAL_COLORS) {
                    colors.add(color);
                }
                for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                    colors.add(color);
                }
                PieDataSet dataSet = new PieDataSet(entries, dataname);
                dataSet.setColors(colors);
                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter(pieChart));

                data.setValueTextSize(8f);
                pieChart.setEntryLabelTextSize(8f);
                data.setValueTextColor(Color.BLACK);
                pieChart.setCenterText(pie_center_text);
                pieChart.setData(data);
                pieChart.invalidate();

                pieChart.animateY(1400, Easing.EaseInOutQuad);
                pieChart.getLegend().setEnabled(false);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            Log.e("pie_chart", "ERRPIE><>" + e.getMessage());
        }
    }


}