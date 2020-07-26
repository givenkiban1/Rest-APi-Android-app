package com.example.funrestapi;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private DatePickerDialog pickerFrom;
    private DatePickerDialog pickerTo;
    private String dateFromPicker = "";
    private String dateToPicker = "";

    private static final String TAG = "MainActivity";

    public static String toPrettyFormat(JSONArray json)
    {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    public static String toPrettyFormat(JSONObject json)
    {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //making the app full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        final TextView country_name = findViewById(R.id.editText1);
        final TextView output = findViewById(R.id.editText5);

        final TextView dateFrom = findViewById(R.id.editText2);
        final TextView dateTo = findViewById(R.id.editText3);


        dateFrom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        pickerFrom = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        dateFrom.setText(
                                                year+ "-" + ((monthOfYear + 1) <10 ? "0"+(monthOfYear + 1): (monthOfYear + 1))

                                                        + "-" + (dayOfMonth<10? "0"+dayOfMonth: dayOfMonth)
                                        );

                                        dateFromPicker = (
                                                year+ "-" + ((monthOfYear + 1) <10 ? "0"+(monthOfYear + 1): (monthOfYear + 1))

                                                        + "-" + (dayOfMonth<10? "0"+dayOfMonth: dayOfMonth)
                                                );
                                    }
                                }, year, month, day);

                        pickerFrom.getDatePicker().setMinDate((new Date("01/01/2020")).getTime());
                        pickerFrom.getDatePicker().setMaxDate(System.currentTimeMillis());
                        pickerFrom.show();


                    }
                }
        );


        dateTo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        pickerTo = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        dateTo.setText(
                                                year+ "-" + ((monthOfYear + 1) <10 ? "0"+(monthOfYear + 1): (monthOfYear + 1))

                                                        + "-" + (dayOfMonth<10? "0"+dayOfMonth: dayOfMonth)
                                        );
                                        dateToPicker = (
                                                year+ "-" + ((monthOfYear + 1) <10 ? "0"+(monthOfYear + 1): (monthOfYear + 1))

                                                        + "-" + (dayOfMonth<10? "0"+dayOfMonth: dayOfMonth)
                                        );
                                    }
                                }, year, month, day);

                        pickerTo.getDatePicker().setMinDate((new Date("01/01/2020")).getTime());
                        pickerTo.getDatePicker().setMaxDate(System.currentTimeMillis());
                        pickerTo.show();


                    }
                }
        );




        final Button makeReq = findViewById(R.id.button);

        final Button availCountries = findViewById(R.id.button2);

        final Button timeLineOfCountry = findViewById(R.id.button3);
        
        final Button allCasesWW = findViewById(R.id.button4);

        final Button clearDate1 = findViewById(R.id.button5);

        final Button clearDate2 = findViewById(R.id.button6);
        

        clearDate1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateFrom.setText("");
                        dateFromPicker = "";
                    }
                }
        );

        clearDate2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateTo.setText("");
                        dateToPicker = "";
                    }
                }
        );




        makeReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String country = country_name.getText().toString();
                country = country.replace(" ","-").toLowerCase();


                //if no country entered, make it global
                //if no start date entered, only make it for all records from 01/01/2020 until end date
                //if no end date, then make it for all records from start date until today
                //https://api.covid19api.com/total/country/za/status/confirmed?from=2020-03-01&to=2020-04-01

                Log.e("date value-1", dateFromPicker);
                Log.e("date value-2", dateToPicker);



                String url  ="";

                if (dateToPicker!="" & dateFromPicker!=""){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
                    url = "https://api.covid19api.com/total/country/"+ country +"/status/confirmed?from="+

                            dateFromPicker
                            +"&to="+
                            dateToPicker
                    ;
                    Log.d(TAG, "onClick: "+ url);
                }
                else
                if (dateFromPicker=="" && dateToPicker!=""){

                    url = "https://api.covid19api.com/total/country/"+ country +"/status/confirmed?from=01-01-2020&to="+dateToPicker;
                    Log.d(TAG, "onClick: "+ url);
                }
                else
                if (dateFromPicker!="" && dateToPicker==""){
                    //https://api.covid19api.com/total/country/za/status/confirmed?from=2020-01-01&to=2020-06-01
                    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

                    String date = dateFormat.format(System.currentTimeMillis());

                    Log.e("date", date);

                    url = "https://api.covid19api.com/total/country/"+ country +"/status/confirmed?from="+dateFromPicker+"&to="+date;
                    Log.d(TAG, "onClick: "+ url);
                }
                else
                {
                    url = "https://api.covid19api.com/country/"+ country +"/status/confirmed";
                    Log.d(TAG, "onClick: "+ url);
                }

                Log.d(TAG, "onClick: "+ url);

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                JsonArrayRequest objectRequest = new JsonArrayRequest(
                        Request.Method.GET
                        ,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                String out = toPrettyFormat(response);
                                output.setText(out);

                                Log.d("onResponse: ", out);

                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                output.setText(error.toString());
                                Log.d("onError", error.toString());
                            }
                        }
                );


                requestQueue.add(objectRequest);
            }
        });

        availCountries.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //start

                        String url = "https://api.covid19api.com/countries";

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                        JsonArrayRequest objectRequest = new JsonArrayRequest(
                                Request.Method.GET
                                ,
                                url,
                                null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                        String out = toPrettyFormat(response);
                                        output.setText(out);

                                        Log.d("onResponse: ", out);

                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        output.setText(error.toString());
                                        Log.d("onError", error.toString());
                                    }
                                }
                        );


                        requestQueue.add(objectRequest);


                        //end
                    }
                }
        );

        timeLineOfCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String country = country_name.getText().toString();
                country = country.replace(" ","-").toLowerCase();

                String url = "https://corona.azure-api.net/timeline/"+ country;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                JsonArrayRequest objectRequest = new JsonArrayRequest(
                        Request.Method.GET
                        ,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                String out = toPrettyFormat(response);
                                output.setText(out);

                                Log.d("onResponse: ", out);

                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                output.setText(error.toString());
                                Log.d("onError", error.toString());
                            }
                        }
                );


                requestQueue.add(objectRequest);
            }
        });


        allCasesWW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://api.covid19api.com/world/total";

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET
                        ,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                String out = toPrettyFormat(response);
                                output.setText(out);

                                Log.d("onResponse: ", out);

                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                output.setText(error.toString());
                                Log.d("onError", error.toString());
                            }
                        }
                );


                requestQueue.add(objectRequest);
            }
        });


    }
}
