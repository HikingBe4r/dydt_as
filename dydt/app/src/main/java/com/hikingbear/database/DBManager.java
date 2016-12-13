package com.hikingbear.database;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;

public class DBManager extends SQLiteOpenHelper {

    //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT

    public static final String DATABASE_NAME = "scedule_list.db";
    public static final String SCHEDULE_TABLE_NAME = "SCHEDULE_LIST";
    public static final String SCHEDULE_COLUMN_ID = "_id";
    public static final String SCHEDULE_COLUMN_SUBJECT = "subject";
    public static final String SCHEDULE_COLUMN_DESCRIPTION = "description";
    public static final String SCHEDULE_COLUMN_WEEKDAY = "weekday";
    public static final String SCHEDULE_COLUMN_HOUR = "hour";
    public static final String SCHEDULE_COLUMN_MINUTE = "minute";
    public static final String SCHEDULE_COLUMN_DELETEABLE = "deleteable";
    public static final String SCHEDULE_COLUMN_PACKAGENAME = "packagename";
    public static final String SCHEDULE_COLUMN_APPNAME = "appname";

    //public static final String SCHEDULE_COLUMN_YEAR = "year";
    //public static final String SCHEDULE_COLUMN_MONTH = "month";
    //public static final String SCHEDULE_COLUMN_DAY = "day";

    public DBManager(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TODO Auto-generated method stub

         String sql ="CREATE TABLE IF NOT EXISTS SCHEDULE_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, subject TEXT, description TEXT, weekday LONG, hour LONG, minute LONG, deleteable INTEGER, packagename TEXT, appname TEXT);";
        // _id, subject(제목), description(내용), weekday(요일), hour(시), minute(분), deleteable(임시저장 여부) 0(false)or 1(true), packagename(com.example.~~) 랑 appname(어플밑에 이름)

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS SCHEDULE_LIST");
        onCreate(db);
    }

    /**
     * int _id: 자동생성되는 제목에 구분짓기위해서 쓰긴했는데 안써도 될듯.
     * @return
     */
    public boolean initDB() {
        Calendar c = Calendar.getInstance();
        long cTimeWeekday = c.get(Calendar.DAY_OF_WEEK);
        long cTimeHour = c.get(Calendar.HOUR);
        long cTimeMinute = c.get(Calendar.MINUTE);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT _id FROM SCHEDULE_LIST WHERE _id= "+"(SELECT MAX(_id) FROM SCHEDULE_LIST)",null);
        int _id = 0;
        try {
            if(res != null) {
                res.moveToLast();
                _id = res.getInt(0);
            }
        } catch (Exception e) {
            System.out.println("initDB 에러");
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, "자동생성 "+res.getCount()+"번");   // 이건 테스트로 하는거니까 지울거임. 원래 여기에 _id를 쓰고있었는데 필요없는듯
        contentValues.put(SCHEDULE_COLUMN_DELETEABLE, 1); // 1은 삭제가능하다. 0은 삭제불가능하다.
        contentValues.put(SCHEDULE_COLUMN_WEEKDAY, cTimeWeekday);
        contentValues.put(SCHEDULE_COLUMN_HOUR, cTimeHour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, cTimeMinute);

        db.insert(SCHEDULE_TABLE_NAME, null, contentValues);

        return true;
    }

    /**
     *
     * @param _column  테이블의 항목(이름)을 가져와서 그 값이 존재하는지.
     * @return  true // 삭제가능
     *          false // 삭제불가
     */
    public boolean isDeleteable(int _id, String _column) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT "+_column+" FROM SCHEDULE_LIST WHERE _id = "+_id, null);
        try {
            System.out.println("cs.getString(0);  " +cs.getString(0));
        } catch (Exception e) {

        }
        return true;
    }

    public boolean insertSchedule() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(SCHEDULE_TABLE_NAME, null, null);

        return true;
    }
    public boolean insertSchedule  (String subject, String description, long weekday, long hour, long minute, String packagename, String appname)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, subject);
        contentValues.put(SCHEDULE_COLUMN_DESCRIPTION, description);
        contentValues.put(SCHEDULE_COLUMN_WEEKDAY, weekday);
        contentValues.put(SCHEDULE_COLUMN_HOUR, hour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, minute);
        contentValues.put(SCHEDULE_COLUMN_DELETEABLE, 0);
        contentValues.put(SCHEDULE_COLUMN_PACKAGENAME, packagename);
        contentValues.put(SCHEDULE_COLUMN_APPNAME, appname);

        db.insert(SCHEDULE_TABLE_NAME, null, contentValues);

        return true;
    }

    public boolean updateSchedule (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SCHEDULE_TABLE_NAME, null, "_id = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    public boolean updateSchedule (Integer id, String subject, String description, long weekday, long hour, long minute, String packagename, String appname)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_COLUMN_SUBJECT, subject);
        contentValues.put(SCHEDULE_COLUMN_DESCRIPTION, description);
        contentValues.put(SCHEDULE_COLUMN_WEEKDAY, weekday);
        contentValues.put(SCHEDULE_COLUMN_HOUR, hour);
        contentValues.put(SCHEDULE_COLUMN_MINUTE, minute);
        contentValues.put(SCHEDULE_COLUMN_DELETEABLE, 0);
        contentValues.put(SCHEDULE_COLUMN_PACKAGENAME, packagename);
        contentValues.put(SCHEDULE_COLUMN_APPNAME, appname);
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

    /**
     * 마지막 항의 id를 찾기위함.
     */
    public int lastRecord() {
        System.out.println("========================================");
        System.out.println("lastRecord() 입니다.");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT _id FROM SCHEDULE_LIST WHERE _id= "+"(SELECT MAX(_id) FROM SCHEDULE_LIST)",null);

        System.out.println("getCount: "+res.getCount());
        //System.out.println("getInt: "+res.getInt(0));

        System.out.println("========================================");

        try {
            if (res != null) {
                res.moveToLast();
            }
            return res.getInt(0);
        }catch(Exception e) {
        }
        return 0;
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