package in.aceventura.evolvuschool.models;

import java.util.ArrayList;

public class LineChartArray {

    ArrayList<LineChartModel> lineChartModels;
    String examName;

    public ArrayList<LineChartModel> getLineChartModels() {
        return lineChartModels;
    }

    public void setLineChartModels(ArrayList<LineChartModel> lineChartModels) {
        this.lineChartModels = lineChartModels;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }
}
