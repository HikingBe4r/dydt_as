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

    private HashMap hp;

    public DBManager(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql ="CREATE TABLE IF NOT EXISTS SCHEDULE_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS SCHEDULE_LIST");
        onCreate(db);
    }
    //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
    public boolean insertSchedule  (String subject, int year, int month, int day, int hour, int minute, String description)
    {
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

    public boolean deleteTest(int _id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " +
                SCHEDULE_TABLE_NAME +
                " WHERE _id= " +
                _id);

        db.close();
        return true;
    }

    /**
     *  id가 1부터 시작해서 총 count된 갯수만큼 까지 재정렬하는 기능
     *  ex) 3,4,7,8,9  -> 1,2,3,4,5
     * */
    public boolean sortSchedule() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("SELECT * FROM "+SCHEDULE_TABLE_NAME+" ORDER BY _id ASC"); // asc 오름차순, desc 내림차순
        int count = db.rawQuery("SELECT * FROM "+SCHEDULE_TABLE_NAME, null).getCount();      // 총 db 갯수
        for (int i = 0; i < count; i++) {

        }

        return true;
    }
/*
    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }*/

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SCHEDULE_LIST where _id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SCHEDULE_TABLE_NAME);
        return numRows;
    }

    public boolean updateSchedule (Integer id, String subject, int year, int month, int day, int hour, int minute, String description)
    {
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

    public Integer deleteSchedule (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SCHEDULE_TABLE_NAME,
                "_.id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllContacts()
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