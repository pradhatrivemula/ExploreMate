package com.example.hp.smart_city_traveller;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class Current_Route extends Fragment implements OnMapReadyCallback {
    TextView totdisttxt;
    Dialog da;
    RelativeLayout ray;
    private GoogleMap mMap;
    TableRow tbprog,tbfinish,tbnav;
    ImageView left,right;
    TextView txt;
    Button finish;
    SupportMapFragment smf;
    Calendar cal;
    String date,uid;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    double mylat,mylng;
    Marker[] mp;
    Marker yourmp;
    int pcount=-1;
    Boolean y=false;
    String[] mname,mcontact,mlat,mlng,maddress,mdistance,misopen,mopentill,mrating,mreview,mprice,mchoice;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frame,null,false);
        cal=Calendar.getInstance();
        ray= (RelativeLayout) v.findViewById(R.id.ray);
        totdisttxt= (TextView) v.findViewById(R.id.cr_totdist);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date d=Calendar.getInstance().getTime();
        date=sdf.format(d);
        sp=getActivity().getSharedPreferences("travel", Context.MODE_PRIVATE);
        uid= sp.getString("userid", "");

        tbprog= (TableRow) v.findViewById(R.id.tbprog);
        tbnav= (TableRow) v.findViewById(R.id.tbnav);
        tbfinish= (TableRow) v.findViewById(R.id.tbfinish);
        left= (ImageView) v.findViewById(R.id.mr_left);
        right= (ImageView) v.findViewById(R.id.mr_right);
        txt= (TextView) v.findViewById(R.id.mr_text);
        txt.setText("1. Starting Position");
        finish= (Button) v.findViewById(R.id.mr_finish);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });

        right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                right();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbprog.setVisibility(View.VISIBLE);
                tbfinish.setVisibility(View.GONE);
                tbnav.setVisibility(View.GONE);
                new deloldplaces().execute(uid);
            }
        });


        return  v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        new getcurrent().execute(uid, date);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    String a = marker.getTitle();
                    String temp[] = a.split("\\.");
                    for (int j = 0; j < mname.length; j++)
                    {
                        if (mname[j].compareTo(temp[1]) == 0) {
                            markdialog(j);
                            break;
                        }
                    }
                } catch (Exception e) {

                }
                return true;
            }
        });
    }

    public class getcurrent extends AsyncTask<String,JSONObject,String>
    {
        @Override
        protected String doInBackground(String... params) {
            String a="b";
            RestAPI api=new RestAPI();
            try
            {
                JSONObject json=api.getcurrentroute(params[0],params[1]);
                JSONParse jp=new JSONParse();
                a=jp.mainparse(json);
            } catch (Exception e) {
                a=e.getMessage();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.compareTo("no")!=0)
            {
                tbprog.setVisibility(View.GONE);
                tbfinish.setVisibility(View.VISIBLE);
                tbnav.setVisibility(View.VISIBLE);
                s=s.replaceAll("false","");

                String t1[]=s.split("\\$");
                String t2[]=t1[0].split(",");

                mylat = Double.parseDouble(t2[0]);
                mylng = Double.parseDouble(t2[1]);

                s=t1[1];

                String temp[]=s.split("\\*\\#");
                mname=new String[temp.length];
                mcontact=new String[temp.length];
                mlat=new String[temp.length];
                mlng=new String[temp.length];
                maddress=new String[temp.length];
                mdistance=new String[temp.length];
                misopen=new String[temp.length];
                mopentill=new String[temp.length];
                mrating=new String[temp.length];
                mreview=new String[temp.length];
                mprice=new String[temp.length];
                mchoice=new String[temp.length];
                mp=new Marker[temp.length];

                for(int i=0;i<temp.length;i++)
                {
                    String temp1[]=temp[i].split("\\*");
                    mname[i]=temp1[2];
                    mcontact[i]=temp1[3];
                    mlat[i]=temp1[4];
                    mlng[i]=temp1[5];
                    maddress[i]=temp1[6];
                    mdistance[i]=temp1[7];
                    misopen[i]=temp1[8];
                    mopentill[i]=temp1[9];
                    mrating[i]=temp1[10];
                    mreview[i]=temp1[11];
                    mprice[i]=temp1[12];
                    mchoice[i]=temp1[13];
                }

                total_travel_distance();
                drawonmap();

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
            else if(s.compareTo("no")==0)
            {
                tbprog.setVisibility(View.GONE);
                Snackbar snack=Snackbar.make(ray,"No Travel Plans",Snackbar.LENGTH_SHORT);
                View vw=snack.getView();
                TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                txt.setTextColor(Color.RED);
                snack.show();
            }
            else {
                if (s.contains("Unable to resolve host")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                    ad.setTitle("Unable to Connect!");
                    ad.setMessage("Check your Internet Connection,Unable to connect the Server");
                    ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    ad.show();
                } else {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void drawonmap()
    {
        try {
            LatLng ll = new LatLng(mylat, mylng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 12));
            MarkerOptions myo = new MarkerOptions();
            myo.title("Start & End Position");
            myo.position(ll);
            yourmp=mMap.addMarker(myo);
            yourmp.showInfoWindow();
            MarkerOptions m1 = null;
            LatLng ll1 = null;
            MarkerOptions m2 = null;
            LatLng ll2 = null;
            PolylineOptions po = null;
            PolylineOptions po1 = new PolylineOptions();
            for (int i = 0; i < mname.length; i++)
            {
                m1 = new MarkerOptions();
                m1.title((i+2)+"."+mname[i]);
                ll1 = new LatLng(Double.parseDouble(mlat[i]), Double.parseDouble(mlng[i]));
                m1.position(ll1);
                mp[i]=mMap.addMarker(m1);

                if ((i + 1) < mname.length) {
                    m2 = new MarkerOptions();
                    m2.title((i+2)+"."+mname[i+1]);
                    ll2 = new LatLng(Double.parseDouble(mlat[i + 1]), Double.parseDouble(mlng[i + 1]));
                    m2.position(ll2);
                }

                po = new PolylineOptions();

                if ((i + 1) < mname.length) {
                    po.add(ll1);
                    po.add(ll2);
                    po.color(Color.BLUE);
                    po.width(5);
                }

                mMap.addPolyline(po);

            }



            LatLng ll0 = new LatLng(Double.parseDouble(mlat[0]), Double.parseDouble(mlng[0]));
            po1.add(ll);
            po1.add(ll0);
            po1.color(Color.RED);
            po1.width(6);

            if (mname.length > 1) {
                LatLng lll = new LatLng(Double.parseDouble(mlat[mname.length - 1]), Double.parseDouble(mlng[mname.length - 1]));
                po1.add(ll);
                po1.add(lll);
                po1.color(Color.RED);
                po1.width(6);
            }
            mMap.addPolyline(po1);
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Draw"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void left()
    {
        pcount--;
        if(pcount>=0)
        {
            if(pcount<mname.length)
            {
                right.setVisibility(View.VISIBLE);
                left.setVisibility(View.VISIBLE);
                LatLng ll = new LatLng(Double.parseDouble(mlat[pcount]),Double.parseDouble(mlng[pcount]));
                mp[pcount].showInfoWindow();
                txt.setText((pcount + 2) + ". " + mname[pcount]);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));

            }
        }
        else
        {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.INVISIBLE);
            LatLng ll = new LatLng(mylat, mylng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
            txt.setText("1. Starting point");
            yourmp.showInfoWindow();
        }

    }

    public void right()
    {
        pcount++;
        if(pcount<mname.length)
        {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
            LatLng ll = new LatLng(Double.parseDouble(mlat[pcount]),Double.parseDouble(mlng[pcount]));
            mp[pcount].showInfoWindow();
            txt.setText((pcount + 2) + ". " + mname[pcount]);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));

        }
        else
        {
            right.setVisibility(View.INVISIBLE);
            left.setVisibility(View.VISIBLE);
            LatLng ll = new LatLng(mylat, mylng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
            txt.setText((pcount+2)+". "+"End point");
            yourmp.showInfoWindow();
        }

    }

    public class deloldplaces extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.delroute(params[0]);
                JSONParse jp=new JSONParse();
                a=jp.parsing(json);
            } catch (Exception e) {
                a=e.getMessage();
            }

            return a;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tbprog.setVisibility(View.GONE);
            Snackbar snack=Snackbar.make(ray,"Travel Plan Successfully Finished,Hope You Liked It!!",Snackbar.LENGTH_SHORT);
            View vw=snack.getView();
            TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
            txt.setTextColor(Color.GREEN);
            snack.show();
        }
    }

    public void markdialog(int i)
    {
        i=pcount;
        da=new Dialog(getActivity());
        da.requestWindowFeature(Window.FEATURE_NO_TITLE);
        da.setContentView(R.layout.map_marker_dialog);
        TextView markname,markrating,markreview,markdist;
        markname= (TextView) da.findViewById(R.id.mm_name);
        markrating= (TextView) da.findViewById(R.id.mm_rating);
        markreview= (TextView) da.findViewById(R.id.mm_review);
        markdist= (TextView) da.findViewById(R.id.mm_dist);
        if(i==0)
        {
            markdist.setVisibility(View.VISIBLE);
            Location p1=new Location("");
            p1.setLatitude(mylat);
            p1.setLongitude(mylng);

            Location p2=new Location("");
            p2.setLatitude(Double.parseDouble(mlat[i]));
            p2.setLongitude(Double.parseDouble(mlng[i]));
            float dt=p1.distanceTo(p2)/1000;
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            String s2="<b>"+"Distance: "+"</b>"+numberFormat.format(dt)+" Km";
            markdist.setText(Html.fromHtml(s2));
        }
        else
        {
            markdist.setVisibility(View.VISIBLE);
            String md=calc_dist(i);
            markdist.setText(Html.fromHtml(md));
        }
        String s="<b>"+"Ratings: "+"</b>"+mrating[i];
        String s1="<b>"+"Review: "+"</b>"+mreview[i];
        markname.setText(mname[i]);
        markrating.setText(Html.fromHtml(s));
        markreview.setText(Html.fromHtml(s1));
        da.show();
        pcount=i;
        txt.setText((pcount + 2) + ". " + mname[i]);
        right.setVisibility(View.VISIBLE);
        left.setVisibility(View.VISIBLE);
    }

    public String calc_dist(int i)
    {
        Location p1=new Location("");
        p1.setLatitude(Double.parseDouble(mlat[i-1]));
        p1.setLongitude(Double.parseDouble(mlng[i-1]));

        Location p2=new Location("");
        p2.setLatitude(Double.parseDouble(mlat[i]));
        p2.setLongitude(Double.parseDouble(mlng[i]));
        float dt=p1.distanceTo(p2)/1000;
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        String s2="<b>"+"Distance: "+"</b>"+numberFormat.format(dt)+" Km";
        return s2;
    }

    public void total_travel_distance()
    {
        float totdist=0;
        for(int i=0;i<mname.length;i++)
        {
            Location l2=new Location("");
            l2.setLatitude(Double.parseDouble(mlat[i]));
            l2.setLongitude(Double.parseDouble(mlng[i]));

            Location l1=new Location("");
            l1.setLatitude(mylat);
            l1.setLongitude(mylng);

            if(i==0)
            {
                totdist+=l1.distanceTo(l2);
            }
            else
            {
                Location l3=new Location("");
                l3.setLatitude(Double.parseDouble(mlat[i-1]));
                l3.setLongitude(Double.parseDouble(mlng[i-1]));

                totdist+=l3.distanceTo(l2);

                if(i<mname.length-1)
                {
                    totdist+=l2.distanceTo(l1);
                }
            }
        }
        totdist=totdist/1000;
        DecimalFormat df=new DecimalFormat("#.00");
        totdisttxt.setText("Total Travel Distance: "+df.format(totdist)+" Km");
    }

}
