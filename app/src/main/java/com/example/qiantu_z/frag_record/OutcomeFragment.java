package com.example.qiantu_z.frag_record;

import com.example.qiantu_z.R;
import com.example.qiantu_z.db.DBManager;
import com.example.qiantu_z.db.TypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment {

    public void loadDataToGv() {
        super.loadDataToGv();
        //获取数据库当中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita);

    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemtoAccounttb(accountBean);

    }

}
