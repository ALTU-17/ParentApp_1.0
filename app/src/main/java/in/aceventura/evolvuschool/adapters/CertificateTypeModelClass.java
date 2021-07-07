package in.aceventura.evolvuschool.adapters;

import java.util.Objects;

public class CertificateTypeModelClass {
    String bc_format_id, name;
    boolean aBoolean;

    public CertificateTypeModelClass() {


    }

    public String getBc_format_id() {
        return bc_format_id;
    }

    public void setBc_format_id(String bc_format_id) {
        this.bc_format_id = bc_format_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    @Override
    public String toString() {
        return "CertificateTypeModelClass[" +
                "bc_format_id='" + bc_format_id + '\'' +
                ", name='" + name + '\'' +
                ", aBoolean=" + aBoolean +
                ']';
    }
}
