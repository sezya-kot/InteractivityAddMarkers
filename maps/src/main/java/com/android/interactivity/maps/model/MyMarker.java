package com.android.interactivity.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kot on 28-Nov-14.
 */
public class MyMarker implements Parcelable {

    private int   db_id;
    private double mLatitude;
    private double mLongitude;
    private String mTitle;
    private String mDescription;

    public MyMarker() {
    }

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(db_id);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
    }
    public static final Parcelable.Creator<MyMarker> CREATOR
            = new Parcelable.Creator<MyMarker>() {
        public MyMarker createFromParcel(Parcel in) {
            return new MyMarker(in);
        }

        public MyMarker[] newArray(int size) {
            return new MyMarker[size];
        }
    };

    public MyMarker(Parcel in) {
        db_id = in.readInt();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mTitle = in.readString();
        mDescription = in.readString();
    }

    public Marker getMarker(GoogleMap map) {
        Marker marker = null;
        if (map != null){
            marker = map.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)));
            marker.setTitle(mTitle);
            marker.setSnippet(mDescription);
            marker.setDraggable(true);
            marker.showInfoWindow();
        }
        return marker;
    }

    public void remove() {
        this.db_id = -1;
        this.mTitle = "";
        this.mDescription = "";
        this.mLatitude = 0;
        this.mLongitude = 0;
    }

    public boolean isEmpty() {
        if (TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mDescription) && mLatitude == 0 && mLongitude == 0) {return true;}
        else return false;
    }
}
