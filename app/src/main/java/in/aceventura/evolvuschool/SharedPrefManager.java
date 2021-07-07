package in.aceventura.evolvuschool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 7/20/2017.
 */

public class SharedPrefManager {

    static final String KEY_REG_ID = "reg_id";
    private static final String KEY_ROLE_ID = "role_id";
    static final String KEY_PARENT_ID = "parent_id";
    static final String KEY_STUDENT_ID = "student_id";
    static final String KEY_STUDENT_CLASS = "class_id";
    private static final String KEY_STUDENT_SECTION = "section_id";
    private static final String KEY_STUDENT_NAME = "first_name";
    public static final String KEY_ACADEMIC_YEAR = "academic_yr";
    private static final String KEY_COMMENT_ID = "comment_id";
    private static final String KEY_HOMEWORK_ID = "homework_id";
    public static final String KEY_PARENT_COMMENT = "parent_comment";
    public static final String KEY_PHONE_NO = "phone_no";
    private static final String T_USER_ID = "user_id";
    private static final String T_NAME = "name";
    private static final String T_REG_ID = "reg_id";
    private static final String T_ROLE_ID = "role_id";
    private static final String T_ACADEMIC_YEAR = "academic_yr";
    private static final String KEY_SHORT_NAME = "short_name";
    private static final String KEY_URL = "url";
    private static final String KEY_DURL = "dUrl";
    private static final String KEY_APPVERSION = "app_version";
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_ID = "user_id";
    private static final String GENDER = "child_gender";
    private static final String NAME = "child_name";
    private static final String CLASS = "child_class";
    private static final String KEYFLAG = "flag";
    public static final String KEY_CHANGE_ACADEMIC_YEAR = "change_academic_yr";

    private static final String SECTION = "child_section";
    private static final String TEACHER = "child_teacher";
    private static final String ROLLNO = "child_rollno";
    private static final String KEY_PWD = "password";
    private static final String KEY_TIMER = "timer";
    private static final String CLASS_ID = "classid";
    private static final String SECTION_ID = "sectionid";
    private static final String Notiremark_ID = "notiremarkid";
    private static final String ActivityName = "activity";

    private static final String KEY_ONLINE_EXAM_CARD = "online_exam_card";


    private static SharedPrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    private static final String TAG_TOKEN = "tagtoken";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    //saving the time of exam
    void setTime(String time) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TIMER, time);
        editor.apply();
    }
