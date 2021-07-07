package in.aceventura.evolvuschool.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import in.aceventura.evolvuschool.R;

public class ResultViewHolder extends ChildViewHolder {

    private TextView sub, heading, mark;
    LinearLayout ll_tv_cbsc;
    String mCBSC;

    public ResultViewHolder(View itemView, String mCBSC) {
        super(itemView);
        this.mCBSC = mCBSC;
        sub = itemView.findViewById(R.id.sub);
        heading = itemView.findViewById(R.id.heading);
        mark = itemView.findViewById(R.id.mark);
    }

    public void setSub(String subject) {
        sub.setText(subject);
    }

    public void setHeading(String mheading) {
        heading.setText(mheading);
    }

    public void setMark(String omark) {
        mark.setText(omark);
    }


}
