package com.example.accountbook.ui.fragment.particular;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.adapter.IncomeRecycleAdapter;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomStatusBarBackground;
import com.example.accountbook.tools.ToastUtil;
import com.example.accountbook.tools.WarningView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParticularFragment extends Fragment {

    private static final String TAG = "ParticularFragment";

    private static boolean noIncome = false;

    @BindView(R.id.custom_status_bar_background)
    View statusBar;
    @BindView(R.id.title_name)
    TextView title;
    @BindView(R.id.search_year)
    EditText searchYear;
    @BindView(R.id.search_month)
    EditText searchMonth;
    @BindView(R.id.search_day)
    EditText searchDay;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.particular_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.particular_recycler_time)
    TextView noIncomeWarning;

    private boolean errorTime = true;
    private List<IncomeEntity> incomeEntities = MainActivity.getIncomeEntities();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_particular, container, false);
        ButterKnife.bind(this,root);
        initView();
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (MainActivity.getIncomeEntities().size() == 0){
            noIncomeWarning.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noIncomeWarning.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void viewUpdate(WarningView warningView){
        if (warningView.isWarningViewShow()){
            noIncomeWarning.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noIncomeWarning.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void initView() {
        CustomStatusBarBackground.drawableViewStatusBar(getContext(), R.drawable.custom_gradient_main_title, statusBar);
        if (MainActivity.getIncomeEntities().size() == 0){
            noIncomeWarning.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        title.setText(R.string.title_particular);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        if (MainActivity.getIncomeRecycleAdapter() == null) {
            MainActivity.setIncomeRecycleAdapter(new IncomeRecycleAdapter(getContext(), incomeEntities));
        }
        recyclerView.setAdapter(MainActivity.getIncomeRecycleAdapter());
    }

    @OnClick(R.id.search_button)
    public void onViewClicked() {
        String filterDate = "";
        if (!TextUtils.isEmpty(searchYear.getText().toString())){
            if (!TextUtils.isEmpty(searchMonth.getText().toString())){
                if (!TextUtils.isEmpty(searchDay.getText().toString())){
                    filterDate = searchYear.getText().toString() + "-" + searchMonth.getText().toString() + "-" + searchDay.getText().toString();
                    MainActivity.getIncomeRecycleAdapter().getFilter().filter(filterDate);
                }else {
                    filterDate =searchYear.getText().toString() + "-" + searchMonth.getText().toString();
                    MainActivity.getIncomeRecycleAdapter().getFilter().filter(filterDate);
                }
            }else {
                if (!TextUtils.isEmpty(searchDay.getText().toString())){
                    ToastUtil.toast(getContext(),getContext().getString(R.string.input_no_month_toast_text));
                }else {
                    filterDate = searchYear.getText().toString();
                    MainActivity.getIncomeRecycleAdapter().getFilter().filter(filterDate);
                }
            }
        }else if (TextUtils.isEmpty(searchDay.getText().toString()) && TextUtils.isEmpty(searchMonth.getText().toString())){
            MainActivity.getIncomeRecycleAdapter().getFilter().filter(filterDate);
        }else ToastUtil.toast(getContext(),getContext().getString(R.string.input_no_year_toast_text));
    }
}