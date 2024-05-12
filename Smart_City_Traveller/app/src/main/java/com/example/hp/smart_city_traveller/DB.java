package com.example.hp.smart_city_traveller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by hp on 8/5/2016.
 */
public class DB {

    private static final String DBNAME="traveldb";
    private static final String TBNAME="Places";
    private static final String TBTEMP="temp";
    private static final String TempPlace="temp_place";
    private static final int DBVERSION=1;


    private static final String AREA="area";
    private static final String LAT="lat";
    private static final String LNG="lng";

    private static final String DIST="dist";

    private static final String TNAME="tname";
    private static final String TCONT="tcont";
    private static final String TLAT="tlat";
    private static final String TLNG="tlng";
    private static final String TADD="tadd";
    private static final String TDIST="tdist";
    private static final String TISOPEN="tispon";
    private static final String TOPENTILL="topentill";
    private static final String TRATING="trating";
    private static final String TREVIEW="treview";
    private static final String TPRICE="tprice";


    Context con;
    dbhelp dbh;
    SQLiteDatabase sqldb;


    public class dbhelp extends SQLiteOpenHelper
    {

        public dbhelp(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TBNAME + " (" +
                    AREA + " TEXT NOT NULL, " + LAT + " TEXT NOT NULL, " + LNG + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + TBTEMP + " (" +
                    DIST + " FLOAT NOT NULL, " + LAT + " TEXT NOT NULL, " + LNG + " TEXT NOT NULL, " + AREA + " TEXT NOT NULL);");

            db.execSQL("CREATE TABLE " + TempPlace + " (" +
                    TNAME + " TEXT NOT NULL, " + TCONT + " TEXT NOT NULL, " + TLAT + " TEXT NOT NULL, " +
                    TLNG + " TEXT NOT NULL, " + TADD + " TEXT NOT NULL, " + TDIST + " TEXT NOT NULL, " +
                    TISOPEN + " TEXT NOT NULL, " + TOPENTILL + " TEXT NOT NULL, " + TRATING + " FLOAT NOT NULL, "
                    + TREVIEW + " TEXT NOT NULL, " + TPRICE + " TEXT NOT NULL);");

            //addtodb();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TBNAME + "");
            db.execSQL("DROP TABLE IF EXISTS " + TBTEMP + "");
            db.execSQL("DROP TABLE IF EXISTS " + TempPlace + "");

        }

    }

    public DB(Context c)
    {
        con=c;
    }
    public DB open()
    {
        dbh=new dbhelp(con);
        sqldb=dbh.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbh.close();
    }

    public String insertdata(String name, String lat, String lng)
    {
        String s="false";
        ContentValues cv=new ContentValues();
        cv.put(AREA,name);
        cv.put(LAT,lat);
        cv.put(LNG,lng);
        sqldb.insert(TBNAME, null, cv);
        s="true";
        return s;
    }

    public String getdata()
    {
        String s="";
        Cursor c=sqldb.query(TBNAME, new String[]{AREA, LAT, LNG}, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do
            {
                s+=c.getString(0)+"*"+c.getString(1)+"*"+c.getString(2)+"#";
            }
            while (c.moveToNext());
        }
        else
        {
            s="no data";
        }
        return s;

    }

    public void inserttemp(float dist, String s, String s1,String a)
    {
        ContentValues cv=new ContentValues();
        cv.put(DIST,dist);
        cv.put(LAT,s);
        cv.put(LNG,s1);
        cv.put(AREA,a);
        sqldb.insert(TBTEMP,null,cv);
    }

    public void deletetemp()
    {
        sqldb.delete(TBTEMP, null, null);
    }

    public String gettempdata()
    {
        String s="";
        Cursor c=sqldb.query(TBTEMP,new String[]{DIST,LAT,LNG,AREA},null,null,null,null,DIST + " ASC");
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do
            {
                s+=c.getFloat(0)+"*"+c.getString(1)+"*"+c.getString(2)+"*"+c.getString(3)+"#";;
            }
            while (c.moveToNext());
        }
        else
        {
            s="no";
        }

        return s;

    }

    public void addtodb(String name, String cont, String lat, String lng, String addres, String dist, String isopen, String opentill, String rating, String review, String price)
    {
        ContentValues cv=new ContentValues();
        cv.put(TNAME,name);
        cv.put(TCONT,cont);
        cv.put(TLAT,lat);
        cv.put(TLNG,lng);
        cv.put(TADD,addres);
        cv.put(TDIST,dist);
        cv.put(TISOPEN,isopen);
        cv.put(TOPENTILL,opentill);
        cv.put(TRATING,rating);
        cv.put(TREVIEW,review);
        cv.put(TPRICE,price);
        sqldb.insert(TempPlace,null,cv);
    }

    public String getdbdata(String limit)
    {
        String s="";
        int i=0;
        Cursor c=sqldb.query(TempPlace,new String[]{TNAME,TCONT,TLAT,TLNG,TADD,TDIST,TISOPEN,TOPENTILL,TRATING,TREVIEW,TPRICE},null,null,null,null,TRATING + " DESC",limit);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do
            {
                s+=c.getString(0)+"*"+c.getString(1)+"*"+c.getString(2)+"*"+c.getString(3)+"*"+c.getString(4)+"*"+c.getString(5)+"*"+c.getString(6)+"*"+c.getString(7)+"*"+c.getFloat(8)+"*"+c.getString(9)+"*"+c.getString(10)+"*#";
            }
            while (c.moveToNext());
            i=1;
        }
        else
        {
            s="na";
        }

        if(i==1)
        {
            sqldb.delete(TempPlace,null,null);
        }

        return s;
    }


}
