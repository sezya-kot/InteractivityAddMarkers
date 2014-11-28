package com.android.interactivity.maps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.interactivity.maps.db.DBHelper;
import com.android.interactivity.maps.db.MarkerContentProvider;
import com.android.interactivity.maps.model.MyMarker;
import com.android.interactivity.maps.system.Debug;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends ActionBarActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener,
        GoogleMap.OnMarkerDragListener{

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String MARKER_TITLE                 = "MARKER_TITLE";
    public static final String MARKER_DESCRIPTION           = "MARKER_DESCRIPTION";
    public static final String POINT_LONGITUDE              = "POINT_LONGITUDE";
    public static final String POINT_LATITUDE               = "POINT_LATITUDE";
    public static final String MARKER_CREATING_KEY          = "MARKER_CREATING_KEY";
    public static final String MARKER_ID                   = "MARKER_ID";
    public static final int SHOW_ALL_MARKERS                = 1;
    private static final int EDIT                           = 1;
    private static final int ADD_NEW                        = 2;
    public static final int SHOW_ONE_MARKER                 = 2;
    private static final String MY_MARKER                   = "MY_MARKER";
    private static final String MYMARKERS                   = "MY_MARKERS";

    MyMarker mMyMarker;
    Marker mMarker;

    ImageButton mMarkerList;
    Button mAddEdit;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setIU();

        if (savedInstanceState == null) {
            mMyMarker = new MyMarker();
            mMarker = null;
        } else {
            mMyMarker = savedInstanceState.getParcelable(MY_MARKER);
        }
        setUpMapIfNeeded();
    }
    private void setIU() {
        mMarkerList = (ImageButton) findViewById(R.id.list);
        mMarkerList.setOnClickListener(this);
        mAddEdit = (Button) findViewById(R.id.next);
        mAddEdit.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        {
            outState.putParcelable(MY_MARKER, mMyMarker);
        }
    }

    /**
     * set UI of map
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            if(Debug.MODE) { Log.d(TAG, "mMap was null!!!");}

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMarkerDragListener(this);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                if(Debug.MODE) { Log.d(TAG, "mMap exist!!!");}
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMyMarker.setDb_id(getIntent().getIntExtra((MarkerListActivity.CURSOR_ID), -1));
            Cursor cursor = getContentResolver().query(MarkerContentProvider.MARKER_CONTENT_URI, null, null, null, null);
                drawMarkersFromDb(cursor);
            cursor.close();
        if (mMyMarker.getDb_id() > 0) buttonToEdit();
        }


    private void drawMarkersFromDb(Cursor cursor) {
        if (cursor == null) {
            // Insert code here to handle the error.
            Log.e(TAG, "Cursor is null!");
        } else if (cursor.getCount() < 1) {
            // If the Cursor is empty, the provider found no matches
            Log.w(TAG, "Cursor is empty!");
        } else {
            // Insert code here to do something with the results
            Log.v(TAG, "Cursor is right!");
            while (cursor.moveToNext()) {
                drawMarkerOnMap(cursor);
            }
        }
    }


    private void drawMarkerOnMap(Cursor c) {
        if (mMap != null ) {
            mMarker = mMap.addMarker(cursorToMarkerOptions(c));
            if (mMyMarker.getDb_id() == c.getLong(c.getColumnIndex(DBHelper._ID))) {
                mMarker.setDraggable(true);
                mMarker.showInfoWindow();
                fillFromCursor(c);
            }
            mMarker = null;
            }
    }

    private void fillFromCursor(Cursor c) {
        mMyMarker.setTitle(c.getString(c.getColumnIndex(DBHelper.FIELD_TITLE)));
        mMyMarker.setDescription(c.getString(c.getColumnIndex(DBHelper.FIELD_DSC)));
        mMyMarker.setLatitude(c.getInt(c.getColumnIndex(DBHelper.FIELD_LAT)));
        mMyMarker.setLongitude(c.getInt(c.getColumnIndex(DBHelper.FIELD_LNG)));
    }
    private MarkerOptions cursorToMarkerOptions(Cursor c) {
        MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(
                    c.getDouble(c.getColumnIndex(DBHelper.FIELD_LAT)),
                    c.getDouble(c.getColumnIndex(DBHelper.FIELD_LNG))
            ));
            mo.title(c.getString(c.getColumnIndex(DBHelper.FIELD_TITLE)));
            mo.snippet(c.getString(c.getColumnIndex(DBHelper.FIELD_DSC)));
        return mo;

    }

    @Override
    public void onMapClick(LatLng point) {
        drawMarker(point);
        Log.d(TAG, "onMapClick(" + point.latitude + ", " + point.longitude + ")");
        buttonToAdd();
    }

    private void buttonToAdd() {
        mAddEdit.setVisibility(View.VISIBLE);
        mAddEdit.setText(getString(R.string.add_marker));
    }

    // draw for click on the map

    private void drawMarker(LatLng point) {
        mMyMarker.remove();
        if (mMarker != null) mMarker.remove();
        if (mMap != null){
            mMarker = mMap.addMarker(new MarkerOptions().position(point));
            mMarker.setDraggable(true);
        if (Debug.MODE) Log.d(TAG, "mMarker.getId(): " + mMarker.getId());
            mMarker.showInfoWindow();
            getMarkerInfo(mMarker);
        }
    }

    private void getMarkerInfo(Marker m) {
        mMyMarker.setTitle(m.getTitle());
        mMyMarker.setDescription(m.getSnippet());
        mMyMarker.setLatitude(m.getPosition().latitude);
        mMyMarker.setLongitude(m.getPosition().longitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.exit:
                finish();
                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Marker wasn't created!", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                            addMyMarkerFromIntent(data);
                            saveMarker();
                    }
                    break;
                case 2: {
                    if (resultCode == RESULT_OK) {
                            addMyMarkerFromIntent(data);
                            if(mMyMarker.getDb_id() > 0) updateMarker();
                            else saveMarker();
                    }
                    break;
                }
                default:
            }
        setUpMap();
        }
    }

    private void addMyMarkerFromIntent(Intent data) {
        mMyMarker.setTitle(data.getStringExtra(MARKER_TITLE));
        mMyMarker.setDescription(data.getStringExtra(MARKER_DESCRIPTION));
        mMyMarker.setLatitude(data.getDoubleExtra(POINT_LATITUDE, 0));
        mMyMarker.setLongitude(data.getDoubleExtra(POINT_LONGITUDE, 0));
    }

    private void updateMarker() {
        ContentValues cv = getContentValues();
        Uri updateUri = Uri.withAppendedPath(MarkerContentProvider.MARKER_CONTENT_URI, "" + mMyMarker.getDb_id());
        getContentResolver().update(updateUri, cv, null, null);
    }

    private void saveMarker() {
        ContentValues cv = getContentValues();
        getContentResolver().insert(MarkerContentProvider.MARKER_CONTENT_URI, cv);

    }

    private ContentValues getContentValues() {
        ContentValues cv= new ContentValues();
        cv.put(DBHelper.FIELD_TITLE, mMyMarker.getTitle());
        cv.put(DBHelper.FIELD_DSC, mMyMarker.getDescription());
        cv.put(DBHelper.FIELD_LAT, mMyMarker.getLatitude());
        cv.put(DBHelper.FIELD_LNG, mMyMarker.getLongitude());
        return cv;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        hideButton();
        return true;
    }

    private void hideButton() {
        mAddEdit.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list:
                Intent i = new Intent(this, MarkerListActivity.class);
                startActivity(i);
                break;
            case R.id.next:
                if (mAddEdit.getText().toString().equals(getString(R.string.edit))) {
                    if (Debug.MODE) Log.d(TAG, "Button edit!");
                    Intent intent = packIntent();
                    startActivityForResult(intent, 2);
                }
                else if (mAddEdit.getText().toString().equals(getString(R.string.add_marker))){
                    if (Debug.MODE) Log.d(TAG, "Button add marker!");
                    Intent intent = packIntent();
                    startActivityForResult(intent, 1);
                }
                break;

        }
    }

    private Intent packIntent() {
        Intent intent = new Intent(this, MarkerControllerActivity.class);
        intent.putExtra(MARKER_ID, mMyMarker.getDb_id());
        intent.putExtra(MARKER_TITLE, mMyMarker.getTitle());
        intent.putExtra(MARKER_DESCRIPTION, mMyMarker.getDescription());
        intent.putExtra(POINT_LATITUDE, mMyMarker.getLatitude());
        intent.putExtra(POINT_LONGITUDE, mMyMarker.getLongitude());
        return intent;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.showInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        getMarkerInfo(marker);
        buttonToEdit();

    }

    private void buttonToEdit() {
        mAddEdit.setVisibility(View.VISIBLE);
        mAddEdit.setText("Edit");
    }


}
