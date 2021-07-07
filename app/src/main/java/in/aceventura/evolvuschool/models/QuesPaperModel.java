package in.aceventura.evolvuschool.models;

/**
 * Created by "Manoj Waghmare" on 19,Aug,2020
 **/
public class QuesPaperModel {
    private String fileName,fileSize,download_url,qb_Id;

    public QuesPaperModel(String fileName, String fileSize,String download_url,String qb_Id) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.download_url = download_url;
        this.qb_Id = qb_Id;
    }

    public String getQb_Id() {
        return qb_Id;
    }

    public void setQb_Id(String qb_Id) {
        this.qb_Id = qb_Id;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
