package com.example.qiantu_z;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qiantu_z.adapter.AccountAdapter;
import com.example.qiantu_z.db.AccountBean;
import com.example.qiantu_z.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView todayLv; //展示今日收支情况
    //声名数据源
    List<AccountBean> mDatas;
    int year, month, day;
    TextView main_top_time;
    //头布局相关控件
    TextView topouttv, topintv, toplefttv, dayouttv, dayintv;
    private AccountAdapter adapter;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        inittime();
        main_top_time.setText(Html.fromHtml("<u>" + year + "-" + month + "-" + day + "<u>"));
        //添加listview的头布局
        addListViewHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器,加载每一行数据
        adapter = new AccountAdapter(MainActivity.this, mDatas);
        todayLv.setAdapter(adapter);
        //设置ListView的长按事件
        setLvLongClickListener();
    }

    //初始化控件
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        main_top_time = findViewById(R.id.main_tv_nowtime);
    }

    //添加listview头布局
    private void addListViewHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_main_listview_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局控件
        topouttv = headerView.findViewById(R.id.item_main_listview_top_allout);
        topintv = headerView.findViewById(R.id.item_main_listview_top_allin);
        toplefttv = headerView.findViewById(R.id.item_main_listview_top_allleft);
        dayouttv = headerView.findViewById(R.id.item_main_listview_top_dayout);
        dayintv = headerView.findViewById(R.id.item_main_listview_top_dayin);
    }

    //获取当前时间
    private void inittime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //当此activity获取焦点时调用此方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopShow();
    }

    private void setTopShow() {
        float outcomeSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        float incomeSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeSumMoneyOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        float incomeSumMoneyOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        topouttv.setText("￥ " + String.valueOf(outcomeSumMoneyOneMonth));
        topintv.setText("￥ " + String.valueOf(incomeSumMoneyOneMonth));
        toplefttv.setText("￥ " + String.valueOf((incomeSumMoneyOneMonth - outcomeSumMoneyOneMonth)));
        dayouttv.setText("￥ " + String.valueOf(outcomeSumMoneyOneDay));
        dayintv.setText("￥ " + String.valueOf(incomeSumMoneyOneDay));
    }

    //在onResume时更新数据
    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();  //提示适配器数据更新

    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.main_iv_list:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                break;
            case R.id.main_iv_chart:
                intent = new Intent(MainActivity.this, ChartActivity.class);
                break;
            case R.id.main_ib_plusone:
                intent = new Intent(MainActivity.this, RecordActivity.class);
                break;
            case R.id.main_tv_nowtime:
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        MainActivity.this.year = year;
                        MainActivity.this.month = month + 1;
                        MainActivity.this.day = dayOfMonth;
                        String monthStr = "" + MainActivity.this.month;
                        String dayStr = "" + MainActivity.this.day;
                        if (MainActivity.this.month < 10) {
                            monthStr = "0" + MainActivity.this.month;
                        }
                        if (MainActivity.this.day < 10) {
                            dayStr = "0" + MainActivity.this.day;
                        }
                        main_top_time.setText(Html.fromHtml("<u>" + MainActivity.this.year + "-" + monthStr + "-" + dayStr + "<u>"));
                        loadDBData();
                        setTopShow();
                    }
                }, year, month - 1, day);
                dpd.show();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }

    }

    //设置ListView的长按事件
    private void setLvLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //点击了头布局，第一条记录从1开始
                    return false;
                }
                AccountBean longClickBean = mDatas.get(position - 1); // 获取被长按的这条信息
                int longClick_id = longClickBean.getId();
                //弹出确认是否删除对话框
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确认")
                        .setMessage("确认要删除此项记录吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.deleteItemFromAccounttbById(longClick_id);
                                mDatas.remove(longClickBean);
                                adapter.notifyDataSetChanged();   //提示适配器数据更新
                                setTopShow();   //更新头布局
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return false;
            }
        });
    }
}