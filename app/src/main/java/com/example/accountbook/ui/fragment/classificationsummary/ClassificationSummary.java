package com.example.accountbook.ui.fragment.classificationsummary;

import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.accountbook.R;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.ArcView;
import com.example.accountbook.tools.DateCalculation;
import com.example.accountbook.tools.ListFilter;
import com.example.accountbook.tools.ToastUtil;

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
    private TextView noRecord;
    private TextView textMonth;
    private TextView textDay;
    private Button filterButton;
    private List<IncomeEntity> incomeEntities;

    private boolean YEAR = false;
    private boolean MONTH = false;
    private boolean DAY = false;
    private String normlYear = String.valueOf(calendar.get(Calendar.YEAR));
    private String normlMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
    private String normlDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

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
        constraintLayout = (ConstraintLayout) rootView.findViewById(R.id.filter_constraintLayout);
        constraintLayout.setVisibility(View.VISIBLE);
        filterButton = (Button) rootView.findViewById(R.id.filter_button);
        filterYear = (TextView) rootView.findViewById(R.id.filter_year);
        filterMonth = (TextView) rootView.findViewById(R.id.filter_month);
        filterDay = (TextView) rootView.findViewById(R.id.filter_day);
        textDay = (TextView) rootView.findViewById(R.id.day_text);
        textMonth = (TextView) rootView.findViewById(R.id.month_text);
        noRecord = (TextView) rootView.findViewById(R.id.no_record_text);
        initincomeEntities();
        initData();
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView();
            }
        });
        return rootView;
    }

    public void updateView() {
        if (filterYear == null || getContext() == null) {
            return;
        }
        String filterDate = new String();
        String year = filterYear.getText().toString();
        String month = filterMonth.getText().toString();
        String day = filterDay.getText().toString();

        filterDate = DateCalculation.exactDate(year, month, day, getContext());
        whetherUpdateData(year, month, day,filterDate);
    }

    private void whetherUpdateData(String year, String month, String day,String filterDate) {
        if (TextUtils.isEmpty(year)) {
            ToastUtil.toast(getContext(), getContext().getString(R.string.input_no_year_toast_text));
        } else if (TextUtils.isEmpty(month)) {
            if (MONTH || DAY) {
                ToastUtil.toast(getContext(), getContext().getString(R.string.input_no_month_toast_text));
            }else {
                incomeEntities = ListFilter.listFilter(filterDate);
                initData();
            }
        } else if (TextUtils.isEmpty(day)) {
            if (DAY) {
                ToastUtil.toast(getContext(), getContext().getString(R.string.input_no_day_toast_text));
            }else {
                incomeEntities = ListFilter.listFilter(filterDate);
                initData();
            }
        }else {
            incomeEntities = ListFilter.listFilter(filterDate);
            initData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void initData() {
        if (YEAR){
            Log.d(TAG, "initData: YEAR");
        }else if (MONTH){
            Log.d(TAG, "initData: MONTH");
        }else {
            Log.d(TAG, "initData: DAY");
        }
        Log.d(TAG, "initData: 被调用");
        double sMoneySum = 0;
        double zMoneySum = 0;

        for (int i = 0; i < incomeEntities.size(); i++) {
            Log.d(TAG, "initData: incomeEntities.get(i).getIncomeMoney() = " + incomeEntities.get(i).getIncomeMoney());
            if (incomeEntities.get(i).getIncomeType() == 0) {
                zMoneySum += Double.parseDouble(incomeEntities.get(i).getIncomeMoney());
            } else
                sMoneySum += Double.parseDouble(incomeEntities.get(i).getIncomeMoney());
        }
        if (zMoneySum == 0 && sMoneySum == 0) {
            arcView.setVisibility(View.GONE);
            noRecord.setVisibility(View.VISIBLE);
        } else {
            arcView.setVisibility(View.VISIBLE);
            noRecord.setVisibility(View.GONE);
            Proportion sProportion = new Proportion();
            Proportion zProportion = new Proportion();
            DecimalFormat df = new DecimalFormat("######0.00");
            sProportion.money = Double.parseDouble(df.format(sMoneySum));
            sProportion.type = getString(R.string.general_income_string);
            zProportion.money = Double.parseDouble(df.format(zMoneySum));
            zProportion.type = getString(R.string.total_expenditure_string);
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
            initView();
        }
    }

    public void initView() {
        arcView.setMaxNum(2);
        arcView.setColor(0, Color.parseColor("#EE6D7F"));
        arcView.setColor(1, Color.parseColor("#89E496"));
        arcView.invalidate();
    }

    public void initincomeEntities() {
        if (YEAR) {
            filterYear.setText(normlYear);
            incomeEntities = ListFilter.listFilter(normlYear);
        }
        if (MONTH) {
            filterYear.setText(normlYear);
            filterMonth.setText(normlMonth);
            filterMonth.setVisibility(View.VISIBLE);
            textMonth.setVisibility(View.VISIBLE);
            if (Integer.parseInt(normlMonth) < 10 && !normlMonth.contains("0"))
                normlMonth = "0" + normlMonth;
            incomeEntities = ListFilter.listFilter(normlYear + "-" + normlMonth);
        }
        if (DAY) {
            filterYear.setText(normlYear);
            filterMonth.setText(normlMonth);
            filterDay.setText(normlDay);
            filterMonth.setVisibility(View.VISIBLE);
            filterDay.setVisibility(View.VISIBLE);
            textDay.setVisibility(View.VISIBLE);
            textMonth.setVisibility(View.VISIBLE);
            if (Integer.parseInt(normlMonth) < 10 && !normlMonth.contains("0"))
                normlMonth = "0" + normlMonth;
            if (Integer.parseInt(normlDay) < 10 && !normlDay.contains("0"))
                normlDay = "0" + normlDay;
            incomeEntities = ListFilter.listFilter(normlYear + "-" + normlMonth + "-" + normlDay);
        }
    }

    public ArcView getArcView() {
        return arcView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        initincomeEntities();
        initData();
    }

    class Proportion {
        double money;
        String type;
    }
}