package com.wjg53.accountingapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ravi on 20/02/18.
 */

public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEXT = "note";
    public static final String COLUMN_IMAGEID = "imageid";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_INOROUT = "inorout";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private int inorout;
    private double value;
    private int itemID;
    private int imageID;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"         //Data ID
                    + COLUMN_IMAGEID + " TEXT,"                      //Save Image ID
                    + COLUMN_TEXT + " TEXT,"                          //Save Item ID
                    + COLUMN_VALUE + " TEXT,"                         //Value of bill
                    + COLUMN_INOROUT + " TEXT,"                       //Cost or Income
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_DATE"     //Date of record
                    + ")";
    /*There are 6 columns of this table. This table stores the data ID, image ID, item ID,
     boolean value of income or expense,value of bill and record date*/

    public Note() {
    }

    public Note(int id, int itemID, int imageID ,String timestamp,double value ,int inorout) {
        this.id = id;
        this.itemID = itemID;
        this.value = value;
        this.imageID = imageID;
        this.timestamp = timestamp;
        this.inorout = inorout;
    }

    public int getId() {
        return id;
    }

    public int getItemID() {
        return itemID;
    }

    public int getInorout() {
        return inorout;
    }

    public void setInorout(int inorout) { this.inorout = inorout; }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getImageId() {
        return imageID;
    }

    public void setImageId(int imageID) {
        this.imageID = imageID;
    }

    public String getTimestamp() { return timestamp; }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String,Object> getBill(int id){
        Map<String,Object> notes = new HashMap<String, Object>();
        return notes;
    }
}
