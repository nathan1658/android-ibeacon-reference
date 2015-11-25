package com.radiusnetworks.ibeaconreference;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {
    private final static int _DBVersion = 1; //<-- 版本

    private final static String _DBName = "BeaconName.db";  //<-- db name

    private final static String _TableName = "Name"; //<-- table name

    public SQLite(Context context)
    {
        super(context , _DBName,null,_DBVersion);
    }

    public SQLite(Context context, String name, CursorFactory factory,
                  int version) {
        super(context, _DBName, factory, _DBVersion);
        // TODO Auto-generated constructor stub
        //context=內容物件；name=傳入資料庫名稱；factory=複雜查詢時使用；version=資料庫版本
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub


        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( " +

        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +

        "UUID VARCHAR, " +

        "NAME VARCHAR" +

        ");";

        db.execSQL(SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

// TODO Auto-generated method stub

        final String SQL = "DROP TABLE " + _TableName;

        db.execSQL(SQL);

    }

}