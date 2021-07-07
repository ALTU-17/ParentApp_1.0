package in.aceventura.evolvuschool;

public class DataSet {
    //Remark - Teacher Module Functions Starts Here
    private String tmRPublish, tmRRID, tmRAcknoledge, tmRTid, tmRSubName, tmRClsName, tmRSecName, tmRSectionID, tmRSubjectID, tmRClassID, tmRDate, tmRSubject, tmRDescription, tmRStudID, tmRStudFName, tmRStudMName, tmRStudLName;
    //Homework Teacher Module Functions Starts Here
    private String tmHPublish, tmHTid, tmHHid, tmHSubName, tmHClsName, tmHSecName, tmHSection, tmHSubject, tmHClass, tmHSDate, tmHEDate, tmHDescription;
    private String tnImagelist;
    private String tniDate;
    private String sid, pid;
    private String maleGender, femaleGender;
    //Book Availability
    private String bCategory, bTitle, bAuthor, bCategoryName;
    //Class Timetable
    private String classIn, classOut, satClassIn, satClassOut, ttMonday, ttTuesday, ttWednesday, ttThursday, ttFriday, ttSaturday;
    private Integer periodNo;
    private String Cirachievement_id, CirCevent, Circlass_id, Cirsection_id, Cirstudent_id, Cirposition, Cirachievement, Cirdescription, Cirpublish, Ciracademic_yr, Cirfirst_name, Cirmid_name;
    long Cirdate;

    public String getCirachievement_id() {
        return Cirachievement_id;
    }

    public void setCirachievement_id(String cirachievement_id) {
        Cirachievement_id = cirachievement_id;
    }

    public String getCirCevent() {
        return CirCevent;
    }

    public void setCirCevent(String cirCevent) {
        CirCevent = cirCevent;
    }

    public long getCirdate() {
        return Cirdate;
    }

    public void setCirdate(long cirdate) {
        Cirdate = cirdate;
    }

    public String getCirclass_id() {
        return Circlass_id;
    }

    public void setCirclass_id(String circlass_id) {
        Circlass_id = circlass_id;
    }

    public String getCirsection_id() {
        return Cirsection_id;
    }

    public void setCirsection_id(String cirsection_id) {
        Cirsection_id = cirsection_id;
    }

    public String getCirstudent_id() {
        return Cirstudent_id;
    }

    public void setCirstudent_id(String cirstudent_id) {
        Cirstudent_id = cirstudent_id;
    }

    public String getCirposition() {
        return Cirposition;
    }

    public void setCirposition(String cirposition) {
        Cirposition = cirposition;
    }

    public String getCirachievement() {
        return Cirachievement;
    }

    public void setCirachievement(String cirachievement) {
        Cirachievement = cirachievement;
    }

    public String getCirdescription() {
        return Cirdescription;
    }

    public void setCirdescription(String cirdescription) {
        Cirdescription = cirdescription;
    }

    public String getCirpublish() {
        return Cirpublish;
    }

    public void setCirpublish(String cirpublish) {
        Cirpublish = cirpublish;
    }

    public String getCiracademic_yr() {
        return Ciracademic_yr;
    }

    public void setCiracademic_yr(String ciracademic_yr) {
        Ciracademic_yr = ciracademic_yr;
    }

    public String getCirfirst_name() {
        return Cirfirst_name;
    }

    public void setCirfirst_name(String cirfirst_name) {
        Cirfirst_name = cirfirst_name;
    }

    public String getCirmid_name() {
        return Cirmid_name;
    }

    public void setCirmid_name(String cirmid_name) {
        Cirmid_name = cirmid_name;
    }

    public String getCirlast_name() {
        return Cirlast_name;
    }

    public void setCirlast_name(String cirlast_name) {
        Cirlast_name = cirlast_name;
    }

    private String Cirlast_name;

    //Homework
    private String hClass, hSubject, hSDate, hEDate, hDescription, hStatus, hTComment, hPComment, hSection, hId, cId;

    //Teacher Note
    private String tnId, tnClass, tnSubject, tnDate, tnDescription, tnFile, tnTname, tnSection, tnSectionId, note_file, note_size;

    //Notice
    private String nClass, nDate, nFromDate, nToDate, nStartTime, tnNotelist, notice_attachment, nEndTime, nSubject, nDescription, nFile, nType, nTeacher, nId;

    //Remark
    private String rSubject, rDate, rRemarkSubject, rDescription, rTeachername, rId, rAck;

