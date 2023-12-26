package com.example.qiantu_z;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qiantu_z.db.DBManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:
                finish();
                break;
            case R.id.setting_clearall:
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("确认")
                        .setMessage("本次操作不可逆，确认要删除全部记录吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.deleteAllFromAccounttbById();

                                Toast.makeText(SettingActivity.this, "删除成功", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;

        }
    }
}