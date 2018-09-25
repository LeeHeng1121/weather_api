package com.example.abc17034.weather;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MainActivity extends Activity{
    String place = "";
    String apikey1 = "0zvNe4pKd5idXlEQdjsQ3qoMAAjSSK1I";
    String apikey2 = "ewaV64q2MWmezSaXCIj6oi160pxMGejb";
    String apikey=apikey1;
    String location_code_taipei = "4-315078_1_AL";
    String location_code_vietnam = "1-353981_1_AL";
    String location_code_yokohama = "2383413";
    String location_code_seattle = "331424";
    String location_code="";
    String ENDPOINT= "";
    final String tag = "MainActivity";

    int flag=0;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

    }


    private void fetchPosts(int i) {


        if (i==1){
            ENDPOINT= "http://dataservice.accuweather.com/currentconditions/v1/"+location_code+"?apikey=" + apikey +"&language=en";
        }else if (i==2){
            ENDPOINT = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + location_code + "?apikey=" + apikey + "&language=en&metric=true";
        }
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);


    }



    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onResponse(String response){
            Log.i("PostActivity",response);
                try {
                    JSONArray jArr = new JSONArray(response);
                    JSONObject jo = jArr.getJSONObject(0);

                    System.out.println("LocalObservationDateTime:" + jo.getString("LocalObservationDateTime"));
                    TextView tmp = findViewById(R.id.time);
                    SimpleDateFormat timetmp = new SimpleDateFormat(jo.getString("LocalObservationDateTime"));
                    String inputPattern = "yyyy-MM-dd'T'hh:mm:ssX";
                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                    Date date = null;
                    try {
                        date = inputFormat.parse(jo.getString("LocalObservationDateTime"));
                        tmp.setText(date.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("WeatherText:" + jo.getString("WeatherText"));
                    tmp = findViewById(R.id.weather);
                    tmp.setText(jo.getString("WeatherText"));
                    JSONObject Temperature = jo.getJSONObject("Temperature");
                    JSONObject Metric = Temperature.getJSONObject("Metric");
                    System.out.println("Temperature:" + Metric.getString("Value"));
                    tmp = findViewById(R.id.temperature);
                    tmp.setText(Metric.getString("Value"));
                    tmp = findViewById(R.id.place);
                    tmp.setText(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject Temperature_jo = new JSONObject(response);
                    Observable<JSONObject> observable = Observable.just(Temperature_jo);
                    Observer<JSONObject> observer = new Observer<JSONObject>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JSONObject jsonObject) {
                            Log.d(tag, "Item: " + jsonObject.toString());
                            TextView tmp;
                            try {
                                JSONArray DailyForecasts = jsonObject.getJSONArray("DailyForecasts");
                                JSONObject jo2 = DailyForecasts.getJSONObject(0);
                                JSONObject Temperature = jo2.getJSONObject("Temperature");
                                final JSONObject Minimum = Temperature.getJSONObject("Minimum");
                                final JSONObject Maximum = Temperature.getJSONObject("Maximum");
                                tmp = findViewById(R.id.forcastText);
                                JSONObject Headline = jsonObject.getJSONObject("Headline");
                                tmp.setText(Headline.getString("Text"));
                                tmp = findViewById(R.id.min);
                                tmp.setText(Minimum.getString("Value"));
                                tmp = findViewById(R.id.max);
                                tmp.setText(Maximum.getString("Value"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(tag, "Error!");
                        }

                        @Override
                        public void onComplete() {

                        }

                    };

                    observable.subscribe(observer);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Response.ErrorListener", error.toString());
        }
    };


    public void buttonOnClick(View view) {
        switch (view.getId())
        {
            case R.id.button_seattle:
                place="Seattle";
                location_code= location_code_seattle;
                fetchPosts(1);
                fetchPosts(2);
                setContentView(R.layout.weather);
                break;
            case R.id.button_yokohama:
                place="Yokohama";
                location_code=location_code_yokohama;
                fetchPosts(1);
                fetchPosts(2);
                setContentView(R.layout.weather);
                break;
            case R.id.button_taipei:
                place="Taipei";
                location_code=location_code_taipei;
                fetchPosts(1);
                fetchPosts(2);
                setContentView(R.layout.weather);
                break;
//            case R.id.button_vietnam:
//                place="Vietnam";
//                location_code=location_code_vietnam;
//                fetchPosts(1);
//                fetchPosts(2);
//                setContentView(R.layout.weather);
//                break;
            case R.id.button_GoHome:
                setContentView(R.layout.activity_main);
                break;
            case R.id.button_api:
                if(apikey.equals(apikey1)){
                apikey=apikey2;
                Toast.makeText(this, "apikey2 loaded. \n "+ apikey, Toast.LENGTH_SHORT).show();
                }else{
                apikey=apikey1;
                Toast.makeText(this, "apikey1 loaded. \n "+ apikey, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}
