package com.example.accountbook.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.database.table.IncomeEntity;

import java.text.SimpleDateFormat;


public class CustomDialog extends Dialog {
    private static final String TAG = "CustomDialog";

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private ItemClickListenerInterface itemClickListenerInterface;
    private TextView dialogShowText;
    private TextView dialogShowTime;
    private TextView dialogShowRemark;
    private TextView dialogShowParticular;
    private TextView dialogDeleteItem;
    private TimePicker customTimePicker;
    private DatePicker customDatePicker;
    private Button dialogButtonConfirm;
    private Button dialogButtonCancel;
    private View itemLine;
    private IncomeEntity entity;
    private Calendar calendar;

    private String dateStr;
    private String title;
    private String confirmButtonText;
    private String cancelButtonText;
    private String showParticularText;
    private String deleteItemText;
    private boolean selectBox = false;
    private boolean displayBox = false;
    private boolean selectItemBox =false;
    private boolean picker = false;
    private boolean next = true;
    private boolean cancel = true;
    private boolean wrongTime = false;
    private boolean nowDay = true;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public interface ItemClickListenerInterface{

        public void showParticular();

        public void deleteItem();
    }

    public CustomDialog(Context context, IncomeEntity entity){
        super(context);
        this.context = context;
        this.entity = entity;
        displayBox = true;
    }

    public CustomDialog(Context context, Calendar calendar){
        super(context);
        this.context = context;
        this.calendar = calendar;
        picker = true;
    }

    public CustomDialog(Context context, String fistItem,String secondItem) {
        super(context);
        this.context = context;
        this.deleteItemText = secondItem;
        this.showParticularText = fistItem;
        selectItemBox = true;
    }

    public CustomDialog(Context context, String title, String confirmButtonText, String cancelButtonText) {
        super(context);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cancelButtonText = cancelButtonText;
        selectBox = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    public void initView(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dialog, null);
        setContentView(view);

        customTimePicker = (TimePicker) view.findViewById(R.id.tallyFragment_timePicker);
        customDatePicker = (DatePicker) view.findViewById(R.id.tallyFragment_datePicker);
        dialogShowText = (TextView) view.findViewById(R.id.dialog_show_text);
        dialogShowTime = (TextView) view.findViewById(R.id.dialog_show_text_time);
        dialogShowRemark = (TextView) view.findViewById(R.id.dialog_show_text_remark);
        dialogButtonConfirm = (Button) view.findViewById(R.id.dialog_show_confirm_button);
        dialogButtonCancel = (Button) view.findViewById(R.id.dialog_show_cancel_button);
        dialogShowParticular = (TextView) view.findViewById(R.id.dialog_show_particular);
        dialogDeleteItem = (TextView) view.findViewById(R.id.dialog_delete_item);
        itemLine = (View) view.findViewById(R.id.item_line);

        if (selectBox){
            dialogShowText.setTextSize(24);
            dialogShowText.setTextColor(context.getColor(R.color.color_black));
            dialogShowText.setVisibility(View.VISIBLE);
            dialogButtonConfirm.setVisibility(View.VISIBLE);
            dialogButtonCancel.setVisibility(View.VISIBLE);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics();// 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.6);// 高度设置为屏幕的0.6
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white);
            dialogWindow.setAttributes(lp);
        }

