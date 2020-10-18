package com.example.agricare;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class home_fragment extends Fragment {


    public home_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_xml, container, false);


        Button get_info = (Button) rootView.findViewById(R.id.get_info_button) ;

        final EditText area_text = (EditText) rootView.findViewById(R.id.area_edit_text) ;


        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                     String s1 = area_text.getText().toString() ;
                     Double d1 = Double.parseDouble(s1) ;

                    ( (MainActivity)getActivity() ).go_to_soil_info(d1);

                    FragmentManager fm = getFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return  rootView ;

    }

}
