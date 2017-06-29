package com.dribbble.shotsviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dribbble.shotsviewer.data.Shot;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    private final int FIRST_ELEMENT = 0;
    private static final String DATABASE_NAME = "weather_database";
    private SQLiteDatabase db;

    public DataBase(Context context) {
        super(context,DATABASE_NAME,null,1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBTable.DROP_TABLE);
        onCreate(db);
    }



    public void insertData(Shot shot){

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTable.ID, shot.getId());
            contentValues.put(DBTable.TITLE, shot.getTitle());
            contentValues.put(DBTable.DESCRIPTION, shot.getDescription());
            contentValues.put(DBTable.IMAGE_URL, shot.getImageURL());
            contentValues.put(DBTable.UNIX_DATE,shot.getUnixDate());
            contentValues.put(DBTable.DAY,shot.getDay());

        try {
            db.insertOrThrow(DBTable.TABLE_NAME, null, contentValues);
        }
        catch (SQLiteConstraintException e){
            Log.e("SQLiteWxception" , e.toString());

        }

    }



    public ArrayList<Shot> getAllDataFromDB(){
        ArrayList<Shot> shots = null;
        Cursor userCursor = db.rawQuery("select * from " + DBTable.TABLE_NAME, null);
        if (userCursor.moveToFirst()) {
            shots = new ArrayList<>(userCursor.getCount());
            do {
                Shot shot  = new Shot();
                shot.setId(userCursor.getInt(userCursor.getColumnIndex(DBTable.ID)));
                shot.setTitle(userCursor.getString(userCursor.getColumnIndex(DBTable.TITLE)));
                shot.setDescription(userCursor.getString(userCursor.getColumnIndex(DBTable.DESCRIPTION)));
                shot.setImageURL(userCursor.getString(userCursor.getColumnIndex(DBTable.IMAGE_URL)));
                shot.setUnixDate(userCursor.getLong(userCursor.getColumnIndex(DBTable.UNIX_DATE)));
                shot.setDay(userCursor.getLong(userCursor.getColumnIndex(DBTable.DAY)));
                shots.add(shot);
            } while (userCursor.moveToNext());
        }
        userCursor.close();
        return shots;
    }



    public long getMaxUnixDateFromDB(){
        try {
            Cursor userCursor = db.rawQuery("select max(" + DBTable.UNIX_DATE + ")  from " + DBTable.TABLE_NAME, null);
            if (userCursor.moveToFirst()) {
                return userCursor.getLong(FIRST_ELEMENT);
            } else {
                userCursor.close();
                return 0;
            }
        }
        catch (SQLiteException e){
            return 0;
        }
    }



    public long getMinUnixDateFromDB(){
        try {
            Cursor userCursor = db.rawQuery("select min(" + DBTable.UNIX_DATE + ")  from " + DBTable.TABLE_NAME, null);
            if (userCursor.moveToFirst()) {
                return userCursor.getLong(FIRST_ELEMENT);
            } else {
                userCursor.close();
                return 0;
            }
        }
        catch (SQLiteException e){
            return 0;
        }
    }

    public long getMaxDayFromDB(){
        try {
            Cursor userCursor = db.rawQuery("select max(" + DBTable.DAY + ")  from " + DBTable.TABLE_NAME, null);
            if (userCursor.moveToFirst()) {
                return userCursor.getLong(FIRST_ELEMENT);
            } else {
                userCursor.close();
                return 0;
            }
        }
        catch (SQLiteException e){
            return 0;
        }
    }


    public ArrayList<Shot> getShotsForMinUnixDay(Long unixDay){
        ArrayList<Shot> shots = null;
        Cursor userCursor = db.rawQuery("select *  from " + DBTable.TABLE_NAME+" where "+DBTable.UNIX_DATE+"=\""+unixDay+"\"", null);
        if (userCursor.moveToFirst()) {
            shots = new ArrayList<>(userCursor.getCount());
            do {
                Shot shot = new Shot();
                shot.setId(userCursor.getInt(userCursor.getColumnIndex(DBTable.ID)));
                shot.setTitle(userCursor.getString(userCursor.getColumnIndex(DBTable.TITLE)));
                shot.setDescription(userCursor.getString(userCursor.getColumnIndex(DBTable.DESCRIPTION)));
                shot.setImageURL(userCursor.getString(userCursor.getColumnIndex(DBTable.IMAGE_URL)));
                shot.setUnixDate(userCursor.getLong(userCursor.getColumnIndex(DBTable.UNIX_DATE)));
                shot.setDay(userCursor.getLong(userCursor.getColumnIndex(DBTable.DAY)));
                shots.add(shot);

            } while (userCursor.moveToNext());
        }
        userCursor.close();
        return shots;
    }


    public void deleteOldData(int id){
        db.execSQL("delete from " + DBTable.TABLE_NAME+" where "+ DBTable.ID +"="+id+"");
    }



    public int countData(){
        int res = 0 ;
        Cursor userCursor = db.rawQuery("select count(*) from " + DBTable.TABLE_NAME, null);
        if (userCursor.moveToFirst()) {
            res = userCursor.getInt(FIRST_ELEMENT);
        }
        userCursor.close();
        return res;
    }


}
