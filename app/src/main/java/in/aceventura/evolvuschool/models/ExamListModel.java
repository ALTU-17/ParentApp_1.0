package in.aceventura.evolvuschool.models;

import java.io.Serializable;

/**
 * Created by "Manoj Waghmare" on 14,Aug,2020
 **/
public class ExamListModel implements Serializable {
    private String examName, className, subjectName, question_bank_id, exam_id, class_id,
            subject_id, qb_type, create_date, teacher_id, complete, acd_yr, section_id, status,
            qp_id, weightage, sm_id, image_list, qb_name, download_url, stud_id,
            student_attempt_status, instructions;

    public ExamListModel(String examName, String className, String subjectName,
                         String question_bank_id, String exam_id, String class_id,
                         String subject_id, String qb_type, String create_date, String teacher_id,
                         String complete, String acd_yr, String section_id, String status,
                         /*String qp_id,*/ String weightage, /*String sm_id,*/ String image_list,
                         String qb_name, String download_url, String stud_id,
                         String student_attempt_status, String instructions) {
        this.examName = examName;
        this.className = className;
        this.subjectName = subjectName;
        this.question_bank_id = question_bank_id;
        this.exam_id = exam_id;
        this.class_id = class_id;
        this.subject_id = subject_id;
        this.qb_type = qb_type;
        this.create_date = create_date;
        this.teacher_id = teacher_id;
        this.complete = complete;
        this.acd_yr = acd_yr;
        this.section_id = section_id;
        this.status = status;
        /*this.qp_id = qp_id;*/
        this.weightage = weightage;
        /*this.sm_id = sm_id;*/
        this.image_list = image_list;
        this.qb_name = qb_name;
        this.download_url = download_url;
        this.stud_id = stud_id;
        this.student_attempt_status = student_attempt_status;
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStudent_attempt_status() {
        return student_attempt_status;
    }

    public void setStudent_attempt_status(String student_attempt_status) {
        this.student_attempt_status = student_attempt_status;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getQb_name() {
        return qb_name;
    }

    public void setQb_name(String qb_name) {
        this.qb_name = qb_name;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getQb_type() {
        return qb_type;
    }

    public void setQb_type(String qb_type) {
        this.qb_type = qb_type;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getAcd_yr() {
        return acd_yr;
    }

    public void setAcd_yr(String acd_yr) {
        this.acd_yr = acd_yr;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQp_id() {
        return qp_id;
    }

    public void setQp_id(String qp_id) {
        this.qp_id = qp_id;
    }

    public String getWeightage() {
        return weightage;
    }

    public void setWeightage(String weightage) {
        this.weightage = weightage;
    }

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getImage_list() {
        return image_list;
    }

    public void setImage_list(String image_list) {
        this.image_list = image_list;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
