package in.aceventura.evolvuschool.models;

public class Detail_result {
    private String subject,mark_heading,mark_obtained,highest_marks;

    public Detail_result(String subject, String mark_heading, String mark_obtained,String highest_marks) {
        this.subject = subject;
        this.mark_heading = mark_heading;
        this.mark_obtained = mark_obtained;
        this.highest_marks = highest_marks;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMark_heading() {
        return mark_heading;
    }

    public void setMark_heading(String mark_heading) {
        this.mark_heading = mark_heading;
    }

    public String getMark_obtained() {
        return mark_obtained;
    }

    public void setMark_obtained(String mark_obtained) {
        this.mark_obtained = mark_obtained;
    }

    public String getHighest_marks() {
        return highest_marks;
    }

    public void setHighest_marks(String highest_marks) {
        this.highest_marks = highest_marks;
    }

}
