package com.example.accountbook.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    private TextView dialogAlterItem;
    private TimePicker customTimePicker;
    private DatePicker customDatePicker;
    private Button dialogButtonConfirm;
    private Button dialogButtonCancel;
    private View itemLine;
    private View itemLine01;
    private IncomeEntity entity;
    private Calendar calendar;

    private int presentYear;
    private int presentMonth;
    private int presentDay;
    private String dateStr;
    private String title;
    private String confirmButtonText;
    private String cancelButtonText;
    private String showParticularText;
    private String deleteItemText;
    private String alterItemText;
    private boolean selectBox = false;
    private boolean displayBox = false;
    private boolean selectItemBox =false;
    private boolean picker = false;
    private boolean next = true;
    private boolean cancel = true;
    private boolean errorTime = false;
    private boolean nowDay = true;

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public interface ItemClickListenerInterface{

        public void showParticular();

        public void deleteItem();

        public void alterItem();
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

    public CustomDialog(Context context, String[] strings) {
        super(context);
        this.context = context;
        this.alterItemText = strings[2];
        this.deleteItemText = strings[1];
        this.showParticularText = strings[0];
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
        dialogAlterItem = (TextView) view.findViewById(R.id.dialog_alter_item);
        itemLine = (View) view.findViewById(R.id.item_line);
        itemLine01 = (View) view.findViewById(R.id.item_line01);

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
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white_no_frame);
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
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white_no_frame);
            dialogWindow.setAttributes(lp);
        }

        if (selectItemBox){
            dialogShowParticular.setVisibility(View.VISIBLE);
            dialogDeleteItem.setVisibility(View.VISIBLE);
            dialogAlterItem.setVisibility(View.VISIBLE);
            itemLine.setVisibility(View.VISIBLE);
            itemLine01.setVisibility(View.VISIBLE);

            Window dialogWindow = getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = context.getResources().getDisplayMetrics();// 获取屏幕宽、高用
            lp.width = (int) (d.widthPixels * 0.5); // 高度设置为屏幕的0
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white_no_frame);
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
            dialogWindow.setBackgroundDrawableResource(R.drawable.circular_bead_white_no_frame);
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
            dialogShowText.setText(String.valueOf(entity.getIncomeMoney()));
            dialogShowTime.setText(dateStr);
            dialogShowRemark.setText("    "+ entity.getIncomeRemark());
        }

        if (selectItemBox){
            dialogShowParticular.setText(showParticularText);
            dialogDeleteItem.setText(deleteItemText);
            dialogAlterItem.setText(alterItemText);

            dialogShowParticular.setOnClickListener(new clickListener());
            dialogDeleteItem.setOnClickListener(new clickListener());
            dialogAlterItem.setOnClickListener(new clickListener());
        }

        if (picker){
            dialogButtonConfirm.setOnClickListener(new clickListener());
            dialogButtonCancel.setOnClickListener(new clickListener());
            dialogButtonCancel.setText(context.getString(R.string.cancel_string));
            dialogButtonConfirm.setText(context.getString(R.string.confirm_string));
            DateStringText dateStringText = new DateStringText(calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),calendar.get(Calendar.MINUTE),calendar.get(Calendar.HOUR_OF_DAY));
            MainActivity.setDateStringText(dateStringText);
            presentYear = calendar.get(Calendar.YEAR);
            presentMonth = calendar.get(Calendar.MONTH);
            presentDay = calendar.get(Calendar.DAY_OF_MONTH);
            customDatePicker.init(presentYear, presentMonth, presentDay, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    if (presentYear < year) {
                        errorTime = true;
                    } else if (presentYear == year){
                        if (presentMonth < monthOfYear){
                            errorTime = true;
                        }else {
                            if (presentDay < dayOfMonth)
                                errorTime = true;
                            else {
                                if (presentYear == year && presentMonth == monthOfYear && presentDay == dayOfMonth){
                                    nowDay = true;
                                }else {
                                    nowDay = false;
                                }
                                errorTime = false;
                                MainActivity.getDateStringText().setDay(dayOfMonth);
                                MainActivity.getDateStringText().setYear(year);
                                MainActivity.getDateStringText().setMonth(monthOfYear);
                            }
                        }
                    }else {
                        errorTime = false;
                        MainActivity.getDateStringText().setDay(dayOfMonth);
                        MainActivity.getDateStringText().setYear(year);
                        MainActivity.getDateStringText().setMonth(monthOfYear);
                    }
                }
            });
            customTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    if (nowDay){
                        if (calendar.get(Calendar.HOUR_OF_DAY) >= hourOfDay && calendar.get(Calendar.MINUTE) >= minute){
                            MainActivity.getDateStringText().setHour(hourOfDay);
                            MainActivity.getDateStringText().setMinute(minute);
                            errorTime = false;
                        }else {
                            errorTime = true;
                        }
                    }else {
                        MainActivity.getDateStringText().setHour(hourOfDay);
                        MainActivity.getDateStringText().setMinute(minute);
                        errorTime = false;
                    }
                }
            });
        }
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public void setItemClickListener(ItemClickListenerInterface itemClickListenerInterface){
        this.itemClickListenerInterface = itemClickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.dialog_show_confirm_button:
                    if (picker){
                        if (errorTime){
                            ToastUtil.toast(context,context.getString(R.string.select_error_time_toast_text));
                        }else {
                            if (next){
                                customDatePicker.setVisibility(View.GONE);
                                customTimePicker.setVisibility(View.VISIBLE);
                                dialogButtonCancel.setText(context.getString(R.string.return_string));
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
                        if (!cancel) {
                            errorTime = false;
                            customDatePicker.setVisibility(View.VISIBLE);
                            customTimePicker.setVisibility(View.GONE);
                            dialogButtonCancel.setText(context.getString(R.string.cancel_string));
                            next = true;
                            cancel = true;
                        } else {
                            clickListenerInterface.doCancel();
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
                case R.id.dialog_alter_item:
                    itemClickListenerInterface.alterItem();
            }
        }
    };
}