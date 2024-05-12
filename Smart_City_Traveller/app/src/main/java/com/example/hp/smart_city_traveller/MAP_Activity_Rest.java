package com.example.hp.smart_city_traveller;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hp on 8/19/2016.
 */
public class MAP_Activity_Rest extends AppCompatActivity implements OnMapReadyCallback
{
    String[] routes=new String[3];
    Boolean finish=false;
    float r1=0,r2=0,r3=0;
    TextView totdistxt;
    int datasentcount=0;
    int route=1;
    Dialog da,df;
    private GoogleMap mMap;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    double mylat,mylng;
    String[] f0name,f0contact,f0lat,f0lng,f0address,f0distance,f0isopen,f0opentill,f0rating,f0review,f0price,f0choice;
    String[] f1name,f1contact,f1lat,f1lng,f1address,f1distance,f1isopen,f1opentill,f1rating,f1review,f1price,f1choice;
    String[] f2name,f2contact,f2lat,f2lng,f2address,f2distance,f2isopen,f2opentill,f2rating,f2review,f2price,f2choice;

    String[] mname,mcontact,mlat,mlng,maddress,mdistance,misopen,mopentill,mrating,mreview,mprice,mchoice;
    int pos;
    String start="",next="",uid;
    int breakfast=0,lunch=0,cafe=0,dinner=0,place=0,placeindex=0;
    String breakname,breakcontact,breaklat,breaklng,breakaddress,breakdistance,breakisopen,breakopentill,breakrating,breakreview,breakprice,breakchoice;
    String lunchname,lunchcontact,lunchlat,lunchlng,lunchaddress,lunchdistance,lunchisopen,lunchopentill,lunchrating,lunchreview,lunchprice,lunchchoice;
    String cafename,cafecontact,cafelat,cafelng,cafeaddress,cafedistance,cafeisopen,cafeopentill,caferating,cafereview,cafeprice,cafechoice;
    String dinnername,dinnercontact,dinnerlat,dinnerlng,dinneraddress,dinnerdistance,dinnerisopen,dinneropentill,dinnerrating,dinnerreview,dinnerprice,dinnerchoice;
    String[] placename,placecontact,placelat,placelng,placeaddress,placedistance,placeisopen,placeopentill,placerating,placereview,placeprice,placechoice;

    String[] p1name,p1contact,p1lat,p1lng,p1address,p1distance,p1isopen,p1opentill,p1rating,p1review,p1price,p1choice;
    String[] p2name,p2contact,p2lat,p2lng,p2address,p2distance,p2isopen,p2opentill,p2rating,p2review,p2price,p2choice;
    String[] p3name,p3contact,p3lat,p3lng,p3address,p3distance,p3isopen,p3opentill,p3rating,p3review,p3price,p3choice;

