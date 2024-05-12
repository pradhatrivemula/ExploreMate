package com.example.hp.smart_city_traveller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hp on 8/9/2016.
 */
public class Planning_Rest extends AppCompatActivity
{
    ArrayList<String> placeindex;
    String place_selection_by="";
    int place_array_count;
    int[] ffirst,fsecond,fthird;
    int fcount=0;
    TextView sel_place,wait;
    Boolean first=true;
    int p = 0;
    String[] dbarea,dblat,dblng,dist;
    String fromtime,totime,uid;
    double mylat,mylng;
    int totmin=0;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String Ans1,Ans2,Ans3;
    float Distance;
    int t1hr,t2hr,t1min,t2min;
    String request="";
    String idpass="https://api.foursquare.com/v2/venues/explore?client_id=TDQG4LF1M1O5SZESHJ42BRSZQDPJY4B53NQN5RDTRRZZPNYM&client_secret=PGLQ4RV5R0FDIPHAPJO4RT3KJXO02ARZ4LQ3NWS3TPFIUUJ0&v=20130815";
    String sortby="&sortByDistance=1";
    String limit="&limit=10";
    String start="na",next="na";
    String[] name,contact,lat,lng,address,distance,isopen,opentill,rating,review,price;
    int add=1000;String addr;
    int inscount;
    RelativeLayout ray;
    TableRow tbprog,tbbtn,tbhead,tbwait,tblist;

    String sdlat,sdlng;
    ListView plist;
    ArrayList<String> plist_item;

    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler();
    Dialog da,ps;
    Place_Adapter pa;

    String[] pname,pcontact,plat,plng,paddress,pdistance,pisopen,popentill,prating,preview,pprice;
    String[] fname,fcontact,flat,flng,faddress,fdistance,fisopen,fopentill,frating,freview,fprice,fchoice;
    String[] f1name,f1contact,f1lat,f1lng,f1address,f1distance,f1isopen,f1opentill,f1rating,f1review,f1price,f1choice;
    String[] f2name,f2contact,f2lat,f2lng,f2address,f2distance,f2isopen,f2opentill,f2rating,f2review,f2price,f2choice;
    String breakfast="na",lunch="na",cafe="na",dinner="na";

    String[] uname,ucontact,ulat,ulng,uaddress,udistance,uisopen,uopentill,urating,ureview,uprice,uchoice;

    int counter=0;
    String choice="",Pricerange="";
    Button confirm,cancel;
    int pcount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_rest);
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        tbwait= (TableRow) findViewById(R.id.tbwait);
        tbprog= (TableRow) findViewById(R.id.tbprog);
        tbhead= (TableRow) findViewById(R.id.tbhead);
        tbwait.setVisibility(View.GONE);
        tbprog.setVisibility(View.GONE);
        tbbtn= (TableRow) findViewById(R.id.tbbtn);
        tblist= (TableRow) findViewById(R.id.tblist);
        ray= (RelativeLayout) findViewById(R.id.ray);
        confirm= (Button) findViewById(R.id.pconfirm);
        cancel= (Button) findViewById(R.id.pcancel);
        plist= (ListView) findViewById(R.id.plist);
        plist_item=new ArrayList<String>();
        sel_place= (TextView) findViewById(R.id.sel_place);
        wait= (TextView) findViewById(R.id.waittxt);
        placeindex=new ArrayList<String>();
        getdata();  // GET DEFAULT PLACES
        Intent i=getIntent();
        fromtime=i.getStringExtra("fromtime");
        totime=i.getStringExtra("totime");
        sp=getSharedPreferences("travel", Context.MODE_PRIVATE);
        uid=sp.getString("userid", "");
        mylat=Double.parseDouble(sp.getString(uid+"lat", "0.0"));
        mylng=Double.parseDouble(sp.getString(uid+"lng", "0.0"));

        Pricerange="&price="+sp.getString("price","");
        select_start_position();
        checkdefultdist();  //CHECK DIST BETWN MYLOC TO DEFAULT PLACES & SORT BY DIST
        fname=new String[20];
        fcontact=new String[20];
        flat=new String[20];
        flng=new String[20];
        faddress=new String[20];
        fdistance=new String[20];
        fisopen=new String[20];
        fopentill=new String[20];
        frating=new String[20];
        freview=new String[20];
        fprice=new String[20];
        fchoice=new String[20];

        f1name=new String[20];
        f1contact=new String[20];
        f1lat=new String[20];
        f1lng=new String[20];
        f1address=new String[20];
        f1distance=new String[20];
        f1isopen=new String[20];
        f1opentill=new String[20];
        f1rating=new String[20];
        f1review=new String[20];
        f1price=new String[20];
        f1choice=new String[20];

        f2name=new String[20];
        f2contact=new String[20];
        f2lat=new String[20];
        f2lng=new String[20];
        f2address=new String[20];
        f2distance=new String[20];
        f2isopen=new String[20];
        f2opentill=new String[20];
        f2rating=new String[20];
        f2review=new String[20];
        f2price=new String[20];
        f2choice=new String[20];

        pname=new String[40];
        pcontact=new String[40];
        plat=new String[40];
        plng=new String[40];
        paddress=new String[40];
        pdistance=new String[40];
        pisopen=new String[40];
        popentill=new String[40];
        prating=new String[40];
        preview=new String[40];
        pprice=new String[40];

        uname=new String[20];
        ucontact=new String[20];
        ulat=new String[20];
        ulng=new String[20];
        uaddress=new String[20];
        udistance=new String[20];
        uisopen=new String[20];
        uopentill=new String[20];
        urating=new String[20];
        ureview=new String[20];
        uprice=new String[20];
        uchoice=new String[20];
        ps_Dailog();
