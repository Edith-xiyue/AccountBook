package com.example.accountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.database.MyDataBase;
import com.example.accountbook.database.dao.IncomeDao;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomDialog;
import com.example.accountbook.tools.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class IncomeRecycleAdapter extends RecyclerView.Adapter<IncomeRecycleAdapter.IncomeRecycleHolder> {

    private static String TAG = "MyRecycleAdapter";

    private Context mContext;
    private int mPosition;
    private List<IncomeEntity> incomeEntities;

    public IncomeRecycleAdapter(Context context, List<IncomeEntity> incomeEntities){
        Log.d(TAG, "MyRecycleAdapter: " + incomeEntities.size());
        this.mContext = context;
        this.incomeEntities = incomeEntities;
    }

    @NonNull
    @Override
    public IncomeRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycle_particular, parent, false);
        IncomeRecycleHolder incomeRecycleHolder = new IncomeRecycleHolder(inflate,mContext);
        return incomeRecycleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeRecycleHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position = " + position);
        this.mPosition = position;
        holder.setsPosition(position);
    }

    @Override
    public int getItemCount() {
        return null == incomeEntities ? 0 : incomeEntities.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void addItem(IncomeEntity device){
        incomeEntities.add(device);
        for (int i =0 ;i < incomeEntities.size();i++)
            Log.d(TAG, "add: mDeviceList.name: "+ (i+1) +"="+incomeEntities.get(i).getId());
        this.notifyItemInserted(incomeEntities.size() - 1);
    }

    public void addAllItem(List<IncomeEntity> incomeEntities){
        Log.d(TAG, "addAllItem: sizi = " + incomeEntities.size());
        int start = incomeEntities.size();
        this.incomeEntities.addAll(incomeEntities);
        notifyItemRangeInserted(start,incomeEntities.size());
    }

    class IncomeRecycleHolder extends RecyclerView.ViewHolder implements GestureDetector.OnGestureListener{

        private TextView money;
        private TextView time;
        private TextView remark;
        private ConstraintLayout recycleItem;
        private GestureDetector sGestureDetector;
        private Context sContext;

        private boolean delete;
        private String dateStr;
        private int sPosition;

        public IncomeRecycleHolder(@NonNull View itemView,Context context) {
            super(itemView);
            this.sContext = context;
        }

        private void initData() {
            sGestureDetector = new GestureDetector(sContext,this);
            remark.setText(incomeEntities.get(sPosition).getIncomeRemark());
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = dateformat.format(incomeEntities.get(sPosition).getIncomeTime());
            time.setText(dateStr);
            money.setText(String.valueOf(incomeEntities.get(sPosition).getIncomeMoney()));
        }


        public void initView(){
            remark = itemView.findViewById(R.id.recycle_particular_remark);
            time = itemView.findViewById(R.id.recycle_particular_time);
            money = itemView.findViewById(R.id.recycle_particular_money);
            recycleItem = itemView.findViewById(R.id.recycler_particular_item);
            if (incomeEntities.get(sPosition).getIncomeType() == 0)
                recycleItem.setBackgroundResource(R.drawable.circular_bead_red);
            else
                recycleItem.setBackgroundResource(R.drawable.circular_bead_green);
        }

        public int getsPosition() {
            return sPosition;
        }

        public void setsPosition(int position) {
            this.sPosition = position;
            initView();
            initData();
            setClickListener();
        }

        public void setClickListener(){
            recycleItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return sGestureDetector.onTouchEvent(event);
                }
            });
        }

        @Override
        public boolean onDown(MotionEvent e) {
            delete = false;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            showDetailDialog(sContext,sPosition);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            CustomDialog customDialog = new CustomDialog(sContext, sContext.getString(R.string.incomeRecycleAdapter_dialog_text_show_particular), sContext.getString(R.string.incomeRecycleAdapter_dialog_text_delete_item));
            Log.d(TAG, "onLongPress: 注册点击事件。");
            customDialog.setItemClickListener(new CustomDialog.ItemClickListenerInterface() {
                @Override
                public void showParticular() {
                    Log.d(TAG, "showParticular: 点击事件？");
                    showDetailDialog(sContext,sPosition);
                    customDialog.dismiss();
                }

                @Override
                public void deleteItem() {
                    Log.d(TAG, "showParticular: 点击事件？");
                    deleteItemDialog(sContext,sPosition);
                    customDialog.dismiss();
                }
            });
            customDialog.show();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    public void showDetailDialog(Context context, int position){
        CustomDialog customDialog = new CustomDialog(context,incomeEntities.get(position));
        customDialog.show();
    }

    public void deleteItemDialog(Context context, int position){
        CustomDialog customDialog = new CustomDialog(context, context.getString(R.string.incomeRecycleAdapter_dialog_normal_more_button_content), context.getString(R.string.incomeRecycleAdapter_dialog_btn_confirm_text), context.getString(R.string.incomeRecycleAdapter_dialog_btn_cancel_text));
        customDialog.show();
        customDialog.setClickListener(new CustomDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                try{
                    IncomeEntity entity = incomeEntities.get(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IncomeDao incomeDao = MyDataBase.getInstance().getIncomeDao();
                            IncomeEntity entity1 = incomeDao.getIncomeEntity(entity.getIncomeTime());
                            incomeDao.delete(entity1);
                        }
                    }).start();
                    incomeEntities.remove(position);
                    MainActivity.getIncomeRecycleAdapter().notifyItemInserted(incomeEntities.size() - 1);;
                    ToastUtil.toast(context,context.getString(R.string.incomeRecycleAdapter_dialog_btn_confirm_succeed_hint_text));

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.toast(context,context.getString(R.string.incomeRecycleAdapter_dialog_btn_confirm_defeated_hint_text));
                }
                customDialog.dismiss();
            }

            @Override
            public void doCancel() {
                ToastUtil.toast(context,context.getString(R.string.incomeRecycleAdapter_dialog_btn_cancel_hint_text));
                customDialog.dismiss();
            }
        });
    }
}
