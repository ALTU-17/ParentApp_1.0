package in.aceventura.evolvuschool;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vinod Patil on 30-08-2017.
 */

public class DataObject {
    @SerializedName("bCategory")
    private String bCategory;

    public DataObject() {
    }

    public String getBookCategory() {
        return bCategory;
    }

    public void setBookCategory(String bCategory) {
        this.bCategory = bCategory;
    }

}