package in.aceventura.evolvuschool.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.aceventura.evolvuschool.ExamPageActivity;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.RequestHandler;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.models.AnsPaperModel;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class AnswerPaperAdapter extends RecyclerView.Adapter<AnswerPaperAdapter.ViewHolder> {

    private Context context;
    private List<AnsPaperModel> ansList;
    ArrayList<String> fileSizeArray;
    ArrayList<String> fileNameArray;
    String newUrl, name;
    ProgressDialog progressDialog;

    //constructor
    public AnswerPaperAdapter(Context context, List<AnsPaperModel> ansList) {
        this.context = context;
        this.ansList = ansList;

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        String dUrl = mDatabaseHelper.getPURL(1);
        progressDialog = new ProgressDialog(context);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
    }


    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_qp, delete;
        TextView tv_qpName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_qp = itemView.findViewById(R.id.iv_qp);
            tv_qpName = itemView.findViewById(R.id.tv_qpName);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    //setting layout file
    @NonNull
    @Override
    public AnswerPaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.ans_paper_list_item, parent, false)
        );
    }


    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull AnswerPaperAdapter.ViewHolder holder, int position) {

        AnsPaperModel ansPaperModel = ansList.get(position);

        String anspaper = ansPaperModel.getFileName();

        fileSizeArray = ansPaperModel.getFileSizeArray();
        fileNameArray = ansPaperModel.getFileNameArray();


        if (anspaper.contains(".PDF") || anspaper.contains(".pdf")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_pdf));
        } else if (anspaper.contains(".doc") || anspaper.contains(".docx") || anspaper.contains(
                ".txt")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_word));
        } else {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_image));
        }

        holder.tv_qpName.setText(ansList.get(position).getFileName());

        //Remove Answer paper from list
        holder.delete.setOnClickListener(v -> removeAnsPaper(position, fileSizeArray, fileNameArray));

    }

    private void removeAnsPaper(int position, ArrayList<String> fileSizeArray, ArrayList<String> fileNameArray) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, newUrl +
                "delete_uploaded_file_from_tmp_folder",
                response -> {
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("delete_ans_paper", jsonObject.toString());
                            if (jsonObject.getString("status").equals("true")) {
                                fileSizeArray.remove(position);
                                fileNameArray.remove(position);
                                ansList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, ansList.size());
                                progressDialog.dismiss();
                                notifyDataSetChanged();
                            } else {
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.toString());
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    System.out.println(error.toString());
                    Log.i("error", "onErrorResponse: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("short_name", name);
                params.put("doc_type_folder", "uploaded_ans_paper");
                params.put("filename", ansList.get(position).getFileName());
                params.put("question_bank_id", ansList.get(position).getQuestion_bank_id());
                System.out.println("delete_params" + params);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    //setting count
    @Override
    public int getItemCount() {
        return ansList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
