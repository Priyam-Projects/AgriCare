package com.example.agricare;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Custom_Fragment_Page_Adapter extends FragmentPagerAdapter {

    public Custom_Fragment_Page_Adapter(FragmentManager fm) {
        super(fm) ;
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0)return new home_fragment() ;

        else return new List_of_Schemes() ;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if(position==0)return "HOME" ;
        else return "SCHEMES" ;

    }

    @Override
    public int getCount() {
        return 2;
    }
}
