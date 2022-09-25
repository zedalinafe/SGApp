package emmasircolour.com.sgapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.icu.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.valueOf;
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "u306915389_sgdb";
    public static final String TABLE_savingsgroups="savingsgroups";
    public static final String TABLE_usersavingsgroup="usersavingsgroup";
    public static final String COLUMN_sgnumber="sgnumber";
    public static final String COLUMN_sgname="sgname";
    public static final String COLUMN_sgformationdate="sgformationdate";
    public static final String COLUMN_cycle ="cycle";
    public static final String COLUMN_shareoutdate="shareoutdate";
    public static final String COLUMN_approvalcount="approvalcount";
    public static final String COLUMN_savebasevalue="savebasevalue";
    public static final String COLUMN_noofweeks="noofweeks";
    public static final String COLUMN_deviceid="deviceid";
    public static final String COLUMN_syncode="synccode";
    public static final String COLUMN_syncod="syncode";
    public static final String COLUMN_userphonenumber="userphonenumber";
    public static final String COLUMN_datejoined="datejoined";
    public static final String COLUMN_memberactive="memberactive";
    public static final String COLUMN_loanservicecharge="loanservicecharge";


    //database version
    private static final int DB_VERSION =1 ;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //String sqlAddcolumn="ALTER TABLE usersavingsgroup ADD memberactive INT NOT NULL DEFAULT 1";
        //db.execSQL(sqlAddcolumn);


    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
String sqlsavingsgroup="CREATE TABLE savingsgroups ( sgnumber char(12) NOT NULL, sgname text  NOT NULL, sgformationdate date NOT NULL, cycle int(11) NOT NULL, shareoutdate date NOT NULL, approvalcount int(11) NOT NULL, savebasevalue int(11) NOT NULL, noofweeks int(11) NOT NULL, deviceid varchar(36)  NOT NULL,synccode int NOT NULL DEFAULT 0 ,loanservicecharge DECIMAL NOT NULL DEFAULT 0 ) ;";
db.execSQL(sqlsavingsgroup);
String sqlusersavings="CREATE TABLE usersavingsgroup (   sgnumber varchar(12)  NOT NULL,   userphonenumber varchar(12) NOT NULL,   datejoined date DEFAULT NULL,   syncode int(11) NOT NULL DEFAULT 0,memberactive int(11) NOT NULL DEFAULT 0 ) ";
db.execSQL(sqlusersavings);

    }
    //add the group
    public long addSavingsGroup(String sgnumber, String sgname, String sgformationdate, int cycle, String shareoutdate, int approvalcount, int savebasevalue, int noofweeks, String deviceid, Double loanservicecharge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_sgnumber, sgnumber);
        contentValues.put(COLUMN_sgname, sgname);
        contentValues.put(COLUMN_sgformationdate, sgformationdate);
        contentValues.put(COLUMN_cycle, cycle);
        contentValues.put(COLUMN_shareoutdate, shareoutdate);
        contentValues.put(COLUMN_approvalcount, approvalcount);
        contentValues.put(COLUMN_savebasevalue, savebasevalue);
        contentValues.put(COLUMN_noofweeks,noofweeks);
        contentValues.put(COLUMN_deviceid, deviceid);
        contentValues.put(COLUMN_syncode, 1);
        contentValues.put(COLUMN_loanservicecharge, String.valueOf((loanservicecharge)));
        long test= db.insert(TABLE_savingsgroups, null, contentValues);
        db.close();
        return test;
    }

    // sgnumber varchar(12)  NOT NULL,   userphonenumber varchar(12) NOT NULL,   datejoined date DEFAULT NULL,   syncode int(11) NOT NULL DEFAULT 0,memberactive
    public long usersavingsgroup(String sgnumber, String userphonenumber, String datejoined, int syncode,int memberactive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_sgnumber, sgnumber);
        contentValues.put(COLUMN_userphonenumber, userphonenumber);
        contentValues.put(COLUMN_datejoined, datejoined);
        contentValues.put(COLUMN_syncod, syncode);
        contentValues.put(COLUMN_memberactive, memberactive);
        long test= db.insert(TABLE_usersavingsgroup, null, contentValues);
        db.close();
        return test;
    }



}
