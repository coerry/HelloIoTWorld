package com.cr.helloiotworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static volatile DatabaseHandler sInstance;

    //Database Version
    private static final int DATABASE_VERSION = 2;

    //DB name
    private static final String DATABASE_NAME = "SmartGlass";

    //Table Names
//    private static final String PRODUCT_TABLE_NAME = "product";
//    private static final String PRODUCT_ID = "id";
//    private static final String PRODUCT_NAME = "name";
//    private static final String PRODUCT_BRAND = "brand_id";
//
//    private static final String BRAND_TABLE_NAME = "brand";
//    private static final String BRAND_ID = "id";
//    private static final String BRAND_NAME = "name";

    private DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new DatabaseHandler(context.getApplicationContext());

        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + "Person" +
                " (" + "ID" + " integer primary key autoincrement, " +
                "Name" + " text, " +
                "Creditcard" + " text, " +
                "RFID_TN" + " integer);");

        db.execSQL("create table " + "Drinks" +
                " (" + "ID" + " integer primary key autoincrement, " +
                "Name" + " text, " +
                "Price" + " real, " +
                "Status" + " text, " + // statuses: "-", "chosen", "bought"
                "RFID_TN" + " integer);");

        // adding the bad gay:
        registerClient("Tom Rocket", "000000000000", 2);

        // adding the good guy:
        registerClient("Julia HappyDance", "1111", 1);

        // adding drinks:
        addDrink("Wine", 5f);
        addDrink("Beer", 3f);
        addDrink("Vodka Red Bull", 4f);
        addDrink("Gin Tonic", 6f);
        addDrink("Cola", 2.5f);
        addDrink("Orange Juice", 2.5f);
        addDrink("Soda", 2.5f);

        // Better way to do it is to add table PersonDrinks for multiple users
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + "Person");
        db.execSQL("drop table if exists " + "Things");
        onCreate(db);
    }

    public void registerClient(String name, String creditcard, Integer RFID_TN) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Creditcard", creditcard);
        contentValues.put("RFID_TN", RFID_TN);

        this.getWritableDatabase().insertOrThrow("Person", "", contentValues);
    }

    public boolean checkClient(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select count(*) from " + "Person" + " where "
                + "Name" + " = " + DatabaseUtils.sqlEscapeString(name);

        try (SQLiteStatement statement = db.compileStatement(sql)) {
            return statement.simpleQueryForLong() > 0;
        }
    }

    public Integer getTN(String name) {
        try (Cursor cur = getWritableDatabase().rawQuery("select RFID_TN from Person where Name = " + name, null)) {
            cur.moveToFirst();

            return cur.getInt(0);
        }
    }

    public ArrayList<Map<String, String>> getAllDrinks(String status) {
        try (Cursor cur = getWritableDatabase().rawQuery("select * from " + "Drinks" + " where Status = '" + status + "'", null)) {
            cur.moveToFirst();
            ArrayList<Map<String, String>> al = new ArrayList<>();

            while (!cur.isAfterLast()) {
                al.add(new HashMap<String, String>() {
                    {
                        put(cur.getColumnName(0), cur.getString(0));
                        put(cur.getColumnName(1), cur.getString(1));
                        put(cur.getColumnName(2), cur.getString(2));
                        put(cur.getColumnName(3), cur.getString(3));
                        put(cur.getColumnName(4), "3");
                    }
                });
                cur.moveToNext();
            }

            return al;
        }
    }

    public void addDrink(String name, Float price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Price", price);
        contentValues.put("Status", "-");

        this.getWritableDatabase().insertOrThrow("Drinks", "", contentValues);
    }

    public Long choseDrink(Integer id) {
        try (Cursor cur = getWritableDatabase().rawQuery("select Name, Price from Drinks where ID = " + id, null)) {
            cur.moveToFirst();

            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", cur.getString(0));
            contentValues.put("Price", cur.getFloat(1));
            contentValues.put("Status", "chosen");

            return this.getWritableDatabase().insertOrThrow("Drinks", "", contentValues);
        }
    }

    public void buyDrink(Integer id) {
        this.getWritableDatabase().execSQL("update " + "Drinks" +
                " set Status = '" + "bought" +
                "' where id = '" + id + "'");
    }

    public void buyAllDrinks() {
        this.getWritableDatabase().execSQL("update " + "Drinks" +
                " set Status = '" + "bought" +
                "' where Status = 'chosen'");
    }

//    public void insertProduct(String name, Integer brandId) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(PRODUCT_NAME, name);
//        contentValues.put(PRODUCT_BRAND, brandId);
//        this.getWritableDatabase().insertOrThrow(PRODUCT_TABLE_NAME, "", contentValues);
//    }
//
//    public void insertBrand(String name) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(BRAND_NAME, name);
//        this.getWritableDatabase().insertOrThrow(BRAND_TABLE_NAME, "", contentValues);
//    }
//
//    public void deleteProduct(String name) {
//        this.getWritableDatabase().delete(PRODUCT_TABLE_NAME, PRODUCT_NAME + " = " + "'" + name + "'", null);
//    }
//
//    public void deleteBrand(String name) {
//        this.getWritableDatabase().delete(BRAND_TABLE_NAME, BRAND_NAME + " = " + "'" + name + "'", null);
//    }
//
//    public void updateProduct(String oldName, String newName, Integer newBrandId) {
//        this.getWritableDatabase().execSQL("update " + PRODUCT_TABLE_NAME +
//                " set name = '" + newName +
//                "', brand_id = " + newBrandId +
//                " where name = '" + oldName + "'");
//    }
//
//    public void updateBrand(String old_name, String new_name) {
//        this.getWritableDatabase().execSQL("update " + PRODUCT_TABLE_NAME +
//                " set name = '" + new_name +
//                "' where name = '" + old_name + "'");
//    }
//
//    public Pair<ArrayList<String>, ArrayList<String>> read() {
//        ArrayList<String> arr_product_list = new ArrayList<>();
//        ArrayList<String> arr_brand_list = new ArrayList<>();
//
//        Cursor res_product = getWritableDatabase().rawQuery("select * from " + PRODUCT_TABLE_NAME, null);
//        Cursor res_brand = getWritableDatabase().rawQuery("select * from " + BRAND_TABLE_NAME, null);
//        res_product.moveToFirst();
//        res_brand.moveToFirst();
//        while (!res_product.isAfterLast()) {
//            arr_product_list.add(res_product.getString(1) + " " + res_product.getString(2));
//            res_product.moveToNext();
//        }
//        while (!res_brand.isAfterLast()) {
//            arr_brand_list.add(res_brand.getString(1));
//            res_brand.moveToNext();
//        }
//
//        res_product.close();
//        res_brand.close();
//
//        return Pair.create(arr_product_list, arr_brand_list);
//    }
}
