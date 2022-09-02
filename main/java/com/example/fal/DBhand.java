package com.example.fal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.time.format.DateTimeFormatter;

public class DBhand extends SQLiteOpenHelper {
    Calendar cal;
    SimpleDateFormat sdf;
    private static final String DB_NAME = "Hist";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "his";
    private static final String quer = "Quer";
    private static final String date = "Date";
    private static final String Sno = "Sno";

    public DBhand(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("+ Sno + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ quer + " TEXT,"+ date + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }
    public boolean addNew(String querr) {
        String dat= "";
        cal=Calendar.getInstance();
        sdf=new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss  aaa z");
        dat=sdf.format(cal.getTime()).toString();


        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(quer, querr);
        values.put(date, dat);

        //Log.e("this :::::",id+" "+name+" "+mob+" "+room+" "+reas+" "+place+" "+date+" "+time);
        // after adding all values we are passing
        // content values to our table.
        long result=db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
        if(result==-1)
            return false;
        else
            return true;
    }
    public ArrayList upd()
    {
        int temp=0;
        ArrayList s=new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM his ORDER BY Sno DESC",null, null);
        while (cursor.moveToNext()){
            if(cursor.getString(1).length()!=0){
                String a=String.valueOf(++temp)+". Query: "+cursor.getString(1)+"\n Date and Time: "+cursor.getString(2);
                Log.e("aaada",a);
                s.add(a);
            }}

        cursor.close();
        db.close();
        return s;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

