package com.example.accountbook;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.accountbook.adapter.IncomeRecycleAdapter;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomStatusBarBackground;
import com.example.accountbook.tools.DateStringText;
import com.example.accountbook.tools.EventBusConfig;
import com.example.accountbook.tools.ToastUtil;
import com.example.accountbook.ui.fragment.particular.ParticularFragment;
import com.example.accountbook.ui.fragment.tally.TallyFragment;
import com.example.accountbook.ui.fragment.summarizing.SummarizingFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static Context context;
    private static DateStringText dateStringText = new DateStringText();
    private static List<IncomeEntity> incomeEntities = new ArrayList<>();
    private BottomNavigationBar mBottomNavigationBar;
    private static FragmentManager sFragmentManager;
    private TallyFragment tallyFragment;
    private static ParticularFragment particularFragment;
    private SummarizingFragment summarizingFragment;
    private static IncomeRecycleAdapter incomeRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        EventBus.getDefault().register(this);
        CustomStatusBarBackground.customStatusBarTransparent(this);
        initIncomeEntities();
        initView();
        setEnterFragment();
        setView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void alterParticular(EventBusConfig.alterIncomeEntitie entity){
        FragmentTransaction fragmentTransaction = sFragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(tallyFragment);
        fragmentTransaction.commit();
        mBottomNavigationBar.selectTab(0);
    }

    private void initView() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar_main);
    }

    private void setView() {
        mBottomNavigationBar
                .setMode(BottomNavigationBar. MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.color_white);

        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_tally_select, R.string.title_tally)
                        .setActiveColorResource(R.color.color_gray)
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_tally_default))
                        .setInActiveColorResource(R.color.color_black))
                .addItem(new BottomNavigationItem(R.drawable.icon_collect_select, R.string.title_particular)
                        .setActiveColorResource(R.color.color_gray)
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_collect_default))
                        .setInActiveColorResource(R.color.color_black))
                .addItem(new BottomNavigationItem(R.drawable.icon_minute_select, R.string.title_summarizing)
                        .setActiveColorResource(R.color.color_gray)
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_minute_default))
                        .setInActiveColorResource(R.color.color_black))
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction sFragmentTransaction = sFragmentManager.beginTransaction();
                hideFragment(sFragmentTransaction);

                switch (position) {
                    case 0:
                        if (tallyFragment == null) {
                            tallyFragment = new TallyFragment();
                            sFragmentTransaction.add(R.id.frameContent, tallyFragment);
                        } else {
                            sFragmentTransaction.show(tallyFragment);
                        }
                        break;
                    case 1:
                        if (particularFragment == null) {
                            particularFragment = new ParticularFragment();
                            sFragmentTransaction.add(R.id.frameContent, particularFragment);
                        } else {
                            sFragmentTransaction.show(particularFragment);
                        }
                        tallyFragment.initView();
                        break;
                    case 2:
                        if (summarizingFragment == null) {
                            summarizingFragment = new SummarizingFragment();
                            sFragmentTransaction.add(R.id.frameContent, summarizingFragment);
                        } else {
                            sFragmentTransaction.show(summarizingFragment);
                        }
                        tallyFragment.initView();
                        break;
                }
                sFragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
    public void setEnterFragment(){
        sFragmentManager = getSupportFragmentManager();
        FragmentTransaction sFragmentTransaction = getSupportFragmentManager().beginTransaction();
        tallyFragment = new TallyFragment();
        summarizingFragment = new SummarizingFragment();
        particularFragment = new ParticularFragment();
        sFragmentTransaction.add(R.id.frameContent, particularFragment);
        sFragmentTransaction.add(R.id.frameContent, summarizingFragment);
        sFragmentTransaction.add(R.id.frameContent, tallyFragment);
        sFragmentTransaction.show(tallyFragment);
        sFragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (tallyFragment != null) {
            transaction.hide(tallyFragment);
        }
        if (particularFragment != null) {
            transaction.hide(particularFragment);
        }
        if (summarizingFragment != null) {
            transaction.hide(summarizingFragment);
        }
    }

    public static ParticularFragment getParticularFragment() {
        return particularFragment;
    }

    public static IncomeRecycleAdapter getIncomeRecycleAdapter() {
        if (incomeRecycleAdapter == null)
            incomeRecycleAdapter = new IncomeRecycleAdapter(MainActivity.getContext(),incomeEntities);
        return incomeRecycleAdapter;
    }

    public static void setIncomeRecycleAdapter(IncomeRecycleAdapter incomeRecycleAdapter) {
        MainActivity.incomeRecycleAdapter = incomeRecycleAdapter;
    }

    public static Context getContext(){
        return context;
    }

    public static DateStringText getDateStringText() {
        if (dateStringText == null)
            setDateStringText(new DateStringText());
        return dateStringText;
    }

    public static void setDateStringText(DateStringText dateStringText) {
        MainActivity.dateStringText = dateStringText;
    }

    public static List<IncomeEntity> getIncomeEntities() {
        return incomeEntities;
    }


    public static void setIncomeEntities(List<IncomeEntity> incomeEntities) {
        MainActivity.incomeEntities = incomeEntities;
    }

    public void initIncomeEntities() {
        for (int i = 0;i < incomeEntities.size();i ++){
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = yearFormat.format(incomeEntities.get(i).getIncomeTime());
            incomeEntities.get(i).setDate(dateStr);
        }
    }

    private long time;
    private boolean quit = false;

    @Override
    public void onBackPressed() {
        if (quit == false){
            time = System.currentTimeMillis();
            quit = true;
            ToastUtil.timingToast(MainActivity.this,getString(R.string.quitApp_toast_text),1000);
            return;
        }
        if (System.currentTimeMillis() - time <= 1000){
            super.onBackPressed();
            ToastUtil.cancelToast(MainActivity.this);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }else{
            time = System.currentTimeMillis();
            ToastUtil.timingToast(MainActivity.this,getString(R.string.quitApp_toast_text),1000);
        }
    }
}
