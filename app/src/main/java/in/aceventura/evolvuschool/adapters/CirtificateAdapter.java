package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.aceventura.evolvuschool.CirtificateActivity;
import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.NoticeDetails;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

public class CirtificateAdapter extends RecyclerView.Adapter<CirtificateAdapter.ViewHolder> {

    Activity mContext;
    DatabaseHelper mDatabaseHelper;

    String filename = "";
    DownloadManager dm;
    List<DataSet> mDataSet;
    private int STORAGE_PERMISSION_CODE = 23;
    String name, newUrl, dUrl, pid;
    String a = String.valueOf(10);

    public CirtificateAdapter(Activity mContext, ArrayList<DataSet> mDataSet) {
        this.mContext = mContext;
        this.mDataSet = mDataSet;
        mDatabaseHelper = new DatabaseHelper(mContext);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
       /* Log.e("puel", "url->" + dUrl);

        Log.e("puel", "url->" + dUrl + "index.php/");*/
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cirtificate_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataSet model = mDataSet.get(position);
        holder.tv_cirEvent.setText("Event:" + model.getCirCevent());

        holder.tv_cirDate.setText(DateFormat.format("dd-MM-yyyy", new Date(Long.parseLong(String.valueOf(model.getCirdate())))).toString());
        holder.tv_cirStudentName.setText(model.getCirfirst_name() + " " + model.getCirlast_name());

        //filename = model.getCirfirst_name() + " " + model.getCirlast_name();
        if (model.getCirposition().equals("1")) {
            holder.tv_cirPosition.setText("Position:First");
        } else if (model.getCirposition().equals("2")) {
            holder.tv_cirPosition.setText("Position:Second");
        } else if (model.getCirposition().equals("3")) {
            holder.tv_cirPosition.setText("Position:Third");
        }
        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo pdf name as aparna mam said--30-12-2020
                filename = "Certificate" + "_" + mDataSet.get(position).getCirfirst_name() + "_" + model.getCirlast_name() + ".pdf".trim();


                /* String url = dUrl + "index.php/admin/parent_certificate?operation=download&student_id=" + model.getCirstudent_id() + "&acd_yr=" + model.getCiracademic_yr().trim();*/

                // String url = dUrl + "index.php/admin/parent_certificate?operation=download&student_id=" + model.getCirstudent_id() + "&acd_yr=" + model.getCiracademic_yr().trim();
                String url = dUrl + "index.php/admin/parent_certificate?operation=download&achievement_id=" + model.getCirachievement_id() + "&acd_yr=" + model.getCiracademic_yr().trim();


                Log.e("puel", "url->" + url);
                try {

                    if (isReadStorageAllowed()) {
                        String downloadUrl;

                       /* if (name.equals("SACS")) {
                            downloadUrl = dUrl + "uploads/notice/" + iNoticeId + "/" + itemNoticeValue;
                        } else {
                            downloadUrl = dUrl + "uploads/" + name + "/notice/" + iNoticeId + "/" + itemNoticeValue;
                        }*/

                        dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(url);
                        System.out.println("NOTICEDOWNLOADURL - " + uri.toString());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        request.setMimeType("application/");
                        request.allowScanningByMediaScanner();

                        String folder = "/Download/Evolvuschool/Parent/Certificate/";
                        String StringFile = "/Evolvuschool/Parent/Certificate/";
                        File directory = new File(folder);
                        File directory1 = new File(StringFile);
                        try {
                            request.setDestinationInExternalPublicDir(folder, filename);//v 28 allow to create and it deprecated method

                        } catch (Exception e) {
                            //v 28+
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile+filename);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")"/KrishanImages/" +
                        }


                        Log.e("Download", "donpa" + filename);

                        Toast.makeText(mContext, "Certificate is downloaded. Please check in the Download/Evolvuschool/Parent/Certificate folder or Download folder", Toast.LENGTH_LONG).show();
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
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

    }

    private void requestStoragePermission() {

        try {


            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                Toast.makeText(mContext, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(mContext, "Storage Permission" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isReadStorageAllowed() {


        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_cirEvent, tv_cirDate, tv_cirStudentName, tv_cirPosition;
        ImageView iv_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cirEvent = itemView.findViewById(R.id.tv_cirEvent);
            tv_cirDate = itemView.findViewById(R.id.tv_cirDate);
            tv_cirStudentName = itemView.findViewById(R.id.tv_cirStudentName);
            tv_cirPosition = itemView.findViewById(R.id.tv_cirPosition);
            iv_download = itemView.findViewById(R.id.iv_download);
        }
    }
}
