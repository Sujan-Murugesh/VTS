package com.sujan.trackingme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;


import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper  {

    static String name = "database";
    static int version = 3;

    String createTableUser ="CREATE TABLE if not exists 'user' ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'username' TEXT,'password' TEXT,'email' TEXT,'country' TEXT,'dob' TEXT,'gender' TEXT)";
    String createtbl_location1data ="CREATE TABLE if not exists 'location_start' ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'latitude' TEXT,'longitude' TEXT,'address' TEXT)";
    String createtbl_location2data ="CREATE TABLE if not exists 'location_end' ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'latitude' TEXT,'longitude' TEXT,'address' TEXT)";
    String createtbl_trip ="CREATE TABLE if not exists 'trip' ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'from_ad' TEXT ,'to_ad' TEXT , 'distance' TEXT)";
    String createtbl_tripreport ="CREATE TABLE if not exists 'report' ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT,'tripno' TEXT,'title' TEXT,'vlc_no' TEXT,'vlc_type' TEXT, 'feul_type' TEXT,'driv_name' TEXT,'lic_no' TEXT,'driv_address' TEXT,'distance' TEXT,'from_ad' TEXT,'to_ad' TEXT,'feul_usage' TEXT ,'date' INTEGER)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(createTableUser);
        getWritableDatabase().execSQL(createtbl_location1data);
        getWritableDatabase().execSQL(createtbl_location2data);
        getWritableDatabase().execSQL(createtbl_trip);
        getWritableDatabase().execSQL(createtbl_tripreport);
    }

    public  void insertReport(int tripnum,String title,String vhi_num,String vlc_type,String feul_type,String driv_name,String lic_no,String driv_ad,String dis,String frm_ad,String to_ad,String feul_usage,Long date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("tripno",tripnum);
        contentValues.put("title",title);
        contentValues.put("vlc_no",vhi_num);
        contentValues.put("vlc_type",vlc_type);
        contentValues.put("feul_type",feul_type);
        contentValues.put("driv_name",driv_name);
        contentValues.put("lic_no",lic_no);
        contentValues.put("driv_address",driv_ad);
        contentValues.put("distance",dis);
        contentValues.put("from_ad",frm_ad);
        contentValues.put("to_ad",to_ad);
        contentValues.put("feul_usage",feul_usage);
        contentValues.put("date",date);

        db.insert("report",null,contentValues);
    }

    public void inserting_tripdata(String fromaddress,String toaddress,String distances) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("from_ad",fromaddress);
        contentValues.put("to_ad",toaddress);
        contentValues.put("distance",distances);

        db.insert("trip",null,contentValues);
    }

    public Cursor  getLastTripData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM trip ORDER BY id DESC LIMIT 1", null );
        return res;
    }

    public int getReportCount(){
        //fetch the trip number from db
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from report",null);
        cursor.move(cursor.getCount());
        return  cursor.getCount();
    }

    public int getlastTrip(){
        //fetch the trip number from db
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from trip",null);
        cursor.move(cursor.getCount());
        int x =  cursor.getCount();
        return x;
    }

    public void insertTrips(ContentValues contentValues){
        getWritableDatabase().insert("trip","",contentValues);
    }

    public void insertLocation1(ContentValues contentValues){//100 ok
        getWritableDatabase().insert("location_start","",contentValues);
    }

    public Cursor  getLocation1(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM location_start ORDER BY id DESC LIMIT 1", null );
        return res;
    }

    public void insertLocation2(ContentValues contentValues){
        getWritableDatabase().insert("location_end","",contentValues);
    }

    public void insertUser(ContentValues contentValues){
        getWritableDatabase().insert("user","",contentValues);
    }

    public boolean isLoginValid(String emailin,String password){
        String sql = "select count(*) from user where email= '"+emailin+"' and password='"+password+"'";
        SQLiteStatement sqLiteStatement = getReadableDatabase().compileStatement(sql);
        long l = sqLiteStatement.simpleQueryForLong();

        if(l== 1){
            return true;
        }
        else{
            return false;
        }
    }

    public void updatePassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(password, password);
        db.update("user", values, email+" = ?",new String[] { email });
        db.close();
    }

    public boolean checkUser(String email){
        String[] columns = {
                "id"
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = email + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query("user",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