    private String name, image;
    private int year;
    private String source;
    private String read_status, class_id, section_id, hwRead_status, rmkRead_status;
    private String noticeRead;
    private String updateImages;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    //Student Gender
    @Override
    public String toString() {
        return "DataSet{" +
                "maleGender='" + maleGender + '\'' +
                ", femaleGender='" + femaleGender + '\'' +
                ", hClass='" + hClass + '\'' +
                ", hSubject='" + hSubject + '\'' +
                ", hSDate='" + hSDate + '\'' +
                ", hEDate='" + hEDate + '\'' +
                ", hDescription='" + hDescription + '\'' +
                ", hStatus='" + hStatus + '\'' +
                ", hTComment='" + hTComment + '\'' +
                ", hPComment='" + hPComment + '\'' +
                ", hSection='" + hSection + '\'' +
                ", hId='" + hId + '\'' +
                ", cId='" + cId + '\'' +
                ", tnId='" + tnId + '\'' +
                ", tnClass='" + tnClass + '\'' +
                ", tnSubject='" + tnSubject + '\'' +
                ", tnDate='" + tnDate + '\'' +
                ", tnDescription='" + tnDescription + '\'' +
                ", tnImagelist='" + tnImagelist + '\'' +
                ", tnNotelist='" + tnNotelist + '\'' +
                ", tnTname='" + tnTname + '\'' +
                ", nClass='" + nClass + '\'' +
                ", nDate='" + nDate + '\'' +
                ", nFromDate='" + nFromDate + '\'' +
                ", nToDate='" + nToDate + '\'' +
                ", nStartTime='" + nStartTime + '\'' +
                ", nEndTime='" + nEndTime + '\'' +
                ", nSubject='" + nSubject + '\'' +
                ", nDescription='" + nDescription + '\'' +
                ", nFile='" + nFile + '\'' +
                ", nType='" + nType + '\'' +
                ", nTeacher='" + nTeacher + '\'' +
                ", rSubject='" + rSubject + '\'' +
                ", rDate='" + rDate + '\'' +
                ", rRemarkSubject='" + rRemarkSubject + '\'' +
                ", rDescription='" + rDescription + '\'' +
                ", rTeachername='" + rTeachername + '\'' +
                ", note_file='" + note_file + '\'' +
                ", notice_attachment='" + notice_attachment + '\'' +
                ", rId='" + rId + '\'' +
                ", rAck='" + rAck + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", year=" + year +
                ", source='" + source + '\'' +
                ", worth='" + read_status + '\'' +
                ", cid='" + class_id + '\'' +
                ", sid='" + section_id + '\'' +
                ", hwread='" + hwRead_status + '\'' +
                ", rmkread='" + rmkRead_status + '\'' +
                '}';
    }

    public String getRmkRead_status() {
        return rmkRead_status;
    }

    void setRmkRead_status(String rmkRead_status) {
        this.rmkRead_status = rmkRead_status;
    }

    public String getHwRead_status() {
        return hwRead_status;
    }

    void setHwRead_status(String hwRead_status) {
        this.hwRead_status = hwRead_status;
    }

    public String getSection_id() {
        return section_id;
    }

    void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getClass_id() {
        return class_id;
    }

    void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getRead_status() {
        return read_status;
    }

    void setRead_status(String read_status) {
        this.read_status = read_status;
    }


    public String getTmRRID() {
        return tmRRID;
    }

    public void setTmRRID(String tmRRID) {
        this.tmRRID = tmRRID;
    }

    public String getTmRPublish() {
        return tmRPublish;
    }

    public void setTmRPublish(String tmRPublish) {
        this.tmRPublish = tmRPublish;
    }

    public String getTmRAcknoledge() {
        return tmRAcknoledge;
    }

    public void setTmRAcknoledge(String tmRAcknoledge) {
        this.tmRAcknoledge = tmRAcknoledge;
    }

    public String getTmRTid() {
        return tmRTid;
    }

    public void setTmRTid(String tmRTid) {
        this.tmRTid = tmRTid;
    }

    public String getTmRSubName() {
        return tmRSubName;
    }

    public void setTmRSubName(String tmRSubName) {
        this.tmRSubName = tmRSubName;
    }

    public String getTmRClsName() {
        return tmRClsName;
    }

    public void setTmRClsName(String tmRClsName) {
        this.tmRClsName = tmRClsName;
    }

    public String getTmRSecName() {
        return tmRSecName;
    }