    ImageView left,right,rleft,rright;
    TextView txt,rtxt;
    int pcount=-1,mcount=0;
    int pstart=1;
    int pend=0;
    Marker[] mp;
    Marker yourmp;
    String id="";
    Calendar cal;
    String date,totime;
    Button confirm,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_rest);
        left= (ImageView) findViewById(R.id.mr_left);
        right= (ImageView) findViewById(R.id.mr_right);
        txt= (TextView) findViewById(R.id.mr_text);
        txt.setText("1. Starting point");
        rleft= (ImageView) findViewById(R.id.mr_rleft);
        rright= (ImageView) findViewById(R.id.mr_rright);
        rtxt= (TextView) findViewById(R.id.mr_rtext);
        rtxt.setText("Route 1");
        confirm= (Button) findViewById(R.id.mra_confirm);
        totdistxt= (TextView) findViewById(R.id.mra_totdist);
        cancel= (Button) findViewById(R.id.mra_cancel);
        cal=Calendar.getInstance();
        finaldailog();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pmap);
        mapFragment.getMapAsync(this);
        Intent i=getIntent();
        f0name=i.getStringArrayExtra("fname");
        f0contact=i.getStringArrayExtra("fcontact");
        f0lat=i.getStringArrayExtra("flat");
        f0lng=i.getStringArrayExtra("flng");
        f0address=i.getStringArrayExtra("faddress");
        f0distance=i.getStringArrayExtra("fdistance");
        f0isopen=i.getStringArrayExtra("fisopen");
        f0opentill=i.getStringArrayExtra("fopentill");
        f0rating = i.getStringArrayExtra("frating");
        f0review=i.getStringArrayExtra("freview");
        f0price=i.getStringArrayExtra("fprice");
        f0choice=i.getStringArrayExtra("fchoice");

        f1name=i.getStringArrayExtra("f1name");
        f1contact=i.getStringArrayExtra("f1contact");
        f1lat=i.getStringArrayExtra("f1lat");
        f1lng=i.getStringArrayExtra("f1lng");
        f1address=i.getStringArrayExtra("f1address");
        f1distance=i.getStringArrayExtra("f1distance");
        f1isopen=i.getStringArrayExtra("f1isopen");
        f1opentill=i.getStringArrayExtra("f1opentill");
        f1rating = i.getStringArrayExtra("f1rating");
        f1review=i.getStringArrayExtra("f1review");
        f1price=i.getStringArrayExtra("f1price");
        f1choice=i.getStringArrayExtra("f1choice");

        f2name=i.getStringArrayExtra("f2name");
        f2contact=i.getStringArrayExtra("f2contact");
        f2lat=i.getStringArrayExtra("f2lat");
        f2lng=i.getStringArrayExtra("f2lng");
        f2address=i.getStringArrayExtra("f2address");
        f2distance=i.getStringArrayExtra("f2distance");
        f2isopen=i.getStringArrayExtra("f2isopen");
        f2opentill=i.getStringArrayExtra("f2opentill");
        f2rating = i.getStringArrayExtra("f2rating");
        f2review=i.getStringArrayExtra("f2review");
        f2price=i.getStringArrayExtra("fprice");
        f2choice=i.getStringArrayExtra("f2choice");

        start=i.getStringExtra("start");
        next=i.getStringExtra("next");
        totime=i.getStringExtra("totime");

