package com.example.qiantu_z.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
 * 负责管理数据库
 * 对表当中的数据进行操作：增删改查
 */
public class DBManager {

    private static SQLiteDatabase db;

    /*初始化数据库对象*/
    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }


    /*
     * 读
     * kind：表示收入或支出
     * */
    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        //读取typetb当中的数据
        String sql = "select * from typetb where kind = " + kind;
        Cursor cursor = db.rawQuery(sql, null);
        //循环读取游标内容
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            int imageid = cursor.getInt(cursor.getColumnIndexOrThrow("imageId"));
            int simageid = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageid, simageid, kind1);
            list.add(typeBean);
        }
        cursor.close();
        return list;
    }


    /*
     * 向记账表添加一条元素
     * */
    public static void insertItemtoAccounttb(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("typename", bean.getTypename());
        values.put("sImageId", bean.getsImageId());
        values.put("beizhu", bean.getBeizhu());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());
        db.insert("accounttb", null, values);
    }

    /*
     * 获取记账表当中某一天的所有记录
     * */
    public static List<AccountBean> getAccountListOneDayFromAccounttb(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by time desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //便利符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取记账表当中某一月的所有记录
     * */
    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year, int month) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by time desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //便利符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, 1, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取记账表当中某一月的支出或者收入的所有记录
     * */
    public static List<AccountBean> getAccountListOneMonthByKindFromAccounttb(int year, int month, int kind) {
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and kind=? order by time desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        //便利符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndexOrThrow("beizhu"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, 1, kind1);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取某一天的收入或者支出的总金额
     * kind： 支出——0 收入——1
     * */
    public static float getSumMoneyOneDay(int year, int month, int day, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        //遍历
        if (cursor.moveToNext()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }


    /*
     * 获取某一月的收入或者支出的总金额
     * kind： 支出——0 收入——1
     * */
    public static float getSumMoneyOneMonth(int year, int month, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        //遍历
        if (cursor.moveToNext()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
     * 统计某月份支出或收入总项数
     * */
    public static int getCountItemOneMonth(int year, int month, int kind) {
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(money)"));
            total = count;
        }
        return total;
    }

    /*
     * 获取某一年的收入或者支出的总金额
     * kind： 支出——0 收入——1
     * */
    public static float getSumMoneyOneYear(int year, int kind) {
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        //遍历
        if (cursor.moveToNext()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total = money;
        }
        return total;
    }

    //删除表中某一项
    public static int deleteItemFromAccounttbById(int id) {
        return db.delete("accounttb", "id=?", new String[]{id + ""});
    }

    //删除表中所有项
    public static void deleteAllFromAccounttbById() {
        db.execSQL("delete from accounttb");
    }
}