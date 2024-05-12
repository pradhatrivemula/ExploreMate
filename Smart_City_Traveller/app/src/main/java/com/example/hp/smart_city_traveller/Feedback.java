package com.example.hp.smart_city_traveller;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by hp on 8/22/2016.
 */
public class Feedback extends Fragment{
    RelativeLayout ray;
    EditText txt;
    Button submit;
    Calendar c;
    String date="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.feedback,null,false);
        ray= (RelativeLayout) v.findViewById(R.id.ray);
        txt= (EditText) v.findViewById(R.id.feedtxt);
        submit= (Button) v.findViewById(R.id.feedbtn);
        c=Calendar.getInstance();
        int d=c.get(Calendar.DAY_OF_MONTH);
        int m=(c.get(Calendar.MONTH)+1);
        int y=c.get(Calendar.YEAR);
        date=y+"-"+m+"-"+d;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt.getText().toString().compareTo("")!=0)
                {
                    new feed().execute("1234",txt.getText().toString(),date);
                }
                else
                {
                    Snackbar snack=Snackbar.make(ray,"Enter Feedback",Snackbar.LENGTH_SHORT);
                    View sv=snack.getView();
                    TextView t= (TextView) sv.findViewById(android.support.design.R.id.snackbar_text);
                    t.setTextColor(Color.RED);
                    snack.show();
                    txt.requestFocus();
                }
            }
        });
        return v;
    }

    public class feed extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.feed(params[0],params[1],params[2]);
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
            if(s.compareTo("true")==0)
            {
                txt.setText("");
                Snackbar snack=Snackbar.make(ray,"Feed Submitted",Snackbar.LENGTH_SHORT);
                View v=snack.getView();
                TextView t= (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                t.setTextColor(Color.GREEN);
                snack.show();
            }
            else
            {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
