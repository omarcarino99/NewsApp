package com.example.android.newsapp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.android.newsapp.Fragments.BusinessFragment;
import com.example.android.newsapp.Fragments.PoliticsFragment;
import com.example.android.newsapp.Fragments.SportsFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Business", "Politics", "Sports"};

    private Context mContext;

    public FragmentAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new BusinessFragment();
            case 1: return new PoliticsFragment();
            case 2: return new SportsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
