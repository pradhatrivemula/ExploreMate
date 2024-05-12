package com.example.hp.smart_city_traveller;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Plan_ur_Day extends Fragment
{
    String firsttime="";
    Dialog dgps;
    Calendar cal;
    TextView fromtime, totime;
    int temphr, tempmin;
    Button submit;
    RelativeLayout fromray, toray, ray;
    Double Latitude = 0.0, Longitude = 0.0;
    Context mContext;
    Activity activity;
    GPS_Tracker gps;

    double gorlat=19.1551,gorlng=72.8679;

    TableLayout timelay,adventurelay;
    RadioGroup rgadv;
    Button advsubmit;
    String AdventureAns="";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    double glat=19.1551,glng=72.8679;

    Timer timer;
    TimerTask timerTask;
    Handler handler = new Handler();
    Spinner spin;
    String pricecount="1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.plan_ur_day, container, false);
        cal = Calendar.getInstance();
        fromtime = (TextView) v.findViewById(R.id.fromtime);
        totime = (TextView) v.findViewById(R.id.totime);
        submit = (Button) v.findViewById(R.id.sub);
        fromray = (RelativeLayout) v.findViewById(R.id.fromray);
        toray = (RelativeLayout) v.findViewById(R.id.toray);
        ray = (RelativeLayout) v.findViewById(R.id.hmray);
        timelay= (TableLayout) v.findViewById(R.id.timelay);
        adventurelay= (TableLayout) v.findViewById(R.id.advenure);
        spin= (Spinner) v.findViewById(R.id.pricespin);

        final ArrayList<String> pricelist=new ArrayList<String>();
        pricelist.add("Moderate");
        pricelist.add("High");

        ArrayAdapter<String> adapt=new ArrayAdapter<String>(getActivity(),R.layout.spin_item,R.id.txt,pricelist);
        spin.setAdapter(adapt);

        timer = new Timer();
        mContext = getActivity().getApplicationContext();
        activity=getActivity();

//        getlatlng();
        sp=getActivity().getSharedPreferences("travel",Context.MODE_PRIVATE);
        firsttime=sp.getString("firsttime","");
        if(firsttime.compareTo("")==0)
        {
            addtodb();
        }
        rgadv= (RadioGroup) v.findViewById(R.id.rgadventure);
        advsubmit= (Button) v.findViewById(R.id.advsubmit);

        String hr = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(cal.get(Calendar.MINUTE));
        if (hr.length() == 1)
        {
            hr = "0" + hr;
        }
        if (min.length() == 1) {
            min = "0" + min;
        }
        String time = hr + ":" + min;
        fromtime.setText(time);
        int temp = Integer.parseInt(hr);
        if (temp <= 21) {
            temp += 2;
            if (String.valueOf(temp).length() == 1) {
                totime.setText("0" + temp + ":" + min);
            } else {
                totime.setText(temp + ":" + min);
            }
        } else if (temp == 22) {
            totime.setText("00" + ":" + min);
        } else if (temp == 23) {
            totime.setText("01" + ":" + min);
        }

        fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str[] = fromtime.getText().toString().split(":");
                String thr = str[0];
                String tmin = str[1];
                dialog("from", thr, tmin);
            }
        });

        totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str[] = totime.getText().toString().split(":");
                String thr = str[0];
                String tmin = str[1];
                dialog("to", thr, tmin);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!weHavePermissionforGPS())
                {
                    requestforGPSPermissionFirst();
                }
                else {

                    if (fromtime.getText().toString().compareTo(totime.getText().toString()) != 0) {
                        String time1[] = fromtime.getText().toString().split(":");
                        final int t1hr = Integer.parseInt(time1[0]);
                        final int t1min = Integer.parseInt(time1[1]);

                        String time2[] = totime.getText().toString().split(":");
                        final int t2hr = Integer.parseInt(time2[0]);
                        final int t2min = Integer.parseInt(time2[1]);

                        Calendar today=Calendar.getInstance();
                        today.set(Calendar.HOUR_OF_DAY,t1hr);
                        today.set(Calendar.MINUTE,t1min);
                        today.set(Calendar.SECOND,0);
                        today.set(Calendar.MILLISECOND,0);

                        Calendar end=Calendar.getInstance();
                        if((t1hr>=6 && t1hr<=23) && (t2hr>=0 && t2hr<=4))
                        {
                            end.add(Calendar.DAY_OF_MONTH,1);
                        }
                        end.set(Calendar.HOUR_OF_DAY,t2hr);
                        end.set(Calendar.MINUTE,t2min);
                        end.set(Calendar.SECOND,0);
                        end.set(Calendar.MILLISECOND,0);

                        int mesg=0;
                        long diff=Calculatemins(today,end);
                        if(diff<0)
                        {
                            mesg=3;
                        }
                        else if(diff<120)
                        {
                            mesg=1;
                        }
                        else if(diff>1320)
                        {
                            mesg=2;
                        }
                        else
                        {
                            if(today.before(end))
                            {
                                if(t1hr>=3 && t1hr<=5)
                                {
                                    mesg=5;
                                }
                                else if(t2hr>=5 && t2hr<=7)
                                {
                                    mesg=4;
                                }
                                else
                                {
                                    if (t1hr <= 9 && t2hr >= 19) {
                                        //ASK FOR ADVENTURE PARK
                                        timelay.setVisibility(View.GONE);
                                        adventurelay.setVisibility(View.VISIBLE);

                                    }
                                    else
                                    {
                                        Intent i = new Intent(getActivity(),Map_Point_Selection.class);
                                        i.putExtra("fromtime", fromtime.getText().toString());
                                        i.putExtra("totime", totime.getText().toString());
                                        i.putExtra("to","rest");
                                        startActivity(i);
                                    }
                                }
                            }
                            else
                            {
                                mesg=3;
                            }
                        }


                        if(mesg==1)
                        {
                            Snackbar snack = Snackbar.make(ray, "The Time Difference should be Minimum 120 mins", Snackbar.LENGTH_LONG);
                            View vw = snack.getView();
                            TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                            txt.setTextColor(Color.RED);
                            snack.show();
                        }
                        else if(mesg==2)
                        {
                            Snackbar snack = Snackbar.make(ray, "Maximum only 22 hours are allowed", Snackbar.LENGTH_LONG);
                            View vw = snack.getView();
                            TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                            txt.setTextColor(Color.RED);
                            snack.show();
                        }
                        else if(mesg==3)
                        {
                            toray.setBackgroundColor(Color.RED);
                            Snackbar snack = Snackbar.make(ray, "To-Time should be less then 24 hours from From-Time", Snackbar.LENGTH_LONG);
                            View vw = snack.getView();
                            TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                            txt.setTextColor(Color.RED);
                            snack.show();
                        }
                        else if(mesg==4)
                        {
                            Snackbar snack = Snackbar.make(ray, "To-Time should be less then 5 am", Snackbar.LENGTH_LONG);
                            View vw = snack.getView();
                            TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                            txt.setTextColor(Color.RED);
                            snack.show();
                        }
                        else if(mesg==5)
                        {
                            Snackbar snack = Snackbar.make(ray, "From-Time should be greater then 5 am or less then 2 am", Snackbar.LENGTH_LONG);
                            View vw = snack.getView();
                            TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                            txt.setTextColor(Color.RED);
                            snack.show();
                        }

                    }
                }
            }
        });

        //ADVENTURE

        rgadv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.waterpark) {
                    AdventureAns = "waterpark";
                } else if (checkedId == R.id.amusepark) {
                    AdventureAns = "amusement";
                } else if (checkedId == R.id.advnone) {
                    AdventureAns = "none";
                }
            }
        });

        advsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdventureAns.compareTo("") != 0) {
                    if (AdventureAns.compareTo("none") == 0) {
                        Intent i = new Intent(getActivity(),Map_Point_Selection.class);
                        i.putExtra("fromtime", fromtime.getText().toString());
                        i.putExtra("totime", totime.getText().toString());
                        i.putExtra("to","rest");
                        startActivity(i);

                        timelay.setVisibility(View.VISIBLE);
                        adventurelay.setVisibility(View.GONE);
                    } else {
                        timelay.setVisibility(View.VISIBLE);
                        adventurelay.setVisibility(View.GONE);
                        editor = sp.edit();
                        editor.putString("adv", AdventureAns);
                        editor.commit();
                        Intent i = new Intent(getActivity(),Map_Point_Selection.class);
                        i.putExtra("fromtime", fromtime.getText().toString());
                        i.putExtra("totime", totime.getText().toString());
                        i.putExtra("to", "adv");
                        i.putExtra("type", "adv");
                        startActivity(i);
                    }
                }
                else
                {
                    Snackbar snack = Snackbar.make(ray, "Select Any one to Proceed", Snackbar.LENGTH_SHORT);
                    View vw = snack.getView();
                    TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                    txt.setTextColor(Color.RED);
                    snack.show();
                }
            }
        });


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pricecount=""+(i+1);
                editor=sp.edit();
                editor.putString("price",pricecount);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    public long Calculatemins(Calendar d1c, Calendar d2c)
    {
        java.util.Date d1=d1c.getTime(),d2=d2c.getTime();

        long duration  = d2.getTime() - d1.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        return diffInMinutes;
    }

    public void dialog(final String source, String hr, String min) {
        final Dialog d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.time_dialog);

        final EditText hourtxt, mintxt;
        hourtxt = (EditText) d.findViewById(R.id.hr);
        mintxt = (EditText) d.findViewById(R.id.min);
        hourtxt.setCursorVisible(false);
        mintxt.setCursorVisible(false);
        final ImageView uphr, dwnhr, upmin, dwnmin;
        uphr = (ImageView) d.findViewById(R.id.up_hr);
        dwnhr = (ImageView) d.findViewById(R.id.dwn_hr);
        upmin = (ImageView) d.findViewById(R.id.up_min);
        dwnmin = (ImageView) d.findViewById(R.id.dwn_min);
        Button ok = (Button) d.findViewById(R.id.ok);

                hourtxt.setText(hr);
        mintxt.setText(min);
        hourtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourtxt.setCursorVisible(true);
            }
        });

        mintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mintxt.setCursorVisible(true);
            }
        });

        hourtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(hourtxt.getText().toString()) > 23 || Integer.parseInt(hourtxt.getText().toString()) < 0) {
                        hourtxt.setText("00");
                        hourtxt.setSelection(hourtxt.length());
                    }
                } catch (Exception e) {

                }
            }
        });

        mintxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(mintxt.getText().toString()) > 59 || Integer.parseInt(mintxt.getText().toString()) < 0) {
                        mintxt.setText("00");
                        mintxt.setSelection(hourtxt.length());
                    }
                } catch (Exception e) {
                }
            }
        });

        uphr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hourtxt.requestFocus();
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        uphr.setImageResource(R.drawable.uppress);
                        break;
                    case MotionEvent.ACTION_UP:
                        uphr.setImageResource(R.drawable.up);

                        hourtxt.setCursorVisible(false);
                        mintxt.setCursorVisible(false);
                        temphr = Integer.parseInt(hourtxt.getText().toString());
                        if (temphr <= 22) {
                            temphr += 1;
                            if (String.valueOf(temphr).length() == 1) {
                                hourtxt.setText("0" + temphr);
                            } else {
                                hourtxt.setText("" + temphr);
                            }
                            hourtxt.setSelection(hourtxt.length());
                        } else {
                            temphr = 00;
                            hourtxt.setText("0" + temphr);
                            hourtxt.setSelection(hourtxt.length());
                        }

                        break;
                }
                return true;
            }
        });

        dwnhr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hourtxt.requestFocus();
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        dwnhr.setImageResource(R.drawable.downpress);
                        break;
                    case MotionEvent.ACTION_UP:
                        dwnhr.setImageResource(R.drawable.down);

                        hourtxt.setCursorVisible(false);
                        mintxt.setCursorVisible(false);
                        temphr = Integer.parseInt(hourtxt.getText().toString());
                        if (temphr >= 2) {
                            temphr -= 1;
                            if (String.valueOf(temphr).length() == 1) {
                                hourtxt.setText("0" + temphr);
                            } else {
                                hourtxt.setText("" + temphr);
                            }

                        } else if (temphr == 1) {
                            temphr = 0;
                            hourtxt.setText("0" + temphr);
                        } else {
                            temphr = 23;
                            hourtxt.setText("" + temphr);
                        }
                        hourtxt.setSelection(hourtxt.length());

                        break;
                }
                return true;
            }
        });

        upmin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mintxt.requestFocus();
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_DOWN:
                        upmin.setImageResource(R.drawable.uppress);
                        break;
                    case MotionEvent.ACTION_UP:
                        upmin.setImageResource(R.drawable.up);
                        hourtxt.setCursorVisible(false);
                        mintxt.setCursorVisible(false);
                        tempmin = Integer.parseInt(mintxt.getText().toString());
                        if (tempmin <= 58)
                        {
                            tempmin += 1;
                            if (String.valueOf(tempmin).length() == 1) {
                                mintxt.setText("0" + tempmin);
                            } else {
                                mintxt.setText("" + tempmin);
                            }
                            mintxt.setSelection(mintxt.length());
                        }
                        else
                        {
                            tempmin = 00;
                            mintxt.setText("0" + tempmin);
                            mintxt.setSelection(mintxt.length());
                        }
                        mintxt.setSelection(mintxt.length());
                        break;
                }
                return true;
            }
        });

        dwnmin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mintxt.requestFocus();
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        dwnmin.setImageResource(R.drawable.downpress);
                        break;
                    case MotionEvent.ACTION_UP:
                        dwnmin.setImageResource(R.drawable.down);

                        hourtxt.setCursorVisible(false);
                        mintxt.setCursorVisible(false);
                        tempmin = Integer.parseInt(mintxt.getText().toString());
                        if (tempmin >= 2) {
                            tempmin -= 1;
                            if (String.valueOf(tempmin).length() == 1) {
                                mintxt.setText("0" + tempmin);
                            } else {
                                mintxt.setText("" + tempmin);
                            }

                        } else if (tempmin == 1) {
                            tempmin = 0;
                            mintxt.setText("0" + tempmin);
                        } else {
                            tempmin = 59;
                            mintxt.setText("" + tempmin);
                        }
                        mintxt.setSelection(mintxt.length());
                        break;
                }
                return true;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (source.compareTo("from") == 0)
                {
                    fromtime.setText(hourtxt.getText().toString() + ":" + mintxt.getText().toString());
                }
                else if (source.compareTo("to") == 0)
                {
                    totime.setText(hourtxt.getText().toString() + ":" + mintxt.getText().toString());
                }

                d.cancel();
                if (fromtime.getText().toString().compareTo(totime.getText().toString()) == 0)
                {
                    toray.setBackgroundColor(Color.RED);
                    Snackbar snack = Snackbar.make(ray, "To-Time should be less then 24 hours from From-Time", Snackbar.LENGTH_LONG);
                    View vw = snack.getView();
                    TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                    txt.setTextColor(Color.RED);
                    snack.show();
                }
                else
                {
                    toray.setBackgroundColor(Color.parseColor("#666666"));
                }


            }
        });

        d.show();
    }


    public void getlatlng()
    {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        else
        {
            //Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPS_Tracker(mContext,activity);

            // Check if GPS enabled
            if (gps.canGetLocation())
            {
                gpsdailog();
                initializeTimerTask();
                timer.schedule(timerTask, 0, 500);
                Latitude = gps.getLatitude();
                Longitude = gps.getLongitude();


                //Toast.makeText(getActivity(), "Your Location is - \nLat: " + Latitude + "\nLong: " + Longitude, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Enable GPS", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Latitude = gps.getLatitude();
                        Longitude = gps.getLongitude();
//                        Toast.makeText(getActivity(),Latitude+","+Longitude, Toast.LENGTH_SHORT).show();
                        if(Latitude!=0.0 && Longitude!=0.0)
                        {
                            editor = sp.edit();
                            editor.putString("lat", String.valueOf(Latitude));
                            editor.putString("lng", String.valueOf(Longitude));
                            editor.commit();
                            try
                            {
                                dgps.cancel();
                            }
                            catch (Exception e){}
                        }
                    }
                });
            }
        };
    }

    public void addtodb()
    {
        String s="";
        DB db=new DB(getActivity());
        db.open();
        s+=db.insertdata("Churchgate","18.925914","72.830078");
        s+=db.insertdata("Bandra","19.056882","72.830852");
        s+=db.insertdata("Juhu","19.110051","72.824009");
        s+=db.insertdata("Powai","19.117681","72.903761");
        s+=db.insertdata("Goregaon","19.160232","72.855271");
        db.close();
        editor=sp.edit();
        editor.putString("firsttime","1000");
        editor.commit();
//        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public void getdata()
    {
        DB db=new DB(getActivity());
        db.open();
        String str=db.getdata();
        db.close();
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }

    public Boolean checktime()
    {
        int t1hr,t2hr;
        String temp1[]=fromtime.getText().toString().split(":");
        t1hr=Integer.parseInt(temp1[0]);
        String temp2[]=fromtime.getText().toString().split(":");
        t2hr=Integer.parseInt(temp2[0]);
        return true;
    }

    public void gpsdailog()
    {
        dgps=new Dialog(getActivity());
        dgps.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dgps.setContentView(R.layout.gspdailog);
//        dgps.setCancelable(false);
        dgps.show();
    }


    private boolean weHavePermissionforGPS()
    {
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestforGPSPermissionFirst()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))
        {
            requestForResultContactsPermission();
        }
        else
        {
            requestForResultContactsPermission();
        }
    }
    private void requestForResultContactsPermission()
    {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 111);
    }

}
