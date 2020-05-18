package com.wjg53.accountingapp;
/*Create By WONG Yuk Kit*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wjg53.accountingapp.BillsItem;
import com.wjg53.accountingapp.Note;
import com.wjg53.accountingapp.R;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;
    public static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    // Database Name
    private static final String DATABASE_NAME = "bills_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    /*Insert expenditure bill*/
    public long insertNote(int itemID,int imageID,double value,int inorout) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_TEXT, itemID);
        values.put(Note.COLUMN_IMAGEID,imageID);
        values.put(Note.COLUMN_VALUE,"-"+value);
        values.put(Note.COLUMN_INOROUT,inorout);
        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);
        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    /*Insert income bill*/
    public long insertInNote(int itemID,int imageID,double value,int inorout) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_TEXT, itemID);
        values.put(Note.COLUMN_IMAGEID,imageID);
        values.put(Note.COLUMN_VALUE,value);
        values.put(Note.COLUMN_INOROUT,inorout);
        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    /**/
    public Map<String,Object> getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        BillsItem billsItem = new BillsItem();
        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TEXT, Note.COLUMN_TIMESTAMP, Note.COLUMN_IMAGEID,Note.COLUMN_VALUE,Note.COLUMN_INOROUT},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToLast();
        Map<String,Object> notes = new HashMap<String, Object>();
        id = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID));
        int imageID = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_IMAGEID));
        int itemID = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_TEXT));
        int inorout = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_INOROUT));
        String value = cursor.getString(cursor.getColumnIndex(Note.COLUMN_VALUE));
        String time = cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP));
        if(inorout == 0) {
            notes.put("id", id);
            notes.put("image", billsItem.setImage(imageID));
            notes.put("title", billsItem.setItem(itemID));
            notes.put("value", value);
            notes.put("time", time);
        }
        else if(inorout == 1){
            notes.put("id", id);
            notes.put("image", billsItem.setinComeimages(imageID));
            notes.put("title", billsItem.setinComeitems(itemID));
            notes.put("value", value);
            notes.put("time", time);
        }
            // close the db connection
            cursor.close();
        return notes;
    }

    /*To pass ArrayList to EditAcitvity*/
    public ArrayList<String> getStringNote(long id) {
        // get readable database as we are not inserting anything
        ArrayList<String> edits = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        BillsItem billsItem = new BillsItem();
        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TEXT, Note.COLUMN_TIMESTAMP, Note.COLUMN_IMAGEID,Note.COLUMN_VALUE,Note.COLUMN_INOROUT},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToLast();
        id = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID));
        int imageID = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_IMAGEID));
        int itemID = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_TEXT));
        int inorout = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_INOROUT));
        String value = cursor.getString(cursor.getColumnIndex(Note.COLUMN_VALUE));
        String time = cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP));
        if(inorout == 0) {
            edits.add(0, String.valueOf(id));
            edits.add(1, String.valueOf(imageID));
            edits.add(2, String.valueOf(itemID));
            edits.add(3, value);
            edits.add(4, time);
            edits.add(5, String.valueOf(inorout));
        }
        else if(inorout == 1){
            edits.add(0, String.valueOf(id));
            edits.add(1, String.valueOf(imageID));
            edits.add(2, String.valueOf(itemID));
            edits.add(3, value);
            edits.add(4, time);
            edits.add(5, String.valueOf(inorout));
        }
        // close the db connection
        cursor.close();
        return edits;
    }

    /*Get all bill(only run at OnCreate)*/
    public List<Map<String,Object>> getAllNotes() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BillsItem billsItem = new BillsItem();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                Map<String,Object> notes = new HashMap<String, Object>();
                int id = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID));
                int imageid = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_IMAGEID));
                int itemid = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_TEXT));
                int inorout = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_INOROUT));
                String value = cursor.getString(cursor.getColumnIndex(Note.COLUMN_VALUE));
                String time = cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP));
                if(inorout == 0) {
                    notes.put("id", id);
                    notes.put("image", billsItem.setImage(imageid));
                    notes.put("title", billsItem.setItem(itemid));
                    notes.put("value", value);
                    notes.put("time", time);
                    list.add(notes);
                }
                else if(inorout == 1){
                    notes.put("id", id);
                    notes.put("image", billsItem.setinComeimages(imageid));
                    notes.put("title", billsItem.setinComeitems(itemid));
                    notes.put("value", value);
                    notes.put("time", time);
                    list.add(notes);
                }
            } while (cursor.moveToPrevious());
        }

        // close db connection
        db.close();

        // return notes list
        return list;
    }

    /*Get all bills by date*/
    public List<Map<String,Object>> getbillbydate(String str) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BillsItem billsItem = new BillsItem();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        //str = "2019-10-30";
        try {
            date1 = format1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                Note.COLUMN_TIMESTAMP + " = " + "date('"+str+"')";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                Map<String,Object> notes = new HashMap<String, Object>();
                int id = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID));
                int imageid = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_IMAGEID));
                int itemid = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_TEXT));
                int inorout = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_INOROUT));
                String value = cursor.getString(cursor.getColumnIndex(Note.COLUMN_VALUE));
                String time = cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP));
                if(inorout == 0) {
                    notes.put("id", id);
                    notes.put("image", billsItem.setImage(imageid));
                    notes.put("title", billsItem.setItem(itemid));
                    notes.put("value", value);
                    notes.put("time", time);
                    list.add(notes);
                }
                else if(inorout == 1){
                    notes.put("id", id);
                    notes.put("image", billsItem.setinComeimages(imageid));
                    notes.put("title", billsItem.setinComeitems(itemid));
                    notes.put("value", value);
                    notes.put("time", time);
                    list.add(notes);
                }
            } while (cursor.moveToPrevious());
        }

        // close db connection
        db.close();
        // return notes list
        return list;
    }

    /*Updata function*/
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TEXT, note.getItemID());
        values.put(Note.COLUMN_VALUE,note.getValue());
        values.put(Note.COLUMN_IMAGEID,note.getImageId());
        values.put(Note.COLUMN_INOROUT,note.getInorout());

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }
    /*Delete function*/
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    /*Count expenditure or income value*/
    public String countValue(int inorout){
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                Note.COLUMN_INOROUT + " = " + inorout;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        double cal = 0;
        if (cursor.moveToLast()) {
            do {
                String value = cursor.getString(cursor.getColumnIndex(Note.COLUMN_VALUE));
                cal = Double.valueOf(value) + cal;
            } while (cursor.moveToPrevious());
        }
        String result = String.valueOf(cal);
        return result;
    }

    /*Count current day expenditure or income value*/
    public String countCurrenDayValue(int inorout,String str){
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        //str = "2019-10-30";
        try {
            date1 = format1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                Note.COLUMN_INOROUT + " = " + inorout + " AND " + Note.COLUMN_TIMESTAMP + " = " + "date('"+str+"')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        double cal = 0;
        if (cursor.moveToLast()) {
            do {
                String value = cursor.getString(cursor.getColumnIndex(Note.COLUMN_VALUE));
                cal = Double.valueOf(value) + cal;
            } while (cursor.moveToPrevious());
        }
        String result = String.valueOf(cal);
        return result;
    }

}

