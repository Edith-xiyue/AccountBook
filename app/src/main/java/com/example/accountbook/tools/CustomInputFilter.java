package com.example.accountbook.tools;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

public class CustomInputFilter implements InputFilter {

    public static final int maxMoney = 10000;
    public static final int decimalPlace = 2;
    public static final String numberDecimal = ".";
    Pattern pattern;

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return null;
    }
}
