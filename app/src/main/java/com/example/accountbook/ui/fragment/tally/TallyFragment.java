package com.example.accountbook.ui.fragment.tally;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.database.MyDataBase;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomDialog;
import com.example.accountbook.tools.CustomStatusBarBackground;
import com.example.accountbook.tools.CustomTextWatcher;
import com.example.accountbook.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TallyFragment extends Fragment {

    private static final String TAG = "TallyFragment";
    @BindView(R.id.custom_status_bar_background)
    View customStatusBarBackground;
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.tally_save)
    Button tallySave;

    private long Time;
    private int payment = 0;
    private boolean initCalendar = false;
    private String[] paymentType;
    private String Money;
    private String Remark;
    private TextView title;
    private String dateStr;

    private View statusBar;
    private Spinner sSpinner;
    private Calendar calendar = Calendar.getInstance();
    private IncomeEntity incomeEntity;
    private ArrayAdapter<String> sSpinnerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tally, container, false);
        statusBar = root.findViewById(R.id.custom_status_bar_background);
        sSpinner = root.findViewById(R.id.income_expenses_spinner);
        ButterKnife.bind(this,root);
        EventBus.getDefault().register(this);
        CustomStatusBarBackground.drawableViewStatusBar(getContext(), R.drawable.custom_gradient_main_title, statusBar);
        title = root.findViewById(R.id.title_name);
        paymentType = getActivity().getResources().getStringArray(R.array.spingarr);
        sSpinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_item, paymentType);
        sSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_style);
        title.setText(R.string.title_tally);
        money.addTextChangedListener(new CustomTextWatcher(getContext(),money));
        sSpinner.setAdapter(sSpinnerAdapter);
        initTime();
        sSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        payment = 0;
                        break;
                    case 1:
                        payment = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    public void initTime(){
        if (initCalendar){
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Time = calendar.getTimeInMillis();
            dateStr = dateformat.format(Time);
            time.setText(dateStr);
            initCalendar = false;
        }else {
            Time = System.currentTimeMillis();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Log.d(TAG, "initTime: Time = " + Time);
            dateStr = dateformat.format(Time);
            time.setText(dateStr);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void typeStorage(IncomeEntity eventMessage){

    }

    @OnClick({R.id.tally_save,R.id.time})
    public void onViewClicked(View view) {
//        ArrayList<IncomeEntity> incomeEntities = new ArrayList<>();
//        EventBus.getDefault().post();
        switch (view.getId()){
            case R.id.tally_save:
                if (TextUtils.isEmpty(remark.getText().toString()) && TextUtils.isEmpty(money.getText().toString())){
                    ToastUtil.toast(getContext(),"请填写金额和备注。");
                }else if (TextUtils.isEmpty(remark.getText().toString())){
                    ToastUtil.toast(getContext(),"请填写备注。");
                }else if (TextUtils.isEmpty(money.getText().toString())){
                    ToastUtil.toast(getContext(),"请填写金额。");
                }else if (Double.parseDouble(money.getText().toString()) > 10000){
                    ToastUtil.toast(getContext(),"最大金额为10000。");
                } else {
                    showNormalMoreButtonDialog();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive() && getActivity().getCurrentFocus() != null) {
                        if (getActivity().getCurrentFocus().getWindowToken() != null) {
                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                }
                break;
            case R.id.time:
                CustomDialog customDialog = new CustomDialog(getContext(),Calendar.getInstance());
                customDialog.show();
                customDialog.setClickListener(new CustomDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        Log.d(TAG, "doConfirm: Picker 3" + MainActivity.getDateStringText().toString());
                        calendar.set(MainActivity.getDateStringText().getYear(),MainActivity.getDateStringText().getMonth(),MainActivity.getDateStringText().getDay(),MainActivity.getDateStringText().getHour(),MainActivity.getDateStringText().getMinute());
                        initCalendar =true;
                        initTime();
                        customDialog.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        customDialog.dismiss();
                    }
                });
                break;
        }
    }

    private void showNormalMoreButtonDialog() {
        CustomDialog customDialog = new CustomDialog(getContext(), getString(R.string.tallyFragment_dialog_normal_more_button_content), getString(R.string.tallyFragment_dialog_btn_confirm_text), getString(R.string.tallyFragment_dialog_btn_cancel_text));
        customDialog.show();
        customDialog.setClickListener(new CustomDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                Remark = String.valueOf(remark.getText());
                Money = String.valueOf(money.getText());
                try {
                    incomeEntity = new IncomeEntity();
                    incomeEntity.setIncomeType(payment);
                    Log.d(TAG, "onClick: IncomeType = " + incomeEntity.getIncomeType());
                    incomeEntity.setIncomeRemark(Remark);
                    incomeEntity.setIncomeMoney(Money);
                    incomeEntity.setIncomeTime(Time);
                    remark.setText("");
                    money.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyDataBase.getInstance().getIncomeDao().insert(incomeEntity);
                            List<IncomeEntity> sIncomeEntity = MyDataBase.getInstance().getIncomeDao().getAllIncome();
                            Log.d(TAG, "run: " + sIncomeEntity.toString());
                        }
                    }).start();
                    MainActivity.getIncomeRecycleAdapter().addItem(incomeEntity);
                    ToastUtil.toast(getActivity(), getString(R.string.tallyFragment_dialog_btn_confirm_succeed_hint_text));
                    customDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    remark.setText(Remark);
                    money.setText(Money);
                    ToastUtil.toast(getActivity(), getString(R.string.tallyFragment_dialog_btn_confirm_defeated_hint_text));
                    customDialog.dismiss();
                }
            }

            @Override
            public void doCancel() {
                ToastUtil.toast(getActivity(), getString(R.string.tallyFragment_dialog_btn_cancel_hint_text));
                customDialog.dismiss();
            }
        });
    }
    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }
}