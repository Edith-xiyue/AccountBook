package com.example.accountbook.ui.fragment.summarizing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.accountbook.R;
import com.example.accountbook.adapter.SummarizingPagerAdapter;
import com.example.accountbook.tools.CustomStatusBarBackground;
import com.example.accountbook.ui.fragment.classificationsummary.ClassificationSummary;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SummarizingFragment extends Fragment {

    private static final String TAG = "SummarizingFragment";
    private TabLayout tabLayout = null;
    private ViewPager viewPager;
    private LinearLayout titleLayout;
    private TextView titleText;
    private FragmentStatePagerAdapter pagerAdapter;
    private static ClassificationSummary yearClassificationSummary = new ClassificationSummary("year");
    private static ClassificationSummary monthClassificationSummary = new ClassificationSummary("month");
    private static ClassificationSummary dayClassificationSummary = new ClassificationSummary("day");

    private View statusBar;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] mTabTitles = new String[3];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_summarizing, container, false);
        statusBar = root.findViewById(R.id.custom_status_bar_background);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.tab_viewpager);
        titleLayout = root.findViewById(R.id.title);
        titleText = root.findViewById(R.id.title_name);
        initView();
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        pagerAdapter.notifyDataSetChanged();
    }

    public static ClassificationSummary getYearClassificationSummary() {
        return yearClassificationSummary;
    }

    public static ClassificationSummary getMonthClassificationSummary() {
        return monthClassificationSummary;
    }

    public static ClassificationSummary getDayClassificationSummary() {
        return dayClassificationSummary;
    }

    public void initView(){
        mTabTitles[0] = getString(R.string.year_text);
        mTabTitles[1] = getString(R.string.month_text);
        mTabTitles[2] = getString(R.string.day_text);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        fragments.add(yearClassificationSummary);
        fragments.add(monthClassificationSummary);
        fragments.add(dayClassificationSummary);
        pagerAdapter = new SummarizingPagerAdapter(getChildFragmentManager(),fragments,mTabTitles);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        CustomStatusBarBackground.drawableViewStatusBar(getContext(),R.drawable.custom_gradient_main_title,statusBar);
        titleText.setText(R.string.title_summarizing);
        titleLayout.setVisibility(View.VISIBLE);
        tabLayout.getTabAt(0).setCustomView(getTabView(0));
        tabLayout.getTabAt(1).setCustomView(getTabView(1));
        tabLayout.getTabAt(2).setCustomView(getTabView(2));
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_item, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTabTitles[position]);
        return v;
    }
}