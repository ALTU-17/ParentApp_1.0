package in.aceventura.evolvuschool.models;

public class SendBonafideRequisitionDataModel {
    String bonafied_req_id;
    String student_id;
    String bonafied_formate_id;
    String email_id;
    String parent_id;
    String academic_yr;
    String req_acd_yr;
    String bc_name;
    String date;
    String fullname;
    String class_name;

    public String getBc_name() {
        return bc_name;
    }

    public void setBc_name(String bc_name) {
        this.bc_name = bc_name;
    }

    public String getIsGenerated() {
        return IsGenerated;
    }

    public void setIsGenerated(String isGenerated) {
        IsGenerated = isGenerated;
    }

    String IsGenerated;

    public String getReq_acd_yr() {
        return req_acd_yr;
    }

    public void setReq_acd_yr(String req_acd_yr) {
        this.req_acd_yr = req_acd_yr;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public SendBonafideRequisitionDataModel(String bonafied_req_id, String student_id, String bonafied_formate_id, String email_id, String parent_id, String academic_yr, String date) {
        this.bonafied_req_id = bonafied_req_id;
        this.student_id = student_id;
        this.bonafied_formate_id = bonafied_formate_id;
        this.email_id = email_id;
        this.parent_id = parent_id;
        this.academic_yr = academic_yr;
        this.date = date;
    }

    public SendBonafideRequisitionDataModel() {

    }

    public String getBonafied_req_id() {
        return bonafied_req_id;
    }

    public void setBonafied_req_id(String bonafied_req_id) {
        this.bonafied_req_id = bonafied_req_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getBonafied_formate_id() {
        return bonafied_formate_id;
    }

    public void setBonafied_formate_id(String bonafied_formate_id) {
        this.bonafied_formate_id = bonafied_formate_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SendBonafideRequisitionDataModel{" +
                "bonafied_req_id='" + bonafied_req_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", bonafied_formate_id='" + bonafied_formate_id + '\'' +
                ", email_id='" + email_id + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", academic_yr='" + academic_yr + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