    public void setTmRSecName(String tmRSecName) {
        this.tmRSecName = tmRSecName;
    }

    public String getTmRSectionID() {
        return tmRSectionID;
    }

    public void setTmRSectionID(String tmRSectionID) {
        this.tmRSectionID = tmRSectionID;
    }

    public String getTmRSubjectID() {
        return tmRSubjectID;
    }

    public void setTmRSubjectID(String tmRSubjectID) {
        this.tmRSubjectID = tmRSubjectID;
    }

    public String getTmRClassID() {
        return tmRClassID;
    }

    public void setTmRClassID(String tmRClassID) {
        this.tmRClassID = tmRClassID;
    }

    public String getTmRDate() {
        return tmRDate;
    }

    public void setTmRDate(String tmRDate) {
        this.tmRDate = tmRDate;
    }

    public String getTmRSubject() {
        return tmRSubject;
    }

    public void setTmRSubject(String tmRSubject) {
        this.tmRSubject = tmRSubject;
    }

    public String getTmRDescription() {
        return tmRDescription;
    }

    public void setTmRDescription(String tmRDescription) {
        this.tmRDescription = tmRDescription;
    }

    public String getTmRStudID() {
        return tmRStudID;
    }

    public void setTmRStudID(String tmRStudID) {
        this.tmRStudID = tmRStudID;
    }

    public String getTmRStudFName() {
        return tmRStudFName;
    }

    public void setTmRStudFName(String tmRStudFName) {
        this.tmRStudFName = tmRStudFName;
    }

    public String getTmRStudMName() {
        return tmRStudMName;
    }

    public void setTmRStudMName(String tmRStudMName) {
        this.tmRStudMName = tmRStudMName;
    }

    public String getTmRStudLName() {
        return tmRStudLName;
    }

    public void setTmRStudLName(String tmRStudLName) {
        this.tmRStudLName = tmRStudLName;
    }

    public String getTmHSubName() {
        return tmHSubName;
    }

    public void setTmHSubName(String tmHSubName) {
        this.tmHSubName = tmHSubName;
    }

    public String getTmHClsName() {
        return tmHClsName;
    }

    public void setTmHClsName(String tmHClsName) {
        this.tmHClsName = tmHClsName;
    }

    public String getTmHSecName() {
        return tmHSecName;
    }

    public void setTmHSecName(String tmHSecName) {
        this.tmHSecName = tmHSecName;
    }

    public String getTmHDescription() {
        return tmHDescription;
    }

    public void setTmHDescription(String tmHDescription) {
        this.tmHDescription = tmHDescription;
    }

    public String getTmHPublish() {
        return tmHPublish;
    }

    public void setTmHPublish(String tmHPublish) {
        this.tmHPublish = tmHPublish;
    }

    public String getTmHTid() {
        return tmHTid;
    }

    public void setTmHTid(String tmHTid) {
        this.tmHTid = tmHTid;
    }

    public String getTmHHid() {
        return tmHHid;
    }

    public void setTmHHid(String tmHHid) {
        this.tmHHid = tmHHid;
    }

    public String getTmHSection() {
        return tmHSection;
    }

    public void setTmHSection(String tmHSection) {
        this.tmHSection = tmHSection;
    }

    public String getTmHSubject() {
        return tmHSubject;
    }

    public void setTmHSubject(String tmHSubject) {
        this.tmHSubject = tmHSubject;
    }

    public String getTmHClass() {
        return tmHClass;
    }

    public void setTmHClass(String tmHClass) {
        this.tmHClass = tmHClass;
    }

    public String getTmHSDate() {
        return tmHSDate;
    }

    public void setTmHSDate(String tmHSDate) {
        this.tmHSDate = tmHSDate;
    }

    public String getTmHEDate() {
        return tmHEDate;
    }

    public void setTmHEDate(String tmHEDate) {
        this.tmHEDate = tmHEDate;
    }

    //Remark Constructors Start Here
    public String getrSubject() {

        return rSubject;
    }

    public void setrSubject(String rSubject) {

        this.rSubject = rSubject;
    }


    public String getrDate() {
        return rDate;
    }

    void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public String getrRemarkSubject() {

        return rRemarkSubject;
    }

    void setrRemarkSubject(String rRemarkSubject) {

        this.rRemarkSubject = rRemarkSubject;
    }

    public String getrDescription() {

        return rDescription;
    }

    void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public String getrTName() {

        return rTeachername;
    }

