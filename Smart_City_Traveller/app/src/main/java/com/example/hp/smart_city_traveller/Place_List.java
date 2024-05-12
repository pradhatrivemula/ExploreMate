package com.example.hp.smart_city_traveller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hp on 8/6/2016.
 */
public class Place_List extends AppCompatActivity{

    String type;
    String Latitude,Longitude;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String request="https://api.foursquare.com/v2/venues/explore?client_id=TDQG4LF1M1O5SZESHJ42BRSZQDPJY4B53NQN5RDTRRZZPNYM&client_secret=PGLQ4RV5R0FDIPHAPJO4RT3KJXO02ARZ4LQ3NWS3TPFIUUJ0&v=20130815";
    String sortby="&sortByDistance=1";

    String[] name,contact,lat,lng,address,distance,isopen,opentill,rating,review,price;
    int add=1000;String addr;

    TableRow tbcont,tbadd,tbdist,tbopen,tbprice,tbrating,tbreview,tbprog;
    Place_Adapter pa;
    ListView list;
    List<HashMap<String, String>> lst;
    HashMap<String, String> hm;

    String[] from={"name","cont","add","dist","open","price","rating","review"};
    int[] to={R.id.pl_name,R.id.pl_cont,R.id.pl_add,R.id.pl_dist,R.id.pl_open,R.id.pl_price,R.id.pl_rating,R.id.pl_review};
    SimpleAdapter adapt;

    RelativeLayout ray;
    int inscount;
    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler();
    String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_list);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Place List");

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        timer = new Timer();
        Intent i=getIntent();
        type = i.getStringExtra("type");
        sp=getSharedPreferences("travel", Context.MODE_PRIVATE);
        String uid=sp.getString("userid","");
        Latitude = sp.getString(uid+"lat", "0.0");
        Longitude=sp.getString(uid+"lng", "0.0");
        //Toast.makeText(Place_List.this,type, Toast.LENGTH_SHORT).show();
        tbprog= (TableRow) findViewById(R.id.tbprog);
        ray= (RelativeLayout) findViewById(R.id.ray);
//        tbcont= (TableRow) findViewById(R.id.conttb);
//        tbadd= (TableRow) findViewById(R.id.addrtb);
//        tbdist= (TableRow) findViewById(R.id.disttb);
//        tbopen= (TableRow) findViewById(R.id.opentilltb);
//        tbprice= (TableRow) findViewById(R.id.pricetb);
//        tbrating= (TableRow) findViewById(R.id.ratingtb);
//        tbreview= (TableRow) findViewById(R.id.reviewtb);
        list= (ListView) findViewById(R.id.placelist);
        lst=new ArrayList<HashMap<String,String>>();
        if(type.compareTo("adv")==0)
        {
            String ll="&ll="+Latitude+","+Longitude;
            String query="&query="+sp.getString("adv","");
            request+=ll+query+sortby;
            calculate_place(request);
        }
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
                        Toast.makeText(Place_List.this, "Found Nothing", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Parse_JSON(items);
                    }
                } catch (JSONException e) {
                    Toast.makeText(Place_List.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Place_List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Place_List.this);
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
            Toast.makeText(Place_List.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

    public void SETNA()
    {
        for(int x=1;x<name.length;x++)
        {

            if(name[x]==null)
            {
                name[x]="na";
            }
            name[x].replaceAll("\"","");
            name[x].replaceAll("'","");

            if(contact[x]==null)
            {
                contact[x]="na";
            }
            contact[x].replaceAll("\"","");
            contact[x].replaceAll("'","");

            if(lat[x]==null)
            {
                lat[x]="na";
            }
            lat[x].replaceAll("\"","");
            lat[x].replaceAll("'","");

            if(lng[x]==null)
            {
                lng[x]="na";
            }
            lng[x].replaceAll("\"","");
            lng[x].replaceAll("'","");

            if(address[x]==null)
            {
                address[x]="na";
            }
            address[x].replaceAll("\"","");
            address[x].replaceAll("'","");

            if(distance[x]==null)
            {
                distance[x]="0";
            }
            distance[x].replaceAll("\"","");
            distance[x].replaceAll("'","");

            if(isopen[x]==null)
            {
                isopen[x]="na";
            }
            isopen[x].replaceAll("\"","");
            isopen[x].replaceAll("'","");

            if(opentill[x]==null)
            {
                opentill[x]="na";
            }
            opentill[x].replaceAll("\"","");
            opentill[x].replaceAll("'","");

            if(rating[x]==null)
            {
                rating[x]="0";
            }
            rating[x].replaceAll("\"","");
            rating[x].replaceAll("'","");

            if(review[x]==null)
            {
                review[x]="na";
            }
            review[x].replaceAll("\"","");
            review[x].replaceAll("'","");

            if(price[x]==null)
            {
                price[x]="na";
            }
            price[x].replaceAll("\"","");
            price[x].replaceAll("'","");

            //Toast.makeText(Place_List.this,name[x]+"\n"+contact[x]+"\n"+lat[x]+"\n"+lng[x]+"\n"+distance[x]+"\n"+isopen[x]+"\n"+opentill[x]+"\n"+rating[x]+"\n"+review[x]+"\n"+price[x], Toast.LENGTH_SHORT).show();
//            new addtodb().execute(name[x],contact[x], lat[x],lng[x],address[x],distance[x],isopen[x],opentill[x],rating[x],review[x],"1234",price[x]);


            DB db=new DB(Place_List.this);
            db.open();
            db.addtodb(name[x],contact[x],lat[x],lng[x],address[x],distance[x],isopen[x],opentill[x],rating[x],review[x],price[x]);
            db.close();
        }

        DB db=new DB(Place_List.this);
        db.open();
        String res=db.getdbdata("50");
        db.close();

        String tres[]=res.split("\\#");
        name = new String[tres.length];
        contact = new String[tres.length];
        lat = new String[tres.length];
        lng = new String[tres.length];
        address = new String[tres.length];
        distance = new String[tres.length];
        opentill = new String[tres.length];
        isopen = new String[tres.length];
        rating = new String[tres.length];
        review = new String[tres.length];
        price = new String[tres.length];

        for(int i=0;i<tres.length;i++)
        {
            String temp1[]=tres[i].split("\\*");
            name[i] = temp1[0];
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
        }

        initializeTimerTask();
        timer.schedule(timerTask, 0, 500);

        pa=new Place_Adapter(Place_List.this,name,contact,lat,lng,address,distance,isopen,opentill,rating,review,price);
        list.setAdapter(pa);
        tbprog.setVisibility(View.GONE);
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        int i=pa.getz();
                        if (i==1)
                        {
                            finish();
                            pa.setz(0);
                        }
                    }
                });
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
