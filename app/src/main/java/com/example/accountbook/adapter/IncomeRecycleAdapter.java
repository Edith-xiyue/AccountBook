package com.example.accountbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.database.MyDataBase;
import com.example.accountbook.database.dao.IncomeDao;
import com.example.accountbook.database.table.IncomeEntity;
import com.example.accountbook.tools.CustomDialog;
import com.example.accountbook.tools.EventBusConfig;
import com.example.accountbook.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IncomeRecycleAdapter extends RecyclerView.Adapter<IncomeRecycleAdapter.IncomeRecycleHolder> implements Filterable {

    private static String TAG = "MyRecycleAdapter";

    private Context mContext;
    private int mPosition;
    private int deletePosition = 0;
    private TextView textView;
    private boolean noIncome = false;
    private List<IncomeEntity> sourceIncomeEntities = new ArrayList<>();
    private static List<IncomeEntity> filterIncomeEntities = new ArrayList<>();

    public static List<IncomeEntity> getFilterIncomeEntities() {
        return filterIncomeEntities;
    }

    public IncomeRecycleAdapter(Context context, List<IncomeEntity> incomeEntities){
        this.mContext = context;
        this.sourceIncomeEntities = incomeEntities;
        this.filterIncomeEntities = incomeEntities;
    }

    @NonNull
    @Override
    public IncomeRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycle_particular, parent, false);
        IncomeRecycleHolder incomeRecycleHolder = new IncomeRecycleHolder(inflate,mContext);
        textView = inflate.findViewById(R.id.no_record_text);
        return incomeRecycleHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeRecycleHolder holder, int position) {
        this.mPosition = position;
        this.deletePosition = position;
        holder.setsPosition(position);
    }

    @Override
    public int getItemCount() {
        return null == filterIncomeEntities ? 0 : filterIncomeEntities.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void addItem(IncomeEntity device){
        sourceIncomeEntities.add(device);
        this.notifyItemInserted(sourceIncomeEntities.size() - 1);
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filterIncomeEntities = sourceIncomeEntities;
                    //没有过滤的内容，则使用源数据
                } else {
                    List<IncomeEntity> filteredList = new ArrayList<>();
                    for (int i = 0;i < sourceIncomeEntities.size(); i ++){
                        if (sourceIncomeEntities.get(i).getDate().contains(charString)){
                            filteredList.add(sourceIncomeEntities.get(i));
                        }
                    }
                    filterIncomeEntities = filteredList;
                }
                if (filterIncomeEntities.size() == 0){
                    EventBusConfig.WarningView warningView= new EventBusConfig.WarningView();
                    warningView.setWarningViewShow(true);
                    EventBus.getDefault().post(warningView);
                }else {
                    EventBusConfig.WarningView warningView= new EventBusConfig.WarningView();
                    warningView.setWarningViewShow(false);
                    EventBus.getDefault().post(warningView);
                }
                filterResults.values = filterIncomeEntities;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterIncomeEntities = (ArrayList<IncomeEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteItem(EventBusConfig.deleteIncomeEntitie delete){
        sourceIncomeEntities.remove(deletePosition);
        filterIncomeEntities.remove(mPosition);
        notifyItemRemoved(mPosition);
        IncomeRecycleAdapter.this.notifyDataSetChanged();
        ToastUtil.toast(mContext,mContext.getString(R.string.incomeRecycleAdapter_dialog_btn_confirm_succeed_hint_text));
    }

    class IncomeRecycleHolder extends RecyclerView.ViewHolder implements GestureDetector.OnGestureListener{

        private TextView money;
        private TextView time;
        private TextView remark;
        private ConstraintLayout recycleItem;
        private GestureDetector sGestureDetector;
        private Context sContext;

        private String dateStr;
        private int sPosition;

        public IncomeRecycleHolder(@NonNull View itemView,Context context) {
            super(itemView);
            this.sContext = context;
        }

        private void initData() {
            sGestureDetector = new GestureDetector(sContext,this);
            remark.setText(filterIncomeEntities.get(sPosition).getIncomeRemark());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = dateFormat.format(filterIncomeEntities.get(sPosition).getIncomeTime());
            time.setText(dateStr);
            money.setText(String.valueOf(filterIncomeEntities.get(sPosition).getIncomeMoney()));
        }


        public void initView(){
            remark = itemView.findViewById(R.id.recycle_particular_remark);
            time = itemView.findViewById(R.id.recycle_particular_time);
            money = itemView.findViewById(R.id.recycle_particular_money);
            recycleItem = itemView.findViewById(R.id.recycler_particular_item);
            if (filterIncomeEntities.get(sPosition).getIncomeType() == 0)
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
            Log.d(TAG, "alterIncomeEntities 1: " + filterIncomeEntities.get(sPosition).toString());
            String[] strings = { sContext.getString(R.string.incomeRecycleAdapter_dialog_text_show_particular), sContext.getString(R.string.incomeRecycleAdapter_dialog_text_delete_item),sContext.getString(R.string.incomeRecycleAdapter_dialog_text_alter_item)};
            CustomDialog customDialog = new CustomDialog(sContext,strings);
            customDialog.setItemClickListener(new CustomDialog.ItemClickListenerInterface() {
                @Override
                public void showParticular() {
                    showDetailDialog(sContext,sPosition);
                    customDialog.dismiss();
                }

                @Override
                public void deleteItem() {
                    deleteItemDialog(sContext,sPosition);
                    customDialog.dismiss();
                }

                @Override
                public void alterItem() {
                    Log.d(TAG, "alterIncomeEntities 2: " + filterIncomeEntities.get(sPosition).toString());
                    EventBus.getDefault().post(new EventBusConfig.alterIncomeEntitie(filterIncomeEntities.get(sPosition)));
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
        CustomDialog customDialog = new CustomDialog(context, sourceIncomeEntities.get(position));
        customDialog.show();
    }

    public void deleteItemDialog(Context context, int position){
        CustomDialog customDialog = new CustomDialog(context, context.getString(R.string.incomeRecycleAdapter_dialog_normal_more_button_content), context.getString(R.string.incomeRecycleAdapter_dialog_btn_confirm_text), context.getString(R.string.incomeRecycleAdapter_dialog_btn_cancel_text));
        customDialog.show();
        customDialog.setClickListener(new CustomDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                try{
                    IncomeEntity entity = null;
                    int filtered = filterIncomeEntities.size();
                    if(filtered != 0){
                        entity = filterIncomeEntities.get(position);
                        for (int i = 0;i < sourceIncomeEntities.size();i++){
                            if (sourceIncomeEntities.get(i).equals(entity)){
                                deletePosition = i;
                            }
                        }
                    }else {
                        entity = sourceIncomeEntities.get(position);
                    }
                    IncomeEntity finalEntity = entity;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IncomeDao incomeDao = MyDataBase.getInstance().getIncomeDao();
                            IncomeEntity entity1 = incomeDao.getIncomeEntity(finalEntity.getIncomeTime());
                            incomeDao.delete(entity1);
                            EventBus.getDefault().post(new EventBusConfig.deleteIncomeEntitie());
                        }
                    }).start();

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