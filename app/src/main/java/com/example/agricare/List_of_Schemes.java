package com.example.agricare;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * A simple {@link Fragment} subclass.
 */
public class List_of_Schemes extends Fragment {


    public int check_final = 0 ;

    private  ProgressDialog progressDialog1 ;

    private ArrayList<SCHEME_CLASS> arrayList = new ArrayList<SCHEME_CLASS>() ;

    private View rootView;

    private ArrayList<SCHEME_CLASS> arrayList2 = new ArrayList<SCHEME_CLASS>() ;

    private ArrayList<String> url_array = new ArrayList<String>() ;

    private ArrayList<String> des_array = new ArrayList<String>() ;

    String second_url ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("mytag", "mai yaha entry karliya: ");

        rootView = inflater.inflate(R.layout.list_view_schemes_xml, container, false);

        Log.d("mytag", "xml load karliya ");
        Fetch_Schemes fetch_schemes = new Fetch_Schemes();
        fetch_schemes.execute() ;
        return rootView;

    }

    private void call_the_list_view(){

        Log.d("mytag", "list view me agaya ");

        Log.d("mytag", ""+arrayList.size()+arrayList.toString());

        ListView listView = (ListView)rootView.findViewById(R.id.list_view_schemes) ;

        SCHEME_ADAPTER scheme_adapter = new SCHEME_ADAPTER(getActivity(),arrayList) ;

        listView.setAdapter(scheme_adapter);

        Log.d("mytag", "adapter attach hogaya ");

    }

    private class Fetch_Schemes extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            Log.d("mytag", "just before progress dialogue: ");
            progressDialog1 = new ProgressDialog(getActivity()) ;
            progressDialog1.setMessage("Fetching Latest Schemes");
            progressDialog1.show();

            Log.d("mytag", "progress dialogue load hogaya: ");
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String url = "http://agricoop.nic.in/programmes-schemes-listing";


                Log.d("mytag", " "+url);

                Document document = Jsoup.connect(url.toLowerCase()).get() ;

                Log.d("mytag", "url se connect hogayaa ");

                Elements elements = document.getElementsByTag("a") ;

                Log.d("mytag", " a wala sara milgaya ");

                for (Element element : elements){

                    String href_here =  element.attr("abs:href");

                    if( href_here.contains("programmesandschemes") ){

                        String s1 = element.text() ;

                        SCHEME_CLASS scheme_class = new SCHEME_CLASS(s1,href_here);

                        arrayList.add(scheme_class) ;

                    }


                }

                Log.d("mytag", "arraylist agaya ");


            }catch (Exception e){

                Log.d("mytag", "#$%$%%%%%%%#$#%#$#(*&$#($&@*&%)*%#&%&@#)(%*)(#$*@)($*)($#*@$(: ");

                e.printStackTrace();

            }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog1.dismiss() ;
            Log.d("mytag", "progress dialogue dismissed: ");
            call_the_list_view() ;
        }


    }

    public void on_CLick_on_Item(String url2){

        if( url2.contains(".pdf") ){

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
            // Note the Chooser below. If no applications match,
            // Android displays a system message.So here there is no need for try-catch.
            startActivity(Intent.createChooser(intent, "Browse with"));

        }

        else {

            Log.d("mytag", " click hogaya ");

            second_url = url2 ;

            Fetch_Schemes2 fetch_schemes2 = new Fetch_Schemes2() ;
            fetch_schemes2.execute() ;

        }

    }

    private class Fetch_Schemes2 extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            Log.d("mytag", " iska fetch start ");
            progressDialog1 = new ProgressDialog(getActivity()) ;
            progressDialog1.setMessage("Fetching Schemes 2");
            progressDialog1.show();
            Log.d("mytag", "progress dialoge ko start kardiya : ");

            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Log.d("mytag", "yaha agya in background me: ");

                Document document = (Document) Jsoup.connect(second_url).get();

                Log.d("mytag", "connect hogaya url se ");

                Elements elements = document.getElementsByTag("a") ;

                for (Element element : elements){

                    String href_here =  element.attr("abs:href");

                    String text_here = element.text();

                    if( ( href_here.contains(".pdf") ) && ( text_here.contains("Download") ) ){

                        url_array.add(href_here );

                    }


                }

                boolean c5=FALSE;

                elements = document.getElementsByClass("views-field views-field-title") ;

                for( Element element : elements ){

                    String des_here = element.text();

                    Log.d("mytag", "yo bro bro bro: ");

                    if(c5)des_array.add(des_here) ;

                    c5=TRUE ;

                }

                Log.d("mytag", "dono array collected dude: " +url_array.size() +" ab dusra wla" + des_array.size() );

            }catch (Exception e){

                Log.d("mytag", "mai catch me hu yaha: ");

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog1.dismiss();
            Log.d("mytag", "post me agaya mai: ");
            join_them() ;
        }

    }

    public void join_them(){

        Log.d(TAG, "dono ko jod do ab ");

        for(int i=0;i<Math.min(url_array.size(),des_array.size());i++){

            SCHEME_CLASS scheme_class = new SCHEME_CLASS( des_array.get(i) , url_array.get(i)   ) ;

            arrayList2.add(scheme_class) ;

        }
        Log.d("mytag", "dono jodne ke baad final arraylist size hai " + arrayList2.size() );


        update_list_view() ;


    }

    public void update_list_view(){

        Log.d(TAG, "ab update karna hai isko ");

        ListView listView = (ListView)rootView.findViewById(R.id.list_view_schemes) ;

        SCHEME_ADAPTER scheme_adapter2 = new SCHEME_ADAPTER(getActivity(),arrayList2) ;

        listView.setAdapter(scheme_adapter2);

    }


    public class SCHEME_ADAPTER extends ArrayAdapter<SCHEME_CLASS> {



        public SCHEME_ADAPTER(Context context, ArrayList<SCHEME_CLASS> schemes){

            super(context,0,schemes);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View listView = convertView ;

            if(listView==null){
                listView = LayoutInflater.from(getContext()).inflate(R.layout.scheme_adpater_xml, parent, false);
            }

            final SCHEME_CLASS temp_obj = getItem(position) ;


            TextView textView = (TextView)listView.findViewById(R.id.Scheme_name) ;
            textView.setText( temp_obj.get_Des().substring(0, Math.min(25,temp_obj.get_Des().length()-1)  ) );

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    on_CLick_on_Item(temp_obj.get_Url());


                }
            });


            return listView;
        }
    }




}
