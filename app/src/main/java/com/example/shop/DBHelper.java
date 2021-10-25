package com.example.shop;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper  extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "shopBD";
    public static final String TABLE_GOODS = "good";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRISE= "prise";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_GOODS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_PRISE + " text" + ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_GOODS);
        onCreate(db);
    }
}