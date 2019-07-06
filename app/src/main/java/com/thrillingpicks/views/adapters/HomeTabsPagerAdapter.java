package com.thrillingpicks.views.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.thrillingpicks.views.fragments.home.TodayHomeFragment;
import com.thrillingpicks.views.fragments.home.TomorrowFragment;

public class HomeTabsPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    Resources resources;


    public HomeTabsPagerAdapter(Resources resources, FragmentManager childFragmentManager, int tabCount) {
        super(childFragmentManager);
        this.resources = resources;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new TodayHomeFragment();
            case 1:
                return  new TomorrowFragment();
             default:
                 return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
