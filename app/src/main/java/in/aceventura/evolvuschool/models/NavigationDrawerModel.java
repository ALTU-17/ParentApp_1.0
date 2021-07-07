package in.aceventura.evolvuschool.models;

public class NavigationDrawerModel {
    String nId;
    String nName;
    int nImage;

    public NavigationDrawerModel(String nId, String nName, int nImage) {
        this.nId = nId;
        this.nName = nName;
        this.nImage = nImage;
    }

    public NavigationDrawerModel() {

    }

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }

    public int getnImage() {
        return nImage;
    }

    public void setnImage(int nImage) {
        this.nImage = nImage;
    }
}
