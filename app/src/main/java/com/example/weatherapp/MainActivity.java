package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    TextView country_nm, city_nm,GetTimeAndDate, temp_vl, descp_nm, longiitude, latitude, feels_like,pressure, humidity, speed;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.CityName);
        button = findViewById(R.id.button);
        country_nm = findViewById(R.id.country);
        city_nm = findViewById(R.id.city);
        longiitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitute);
        temp_vl = findViewById(R.id.temp);
        descp_nm = findViewById(R.id.descp);
        imageView = findViewById(R.id.image);
        feels_like = findViewById(R.id.Feels);
        pressure = findViewById(R.id.Pressure);
        humidity = findViewById(R.id.Humidity);
        speed = findViewById(R.id.Wind);
        GetTimeAndDate = findViewById(R.id.dateTime);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy \nKK:mm:ss aaa", Locale.getDefault());
        Date = simpleDateFormat.format(calendar.getTime());

        editText.addTextChangedListener(textWatcher);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
                GetTimeAndDate.setText(Date);
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String CityName = editText.getText().toString().trim();
            button.setEnabled(!CityName.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

        public void findWeather()
        {

            final String city = editText.getText().toString();
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=60d0caa7b17d862079f680198ec596e1";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //calling API

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        //finding Country
                        JSONObject object1 = jsonObject.getJSONObject("sys");
                        String country_find = object1.getString("country");
                        country_nm.setText(country_find);


                        //Finding City
                        String city_find = jsonObject.getString("name");
                        city_nm.setText(city_find);

                        //Finding Longitude and Latitude
                        JSONObject objectLo = jsonObject.getJSONObject("coord");
                        double longitude_val = objectLo.getDouble("lon");
                        longiitude.setText(longitude_val+"° E");

                        JSONObject objectLa = jsonObject.getJSONObject("coord");
                        double latitude_val = objectLa.getDouble("lat");
                        latitude.setText(latitude_val+"° N");


                        //Finding Description
                        JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
                        JSONObject obj1 = jsonArray1.getJSONObject(0);
                        String descp = obj1.getString("description");
                        descp_nm.setText(descp);

                        //Finding Temperature
                        JSONObject object2 = jsonObject.getJSONObject("main");
                        String temp_find = object2.getString("temp");
                        temp_vl.setText(temp_find+" °C");

                        //Finding Icon

                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String icon = obj.getString("icon");
                        Picasso.get().load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

                        //Finding Feels Like
                        JSONObject object3 = jsonObject.getJSONObject("main");
                        int feels_find = object3.getInt("feels_like");
                        feels_like.setText(feels_find+" °C");

                        //Finding Pressure
                        double pressure_find = object3.getDouble("pressure");
                        pressure.setText(pressure_find+" hPa");

                        //Finding Humidity
                        double humidity_find = object3.getDouble("humidity");
                        humidity.setText(humidity_find+" %");

                        //Finding Wind Speed
                        JSONObject object4 = jsonObject.getJSONObject("wind");
                        double windSpeed_find = object4.getDouble("speed");
                        speed.setText(windSpeed_find*3.6+" kph");



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }

            });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }

}