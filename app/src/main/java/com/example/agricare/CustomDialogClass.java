package com.example.agricare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.example.agricare.R;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public String Crop_Name_here ;
    FragmentManager fm ;

    public CustomDialogClass(Activity a, String crop_name, FragmentManager fm1) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        Crop_Name_here = crop_name ;
        fm=fm1 ;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_crop_dialogue_xml);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    public void submit_it(){

        JSONObject postData = new JSONObject() ;

        try {

            postData.put("lat", (MainActivity.latitude));
            postData.put("long", MainActivity.longitude);
            postData.put("crop",Crop_Name_here.toLowerCase()) ;

            Log.d("select", "crop request bheja ");

            new SendDeviceDetails().execute("https://agricare-backend-server.herokuapp.com/selectedCrop", postData.toString());

        }catch (Exception e){

        }


    }


    private class SendDeviceDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.d("select", "crop request initiate hogaya ");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                Log.d("select", "connecting bro: ");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

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

                Log.d("select", "doInBackground: " + data);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("select", "aree yaha kyu aagaya catch me: ");

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
            Log.d("select", "onPostExecute: DONE BRO");

            MainActivity a = MainActivity.getInstance();

            a.on_HOME_SELECT();

            //my_progress.dismiss();

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                submit_it() ;
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }



}