package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import in.aceventura.evolvuschool.BonafideCertificateActivity;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.models.StudentClassModel;

public class CertificateTypeClassAdapter extends RecyclerView.Adapter<CertificateTypeClassAdapter.ViewHolder> {

    BonafideCertificateActivity bonafideCertificateActivity;
    ArrayList<CertificateTypeModelClass> arrayList;
    List<CertificateTypeModelClass> setarrayList;
    CertificateTypeModelClass modelClass1 = null;
    OnItemClick onItemClick = null;

    public CertificateTypeClassAdapter(BonafideCertificateActivity bonafideCertificateActivity, ArrayList<CertificateTypeModelClass> arrayList, List<CertificateTypeModelClass> setarrayList, OnItemClick onItemClick) {
        this.bonafideCertificateActivity = bonafideCertificateActivity;
        this.arrayList = arrayList;
        this.setarrayList = setarrayList;
        this.onItemClick = onItemClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_studentname_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CertificateTypeModelClass modelClass = arrayList.get(position);
        holder.cb_studentclassname.setText(modelClass.getName());
        holder.cb_studentclassname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("AdapterisChecked", "br=e" + holder.cb_studentclassname.isChecked());
                try {


                   // modelClass1 = new CertificateTypeModelClass();

                    if (b) {
                        Log.e("AdapterisChecked", "InCheked" + holder.cb_studentclassname.isChecked());
//
//                        modelClass1.setBc_format_id(modelClass.getBc_format_id());
//                        modelClass1.setName(modelClass.getName());
//                        modelClass1.setaBoolean(true);
//                        setarrayList.add(position, modelClass1);

                        modelClass.setaBoolean(true);

                        if (holder.cb_studentclassname.getText().equals("Fee Bonafide Certificate")) {
                            onItemClick.onClick(true);
                        }


                    } else  {



//                        modelClass1.setBc_format_id(modelClass.getBc_format_id());
//                        modelClass1.setName(modelClass.getName());
//                        modelClass1.setaBoolean(false);
//                        setarrayList.add(position, modelClass1);


                        modelClass.setaBoolean(false);


                        if (holder.cb_studentclassname.getText().equals("Fee Bonafide Certificate")) {

                            onItemClick.onClick(false);
                        } else {

                        }


                        Log.e("AdapterisChecked", "NotCheked" + holder.cb_studentclassname.isChecked());

                    }
                    setarrayList.clear();
                    setarrayList.addAll(arrayList);


                } catch (Exception e) {
                    e.getMessage();
                    Log.e("AdapterisChecked", "NotCheerrr+" + e.getMessage());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_studentclassname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_studentclassname = itemView.findViewById(R.id.cb_studentclassname);
        }
    }
}