//        for(int a=0;a<f0name.length;a++)
//        {
//            Toast.makeText(MAP_Activity_Rest.this,f0name[a]+"\n"+f1name[a]+"\n"+f2name[a], Toast.LENGTH_SHORT).show();
//        }

        sp = getSharedPreferences("travel", Context.MODE_PRIVATE);
        uid=sp.getString("userid","");
        mylat = Double.parseDouble(sp.getString(uid+"lat", "0.0"));
        mylng = Double.parseDouble(sp.getString(uid+"lng", "0.0"));

        String tempto[]=totime.split(":");
        if(Integer.parseInt(tempto[0])>=0 && Integer.parseInt(tempto[0])<=4)
        {
            int d = (cal.get(Calendar.DAY_OF_MONTH)+1);
            int m = (cal.get(Calendar.MONTH) + 1);
            int y = cal.get(Calendar.YEAR);
            String mn="",dd="";
            if(String.valueOf(m).length()==1)
            {
                mn="0"+m;
            }
            else
            {
                mn=""+m;
            }
            if(String.valueOf(d).length()==1)
            {
                dd="0"+d;
            }
            else
            {
                dd=""+d;
            }
            date = y + "-" + mn + "-" + dd;
        }
        else
        {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date d=Calendar.getInstance().getTime();
            date=sdf.format(d);
        }

        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });

        rleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rleft();
            }
        });

        rright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rright();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor=sp.edit();
                editor.putString(uid+"mylat",mylat+"");
                editor.putString(uid+"mylng",mylng+"");
                editor.commit();

                finaldailog();
                new deloldplaces().execute(uid);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
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
        return  true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

        if(route==1)
        {
            setupplaces(f0name, f0contact, f0lat, f0lng, f0address, f0distance, f0isopen, f0opentill, f0rating, f0review, f0price, f0choice);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void setupplaces(String[] fname,String[] fcontact,String[] flat,String[] flng,String[] faddress,String[] fdistance,String[] fisopen,String[] fopentill,String[] frating,String[] freview,String[] fprice,String[] fchoice)
    {
        try {
            for (int j = 0; j < fname.length; j++)
            {

                if (fname[j] == null)
                {
                    pos = j;
                    break;
                }
                else
                {
                    if (fchoice[j].compareTo("Breakfast") == 0) {
                        breakfast = 1;
                        breakname = fname[j];
                        breakcontact = fcontact[j];
                        breaklat = flat[j];
                        breaklng = flng[j];
                        breakaddress = faddress[j];
                        breakdistance = fdistance[j];
                        breakisopen = fisopen[j];
                        breakopentill = fopentill[j];
                        breakrating = frating[j];
                        breakreview = freview[j];
                        breakprice = fprice[j];
                        breakchoice = fchoice[j];
//                        Toast.makeText(MAP_Activity_Rest.this,"b-"+ breakname, Toast.LENGTH_SHORT).show();
                    } else if (fchoice[j].compareTo("Lunch") == 0) {
                        lunch = 1;
                        lunchname = fname[j];
                        lunchcontact = fcontact[j];
                        lunchlat = flat[j];
                        lunchlng = flng[j];
                        lunchaddress = faddress[j];
                        lunchdistance = fdistance[j];
                        lunchisopen = fisopen[j];
                        lunchopentill = fopentill[j];
                        lunchrating = frating[j];
                        lunchreview = freview[j];
                        lunchprice = fprice[j];
                        lunchchoice = fchoice[j];
//                        Toast.makeText(MAP_Activity_Rest.this,"l-"+ lunchname, Toast.LENGTH_SHORT).show();
                    } else if (fchoice[j].compareTo("Evening Snacks") == 0) {
                        cafe = 1;
                        cafename = fname[j];
                        cafecontact = fcontact[j];
                        cafelat = flat[j];
                        cafelng = flng[j];
                        cafeaddress = faddress[j];
                        cafedistance = fdistance[j];
                        cafeisopen = fisopen[j];
                        cafeopentill = fopentill[j];
                        caferating = frating[j];
                        cafereview = freview[j];
                        cafeprice = fprice[j];
                        cafechoice = fchoice[j];
//                        Toast.makeText(MAP_Activity_Rest.this,"e-"+ cafename, Toast.LENGTH_SHORT).show();
                    }
                    else if (fchoice[j].compareTo("Dinner") == 0)
                    {
                        dinner = 1;
                        dinnername = fname[j];
                        dinnercontact = fcontact[j];
                        dinnerlat = flat[j];
                        dinnerlng = flng[j];
                        dinneraddress = faddress[j];
                        dinnerdistance = fdistance[j];
                        dinnerisopen = fisopen[j];
                        dinneropentill = fopentill[j];
                        dinnerrating = frating[j];
                        dinnerreview = freview[j];
                        dinnerprice = fprice[j];
                        dinnerchoice = fchoice[j];
//                        Toast.makeText(MAP_Activity_Rest.this,"d-"+ dinnername, Toast.LENGTH_SHORT).show();
                    } else if (fchoice[j].compareTo("place") == 0) {
                        place++;
                    }
                }
            }
            place(fname,fcontact,flat,flng,faddress,fdistance,fisopen,fopentill,frating,freview,fprice,fchoice);
        }
        catch (Exception e)
        {
            Toast.makeText(MAP_Activity_Rest.this, "setup"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        setmaparray();
    }

    public void place(String[] fname,String[] fcontact,String[] flat,String[] flng,String[] faddress,String[] fdistance,String[] fisopen,String[] fopentill,String[] frating,String[] freview,String[] fprice,String[] fchoice)
    {
        try {
            placename = new String[place];
            placecontact = new String[place];
            placelat = new String[place];
            placelng = new String[place];
            placeaddress = new String[place];
            placedistance = new String[place];
            placeisopen = new String[place];
            placeopentill = new String[place];
            placerating = new String[place];
            placereview = new String[place];
            placeprice = new String[place];
            placechoice = new String[place];
            int p = 0;
            for (int i = 0; i < fname.length; i++) {
                if (fchoice[i] != null) {
                    if (fchoice[i].compareTo("place") == 0) {
                        placename[p] = fname[i];
                        placecontact[p] = fcontact[i];
                        placelat[p] = flat[i];
                        placelng[p] = flng[i];
                        placeaddress[p] = faddress[i];
                        placedistance[p] = fdistance[i];
                        placeisopen[p] = fisopen[i];
                        placeopentill[p] = fopentill[i];
                        placerating[p] = frating[i];
                        placereview[p] = freview[i];
                        placeprice[p] = fprice[i];
                        placechoice[p] = fchoice[i];
//                        Toast.makeText(MAP_Activity_Rest.this, placename[p], Toast.LENGTH_SHORT).show();
                        p++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(MAP_Activity_Rest.this, "f"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setmaparray()
    {
        try {
            mname = new String[pos];
            mcontact = new String[pos];
            mlat = new String[pos];
            mlng = new String[pos];
            maddress = new String[pos];
            mdistance = new String[pos];
            misopen = new String[pos];
            mopentill = new String[pos];
            mrating = new String[pos];
            mreview = new String[pos];
            mprice = new String[pos];
            mchoice = new String[pos];
            mp=new Marker[(pos+1)];
//            Toast.makeText(MAP_Activity_Rest.this, "size="+mname.length, Toast.LENGTH_SHORT).show();
            if (start.compareTo("place") != 0) {
//                Toast.makeText(MAP_Activity_Rest.this, "Not Place" + "   " + start, Toast.LENGTH_SHORT).show();
                if (start.compareTo("breakfast") == 0) {
                    startbreakfast(0);
                } else if (start.compareTo("lunch") == 0) {
                    startlunch(0);
                } else if (start.compareTo("cafe") == 0) {
                    startcafe(0);
                } else if (start.compareTo("dinner") == 0) {
                    startdinner(0);
                }
            } else {
//                Toast.makeText(MAP_Activity_Rest.this, "Place" + "   " + start, Toast.LENGTH_SHORT).show();
                if (next.compareTo("breakfast") == 0) {
                    if(place>0)
                    {
                        startbreakfast(1);
                    }
                    else
                    {
                        startbreakfast(0);
                    }
                } else if (next.compareTo("lunch") == 0) {
                    if(place>0)
                    {
                        startlunch(1);
                    }
                    else
                    {
                        startlunch(0);
                    }
                } else if (next.compareTo("cafe") == 0) {
                    if(place>0)
                    {
                        startcafe(1);
                    }
                    else
                    {
                        startcafe(0);
                    }
                } else if (next.compareTo("dinner") == 0) {
                    if(place>0)
                    {
                        startdinner(1);
                    }
                    else
                    {
                        startdinner(0);
                    }
                }
                else if(next.compareTo("place")==0)
                {
                    for(int i=0;i<placename.length;i++)
                    {
                        mname[i]=placename[i];
                        mcontact[i]=placecontact[i];
                        mlat[i]=placelat[i];
                        mlng[i]=placelng[i];
                        maddress[i]=placeaddress[i];
                        mdistance[i]=placedistance[i];
                        misopen[i]=placeisopen[i];
                        mopentill[i]=placeopentill[i];
                        mrating[i]=placerating[i];
                        mreview[i]=placereview[i];
                        mprice[i]=placeprice[i];
                        mchoice[i]=placechoice[i];
                    }
                }
            }

            if(finish)
            {
                drawonmap();
            }
            else
            {
                make_new_routes();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(MAP_Activity_Rest.this,"array-"+e.getMessage()+"\n"+e.getStackTrace()[0].getLineNumber(), Toast.LENGTH_SHORT).show();
        }
    }

    public void make_new_routes()
    {
        clear_defaults();
        if(r1==0)
        {
            r1=total_travel_distance();
            setupplaces(f1name, f1contact, f1lat, f1lng, f1address, f1distance, f1isopen, f1opentill, f1rating, f1review, f1price, f1choice);
        }
        else if(r2==0)
        {
            r2=total_travel_distance();
            setupplaces(f2name, f2contact, f2lat, f2lng, f2address, f2distance, f2isopen, f2opentill, f2rating, f2review, f2price, f2choice);
        }
        else if(r3==0)
        {
            r3=total_travel_distance();
            calc_routes();
            finish=true;
            df.cancel();
            callroute(Float.parseFloat(routes[0]));
        }

    }

    public void calc_routes()
    {
        float b;
        float c;
        if(r1>r2)
        {
            c=r1;
            b=r2;
        }
        else
        {
            c=r2;
            b=r1;
        }

        if(c>r3)
        {
            if(b>r3)
            {
                routes[0]=""+r3;
                routes[1]=""+b;
                routes[2]=""+c;
            }
            else
            {
                routes[0]=""+b;
                routes[1]=""+r3;
                routes[2]=""+c;
            }
        }
        else
        {
            routes[0]=""+b;
            routes[1]=""+c;
            routes[2]=""+r3;
        }
        totdistxt.setText("Total Travel Distance: "+routes[0]+" Km");
    }

    public void startbreakfast(int q)
    {
        for (int i = 0; i < mname.length; i++) {
            if (i == 0) {
                if(q==1)
                {
                    pc(i);
                    placeindex++;
                    place--;
                }
                else if(q==0) {
                    bf(i);
                }
            }
            else if(q==1 && breakfast==1)
            {
                bf(i);
                breakfast=0;
            }
            else if (lunch == 1 && place > 0) {
                pc(i);
                placeindex++;
                lunch = 2;
                place--;
            } else if ((lunch == 2) || (lunch==1 && place==0)) {
                lch(i);
                lunch = 0;
            } else if (cafe == 1 && place > 0) {
                pc(i);
                placeindex++;
                cafe = 2;
                place--;
            } else if (cafe == 2 && place > 0) {
                pc(i);
                placeindex++;
                cafe = 3;
                place--;
            } else if ((cafe == 3) || (cafe == 2 && place == 0) || (cafe == 1 && place == 0)) {
                cf(i);
                cafe = 0;
            } else if (dinner == 1 && place > 0) {
                pc(i);
                placeindex++;
                dinner = 2;
                place--;
            } else if ((dinner == 2) || (dinner==1 && place==0)) {
                dn(i);
                dinner = 0;
            } else {
                if (place > 0) {
                    pc(i);
                    placeindex++;
                    place--;
                }
            }
        }
    }

    public void startlunch(int q)
    {
        for (int i = 0; i < mname.length; i++) {
            if (i == 0) {
                if(q==1)
                {
                    pc(i);
                    placeindex++;
                    place--;
                }
                else if(q==0) {
                    lch(i);
                }
            }
            else if(q==1 && lunch==1)
            {
                lch(i);
                lunch=0;
            }
            else if (cafe == 1 && place > 0) {
                pc(i);
                placeindex++;
                cafe = 2;
                place--;
            } else if (cafe == 2 && place > 0) {
                pc(i);
                placeindex++;
                cafe = 3;
                place--;
            } else if ((cafe == 3) || (cafe == 2 && place == 0)|| (cafe == 1 && place == 0)) {
                cf(i);
                cafe = 0;
            } else if (dinner == 1 && place > 0) {
                pc(i);
                placeindex++;
                dinner = 2;
                place--;
            } else if ((dinner == 2) || (dinner==1 && place==0)) {
                dn(i);
                dinner = 0;
            } else if (breakfast == 1 && place > 0) {
                pc(i);
                placeindex++;
                breakfast = 2;
                place--;
            } else if (breakfast == 2 && place > 0) {
                pc(i);
                placeindex++;
                breakfast = 3;
                place--;
            } else if ((breakfast == 3) || (breakfast == 2 && place == 0)|| (breakfast == 1 && place == 0)) {
                bf(i);
                breakfast = 0;
            } else {
                if (place > 0) {
                    pc(i);
                    placeindex++;
                    place--;
                }
            }
        }
    }

    public void startcafe(int q)
    {
        for (int i = 0; i < mname.length; i++)
        {
            if (i == 0) {
                if(q==1)
                {
                    pc(i);
                    placeindex++;
                    place--;
                }
                else if(q==0) {
                    cf(i);
                }
            }
            else if(q==1 && cafe==1)
            {
                cf(i);
                cafe=0;
            }
            else if (dinner == 1 && place > 0)
            {
                pc(i);
                placeindex++;
                dinner = 2;
                place--;
            }
            else if ((dinner == 2) || (dinner==1 && place==0))
            {
                dn(i);
                dinner = 0;
            }
            else if (breakfast == 1 && place > 0)
            {
                pc(i);
                placeindex++;
                breakfast = 2;
                place--;
            }
            else if (breakfast == 2 && place > 0)
            {
                pc(i);
                placeindex++;
                breakfast = 3;
                place--;
            }
            else if ((breakfast == 3) || (breakfast == 2 && place == 0)|| (breakfast == 1 && place == 0)) {
                bf(i);
                breakfast = 0;
            }
            else if (lunch == 1 && place > 0)
            {
                pc(i);
                placeindex++;
                lunch = 2;
                place--;
            }
            else if ((lunch == 2) || (lunch==1 && place==0))
            {
                lch(i);
                lunch = 0;
            }
            else
            {
                if (place > 0)
                {
                    pc(i);
                    placeindex++;
                    place--;
                }
            }
        }
    }

    public void startdinner(int q)
    {
        for (int i = 0; i < mname.length; i++)
        {
            if (i == 0) {
                if(q==1)
                {
                    pc(i);
                    placeindex++;
                    place--;
                }
                else if(q==0)
                {
                    dn(i);
                }
            }
            else if(q==1 && dinner==1)
            {
                dn(i);
                dinner=0;
            }
            else if (breakfast == 1 && place > 0)
            {
                pc(i);
                placeindex++;
                breakfast = 2;
                place--;
            }
            else if (breakfast == 2 && place > 0)
            {
                pc(i);
                placeindex++;
                breakfast = 3;
                place--;
            }
            else if ((breakfast == 3) || (breakfast == 2 && place == 0)|| (breakfast == 1 && place == 0))
            {
                bf(i);
                breakfast = 0;
            }
            else if (lunch == 1 && place > 0)
            {
                pc(i);
                placeindex++;
                lunch = 2;
                place--;
            }
            else if ((lunch == 2) || (lunch==1 && place==0))
            {
                lch(i);
                lunch = 0;
            }
            else if (cafe == 1 && place > 0)
            {
                pc(i);
                placeindex++;
                cafe = 2;
                place--;
            }
            else if (cafe == 2 && place > 0)
            {
                pc(i);
                placeindex++;
                cafe = 3;
                place--;
            }
            else if ((cafe == 3) || (cafe == 2 && place == 0)|| (cafe == 1 && place == 0))
            {
                cf(i);
                cafe = 0;
            }
            else
            {
                if (place > 0)
                {
                    pc(i);
                    placeindex++;
                    place--;
                }
            }
        }
    }

    public void bf(int po)
    {
        mname[po]=breakname;
        mcontact[po]=breakcontact;
        mlat[po]=breaklat;
        mlng[po]=breaklng;
        maddress[po]=breakaddress;
        mdistance[po]=breakdistance;
        misopen[po]=breakisopen;
        mopentill[po]=breakopentill;
        mrating[po]=breakrating;
        mreview[po]=breakreview;
        mprice[po]=breakprice;
        mchoice[po]=breakchoice;
//        Toast.makeText(MAP_Activity_Rest.this,mname[po]+"-"+mchoice[po], Toast.LENGTH_SHORT).show();
    }

    public void lch(int po)
    {
        mname[po]=lunchname;
        mcontact[po]=lunchcontact;
        mlat[po]=lunchlat;
        mlng[po]=lunchlng;
        maddress[po]=lunchaddress;
        mdistance[po]=lunchdistance;
        misopen[po]=lunchisopen;
        mopentill[po]=lunchopentill;
        mrating[po]=lunchrating;
        mreview[po]=lunchreview;
        mprice[po]=lunchprice;
        mchoice[po]=lunchchoice;
//        Toast.makeText(MAP_Activity_Rest.this,mname[po]+"-"+mchoice[po], Toast.LENGTH_SHORT).show();
    }

    public void cf(int po)
    {
        mname[po]=cafename;
        mcontact[po]=cafecontact;
        mlat[po]=cafelat;
        mlng[po]=cafelng;
        maddress[po]=cafeaddress;
        mdistance[po]=cafedistance;
        misopen[po]=cafeisopen;
        mopentill[po]=cafeopentill;
        mrating[po]=caferating;
        mreview[po]=cafereview;
        mprice[po]=cafeprice;
        mchoice[po]=cafechoice;
//        Toast.makeText(MAP_Activity_Rest.this,mname[po]+"-"+mchoice[po], Toast.LENGTH_SHORT).show();
    }

    public void dn(int po)
    {
        mname[po]=dinnername;
        mcontact[po]=dinnercontact;
        mlat[po]=dinnerlat;
        mlng[po]=dinnerlng;
        maddress[po]=dinneraddress;
        mdistance[po]=dinnerdistance;
        misopen[po]=dinnerisopen;
        mopentill[po]=dinneropentill;
        mrating[po]=dinnerrating;
        mreview[po]=dinnerreview;
        mprice[po]=dinnerprice;
        mchoice[po]=dinnerchoice;
//        Toast.makeText(MAP_Activity_Rest.this,mname[po]+"-"+mchoice[po], Toast.LENGTH_SHORT).show();
    }

    public void pc(int po)
    {
        mname[po]=placename[placeindex];
        mcontact[po]=placecontact[placeindex];
        mlat[po]=placelat[placeindex];
        mlng[po]=placelng[placeindex];
        maddress[po]=placeaddress[placeindex];
        mdistance[po]=placedistance[placeindex];
        misopen[po]=placeisopen[placeindex];
        mopentill[po]=placeopentill[placeindex];
        mrating[po]=placerating[placeindex];
        mreview[po]=placereview[placeindex];
        mprice[po]=placeprice[placeindex];
        mchoice[po]=placechoice[placeindex];
//        Toast.makeText(MAP_Activity_Rest.this,mname[po]+"-"+mchoice[po], Toast.LENGTH_SHORT).show();
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

                if ((i + 1) < mname.length)
                {
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
            Toast.makeText(MAP_Activity_Rest.this, "Draw"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public float total_travel_distance()
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
        return Float.parseFloat(df.format(totdist));
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

    public class finalroute extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.addfinalroute(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15],params[16]);
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
            if(s.compareTo("true")!=0) {
                Toast.makeText(MAP_Activity_Rest.this, "ins----" + s, Toast.LENGTH_SHORT).show();
            }
            else
            {
                datasentcount++;
//                Toast.makeText(MAP_Activity_Rest.this,datasentcount+"-"+pos, Toast.LENGTH_SHORT).show();
                if(datasentcount==pos)
                {
                    df.cancel();
                    finish();
                }
            }
        }
    }

    public void finaldailog()
    {
        df=new Dialog(MAP_Activity_Rest.this);
        df.requestWindowFeature(Window.FEATURE_NO_TITLE);
        df.setContentView(R.layout.finaldialog);
        df.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00000000")));
        df.show();
    }

    public class getid extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.getplaceid();
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
//            Toast.makeText(MAP_Activity_Rest.this,s, Toast.LENGTH_SHORT).show();
            id=s;
            for(int h=0;h<mname.length;h++)
            {
//                Toast.makeText(MAP_Activity_Rest.this, mname[h]+"\n"+mcontact[h]+"\n"+mlat[h]+"\n"+mlng[h]+"\n"+ maddress[h]+"\n"+ mdistance[h]+"\n"+misopen[h]+"\n"+ mopentill[h]+"\n"+mrating[h]+"\n"+ mreview[h]+"\n"+ mprice[h]+"\n"+ mchoice[h], Toast.LENGTH_LONG).show();
                new finalroute().execute(id,uid,""+(h+1), mname[h], mcontact[h], mlat[h], mlng[h], maddress[h], mdistance[h], misopen[h], mopentill[h], mrating[h], mreview[h], mprice[h], mchoice[h],date,mylat+","+mylng);
            }
        }
    }

    public void markdialog(int i)
    {
        da=new Dialog(MAP_Activity_Rest.this);
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

        String s="<b>"+"Ratings: </b>"+mrating[i];
        String s1="<b>"+"Review: </b>"+mreview[i];

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
            new getid().execute();
        }
    }

    public void clear_defaults()
    {
        mMap.clear();
        txt.setText("1. Starting point");
        left.setVisibility(View.INVISIBLE);
        right.setVisibility(View.VISIBLE);
        breakfast=0;
        lunch=0;
        cafe=0;
        dinner=0;
        place=0;
        placeindex=0;
        pcount=-1;
    }

    public void rleft()
    {
        clear_defaults();
        route--;
        if(route==1)
        {
            callroute(Float.parseFloat(routes[0]));
            rleft.setVisibility(View.INVISIBLE);
            rtxt.setText("Route 1");
            totdistxt.setText("Total Travel Distance: "+routes[0]+" Km");
        }
        else if(route==2)
        {
            callroute(Float.parseFloat(routes[1]));
            rleft.setVisibility(View.VISIBLE);
            rright.setVisibility(View.VISIBLE);
            rtxt.setText("Route 2");
            totdistxt.setText("Total Travel Distance: "+routes[1]+" Km");
        }
    }

    public void rright()
    {
        clear_defaults();
        route++;
        if(route==2)
        {
            callroute(Float.parseFloat(routes[1]));
            rleft.setVisibility(View.VISIBLE);
            rright.setVisibility(View.VISIBLE);
            rtxt.setText("Route 2");
            totdistxt.setText("Total Travel Distance: "+routes[1]+" Km");
        }
        else if(route==3)
        {
            callroute(Float.parseFloat(routes[2]));
            rright.setVisibility(View.INVISIBLE);
            rtxt.setText("Route 3");
            totdistxt.setText("Total Travel Distance: "+routes[2]+" Km");
        }

    }

    public void callroute(float ans)
    {
        if(ans==r1)
        {
            totdistxt.setText("Total Travel Distance: "+ans+" Km");
            setupplaces(f0name, f0contact, f0lat, f0lng, f0address, f0distance, f0isopen, f0opentill, f0rating, f0review, f0price, f0choice);
        }
        else if(ans==r2)
        {
            setupplaces(f1name, f1contact, f1lat, f1lng, f1address, f1distance, f1isopen, f1opentill, f1rating, f1review, f1price, f1choice);
        }
        else if(ans==r3)
        {
            setupplaces(f2name, f2contact, f2lat, f2lng, f2address, f2distance, f2isopen, f2opentill, f2rating, f2review, f2price, f2choice);
        }
    }

}
