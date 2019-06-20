package com.example.lp.ddncredit.mainview.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示的考勤Bean
 * */
public class AttendShowBean {
    private String relation;
    private String babyname;
    private String clazzname;
    private String icnumber;
    private String attendtime;

    private List<String> reletions=new ArrayList<>();
    private List<String> urls=new ArrayList<>();

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getBabyname() {
        return babyname;
    }

    public void setBabyname(String babyname) {
        this.babyname = babyname;
    }

    public String getClazzname() {
        return clazzname;
    }

    public void setClazzname(String clazzname) {
        this.clazzname = clazzname;
    }

    public String getIcnumber() {
        return icnumber;
    }

    public void setIcnumber(String icnumber) {
        this.icnumber = icnumber;
    }

    public String getAttendtime() {
        return attendtime;
    }

    public void setAttendtime(String attendtime) {
        this.attendtime = attendtime;
    }


    public List<String> getReletions() {
        return reletions;
    }

    public void setReletions(List<String> reletions) {
        this.reletions = reletions;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "AttendShowBean{" +
                "relation='" + relation + '\'' +
                ", babyname='" + babyname + '\'' +
                ", clazzname='" + clazzname + '\'' +
                ", icnumber='" + icnumber + '\'' +
                ", attendtime='" + attendtime + '\'' +
                ", reletions=" + reletions +
                ", urls=" + urls +
                '}';
    }
}
