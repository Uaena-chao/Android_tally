package com.example.qiantu_z;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.qiantu_z.adapter.RecordPagerAdapter;
import com.example.qiantu_z.frag_record.IncomeFragment;
import com.example.qiantu_z.frag_record.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout = findViewById(R.id.record_iv_tabs);
        viewPager = findViewById(R.id.record_vp);

        initPager();
    }

    private void initPager() {
        //初始化ViewPager页面的集合
        List<Fragment> fragmentList = new ArrayList<>();

        OutcomeFragment outFrag = new OutcomeFragment(); // 支出
        IncomeFragment inFrag = new IncomeFragment();  // 收入
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        //创建适配器
        RecordPagerAdapter pageradapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        //设置适配器
        viewPager.setAdapter(pageradapter);
        //将tablayout与viewpager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }

    //    点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}