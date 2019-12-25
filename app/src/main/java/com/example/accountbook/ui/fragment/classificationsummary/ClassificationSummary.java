package com.example.accountbook.ui.fragment.classificationsummary;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.ArcView;
import com.example.accountbook.tools.ListFilter;
import com.example.accountbook.ui.fragment.summarizing.SummarizingFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassificationSummary extends Fragment {

    private static final String TAG = "ClassificationSummary";

    private Calendar calendar = Calendar.getInstance();
    private ArcView arcView;
    private ConstraintLayout constraintLayout;
    private TextView filterYear;
    private TextView filterMonth;
    private TextView filterDay;
    private TextView textYear;
    private TextView textMonth;
    private TextView textDay;
    private Button filterButton;
    private List<IncomeEntity> incomeEntities;

    private boolean YEAR = false;
    private boolean MONTH = false;
    private boolean DAY = false;
    private String year = String.valueOf(calendar.get(Calendar.YEAR));
    private String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
    private String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    public ClassificationSummary(String DateType) {
        if (DateType.contains("year")) YEAR = true;
        if (DateType.contains("month")) MONTH = true;
        if (DateType.contains("day")) DAY = true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summarizing, container, false);
        arcView = (ArcView) rootView.findViewById(R.id.arc);
        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.filter_linearLayout);
        constraintLayout.setVisibility(View.VISIBLE);
        filterButton = (Button) rootView.findViewById(R.id.filter_button);
        filterYear = (TextView) rootView.findViewById(R.id.filter_year);
        filterMonth = (TextView) rootView.findViewById(R.id.filter_month);
        filterDay = (TextView) rootView.findViewById(R.id.filter_day);
        textDay = (TextView) rootView.findViewById(R.id.day_text);
        textMonth = (TextView) rootView.findViewById(R.id.month_text);
        textYear = (TextView) rootView.findViewById(R.id.year_text);
        arcView.setVisibility(View.VISIBLE);
        initincomeEntities();
        initData();
        initView();
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView();
            }
        });
        return rootView;
    }

    public void updateView(){
        String filterString;
        if (YEAR){
            if (filterYear == null) return;
            filterString = String.valueOf(filterYear.getText());
            incomeEntities = ListFilter.listFilter(filterString);
            initData();
            initView();
        }

        if (MONTH){
            if (filterYear == null) return;
            filterString = filterYear.getText() + "-" + filterMonth.getText();
            incomeEntities = ListFilter.listFilter(filterString);
            initData();
            initView();
        }

        if (DAY){
            if (filterYear == null) return;
            filterString = filterYear.getText() + "-" + filterMonth.getText() + "-" + filterDay.getText();
            incomeEntities = ListFilter.listFilter(filterString);
            initData();
            initView();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public  void initData(){
        double sMoneySum = 0;
        double zMoneySum = 0;
        for (int i = 0;i < incomeEntities.size();i ++){
            if (incomeEntities.get(i).getIncomeType() == 0){
                zMoneySum += Double.parseDouble(incomeEntities.get(i).getIncomeMoney());
            }else
                sMoneySum += Double.parseDouble(incomeEntities.get(i).getIncomeMoney());
        }
        Proportion sProportion = new Proportion();
        Proportion zProportion = new Proportion();
        sProportion.money = sMoneySum;
        sProportion.type = getString(R.string.total_expenditure_string);
        zProportion.money = zMoneySum;
        zProportion.type = getString(R.string.general_income_string);
        List<Proportion> proportions = new ArrayList<>();
        proportions.add(zProportion);
        proportions.add(sProportion);
        ArcView.ArcViewAdapter myArcAdapter = arcView.new ArcViewAdapter<Proportion>() {

            @Override
            public double getValue(Proportion proportion) {
                return proportion.money;
            }

            @Override
            public String getText(Proportion proportion) {
                return proportion.type;
            }
        };
        myArcAdapter.setData(proportions);//设置数据集
    }

    public void initView(){
        arcView.setMaxNum(2);
        arcView.setColor(0,Color.parseColor("#EE6D7F"));
        arcView.setColor(1,Color.parseColor("#89E496"));
        arcView.invalidate();
    }

    public void initincomeEntities() {
        if (YEAR){
            filterYear.setText(year);
            incomeEntities = ListFilter.listFilter(year);
        }
        if (MONTH){
            filterYear.setText(year);
            filterMonth.setText(month);
            filterMonth.setVisibility(View.VISIBLE);
            textMonth.setVisibility(View.VISIBLE);
            incomeEntities = ListFilter.listFilter(year + "-" + month);
        }
        if (DAY){
            filterYear.setText(year);
            filterMonth.setText(month);
            filterDay.setText(day);
            filterMonth.setVisibility(View.VISIBLE);
            filterDay.setVisibility(View.VISIBLE);
            textDay.setVisibility(View.VISIBLE);
            textMonth.setVisibility(View.VISIBLE);
            incomeEntities = ListFilter.listFilter(year + "-" + month + "-" + day);
        }
    }

    public ArcView getArcView() {
        return arcView;
    }

    class  Proportion{
        double money;
        String type;
    }
}