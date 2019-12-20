package com.example.accountbook.tools;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
    private EditText editText;
    private Context context;
    private int digits = 2;

    public CustomTextWatcher(Context context,EditText editText) {
        this.editText = editText;
        this.context = context;
    }
    public CustomTextWatcher setDigits(int d) {
        digits = d;
        return this;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        //删除“.”后面超过2位后的数据
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + digits+1);
                editText.setText(s);
                editText.setSelection(s.length()); //光标移到最后
                ToastUtil.toast(context,"最多输入两位小数");
            }
        }
        //如果"."在起始位置,则起始位置自动补0
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            StringBuffer stringBuffer = new StringBuffer(s);
            stringBuffer.deleteCharAt(0);
            editText.setText(stringBuffer);
            editText.setSelection(1);
            return;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
