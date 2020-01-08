package com.example.accountbook.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class SummarizingPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> list = new ArrayList<>();
    private String[] titles;

    public SummarizingPagerAdapter(FragmentManager mFragmentManager,List fragmentList,String[] title) {
        super(mFragmentManager);
        list = fragmentList;
        titles = title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > 0)
            return titles[position];
        return null;
    }
}