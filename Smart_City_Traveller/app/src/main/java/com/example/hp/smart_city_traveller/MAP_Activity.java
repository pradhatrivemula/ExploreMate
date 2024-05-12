package com.example.hp.smart_city_traveller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by hp on 8/8/2016.
 */
public class MAP_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat, lng;
    String name;
    SharedPreferences sp;
    TextView dist,time;
    double mylat, mylng;
    String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.smap);
        mapFragment.getMapAsync(this);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("MAP View");
        dist= (TextView) findViewById(R.id.ma_distance);
        time= (TextView) findViewById(R.id.ma_time);
        Intent i = getIntent();
        lat = Double.parseDouble(i.getStringExtra("lat"));
        lng = Double.parseDouble(i.getStringExtra("lng"));
        Toast.makeText(this, lat+","+lng, Toast.LENGTH_SHORT).show();

        name = i.getStringExtra("name");
        sp = getSharedPreferences("travel", Context.MODE_PRIVATE);
        uid=sp.getString("userid","");
        mylat = Double.parseDouble(sp.getString(uid+"lat", "0.0"));
        mylng = Double.parseDouble(sp.getString(uid+"lng", "0.0"));

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Location myloc=new Location("");
        myloc.setLatitude(mylat);
        myloc.setLongitude(mylng);

        Location destloc=new Location("");
        destloc.setLatitude(lat);
        destloc.setLongitude(lng);

        double diff=myloc.distanceTo(destloc);
        diff=diff/1000;
        String temp=""+diff;
        String temp1[]=temp.split("\\.");

        if(temp1.length>1)
        {
            int c=temp1[1].length();
            c=c-1;

            String temp4=temp1[1].substring(0,1);
            String temp3=temp1[0]+"."+temp4;
            String s="<b>"+"Distance :"+"</b>"+temp3+" Kms";
            dist.setText(Html.fromHtml(s));
        }
        else
        {
            String s="<b>"+"Distance :"+"</b>"+diff+" Kms";
            dist.setText(Html.fromHtml(s));
        }

        double t=diff*3;
        String s1="<b>"+"Time :"+"</b>"+Math.round(t)+" Mins";
        time.setText(Html.fromHtml(s1));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng ll = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 12));
        MarkerOptions mo = new MarkerOptions();
        mo.title(name);
        mo.position(ll);
        mMap.addMarker(mo);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        MarkerOptions my=new MarkerOptions();
        my.title("My Location");
        LatLng myll=new LatLng(mylat,mylng);
        my.position(myll);
        my.icon(BitmapDescriptorFactory.fromResource(R.drawable.mymark));
        mMap.addMarker(my);


        PolylineOptions po=new PolylineOptions();
        po.add(ll);
        po.add(myll);
        po.color(Color.BLUE);
        po.width(6);

        Polyline line=mMap.addPolyline(po);
        line.setClickable(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
