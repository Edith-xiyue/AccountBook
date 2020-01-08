package com.example.accountbook.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ToastUtil {

    private static Timer timer = new Timer();
    private static customTimerTask timerTask;
    private static Toast myToast;

    public static void toast(Context context, String text){
        if (myToast != null) {
            myToast.cancel();
        }
        myToast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        myToast.show();
    }

    public static void cancelToast(Context context){
        if (myToast != null) {
            myToast.cancel();
        }
    }

    public static void timingToast(Context context, String text, int time){
        toast(context,text);
        timerTask = new customTimerTask();
        timer.schedule(timerTask,time);
    }

    static class customTimerTask extends TimerTask{

        private static final String TAG = "timerTask";

        @Override
        public void run() {
            if (myToast != null) {
                myToast.cancel();
            }
            this.cancel();
            Log.d(TAG, "run:  TimerTask 已经被cancel");
        }
    }
}