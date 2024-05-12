package com.example.hp.smart_city_traveller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Register extends AppCompatActivity {
    TableRow tbprog;
    TableLayout tablay;
    DatePickerDialog date;
    EditText name,dob,contact,email,pass,cpass;
    Button btn;
    RelativeLayout ray;
    String id;
    String q1="",q2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Register");

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        name= (EditText) findViewById(R.id.name);
        dob= (EditText) findViewById(R.id.dob);
        contact= (EditText) findViewById(R.id.cont);
        email= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.pass);
        cpass= (EditText) findViewById(R.id.cpass);
        btn= (Button) findViewById(R.id.submit);
        ray= (RelativeLayout) findViewById(R.id.ray);
        tbprog= (TableRow) findViewById(R.id.tbprog);
        tbprog.setVisibility(View.GONE);
        tablay= (TableLayout) findViewById(R.id.tablay);
        tablay.setVisibility(View.VISIBLE);

        new autoid().execute();
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().compareTo("")!=0 || dob.getText().toString().compareTo("")!=0 || contact.getText().toString().compareTo("")!=0 || email.getText().toString().compareTo("")!=0 || pass.getText().toString().compareTo("")!=0 || cpass.getText().toString().compareTo("")!=0)
                {
                    if(name.getText().toString().compareTo("")!=0)
                    {
                        if(dob.getText().toString().compareTo("")!=0)
                        {
                            if(contact.getText().toString().compareTo("")!=0)
                            {
                                if(email.getText().toString().compareTo("")!=0)
                                {
                                    if(pass.getText().toString().compareTo("")!=0)
                                    {
                                        if(cpass.getText().toString().compareTo("")!=0)
                                        {
                                            if(pass.getText().toString().compareTo(cpass.getText().toString())==0)
                                            {
                                               final Dialog d=new Dialog(Register.this);
                                                d.setCancelable(false);
                                                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                d.setContentView(R.layout.ques_dialog1);
                                                final TableLayout tabfood,tabdrinks;
                                                tabfood= (TableLayout) d.findViewById(R.id.tabfood);
                                                tabdrinks= (TableLayout) d.findViewById(R.id.tabdrink);
                                                RadioGroup rg= (RadioGroup) d.findViewById(R.id.rg);
                                                Button btn1= (Button) d.findViewById(R.id.dsubmit);
                                                RadioGroup rg1= (RadioGroup) d.findViewById(R.id.rg1);
                                                Button btn2= (Button) d.findViewById(R.id.ddsubmit);

                                                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                        if(checkedId==R.id.rv)
                                                        {
                                                            q1="veg";
                                                        }
                                                        else if(checkedId==R.id.rnv)
                                                        {
                                                            q1="nonveg";
                                                        }
                                                    }
                                                });

                                                rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                        if(checkedId==R.id.rd)
                                                        {
                                                            q2="yes";
                                                        }
                                                        else if(checkedId==R.id.rnd)
                                                        {
                                                            q2="no";
                                                        }
                                                    }
                                                });

                                                btn1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (q1.compareTo("") != 0) {
                                                            tabfood.setVisibility(View.GONE);
                                                            tabdrinks.setVisibility(View.VISIBLE);

                                                        } else {
                                                            Snackbar snack = Snackbar.make(ray, "Select Whether You are Vegeterian or Not!", Snackbar.LENGTH_LONG);
                                                            View vw = snack.getView();
                                                            TextView txt = (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                                                            txt.setTextColor(Color.RED);
                                                            snack.show();
                                                        }
                                                    }
                                                });

                                                btn2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if(q2.compareTo("")!=0)
                                                        {
                                                            new registertask().execute(id,name.getText().toString(),contact.getText().toString(),dob.getText().toString(),email.getText().toString(),pass.getText().toString(),q1,q2,"na");
                                                            d.cancel();
                                                            tablay.setVisibility(View.GONE);
                                                            tbprog.setVisibility(View.VISIBLE);

                                                        }
                                                        else
                                                        {
                                                            Snackbar snack=Snackbar.make(ray,"Select Whether You Drink or Not!",Snackbar.LENGTH_LONG);
                                                            View vw=snack.getView();
                                                            TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                                                            txt.setTextColor(Color.RED);
                                                            snack.show();
                                                        }
                                                    }
                                                });
                                                d.show();
                                            }
                                            else
                                            {
                                                pass.setText("");
                                                cpass.setText("");
                                                pass.requestFocus();
                                                Snackbar snack=Snackbar.make(ray,"Passwords dont Match!!",Snackbar.LENGTH_SHORT);
                                                View vw=snack.getView();
                                                TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                                                txt.setTextColor(Color.RED);
                                                snack.show();
                                            }
                                        }
                                        else
                                        {
                                            cpass.setError("Re-Enter Password");
                                            cpass.requestFocus();
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
                                contact.setError("Enter Contact Number");
                                contact.requestFocus();
                            }
                        }
                        else
                        {
                            dob.setError("Enter Date of Birth");
                            dob.requestFocus();
                        }
                    }
                    else
                    {
                        name.setError("Enter Name");
                        name.requestFocus();
                    }
                }
                else
                {
                    Snackbar snack=Snackbar.make(ray, "All Fields are Mandatory!",Snackbar.LENGTH_SHORT);
                    View vw=snack.getView();
                    TextView txt= (TextView) vw.findViewById(android.support.design.R.id.snackbar_text);
                    txt.setTextColor(Color.RED);
                    snack.show();
                    name.requestFocus();
                }

            }
        });

        email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new emailcheck().execute(email.getText().toString());
            }
        });


        Calendar newCalendar = Calendar.getInstance();
        date = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String dd=dateFormatter.format(newDate.getTime());
                dob.setText(dd);
                //Toast.makeText(Register.this, dd, Toast.LENGTH_SHORT).show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

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

    public class registertask extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.register(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8]);
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
            if(b.compareTo("true")==0)
            {
                finish();
            }
            else if(b.compareTo("false")==0)
            {
                Toast.makeText(Register.this, "Problem in Registering", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Register.this,b, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class autoid extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.getuid();
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
            id=b;
            //Toast.makeText(Register.this,id, Toast.LENGTH_SHORT).show();
        }
    }

    public class emailcheck extends AsyncTask<String,JSONObject,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String a="back";
            RestAPI api=new RestAPI();
            try {
                JSONObject json=api.checkemail(params[0]);
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
            if(b.compareTo("yes")==0)
            {
                email.setText("");
                email.setError("Already Exists");
                email.requestFocus();
            }
        }
    }
}
