package com.example.agricare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    static MainActivity instance ;
    public static int Main_count = 0 ;

    public TextView textView_for_home ;
    public TextView textView_for_warehouse ;
    public TextView textView_for_schemes ;

    public static double Nitrogen,pH,CEC,area,humidity,temperature,rainfall,latitude, longitude;

    int check1 = 0 ;
    int check2 = 0 ;

    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    public String JSONresponse ;
    ProgressDialog my_progress ;

    String url_to_fetch = null ;
    String result ;

    JSONObject jsonObject ;
    JSONArray jsonArray ;

    public static String STATE ;
    public static String DISTRICT ;

    public static ArrayList<CROP_CLASS> CROPS_LIST = new ArrayList<CROP_CLASS>();

    int count_of_crops = -1 ;

    int count6 = 0 ;

    Map<String, Integer> myMap = new HashMap<String, Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        textView_for_home = (TextView)findViewById(R.id.home_category) ;
        textView_for_warehouse = (TextView)findViewById(R.id.warehouse_category) ;
        textView_for_schemes = (TextView)findViewById(R.id.schemes_category) ;


        /*setContentView(R.layout.activity_main);


        ViewPager viewPager = (ViewPager)findViewById(R.id.myviewpager) ;

        Custom_Fragment_Page_Adapter custom_fragment_page_adapter = new Custom_Fragment_Page_Adapter(getSupportFragmentManager()) ;

        viewPager.setAdapter(custom_fragment_page_adapter);

         */

        instance=this ;

        myMap.put("rice",0) ;
        myMap.put("paddy",1);
        myMap.put("potato",2);
        myMap.put("brinjal",3);
        myMap.put("masur Dal",4);
        myMap.put("mustard",5);
        myMap.put("onion",6);
        myMap.put("pumpkin",7);
        myMap.put("green chilli",8);
        myMap.put("wheat",9) ;
        myMap.put("sweet pumpkin",10);
        myMap.put("mango",11);
        myMap.put("grapes",12);
        myMap.put("watermelon",13);
        myMap.put("arhar dal",14);
        myMap.put("bitter gourd",15);
        myMap.put("cauliflower",16);
        myMap.put("cucumbar",17);
        myMap.put("tomato",18);
        myMap.put("ginger",19);
        myMap.put("maize",20);


        //MOST IMP

        // We can access the fragment manager of the current activity by the below code

         getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new home_fragment())
                .commit(); // Replaces the current fragment with the new one


         textView_for_home.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 on_HOME_SELECT() ;

             }
         });

        textView_for_warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                on_WAREHOUSE_SELECT() ;

            }
        });

        textView_for_schemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                on_SCHEMES_SELECT() ;

            }
        });


        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new List_of_Schemes())
                .commit();

         */



        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new Warehouse_Frag())
                .commit();

         */



    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void on_HOME_SELECT(){

         CROPS_LIST.clear();

         count6=0;
         check1=0;
         check2=0;


        textView_for_home.setTextColor(Color.parseColor("#ffffff"));
        textView_for_schemes.setTextColor(Color.parseColor("#adaaaa"));
        textView_for_warehouse.setTextColor(Color.parseColor("#adaaaa"));



        FragmentManager fm = getFragmentManager();
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new home_fragment())
                .commit();
    }

    public void on_WAREHOUSE_SELECT(){

        textView_for_home.setTextColor(Color.parseColor("#adaaaa"));
        textView_for_schemes.setTextColor(Color.parseColor("#adaaaa"));
        textView_for_warehouse.setTextColor(Color.parseColor("#ffffff"));



        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new Warehouse_Frag())
                .commit();



    }

    public void on_SCHEMES_SELECT(){


        textView_for_home.setTextColor(Color.parseColor("#adaaaa"));
        textView_for_schemes.setTextColor(Color.parseColor("#ffffff"));
        textView_for_warehouse.setTextColor(Color.parseColor("#adaaaa"));


        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new List_of_Schemes())
                .commit();


    }

    public void go_to_best_crops(){

        JSONObject postData = new JSONObject();
        try {
            count6=0;
            postData.put("first", 1);
            postData.put("N", Nitrogen);
            postData.put("Temp", temperature);
            postData.put("Humidity", humidity);
            postData.put("pH", pH);
            postData.put("Rain", rainfall);
            postData.put("CEC",CEC) ;
            new SendDeviceDetails().execute("https://agricare-backend-server.herokuapp.com/SoilFactor",postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class SendDeviceDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(count6==0) {
                my_progress = new ProgressDialog(MainActivity.this);
                my_progress.setMessage("Predicting Best Crops");
                my_progress.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true); // this is set to true in order to output some body to the server

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                data = "" ;

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
                result = data ;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //my_progress.dismiss();
            if(count6==0)
                do_it() ;
            else {
                Extract_percentage_of_farmer extract_percentage_of_farmer = new Extract_percentage_of_farmer();
                extract_percentage_of_farmer.execute() ;
            }
        }
    }

    public class Extract_percentage_of_farmer extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            Log.d("mytag", "mereko raw json milgaya ");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Log.d("percentage", "raw json " +result );

                jsonObject = new JSONObject(result);

                Log.d("percentage", "json object milgaaya: ");

                jsonArray = jsonObject.getJSONArray("returnPercentage") ;

                Log.d("percentage", "json arrrayv milgaya: ");

                int size1=CROPS_LIST.size();

                for(int i=0;i<size1;i++){

                    CROP_CLASS crop_class = CROPS_LIST.get(i) ;

                    String crop_name = crop_class.getName().toLowerCase() ;

                    Log.d("percentage", "searching for : " + crop_name + "with index " + myMap.get(crop_name)  );

                    String d3 = jsonArray.getString(myMap.get(crop_name).intValue()) ;

                    int d5 ;

                    if(d3=="null")d5=(int)0;

                    else d5 = (Integer.parseInt(d3)) ;

                    Log.d("percentage", "yo bro working "+crop_name+" iska percentage = " + d5 );

                    crop_class.setPercentage_of_farmer(d5);

                    Log.d("are bhai", "aaya toh yaha ");

//                    if(MainActivity.Main_count==1 && crop_name=="paddy" )crop_class.setPercentage_of_farmer(100);
//
//                    if( MainActivity.Main_count==2 && crop_name=="rice" ){
//                        crop_class.setPercentage_of_farmer(50);
//
//                    }
//
//                    if( MainActivity.Main_count==2 && crop_name=="paddy" ){
//                        crop_class.setPercentage_of_farmer(50);
//
//                    }

                    CROPS_LIST.set(i,crop_class) ;



                }

            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("percentage", "mai fragment call kardiya ");
            call_the_fragment();
        }
    }

    public void do_it(){
        Get_crop_list get_crop_list = new Get_crop_list();
        get_crop_list.execute() ;
    }


    public void get_live_price1(){

        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            if (geocoder.isPresent()) {
                StringBuilder stringBuilder = new StringBuilder();
                if (addresses.size()>0) {
                    Address returnAddress = addresses.get(0);

                     DISTRICT = returnAddress.getLocality();
                    //String name = returnAddress.getFeatureName();
                    //String subLocality = returnAddress.getSubLocality();
                    //String country = returnAddress.getCountryName();
                    //String region_code = returnAddress.getCountryCode();
                    //String zipcode = returnAddress.getPostalCode();
                    STATE = returnAddress.getAdminArea();
                    Log.d("mytag", STATE +" hrelllo   "+DISTRICT);

                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONresponse = "" ;
        check1 = 2 ;
        url_to_fetch = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd0000016d46c71ac6174dcc4c6ca15c50ec9c05&format=json&offset=0&limit=10000" ;

        FetchAsyncTask fetchAsyncTask = new FetchAsyncTask() ;
        fetchAsyncTask.execute() ;

    }

    public void percentage_of_farmers(){

        count6=1 ;


        JSONObject postData = new JSONObject() ;

        try {

            postData.put("lat", latitude);
            postData.put("long",longitude);

            Log.d("mytag", "yaha se mai call kardiya  ");


            new SendDeviceDetails().execute("https://agricare-backend-server.herokuapp.com/simplePercentage", postData.toString());



        }catch (Exception e){
            e.printStackTrace();
        }


        Log.d("mytag", "dismiss kardiya isko : ");



    }

    public void call_the_fragment(){

        my_progress.dismiss();

        Log.d("myatag", "call_the_fragment: yaha iska size hai  " + CROPS_LIST.size());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new list_of_crops_frag())
                .commit();

    }







    public void Extract_live_price() throws JSONException {

        jsonObject = new JSONObject(JSONresponse) ;

        jsonArray = jsonObject.getJSONArray("records") ;

        int size1 = CROPS_LIST.size();

        Log.d("mytag", "here its is : " + size1);

        for(int i=0;i<size1;i++){

            CROP_CLASS crop = CROPS_LIST.get(i) ;

            String temp_name = crop.getName().toLowerCase() ;
            String temp_state = STATE.toLowerCase();

            String temp_district = DISTRICT.toLowerCase();

            double price1=0,price2=0,price3=0;

            for(int j=0;j<jsonArray.length();j++){

                JSONObject jsonObject2 = jsonArray.getJSONObject(j) ;

                String comm = jsonObject2.getString("commodity").toLowerCase() ;

                if( comm.contains(temp_name) == FALSE )continue;

                String state_name = jsonObject2.getString("state").toLowerCase() ;
                String distric_name = jsonObject2.getString("district").toLowerCase() ;
                double price_here = jsonObject2.getDouble("modal_price") ;

                if(state_name.equals(temp_state))price2= price_here   ;

                if(distric_name.equals(temp_district)){
                    price3 = price_here ;
                    break;
                }

                price1 = price_here ;

                Log.d("mytag", " setting crop live price " + comm +" "+state_name+"here it is "+distric_name );

            }

            if(price3 != 0)crop.setLive_price(price3);

            else if( price2 != 0 )crop.setLive_price(price2);

            else crop.setLive_price(price1);

            CROPS_LIST.set(i,crop) ;

            Log.d("mytag", " "+Double.toString(i));

        }



    }





    public void go_to_soil_info(double area_val) throws JSONException {

        area = area_val ;

        //call soil and climate info

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        latitude = 0 ;
        longitude = 0 ;

        getLocation();

        //longitude = -64 ;
        //latitude = 45 ;

        //call the other fragment

        Log.d("mytag", "we got it bro : ");

    }

    public void getLocation(){



            GpsTracker gpsTracker = new GpsTracker(MainActivity.this);
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                check2=1;
                get_weather();
            } else {
                gpsTracker.showSettingsAlert();
            }


        Log.d("mytag", "getLocation: mil gaya  "+Double.toString(longitude) );

    }

    public void get_weather(){

        check1=0;

        JSONresponse = "" ;

        String long_in_string = Double.toString(longitude) ;
        String lati_in_string = Double.toString(latitude) ;

        url_to_fetch = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lati_in_string + "&lon=" + long_in_string + "&exclude=current,minutely,hourly,alerts&appid=63878412f036d4dea1bd97d9f871193c" ;

        FetchAsyncTask fetchAsyncTask = new FetchAsyncTask() ;
        fetchAsyncTask.execute() ;

    }

    public void get_soil(){

        check1=1;

        JSONresponse = "" ;

        //changing url to fetch
        String long_in_string = Double.toString(longitude) ;
        String lati_in_string = Double.toString(latitude) ;

        url_to_fetch = "https://rest.soilgrids.org/soilgrids/v2.0/properties/query?lon="+long_in_string+"&lat=" +lati_in_string + "&property=cec&property=nitrogen&property=phh2o" + "&depth=0-5cm&value=mean" ;

        Log.d("mytag", "get_soil: " + url_to_fetch);

        FetchAsyncTask fetchAsyncTask = new FetchAsyncTask() ;
        fetchAsyncTask.execute() ;

    }


    public void Extract_Json_Soil() throws JSONException {


        try {

            Log.d("mytag", "Extract_Json_Soil: " + JSONresponse );

            jsonObject = new JSONObject(JSONresponse);

            Log.d("mytag", "bol ab: ");

            jsonObject = jsonObject.getJSONObject("properties");

            jsonArray = jsonObject.getJSONArray("layers");

            jsonObject = jsonArray.getJSONObject(0);

            jsonArray = jsonObject.getJSONArray("depths");

            jsonObject = jsonArray.getJSONObject(0);

            jsonObject = jsonObject.getJSONObject("values");

            String temp1 = jsonObject.getString("mean") ;

            if(temp1=="null")CEC=15 ;

            else CEC = jsonObject.getDouble("mean");



            Log.d("mytag", " " + Double.toString(CEC) );


            jsonObject = new JSONObject(JSONresponse);

            jsonObject = jsonObject.getJSONObject("properties");

            jsonArray = jsonObject.getJSONArray("layers");

            jsonObject = jsonArray.getJSONObject(1);

            jsonArray = jsonObject.getJSONArray("depths");

            jsonObject = jsonArray.getJSONObject(0);

            jsonObject = jsonObject.getJSONObject("values");

            temp1 = jsonObject.getString("mean") ;

            if(temp1=="null")Nitrogen=75 ;

            else Nitrogen = jsonObject.getDouble("mean");


            jsonObject = new JSONObject(JSONresponse);

            jsonObject = jsonObject.getJSONObject("properties");

            jsonArray = jsonObject.getJSONArray("layers");

            jsonObject = jsonArray.getJSONObject(2);

            jsonArray = jsonObject.getJSONArray("depths");

            jsonObject = jsonArray.getJSONObject(0);

            jsonObject = jsonObject.getJSONObject("values");

            temp1 = jsonObject.getString("mean") ;

            if(temp1=="null")pH=85 ;

            else pH = jsonObject.getDouble("mean");

            pH = pH/10.0 ;



        }catch(Exception e){

            Log.d("mytag", "chutiyapa: ");

        }










    }

    public void Extract_Json_Weather() throws JSONException {

        try {
            double humidity1 = 0, temperature1 = 0, rain1 = 0;

            jsonObject = new JSONObject(JSONresponse);

            jsonArray = jsonObject.getJSONArray("daily");

            for (int i = 0; i < 8; i++) {

                jsonObject = jsonArray.getJSONObject(i);

                JSONObject temp = jsonObject.getJSONObject("temp");

                temperature1 += (temp.getDouble("day"));

                humidity1 += (jsonObject.getDouble("humidity"));

                if (jsonObject.has("rain")) rain1 += (jsonObject.getDouble("rain"));

            }

            humidity = humidity1 / 8.0;
            temperature = temperature1 / 8.0;
            rainfall = rain1 / 8.0;

            rainfall = rainfall * 60  ;
            temperature = temperature-273 ;



        }
        catch (Exception e){
            Log.d("mytag", "HAGG DIYA IN WEATHER EXTRACTION ");
        }

    }



    public class Get_crop_list extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                jsonObject = new JSONObject(result) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                jsonArray = jsonObject.getJSONArray("final_crop_list") ;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i=0;i<jsonArray.length();i++){
                try {
                    jsonObject = jsonArray.getJSONObject(i) ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String name_of_crop = null;
                try {
                    name_of_crop = jsonObject.getString("crop");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                double expected_price = 0;
                try {
                    expected_price = jsonObject.getDouble("Expected_price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CROPS_LIST.add( new CROP_CLASS(name_of_crop,34,expected_price,0) ) ;

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);


            Log.d("mytag", "no of crops: " + CROPS_LIST.size() );

            get_live_price1() ;
        }
    }

    private class FetchAsyncTask extends AsyncTask<Void,Void,Void> {



        @Override
        protected void onPreExecute() {
            if(check1==0 || check1==1 )my_progress = new ProgressDialog(MainActivity.this) ;
            if(check1==0)my_progress.setMessage("Fetching Weather...");
            else if(check1==1) my_progress.setMessage("Fetching Soil...");

            if(check1==0 || check1==1 )my_progress.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                //calling methods from within doInBackground forces those methods to be executed in the same background thread

                 URL url = new URL(url_to_fetch) ;

                 JSONresponse = "" ;

                 makeHTTPrequest(url) ;
                 Log.d("mytag", "isko kar ab: ");

                 if(check1==1)Extract_Json_Soil();
                 else if(check1==0) Extract_Json_Weather();
                 else Extract_live_price();

                 if(check1==2) Log.d("mytag", "doInBackgr ye wala crop list modify hogaya" );


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if(check1==0 || check1==1)my_progress.dismiss();

            if(check1==0)get_soil();

            else if(check1==1)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Soil_info_frag())
                    .commit();

            else {

                Log.d("mytag", "maiine call kardiya percentage of farmer ko: ");



                percentage_of_farmers() ;

            }

        }
    }

    public void makeHTTPrequest(URL url) throws IOException {

        HttpURLConnection urlConnection = null ;
        InputStream inputStream = null ;

        urlConnection = (HttpURLConnection)url.openConnection() ;
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(10000);
        urlConnection.connect();

        inputStream = urlConnection.getInputStream() ;

        Log.d("mytag", "ye hogaya bhai: ");

        readfromstream(inputStream) ;


    }

    public void readfromstream(InputStream inputStream) throws IOException {

        StringBuilder my_string_builder = new StringBuilder() ;

        if( inputStream != null ){

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8")) ;

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader) ;

            String each_line = bufferedReader.readLine() ;

            while(each_line != null){
                //Log.d("mytag", "ye hogaya bhai: " + each_line);
                my_string_builder.append(each_line) ;
                each_line = bufferedReader.readLine() ;
            }


        }

        JSONresponse = my_string_builder.toString();

    }








}
