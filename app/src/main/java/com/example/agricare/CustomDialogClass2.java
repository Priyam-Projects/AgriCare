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
import android.widget.EditText;
import android.widget.TextView;

import com.example.agricare.R;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CustomDialogClass2 extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    private Button yes, no;
    public int index ;
    private ProgressDialog progressDialog ;

    public CustomDialogClass2(Activity a,int index_here) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        index = index_here ;

        Log.d("ayush", "dialogue to bangaya: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_warehouse_dialogue_xml);
        yes = (Button) findViewById(R.id.btn_submit);
        no = (Button) findViewById(R.id.btn_dismiss);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    public void submit_it(){

        JSONObject postData = new JSONObject() ;

        try {

            Log.d("mytag", "submit_it: me agayaa ");

            EditText editText = (EditText)findViewById(R.id.amount_of_crop) ;

            String s1 = editText.getText().toString() ;

            int amount = Integer.parseInt(s1) ;

            postData.put("index", index);
            postData.put("added", amount);

            Log.d("mytag", "request bheja  ");

            new SendDeviceDetails().execute(" https://agricare-backend-server.herokuapp.com/warehouseUpdate", postData.toString());

        }catch (Exception e){

        }


    }


    private class SendDeviceDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            Log.d("mytag", "onPreExecute: ");


            super.onPreExecute();

            Log.d("mytag", "dialog shown: ");


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
                Log.d("mytag", "yo bro here result " + data );

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

            Log.d("mytag", "over : ");

            MainActivity a = MainActivity.getInstance();

            a.on_WAREHOUSE_SELECT();

            //my_progress.dismiss();

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit_it() ;
                break;
            case R.id.btn_dismiss:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }



}