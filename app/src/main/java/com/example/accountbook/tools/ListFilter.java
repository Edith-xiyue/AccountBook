package com.example.accountbook.tools;

import android.util.Log;

import com.example.accountbook.MainActivity;
import com.example.accountbook.database.table.IncomeEntity;

import java.util.ArrayList;
import java.util.List;

public class ListFilter {

    private static List<IncomeEntity> filterEntities;
    private static List<IncomeEntity> sourceEntities = MainActivity.getIncomeEntities();;

    private static final String TAG = "ListFilter";

    public static List<IncomeEntity> listFilter(String charString) {
        filterEntities = new ArrayList<>();
        for (int i = 0; i < sourceEntities.size(); i++) {
            Log.d(TAG, "listFilter: sourceEntities = " + sourceEntities.get(i).toString());
            if (sourceEntities.get(i).getDate().contains(charString)) {
                filterEntities.add(sourceEntities.get(i));
            }
        }
        return filterEntities;
    }
}
