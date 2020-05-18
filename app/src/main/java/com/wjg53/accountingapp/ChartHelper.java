package com.wjg53.accountingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChartHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "bills_db";

    public ChartHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public ArrayList<PieEntry> countinValue(){
        String selectQuery = " SELECT "+Note.COLUMN_TEXT+ " , SUM("+Note.COLUMN_VALUE+ ") , "+ Note.COLUMN_INOROUT+ " FROM " + Note.TABLE_NAME + " GROUP BY " +
                Note.COLUMN_TEXT;
        BillsItem billsItem = new BillsItem();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast()) {
            do {
                int inorout = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_INOROUT));
                int item = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_TEXT));
                int value = cursor.getInt(cursor.getColumnIndex("SUM("+Note.COLUMN_VALUE+ ")"));
                if (inorout == 1){
                    entries.add(new PieEntry(value, billsItem.setinComeitems(item)));
                }
            } while (cursor.moveToPrevious());
        }
        return entries;
    }

    public ArrayList<PieEntry> countoutValue(){
        String selectQuery = " SELECT "+Note.COLUMN_TEXT+ " , "+Note.COLUMN_INOROUT+ " , SUM("+ Note.COLUMN_VALUE+ ") FROM " + Note.TABLE_NAME + " GROUP BY " +
                Note.COLUMN_TEXT;
        BillsItem billsItem = new BillsItem();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast()) {
            do {
                int inorout = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_INOROUT));
                int item = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_TEXT));
                int value = cursor.getInt(cursor.getColumnIndex("SUM("+Note.COLUMN_VALUE+ ")"));
                if (inorout == 0){
                    entries.add(new PieEntry(Math.abs(value), billsItem.setItem(item)));
                }
            } while (cursor.moveToPrevious());
        }
        return entries;
    }

}
