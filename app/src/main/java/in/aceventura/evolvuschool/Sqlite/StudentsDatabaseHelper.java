package in.aceventura.evolvuschool.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentsDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "StudentDatabaseHelper";
    private ContentValues contentValues = new ContentValues();

    //Student Database
    private static final String TABLE_NAME = "students_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "studentName";
    private static final String COL_3  = "gender";
    private static final String COL_4  = "roll_no";
    private static final String COL_5  = "class";
    private static final String COL_6  = "section";
    private static final String COL_7  = "teacher_name";
    private static final String COL_8  = "student_id";
    private static final String COL_9  = "class_id";
    private static final String COL_10  = "section_id";
    private static final String COL_11  = "dob";

    public StudentsDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    //Creating the Database...
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," +  COL_7 + " TEXT," + COL_8 + " TEXT," + COL_9 + " TEXT," + COL_10 + " TEXT," + COL_11 + " TEXT)";

        /*String createTable =
                "CREATE TABLE " + TABLE_NAME + COL_1 + " (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," + COL_7 + " TEXT)";*/
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE EXISTS" + TABLE_NAME);
        onCreate(db);

    }

    //Saving short name and Urls into the Database...
    public boolean saveStudentDetails(String studentName,String gender,String rollNo, String Class,String section,String teacherName,String studentId,String class_id, String section_id, String dob){
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues.put(COL_2,studentName);
        contentValues.put(COL_3,gender);
        contentValues.put(COL_4,rollNo);
        contentValues.put(COL_5,Class);
        contentValues.put(COL_6,section);
        contentValues.put(COL_7,teacherName);
        contentValues.put(COL_8,studentId);
        contentValues.put(COL_9,class_id);
        contentValues.put(COL_10,section_id);
        contentValues.put(COL_11,dob);

        long resultSN = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        //if data is inserted incorrectly  it will return -1
        return resultSN != -1;
    }


    //2-Retrieving gender from the Database...
    public String getStudentName(long id) {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        try {
            try (Cursor csr = db.query(TABLE_NAME, null, whereclause, whereargs, null, null, null)) {
                if (csr != null) {
                    if (csr.moveToFirst()) {
                        rv = csr.getString(csr.getColumnIndex(COL_2));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rv;
    }


    //3-Retrieving gender from the Database...
    public String getGender(long id) {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        try {
            try (Cursor csr = db.query(TABLE_NAME, null, whereclause, whereargs, null, null, null)) {
                if (csr != null) {
                    if (csr.moveToFirst()) {
                        rv = csr.getString(csr.getColumnIndex(COL_3));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rv;
    }


    //4-Retrieving rollNo from the Database...
    public String getRollNo(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_4));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //5-Retrieving Class from the Database...
    public String getClass(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_5));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //6-Retrieving division from the Database...
    public String getSection(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_6));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //7-Retrieving teacherName from the Database...
    public String getTeacherName(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_7));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //8-Retrieving studentId from the Database...
    public String getStudentId(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_8));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //9-Retrieving classId from the Database...
    public String getClassId(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_9));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //10-Retrieving sectionId from the Database...
    public String getSectionId(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }

    //11-Retrieving studentDob from the Database...
    public String getDob(long id)
    {
        String rv = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String whereclause = "ID=?";
        String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr = db.query(TABLE_NAME,null,whereclause,whereargs,null,null,null);
        try {
            if (csr != null) {
                if (csr.moveToFirst()) {
                    rv = csr.getString(csr.getColumnIndex(COL_11));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (csr!=null){
                csr.close();
            }
        }
        return rv;
    }


    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void clearData(){
        SQLiteDatabase db = this.getWritableDatabase();

    /*String clear = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clear);*/

        db.delete(TABLE_NAME,null,null);
        db.close();
        db.execSQL("vacuum");
    }

}
