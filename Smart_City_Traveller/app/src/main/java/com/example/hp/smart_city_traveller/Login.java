package com.example.hp.smart_city_traveller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class Login extends AppCompatActivity{

    EditText email,pass;
    Button signin,signup;
    RelativeLayout ray;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String uid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("travel", Context.MODE_PRIVATE);
        uid=sp.getString("userid","");

        if(!weHavePermissionforGPS())
        {
            requestforGPSPermissionFirst();
        }

        if(uid.length()>0)
        {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else {

            setContentView(R.layout.login);
            ActionBar ab=getSupportActionBar();
            ab.setTitle("Smart City Traveller");
            email= (EditText) findViewById(R.id.lemail);
            pass= (EditText) findViewById(R.id.lpass);
            signin= (Button) findViewById(R.id.signin);
            signup= (Button) findViewById(R.id.signup);
            ray= (RelativeLayout) findViewById(R.id.ray);

            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(email.getText().toString().compareTo("")!=0 || pass.getText().toString().compareTo("")!=0)
                    {
                        if(email.getText().toString().compareTo("")!=0)
                        {
                            if(pass.getText().toString().compareTo("")!=0)
                            {
                                if(!weHavePermissionforGPS())
                                {
                                    requestforGPSPermissionFirst();
                                }
                                else {
                                    new logintask().execute(email.getText().toString(), pass.getText().toString());
                                }
                            }
                            else
                            {
                                pass.setError("Enter Password");
                                pass.requestFocus();
                            }
                        }
                        else
                        {
                            email.setError("Enter Email Address");
                            email.requestFocus();
                        }
                    }
                    else
                    {
                        Snackbar snack=Snackbar.make(ray,"Enter Your Credentials to Proceed",Snackbar.LENGTH_SHORT);
                        View vw=snack.getView();
                        TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                        txt.setTextColor(Color.RED);
                        snack.show();
                        email.requestFocus();
                    }
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Login.this,Register.class);
                    startActivity(i);
                }
            });

        }
    }

    public class logintask extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.login(params[0],params[1]);
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
            String b="post";
            b=s;
            if(b.compareTo("false")!=0)
            {
                if(b.length()<6)
                {
//                    Toast.makeText(Login.this,s, Toast.LENGTH_SHORT).show();
                    editor=sp.edit();
                    editor.putString("userid",b);
                    editor.commit();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(Login.this,s, Toast.LENGTH_SHORT).show();
                }
//                finish();
            }
            else
            {
                Snackbar snack=Snackbar.make(ray,"Invaid Credentails",Snackbar.LENGTH_SHORT);
                View v=snack.getView();
                TextView txt= (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                txt.setTextColor(Color.RED);
                snack.show();
                email.setText("");
                pass.setText("");
                email.requestFocus();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        email.setText("");
        pass.setText("");
    }

    private boolean weHavePermissionforGPS()
    {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestforGPSPermissionFirst()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
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
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 111);
    }
}
