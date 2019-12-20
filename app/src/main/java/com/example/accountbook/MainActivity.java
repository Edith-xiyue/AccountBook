package com.example.accountbook;

import android.content.Context;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.accountbook.adapter.IncomeRecycleAdapter;
import com.example.accountbook.database.MyDataBase;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomStatusBarBackground;
import com.example.accountbook.tools.DateStringText;
import com.example.accountbook.ui.fragment.particular.ParticularFragment;
import com.example.accountbook.ui.fragment.tally.TallyFragment;
import com.example.accountbook.ui.fragment.summarizing.SummarizingFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private static DateStringText dateStringText = new DateStringText();
    private static List<IncomeEntity> incomeEntities = new ArrayList<>();
    private BottomNavigationBar mBottomNavigationBar;
    private static FragmentManager sFragmentManager;
    private TallyFragment tallyFragment;
    private ParticularFragment particularFragment;
    private SummarizingFragment summarizingFragment;
    private static IncomeRecycleAdapter incomeRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        CustomStatusBarBackground.customStatusBarTransparent(this);
        initView();
        MyDataBase.init(this);
        setEnterFragment();
        setView();
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
                //B: [PT-79] [Intranet Chat] [APP][UI] BottomNavigationBar 控件优化,Allen Luo,2019/11/13
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

                        break;
                    case 2:
                        if (summarizingFragment == null) {
                            summarizingFragment = new SummarizingFragment();
                            sFragmentTransaction.add(R.id.frameContent, summarizingFragment);
                        } else {
                            sFragmentTransaction.show(summarizingFragment);
                        }
                        break;
                }
                sFragmentTransaction.commit();
                //E: [PT-79] [Intranet Chat] [APP][UI] BottomNavigationBar 控件优化,Allen Luo,2019/11/13
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
    //B: [PT-79] [Intranet Chat] [APP][UI] BottomNavigationBar 控件优化,Allen Luo,2019/11/13
    public void setEnterFragment(){
        sFragmentManager = getSupportFragmentManager();
        FragmentTransaction sFragmentTransaction = getSupportFragmentManager().beginTransaction();
        tallyFragment = new TallyFragment();
        sFragmentTransaction.add(R.id.frameContent, tallyFragment);
        sFragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (tallyFragment != null){
            transaction.hide(tallyFragment);
        }
        if (particularFragment != null){
            transaction.hide(particularFragment);
        }
        if (summarizingFragment != null){
            transaction.hide(summarizingFragment);
        }
    }

    public static IncomeRecycleAdapter getIncomeRecycleAdapter() {
        if (incomeRecycleAdapter == null){
            incomeRecycleAdapter = new IncomeRecycleAdapter(MainActivity.getContext(),incomeEntities);
        }
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
}
