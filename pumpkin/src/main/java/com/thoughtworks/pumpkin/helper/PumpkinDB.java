package com.thoughtworks.pumpkin.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PumpkinDB extends SQLiteOpenHelper {

    public static String CATEGORY_TABLE_NAME = "category";
    public static String WISH_LIST_TABLE_NAME = "wishlist";

    public PumpkinDB(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + " (name TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + WISH_LIST_TABLE_NAME + " (name TEXT, id TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
