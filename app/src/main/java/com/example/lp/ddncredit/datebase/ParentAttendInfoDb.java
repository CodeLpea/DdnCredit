package com.example.lp.ddncredit.datebase;

import org.litepal.crud.LitePalSupport;
/**
 * 家长考勤信息表
 *lp
 * 2019/05/29
 * */
public class ParentAttendInfoDb  extends LitePalSupport{
    private Long  id;
    private long  studentID;
    private long  parentID;
    private String createTime;
    private String  picPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
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
}
