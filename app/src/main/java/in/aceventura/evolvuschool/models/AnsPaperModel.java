package in.aceventura.evolvuschool.models;

import java.util.ArrayList;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class AnsPaperModel {
    private String fileName,question_bank_id;
    private ArrayList<String > fileSizeArray,fileNameArray;

    public AnsPaperModel(String fileName, String question_bank_id) {
        this.fileName = fileName;
        this.question_bank_id= question_bank_id;
    }

    public AnsPaperModel(String fileName, String question_bank_id,ArrayList<String>fileSizeArray,
                         ArrayList<String>fileNameArray) {

        this.fileName = fileName;
        this.question_bank_id= question_bank_id;
        this.fileSizeArray = fileSizeArray;
        this.fileNameArray = fileNameArray;
    }

    public ArrayList<String> getFileSizeArray() {
        return fileSizeArray;
    }

    public void setFileSizeArray(ArrayList<String> fileSizeArray) {
        this.fileSizeArray = fileSizeArray;
    }

    public ArrayList<String> getFileNameArray() {
        return fileNameArray;
    }

    public void setFileNameArray(ArrayList<String> fileNameArray) {
        this.fileNameArray = fileNameArray;
    }

    public String getQuestion_bank_id() {
        return question_bank_id;
    }

    public void setQuestion_bank_id(String question_bank_id) {
        this.question_bank_id = question_bank_id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}