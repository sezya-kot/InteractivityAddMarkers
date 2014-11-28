package com.android.interactivity.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_controller);

        if (getIntent() != null) {
            if (Debug.MODE) Log.d(TAG, "getIntent: " + getIntent().getStringExtra(MapsActivity.MARKER_TITLE));
        }

        setUI();
    }

    private void setUI() {
        mTitle = (EditText) findViewById(R.id.marker_title);
        mTitle.setText(getIntent().getStringExtra(MapsActivity.MARKER_TITLE));
        mDescription = (EditText) findViewById(R.id.marker_description);
        mDescription.setText(getIntent().getStringExtra(MapsActivity.MARKER_DESCRIPTION));
        mAddMarker = (Button) findViewById(R.id.add_marker);
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
                    Intent resultIntent = getIntent();
                    resultIntent.putExtra(MapsActivity.MARKER_TITLE, mTitle.getText().toString());
                    resultIntent.putExtra(MapsActivity.MARKER_DESCRIPTION, mDescription.getText().toString());
                    resultIntent.putExtra(MarkerListActivity.WHAT_SHOW, -1);
                    setResult(RESULT_OK, resultIntent);
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
}
