package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aceventura.evolvuschool.DataSet;
import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.Sqlite.DatabaseHelper;

public class SubjectClassAdapter extends RecyclerView.Adapter<SubjectClassAdapter.ViewHolder> {
    Activity mActivity;
    ArrayList<String> arrayList;


    public SubjectClassAdapter(Activity mActivity, ArrayList<String> arrayList) {
        this.mActivity = mActivity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_studentclass_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_studentSubject.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_studentSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_studentSubject = itemView.findViewById(R.id.tv_studentSubject);
        }
    }
}
