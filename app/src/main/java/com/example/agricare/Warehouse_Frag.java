package com.example.agricare;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Warehouse_Frag extends Fragment {

    public ArrayList<WAREHOUSE_CLASS> arrayList_warehouse = new ArrayList<>() ;
    public String result2 ;
    private ProgressDialog progressDialog ;
    private View rootView ;
    private Map<Integer, Integer> myMap2 = new HashMap<Integer, Integer>();
    private int check1 ;
    private double l_lat,l_long ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_warehouse_, container, false);

        myMap2.put(3636,0) ;
        myMap2.put(987,1) ;
        myMap2.put(13307,2) ;
        myMap2.put(5500,3) ;
        myMap2.put(3636,4) ;
        myMap2.put(9735,5) ;
        myMap2.put(6643,6) ;
        myMap2.put(6645,7) ;
        myMap2.put(8021,8) ;
        myMap2.put(7360,9) ;
        myMap2.put(2173,10) ;
        myMap2.put(5501,11) ;
        myMap2.put(13560,12) ;
        myMap2.put(5502,13) ;
        myMap2.put(15500,14) ;
        myMap2.put(9500,15) ;
        myMap2.put(8500,16) ;
        myMap2.put(11525,17) ;
        myMap2.put(11450,18) ;
        myMap2.put(22641,19) ;
        myMap2.put(95450,20) ;
        myMap2.put(11451,21) ;

        check1 =0 ;

        getLocation() ;

        return rootView ;

    }

    private void call_it(){

        JSONObject postData = new JSONObject();
        try {
            Log.d("mytag", "my lat and long " + l_lat + " hey" + l_long );
            postData.put("lat", l_lat);
            postData.put("long", l_long);
            Log.d("mytag", "send device details ");
            new SendDeviceDetails().execute("https://agricare-backend-server.herokuapp.com/warehouses",postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getLocation(){



        GpsTracker gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            l_lat = gpsTracker.getLatitude();
            l_long = gpsTracker.getLongitude();

            call_it();

        } else {
            gpsTracker.showSettingsAlert();
        }


    }

    private class SendDeviceDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity()) ;
            progressDialog.setMessage("Getting Warehouses near your location..");
            progressDialog.show();


        }

        @Override
        protected Void doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                if(check1==0) {

                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    data = "";

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                    result2 = data;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

                Extract_warehouses() ;

            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            display_it() ;


        }
    }

    private void display_it(){

        ListView listView = rootView.findViewById(R.id.list_of_warehouse) ;

        Warehouse_Adapter warehouse_adapter = new Warehouse_Adapter(getActivity(),arrayList_warehouse) ;

        listView.setAdapter(warehouse_adapter);

    }

    private void Extract_warehouses(){

        try {

            JSONObject jsonObject = new JSONObject(result2);

            JSONArray jsonArray = jsonObject.getJSONArray("data" );

            for(int i=0;i<Math.min(10,jsonArray.length());i++){

                JSONObject jsonObject2 = jsonArray.getJSONObject(i) ;

                String location = jsonObject2.getString("Place") ;
                int total = jsonObject2.getInt("capacity") ;
                int avail = jsonObject2.getInt("current_capacity") ;
                int distance = jsonObject2.getInt("distance_from_me") ;

                WAREHOUSE_CLASS warehouse_class = new WAREHOUSE_CLASS(location,total,avail,distance) ;

                arrayList_warehouse.add(warehouse_class) ;

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class Warehouse_Adapter extends ArrayAdapter<WAREHOUSE_CLASS> {


        public Warehouse_Adapter(Context context, ArrayList<WAREHOUSE_CLASS> Warehouses) {

            super(context, 0, Warehouses);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View ListView = convertView ;

            final WAREHOUSE_CLASS temp = getItem(position) ;

            if( ListView == null ){
                ListView = LayoutInflater.from(getContext()).inflate(R.layout.warehouse_adpater, parent, false);
            }



            TextView textView = (TextView)ListView.findViewById(R.id.location_of_warehouse) ;

            textView.setText("Location : " +  (temp.getLocation().substring(0, Math.min(20,temp.getLocation().length()-1 ) ) ) +  "...");


            textView = (TextView)ListView.findViewById(R.id.total_capacity) ;

            double total_capac = temp.getTotal() ;

            String s5 = Double.toString(total_capac) ;

            textView.setText("Total :  " + s5 );


            textView = (TextView)ListView.findViewById(R.id.avail_capacity) ;

            double avail_capac = temp.getAvail() ;

            s5 = Double.toString(total_capac-avail_capac) ;

            textView.setText("Avail : " + s5 );



            textView = (TextView)ListView.findViewById(R.id.distance) ;

            double distance_val = temp.getDistance();

            s5 = Double.toString(distance_val) ;

            textView.setText( "Distance : " + s5 + " km" );

            ListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onWarehouseClick(temp.getTotal());


                }
            });


            return ListView ;

        }
    }

    private void onWarehouseClick(int capac){

        Log.d("ayush", "onWarehouseClick: ");

        CustomDialogClass2 customDialogClass2 = new CustomDialogClass2(getActivity(),myMap2.get(capac)) ;

        customDialogClass2.show();

    }







}
