package com.example.lp.ddncredit.mainview.view.adapter;
/**
* 家长刷卡传输的考勤信息
* */
public class attendParentBean {
    private photoShowBean photoShowBean;
    private String role;
    private String babyname;
    private String clazzname;
    private String icnumber;
    private String attendtime;

    public attendParentBean(com.example.lp.ddncredit.mainview.view.adapter.photoShowBean photoShowBean,
                            String role,
                            String babyname,
                            String clazzname,
                            String icnumber,
                            String attendtime) {
        this.photoShowBean = photoShowBean;
        this.role = role;
        this.babyname = babyname;
        this.clazzname = clazzname;
        this.icnumber = icnumber;
        this.attendtime = attendtime;
    }

    public attendParentBean() {
    }

    public com.example.lp.ddncredit.mainview.view.adapter.photoShowBean getPhotoShowBean() {
        return photoShowBean;
    }

    public void setPhotoShowBean(com.example.lp.ddncredit.mainview.view.adapter.photoShowBean photoShowBean) {
        this.photoShowBean = photoShowBean;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}
