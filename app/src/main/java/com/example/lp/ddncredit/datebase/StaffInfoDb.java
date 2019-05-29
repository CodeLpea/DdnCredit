package com.example.lp.ddncredit.datebase;

import org.litepal.crud.LitePalSupport;
/**
 * 教师信息表
 *lp
 * 2019/05/29
 * */
public class StaffInfoDb extends LitePalSupport {
    private Long id;
    private long  staffID;
    private long rfid;
    private String name;
    private String phone;
    private int type;

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

    public long getRfid() {
        return rfid;
    }

    public void setRfid(long rfid) {
        this.rfid = rfid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StaffInfoDb{" +
                "id=" + id +
                ", staffID=" + staffID +
                ", rfid=" + rfid +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                '}';
    }
}
