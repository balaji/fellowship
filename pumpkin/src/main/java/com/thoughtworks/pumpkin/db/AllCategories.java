package com.thoughtworks.pumpkin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AllCategories extends SQLiteOpenHelper {

    public static String TABLE_NAME = "category";
    public static String DATABASE_NAME = "pumpkin.db";
    private static int VERSION = 1;

    public AllCategories(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
