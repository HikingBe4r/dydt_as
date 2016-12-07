package com.hikingbear.database;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class DBManager extends SQLiteOpenHelper {

    //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT

    public static final String DATABASE_NAME = "scedule_list.db";
    public static final String SCHEDULE_TABLE_NAME = "SCHEDULE_LIST";
    public static final String SCHEDULE_COLUMN_ID = "_id";
    public static final String SCHEDULE_COLUMN_SUBJECT = "subject";
    public static final String SCHEDULE_COLUMN_YEAR = "year";
    public static final String SCHEDULE_COLUMN_MONTH = "month";
    public static final String SCHEDULE_COLUMN_DAY = "day";
    public static final String SCHEDULE_COLUMN_HOUR = "hour";
    public static final String SCHEDULE_COLUMN_MINUTE = "minute";
    public static final String SCHEDULE_COLUMN_DESCRIPTION = "description";
    public static final String SCHEDULE_COLUMN_APP = "app";

    private HashMap hp;


    public DBManager(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql ="CREATE TABLE IF NOT EXISTS SCHEDULE_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, subject TEXT, year LONG, month LONG, day LONG, hour LONG, minute LONG, description TEXT, app TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS SCHEDULE_LIST");
        onCreate(db);
    }
    //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
    public boolean insertSchedule  (String subject, long year, long month, long day, long hour, long minute, String description)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, subject);
        contentValues.put(SCHEDULE_COLUMN_YEAR, year);
        contentValues.put(SCHEDULE_COLUMN_MONTH, month);
        contentValues.put(SCHEDULE_COLUMN_DAY, day);
        contentValues.put(SCHEDULE_COLUMN_HOUR, hour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, minute);
        contentValues.put(SCHEDULE_COLUMN_DESCRIPTION, description);
        db.insert(SCHEDULE_TABLE_NAME, null, contentValues);

        return true;
    }
    public boolean insertSchedule  (String subject, long year, long month, long day, long hour, long minute, String description, String appname)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, subject);
        contentValues.put(SCHEDULE_COLUMN_YEAR, year);
        contentValues.put(SCHEDULE_COLUMN_MONTH, month);
        contentValues.put(SCHEDULE_COLUMN_DAY, day);
        contentValues.put(SCHEDULE_COLUMN_HOUR, hour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, minute);
        contentValues.put(SCHEDULE_COLUMN_DESCRIPTION, description);
        contentValues.put(SCHEDULE_COLUMN_APP, appname);
        db.insert(SCHEDULE_TABLE_NAME, null, contentValues);

        return true;
    }
    public boolean updateSchedule (Integer id, String subject, long year, long month, long day, long hour, long minute, String description)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, subject);
        contentValues.put(SCHEDULE_COLUMN_YEAR, year);
        contentValues.put(SCHEDULE_COLUMN_MONTH, month);
        contentValues.put(SCHEDULE_COLUMN_DAY, day);
        contentValues.put(SCHEDULE_COLUMN_HOUR, hour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, minute);
        contentValues.put(SCHEDULE_COLUMN_DESCRIPTION, description);
        db.update(SCHEDULE_TABLE_NAME, contentValues, "_id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateSchedule (Integer id, String subject, long year, long month, long day, long hour, long minute, String description, String appname)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, subject);
        contentValues.put(SCHEDULE_COLUMN_YEAR, year);
        contentValues.put(SCHEDULE_COLUMN_MONTH, month);
        contentValues.put(SCHEDULE_COLUMN_DAY, day);
        contentValues.put(SCHEDULE_COLUMN_HOUR, hour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, minute);
        contentValues.put(SCHEDULE_COLUMN_DESCRIPTION, description);
        contentValues.put(SCHEDULE_COLUMN_APP, appname);

        db.update(SCHEDULE_TABLE_NAME, contentValues, "_id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean deleteSchedule(int _id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " +
                SCHEDULE_TABLE_NAME +
                " WHERE _id= " +
                _id);

        db.close();
        return true;
    }

    public Cursor fetchAllNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor =  db.rawQuery("SELECT * FROM SCHEDULE_LIST", null);
        //app.myDbHelper.MyDB().query("cardlist", new String[] {"cardid as _id","cardname","carddesc"}, null, null, null, null, null);
        try {
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        }catch(Exception e) {
            return mCursor;
        }
}

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SCHEDULE_LIST where _id="+id+"", null );
        return res;
    }

    public int get_id(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SCHEDULE_LIST where _id="+id+"", null );
        int _id = res.getInt(0);
        return _id;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SCHEDULE_TABLE_NAME);
        return numRows;
    }

    public ArrayList<String> getAllSchedule()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SCHEDULE_LIST", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SCHEDULE_COLUMN_SUBJECT)));
            res.moveToNext();
        }
        return array_list;
    }
}

/*
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    public DBManager (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create new table
        // create table 테이블명 (컬럼명 타입옵션);
        String sql ="CREATE TABLE IF NOT EXISTS SCHEDULE_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // db insert = 계정가입
    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    // 비밀번호 변경에 쓰면 좋을듯.
    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
        Cursor cursor = db.rawQuery("select * from ACCOUNT_LIST", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " : subject: "
                    + cursor.getString(1)
                    + ", year: "
                    + cursor.getInt(2)
                    + ", month: "
                    + cursor.getInt(3)
                    + ", day: "
                    + cursor.getInt(4)
                    + ", hour: "
                    + cursor.getInt(5)
                    + ", minute: "
                    + cursor.getInt(6)
                    + ", description: "
                    + cursor.getString(7)
                    + "\n";
        }
        return str;
    }


    // 중복확인이긴한데 일정이라 중복확인 할 일이 없음.
    /*
    public boolean DepulicateEmail (String inputEmail) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("SELECT email FROM ACCOUNT_LIST", null);

        while(cursor.moveToNext()) {
            str += cursor.getString(0)
                    + "\n";
            if(cursor.getString(0).equals(inputEmail))
                return true;
        }
        return false;
    }

}

*/