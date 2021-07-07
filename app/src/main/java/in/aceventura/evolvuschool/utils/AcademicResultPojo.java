package in.aceventura.evolvuschool.utils;

import java.io.Serializable;

public class AcademicResultPojo implements Serializable {
    private String examName,examId;

    public AcademicResultPojo(String examName, String examId) {
        this.examName = examName;
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }
}
