package com.example.hp.smart_city_traveller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by hp on 8/5/2016.
 */
public class Test extends AppCompatActivity{

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        mContext = this;
        GPS_Tracker gps;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Test.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else
        {
            Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPS_Tracker(mContext, Test.this);

            // Check if GPS enabled
            if (gps.canGetLocation())
            {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();


                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else
            {

            }
        }
    }



}
