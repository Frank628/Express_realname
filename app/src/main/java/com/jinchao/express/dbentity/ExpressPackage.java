package com.jinchao.express.dbentity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by OfferJiShu01 on 2016/7/28.
 */
@Table(name = "express")
public class ExpressPackage implements Serializable{
    @Column(name = "id",isId = true,autoGen=true)
    private int id;
    @Column(name = "idcard")
    private String idcard;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birth")
    private String birth;
    @Column(name = "nation")
    private String nation;
    @Column(name = "packagepic")
    private String packagepic;
    @Column(name = "expresspic")
    private String expresspic;
    @Column(name = "personpic")
    private String personpic;
    @Column(name = "address")
    private String address;
    @Column(name = "yundanhao")
    private String yundanhao;
    @Column(name = "time")
    private String time;
    public ExpressPackage() {
    }

    public ExpressPackage(String idcard, String name, String gender, String birth, String nation, String packagepic, String expresspic, String personpic, String address, String yundanhao) {
        this.idcard = idcard;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.nation = nation;
        this.packagepic = packagepic;
        this.expresspic = expresspic;
        this.personpic = personpic;
        this.address = address;
        this.yundanhao = yundanhao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPackagepic() {
        return packagepic;
    }

    public void setPackagepic(String packagepic) {
        this.packagepic = packagepic;
    }

    public String getExpresspic() {
        return expresspic;
    }

    public void setExpresspic(String expresspic) {
        this.expresspic = expresspic;
    }

    public String getPersonpic() {
        return personpic;
    }

    public void setPersonpic(String personpic) {
        this.personpic = personpic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYundanhao() {
        return yundanhao;
    }

    public void setYundanhao(String yundanhao) {
        this.yundanhao = yundanhao;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ExpressPackage{" +
                "id=" + id +
                ", idcard='" + idcard + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                ", nation='" + nation + '\'' +
                ", packagepic='" + packagepic + '\'' +
                ", expresspic='" + expresspic + '\'' +
                ", personpic='" + personpic + '\'' +
                ", address='" + address + '\'' +
                ", yundanhao='" + yundanhao + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
