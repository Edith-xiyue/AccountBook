package com.example.accountbook.tools;

import com.example.accountbook.MainActivity;
import com.example.accountbook.database.table.IncomeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSort {
    private static final String TAG = "ListSort";

    public static void CollectionsList(List<IncomeEntity> myBeanList) {
        Collections.sort(myBeanList, new Comparator<IncomeEntity>() {
            @Override
            public int compare(IncomeEntity o1, IncomeEntity o2) {
                long i =  o1.getIncomeTime() - o2.getIncomeTime();
                if (i > 0)
                    return -1;
                else if (i == 0)
                    return 0;
                else
                    return 1;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
    }
}
