package com.example.agricare;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class Soil_info_frag extends Fragment {


    public Soil_info_frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.soil_info_xml, container, false);

        Log.d("abhi kar raha", ( Double.toString( MainActivity.pH )  )) ;

        Log.d("mytag", "yaha kya hua yaar :  ") ;



        // we can access these variables from other class without even creating any instance, because :
        //      1. these variables are declared as public
        //      2. these variables are declared as static so we can access them without instance

        double pH_val = MainActivity.pH ;
        double cec_val = MainActivity.CEC ;
        double nitrogen_val = MainActivity.Nitrogen ;
        double rain_val = MainActivity.rainfall;
        double temp_val = MainActivity.temperature ;
        double humid_val = MainActivity.humidity ;

        TextView textView = (TextView)rootView.findViewById(R.id.PHTEXT) ;
        textView.setText( String.format("pH : %.2f", pH_val) );

        textView = (TextView)rootView.findViewById(R.id.NITROGENTEXT) ;
        textView.setText(String.format("Nitrogen : %.2f",nitrogen_val));

        textView = (TextView)rootView.findViewById(R.id.CECTEXT) ;
        textView.setText(String.format("CEC : %.2f", cec_val));

//        if(rain_val==0)
//            rain_val = 150 + ( Math.random()*100 ) ;

        textView = (TextView)rootView.findViewById(R.id.RAINTEXT) ;
        textView.setText( String.format("RAIN : %.2f", rain_val) );

        textView = (TextView)rootView.findViewById(R.id.TEMPTEXT) ;
        textView.setText(String.format("TEMP : %.2f", temp_val));

        textView = (TextView)rootView.findViewById(R.id.HUMIDTEXT) ;
        textView.setText(String.format("HUMIDITY : %.2f", humid_val));

        Button button = (Button)rootView.findViewById(R.id.get_best_crops_button) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // we are provoking a non-static method without creating its instance by using context

                ( (MainActivity)getActivity() ).go_to_best_crops();

                FragmentManager fm = getFragmentManager();
//                if (fm.getBackStackEntryCount() > 0) {
//                    fm.popBackStack();
//                }

            }
        });


        return  rootView ;
    }

}
