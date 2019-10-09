package com.cr.helloiotworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //DB name
    private static final String DATABASE_NAME = "PB";

    //Table Names
    private static final String PRODUCT_TABLE_NAME = "product";
    private static final String PRODUCT_ID = "id";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_BRAND = "brand_id";

    private static final String BRAND_TABLE_NAME = "brand";
    private static final String BRAND_ID = "id";
    private static final String BRAND_NAME = "name";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + BRAND_TABLE_NAME +
                " (" + BRAND_ID + " integer primary key autoincrement, " +
                BRAND_NAME + " text);");
        db.execSQL("create table " + PRODUCT_TABLE_NAME +
                " (" + PRODUCT_ID + " integer primary key autoincrement, " +
                PRODUCT_NAME + " text, " +
                PRODUCT_BRAND + " integer, " +
                "constraint fk_brand " +
                "foreign key (" + PRODUCT_BRAND + ")" +
                "references brand(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + PRODUCT_TABLE_NAME);
        db.execSQL("drop table if exists " + BRAND_TABLE_NAME);
        onCreate(db);
    }

    public void insertProduct(String name, Integer brandId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_NAME, name);
        contentValues.put(PRODUCT_BRAND, brandId);
        this.getWritableDatabase().insertOrThrow(PRODUCT_TABLE_NAME, "", contentValues);
    }

    public void insertBrand(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BRAND_NAME, name);
        this.getWritableDatabase().insertOrThrow(BRAND_TABLE_NAME, "", contentValues);
    }

    public void deleteProduct(String name) {
        this.getWritableDatabase().delete(PRODUCT_TABLE_NAME, PRODUCT_NAME + " = " + "'" + name + "'", null);
    }

    public void deleteBrand(String name) {
        this.getWritableDatabase().delete(BRAND_TABLE_NAME, BRAND_NAME + " = " + "'" + name + "'", null);
    }

    public void updateProduct(String oldName, String newName, Integer newBrandId) {
        this.getWritableDatabase().execSQL("update " + PRODUCT_TABLE_NAME +
                " set name = '" + newName +
                "', brand_id = " + newBrandId +
                " where name = '" + oldName + "'");
    }

    public void updateBrand(String old_name, String new_name) {
        this.getWritableDatabase().execSQL("update " + PRODUCT_TABLE_NAME +
                " set name = '" + new_name +
                "' where name = '" + old_name + "'");
    }

    public Pair<ArrayList<String>, ArrayList<String>> read() {
        ArrayList<String> arr_product_list = new ArrayList<>();
        ArrayList<String> arr_brand_list = new ArrayList<>();

        Cursor res_product = getWritableDatabase().rawQuery("select * from " + PRODUCT_TABLE_NAME, null);
        Cursor res_brand = getWritableDatabase().rawQuery("select * from " + BRAND_TABLE_NAME, null);
        res_product.moveToFirst();
        res_brand.moveToFirst();
        while (!res_product.isAfterLast()) {
            arr_product_list.add(res_product.getString(1) + " " + res_product.getString(2));
            res_product.moveToNext();
        }
        while (!res_brand.isAfterLast()) {
            arr_brand_list.add(res_brand.getString(1));
            res_brand.moveToNext();
        }

        res_product.close();
        res_brand.close();

        return Pair.create(arr_product_list, arr_brand_list);
    }
}
