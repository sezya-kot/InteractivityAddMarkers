package com.android.interactivity.maps.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.interactivity.maps.R;
import com.android.interactivity.maps.db.DBHelper;

public class MarkerCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public MarkerCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.marker_item, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {
        TextView title = (TextView) v.findViewById(R.id.title_i);
        title.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_TITLE))+ " " + cursor.getInt(cursor.getColumnIndex(DBHelper._ID)));
        TextView dsc = (TextView) v.findViewById(R.id.dsc_i);
        dsc.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_DSC)));
        TextView lat = (TextView) v.findViewById(R.id.latd);
        lat.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_LAT)));
        TextView lngt = (TextView) v.findViewById(R.id.lngt);
        lngt.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FIELD_LNG)));
    }
}
