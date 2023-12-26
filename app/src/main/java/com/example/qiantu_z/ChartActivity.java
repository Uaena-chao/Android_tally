package com.example.qiantu_z;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qiantu_z.adapter.AccountAdapter;
import com.example.qiantu_z.db.AccountBean;
import com.example.qiantu_z.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    TextView dateTv, inTv, outTv;
    Button inBtn, outBtn;
    ListView chartLv;
    NumberPicker np_year, np_month;
    View yearandmonth_select;
    AlertDialog.Builder builder;
    List<AccountBean> mDatas;
    private int year, month, kind = 0;
    private AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initView();
        initTime();
        initStatistics(year, month);

        mDatas = new ArrayList<>();
        //设置适配器,加载每一行数据
        adapter = new AccountAdapter(ChartActivity.this, mDatas);
        chartLv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLvData();
    }

    private void refreshLvData() {
        List<AccountBean> list = DBManager.getAccountListOneMonthByKindFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();  //提示适配器数据更新
    }

    //初始化某年某月的数据
    private void initStatistics(int year, int month) {
        float insumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outsumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);
        dateTv.setText(year + "年" + month + "月账单");
        inTv.setText("共" + incountItemOneMonth + "笔收入,￥" + insumMoneyOneMonth);
        outTv.setText("共" + outcountItemOneMonth + "笔支出,￥" + outsumMoneyOneMonth);

    }


    //初始化时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    //初始化控件
    private void initView() {
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_in);
        outTv = findViewById(R.id.chart_tv_out);
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        chartLv = findViewById(R.id.chart_lv);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_calendar:
                builder = new AlertDialog.Builder(ChartActivity.this);
                yearandmonth_select = getLayoutInflater().from(ChartActivity.this).inflate(R.layout.yearandmonth_select, null);
                np_year = yearandmonth_select.findViewById(R.id.number_picker_year);
                np_month = yearandmonth_select.findViewById(R.id.number_picker_month);
                np_year.setMinValue(1949);
                np_year.setMaxValue(2049);
                np_month.setMinValue(1);
                np_month.setMaxValue(12);
                np_year.setValue(year);
                np_month.setValue(month);
                builder.setView(yearandmonth_select);    // 指定自定义布局
                builder.setTitle("选择年月")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                year = np_year.getValue();
                                month = np_month.getValue();
                                initStatistics(year, month);
                                Log.d("zhu", String.valueOf(year));
                                Log.d("zhu", String.valueOf(month));
                                refreshLvData();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();

                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                kind = 0;
                refreshLvData();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                kind = 1;
                refreshLvData();
                break;
        }
    }

    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setTextColor(Color.BLACK);
            inBtn.setTextColor(Color.WHITE);
        }
        if (kind == 1) {
            outBtn.setTextColor(Color.WHITE);
            inBtn.setTextColor(Color.BLACK);
        }
    }
}