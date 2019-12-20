package com.example.accountbook.ui.fragment.particular;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.adapter.IncomeRecycleAdapter;
import com.example.accountbook.database.MyDataBase;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomStatusBarBackground;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ParticularFragment extends Fragment {
    private static final String TAG = "ParticularFragment";

    private View statusBar;
    private List<IncomeEntity> incomeEntities = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView title;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_particular, container, false);
        statusBar = root.findViewById(R.id.custom_status_bar_background);
        title = root.findViewById(R.id.title_name);
        recyclerView = root.findViewById(R.id.particular_recycler);
        initView();
        return root;
    }
    public void initView(){
        CustomStatusBarBackground.drawableViewStatusBar(getContext(),R.drawable.custom_gradient_main_title,statusBar);
        title.setText(R.string.title_particular);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        MainActivity.setIncomeRecycleAdapter(new IncomeRecycleAdapter(getContext(),incomeEntities));
        recyclerView.setAdapter(MainActivity.getIncomeRecycleAdapter());

        new Thread(new Runnable() {
            @Override
            public void run() {
                incomeEntities = MyDataBase.getInstance().getIncomeDao().getAllIncome();
                Log.d(TAG, "run: incomeEntities.size() = " + incomeEntities.size());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: runOnUiThread");
                        if (MainActivity.getIncomeRecycleAdapter() != null){
                            MainActivity.getIncomeRecycleAdapter().addAllItem(incomeEntities);
                        }
                    }
                });
            }
        }).start();
    }
}