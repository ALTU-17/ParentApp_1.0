package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.models.QuesPaperModel;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class QuestionPaperAdapter extends RecyclerView.Adapter<QuestionPaperAdapter.ViewHolder> {

    int STORAGE_PERMISSION_CODE = 23;
    private Context context;
    private List<QuesPaperModel> qpList;
    private String newUrl, name, dUrl;

    //constructor
    public QuestionPaperAdapter(Context context, List<QuesPaperModel> qpList) {
        this.context = context;
        this.qpList = qpList;
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
    }

    //setting layout file
    @NonNull
    @Override
    public QuestionPaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ques_paper_list_item, parent, false));
    }

    //Logic & Setting the values to UI
    @Override
    public void onBindViewHolder(@NonNull QuestionPaperAdapter.ViewHolder holder, int position) {

        QuesPaperModel qpModel = qpList.get(position);
        String quespaper = qpModel.getFileName();

        if (quespaper.contains(".PDF") || quespaper.contains(".pdf")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_pdf));
        } else if (quespaper.contains(".doc") || quespaper.contains(".docx") || quespaper.contains(".txt")) {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_word));
        } else {
            holder.iv_qp.setBackground(ContextCompat.getDrawable(context, R.drawable.file_image));
        }

        holder.tv_qpName.setText(qpList.get(position).getFileName());


        //Download Question Paper for exam
        holder.qp_download.setOnClickListener(v -> {
            String quesPaperName = qpModel.getFileName();
            String download_url = qpModel.getDownload_url();
            String qb_Id = qpModel.getQb_Id();

            downloadNotice(quesPaperName, download_url, qb_Id);
            Toast.makeText(context, "Downloading Question Paper  " + quesPaperName, Toast.LENGTH_LONG).show();
        });
    }

    private void downloadNotice(String quesPaperName, String download_url, String qb_Id) {
        if (isReadStorageAllowed()) {
            //https://sfs.evolvu.in/onlineexam/test/uploads/SFSNW/uploaded_ques_paper/169/544052.jpeg
            String quesDownloadUrl = download_url + name + "/" + "uploaded_ques_paper/" + qb_Id + "/" + quesPaperName.trim();
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(quesDownloadUrl);
            System.out.println("Q_PAPER_URL - " + uri.toString());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setMimeType("application/");
            request.allowScanningByMediaScanner();


            String folder = "/Download/Evolvuschool/Parent/Question/";
            String StringFile = "/Evolvuschool/Parent/Question/";
            File directory = new File(folder);
            File directory1 = new File(StringFile);
            try {
                request.setDestinationInExternalPublicDir(folder, quesPaperName);//v 28 allow to create and it deprecated method

            } catch (Exception e) {

                //v 28+
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + quesPaperName);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")
            }
            Toast.makeText(context, "Attachment is downloaded. Please check in the Download/Evolvuschool/Parent/Question folder or Downloads folder", Toast.LENGTH_LONG).show();


            //If directory not exists create one....
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (!directory1.exists()) {
                directory1.mkdirs();
            }
            dm.enqueue(request);
            return;

        }
        requestStoragePermission();
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(context, "To Download Question Paper Please Allow the Storage " + "Permission",
                    Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission

        ActivityCompat.requestPermissions((Activity) context,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //setting count
    @Override
    public int getItemCount() {
        return qpList.size();
    }

    /*//This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
    @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                // Toast.makeText(context,"Permission granted now you can read the storage",Toast
                .LENGTH_LONG).show();

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(context, "Oops you just denied the permission", Toast.LENGTH_LONG)
                .show();
            }
        }
    }*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Binding the views with components from layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_qp;
        TextView tv_qpName;
        LinearLayout qp_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_qp = itemView.findViewById(R.id.iv_qp);
            tv_qpName = itemView.findViewById(R.id.tv_qpName);
            qp_download = itemView.findViewById(R.id.qp_download);
        }
    }
}

