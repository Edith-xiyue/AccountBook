package com.example.accountbook.ui.fragment.summarizing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accountbook.R;
import com.example.accountbook.tools.CustomStatusBarBackground;

public class SummarizingFragment extends Fragment {

    private View statusBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_summarizing, container, false);
        statusBar = root.findViewById(R.id.custom_status_bar_background);
        CustomStatusBarBackground.drawableViewStatusBar(getContext(),R.drawable.custom_gradient_main_title,statusBar);
        final TextView textView = root.findViewById(R.id.title_name);
        textView.setText(R.string.title_summarizing);
        return root;
    }
}