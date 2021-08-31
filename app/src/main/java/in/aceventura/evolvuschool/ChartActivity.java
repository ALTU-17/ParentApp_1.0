package in.aceventura.evolvuschool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.models.Detail_result;

public class ChartActivity extends AppCompatActivity {
    private LineChart mChart;
    // ArrayList<ILineDataSet> dataSets1;

    /*String PiechartData = "{\n" +
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

    /* String values = "[\n" +
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mDatabaseHelper = new DatabaseHelper(this);

        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);


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


        setupPieChart();
        // loadPieChartData(PiechartData);
        //  loadPieChartData();
        getChartMarksPieChart();
        showBarChart();
        // getChartMarksLineChart1(values);


        String Valuesfrom = "[{\"Exam_name\":\"Term 1\",\"Details\":[\"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"54\\\"}\",\"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"51\\\"}\",\"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"56\\\"}\",\"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"57\\\"}\",\"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"58\\\"}\",\"{\\\"Subject\\\":\\\"Marathi\\\",\\\"Percentage\\\":\\\"39\\\"}\",\"{\\\"Subject\\\":\\\"Sanskrit\\\",\\\"Percentage\\\":\\\"37\\\"}\",\"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"42\\\"}\"]},{\"Exam_name\":\"Periodic Test 2\",\"Details\":[\"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"49\\\"}\",\"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"50\\\"}\",\"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"49\\\"}\",\"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"49\\\"}\"]},{\"Exam_name\":\"Term 2\",\"Details\":[\"{\\\"Subject\\\":\\\"English_rc\\\",\\\"Percentage\\\":\\\"86\\\"}\",\"{\\\"Subject\\\":\\\"Hindi_rc\\\",\\\"Percentage\\\":\\\"85\\\"}\",\"{\\\"Subject\\\":\\\"Maths_rc\\\",\\\"Percentage\\\":\\\"80\\\"}\",\"{\\\"Subject\\\":\\\"Science\\\",\\\"Percentage\\\":\\\"84\\\"}\",\"{\\\"Subject\\\":\\\"Social Science\\\",\\\"Percentage\\\":\\\"88\\\"}\",\"{\\\"Subject\\\":\\\"Computer Applications\\\",\\\"Percentage\\\":\\\"41\\\"}\"]}]";

        getChartMarksLineChart24(Valuesfrom);
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
        ArrayList<Double> valueList = new ArrayList<Double>();
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
       /* for(int i = 0; i < 6; i++){
            valueList.add(i * 100.1);
        } */
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


        getChartMarksBarChart();
    }

    private void getChartMarksBarChart() {
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

        /* ArrayList<String> xAxisValues = new ArrayList<String>();
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

        set1 = new LineDataSet(entries, "English");
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
                    mExam = jsonObject.getString("Exam_name");
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
                        // set1 = new LineDataSet(values, jsonObject.getString("Exam_name"));

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
        Log.e("linecart", "URL>" + newUrl + "student_marks_details_for_chart");
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, newUrl + "student_marks_details_for_chart", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("linecart", "response><>" + response);
                try {
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
                params.put("section_id", sectionid);
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
        requestQueue.add(request);
    }

    private void newLineChart(String response) {


    }

    private void getChartMarksLineChart24(String response) {


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
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);
                pieChart.setCenterText(pie_center_text);
                pieChart.setData(data);
                pieChart.invalidate();
                pieChart.animateY(1400, Easing.EaseInOutQuad);
                pieChart.getLegend().setEnabled(false);
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("pie_chart", "ERRPIE><>" + e.getMessage());
        }
    }

    private class CustomDataEntry extends DataEntry {
        CustomDataEntry(String x, Number value, Number value2, Number value3) {

            setValue("value2", value);
            setValue("value3", value2);
        }
    }
}