package com.example.attendencemanager_miniproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    private final static String TABLE_NAME="student";
    private final static String DATABASE_NAME="mydb";
    private final static int DATABASE_VERSION=1;
    private final static String COL1="subject_name";
    private final static String COL2="prefix";
    private final static String COL3="firstno";
    private final static String COL4="lastno";
    private Context context;

    SQLiteDatabase db;

    public MyDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TABLE_NAME+" ("+COL1+" VARCHAR,"+COL2+" VARCHAR,"+COL3+" VARCHAR,"+COL4+" VARCHAR"+")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public void storeRangeInSQLite(String subjectName,String prefix,String firstno,String lastno){
        db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1,subjectName);
        cv.put(COL2,prefix);
        cv.put(COL3,firstno);
        cv.put(COL4,lastno);
        db.insert(TABLE_NAME,null,cv);
        db.close();
    }

    //takes subject name and returns arraylist of rangemodel having range associated with subjectname
    public ArrayList<RangeModel> getRangeFromSQLite(String subjectName){

        ArrayList<RangeModel> arr= new ArrayList<>();
        db= this.getReadableDatabase();


        String sql = "SELECT * FROM "+TABLE_NAME;

        Cursor cr = db.rawQuery(sql,null);

        while(cr.moveToNext()){
            RangeModel rm = new RangeModel();
            if(cr.getString(0).equals(subjectName))
            {
                rm.prefix = cr.getString(1);
                rm.firstno=Integer.parseInt(cr.getString(2));
                rm.lastno=Integer.parseInt(cr.getString(3));
                arr.add(rm);
            }
        }


        return arr;
    }

}
