package com.android.interactivity.maps.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();

    /* Database name */
    private static String DB_NAME = "location.db";

    /* Version number of the database */
    private static int VERSION = 1;

    /* Field 1 of the table markers, which is the primary key */
    public static final String _ID = "_id";

    /* Field 2 of the table markers, stores the latitude */
    public static final String FIELD_LAT = "latitude";

    /* Field 3 of the table markers, stores the longitude*/
    public static final String FIELD_LNG = "longitude";

    /* Field 4 of the table markers, stores the title*/
    public static final String FIELD_TITLE = "title";

    /* Field 5 of the table markers, stores the description*/
    public static final String FIELD_DSC = "description";

    /* A constant, stores the the table name */
    public static final String MARKER_TABLE = "markers";

    public static final String SQL_CREATE_TABLE = new StringBuilder("create table " )
            .append(MARKER_TABLE)   .append(" ( ")
            .append(_ID)            .append(" integer primary key autoincrement , ")
            .append(FIELD_LNG)      .append(" double , ")
            .append(FIELD_LAT)      .append(" double , ")
            .append(FIELD_TITLE)    .append(" text , ")
            .append(FIELD_DSC)      .append(" text ")
            .append(" ) ")
            .toString();

    /* An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MARKER_TABLE);
        onCreate(db);
    }

    public void openToRead() {
        this.getReadableDatabase();
    }
}
