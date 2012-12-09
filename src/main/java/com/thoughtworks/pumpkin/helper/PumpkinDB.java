package com.thoughtworks.pumpkin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN.*;

public class PumpkinDB extends SQLiteOpenHelper {

    public static String CATEGORY_TABLE_NAME = "category";
    public static String WISH_LIST_TABLE_NAME = "wishlist";
    public static String SHOP_TABLE_NAME = "shop";

    public PumpkinDB(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + " (name TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + WISH_LIST_TABLE_NAME + " (name TEXT, id TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE " + SHOP_TABLE_NAME + " (name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void resetBookCategories(List<ParseObject> categories) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from " + PumpkinDB.CATEGORY_TABLE_NAME);
        for (ParseObject category : categories) {
            ContentValues values = new ContentValues();
            values.put(SHOP.NAME, category.getString(SHOP.NAME));
            database.insert(PumpkinDB.CATEGORY_TABLE_NAME, SHOP.NAME, values);
        }
        database.close();
    }

    public void resetShops(List<ParseObject> shops) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from " + PumpkinDB.SHOP_TABLE_NAME);
        for (ParseObject shop : shops) {
            ContentValues values = new ContentValues();
            values.put(SHOP.NAME, shop.getString(SHOP.NAME));
            database.insert(PumpkinDB.SHOP_TABLE_NAME, SHOP.NAME, values);
        }
        database.close();
    }

    public void resetWishLists(List<ParseObject> wishLists) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("delete from " + PumpkinDB.WISH_LIST_TABLE_NAME);
        for (ParseObject wishList : wishLists) {
            ContentValues values = new ContentValues();
            values.put(WISH_LIST.NAME, wishList.getString(WISH_LIST.NAME));
            values.put("id", wishList.getObjectId());
            database.insert(PumpkinDB.WISH_LIST_TABLE_NAME, WISH_LIST.NAME, values);
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

    public List<String> getBookCategories() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select name from " + PumpkinDB.CATEGORY_TABLE_NAME, null);
        ArrayList<String> categories = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        database.close();
        return categories;
    }

    public List<String> getShops() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select name from " + PumpkinDB.SHOP_TABLE_NAME, null);
        ArrayList<String> shops = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                shops.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        database.close();
        return shops;
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

    public void insertWishList(String wishListName, String objectId) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WISH_LIST.NAME, wishListName);
        values.put("id", objectId);
        database.insert(PumpkinDB.WISH_LIST_TABLE_NAME, WISH_LIST.NAME, values);
    }

}
