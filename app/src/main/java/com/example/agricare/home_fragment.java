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
        // We do have other methods to pass arguments in fragments, but by default we can't really overload constructor of fargment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //In Fragment we don't generally require onCreate method becuase unlike the activity has setContentView, Fragment has
        // this onCreateView which happens to assign the layout the given fragment and returns the view

        // Basically it just as same as activity's setcontentview

        View rootView = inflater.inflate(R.layout.home_xml, container, false);

        // Layout Inflator instantiates the XML file into the view objects for fragment or activity.

        Button get_info = (Button) rootView.findViewById(R.id.get_info_button) ;

        final EditText area_text = (EditText) rootView.findViewById(R.id.area_edit_text) ;


        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                     String s1 = area_text.getText().toString() ;
                     Double d1 = Double.parseDouble(s1) ;

                    ( (MainActivity)getActivity() ).go_to_soil_info(d1);

//                     getActivity() when used in fragment, it returns the context of the activity which is closely related to the current fragment
//                     Context is the abstract base class, every activity and services etc are derived from context
//                     Each activity is associated with a context, and there is applicationContext which is at the topmost of the whole application.


//                    FragmentManager fm = getFragmentManager();
//                    if (fm.getBackStackEntryCount() > 0) {
//                        fm.popBackStack();
//                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return  rootView ;

    }

}
