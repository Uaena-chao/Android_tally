package com.example.qiantu_z.frag_record;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.example.qiantu_z.R;
import com.example.qiantu_z.db.AccountBean;
import com.example.qiantu_z.db.TypeBean;
import com.example.qiantu_z.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录界面的支出模块
 */
public abstract class BaseRecordFragment extends Fragment {

    TypeBaseAdapter adapter;
    AccountBean accountBean;
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv, beizhutv, timeTv;
    GridView typeGv;
    List<TypeBean> typeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化accountBean对象
        accountBean = new AccountBean();
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);

        setInitTime();
        //给GirdView填充数据的方法
        loadDataToGv();
        setGVListenner();
        return view;
    }


    //获取当前时间，显示在TimeTv上
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        accountBean.setYear(year);
        int month = calendar.get(Calendar.MONTH) + 1;
        accountBean.setMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setDay(day);

    }

    //设置GirdView每一项的点击事件
    private void setGVListenner() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged();

                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageid();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);
            }
        });
    }

    public void loadDataToGv() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        //获取数据库当中的数据源
//        List<TypeBean>outlist = DBManager.getTypeList(0);
//        typeList.addAll(outlist);
//        adapter.notifyDataSetChanged();
        //在子类中重写，故注释掉

    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);
        beizhutv = view.findViewById(R.id.frag_record_tv_mark);

        //让自定义键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();

        //设置监听，确定按钮被点击了
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneystr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneystr) || moneystr.equals("0")) {
                    getActivity().finish();
                    return;
                } else {
                    Float money = Float.parseFloat(moneystr);
                    accountBean.setMoney(money);
                }
                //点击了确认按钮，存储数据到数据库
                saveAccountToDB();
                //并返回上一级
                getActivity().finish();

            }
        });

        beizhutv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bz = new EditText(getContext());
                bz.setInputType(InputType.TYPE_CLASS_TEXT);
                bz.setHint("请输入备注");
                new AlertDialog.Builder(getContext())
                        .setTitle("添加备注")
                        .setView(bz)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String msg = bz.getText().toString().trim();
//                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                if (!TextUtils.isEmpty(msg)) {
                                    beizhutv.setText(msg);
                                    accountBean.setBeizhu(msg);
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStr = timeTv.getText().toString();
                String yearStr = TextUtils.substring(timeStr, 0, 4);
                String monthStr = TextUtils.substring(timeStr, 5, 7);
                String dayStr = TextUtils.substring(timeStr, 8, 10);
                String hourStr = TextUtils.substring(timeStr, 12, 14);
                String minStr = TextUtils.substring(timeStr, 15, 17);
                TimePickerDialog tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int y = accountBean.getYear();
                        int m = accountBean.getMonth();
                        int d = accountBean.getDay();
                        String monthS = "" + m, dS = "" + d, hS = "" + hourOfDay, minuteS = "" + minute;
                        if ((m + 1) < 10) {
                            monthS = "0" + monthS;
                        }
                        if (d < 10) {
                            dS = "0" + d;
                        }
                        if (hourOfDay < 10) {
                            hS = "0" + hourOfDay;
                        }
                        if (minute < 10) {
                            minuteS = "0" + minute;
                        }
                        String t = y + "年" + monthS + "月" + dS + "日" + " " + hS + ":" + minuteS;
                        accountBean.setTime(t);
                        timeTv.setText(t);
                    }
                }, Integer.parseInt(hourStr), Integer.parseInt(minStr), true);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        accountBean.setYear(year);
                        accountBean.setMonth(month + 1);
                        accountBean.setDay(dayOfMonth);
                        tpd.show();
                    }
                }, Integer.parseInt(yearStr), Integer.parseInt(monthStr), Integer.parseInt(dayStr));
                dpd.show();

            }
        });
    }

    public abstract void saveAccountToDB();
}