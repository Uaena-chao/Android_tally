package com.example.qiantu_z.db;

public class TypeBean {
    int id;
    String typename;     //类型名称
    int imageid;         //未被选中的图片
    int simageid;        //被选中的图片
    int kind;            //收入——1 支出——0

    public TypeBean() {
    }

    public TypeBean(int id, String typename, int imageid, int simageid, int kind) {
        this.id = id;
        this.typename = typename;
        this.imageid = imageid;
        this.simageid = simageid;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public int getSimageid() {
        return simageid;
    }

    public void setSimageid(int simageid) {
        this.simageid = simageid;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
