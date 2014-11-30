package com.android.interactivity.maps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.interactivity.maps.db.DBHelper;
import com.android.interactivity.maps.db.MarkerContentProvider;
import com.android.interactivity.maps.model.MyMarker;
import com.android.interactivity.maps.system.Debug;
import org.w3c.dom.Text;

import java.util.Map;

/**
 * Created by kot on 27-Nov-14.
 */
public class MarkerControllerActivity extends Activity implements View.OnClickListener{

    public static final String TAG = MarkerControllerActivity.class.getSimpleName();
    EditText mTitle;
    EditText mDescription;
    Button mAddMarker;
    boolean isEdit;
    MyMarker mMyMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_controller);
        isEdit = getIntent().getBooleanExtra(MapsActivity.IS_EDIT, false);
        mMyMarker = getIntent().getParcelableExtra(MapsActivity.MY_MARKER);

        if (getIntent() != null) {
            if (Debug.MODE) Log.d(TAG, "getIntent: " + getIntent().getStringExtra(MapsActivity.MARKER_TITLE));
        }

        setUI();
    }

    private void setUI() {
        mTitle = (EditText) findViewById(R.id.marker_title);
        mDescription = (EditText) findViewById(R.id.marker_description);
        mAddMarker = (Button) findViewById(R.id.add_marker);
        if (isEdit) {
            mDescription.setText(mMyMarker.getDescription());
            mTitle.setText(mMyMarker.getTitle());
            mAddMarker.setText(getString(R.string.edit_marker));
        }
        mAddMarker.setOnClickListener(this);
    }

    private boolean isEditTextsEmpty(EditText[] editTexts) {
        for(EditText e : editTexts) {
            if (TextUtils.isEmpty(e.getText().toString())) return true;
            else continue;
        } return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_marker:
                if (!isEditTextsEmpty(new EditText[] {mTitle, mDescription})) {
                    mMyMarker.setTitle(mTitle.getText().toString());
                    mMyMarker.setDescription(mDescription.getText().toString());
                    if (isEdit) {
                        updateMarker();
                    } else {
                        saveMarker();
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Enter empty fields!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
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
}
