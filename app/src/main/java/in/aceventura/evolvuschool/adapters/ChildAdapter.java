package in.aceventura.evolvuschool.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.StudentDashboard;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<ListModel> newDataList;
    private Context mContext;
    public boolean showShimmer = true;

    public ChildAdapter(Context mContext, List<ListModel> newDataList) {
        this.newDataList = newDataList;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ViewHolder holder, final int position) {

        if (showShimmer) {
            holder.shimmerFrameLayout4.startShimmer();
        }else {
            holder.shimmerFrameLayout4.startShimmer();
            holder.shimmerFrameLayout4.setShimmer(null);

            holder.student_Name.setBackground(null);
            holder.tv_roll.setBackground(null);
            holder.tv_cs1.setBackground(null);
            holder.tv_ct1.setBackground(null);
            holder.logo.setBackground(null);

            String studentID = newDataList.get(position).getStud_id();
            String classID = newDataList.get(position).getClass_id();
            String sectionID = newDataList.get(position).getSection_id();
            String gender = newDataList.get(position).getGender();

            holder.student_Name.setText(newDataList.get(position).getFirst_name());
            String rn = newDataList.get(position).getRoll_no();
            String teacher = newDataList.get(position).getTeacher_name();

            if (rn.equals("null")) {
                holder.tv_roll.setText("");
            } else {
                holder.tv_roll.setText(rn);
            }

            holder.tv_cs1.setText(newDataList.get(position).getClass_name() + " " + newDataList.get(position).getSection_name());

            if (teacher.equals("null")) {
                holder.tv_ct1.setText("");
            } else {
                holder.tv_ct1.setText(teacher);
            }

            if (gender.equals("M")) {
                holder.logo.setImageResource(R.drawable.boy);
            } else if (gender.equals("F")) {
                holder.logo.setImageResource(R.drawable.girl);
            }
        }

        holder.parentView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StudentDashboard.class);
            intent.putExtra("SID", newDataList.get(position).getStud_id());
            intent.putExtra("CLASSID", newDataList.get(position).getClass_id());
            intent.putExtra("SECTIONID", newDataList.get(position).getSection_id());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // number of shimmer item shown while loading
        int SHIMMER_ITEM_NUMBER = 2;
        return showShimmer ? SHIMMER_ITEM_NUMBER : this.newDataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.child_list, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView student_Name, tv_roll, tv_cs1, tv_ct1;
        ImageView logo;
        ShimmerFrameLayout shimmerFrameLayout4;
        private View parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
            this.student_Name = itemView.findViewById(R.id.student_Name);
            this.tv_roll = itemView.findViewById(R.id.tv_roll);
            this.tv_cs1 = itemView.findViewById(R.id.tv_cs1);
            this.tv_ct1 = itemView.findViewById(R.id.tv_ct1);
            this.logo = itemView.findViewById(R.id.tlogo);
            this.shimmerFrameLayout4 = itemView.findViewById(R.id.shimmer_layout4);
        }
    }
}

