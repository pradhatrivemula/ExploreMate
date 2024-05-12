package com.example.hp.smart_city_traveller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hp on 8/24/2016.
 */
public class Map_Point_Selection extends AppCompatActivity implements OnMapReadyCallback
{
    RelativeLayout ray;
    String lat="",lng="";
    Button sel,can;
    String fromtime,totime,to,type;
    private GoogleMap mMap;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_point_selection);
        ray= (RelativeLayout) findViewById(R.id.mray);
        sp=getSharedPreferences("travel", Context.MODE_PRIVATE);
        uid=sp.getString("userid","");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mpmap);
        mapFragment.getMapAsync(this);
        sel= (Button) findViewById(R.id.mpselection);
        can= (Button) findViewById(R.id.mpcancel);
        Intent i=getIntent();
        fromtime=i.getStringExtra("fromtime");
        totime=i.getStringExtra("totime");
        to=i.getStringExtra("to");
        if(to.compareTo("adv")==0)
        {
            type=i.getStringExtra("adv");
        }

        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(lat.compareTo("")!=0 && lng.compareTo("")!=0)
                {
                    editor = sp.edit();
                    editor.putString(uid+"lat",lat);
                    editor.putString(uid+"lng",lng);
                    editor.commit();

                    if (to.compareTo("rest") == 0) {
                        Intent i = new Intent(Map_Point_Selection.this, Planning_Rest.class);
                        i.putExtra("fromtime", fromtime);
                        i.putExtra("totime", totime);
                        startActivity(i);
                        finish();
                    } else if (to.compareTo("adv") == 0) {
                        Intent i = new Intent(Map_Point_Selection.this, Place_List.class);
                        i.putExtra("type", "adv");
                        startActivity(i);
                        finish();
                    }
                }
                else
                {
                    Snackbar snack=Snackbar.make(ray,"Select a Place!!",Snackbar.LENGTH_SHORT);
                    View vw=snack.getView();
                    TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                    txt.setTextColor(Color.RED);
                    snack.show();
                }
            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ll=new LatLng(Double.parseDouble("19.0760"),Double.parseDouble("72.8777"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,10));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                MarkerOptions m=new MarkerOptions();
                m.position(latLng);
                lat=""+latLng.latitude;
                lng=""+latLng.longitude;
                mMap.addMarker(m);
//                Toast.makeText(Map_Point_Selection.this,lat+","+lng, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
