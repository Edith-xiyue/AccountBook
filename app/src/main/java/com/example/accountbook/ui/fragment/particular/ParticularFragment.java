package com.example.accountbook.ui.fragment.particular;

import android.os.Bundle;
import android.util.Log;
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
import com.example.accountbook.tools.DateCalculation;
import com.example.accountbook.tools.EventBusConfig;

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
    @BindView(R.id.no_record_text)
    TextView noIncomeWarning;

    private boolean errorTime = true;
    private List<IncomeEntity> incomeEntities = MainActivity.getIncomeEntities();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_particular, container, false);
        ButterKnife.bind(this, root);
        initView();
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (MainActivity.getIncomeEntities().size() == 0) {
            noIncomeWarning.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noIncomeWarning.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void viewUpdate(EventBusConfig.WarningView warningView) {
        if (warningView.isWarningViewShow()) {
            noIncomeWarning.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noIncomeWarning.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void initView() {
        CustomStatusBarBackground.drawableViewStatusBar(getContext(), R.drawable.custom_gradient_main_title, statusBar);
        if (MainActivity.getIncomeEntities().size() == 0) {
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
        Log.d(TAG, "onViewClicked: 搜索按钮被按下。");
        String filterDate = new String();
        String year = searchYear.getText().toString();
        String month = searchMonth.getText().toString();
        String day = searchDay.getText().toString();

        filterDate = DateCalculation.exactDate(year,month,day,getContext());
        MainActivity.getIncomeRecycleAdapter().getFilter().filter(filterDate);
    }
}