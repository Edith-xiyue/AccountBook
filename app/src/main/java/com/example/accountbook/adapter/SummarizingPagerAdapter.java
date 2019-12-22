package com.example.accountbook.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.accountbook.ui.fragment.summarizing.SummarizingFragment;

import java.util.List;

public class SummarizingPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private String[] titles;

    public SummarizingPagerAdapter(FragmentManager mFragmentManager,List fragmentList,String[] title) {
        super(mFragmentManager);
        list = fragmentList;
        titles=title;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < list.size()) {
            fragment = list.get(position);
        } else {
            fragment = list.get(0);
        }
        return fragment;
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
