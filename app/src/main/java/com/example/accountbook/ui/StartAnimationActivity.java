/***
 * Copyright (c) 2019 ASKEY Computer Corp. and/or its affiliates. All rights reserved.
 * Created by Allen Luo on 2019/10/15
 * Description: [PT-38][Intranet Chat] [APP][UI]Program launch page and animation
 */
package com.example.accountbook.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.database.MyDataBase;
import com.example.accountbook.tools.CustomStatusBarBackground;
import com.example.accountbook.tools.ToastUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartAnimationActivity extends AppCompatActivity {
    private final String TAG = "StartAnimationActivity";

    @BindView(R.id.start_animation_logo)
    ImageView sAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startanimation);
        CustomStatusBarBackground.customStatusBarTransparent(this);
        ButterKnife.bind(this);

        rotateyAnimRun(sAnimation);

        File images = this.getExternalFilesDir("images");
        File avatar = this.getExternalFilesDir("avatar");
        File voice = this.getExternalFilesDir("voice");
        File video = this.getExternalFilesDir("video");
        File receive = this.getExternalFilesDir("receive");

        MyDataBase.init(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.setIncomeEntities(MyDataBase.getInstance().getIncomeDao().getAllIncome());
            }
        }).start();
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent login = new Intent(StartAnimationActivity.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        },2001);
    }

    public void rotateyAnimRun(View view)
    {
        AnimatorSet set=new AnimatorSet();
        ObjectAnimator animatorTranslate=ObjectAnimator.ofFloat(sAnimation,"translationY",0,0);
        ObjectAnimator animatorScaleX=ObjectAnimator.ofFloat(sAnimation,"ScaleX",1f,2f);
        ObjectAnimator animatorScaleY=ObjectAnimator.ofFloat(sAnimation,"ScaleY",1f,2f);
        ObjectAnimator animatorAlpha=ObjectAnimator.ofFloat(sAnimation,"alpha",1f,2f);
        set.play(animatorTranslate)
                .with(animatorScaleX).with(animatorScaleY).with(animatorAlpha);
        set.setDuration(2000);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
