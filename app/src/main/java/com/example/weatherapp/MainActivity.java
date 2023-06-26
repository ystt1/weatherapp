package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout weatherHomeRl;
    private ProgressBar loadingWeatherPB;
    private TextView cityNameTV,temperatureTV,conditionTV;
    private RecyclerView weatherRV;
    private AutoCompleteTextView cityTIET;
    private TextInputLayout textInputLayout;
    private ImageView backgroundIV,iconIV,searchingIV;
    private ArrayList<WeatherModal> weatherModalArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE=1;
    private String curCity;

    String [] cityOfVietNam=new String[]{"An Giang","Bà Rịa - Vũng Tàu" , "Bắc Giang", "Bắc Kạn",
            "Bạc Liêu" ,
            "Bắc Ninh" ,
            "Bến Tre" ,
            "Bình Định",
            "Bình Dương" ,
            "Bình Phước" ,
            "Bình Thuận" ,
            "Cà Mau" ,
            "Cần Thơ" ,
            "Cao Bằng" ,
            "Đà Nẵng" ,
            "Đắk Lắk" ,
            "Đắk Nông" ,
            "Điện Biên" ,
            "Đồng Nai" ,
            "Đồng Tháp" ,
            "Gia Lai",
            "Hà Giang" ,
            "Hà Nam" ,
            "Hà Nội" ,
            "Hà Tĩnh" ,
            "Hải Dương" ,
            "Hải Phòng" ,
            "Hậu Giang" ,
            "Hòa Bình" ,
            "Hưng Yên" ,
            "Khánh Hòa" ,
            "Kiên Giang" ,
            "Kon Tum" ,
            "Lai Châu" ,
            "Lâm Đồng" ,
            "Lạng Sơn" ,
            "Lào Cai" ,
            "Long An" ,
            "Nam Định" ,
            "Nghệ An" ,
            "Ninh Bình" ,
            "Ninh Thuận",
            "Phú Thọ",
            "Phú Yên" ,
            "Quảng Bình" ,
            "Quảng Nam" ,
            "Quảng Ngãi" ,
            "Quảng Ninh" ,
            "Quảng Trị" ,
            "Sóc Trăng" ,
            "Sơn La" ,
            "Tây Ninh" ,
            "Thái Bình" ,
            "Thái Nguyên" ,
            "Thanh Hóa" ,
            "Thừa Thiên Huế" ,
            "Tiền Giang" ,
            "Thành phố Hồ Chí Minh" ,
            "Trà Vinh" ,
            "Tuyên Quang" ,
            "Vĩnh Long" ,
            "Vĩnh Phúc" ,
            "Yên Bái"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        weatherHomeRl=findViewById(R.id.idWeatherHomeRl);
        loadingWeatherPB=findViewById(R.id.progressBar);
        cityNameTV=findViewById(R.id.idTVCityNameBG);
        temperatureTV=findViewById(R.id.idTVND);
        conditionTV=findViewById(R.id.idTVKH);
        weatherRV=findViewById(R.id.idTTTheoNgay);
        cityTIET=findViewById(R.id.idTIETCity);
        backgroundIV=findViewById(R.id.idIVBG);
        iconIV=findViewById(R.id.IVKH);
        searchingIV=findViewById(R.id.idIVSearch);
        weatherModalArrayList = new ArrayList<>();
        weatherAdapter=new WeatherAdapter(this,weatherModalArrayList);
        weatherRV.setAdapter(weatherAdapter);
        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (  ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED
//        && ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
//
//        }
//        if ( ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
//                    PERMISSION_CODE );
//        }
//        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                curCity = getCurLocation(longi, lat);
            } else {
                Toast.makeText(this, "Không tìm thấy vị trí.", Toast.LENGTH_SHORT).show();
            }
        }

//        curCity=getCurLocation(location.getLongitude(),location.getLatitude());



        getApi(curCity);
        cityTIET.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,cityOfVietNam));
        searchingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=cityTIET.getText().toString();
                if(city.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Vui Lòng Nhập Tên Thành Phố!",Toast.LENGTH_SHORT).show();
                }
                else {
                    getApi(city);
                }
            }

        });

    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
    }
    private String getCurLocation(double longidude,double latitude)
    {
        String cityName = "";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude,longidude,10);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
            else
            {
                Log.d("TAG", "Không Tìm Thấy Vị Trí! ");
                Toast.makeText(this,"Không Tìm Thấy Vị Trí!",Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }
    public static String removeAccent(String s) {

        if (s == null) {
            return null;
        }
        String result = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISSION_CODE) {
            while(grantResults.length<0 || grantResults[0]!=PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this,"Vui lòng cấp quyền",Toast.LENGTH_SHORT).show();
                finish();
                }
            Toast.makeText(this,"Cấp quyền",Toast.LENGTH_SHORT).show();
        }
    }
    private void getApi(String cityName)
    {
        Log.e("cityName", cityName );
        String key="fed96f8301cd48c9a4f65203233004";
        String flag=cityName;
        if(cityName=="Thành phố Hồ Chí Minh") {
            flag = "Ho Chi Minh city";
        }
        String name=removeAccent(flag);


        weatherModalArrayList.clear();
        String url= "https://api.weatherapi.com/v1/forecast.json?key="+key+"&q="+name+"&days=1&aqi=no&alerts=no";
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cityNameTV.setText(cityName);
                try {

                    String temp=response.getJSONObject("current").getString("temp_c");
                    String conditionIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                    temperatureTV.setText(temp+"℃");
                    conditionTV.setText(response.getJSONObject("current").getJSONObject("condition").getString("text"));
                    JSONObject forecastObject=response.getJSONObject("forecast");
                    JSONObject forecast=forecastObject.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray=forecast.getJSONArray("hour");
                    for(int i=0;i<hourArray.length();i++)
                    {
                        JSONObject hourObj=hourArray.getJSONObject(i);
                        String time=hourObj.getString("time");
                        String temp_c=hourObj.getString("temp_c");
                        String icon=hourObj.getJSONObject("condition").getString("icon");
                        String windSpeed =hourObj.getString("wind_kph");
                        weatherModalArrayList.add(new WeatherModal(time,temp_c,icon,windSpeed));
                    }

                    weatherAdapter.notifyDataSetChanged();
                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", String.valueOf(error));
            }     });
        requestQueue.add(jsonObjectRequest);
    }


}