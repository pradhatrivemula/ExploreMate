package com.example.hp.smart_city_traveller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by hp on 8/8/2016.
 */
public class Place_Adapter extends ArrayAdapter<String>
{

    String[] name,contact,lat,lng,address,distance,isopen,opentill,rating,review,price;
    Context con;
    TableRow nametb,conttb,addtb,disttb,opentilltb,pricetb,ratingtb,reviewtb;
    int zz=1000,z=0;
    String id,uid,date;
    Calendar cal;
    SharedPreferences sp;
    public Place_Adapter(Context context, String[] name, String[] contact, String[] lat, String[] lng, String[] address, String[] distance, String[] isopen, String[] opentill, String[] rating, String[] review, String[] price) {
        super(context,R.layout.place_list_item,name);
        this.name=name;
        this.contact=contact;
        this.lat=lat;
        this.lng=lng;
        this.address=address;
        this.distance=distance;
        this.isopen=isopen;
        this.opentill=opentill;
        this.rating=rating;
        this.review=review;
        this.price=price;
        con=context;
        cal=Calendar.getInstance();
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = (cal.get(Calendar.MONTH) + 1);
        int y = cal.get(Calendar.YEAR);
        String mn="";
        if(String.valueOf(m).length()==1)
        {
            mn="0"+m;
        }
        else
        {
            mn=""+m;
        }
        date = y + "-" + mn + "-" + d;
        new getid().execute();
        sp=con.getSharedPreferences("travel",Context.MODE_PRIVATE);
        uid=sp.getString("userid","");
//        Toast.makeText(con, "adapt:"+name.length, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(con);
        View v=inflater.inflate(R.layout.place_list_item, null, true);
        final TextView nametxt,conttxt,addtxt,disttxt,opentilltxt,pricetxt,ratingtxt,reviewtxt;
        final Button selectbtn;
        final ImageView mapimg;
//        Toast.makeText(con, "pos:"+position, Toast.LENGTH_SHORT).show();
        nametb= (TableRow) v.findViewById(R.id.nametb);
        conttb= (TableRow) v.findViewById(R.id.conttb);
        addtb= (TableRow) v.findViewById(R.id.addrtb);
        disttb= (TableRow) v.findViewById(R.id.disttb);
        opentilltb= (TableRow) v.findViewById(R.id.opentilltb);
        pricetb= (TableRow) v.findViewById(R.id.pricetb);
        ratingtb= (TableRow) v.findViewById(R.id.ratingtb);
        reviewtb= (TableRow) v.findViewById(R.id.reviewtb);

        nametxt= (TextView) v.findViewById(R.id.pl_name);
        conttxt= (TextView) v.findViewById(R.id.pl_cont);
        addtxt= (TextView) v.findViewById(R.id.pl_add);
        disttxt= (TextView) v.findViewById(R.id.pl_dist);
        opentilltxt= (TextView) v.findViewById(R.id.pl_open);
        pricetxt= (TextView) v.findViewById(R.id.pl_price);
        ratingtxt= (TextView) v.findViewById(R.id.pl_rating);
        reviewtxt= (TextView) v.findViewById(R.id.pl_review);

        selectbtn= (Button) v.findViewById(R.id.pl_select);
        mapimg= (ImageView) v.findViewById(R.id.pl_map);

        tbvisible();

        if(name[position].compareTo("na")==0)
        {
            nametb.setVisibility(View.GONE);
        }
        else
        {
            nametxt.setText(name[position]);
        }

        if(contact[position].compareTo("na")==0)
        {
            conttb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Contact :"+"</b>"+contact[position];
            conttxt.setText(Html.fromHtml(s));

        }

        if(address[position].compareTo("na")==0)
        {
            addtb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Address :"+"</b>"+address[position];
            addtxt.setText(Html.fromHtml(s));
        }

        if(distance[position].compareTo("na")==0)
        {
            disttb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Distance :"+"</b>"+distance[position];
            disttxt.setText(Html.fromHtml(s));
        }

        if(opentill[position].compareTo("na")==0)
        {
            opentilltb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Open Till :"+"</b>"+opentill[position];
            opentilltxt.setText(Html.fromHtml(s));
        }

        if(price[position].compareTo("na")==0)
        {
            pricetb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Price Estimate :"+"</b>"+price[position];
            pricetxt.setText(Html.fromHtml(s));
        }

        if(rating[position].compareTo("0")==0)
        {
            ratingtb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Rating :"+"</b>"+rating[position];
            ratingtxt.setText(Html.fromHtml(s));
        }

        if(review[position].compareTo("na")==0)
        {
            reviewtb.setVisibility(View.GONE);
        }
        else
        {
            String s="<b>"+"Review :"+"</b>"+review[position];
            reviewtxt.setText(Html.fromHtml(s));
        }

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lt = sp.getString(uid+"lat", "0.0");
                String lg = sp.getString(uid+"lng", "0.0");

                SharedPreferences.Editor editor=sp.edit();
                editor.putString(uid+"mylat",lt);
                editor.putString(uid+"mylng",lg);
                editor.commit();

                z=position;
                new deloldplaces().execute(uid);
            }
        });

        mapimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, MAP_Activity.class);
                i.putExtra("lat", lat[position]);
                i.putExtra("lng", lng[position]);
                i.putExtra("name", name[position]);
                con.startActivity(i);
            }
        });
        return v;
    }

    public void tbvisible()
    {
        nametb.setVisibility(View.VISIBLE);
        conttb.setVisibility(View.VISIBLE);
        addtb.setVisibility(View.VISIBLE);
        disttb.setVisibility(View.VISIBLE);
        opentilltb.setVisibility(View.VISIBLE);
        pricetb.setVisibility(View.VISIBLE);
        ratingtb.setVisibility(View.VISIBLE);
        reviewtb.setVisibility(View.VISIBLE);
    }

    public int getz()
    {
        return zz;
    }
    public void setz(int o)
    {
        zz=o;
    }

    public class finalroute extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.addfinalroute(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15],params[16]);
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
//            Toast.makeText(con,"ins----"+s, Toast.LENGTH_SHORT).show();
            if(s.compareTo("true")==0)
            {
                zz=1;
                Intent i = new Intent(con, MAP_Activity.class);
                i.putExtra("lat", lat[z]);
                i.putExtra("lng", lng[z]);
                i.putExtra("name", name[z]);
                con.startActivity(i);
            }
            else
            {
                Toast.makeText(con,"ins----"+s, Toast.LENGTH_SHORT).show();
            }
        }
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
//            Toast.makeText(con,s, Toast.LENGTH_SHORT).show();
            id=s;
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
            String lt = sp.getString(uid+"lat", "0.0");
            String lg = sp.getString(uid+"lng", "0.0");
            new finalroute().execute(id,uid,"1",name[z], contact[z], lat[z], lng[z], address[z], distance[z], isopen[z], opentill[z], rating[z], review[z], price[z], "Park",date,lt+","+lg);
        }
    }
}
