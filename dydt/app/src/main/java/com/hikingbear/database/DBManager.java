package com.hikingbear.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    public DBManager (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create new table
        // create table 테이블명 (컬럼명 타입옵션);
        String sql ="CREATE TABLE IF NOT EXISTS ACCOUNT_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT);";
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

        Cursor cursor = db.rawQuery("select * from ACCOUNT_LIST", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " : email: "
                    + cursor.getString(1)
                    + ", password: "
                    + cursor.getString(2)
                    + "\n";
        }
        return str;
    }

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