//    }
//    public void setFlag(String flag) {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(KEYFLAG, flag);
//        editor.apply();
//        editor.commit();
//
//    }
//
//    public String getFlag() {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEYFLAG, "");
//    }
//

    public void setNotiremark_ID(String values) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Notiremark_ID, values);
        editor.apply();
        editor.commit();

    }

    public void setActivityName(String values) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ActivityName, values);
        editor.apply();
        editor.commit();

    }

    public String getNotiremark_ID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Notiremark_ID, "");
    }
    public String getActivityName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ActivityName, "");
    }

    boolean setChangeAcademicYear(String academic_yr) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CHANGE_ACADEMIC_YEAR, academic_yr);
        editor.apply();
        return true;
    }

    public String getChangeAcademicYear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CHANGE_ACADEMIC_YEAR, "");
    }

    //getting the time of exam
    public String getTime() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TIMER, "0");
    }


    void setPassword(String pwd) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PWD, pwd);
        editor.apply();
    }

    public String getPassword() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PWD, null);
    }


    boolean childGender(String child_gender) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(GENDER, child_gender);
        editor.apply();

        return true;
    }

    boolean childName(String child_name) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAME, child_name);
        editor.apply();

        return true;
    }

    boolean childClass(String child_class) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(CLASS, child_class);
        editor.apply();

        return true;
    }

    boolean childSection(String child_section) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SECTION, child_section);
        editor.apply();

        return true;
    }

    boolean childTeacher(String child_teacher) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEACHER, child_teacher);
        editor.apply();

        return true;
    }

    boolean childRollNo(String child_rollno) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ROLLNO, child_rollno);
        editor.apply();

        return true;
    }


    public boolean teacherLogin(String user_id, int reg_id, String name, String role_id, String academic_yr) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(T_USER_ID, user_id);
        editor.putInt(T_REG_ID, reg_id);
        editor.putString(T_NAME, name);
        editor.putString(T_ROLE_ID, role_id);
        editor.putString(T_ACADEMIC_YEAR, academic_yr);

        editor.apply();

        return true;
    }


    public boolean studentLogin(int student_id/*, int class_id, int section_id, String first_name, int parent_id*/) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_STUDENT_ID, student_id);
        /*editor.putInt(KEY_STUDENT_CLASS, class_id);
        editor.putInt(KEY_STUDENT_SECTION, section_id);
        editor.putString(KEY_STUDENT_NAME, first_name);
        editor.putInt(KEY_PARENT_ID, parent_id);*/
        editor.apply();

        return true;
    }


    public boolean newLogin(String short_name) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_SHORT_NAME, short_name);
        editor.apply();

        return true;
    }

    public boolean newUrl(String url) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_URL, url);
        editor.apply();

        return true;
    }


    public boolean newdUrl(String dUrl) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_DURL, dUrl);
        editor.apply();

        return true;
    }

    void newAppVersion(String app_version) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_APPVERSION, app_version);
        editor.apply();

    }

    boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, null) != null;
    }

    public boolean isTLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(T_NAME, null) != null;
    }

    boolean setAcademicYear(String academic_yr) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACADEMIC_YEAR, academic_yr);
        editor.apply();
        return true;
    }

    boolean setClassId(String class_id) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLASS_ID, class_id);
        editor.apply();
        return true;
    }

    boolean setSectionId(String section_id) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SECTION_ID, section_id);
        editor.apply();
        return true;
    }

    public boolean homeworkDetails(int comment_id, int homework_id) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_COMMENT_ID, comment_id);
        editor.putInt(KEY_HOMEWORK_ID, homework_id);
        editor.apply();

        return true;
    }

    public boolean userLogin(String user_id, int reg_id, String name, String academic_yr) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, user_id);
        editor.putInt(KEY_REG_ID, reg_id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_ACADEMIC_YEAR, academic_yr);
        editor.apply();

        return true;
    }


    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getShortname() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SHORT_NAME, null);
    }


    public String getUrl() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_URL, null);
    }

    public String getdUrl() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DURL, null);
    }


    public String getAppVersion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_APPVERSION, null);
    }

    public String getUsername() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getChildGender() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(GENDER, null);
    }

    public String getChildName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME, null);
    }

    public String getChildClass() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CLASS, null);
    }

    public String getChildSection() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SECTION, null);
    }

    public String getChildRollNo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ROLLNO, null);
    }

    public String getChildTeacher() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TEACHER, null);
    }

    public String getUserRole() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ROLE_ID, null);
    }


    public Integer getRegId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_REG_ID, 0);
    }

    Integer getParentId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_PARENT_ID, 0);
    }

    Integer getStudentId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_STUDENT_ID, 0);
    }

    Integer getStudentClass() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_STUDENT_CLASS, 0);
    }

    Integer getStudentSection() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_STUDENT_SECTION, 0);
    }

    String getStudentName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_NAME, null);
    }

    public String getAcademicYear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACADEMIC_YEAR, null);
    }

    public String getClassId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CLASS_ID, null);
    }

    public String getSectionId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SECTION_ID, null);
    }

    public Integer getHomeworkId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_HOMEWORK_ID, 0);
    }

    public Integer getCommentId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_COMMENT_ID, 0);
    }

    public String getTUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(T_USER_ID, null);
    }

    public Integer getTRegId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(T_REG_ID, 0);
    }

    public String getTName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(T_NAME, null);
    }

    public String getTRoleId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(T_ROLE_ID, null);
    }

    public String getTAcademicYear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(T_ACADEMIC_YEAR, null);
    }


    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN, null);
    }

    //Guide view Prefs
    void setOnlineExamCardShown() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ONLINE_EXAM_CARD, "Yes");
        editor.apply();
    }

    public String getOnlineExamCardShown() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ONLINE_EXAM_CARD, "No");
    }


}

