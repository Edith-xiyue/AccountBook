package com.example.accountbook.tools;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;

public class DateCalculation {
    private static final String TAG = "DateCalculation";

    public static String exactDateGeneral(String year, String month, String day, Context context) {
        StringBuffer filterDate = new StringBuffer();
        if (TextUtils.isEmpty(year) && TextUtils.isEmpty(month) && TextUtils.isEmpty(day)) {
            return filterDate.toString();
        } else if (TextUtils.isEmpty(year) && (!TextUtils.isEmpty(month) || !TextUtils.isEmpty(day))) {
            ToastUtil.toast(context, context.getString(R.string.input_no_year_toast_text));
        } else if (TextUtils.isEmpty(month) && !TextUtils.isEmpty(day)) {
            ToastUtil.toast(context, context.getString(R.string.input_no_month_toast_text));
        } else {
            filterDate.append(year).append("-").append(month).append("-").append(day);
            Log.d(TAG, "onViewClicked: filterDate = " + filterDate);
            Log.d(TAG, "onViewClicked: switch (tag) = " + dateCalculation(filterDate.toString()));
            switch (dateCalculation(filterDate.toString())) {
                case 0:
                    ToastUtil.toast(context, context.getString(R.string.edit_error_year_toast_text));
                    break;
                case 1:
                    ToastUtil.toast(context, context.getString(R.string.edit_error_month_toast_text));
                    break;
                case 2:
                    ToastUtil.toast(context, context.getString(R.string.edit_error_day_toast_text));
                    break;
                case 3:
                    if (!TextUtils.isEmpty(month)) {
                        if (month.contains("0")) {
                            filterDate.insert(5, "0");
                        }
                        if (!TextUtils.isEmpty(day)) {
                            if (day.contains("0")) {
                                filterDate.insert(8, "0");
                            }
                            return filterDate.toString();
                        } else {
                            return new String(filterDate.substring(0, 7));
                        }
                    } else {
                        return new String(filterDate.substring(0, 4));
                    }
            }
        }
        return filterDate.toString();
    }

    public static String exactDate(String year, String month, String day, Context context) {
        StringBuffer filterDate = new StringBuffer();

        if (TextUtils.isEmpty(year) && TextUtils.isEmpty(month) && TextUtils.isEmpty(day)) {
            return filterDate.toString();
        }
        filterDate.append(year).append("-").append(month).append("-").append(day);
        Log.d(TAG, "onViewClicked: filterDate = " + filterDate);
        Log.d(TAG, "onViewClicked: switch (tag) = " + dateCalculation(filterDate.toString()));
        switch (dateCalculation(filterDate.toString())) {
            case 0:
                ToastUtil.toast(context, context.getString(R.string.edit_error_year_toast_text));
                break;
            case 1:
                ToastUtil.toast(context, context.getString(R.string.edit_error_month_toast_text));
                break;
            case 2:
                ToastUtil.toast(context, context.getString(R.string.edit_error_day_toast_text));
                break;
            case 3:
                if (!TextUtils.isEmpty(month)) {
                    if (!month.contains("0")) {
                        filterDate.insert(5, "0");
                    }
                    if (!TextUtils.isEmpty(day)) {
                        if (!day.contains("0")) {
                            filterDate.insert(8, "0");
                        }
                        return filterDate.toString();
                    } else {
                        return new String(filterDate.substring(0, 7));
                    }
                } else {
                    return new String(filterDate.substring(0, 4));
                }
        }
        return filterDate.toString();
    }

    public static int dateCalculation(String date) {
        String[] strs = date.split("-");
        for (int i = 0; i < strs.length; i++) {
            switch (i) {
                case 0:
                    if (!(Integer.valueOf(strs[i]) >= 2000 && Integer.valueOf(strs[i]) < 2100)) {
                        return 0;
                    }
                    break;
                case 1:
                    if (!(Integer.valueOf(strs[i]) > 0 && Integer.valueOf(strs[i]) < 13)) {
                        return 1;
                    }
                    break;
                case 2:
                    int[] max = {1, 3, 5, 7, 8, 10, 12};
                    int[] min = {4, 6, 9, 11};
                    if (max.equals(Integer.valueOf(strs[1]))) {
                        if (!(Integer.valueOf(strs[i]) > 0 && Integer.valueOf(strs[i]) < 32)) {
                            return 2;
                        }
                    } else if (min.equals(Integer.valueOf(strs[1]))) {
                        if (!(Integer.valueOf(strs[i]) > 0 && Integer.valueOf(strs[i]) < 31)) {
                            return 2;
                        }
                    } else {
                        if ((Integer.valueOf(strs[0]) % 4) == 0) {
                            if (!(Integer.valueOf(strs[i]) > 0 && Integer.valueOf(strs[i]) < 30)) {
                                return 2;
                            }
                        } else {
                            if (!(Integer.valueOf(strs[i]) > 0 && Integer.valueOf(strs[i]) < 29)) {
                                return 2;
                            }
                        }
                    }
                    break;
            }
        }
        return 3;
    }
}
