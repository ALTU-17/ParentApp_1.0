package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.models.StudentClassModel;

public class StudentNameAdapter extends RecyclerView.Adapter<StudentNameAdapter.ViewHolder> {

    Activity mActivity;
    ArrayList<StudentClassModel> arrayList;
    Bundle extras;
    ArrayList<StudentClassModel> setstudentClassModels;
    StudentClassModel studentClassModel = null;

    public StudentNameAdapter(Activity mActivity, ArrayList<StudentClassModel> arrayList, ArrayList<StudentClassModel> setstudentClassModels) {
        this.mActivity = mActivity;
        this.arrayList = arrayList;
        this.setstudentClassModels = setstudentClassModels;
        extras = mActivity.getIntent().getExtras();
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

        Log.e("ChekeArra", "ListSide" + arrayList.size());
        StudentClassModel model = arrayList.get(position);
        holder.cb_studentclassname.setText(model.getName());
        holder.cb_studentclassname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                studentClassModel = new StudentClassModel();

                if (b) {
                    model.setSnameFlag(true);
                    Log.e("AdapterisChecked", "ListSide" + holder.cb_studentclassname.isChecked());

                } else {
                    model.setSnameFlag(false);
                    Log.e("AdapterisChecked", "ListSide" + holder.cb_studentclassname.isChecked());

                }

                setstudentClassModels.clear();
                setstudentClassModels.addAll(arrayList);
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
