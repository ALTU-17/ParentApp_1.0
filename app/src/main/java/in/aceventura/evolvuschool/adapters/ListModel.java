package in.aceventura.evolvuschool.adapters;

public class ListModel {

    public String class_id;
    public String section_id;
    private String first_name;
    private String class_name;
    private String section_name;
    private String teacher_name;
    private String roll_no;
    private String stud_id;
    private String gender;
    private String absent_dates;
    private String total_noti;
    private String total_teachernotes;
    private String total_homeworks;
    private String total_remarks;
    private String total_notices;


    //==================BirthdayModel========================
    private String birthday;

    //==================NewsRVModel========================
    private String news_title;
    private String news_description;
    private String news_date;
    private String news_url;
    private String news_id;
    private String news_image_name;

    //==================EvolvuUpdateRVModel======================
    private String evolvuSubject;
    private String evolvuDetails;
    private String evolvuImgUrl;
    private String evolvuUpdate_Id;

    //==================ImpRVModel=========================
    private String impLinksSubject;
    private String impLinksDetails;
    private String impLinksUrl;

    //==================ResultModel========================
    private String resSubject;
    private String resMarkHeading;
    private String resMarkObtained;

    //=====================================================
    public ListModel(String first_name, String class_name, String section_name, String teacher_name, String roll_no, String stud_id, String class_id, String section_id, String gender, String class_teacher) {
        this.first_name = first_name;
        this.class_name = class_name;
        this.section_name = section_name;
        this.teacher_name = teacher_name;
        this.roll_no = roll_no;
        this.stud_id = stud_id;
        this.class_id = class_id;
        this.section_id = section_id;
        this.gender = gender;
    }
    public ListModel() {

    }

    String getResSubject() {
        return resSubject;
    }

    public void setResSubject(String resSubject) {
        this.resSubject = resSubject;
    }

    String getResMarkHeading() {
        return resMarkHeading;
    }

    public void setResMarkHeading(String resMarkHeading) {
        this.resMarkHeading = resMarkHeading;
    }

    String getResMarkObtained() {
        return resMarkObtained;
    }

    public void setResMarkObtained(String resMarkObtained) {
        this.resMarkObtained = resMarkObtained;
    }

    //==================ResultModel========================


    String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }
    //=====================================================

    String getEvolvuSubject() {
        return evolvuSubject;
    }

    public void setEvolvuSubject(String evolvuSubject) {
        this.evolvuSubject = evolvuSubject;
    }

    String getEvolvuDetails() {
        return evolvuDetails;
    }

    public void setEvolvuDetails(String evolvuDetails) {
        this.evolvuDetails = evolvuDetails;
    }

    String getEvolvuImgUrl() {
        return evolvuImgUrl;
    }

    public void setEvolvuImgUrl(String evolvuImgUrl) {
        this.evolvuImgUrl = evolvuImgUrl;
    }

    //=====================================================

    String getImpLinksSubject() {
        return impLinksSubject;
    }

    public void setImpLinksSubject(String impLinksSubject) {
        this.impLinksSubject = impLinksSubject;
    }

    String getImpLinksDetails() {
        return impLinksDetails;
    }

    public void setImpLinksDetails(String impLinksDetails) {
        this.impLinksDetails = impLinksDetails;
    }

    String getImpLinksUrl() {
        return impLinksUrl;
    }

    public void setImpLinksUrl(String impLinksUrl) {
        this.impLinksUrl = impLinksUrl;
    }

    //=====================================================
    String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }


    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }


    String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAbsent_dates() {
        return absent_dates;
    }

    public void setAbsent_dates(String absent_dates) {
        this.absent_dates = absent_dates;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTotal_noti() {
        return total_noti;
    }

    public void setTotal_noti(String total_noti) {
        this.total_noti = total_noti;
    }

    public String getTotal_teachernotes() {
        return total_teachernotes;
    }

    public void setTotal_teachernotes(String total_teachernotes) {
        this.total_teachernotes = total_teachernotes;
    }

    public String getTotal_homeworks() {
        return total_homeworks;
    }

    public void setTotal_homeworks(String total_homeworks) {
        this.total_homeworks = total_homeworks;
    }

    public String getTotal_remarks() {
        return total_remarks;
    }

    public void setTotal_remarks(String total_remarks) {
        this.total_remarks = total_remarks;
    }

    public String getTotal_notices() {
        return total_notices;
    }

    public void setTotal_notices(String total_notices) {
        this.total_notices = total_notices;
    }

    String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    String getNews_image_name() {
        return news_image_name;
    }

    public void setNews_image_name(String news_image_name) {
        this.news_image_name = news_image_name;
    }

    String getEvolvuUpdate_Id() {
        return evolvuUpdate_Id;
    }

    public void setEvolvuUpdate_Id(String evolvuUpdate_Id) {
        this.evolvuUpdate_Id = evolvuUpdate_Id;
    }
}
