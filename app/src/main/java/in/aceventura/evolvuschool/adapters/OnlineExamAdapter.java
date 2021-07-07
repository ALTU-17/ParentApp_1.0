package in.aceventura.evolvuschool.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.ExamPageActivity;
import in.aceventura.evolvuschool.McqExamWebPageActivity;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RequestHandler;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.models.ExamListModel;


/**
 * Created by "Manoj Waghmare" on 14,Aug,2020
 **/

public class OnlineExamAdapter extends RecyclerView.Adapter<OnlineExamAdapter.ViewHolder> {

    public boolean showShimmer = true;
    String classid, sectionid, Sname, newUrl, dUrl, name, sid, pid, question_bank_id, download_url, academic_yr,
            student_id;
    DatabaseHelper mDatabaseHelper;
    private Context context;
    private List<ExamListModel> examList;
    private ProgressDialog progressDialog;


    //constructor
    public OnlineExamAdapter(Context context, List<ExamListModel> examList) {
        this.context = context;
        this.examList = examList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);
    }

    //setting layout file
    @NonNull
    @Override
    public OnlineExamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.online_exam_item, parent, false));
    }

    //Logic & Setting the values to UI
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OnlineExamAdapter.ViewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        }
        else {
            holder.shimmerFrameLayout.startShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            final ExamListModel examListModel = examList.get(position);
            holder.examName.setText(examListModel.getQb_name());
            holder.subject_name.setText(examListModel.getSubjectName());
            holder.className.setText("( Class " + examListModel.getClassName() + " )");

            String student_attempt_status = examListModel.getStudent_attempt_status();

            if (student_attempt_status.equals("I")) {
                holder.start_exam.setVisibility(View.VISIBLE);
            }
            else if (student_attempt_status.equals("C")) {
                holder.start_exam.setVisibility(View.GONE);
            }
            else {
                holder.start_exam.setVisibility(View.VISIBLE);
            }


            holder.start_exam.setOnClickListener(v -> {
                String qb_type = examListModel.getQb_type();
                //WebView MCQ Exam
                if (qb_type.equals("mcq")) {
                    Intent intent = new Intent(context.getApplicationContext(), McqExamWebPageActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("user", examListModel);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
                //PenPaper Exam Activity
                else if (qb_type.equals("upload")) {
                    // TODO: 16-09-2020
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl + "start_exam",
                            response -> {
                        Log.d("StartExamResponse ", response);
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("success").equals("true")) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(context.getApplicationContext(),
                                            ExamPageActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("user", examListModel);
                                    intent.putExtras(b);
                                    context.startActivity(intent);
                                }
                                else {
                                    progressDialog.dismiss();
                                }
                            }
                            catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                                System.out.println(e.toString());
                            }
                        }
                        else {
                            progressDialog.dismiss();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        System.out.println(error.toString());
                        Log.i("StartExamError: ", error.toString());
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("short_name", name);
                            params.put("question_bank_id", examListModel.getQuestion_bank_id());
                            params.put("student_id", examListModel.getStud_id());
                            Log.d("StartExam_params", params.toString());
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
                }
            });


            //instructions dialog box
            holder.ib_instructions.setOnClickListener(v -> {
                String exam_type = examListModel.getQb_type();
                if (exam_type.equals("mcq")) {
                    OnlineExamAdapter.this.showMcqInstructionsDialog(examListModel.getInstructions());
                }
                else if (exam_type.equals("upload")) {
                    OnlineExamAdapter.this.showInstructionsDialog(examListModel.getInstructions());
                }
            });
        }
    }

    private void showInstructionsDialog(String instructions) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.notify);
        dialog.setTitle("Exam Instructions");
        dialog.setMessage(instructions);
        dialog.setPositiveButton("Ok", (dialog1, id) -> dialog1.dismiss());
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void showMcqInstructionsDialog(String instructions) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.notify);
        dialog.setTitle("Exam Instructions");
        dialog.setMessage(instructions);
        dialog.setPositiveButton("Ok", (dialog1, id) -> dialog1.dismiss());
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    //setting count
    @Override
    public int getItemCount() {
        // number of shimmer item shown while loading
        int SHIMMER_ITEM_NUMBER = 6;
        return showShimmer ? SHIMMER_ITEM_NUMBER : this.examList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView examName, className, subject_name;
        Button start_exam;
        ImageButton ib_instructions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            this.examName = itemView.findViewById(R.id.exam_name);
            this.subject_name = itemView.findViewById(R.id.subject_name);
            this.className = itemView.findViewById(R.id.class_name);
            this.start_exam = itemView.findViewById(R.id.start_exam);
            this.ib_instructions = itemView.findViewById(R.id.ib_instructions);
        }
    }
}

