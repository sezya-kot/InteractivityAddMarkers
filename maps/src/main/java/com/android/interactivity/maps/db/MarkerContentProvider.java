package com.android.interactivity.maps.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.interactivity.maps.system.Debug;

/**
 * Created by kot on 27-Nov-14.
 */
public class MarkerContentProvider extends ContentProvider {

    public static final String TAG = MarkerContentProvider.class.getSimpleName();
    // authority
    public static final String AUTHORITY = "com.android.interactivity.maps";
    // path
    public static final String MARKER_PATH = "markers";

    // general Uri
    public static final Uri MARKER_CONTENT_URI = Uri.parse("content://"
        + AUTHORITY + "/" + MARKER_PATH);

    // MIME Type

    // all rows
    private static final String MARKER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + MARKER_PATH;

    // single rows
    private static final String MARKER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + MARKER_PATH;

    // constants with a different type of query through URI
    private static final int URI_MARKERS    = 1;
    private static final int URI_MARKER_ID  = 2;

    // Uri matcher
    private	static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, MARKER_PATH, URI_MARKERS);
        sUriMatcher.addURI(AUTHORITY, MARKER_PATH + "/#", URI_MARKER_ID);
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    // don't open db in onCreate section, only get mDBHelper instance;
    @Override
    public boolean onCreate() {
        if (Debug.MODE) Log.d(TAG, "onCreate");
        mDBHelper = new DBHelper(getContext());
        return true;
    }
    // read
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (Debug.MODE)  Log.d(TAG, "query: " + uri.toString());

        // check URI
        switch (sUriMatcher.match(uri)) {
            case URI_MARKERS: // general uri
                if (Debug.MODE) Log.d(TAG, "URI_MARKERS");
                break;
            case URI_MARKER_ID: // uri with id
                String id = uri.getLastPathSegment();
                if (Debug.MODE) Log.d(TAG, "URI_MARKER_ID: " + id);
                // add id to selection
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper._ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb = mDBHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBHelper.MARKER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        // ask ContentResolver to notify this cursor about data changing in MARKER_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), MARKER_CONTENT_URI);
        return cursor;
}

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_MARKERS: return MARKER_CONTENT_TYPE;
            case URI_MARKER_ID: return MARKER_CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (Debug.MODE) Log.d(TAG, "insert: " + uri.toString());
        if (sUriMatcher.match(uri) != URI_MARKERS)
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        mDb = mDBHelper.getWritableDatabase();
        long rowID = mDb.insert(DBHelper.MARKER_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(MARKER_CONTENT_URI, rowID);
        // notify about changing in resultUri
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (Debug.MODE) Log.d(TAG, "delete: " + uri.toString());

        // check URI
        switch (sUriMatcher.match(uri)) {
            case URI_MARKERS: // general uri
                if (Debug.MODE) Log.d(TAG, "URI_MARKERS");
                break;
            case URI_MARKER_ID: // uri with id
                String id = uri.getLastPathSegment();
                if (Debug.MODE) Log.d(TAG, "URI_MARKER_ID: " + id);
                // add id to selection
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper._ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb = mDBHelper.getWritableDatabase();
        int cnt = mDb.delete(DBHelper.MARKER_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (Debug.MODE) Log.d(TAG, "update: " + uri.toString());
        // check URI
        switch (sUriMatcher.match(uri)) {
            case URI_MARKERS: // general uri
                if (Debug.MODE) Log.d(TAG, "URI_MARKERS");
                break;
            case URI_MARKER_ID: // uri with id
                String id = uri.getLastPathSegment();
                if (Debug.MODE) Log.d(TAG, "URI_MARKER_ID: " + id);
                // add id to selection
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper._ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb = mDBHelper.getWritableDatabase();
        int cnt = mDb.update(DBHelper.MARKER_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