    void setrTName(String rTeachername) {
        this.rTeachername = rTeachername;
    }


    public String getrId() {

        return rId;
    }

    void setrId(String rId) {
        this.rId = rId;
    }

    public String getrAck() {

        return rAck;
    }

    void setrAck(String rAck) {
        this.rAck = rAck;
    }

    //Remark Constructors Ends Here


    //Teacher Note Functions Start Here

    public String getTnId() {
        return tnId;
    }

    void setTnId(String tnId) {
        this.tnId = tnId;
    }


    public String getTnClass() {
        return tnClass;
    }

    void setTnClass(String tnClass) {
        this.tnClass = tnClass;
    }

    public String getTnSection() {
        return tnSection;
    }

    void setTnSection(String tnSection) {
        this.tnSection = tnSection;
    }

    public String getTnSectionId() {
        return tnSectionId;
    }

    void setTnSectionId(String tnSectionId) {
        this.tnSectionId = tnSectionId;
    }

    public String getTnSubject() {
        return tnSubject;
    }

    void setTnSubject(String tnSubject) {
        this.tnSubject = tnSubject;
    }

    public String getTnDate() {
        return tnDate;
    }

    void setTnDate(String tnDate) {
        this.tnDate = tnDate;
    }

    public String getiDate() {
        return tniDate;
    }

    public void setiDate(String tniDate) {
        this.tniDate = tniDate;
    }


    public String getTnDescription() {
        return tnDescription;
    }

    void setTnDescription(String tnDescription) {
        this.tnDescription = tnDescription;
    }

    /*
        public String getTnFile() {
            return tnFile;
        }

        public void setTnFile(String tnFile) {

            this.tnFile = tnFile;
        }
    */
    public String getTnImagelist() {
        return tnImagelist;
    }

    void setTnImagelist(String tnImagelist) {

        this.tnImagelist = tnImagelist;
    }

    public String getTnTname() {
        return tnTname;
    }

    void setTnTname(String tnTname) {
        this.tnTname = tnTname;
    }

    //get attachment name
    public String getnote_file() {
        return note_file;
    }

    public void setnote_file(String note_file) {
        this.note_file = note_file;
    }

    //get attachment size
    public String getnote_size() {
        return note_size;
    }

    public void setnote_size(String note_size) {
        this.note_size = note_size;
    }


    //Teacher Note Functions End Here


    //Homework Functions Start Here
    public String getsClass() {
        return hClass;
    }

    void setsClass(String hClass) {
        this.hClass = hClass;
    }

    public String getSubject() {
        return hSubject;
    }

    public void setSubject(String hSubject) {
        this.hSubject = hSubject;
    }

    public String getsDate() {
        return hSDate;
    }

    void setsDate(String hSDate) {
        this.hSDate = hSDate;
    }

    public String geteDate() {
        return hEDate;
    }

    void seteDate(String hEDate) {
        this.hEDate = hEDate;
    }

    public String getDescription() {
        return hDescription;
    }

    public void setDescription(String hDescription) {
        this.hDescription = hDescription;
    }


    public String getStatus() {
        return hStatus;
    }

    void setStatus(String hStatus) {
        this.hStatus = hStatus;
    }

    public String getPcomment() {
        return hPComment;
    }

    void setPcomment(String hPComment) {
        this.hPComment = hPComment;
    }

    public String getTcomment() {
        return hTComment;
    }

    void setTcomment(String hTComment) {
        this.hTComment = hTComment;
    }

    public String getSection() {
        return hSection;
    }

    public void setSection(String hSection) {
        this.hSection = hSection;
    }

    public String getHomeworkId() {
        return hId;
    }

    void setHomeworkId(String hId) {
        this.hId = hId;
    }

    public String getCommentId() {
        return cId;
    }

    void setCommentId(String cId) {
        this.cId = cId;
    }

    //Homework Functions End Here


    //Notice Functions Start Here
    public String getnClass() {
        return nClass;
    }

    void setnClass(String nClass) {
        this.nClass = nClass;
    }


    public String getnDate() {
        return nDate;
    }

    void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public String getnFromDate() {
        return nFromDate;
    }

    public void setnFromDate(String nFromDate) {
        this.nFromDate = nFromDate;
    }

    public String getnToDate() {
        return nToDate;
    }

    public void setnToDate(String nToDate) {
        this.nToDate = nToDate;
    }

    public String getnStartTime() {
        return nStartTime;
    }

