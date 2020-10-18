package com.example.agricare;


import android.content.Context;
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

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class list_of_crops_frag extends Fragment {


    public list_of_crops_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_of_crops_xml, container, false);


        ListView listView = (ListView)rootView.findViewById(R.id.my_list_view) ;

        Log.d("mytag", "yaha fragment tatk thik hai ");


        //create an arraylist of crops and its price and then use adaptor

        ArrayList<CROP_CLASS> arrayList = new ArrayList<CROP_CLASS>() ;

        arrayList = MainActivity.CROPS_LIST ;

        Log.d("mytag", "iska size real: ye wala " + arrayList.size());

        Crop_Adapter crop_adapter = new Crop_Adapter( (MainActivity)getActivity(),arrayList ) ;

        listView.setAdapter(crop_adapter);

        return rootView;
    }

    public void onCropClick(String namehere){

        CustomDialogClass customDialogClass = new CustomDialogClass(getActivity(),namehere,getFragmentManager()) ;

        customDialogClass.show();

    }


    public class Crop_Adapter extends ArrayAdapter<CROP_CLASS> {


        public Crop_Adapter(Context context, ArrayList<CROP_CLASS> CROPS) {

            super(context, 0, CROPS);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View ListView = convertView ;

            final CROP_CLASS temp = getItem(position) ;

            if( ListView == null ){
                ListView = LayoutInflater.from(getContext()).inflate(R.layout.crops_adapter_xml, parent, false);
            }

            Log.d("mytag", "yaha thik hai yaaaaaar: ");

            Log.d("mytag", " "+temp.getName());


            TextView textView = (TextView)ListView.findViewById(R.id.nameofcrop) ;

            textView.setText(temp.getName().toUpperCase());





            textView = (TextView)ListView.findViewById(R.id.liveprice) ;

            double live_price = temp.getLive_price() ;

            if(live_price==0)live_price =  2000 + ( Math.random() * 1000 ) ;

            String s5 = Double.toString(live_price) ;

            textView.setText(s5);



            textView = (TextView)ListView.findViewById(R.id.percentagefarmer) ;

            double percentage = temp.getPercentage_of_farmer();

            s5 = Double.toString(percentage) ;

            textView.setText( s5 );

            textView = (TextView)ListView.findViewById(R.id.expectedprice) ;

            double expexted_price = temp.getExpected_price() ;

            expexted_price = live_price + ( (1/(1 + percentage + (MainActivity.rainfall) )) * 100 ) ;

            s5 = Double.toString(expexted_price) ;

            textView.setText( s5 );




            ListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onCropClick( temp.getName() );


                }
            });


            return ListView ;

        }
    }






}
