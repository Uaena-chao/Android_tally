package com.example.qiantu_z;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
    //头布局相关控件
    TextView topouttv, topintv, toplefttv;
    private AccountAdapter adapter;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inittime();
        todayLv = findViewById(R.id.main_lv);
        //添加listview的头布局
        addListViewHeaderView();

        mDatas = new ArrayList<>();
        //设置适配器,加载每一行数据
        adapter = new AccountAdapter(MainActivity.this, mDatas);
        todayLv.setAdapter(adapter);
    }

    //添加listview头布局
    private void addListViewHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_main_listview_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局控件
        topouttv = headerView.findViewById(R.id.item_main_listview_top_allout);
        topintv = headerView.findViewById(R.id.item_main_listview_top_allin);
        toplefttv = headerView.findViewById(R.id.item_main_listview_top_allleft);
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
    }

    //在resume时更新数据
    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_list:

                break;
            case R.id.main_iv_chart:

                break;
            case R.id.main_iv_wallet:

                break;
            case R.id.main_ib_plusone:
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
                break;
        }
    }
}