    public void setnStartTime(String nStartTime) {
        this.nStartTime = nStartTime;
    }

    public String getnEndTime() {
        return nEndTime;
    }

    public void setnEndTime(String nEndTime) {
        this.nEndTime = nEndTime;
    }

    public String getnFile() {
        return nFile;
    }

    public void setnFile(String nFile) {
        this.nFile = nFile;
    }

    public String getnId() {
        return nId;
    }

    void setnId(String nId) {
        this.nId = nId;
    }

    public String getnSubject() {
        return nSubject;
    }

    void setnSubject(String nSubject) {
        this.nSubject = nSubject;
    }

    public String getnDescription() {
        return nDescription;
    }

    void setnDescription(String nDescription) {
        this.nDescription = nDescription;
    }

    public String getnType() {
        return nType;
    }

    void setnType(String nType) {
        this.nType = nType;
    }

    public String getnTeacher() {
        return nTeacher;
    }

    void setnTeacher(String nTeacher) {
        this.nTeacher = nTeacher;
    }


    String getnotice_attachment() {
        return notice_attachment;
    }

    void setnotice_attachment(String notice_attachment) {
        this.notice_attachment = notice_attachment;
    }

    public String getTnNotelist() {
        return tnNotelist;
    }

    public void setTnNotelist(String tnNotelist) {

        this.tnNotelist = tnNotelist;
    }

    //Notice Functions End Here


    //Book Availability Abstract Methods Start Here

    public String getBookCategory() {
        return bCategory;
    }

    public void setBookCategory(String bCategory) {
        this.bCategory = bCategory;
    }

    public String getbTitle() {
        return bTitle;
    }

    void setbTitle(String bTitle) {
        this.bTitle = bTitle;
    }

    public String getbAuthor() {
        return bAuthor;
    }

    void setbAuthor(String bAuthor) {
        this.bAuthor = bAuthor;
    }

    public String getbCategoryName() {
        return bCategoryName;
    }

    void setbCategoryName(String bCategoryName) {
        this.bCategoryName = bCategoryName;
    }


    //Book Availability Abstract Methods End Here


    //Class Timetable Abstract Methods Start Here
    public Integer getPeriodNo() {
        return periodNo;
    }

    void setPeriodNo(Integer periodNo) {
        this.periodNo = periodNo;
    }

    public String getClassIn() {
        return classIn;
    }

    void setClassIn(String classIn) {
        this.classIn = classIn;
    }

    public String getClassOut() {
        return classOut;
    }

    void setClassOut(String classOut) {
        this.classOut = classOut;
    }

    public String getSatClassIn() {
        return satClassIn;
    }

    void setSatClassIn(String satClassIn) {
        this.satClassIn = satClassIn;
    }

    public String getSatClassOut() {
        return satClassOut;
    }

    public void setSatClassOut(String satClassOut) {
        this.satClassOut = satClassOut;
    }

    public String getTtMonday() {
        return ttMonday;
    }

    public void setTtMonday(String ttMonday) {
        this.ttMonday = ttMonday;
    }

    public String getTtTuesday() {
        return ttTuesday;
    }

    public void setTtTuesday(String ttTuesday) {
        this.ttTuesday = ttTuesday;
    }

    public String getTtWednesday() {
        return ttWednesday;
    }

    public void setTtWednesday(String ttWednesday) {
        this.ttWednesday = ttWednesday;
    }

    public String getTtThursday() {
        return ttThursday;
    }

    public void setTtThursday(String ttThursday) {
        this.ttThursday = ttThursday;
    }

    public String getTtFriday() {
        return ttFriday;
    }

    public void setTtFriday(String ttFriday) {
        this.ttFriday = ttFriday;
    }

    public String getTtSaturday() {
        return ttSaturday;
    }

    public void setTtSaturday(String ttSaturday) {
        this.ttSaturday = ttSaturday;
    }

    public String getMaleGender() {
        return maleGender;
    }

    public void setMaleGender(String maleGender) {
        this.maleGender = maleGender;
    }

    public String getFemaleGender() {
        return femaleGender;
    }

    public void setFemaleGender(String femaleGender) {
        this.femaleGender = femaleGender;
    }


    public String getNoticeRead() {
        return noticeRead;
    }

    public void setNoticeRead(String noticeRead) {
        this.noticeRead = noticeRead;
    }

    public String getUpdateImages() {
        return updateImages;
    }

    public void setUpdateImages(String updateImages) {
        this.updateImages = updateImages;
    }
}