        if (displayBox){
            dialogShowText.setTextSize(40);
            dialogShowText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            if (entity.getIncomeType() == 0) dialogShowText.setTextColor(context.getColor(R.color.color_red));
            else dialogShowText.setTextColor(context.getColor(R.color.color_green));
            dialogShowTime.setTextSize(14);
            dialogShowText.setVisibility(View.VISIBLE);
            dialogShowTime.setVisibility(View.VISIBLE);
            dialogShowTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            dialogShowRemark.setVisibility(View.VISIBLE);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics();// 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
            lp.height = (int) (d.heightPixels * 0.3);
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white);
            dialogWindow.setAttributes(lp);
        }

        if (selectItemBox){
            dialogShowParticular.setVisibility(View.VISIBLE);
            dialogDeleteItem.setVisibility(View.VISIBLE);
            itemLine.setVisibility(View.VISIBLE);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics();// 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.5); // 高度设置为屏幕的0
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white);
            dialogWindow.setAttributes(lp);
        }

        if (picker){
            customDatePicker.setVisibility(View.VISIBLE);
            dialogButtonConfirm.setVisibility(View.VISIBLE);
            dialogButtonCancel.setVisibility(View.VISIBLE);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics();// 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white);
            dialogWindow.setAttributes(lp);
        }
    }

    public void initData() {

        if (selectBox) {
            dialogShowText.setText(title);
            dialogButtonConfirm.setText(confirmButtonText);
            dialogButtonCancel.setText(cancelButtonText);

            dialogButtonConfirm.setOnClickListener(new clickListener());
            dialogButtonCancel.setOnClickListener(new clickListener());
        }

        if (displayBox){
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateStr = dateformat.format(entity.getIncomeTime());
            dialogShowText.setText(entity.getIncomeMoney());
            dialogShowTime.setText(dateStr);
            dialogShowRemark.setText("    "+ entity.getIncomeRemark());
        }

        if (selectItemBox){
            dialogShowParticular.setText(showParticularText);
            dialogDeleteItem.setText(deleteItemText);

            dialogShowParticular.setOnClickListener(new clickListener());
            dialogDeleteItem.setOnClickListener(new clickListener());
        }

        if (picker){
            dialogButtonConfirm.setOnClickListener(new clickListener());
            dialogButtonCancel.setOnClickListener(new clickListener());
            dialogButtonCancel.setText("取消");
            dialogButtonConfirm.setText("确定");
            DateStringText dateStringText = new DateStringText(calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),calendar.get(Calendar.MINUTE),calendar.get(Calendar.HOUR_OF_DAY));
            MainActivity.setDateStringText(dateStringText);
            customDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Log.d(TAG, "onDateChanged: wrongTime 当前时间为：" + dateStringText.toString());
                    if (dateStringText.getYear() >= year && dateStringText.getMonth() >= monthOfYear && dateStringText.getDay() >= dayOfMonth){
                        if (dateStringText.getYear() == year && dateStringText.getMonth() == monthOfYear && dateStringText.getDay() == dayOfMonth){
                            nowDay = true;
                        }else {
                            nowDay = false;
                        }
                        wrongTime = false;
                        Log.d(TAG, "onDateChanged 年月日:  wrongTime =" + wrongTime);
                        MainActivity.getDateStringText().setDay(dayOfMonth);
                        MainActivity.getDateStringText().setYear(year);
                        MainActivity.getDateStringText().setMonth(monthOfYear);
                    }else {
                        wrongTime = true;
                        Log.d(TAG, "onDateChanged 年月日:  wrongTime =" + wrongTime);
                    }
                }

            });
            customTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    if (nowDay){
                        Log.d(TAG, "onTimeChanged: wrongTime 现在时间" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                        if (calendar.get(Calendar.HOUR_OF_DAY) >= hourOfDay && calendar.get(Calendar.MINUTE) >= minute){
                            MainActivity.getDateStringText().setHour(hourOfDay);
                            MainActivity.getDateStringText().setMinute(minute);
                            wrongTime = false;
                            Log.d(TAG, "onDateChanged 时分:  wrongTime =" + wrongTime);
                        }else {
                            wrongTime = true;
                            Log.d(TAG, "onDateChanged 时分:  wrongTime =" + wrongTime);
                        }
                    }else {
                        MainActivity.getDateStringText().setHour(hourOfDay);
                        MainActivity.getDateStringText().setMinute(minute);
                        wrongTime = false;
                        Log.d(TAG, "onDateChanged 时分:  wrongTime =" + wrongTime);
                    }
                }
            });
        }
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public void setItemClickListener(ItemClickListenerInterface itemClickListenerInterface){
        Log.d(TAG, "setItemClickListener: 注册点击事件2");
        this.itemClickListenerInterface = itemClickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Log.d(TAG, "onClick: 注册点击事件5.......");
            int id = v.getId();
            switch (id) {
                case R.id.dialog_show_confirm_button:
                    if (picker){
                        if (wrongTime){
                            ToastUtil.toast(context,"请选择正确的时间！");
                        }else {
                            if (next){
                                customDatePicker.setVisibility(View.GONE);
                                customTimePicker.setVisibility(View.VISIBLE);
                                dialogButtonCancel.setText("返回");
                                next = false;
                                cancel = false;
                            }else {
                                clickListenerInterface.doConfirm();
                            }
                        }
                    }else {
                        clickListenerInterface.doConfirm();
                    }
                    break;
                case R.id.dialog_show_cancel_button:
                    if (picker){
                        if (wrongTime) {
                            ToastUtil.toast(context,"请选择正确的时间！");
                        }else{
                            if (!cancel) {
                                customDatePicker.setVisibility(View.VISIBLE);
                                customTimePicker.setVisibility(View.GONE);
                                dialogButtonCancel.setText("取消");
                                next = true;
                                cancel = true;
                            } else {
                                clickListenerInterface.doCancel();
                            }
                        }
                    }else {
                        clickListenerInterface.doCancel();
                    }
                    break;
                case R.id.dialog_show_particular:
                    itemClickListenerInterface.showParticular();
                    break;
                case R.id.dialog_delete_item:
                    itemClickListenerInterface.deleteItem();
                    break;
            }
        }
    };
}