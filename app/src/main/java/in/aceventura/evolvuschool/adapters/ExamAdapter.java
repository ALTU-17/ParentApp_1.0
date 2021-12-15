package in.aceventura.evolvuschool.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import in.aceventura.evolvuschool.R;
import in.aceventura.evolvuschool.models.Detail_result;
import in.aceventura.evolvuschool.viewholders.ExamViewHolder;
import in.aceventura.evolvuschool.viewholders.ResultViewHolder;

public class ExamAdapter extends ExpandableRecyclerViewAdapter<ExamViewHolder, ResultViewHolder> {
    Context context;
    String sid, term_id1, term_id2;
    Activity mActivity;
    String mCBSC;
    String Exam_Name_Term_Id;
    String class_name;

    public ExamAdapter(Activity mActivity,String class_name, Context context, String mCBSC, String Exam_Name_Term_Id, String term_id1, String term_id2, String Sid, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context = context;
        this.sid = Sid;
        this.term_id1 = term_id1;
        this.term_id2 = term_id2;
        this.mActivity = mActivity;
        this.mCBSC = mCBSC;
        this.class_name = class_name;
        this.Exam_Name_Term_Id = Exam_Name_Term_Id;
        Log.e("EXAMADAPTER", "VALUES>" + "TERM 1>" + term_id1);
        Log.e("EXAMADAPTER", "VALUES>" + "TERM 2>" + term_id2);
        Log.e("EXAMADAPTER", "VALUES>" + "TERM 2>" + Exam_Name_Term_Id);
        Log.e("EXAMADAPTER", "VALUES>" + "class_name>" + class_name);

    }

    @Override
    public ExamViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_exams, parent, false);
        return new ExamViewHolder(view, class_name,Exam_Name_Term_Id, term_id1, term_id2, mActivity, context, sid);
    }

    @Override
    public ResultViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_result, parent, false);
        return new ResultViewHolder(view, mCBSC);
    }

    @Override
    public void onBindChildViewHolder(ResultViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final Detail_result detail_result = (Detail_result) group.getItems().get(childIndex);

        holder.setSub(detail_result.getSubject());
        Log.e("EXAM", "Subject=" + detail_result.getSubject() + "=Mark_heading=" + detail_result.getMark_heading());

        holder.setHeading(detail_result.getMark_heading());
        holder.setMark(detail_result.getMark_obtained() + "/" + detail_result.getHighest_marks());


    }

    @Override
    public void onBindGroupViewHolder(ExamViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setExamTitle(group.getTitle());
        if (group.getTitle().equalsIgnoreCase("Term 1") || group.getTitle().equalsIgnoreCase("Term 2") || group.getTitle().equalsIgnoreCase("Final exam")) {

            holder.setITEM(context);

        } else {

        }


    }

}
