package com.example.accountbook.ui.fragment.classificationsummary;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.tools.ArcView;

import java.util.ArrayList;
import java.util.List;

public class ClassificationSummary extends Fragment {

    private static final String TAG = "ClassificationSummary";

    private List<Proportion> proportions = new ArrayList<>();
    private ArcView arcView;
    private double sMoneySum;
    private double zMoneySum;
    public static Fragment newInstance() {
        ClassificationSummary fragment = new ClassificationSummary();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summarizing, container, false);
        arcView = (ArcView) rootView.findViewById(R.id.arc);
        arcView.setVisibility(View.VISIBLE);
        initData();
        initView();
        return rootView;
    }

    public void initData(){
        for (int i = 0;i < MainActivity.getIncomeEntities().size();i ++){
            if (MainActivity.getIncomeEntities().get(i).getIncomeType() == 0){
                zMoneySum += Double.parseDouble(MainActivity.getIncomeEntities().get(i).getIncomeMoney());
            }else
                sMoneySum += Double.parseDouble(MainActivity.getIncomeEntities().get(i).getIncomeMoney());
        }
        Proportion sProportion = new Proportion();
        Proportion zProportion = new Proportion();
        sProportion.money = sMoneySum;
        sProportion.type = "总支出";
        zProportion.money = zMoneySum;
        zProportion.type = "总收入";
        Log.d(TAG, "initData: sProportion = " + (sProportion == null));
        proportions.add(zProportion);
        proportions.add(sProportion);
    }

    public void initView(){
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
        arcView.setMaxNum(2);
        arcView.setColor(0, Color.parseColor("#EE6D7F"));
        arcView.setColor(1,Color.parseColor("#89E496"));
    }
    class  Proportion{
        double money;
        String type;

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
