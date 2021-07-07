package in.aceventura.evolvuschool.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.aceventura.evolvuschool.BonafideCertificateActivityList;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;
import in.aceventura.evolvuschool.models.SendBonafideRequisitionDataModel;

public class SendBonafideRequisitionData extends RecyclerView.Adapter<SendBonafideRequisitionData.ViewHolder> {
    BonafideCertificateActivityList bonafideCertificateActivityList;
    ArrayList<SendBonafideRequisitionDataModel> arrayList;
    private int STORAGE_PERMISSION_CODE = 23;
    DownloadManager dm;
    String name, newUrl, dUrl, pid;
    DatabaseHelper mDatabaseHelper;

    public SendBonafideRequisitionData(BonafideCertificateActivityList bonafideCertificateActivityList, ArrayList<SendBonafideRequisitionDataModel> arrayList) {
        this.bonafideCertificateActivityList = bonafideCertificateActivityList;
        this.arrayList = arrayList;
        Log.e("ValuesInAdapter", "Value?cust");
        mDatabaseHelper = new DatabaseHelper(bonafideCertificateActivityList);
        name = mDatabaseHelper.getName(1);
        newUrl = mDatabaseHelper.getURL(1);
        dUrl = mDatabaseHelper.getPURL(1);

        if (name == null || name.equals("")) {
            name = mDatabaseHelper.getName(1);
            newUrl = mDatabaseHelper.getURL(1);
            dUrl = mDatabaseHelper.getPURL(1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bonafiderequisition_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("ValuesInAdapter", "Value?onBindViewHolder");

        SendBonafideRequisitionDataModel model = arrayList.get(position);
        holder.bonafied_req_id.setText(model.getBonafied_req_id());
        holder.student_id.setText(model.getStudent_id());
        holder.tv_fullname.setText(model.getFullname());
        holder.bonafied_formate_id.setText(model.getBonafied_formate_id());
        holder.tv_className.setText(model.getClass_name());
        holder.email_id.setText(model.getEmail_id());
        holder.parent_id.setText(model.getParent_id());
        holder.academic_yr.setText(model.getReq_acd_yr());
        holder.tv_bc_name.setText(model.getBc_name());
        Log.e("Fils", "isgDate" + model.getDate());
        try {

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");//28/01/2020//"yyyy-MM-dd"
            Date dt1 = format1.parse(model.getDate());//dd/MM/yyyy

            SimpleDateFormat your_format1 = new SimpleDateFormat("dd-MM-yyyy");//"dd/MM/yyyy"

            String nwuptoDate = your_format1.format(dt1);
            holder.date.setText(nwuptoDate);
        }catch (Exception e){

        }


        Log.e("Fils", "isg" + model.getIsGenerated());
        if (model.getIsGenerated().equals("Y")) {
            holder.iv_download.setVisibility(View.VISIBLE);

            holder.iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String filename = "" + model.getFullname() + ".pdf".trim();
                    //   https://sfs.evolvu.in/test/msfs_test/

                    String url = dUrl + "index.php/admin/download_fee_bonafide?student_id=" + model.getStudent_id() + "&req_acd_yr=" + model.getReq_acd_yr() + "&bonafied_req_id=" + model.getBonafied_req_id() + "&short_name=" + name.toString().trim();
                    Log.e("FilsURL", "Values" + url);
                    // Toast.makeText(bonafideCertificateActivityList, "downlaod files", Toast.LENGTH_SHORT).show();


                    Log.e("puel", "url->" + url);
                    try {

                        if (isReadStorageAllowed()) {
                            String downloadUrl;

                       /* if (name.equals("SACS")) {
                            downloadUrl = dUrl + "uploads/notice/" + iNoticeId + "/" + itemNoticeValue;
                        } else {
                            downloadUrl = dUrl + "uploads/" + name + "/notice/" + iNoticeId + "/" + itemNoticeValue;
                        }*/

                            dm = (DownloadManager) bonafideCertificateActivityList.getSystemService(Context.DOWNLOAD_SERVICE);
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
                                Log.e("Fils", "files" + directory.getAbsolutePath());
                            } catch (Exception e) {
                                //v 28+
                                Log.e("Fils", "files" + directory1.getAbsolutePath());

                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, StringFile + filename);//(Environment.DIRECTORY_PICTURES,"parag.jpeg")"/KrishanImages/" +
                            }


                            Log.e("Download", "donpa" + filename);

                            Toast.makeText(bonafideCertificateActivityList, "Certificate is downloaded. Please check in the Download/Evolvuschool/Parent/Certificate folder or Download folder", Toast.LENGTH_LONG).show();
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


        } else {
            holder.iv_download.setVisibility(View.GONE);
        }
        Log.e("ValuesInAdapter", "Value?holder" + arrayList.size());

    }

    private void requestStoragePermission() {

        try {


            if (ActivityCompat.shouldShowRequestPermissionRationale(bonafideCertificateActivityList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
                Toast.makeText(bonafideCertificateActivityList, "To Download Notice Attachment Please Allow the Storage Permission", Toast.LENGTH_LONG).show();
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(bonafideCertificateActivityList, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(bonafideCertificateActivityList, "Storage Permission" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isReadStorageAllowed() {


        int result = ContextCompat.checkSelfPermission(bonafideCertificateActivityList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int getItemCount() {
        Log.e("ValuesInAdapter", "Value?onBSize" + arrayList.size());

        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bonafied_req_id, tv_className,tv_bc_name, tv_fullname, student_id, bonafied_formate_id, email_id, parent_id, academic_yr, date;
        ImageView iv_download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bonafied_req_id = itemView.findViewById(R.id.tv_BonafiedId);
            tv_className = itemView.findViewById(R.id.tv_className);
            tv_bc_name = itemView.findViewById(R.id.tv_bc_name);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);
            student_id = itemView.findViewById(R.id.tv_StudentId);
            bonafied_formate_id = itemView.findViewById(R.id.tv_BonafiedFormateId);
            email_id = itemView.findViewById(R.id.tv_EmailId);
            parent_id = itemView.findViewById(R.id.tv_ParentId);
            academic_yr = itemView.findViewById(R.id.tv_academicyr);
            date = itemView.findViewById(R.id.tv_Date);
            iv_download = itemView.findViewById(R.id.iv_download);
        }
    }
}
