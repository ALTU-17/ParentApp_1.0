package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.adapters.AnswerPaperAdapter;
import in.aceventura.evolvuschool.adapters.QuestionPaperAdapter;
import in.aceventura.evolvuschool.models.AnsPaperModel;
import in.aceventura.evolvuschool.models.ExamListModel;
import in.aceventura.evolvuschool.models.QuesPaperModel;

public class ExamPageActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 222;
    public static int REQUEST_CODE_GET_FILE_PATH = 1;
    public static String fileBase64Code;
    public static String fileName;
    final Random rand = new Random();
    public String atchPDF, atchIMG, fName, iName, PDFpath;
    int diceRoll;
    File pdfFile;
    Uri uri, selectedPDF, outputFileUri, filePathpdf;
    Bitmap bitmap;
    String classid, sectionid, Sname, newUrl, dUrl, name, sid, pid, question_bank_id, download_url, academic_yr,
            student_id, parent_id;
    DatabaseHelper mDatabaseHelper;
    TextView txtpdfname;
    String displayName = null;
    String strFile = null;
    long totalMb = 0;
    QuestionPaperAdapter questionPaperAdapter;
    AnswerPaperAdapter answerPaperAdapter;
    Button saveExam;
    ArrayList<String> fileSizeArray = new ArrayList<>();
    ArrayList<String> fileNameArray = new ArrayList<>();
    Chronometer simpleChronometer;
    String time_taken;
    private int PICK_PDF_REQUEST = 12;
    private List<QuesPaperModel> attachment = new ArrayList<>();
    private List<AnsPaperModel> ansPaperLists = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ProgressBar pb_saveExam;
    private String exam_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page_activtiy);

        //For camera
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mDatabaseHelper = new DatabaseHelper(this);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        academic_yr = (SharedPrefManager.getInstance(this).getAcademicYear());


        TextView exam_name = findViewById(R.id.exam_name);
        TextView acd_yr = findViewById(R.id.acd_yr);
        TextView subject_name = findViewById(R.id.subject_name);
        TextView class_name = findViewById(R.id.class_name);
        TextView qb_name = findViewById(R.id.qb_name);
        TextView uploadAnsPapers = findViewById(R.id.uploadAnsPapers);
        TextView weightage = findViewById(R.id.marks);
        saveExam = findViewById(R.id.btn_save_finish_exam);
        pb_saveExam = findViewById(R.id.pb_saveExam);
        simpleChronometer = findViewById(R.id.timer);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        RecyclerView rv_quesPaper = findViewById(R.id.rv_quesPaper);
        questionPaperAdapter = new QuestionPaperAdapter(this, attachment);
        rv_quesPaper.setAdapter(questionPaperAdapter);
        final LinearLayoutManager newsLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rv_quesPaper.setLayoutManager(newsLinearLayoutManager);

        RecyclerView rv_ansPaper = findViewById(R.id.rv_ansPaper);
        answerPaperAdapter = new AnswerPaperAdapter(this, ansPaperLists);
        rv_ansPaper.setAdapter(answerPaperAdapter);
        final LinearLayoutManager ansPaperLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        rv_ansPaper.setLayoutManager(ansPaperLinearLayoutManager);
        answerPaperAdapter.notifyDataSetChanged();

        parent_id = String.valueOf((SharedPrefManager.getInstance(this).getRegId()));

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            ExamListModel examListModel = (ExamListModel) bundle.getSerializable("user");
            if (examListModel != null) {
                subject_name.setText(examListModel.getSubjectName());
                class_name.setText("( Class - " + examListModel.getClassName() + " )");
                qb_name.setText(examListModel.getQb_name());
                weightage.setText("Max.Marks - " + examListModel.getWeightage());

                question_bank_id = examListModel.getQuestion_bank_id();
                download_url = examListModel.getDownload_url();
                student_id = examListModel.getStud_id();
                classid = examListModel.getClass_id();
                sectionid = examListModel.getSection_id();

                //get the time from API
                getExamTime(name, student_id, question_bank_id);

                JSONArray image_list = null;
                try {
                    image_list = new JSONArray(examListModel.getImage_list());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                assert image_list != null;
                for (int j = 0; j < image_list.length(); j++) {
                    try {
                        JSONObject obj = image_list.getJSONObject(j);
                        String image_name = obj.getString("image_name");
                        String file_size = obj.getString("file_size");
                        QuesPaperModel quesPaperModel = new QuesPaperModel(image_name, file_size, download_url,
                                question_bank_id);
                        attachment.add(quesPaperModel);
                        questionPaperAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //Upload Answer Paper
        uploadAnsPapers.setOnClickListener(v -> selectImage(savedInstanceState));


        //Save and finish exam
        saveExam.setOnClickListener(v -> {
            if (fileSizeArray.size() > 0) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ExamPageActivity.this);
                dialog.setCancelable(true);
                dialog.setIcon(R.drawable.notify);
                dialog.setTitle("Submit Exam");
                dialog.setMessage("Are you sure to submit the  answer ?");
                dialog.setPositiveButton("Yes", (dialog1, id) -> ExamPageActivity.this.finishExam());
                dialog.setNegativeButton("No", (dialog1, id) -> dialog1.dismiss());
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                Toast.makeText(ExamPageActivity.this, "Please upload the Answer Papers", Toast.LENGTH_SHORT).show();
            }
        });

        //to stop timer
        //simpleChronometer.stop();*/

    }//onCreate

    private void getExamTime(String name, String student_id, String question_bank_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "get_time_spend",
                response -> {
                    Log.d("finishExamResponse ", response);
                    if (response != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("success").equals("true")) {
                                time_taken = jsonObject.getString("time_taken");

                                //first convert the string to proper format and then set to timer
                                String[] data = time_taken.split(":");//hh:mm:ss
                                int hours = Integer.parseInt(data[0]);//hh
                                int minutes = Integer.parseInt(data[1]);//mm
                                int seconds = Integer.parseInt(data[2]);//ss

                                int time = seconds + 60 * minutes + 3600 * hours;
                                System.out.println("time in millis = " + TimeUnit.MILLISECONDS.convert(time,
                                        TimeUnit.SECONDS));

                                simpleChronometer.setBase(SystemClock.elapsedRealtime() - (hours * 60000 * 60 + minutes * 60000 + seconds * 1000));
                                simpleChronometer.start();
                            } else {
                                simpleChronometer.stop();
                            }
                        } catch (JSONException e) {
                            simpleChronometer.stop();
                            e.printStackTrace();
                            System.out.println(e.toString());
                        }
                    } else {
                        simpleChronometer.stop();
                    }
                }, error -> {
            simpleChronometer.stop();
            System.out.println(error.toString());
            Log.i("error", "onErrorResponse: " + error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("question_bank_id", question_bank_id);
                params.put("student_id", student_id);
                Log.d("time_params", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void finishExam() {
        String simpleChronometertime = simpleChronometer.getText().toString();
        //Getting the timer value as string
        exam_time = simpleChronometer.getText().toString();
        saveExam.setVisibility(View.GONE);
        pb_saveExam.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "upload_answer_paper",
                response -> {
                    Log.d("finishExamResponse ", response);
                    if (response != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("status").equals("true")) {
                                Toast.makeText(this, "Exam Paper Submitted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), OnlineExamActivity.class);
                                intent.putExtra("SID", student_id);
                                intent.putExtra("PID", parent_id);
                                intent.putExtra("CLASSID", classid);
                                intent.putExtra("SECTIONID", sectionid);
                                startActivity(intent);
                                finish();
                            } else {
                                System.out.println(jsonObject.toString());
                                saveExam.setVisibility(View.VISIBLE);
                                pb_saveExam.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.toString());
                            saveExam.setVisibility(View.VISIBLE);
                            pb_saveExam.setVisibility(View.GONE);
                        }
                    } else {
                        saveExam.setVisibility(View.VISIBLE);
                        pb_saveExam.setVisibility(View.GONE);
                    }
                }, error -> {
            saveExam.setVisibility(View.VISIBLE);
            pb_saveExam.setVisibility(View.GONE);
            System.out.println(error.toString());
            Log.i("error", "onErrorResponse: " + error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("question_bank_id", question_bank_id);
                params.put("student_id", student_id);
                params.put("time_taken", exam_time);
                params.put("acd_yr", academic_yr);
                params.put("file_name", fileNameArray.toString());
                params.put("filesize", fileSizeArray.toString());
                Log.d("finish_params", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void selectImage(final Bundle savedInstanceState) {

        final CharSequence[] options = {"Take Photo", "Choose Images", "Choose Files", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                showCameraChooser(savedInstanceState);
            } else if (options[item].equals("Choose Images")) {
                showFileChooser1();
            } else if (options[item].equals("Choose Files")) {
                showPDFChooser();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showPDFChooser() {
        try {
            Intent intent = new Intent();
            intent.setType("application/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showCameraChooser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            try {
                diceRoll = rand.nextInt(100) + 1;
                //For external directory
                File photo = new File(Environment.getExternalStorageDirectory(), "ans_paper_" + diceRoll +
                        ".jpeg");
                if (photo.exists()) {
                    photo.mkdirs();
                }
                Uri outputFileUri = Uri.fromFile(photo);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, REQUEST_CODE_GET_CAMERA_FILE_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ExamPageActivity.this, "CameraError1" + e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Error" + e.getMessage());
            }
        }
    }

    //when photo is clicked save the uri before before camera activity gets closed.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString("photopath", String.valueOf(outputFileUri));
            super.onSaveInstanceState(outState);
        } else {
            Log.e("onSaveInstanceStateError: ", outState.toString());
        }

    }

    //retrieve the Uri of photo on returning to parent activity
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("photopath")) {
                outputFileUri = Uri.parse(savedInstanceState.getString("photopath"));
            } else {
                Log.e("onSaveInstanceStateError: ", savedInstanceState.toString());
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    //Files attacchments
    protected void showFileChooser1() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, "Choose a file");
            startActivityForResult(intent, REQUEST_CODE_GET_FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SourceLockedOrientationActivity,SwitchIntDef")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Image file from gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GET_FILE_PATH && resultCode == Activity.RESULT_OK) {

            assert data != null;
            if (data.getData() != null) {
                uri = data.getData();
                InputStream stream;
                try {
                    stream = this.getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    assert stream != null;
                    stream.close();
                    final int maxSize = 1000; //500
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmap.getWidth();
                    int inHeight = bitmap.getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1); //90

                    byte[] byte_arr = stream1.toByteArray();
                    File tmpDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/SchoolApp/");
                    tmpDir.mkdir();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                        fileBase64Code = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //To getFile name
                File f = new File(uri.getPath());
                fileName = f.getName() + ".jpeg";

                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                        "upload_question_paper_into_temp_folder", response -> {
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("gallery_attachment", jsonObject.toString());
                            String fileSize = jsonObject.getString("filesize").replace("\\r\\n", "").replace("["
                                    , "").replace("]", "");

                            if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                fileSizeArray.add(fileSize);
                                fileNameArray.add(fileName);

                                AnsPaperModel ansPaperModel = new AnsPaperModel(fileName, question_bank_id,
                                        fileSizeArray, fileNameArray);
                                ansPaperLists.add(ansPaperModel);
                                answerPaperAdapter.notifyDataSetChanged();
                            } else {
                                System.out.println(jsonObject);
                                fileSizeArray.clear();
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.toString());
                            fileSizeArray.clear();
                            progressDialog.dismiss();
                        }
                    } else {
                        System.out.println(response);
                        fileSizeArray.clear();
                        progressDialog.dismiss();
                    }
                }, error -> {
                    progressDialog.dismiss();
                    fileSizeArray.clear();
                    System.out.println(error.toString());
                    Log.i("error", "onErrorResponse: " + error.toString());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("doc_type_folder", "uploaded_ans_paper");
                        params.put("short_name", name);
                        params.put("question_bank_id", question_bank_id);
                        params.put("filename", fileName);
                        params.put("filedata", fileBase64Code);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,

                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
            }
        }


        //Images from camera
        else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH && resultCode == Activity.RESULT_OK) {
            //For camera image blur
            try {
                fileName = diceRoll + ".jpeg";
                File file = null;

                try {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                            "ans_paper_" + diceRoll + ".jpeg");
                    fileName = file.getName();
                } catch (Exception e) {
                    //unable to create  temporary file
                    Toast.makeText(ExamPageActivity.this, "Unable to create temporary file", Toast.LENGTH_SHORT).show();
                }

                Uri uri = Uri.fromFile(file);// TODO: 22-07-2020 added
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                String pathNEW = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);

                if (pathNEW != null) {
                    outputFileUri = Uri.parse(pathNEW);
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);

                try {
                    byte[] byte_arr = stream.toByteArray();
                    fileBase64Code = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                } catch (Exception e) {
                    //Unable to convert image to byte array
                    Toast.makeText(ExamPageActivity.this, "Unable to convert image to byte array",
                            Toast.LENGTH_SHORT).show();
                }

                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                        "upload_question_paper_into_temp_folder", response -> {
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("camera_attachment", jsonObject.toString());
                            String fileSize = jsonObject.getString("filesize").replace("\\r\\n", "").replace("["
                                    , "").replace("]", "");

                            if (!fileSize.equals("0") || !fileSize.equals("0.0")) {
                                fileSizeArray.add(fileSize);
                                fileNameArray.add(fileName);
//                                AnsPaperModel ansPaperModel = new AnsPaperModel(fileName,
//                                        question_bank_id);
                                AnsPaperModel ansPaperModel = new AnsPaperModel(fileName, question_bank_id,
                                        fileSizeArray, fileNameArray);
                                ansPaperLists.add(ansPaperModel);
                                answerPaperAdapter.notifyDataSetChanged();
                            } else {
                                fileSizeArray.clear();

                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            fileSizeArray.clear();
                            progressDialog.dismiss();
                        }
                    } else {
                        fileSizeArray.clear();
                        progressDialog.dismiss();
                    }
                }, error -> {
                    progressDialog.dismiss();
                    fileSizeArray.clear();
                    Log.i("error", "onErrorResponse: " + error.toString());
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("doc_type_folder", "uploaded_ans_paper");
                        params.put("question_bank_id", question_bank_id);
                        params.put("short_name", name);
                        params.put("filename", fileName);
                        params.put("filedata", fileBase64Code);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,

                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        //new FileManager Documents
        else if (requestCode == 101) {
            if (resultCode == RESULT_OK && data != null) {
                selectedPDF = data.getData();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);
                    convertFileToString(PDFpath);
                    displayName = pdfFile.getName();

                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                            "upload_question_paper_into_temp_folder", response -> {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("file_attachment_1", jsonObject.toString());
                                String fileSize =
                                        jsonObject.getString("filesize").replace("\\r\\n", "").replace("[", "").replace("]", "");

                                if (!fileSize.equals("0") || !fileSize.equals("0.0")) {

                                    fileSizeArray.add(fileSize);

                                    fileNameArray.add(displayName);
//                                    AnsPaperModel ansPaperModel = new AnsPaperModel(displayName,
//                                            question_bank_id);
                                    AnsPaperModel ansPaperModel = new AnsPaperModel(fileName, question_bank_id,
                                            fileSizeArray, fileNameArray);

                                    ansPaperLists.add(ansPaperModel);

                                    answerPaperAdapter.notifyDataSetChanged();
                                } else {

                                    fileSizeArray.clear();

                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                fileSizeArray.clear();

                                progressDialog.dismiss();
                            }
                        } else {
                            fileSizeArray.clear();
                            progressDialog.dismiss();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        fileSizeArray.clear();
                        Log.i("error", "onErrorResponse: " + error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("doc_type_folder", "uploaded_ans_paper");
                            params.put("question_bank_id", question_bank_id);
                            params.put("short_name", name);
                            params.put("filename", displayName);
                            params.put("filedata", strFile);
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,

                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

                    pdfFile = new File(FileManager.getPath(ExamPageActivity.this, -1, selectedPDF));
                }
            }
        }

        //2.FileManager Documents
        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathpdf = data.getData();
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (uriString.startsWith("content://") || uriString.startsWith("file://")) {
                    selectedPDF = data.getData();
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);

                    //Checking the file size before conversion it should be less than 25mb
                    long bytes = pdfFile.length();
                    long mb = bytes / (1024 * 1024);
                    totalMb = totalMb + mb; //Getting the TotalSize of files


                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName();

                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                                "upload_question_paper_into_temp_folder", response -> {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("file_attachment_2", jsonObject.toString());
                                    String fileSize =

                                            jsonObject.getString("filesize").replace("\\r" +

                                                            "\\n",

                                                    "").replace("[", "").replace("]", "");

                                    if (!fileSize.equals("0") || !fileSize.equals("0.0")) {

                                        fileSizeArray.add(fileSize);

                                        fileNameArray.add(displayName);
//                                        AnsPaperModel ansPaperModel = new AnsPaperModel(displayName,
//                                                question_bank_id);
                                        AnsPaperModel ansPaperModel = new AnsPaperModel(fileName, question_bank_id,
                                                fileSizeArray, fileNameArray);

                                        ansPaperLists.add(ansPaperModel);

                                        answerPaperAdapter.notifyDataSetChanged();

                                        progressDialog.dismiss();
                                    } else {

                                        fileSizeArray.clear();

                                        progressDialog.dismiss();
                                    }
                                } catch (JSONException e) {

                                    e.printStackTrace();

                                    fileSizeArray.clear();

                                    progressDialog.dismiss();
                                }
                            } else {
                                fileSizeArray.clear();

                                progressDialog.dismiss();
                            }
                        }, error -> {
                            progressDialog.dismiss();
                            fileSizeArray.clear();
                            Log.i("error", "onErrorResponse: " + error.toString());
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("doc_type_folder", "uploaded_ans_paper");
                                params.put("question_bank_id", question_bank_id);
                                params.put("short_name", name);
                                params.put("filename", displayName);
                                params.put("filedata", strFile);
                                Log.d("file_attach_2_params", params.toString());
                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,

                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
                    }

                }
            } else {
                if (uriString.startsWith("content://") || uriString.startsWith("file://")) {
                    selectedPDF = data.getData();
                    PDFpath = getPathForPdf(selectedPDF);
                    pdfFile = new File(PDFpath);

                    //Checking the file size before conversion it should be less than 25mb
                    long bytes = pdfFile.length();
                    long mb = bytes / (1024 * 1024);
                    totalMb = totalMb + mb;

                    if (mb > 25) {
                        Toast.makeText(this, "File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else if (totalMb > 25) {
                        Toast.makeText(this, "Total File size should be less than 25mb", Toast.LENGTH_LONG).show();
                    } else {
                        convertFileToString(PDFpath);
                        displayName = pdfFile.getName();
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                                "upload_question_paper_into_temp_folder", response -> {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("file_attachment_3", jsonObject.toString());
                                    String fileSize =

                                            jsonObject.getString("filesize").replace("\\r" +

                                                            "\\n",

                                                    "").replace("[", "").replace("]", "");

                                    if (!fileSize.equals("0") || !fileSize.equals("0.0")) {

                                        fileSizeArray.add(fileSize);
                                        fileNameArray.add(displayName);
//                                        AnsPaperModel ansPaperModel = new AnsPaperModel(displayName,
//                                                question_bank_id);
                                        AnsPaperModel ansPaperModel = new AnsPaperModel(fileName, question_bank_id,
                                                fileSizeArray, fileNameArray);
                                        ansPaperLists.add(ansPaperModel);
                                        answerPaperAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    } else {
                                        fileSizeArray.clear();
                                        progressDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    fileSizeArray.clear();
                                    progressDialog.dismiss();
                                }
                            } else {
                                fileSizeArray.clear();
                                progressDialog.dismiss();
                            }
                        }, error -> {
                            progressDialog.dismiss();
                            fileSizeArray.clear();
                            Log.i("error", "onErrorResponse: " + error.toString());
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("doc_type_folder", "uploaded_ans_paper");
                                params.put("question_bank_id", question_bank_id);
                                params.put("short_name", name);
                                params.put("filename", displayName);
                                params.put("filedata", strFile);
                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,

                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            uri = null;
            Toast.makeText(this, "Cancelled!", Toast.LENGTH_LONG).show();
        }

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getOrientation();

        if (orientation == Configuration.ORIENTATION_PORTRAIT || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        int rotation = display.getRotation();
        if (rotation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (rotation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //get path of PDF
    public String getPathForPdf(Uri uri) {
        File file = null;
        try {
            //Create a temporary folder where the copy will be saved to
            File temp_folder = this.getExternalFilesDir("TempFolder");

            //Use ContentResolver to get the name of the original name
            //Create a cursor and pass the Uri to it
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            //Check that the cursor is not null
            assert cursor != null;
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            //Get the file name
            String filename = cursor.getString(nameIndex);
            //Close the cursor
            cursor.close();

            //open a InputStream by passing it the Uri
            //We have to do this in a try/catch
            InputStream is = null;
            try {
                is = this.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //We now have a folder and a file name, now we can create a file
            file = new File(temp_folder + "/" + filename);

            //We can now use a BufferedInputStream to pass the InputStream we opened above to it
            BufferedInputStream bis = new BufferedInputStream(is);
            //We will write the byte data to theFileOutputStream, but we first have to create it
            FileOutputStream fos = new FileOutputStream(file);

            byte[] data = new byte[1024];
            long total = 0;
            int count;
            //Below we will read all the byte data and write it to the FileOutputStream
            while ((count = bis.read(data)) != -1) {
                total += count;
                fos.write(data, 0, count);
            }
            //The FileOutputStream is done and the file is created and we can clean and close it
            fos.flush();
            fos.close();

        } catch (IOException e) {
            Log.e("IOException = ", String.valueOf(e));
        }

        //Finally we can pass the path of the file we have copied
        return file.getAbsolutePath();
    }

    //Convert File to String
    public void convertFileToString(String pathOnSdCard) {

        File file = new File(pathOnSdCard);
        try {

            byte[] data = FileUtils.readFileToByteArray(file);//Convert any file, image or video

            // into byte array
            strFile = Base64.encodeToString(data, Base64.NO_WRAP);//Convert byte array into string

            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "TeacherAppFiles");
            if (!root.exists()) root.mkdirs();
            File gpxfile = new File(root, "/file.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(strFile);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Getting Confirmation of user before closing this activity of exam
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.notify).setTitle("Exit").setMessage("Are you sure you " + "want to exit?").setPositiveButton("Yes", (dialog, which) -> {
            simpleChronometer.stop();
            ExamPageActivity.this.finish();
        }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
    }
}


