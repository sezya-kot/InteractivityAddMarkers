package com.android.interactivity.maps;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.android.interactivity.maps.adapter.MarkerCursorAdapter;
import com.android.interactivity.maps.db.DBHelper;
import com.android.interactivity.maps.db.MarkerContentProvider;
import com.android.interactivity.maps.system.Debug;

/**
 * Created by kot on 28-Nov-14.
 */
public class MarkerListActivity extends FragmentActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{

    public static final String TAG = MarkerListActivity.class.getSimpleName();

    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;
    public static final String WHAT_SHOW = "WHAT_SHOW";
    public static final String CURSOR_ID = "CURSOR_ID";

    ListView mListView;
    MarkerCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_list);
        displayListView();


    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void displayListView() {
        mListView = (ListView) findViewById(R.id.marker_list);

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
        mCursorAdapter = new MarkerCursorAdapter(
                this,
                null
        );

        mListView.setAdapter(mCursorAdapter);
        mListView.setOnItemClickListener(this);
    }
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(this,
                MarkerContentProvider.MARKER_CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.changeCursor(null);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Debug.MODE) Log.d(TAG, "Position: " + position);
        if (Debug.MODE) Log.d(TAG, "ID: " + id);

        Intent i = new Intent(this, MapsActivity.class);
            i.putExtra(WHAT_SHOW, MapsActivity.SHOW_ONE_MARKER);
            i.putExtra(CURSOR_ID, (int)id);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MapsActivity.class);
            i.putExtra(CURSOR_ID, -1);
        startActivity(i);
        finish();
    }
}
