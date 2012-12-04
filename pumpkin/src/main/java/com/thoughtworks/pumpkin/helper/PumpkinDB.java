package com.thoughtworks.pumpkin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

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

    public void insertWishList(String wishListName, String objectId) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.ParseObject.COLUMN.WISH_LIST.NAME, wishListName);
        values.put("id", objectId);
        database.insert(PumpkinDB.WISH_LIST_TABLE_NAME, Constant.ParseObject.COLUMN.WISH_LIST.NAME, values);
    }

    public Cursor getBookCategoriesCursor() {
        return getReadableDatabase().rawQuery("select rowid _id," + Constant.ParseObject.COLUMN.CATEGORY.NAME + " from " + PumpkinDB.CATEGORY_TABLE_NAME, null);
    }

    public void resetBookCategories(List<ParseObject> categories) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from " + PumpkinDB.CATEGORY_TABLE_NAME);
        for (ParseObject category : categories) {
            ContentValues values = new ContentValues();
            values.put(Constant.ParseObject.COLUMN.CATEGORY.NAME, category.getString(Constant.ParseObject.COLUMN.CATEGORY.NAME));
            database.insert(PumpkinDB.CATEGORY_TABLE_NAME, Constant.ParseObject.COLUMN.CATEGORY.NAME, values);
        }
        database.close();
    }

    public List<String> getWishListColumn(String columnName) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select " + columnName + " from " + PumpkinDB.WISH_LIST_TABLE_NAME, null);
        ArrayList<String> wishLists = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                wishLists.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        database.close();
        return wishLists;
    }

    public String getWishListId(String name) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select id from " + PumpkinDB.WISH_LIST_TABLE_NAME + " where name = ?", new String[]{name});
        if(cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        String id = cursor.getString(0);
        database.close();
        return id;
    }

    public void resetWishLists(List<ParseObject> wishLists) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from " + PumpkinDB.WISH_LIST_TABLE_NAME);
        for (ParseObject wishList : wishLists) {
            ContentValues values = new ContentValues();
            values.put(Constant.ParseObject.COLUMN.WISH_LIST.NAME, wishList.getString(Constant.ParseObject.COLUMN.WISH_LIST.NAME));
            values.put("id", wishList.getObjectId());
            database.insert(PumpkinDB.WISH_LIST_TABLE_NAME, Constant.ParseObject.COLUMN.WISH_LIST.NAME, values);
        }
        database.close();
    }

    public Cursor getWishListsCursor() {
        return getReadableDatabase().rawQuery("select rowid _id," + Constant.ParseObject.COLUMN.WISH_LIST.NAME
                + " from " + PumpkinDB.WISH_LIST_TABLE_NAME, null);
    }
}
