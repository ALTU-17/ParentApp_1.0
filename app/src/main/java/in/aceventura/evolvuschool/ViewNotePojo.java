package in.aceventura.evolvuschool;

public class ViewNotePojo {

    private String notesId;
    private String date;
    private String classId;
    private String teacherId;
    private String sectionId;
    private String subjectId;
    private String description;
    private String academicYr;
    private String publish;
    private String name;
    private String classname;
    private String sectionname;
    private String smId;
    private String subjectName;


    public ViewNotePojo(String notesId, String date, String classId, String teacherId, String sectionId, String subjectId, String description, String academicYr, String publish, String name, String classname, String sectionname, String smId, String subjectName) {
        super();
        this.notesId = notesId;
        this.date = date;
        this.classId = classId;
        this.teacherId = teacherId;
        this.sectionId = sectionId;
        this.subjectId = subjectId;
        this.description = description;
        this.academicYr = academicYr;
        this.publish = publish;
        this.name = name;
        this.classname = classname;
        this.sectionname = sectionname;
        this.smId = smId;
        this.subjectName = subjectName;
    }

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcademicYr() {
        return academicYr;
    }

    public void setAcademicYr(String academicYr) {
        this.academicYr = academicYr;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSectionname() {
        return sectionname;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

    public String getSmId() {
        return smId;
    }

    public void setSmId(String smId) {
        this.smId = smId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
