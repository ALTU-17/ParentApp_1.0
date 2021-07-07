package in.aceventura.evolvuschool.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class Exam_list extends ExpandableGroup {
    public Exam_list(String title, ArrayList detail_results) {
        super(title, detail_results);
    }
}
