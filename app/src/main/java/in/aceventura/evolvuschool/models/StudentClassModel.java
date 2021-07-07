package in.aceventura.evolvuschool.models;

public class StudentClassModel {
    String name, sid;
    boolean snameFlag;

    public StudentClassModel() {

    }

    public StudentClassModel(String name, String sid, boolean snameFlag) {
        this.name = name;
        this.sid = sid;
        this.snameFlag = snameFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public boolean isSnameFlag() {
        return snameFlag;
    }

    public void setSnameFlag(boolean snameFlag) {
        this.snameFlag = snameFlag;
    }
}