//        new getcustinfo().execute(uid);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Planning_Rest.this, MAP_Activity_Rest_User.class);
                i.putExtra("fname", fname);
                i.putExtra("fcontact", fcontact);
                i.putExtra("flat", flat);
                i.putExtra("flng", flng);
                i.putExtra("faddress", faddress);
                i.putExtra("fdistance", fdistance);
                i.putExtra("fisopen", fisopen);
                i.putExtra("fopentill", fopentill);
                i.putExtra("frating", frating);
                i.putExtra("freview", freview);
                i.putExtra("fprice", fprice);
                i.putExtra("fchoice", fchoice);

                i.putExtra("start", start);
                i.putExtra("next", next);
                i.putExtra("totime", totime);
                startActivity(i);
                finish();
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }

    public void getdata()
    {
        try {
            DB db = new DB(Planning_Rest.this);
            db.open();
            String s = db.getdata();
            db.close();
            String temp[] = s.split("\\#");
            dbarea = new String[temp.length];
            dblat = new String[temp.length];
            dblng = new String[temp.length];
            for (int i = 0; i < temp.length; i++) {
                String temp1[] = temp[i].split("\\*");
                dbarea[i] = temp1[0];
                dblat[i] = temp1[1];
                dblng[i] = temp1[2];

            }
        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Get data-"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void calculatetime()
    {
        try
        {
        String time1[] = fromtime.split(":");
        t1hr = Integer.parseInt(time1[0]);
        t1min = Integer.parseInt(time1[1]);

        String time2[] = totime.split(":");
        t2hr = Integer.parseInt(time2[0]);
        t2min = Integer.parseInt(time2[1]);

        int finalhrs=0,finalmins=0;

        if(t2hr>t1hr)
        {
            finalhrs=t2hr-t1hr;

            if(t1min==t2min)
            {
                finalmins=0;
            }
            else if(t2min>t1min)
            {
                finalmins=t2min-t1min;
            }
            else if(t1min>t2min)
            {
                finalhrs-=1;
                finalmins=60-(t1min-t2min);
            }
        }
        else if(t1hr>t2hr)
        {
//            finalhrs=23-t1hr;
//            if(t2hr==0)
//            {
//                finalhrs+=1;
//            }
//            else
//            {
//                finalhrs+=(t2hr+1);
//            }

            finalhrs=24-(t1hr-t2hr);

            if(t1min==t2min)
            {
                finalmins=0;
            }
            else if(t2min>t1min)
            {
                finalmins=t2min-t1min;
            }
            else if(t1min>t2min)
            {
                finalhrs-=1;
                finalmins=60-(t1min-t2min);
            }

        }
        totmin=(finalhrs*60)+finalmins;
//        Toast.makeText(Planning_Rest.this,+totmin+"\n"+finalhrs+":"+finalmins, Toast.LENGTH_SHORT).show();

        select_destination(); //SELECT A DESTINATION
        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Calc Mins-"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public class getcustinfo extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.getAnsInfo(params[0]);
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
                String temp[]=s.split("\\*");
                Ans1=temp[0];
                Ans2=temp[1];
                Ans3=temp[2];
//                Toast.makeText(Planning_Rest.this,Ans1, Toast.LENGTH_SHORT).show();
            }
            calculatetime();    //CALCULATE TIME IN MINUTES
        }
    }

    public void checkdefultdist()
    {
        try {
            Location myloc = new Location("");
            myloc.setLatitude(mylat);
            myloc.setLongitude(mylng);

            for (int i = 0; i < dbarea.length; i++) {

                Location loc = new Location("");
                loc.setLatitude(Double.parseDouble(dblat[i]));
                loc.setLongitude(Double.parseDouble(dblng[i]));

                float diff = myloc.distanceTo(loc);
                diff = diff / 1000;
                String temp = "" + diff;
                String temp1[] = temp.split("\\.");

                if (temp1.length > 1) {
                    int c = temp1[1].length();
                    c = c - 1;

                    String temp4 = temp1[1].substring(0, 1);
                    String temp3 = temp1[0] + "." + temp4;
                    Distance = Float.parseFloat(temp3);
                } else {
                    Distance = Float.parseFloat(temp);
                }

                DB db = new DB(Planning_Rest.this);
                db.open();
                db.inserttemp(Distance, dblat[i], dblng[i], dbarea[i]);
                db.close();


            }
            DB db = new DB(Planning_Rest.this);
            db.open();
            String s = db.gettempdata();
            db.deletetemp();
            db.close();

            String temp[] = s.split("\\#");
            dist = new String[temp.length];
            dblng = new String[temp.length];
            dblat = new String[temp.length];
            dbarea = new String[temp.length];
            for (int i = 0; i < temp.length; i++) {
                String temp1[] = temp[i].split("\\*");
                dist[i] = temp1[0];
                dblat[i] = temp1[1];
                dblng[i] = temp1[2];
                dbarea[i] = temp1[3];
//            Toast.makeText(Planning_Rest.this,dist[i]+"="+dbarea[i], Toast.LENGTH_SHORT).show();

            }
        }
         catch (Exception e)
         {
             Toast.makeText(Planning_Rest.this, "defaultdist-"+e.getMessage(), Toast.LENGTH_SHORT).show();
         }
    }

    public void select_destination()
    {
        try {
            for (int i = 0; i < dbarea.length; i++) {
                float f = Float.parseFloat(dist[i]) * 3;
                int mins = Math.round(f);

                int partialmin = totmin - (mins * 2);
                if (partialmin <= (60 * (i + 1))) {
                    if (i == 0)
                    {
//                        Toast.makeText(Planning_Rest.this, "Cannot Calculate,time is too short", Toast.LENGTH_SHORT).show();
                        tbprog.setVisibility(View.GONE);
                        tbwait.setVisibility(View.GONE);
                    } else {
                        sdlat = dblat[i - 1];
                        sdlng = dblng[i - 1];
                        String s="<b>"+"Selected Place : "+"</b>"+dbarea[i-1];
                        if(place_selection_by.compareTo("user")!=0) {
                            tbhead.setVisibility(View.VISIBLE);
                        }
                        sel_place.setText(Html.fromHtml(s));
                        select_places();

                        break;

                    }
                } else if (partialmin > (60 * (i + 1))) {
                    if (i == dbarea.length - 1) {
                        sdlat = dblat[i];
                        sdlng = dblng[i];
                        String s="<b>"+"Selected Place : "+"</b>"+dbarea[i];
                        if(place_selection_by.compareTo("user")!=0) {
                            tbhead.setVisibility(View.VISIBLE);
                        }
                        sel_place.setText(Html.fromHtml(s));
                        select_places();
//                        Toast.makeText(Planning_Rest.this, dbarea[i] + "::" + "Selected-" + dbarea[i], Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Select Destination"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void select_places()
    {
        if(totmin>=40)
        {
            if (counter == 0) {
                if (breakfast.compareTo("na") == 0) {
                    breakfast();
                }
            } else if (counter == 1) {
                if (lunch.compareTo("na") == 0) {
                    lunch();
                }
            }
            else if (counter == 2) {
                if (cafe.compareTo("na") == 0) {
                    evening_cafe();
                }
            }
            else if (counter == 3) {
                if (dinner.compareTo("na") == 0) {
                    dinner();
                }
            }
            else
            {
                places1();
            }
        }
        else
        {
            tbwait.setVisibility(View.GONE);
//            tbbtn.setVisibility(View.VISIBLE);
            tbprog.setVisibility(View.GONE);
            tblist.setVisibility(View.VISIBLE);
            procced_to_map();
        }

    }

    public void breakfast()
    {
        try {
            counter++;
            choice = "Breakfast";
//        Toast.makeText(Planning_Rest.this,"###Breakfast###", Toast.LENGTH_SHORT).show();

                if((t1hr>=00 && t1hr<=4) &&(t2hr>=00 && t2hr<=4))
                {
                    breakfast = "no";
                    select_places();
                }
                else
                {
                    if (t1hr >= 6 && t1hr <= 10) {
                        wait.setText("Please Wait....Searching Places for Breakfast!!");
                        breakfast = "yes";
                        String ll = "&ll=" + sdlat + "," + sdlng;
                        String query = "";
                        if (Ans1.compareTo("veg") == 0) {
                            query = "&query=" + Ans1 + ",breakfast";
                        } else {
                            query = "&query=breakfast";
                        }

                        request = idpass + ll + query + sortby + limit+Pricerange;
                        calculate_place(request);
                    } else {
                        breakfast = "no";
                        select_places();
                    }
                }

        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Breakfast"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void lunch()
    {
        try {
            counter++;
            choice = "Lunch";
//        Toast.makeText(Planning_Rest.this,"###Lunch###", Toast.LENGTH_SHORT).show();

            if((t1hr>=00 && t1hr<=4) &&(t2hr>=00 && t2hr<=4))
            {
                lunch = "no";
                select_places();
            }
            else {
                if (((t2hr >= 13 || t2hr >= 14) || (t2hr == 00 || t2hr == 1 || t2hr == 2 || t2hr == 3 || t2hr == 4)) && (t1hr <= 13 || t1hr <= 14)) {
                    wait.setText("Please Wait....Searching Places for Lunch!!");
                    lunch = "yes";
                    String ll = "&ll=" + sdlat + "," + sdlng;
                    String query = "";
                    if (Ans1.compareTo("veg") == 0) {
                        query = "&query=" + Ans1 + ",lunch";
                    } else {
                        query = "&query=nonveg";
                    }
                    request = idpass + ll + query + sortby + limit + Pricerange;
//                    new placetask().execute(request);
                    calculate_place(request);
                } else {
                    lunch = "no";
//                    Toast.makeText(Planning_Rest.this, "Not Lunch time", Toast.LENGTH_SHORT).show();
                    select_places();
                }
            }

        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Lunch"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void evening_cafe()
    {
        try {
        counter++;
        choice="Evening Snacks";
//        Toast.makeText(Planning_Rest.this,"###Lunch###", Toast.LENGTH_SHORT).show();

            if((t1hr>=00 && t1hr<=4) &&(t2hr>=00 && t2hr<=4))
            {
                cafe = "no";
                select_places();
            }
            else {
                if (((t2hr >= 17 || t2hr >= 18)) || (t2hr == 00 || t2hr == 1 || t2hr == 2 || t2hr == 3 || t2hr == 4) && (t1hr <= 17 || t1hr <= 18)) {
                    wait.setText("Please Wait....Searching Places for Evening Snacks!!");
                    cafe = "yes";
                    String ll = "&ll=" + sdlat + "," + sdlng;
                    String query = "";
                    query = "&query=cafe";
                    request = idpass + ll + query + sortby + limit + Pricerange;
//            new placetask().execute(request);
                    calculate_place(request);
                } else {
                    cafe = "no";
//            Toast.makeText(Planning_Rest.this,"Not Cafe time", Toast.LENGTH_SHORT).show();
                    select_places();
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Evening Cafe"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void dinner()
    {
        try {
        counter++;
        choice="Dinner";
//        Toast.makeText(Planning_Rest.this,"###Lunch###", Toast.LENGTH_SHORT).show();
            if((t1hr>=00 && t1hr<=4) &&(t2hr>=00 && t2hr<=4))
            {
                dinner = "no";
                select_places();
            }
            else {
                if(((t2hr>=21 || t2hr>=22))||(t2hr==00||t2hr==1||t2hr==2||t2hr==3||t2hr==4)&& (t1hr<=21 || t1hr<=22))
                {
                    wait.setText("Please Wait....Searching Places for Dinner!!");
                    dinner="yes";
                    String ll="&ll="+sdlat+","+sdlng;
                    String query="";
                    if(Ans1.compareTo("veg")==0 && Ans2.compareTo("no")==0)
                    {
                        query = "&query=" + Ans1+",dinner";
                    }
                    else if(Ans1.compareTo("veg")==0 && Ans2.compareTo("yes")==0)
                    {
                        query = "&query=" + Ans1+",bar";
                    }
                    else if(Ans1.compareTo("nonveg")==0 && Ans2.compareTo("yes")==0)
                    {
                        query = "&query=nonveg,bar";
                    }
                    else if(Ans1.compareTo("nonveg")==0 && Ans2.compareTo("no")==0)
                    {
                        query = "&query=buffet";
                    }
                    request=idpass+ll+query+sortby+limit+ Pricerange;
        //            new placetask().execute(request);
                    calculate_place(request);
                }
                else
                {
                    dinner="no";
        //            Toast.makeText(Planning_Rest.this,"Not Dinner time", Toast.LENGTH_SHORT).show();
                    select_places();
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Dinner"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void places1()
    {
        wait.setText("Please Wait....Searching Places for You to Visit!!");
        counter++;
        choice="places1";
        String ll="&ll="+sdlat+","+sdlng;
        String query="&query=sightseing";
        request=idpass+ll+query+sortby+"&limit=20";
//        new placetask().execute(request);
        calculate_place(request);

    }

    public void places()
    {
        choice="places";
        String ll="&ll="+sdlat+","+sdlng;
        String query="&query=park";
        request=idpass+ll+query+sortby+"&limit=20";
//        new placetask().execute(request);
        calculate_place(request);

    }

    public void calculate_place(String furl)
    {
        JsonObjectRequest jor=new JsonObjectRequest(furl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main=response;
                    JSONObject res=main.getJSONObject("response");
                    JSONArray groups=res.getJSONArray("groups");
                    JSONObject sub_groups = groups.getJSONObject(0);
                    JSONArray items=sub_groups.getJSONArray("items");

                    name = new String[items.length()];
                    contact = new String[items.length()];
                    lat = new String[items.length()];
                    lng = new String[items.length()];
                    address = new String[items.length()];
                    distance = new String[items.length()];
                    opentill = new String[items.length()];
                    isopen = new String[items.length()];
                    rating = new String[items.length()];
                    review = new String[items.length()];
                    price = new String[items.length()];

                    if(items.length()==0)
                    {
                        tbwait.setVisibility(View.GONE);
                        tbprog.setVisibility(View.GONE);
                        Snackbar snack=Snackbar.make(ray,"No Places Available!!",Snackbar.LENGTH_LONG);
                        View v=snack.getView();
                        TextView txt= (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                        txt.setTextColor(Color.RED);
                        snack.show();
                    }
                    else
                    {
//                        Toast.makeText(Planning_Rest.this, choice+"="+items.length(), Toast.LENGTH_SHORT).show();
                        Parse_JSON(items);
                    }
                } catch (JSONException e) {
                    Toast.makeText(Planning_Rest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Planning_Rest.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Planning_Rest.this);
        requestQueue.add(jor);
    }

    public void Parse_JSON(JSONArray items)
    {
        try
        {
            for(int i=0;i<items.length();i++)
            {
                JSONObject items_=items.getJSONObject(i);
                JSONObject venue=items_.getJSONObject("venue");
                //name
                name[i]=venue.getString("name");

                //contact
                try {
                    JSONObject cont = venue.getJSONObject("contact");
                    if (cont.length() > 0) {
                        if (cont.toString().contains("formattedPhone")) {
                            contact[i]=cont.getString("formattedPhone");
                        } else {
                            contact[i]="na";
                        }
                    } else {
                        contact[i]="na";
                    }
                }catch (Exception e)
                {
                    contact[i]="na";
                }


                //price status
                try
                {
                    JSONObject price_=venue.getJSONObject("price");
                    if(price_.length()>0)
                    {
                        if(price_.toString().contains("message"))
                        {
                            price[i]=price_.getString("message");
                        }
                        else
                        {
                            price[i]="na";
                        }
                    }
                    else
                    {
                        price[i]="na";
                    }
                }
                catch (Exception e){
                    price[i]="na";
                }


                //is open
                try
                {
                    JSONObject hours=venue.getJSONObject("hours");
                    if(hours.length()>0)
                    {
                        if(hours.toString().contains("isOpen"))
                        {
                            isopen[i]=hours.getString("isOpen");
                        }
                        else
                        {
                            isopen[i]="na";
                        }
                    }
                    else
                    {
                        isopen[i]="na";
                    }
                }
                catch (Exception e){
                    isopen[i]="na";
                }

                //opentill
                try
                {
                    JSONObject hours=venue.getJSONObject("hours");
                    if(hours.length()>0)
                    {
                        if(hours.toString().contains("status"))
                        {
                            opentill[i]=hours.getString("status");
                        }
                        else
                        {
                            opentill[i]="na";
                        }
                    }
                    else
                    {
                        opentill[i]="na";
                    }
                }
                catch (Exception e){
                    opentill[i]="na";
                }

                //rating
                try{
                    rating[i]=venue.getString("rating");
                }
                catch (Exception e){
                    rating[i]="0";
                }

                //location lat lng
                JSONObject loc=venue.getJSONObject("location");
                lat[i]=loc.getString("lat");
                lng[i]=loc.getString("lng");

                //dist
                distance[i]=loc.getString("distance");

                //address
                try {
                    JSONArray formatadd = loc.getJSONArray("formattedAddress");
                    String partial_Add = "";
                    for (int a = 0; a < formatadd.length(); a++) {
                        partial_Add += formatadd.getString(a)+",";
                    }
                    if(partial_Add.compareTo("")!=0 && partial_Add.contains(","))
                    {
                        partial_Add=partial_Add.substring(0,partial_Add.length()-1);
                    }
                    address[i]=partial_Add;
                }
                catch (Exception e)
                {
                    address[i]="na";
                }

                //review
                try {
                    JSONArray tips = items_.getJSONArray("tips");
                    String r="";
                    for(int t=0;t<tips.length();t++)
                    {
                        JSONObject tips_=tips.getJSONObject(t);
                        r+=tips_.getString("text");
                    }
                    review[i]=r;
                }catch (Exception e)
                {
                    review[i]="na";
                }

                String a=name[i]+"\n";
                a+=contact[i]+"\n";
                a+=lat[i]+"\n";
                a+=lng[i]+"\n";
                a+=address[i]+"\n";
                a+=distance[i]+"\n";
                a+=isopen[i]+"\n";
                a+=opentill[i]+"\n";
                a+=rating[i]+"\n";
                a+=review[i]+"\n";
                a+=price[i];

            }

            SETNA();

        } catch (JSONException e) {
            Toast.makeText(Planning_Rest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void SETNA()
    {
        try
        {
        inscount=0;
        for(int x=1;x<name.length;x++)
        {

            if(name[x]==null)
            {
                name[x]="na";
            }
            name[x]=name[x].replaceAll("\"","");
            name[x]=name[x].replaceAll("\'","");

            if(contact[x]==null)
            {
                contact[x]="na";
            }
            contact[x]=contact[x].replaceAll("\"","");
            contact[x]=contact[x].replaceAll("\'","");

            if(lat[x]==null)
            {
                lat[x]="na";
            }
            lat[x]=lat[x].replaceAll("\"","");
            lat[x]=lat[x].replaceAll("\'","");

            if(lng[x]==null)
            {
                lng[x]="na";
            }
            lng[x]=lng[x].replaceAll("\"","");
            lng[x]=lng[x].replaceAll("\'","");

            if(address[x]==null)
            {
                address[x]="na";
            }
            address[x]=address[x].replaceAll("\"","");
            address[x]=address[x].replaceAll("\'","");

            if(distance[x]==null)
            {
                distance[x]="0";
            }
            distance[x]=distance[x].replaceAll("\"","");
            distance[x]=distance[x].replaceAll("\'","");

            if(isopen[x]==null)
            {
                isopen[x]="na";
            }
            isopen[x]=isopen[x].replaceAll("\"","");
            isopen[x]=isopen[x].replaceAll("\'","");

            if(opentill[x]==null)
            {
                opentill[x]="na";
            }
            opentill[x]=opentill[x].replaceAll("\"","");
            opentill[x]=opentill[x].replaceAll("\'","");

            if(rating[x]==null)
            {
                rating[x]="0";
            }
            rating[x]=rating[x].replaceAll("\"","");
            rating[x]=rating[x].replaceAll("\'","");

            if(review[x]==null)
            {
                review[x]="na";
            }
            review[x]=review[x].replaceAll("\"","");
            review[x]=review[x].replaceAll("\'","");

            if(price[x]==null)
            {
                price[x]="na";
            }
            price[x]=price[x].replaceAll("\"","");
            price[x]=price[x].replaceAll("\'","");

//            Toast.makeText(Planning_Rest.this,name[x]+"\n"+contact[x]+"\n"+lat[x]+"\n"+lng[x]+"\n"+distance[x]+"\n"+isopen[x]+"\n"+opentill[x]+"\n"+rating[x]+"\n"+review[x]+"\n"+price[x], Toast.LENGTH_SHORT).show();

            DB db=new DB(Planning_Rest.this);
            db.open();
//            Toast.makeText(this, (x)+". "+name[x], Toast.LENGTH_SHORT).show();
            db.addtodb(name[x], contact[x], lat[x], lng[x], address[x], distance[x], isopen[x], opentill[x], rating[x], review[x], price[x]);
            db.close();
        }
        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Data In"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try {
        String s="";
        DB db=new DB(Planning_Rest.this);
        db.open();
        if(choice.compareTo("places1")==0)
        {
            s = db.getdbdata("20");
            db.close();
            String temp[]=s.split("\\*\\#");
//            Toast.makeText(Planning_Rest.this, ""+temp.length, Toast.LENGTH_SHORT).show();

            for(int i=0;i<temp.length;i++)
            {
                String temp1[] = temp[i].split("\\*");
                pname[i] = temp1[0];
                pcontact[i] = temp1[1];
                plat[i] = temp1[2];
                plng[i] = temp1[3];
                paddress[i] = temp1[4];
                pdistance[i] = temp1[5];
                pisopen[i] = temp1[6];
                popentill[i] = temp1[7];
                prating[i] = temp1[8];
                preview[i] = temp1[9];
                pprice[i] = temp1[10];

            }
            places();

        }
        else if(choice.compareTo("places")==0)
        {
            s = db.getdbdata("20");
            db.close();
            String temp[]=s.split("\\*\\#");
//            Toast.makeText(Planning_Rest.this, ""+temp.length, Toast.LENGTH_SHORT).show();
            int c=0;
            for(int j=0;j<pname.length;j++)
            {
                if(pname[j]==null)
                {
                    c=j;
//                    Toast.makeText(Planning_Rest.this, ""+c, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            int in=0;
            for(int i=c;i<(c+temp.length);i++)
            {

//                    Toast.makeText(Planning_Rest.this, i+"-" + in, Toast.LENGTH_SHORT).show();
                    String temp1[] = temp[in].split("\\*");
                    pname[i] = temp1[0];
                    pcontact[i] = temp1[1];
                    plat[i] = temp1[2];
                    plng[i] = temp1[3];
                    paddress[i] = temp1[4];
                    pdistance[i] = temp1[5];
                    pisopen[i] = temp1[6];
                    popentill[i] = temp1[7];
                    prating[i] = temp1[8];
                    preview[i] = temp1[9];
                    pprice[i] = temp1[10];
                    in++;
//                    Toast.makeText(Planning_Rest.this, "" + pname[i], Toast.LENGTH_SHORT).show();

            }

            place_array_count=(c+temp.length);
            //IF PLACE ARRAY IS OCCUPIED,RANDOMLY SELECT FROM OTHERS;
            if(((c+temp.length)%3)==0)
            {
                int tot=(c+temp.length)/3;
//                Toast.makeText(Planning_Rest.this, "array tot"+tot, Toast.LENGTH_SHORT).show();
                ffirst=new int[tot];
                fsecond=new int[tot];
                fthird=new int[tot];
            }
            else
            {
                int tot=((c+temp.length)/3)+1;
//                Toast.makeText(Planning_Rest.this, "array tot"+tot, Toast.LENGTH_SHORT).show();
                ffirst=new int[tot];
                fsecond=new int[tot];
                fthird=new int[tot];
            }
            int h=0;
            int f1c=0,f2c=0,f3c=0;
            for(int g=0;g<(c+temp.length);g++)
            {
                ++h;
                if(h==1)
                {
                    ffirst[f1c]=g;
//                    Toast.makeText(Planning_Rest.this, "h-"+h+"\n"+"f1-"+f1c+"\n"+"first-"+ffirst[f1c], Toast.LENGTH_SHORT).show();
                    f1c++;
                }
                else if(h==2)
                {
                    fsecond[f2c]=g;
//                    Toast.makeText(Planning_Rest.this, "h-"+h+"\n"+"f2-"+f2c+"\n"+"second-"+fsecond[f2c], Toast.LENGTH_SHORT).show();
                    f2c++;
                }
                else if(h==3)
                {
                    fthird[f3c]=g;
//                    Toast.makeText(Planning_Rest.this, "h-"+h+"\n"+"f3-"+f3c+"\n"+"third-"+fthird[f3c], Toast.LENGTH_SHORT).show();
                    f3c++;
                    h=0;
                }
            }
            dialog("Places");
        }
        else
        {
            s = db.getdbdata("3");
            db.close();
            String temp[]=s.split("\\*\\#");
            name = new String[temp.length];
            contact = new String[temp.length];
            lat = new String[temp.length];
            lng = new String[temp.length];
            address = new String[temp.length];
            distance = new String[temp.length];
            isopen = new String[temp.length];
            opentill = new String[temp.length];
            rating = new String[temp.length];
            review = new String[temp.length];
            price = new String[temp.length];

            for(int i=0;i<temp.length;i++)
            {
                String temp1[] = temp[i].split("\\*");
                name[i] = temp1[0];
//                Toast.makeText(this, (i+1)+". "+name[i], Toast.LENGTH_SHORT).show();
                contact[i] = temp1[1];
                lat[i] = temp1[2];
                lng[i] = temp1[3];
                address[i] = temp1[4];
                distance[i] = temp1[5];
                isopen[i] = temp1[6];
                opentill[i] = temp1[7];
                rating[i] = temp1[8];
                review[i] = temp1[9];
                price[i] = temp1[10];

                if(i==temp.length-1)
                {
                    if (temp.length > 1)
                    {
//                        Toast.makeText(this, "dailog", Toast.LENGTH_SHORT).show();
                        dialog("Breakfast");
                    }
                    else if (temp.length == 1)
                    {
//                        Toast.makeText(this, "list", Toast.LENGTH_SHORT).show();
                        fill_list(0,true);
                    }
                }

            }
        }

        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Data Out"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean already_added_indexes(String s)
    {
        Boolean ans=false;
        for(int i=0;i<plist_item.size();i++)
        {
            if(plist_item.get(i).toString().compareTo(s)==0)
            {
                ans=true;
            }
        }
        return ans;
    }

    public int getplace_index(String place)
    {
        int ans=0;
        for(int i=0;i<pname.length;i++)
        {
            if(pname[i]!=null)
            {
                if(pname[i].compareTo(place)==0)
                {
                    ans=i;
                }
            }
        }
        return ans;
    }

    public void dialog(String s)
    {
        try
        {

        if (choice.compareTo("places") != 0)
        {

            if(place_selection_by.compareTo("user")==0)
            {
                da = new Dialog(Planning_Rest.this);
                da.requestWindowFeature(Window.FEATURE_NO_TITLE);
                da.setContentView(R.layout.place_dialog);
                da.setCancelable(false);

                TextView dhead = (TextView) da.findViewById(R.id.dhead);
                List<String> ld = new ArrayList<String>();
                dhead.setText(choice + " Choices");

                for (int i = 0; i < name.length; i++) {
                    ld.add(name[i] + "\n" + "Rating:" + rating[i]);
                }

                ArrayAdapter<String> dadapt = new ArrayAdapter<String>(Planning_Rest.this, R.layout.planning_rest_listitem, R.id.plist_text, ld);
                ListView dlist = (ListView) da.findViewById(R.id.dlist);
                dlist.setAdapter(dadapt);

                dlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        da.cancel();
                        plist_item.add(name[position]);
                        ArrayAdapter<String> padapt = new ArrayAdapter<String>(Planning_Rest.this, R.layout.planning_rest_listitem, R.id.plist_text, plist_item);
                        plist.setAdapter(padapt);
                        calculatemins(position);
                    }
                });
                da.show();
            }
            else
            {
                calculatemins(0);
            }


        }
        else
        {
            if(place_selection_by.compareTo("user")==0)
            {
                da = new Dialog(Planning_Rest.this);
                da.requestWindowFeature(Window.FEATURE_NO_TITLE);
                da.setContentView(R.layout.place_dialog);
                da.setCancelable(false);

                TextView dhead = (TextView) da.findViewById(R.id.dhead);
                List<String> ld = new ArrayList<String>();
                placeindex=new ArrayList<String>();
                dhead.  setText(choice + " Choices");
                for (int i = 0; i < pname.length; i++)
                {
                    if(pname[i]!=null)
                    {
                        if(!already_added_indexes(pname[i]))
                        {
                            ld.add(pname[i] + "\n" + "Rating:" + prating[i]);
                            placeindex.add(pname[i]);
                        }
                    }
                }

                ArrayAdapter<String> dadapt = new ArrayAdapter<String>(Planning_Rest.this, R.layout.planning_rest_listitem, R.id.plist_text, ld);
                ListView dlist = (ListView) da.findViewById(R.id.dlist);
                dlist.setAdapter(dadapt);

                dlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        da.cancel();
                        int j=getplace_index(placeindex.get(position));
                        plist_item.add(pname[j]);
                        ArrayAdapter<String> padapt = new ArrayAdapter<String>(Planning_Rest.this, R.layout.planning_rest_listitem, R.id.plist_text, plist_item);
                        plist.setAdapter(padapt);
                        calculatemins(j);
                    }
                });

                if(placeindex.size()>0) {
                    da.show();
                }
                else
                {
                    tbwait.setVisibility(View.GONE);
                    tbprog.setVisibility(View.GONE);
//                    tbbtn.setVisibility(View.VISIBLE);
                    tblist.setVisibility(View.VISIBLE);
                    procced_to_map();
                }
            }
            else
            {


//            Toast.makeText(Planning_Rest.this, ""+pcount, Toast.LENGTH_SHORT).show();
                if (pcount < pname.length)
                {
                    if (pname[pcount] != null)
                    {
                        calculatemins(pcount);
                    }
                    else
                    {
                        tbwait.setVisibility(View.GONE);
                        tbprog.setVisibility(View.GONE);
//                    tbbtn.setVisibility(View.VISIBLE);
                        tblist.setVisibility(View.VISIBLE);
                        procced_to_map();
                    }
                }
                else
                {
                    tbwait.setVisibility(View.GONE);
                    tbprog.setVisibility(View.GONE);
//                tbbtn.setVisibility(View.VISIBLE);
                    tblist.setVisibility(View.VISIBLE);
                    procced_to_map();
                }
            }
        }

        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Dialog"+"\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void calculatemins(int pos)
    {
        try {
        if(choice.compareTo("places")==0)
        {
            pcount++;
            fcount+=3;
        }
        int i=0;
        Location loc1=null;

        for (int x = 0; x < fname.length; x++) {
            if (fname[x] == null) {
                i = x;
                break;
            }
        }

        if(i==0)
        {
            loc1 = new Location("");
            loc1.setLatitude(mylat);
            loc1.setLongitude(mylng);

        }
        else
        {
            loc1 = new Location("");
            loc1.setLatitude(Double.parseDouble(flat[i-1]));
            loc1.setLongitude(Double.parseDouble(flng[i-1]));
        }

        Location loc2 = new Location("");
        if(choice.compareTo("places")!=0)
        {
            loc2.setLatitude(Double.parseDouble(lat[pos]));
            loc2.setLongitude(Double.parseDouble(lng[pos]));
        }
        else
        {
            loc2.setLatitude(Double.parseDouble(plat[pos]));
            loc2.setLongitude(Double.parseDouble(plng[pos]));
        }

        float d=loc1.distanceTo(loc2);
        d=d/1000;
        d=d*3;
        int d1=Math.round(d);
        if(first)
        {
            totmin -= (d1 * 2) + 30;
        }
        else
        {
            totmin -= d1;
        }

        if(choice.compareTo("Breakfast")==0 || choice.compareTo("Evening Snacks")==0)
        {
            totmin-=40;

        }
        else if(choice.compareTo("Lunch")==0 || choice.compareTo("Dinner")==0)
        {
            totmin-=70;
        }
        else
        {
            totmin-=40;
        }

        fill_list(pos,false); //FILL THE LIST VIEW


        }
        catch (Exception e)
        {
            Toast.makeText(Planning_Rest.this, "Calculate Mins"+"\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public int random(int no)
    {
        int rndans=0;
        if(no==1)
        {
            rndans = new Random().nextInt(fsecond.length);
            if(String.valueOf(fsecond[rndans])==null)
            {
                random(no);
            }
        }
        else if(no==2)
        {
            rndans = new Random().nextInt(fthird.length);
            if(String.valueOf(fthird[rndans])==null)
            {
                random(no);
            }
        }
        else if(no==3)
        {
            rndans = new Random().nextInt(ffirst.length);
            if(String.valueOf(ffirst[rndans])==null)
            {
                random(no);
            }
        }
        return rndans;
    }

    public int checkplacerepeat(int no,int index)
    {
        int ff;
        ff=index;
//        Toast.makeText(this, no+"-"+pname[ff], Toast.LENGTH_SHORT).show();
        if(no==1)
        {
            one:
            for(int f=0;f<fname.length;f++)
            {
                if(fname[f]!=null)
                {
                    if(pname[ff]!=null) {
                        if (fname[f].compareTo(pname[ff]) == 0) {
                            ff = random(no);
                            continue one;
                        }
                    }
                }
            }
        }
        else if(no==2)
        {
            two:
            for(int f=0;f<f1name.length;f++)
            {
                if(f1name[f]!=null) {
                    if(pname[f]!=null) {
                        if (f1name[f].compareTo(pname[ff]) == 0) {
                            ff = random(no);
                            continue two;
                        }
                    }
                }
            }
        }
        else if(no==3)
        {
            three:
            for(int f=0;f<f2name.length;f++)
            {
                if(f2name[f]!=null) {
                    if(pname[f]!=null) {
                        if (f2name[f].compareTo(pname[ff]) == 0) {
                            ff = random(no);
                            continue three;
                        }
                    }
                }
            }
        }
        return ff;
    }

    public void fill_list(int j,boolean check)
    {
        try
        {
            //1st FINAL ARRAY
            int i = 0;
            for (int x = 0; x < fname.length; x++) {
                if (fname[x] == null) {
                    i = x;
                    break;
                }
            }

            //2nd FINAL ARRAY
            int i1 = 0;
            for (int x = 0; x < f1name.length; x++) {
                if (f1name[x] == null) {
                    i1 = x;
                    break;
                }
            }

            //3rd FINAL ARRAY
            int i2 = 0;
            for (int x = 0; x < f2name.length; x++) {
                if (f2name[x] == null) {
                    i2 = x;
                    break;
                }
            }

//            Toast.makeText(this, j+"\n"+name[j], Toast.LENGTH_SHORT).show();
            if(choice.compareTo("places")!=0)
            {
                fname[i] = name[j];
                fcontact[i] = contact[j];
                flat[i] = lat[j];
                flng[i] = lng[j];
                faddress[i] = address[j];
                fdistance[i] = distance[j];
                fisopen[i] = isopen[j];
                fopentill[i] = opentill[j];
                frating[i] = rating[j];
                freview[i] = review[j];
                fprice[i] = price[j];
                fchoice[i] = choice;

                if(check)
                {
                    plist_item.add(name[j]);
                    ArrayAdapter<String> padapt = new ArrayAdapter<String>(Planning_Rest.this, R.layout.planning_rest_listitem, R.id.plist_text, plist_item);
                    plist.setAdapter(padapt);

                    f1name[i1] = name[j];
                    f1contact[i1] = contact[j];
                    f1lat[i1] = lat[j];
                    f1lng[i1] = lng[j];
                    f1address[i1] = address[j];
                    f1distance[i1] = distance[j];
                    f1isopen[i1] = isopen[j];
                    f1opentill[i1] = opentill[j];
                    f1rating[i1] = rating[j];
                    f1review[i1] = review[j];
                    f1price[i1] = price[j];
                    f1choice[i1] = choice;

//                Toast.makeText(Planning_Rest.this, "f1--"+f1name[i1], Toast.LENGTH_LONG).show();

                    f2name[i2] = name[j];
                    f2contact[i2] = contact[j];
                    f2lat[i2] = lat[j];
                    f2lng[i2] = lng[j];
                    f2address[i2] = address[j];
                    f2distance[i2] = distance[j];
                    f2isopen[i2] = isopen[j];
                    f2opentill[i2] = opentill[j];
                    f2rating[i2] = rating[j];
                    f2review[i2] = review[j];
                    f2price[i2] = price[j];
                    f2choice[i2] = choice;
                }
                else
                {
                    f1name[i1] = name[1];
                    f1contact[i1] = contact[1];
                    f1lat[i1] = lat[1];
                    f1lng[i1] = lng[1];
                    f1address[i1] = address[1];
                    f1distance[i1] = distance[1];
                    f1isopen[i1] = isopen[1];
                    f1opentill[i1] = opentill[1];
                    f1rating[i1] = rating[1];
                    f1review[i1] = review[1];
                    f1price[i1] = price[1];
                    f1choice[i1] = choice;

//                Toast.makeText(Planning_Rest.this, "f1--"+f1name[i1], Toast.LENGTH_LONG).show();

                    f2name[i2] = name[2];
                    f2contact[i2] = contact[2];
                    f2lat[i2] = lat[2];
                    f2lng[i2] = lng[2];
                    f2address[i2] = address[2];
                    f2distance[i2] = distance[2];
                    f2isopen[i2] = isopen[2];
                    f2opentill[i2] = opentill[2];
                    f2rating[i2] = rating[2];
                    f2review[i2] = review[2];
                    f2price[i2] = price[2];
                    f2choice[i2] = choice;
                }
//                Toast.makeText(Planning_Rest.this, "f2--"+f2name[i2], Toast.LENGTH_LONG).show();
            }
            else
            {
                if(place_selection_by.compareTo("user")==0)
                {
                    fname[i] = pname[j];
                    fcontact[i] = pcontact[j];
                    flat[i] = plat[j];
                    flng[i] = plng[j];
                    faddress[i] = paddress[j];
                    fdistance[i] = pdistance[j];
                    fisopen[i] = pisopen[j];
                    fopentill[i] = popentill[j];
                    frating[i] = prating[j];
                    freview[i] = preview[j];
                    fprice[i] = pprice[j];
                    fchoice[i] = "place";
                }
                else {
//                Toast.makeText(Planning_Rest.this,"fcount-"+fcount, Toast.LENGTH_SHORT).show();
                    int a1 = fcount - 3;
                    int a2 = fcount - 2;
                    int a3 = fcount - 1;


                    //FILL PLACES TO 1st FINAL ARRAY
                    if (a1 > place_array_count) {
                        a1 = random(1);
                    }


                    a1 = checkplacerepeat(1, a1); // CHECK FOR REPEAT PLACES
                    fname[i] = pname[a1];
                    fcontact[i] = pcontact[a1];
                    flat[i] = plat[a1];
                    flng[i] = plng[a1];
                    faddress[i] = paddress[a1];
                    fdistance[i] = pdistance[a1];
                    fisopen[i] = pisopen[a1];
                    fopentill[i] = popentill[a1];
                    frating[i] = prating[a1];
                    freview[i] = preview[a1];
                    fprice[i] = pprice[a1];
                    fchoice[i] = "place";

                    //FILL PLACES TO 1st FINAL ARRAY
                    if (a2 > place_array_count) {
                        a2 = random(2);
                    }

                    a2 = checkplacerepeat(2, a2); // CHECK FOR REPEAT PLACES
                    f1name[i1] = pname[a2];
                    f1contact[i1] = pcontact[a2];
                    f1lat[i1] = plat[a2];
                    f1lng[i1] = plng[a2];
                    f1address[i1] = paddress[a2];
                    f1distance[i1] = pdistance[a2];
                    f1isopen[i1] = pisopen[a2];
                    f1opentill[i1] = popentill[a2];
                    f1rating[i1] = prating[a2];
                    f1review[i1] = preview[a2];
                    f1price[i1] = pprice[a2];
                    f1choice[i1] = "place";

//                Toast.makeText(Planning_Rest.this,"second-"+f1name[i1], Toast.LENGTH_SHORT).show();

                    if (a3 > place_array_count) {
                        a3 = random(3);
                    }

                    a3 = checkplacerepeat(3, a3); // CHECK FOR REPEAT PLACES
                    f2name[i2] = pname[a3];
                    f2contact[i2] = pcontact[a3];
                    f2lat[i2] = plat[a3];
                    f2lng[i2] = plng[a3];
                    f2address[i2] = paddress[a3];
                    f2distance[i2] = pdistance[a3];
                    f2isopen[i2] = pisopen[a3];
                    f2opentill[i2] = popentill[a3];
                    f2rating[i2] = prating[a3];
                    f2review[i2] = preview[a3];
                    f2price[i2] = pprice[a3];
                    f2choice[i2] = "place";
//                Toast.makeText(Planning_Rest.this,"third-"+f2name[i2], Toast.LENGTH_SHORT).show();
                }

            }

            if (choice.compareTo("places") != 0)
            {
                select_places();  //SEARCH FOR PLACES AGAIN
            }
            else
            {
                if(totmin>=30)
                {
                    dialog("new");
                }
                else
                {
                    tbwait.setVisibility(View.GONE);
                    tbprog.setVisibility(View.GONE);
//                    tbbtn.setVisibility(View.VISIBLE);
                    tblist.setVisibility(View.VISIBLE);
                    procced_to_map();
                }
            }
        }
        catch (Exception e)
        {
            StackTraceElement ele=e.getStackTrace()[0];
            Toast.makeText(Planning_Rest.this, "Fill List"+"\n"+e.getMessage()+"\n"+ele.getMethodName()+"-"+ele.getLineNumber(), Toast.LENGTH_LONG).show();
        }
    }

    public void select_start_position()
    {
        int t1;
        String s[]=fromtime.split(":");
        t1=Integer.parseInt(s[0]);

        if(t1==6 || t1==7 || t1==8)
        {
            start="place";
            next="breakfast";

        }
        else if(t1==9 || t1==10)
        {
            start="breakfast";

        }
        else if(t1==11 || t1==12)
        {
            start="place";
            next="lunch";

        }
        else if(t1==13 || t1==14)
        {
            start="lunch";

        }
        else if(t1==15 || t1==16)
        {
            start="place";
            next="cafe";

        }
        else if(t1==17 || t1==18)
        {
            start="cafe";
        }
        else if(t1==19 || t1==20)
        {
            start="place";
            next="dinner";
        }
        else if(t1==21 || t1==22)
        {
            start="dinner";
        }
        else
        {
            start="place";
            next="place";
        }

    }

    public void procced_to_map()
    {
        if(place_selection_by.compareTo("system")==0) {
            Intent i = new Intent(Planning_Rest.this, MAP_Activity_Rest.class);
            i.putExtra("fname", fname);
            i.putExtra("fcontact", fcontact);
            i.putExtra("flat", flat);
            i.putExtra("flng", flng);
            i.putExtra("faddress", faddress);
            i.putExtra("fdistance", fdistance);
            i.putExtra("fisopen", fisopen);
            i.putExtra("fopentill", fopentill);
            i.putExtra("frating", frating);
            i.putExtra("freview", freview);
            i.putExtra("fprice", fprice);
            i.putExtra("fchoice", fchoice);

            i.putExtra("f1name", f1name);
            i.putExtra("f1contact", f1contact);
            i.putExtra("f1lat", f1lat);
            i.putExtra("f1lng", f1lng);
            i.putExtra("f1address", f1address);
            i.putExtra("f1distance", f1distance);
            i.putExtra("f1isopen", f1isopen);
            i.putExtra("f1opentill", f1opentill);
            i.putExtra("f1rating", f1rating);
            i.putExtra("f1review", f1review);
            i.putExtra("f1price", f1price);
            i.putExtra("f1choice", f1choice);


            i.putExtra("f2name", f2name);
            i.putExtra("f2contact", f2contact);
            i.putExtra("f2lat", f2lat);
            i.putExtra("f2lng", f2lng);
            i.putExtra("f2address", f2address);
            i.putExtra("f2distance", f2distance);
            i.putExtra("f2isopen", f2isopen);
            i.putExtra("f2opentill", f2opentill);
            i.putExtra("f2rating", f2rating);
            i.putExtra("f2review", f2review);
            i.putExtra("f2price", f2price);
            i.putExtra("f2choice", f2choice);

            i.putExtra("start", start);
            i.putExtra("next", next);
            i.putExtra("totime", totime);
            startActivity(i);
            finish();
        }
        else if(place_selection_by.compareTo("user")==0)
        {
            tbbtn.setVisibility(View.VISIBLE);
        }
    }

    public void ps_Dailog()
    {
        ps=new Dialog(Planning_Rest.this);
        ps.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ps.setContentView(R.layout.choose);
        ps.setCancelable(false);
        RadioGroup rg= (RadioGroup) ps.findViewById(R.id.crg);
        final Button proceed= (Button) ps.findViewById(R.id.proceed);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.user)
                {
                    place_selection_by="user";
                }
                else if(checkedId==R.id.system)
                {
                    place_selection_by="system";
                }
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(place_selection_by.compareTo("")!=0)
                {
                    if(place_selection_by.compareTo("user")==0) {
                        ps.cancel();
                        tbhead.setVisibility(View.GONE);
                        tblist.setVisibility(View.VISIBLE);
                        tbwait.setVisibility(View.VISIBLE);
                        wait.setText("Please Wait....");
                        tbprog.setVisibility(View.GONE);
                        new getcustinfo().execute(uid);
                    }
                    else
                    {
                        ps.cancel();
                        tbwait.setVisibility(View.GONE);
                        tbprog.setVisibility(View.VISIBLE);
                        new getcustinfo().execute(uid);
                    }
                }
                else
                {
                    Snackbar snack=Snackbar.make(v,"Select any Action",Snackbar.LENGTH_SHORT);
                    View vs=snack.getView();
                    TextView txt= (TextView) vs.findViewById(android.support.design.R.id.snackbar_text);
                    txt.setTextColor(Color.RED);
                    snack.show();
                }
            }
        });

        ps.show();

    }

}
