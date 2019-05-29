package com.example.lp.ddncredit.datebase;

import org.litepal.crud.LitePalSupport;
/**
 * 教师考勤信息表
 *lp
 * 2019/05/29
 * */
public class StaffAttendInfoDb extends LitePalSupport {
    private Long  id;
    private long  staffID;
    private String createTime;
    private String picPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStaffID() {
        return staffID;
    }

    public void setStaffID(long staffID) {
        this.staffID = staffID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public String toString() {
        return "StaffAttendInfoDb{" +
                "id=" + id +
                ", staffID=" + staffID +
                ", createTime='" + createTime + '\'' +
                ", picPath='" + picPath + '\'' +
                '}';
    }
}
