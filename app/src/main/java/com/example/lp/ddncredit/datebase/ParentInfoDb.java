package com.example.lp.ddncredit.datebase;

import org.litepal.crud.LitePalSupport;
/**
 * 家长信息表，一个家长rfid卡号对应一个学生，与学生表形成一对多关系
 *lp
 * 2019/05/29
 * */
public class ParentInfoDb extends LitePalSupport {
    private int id;//id
    private long rfid;//卡号
    private String relationship;//与学生的关系
    private long studentid;//一对多学生
    private long  parentID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRfid() {
        return rfid;
    }

    public void setRfid(long rfid) {
        this.rfid = rfid;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public long getStudentid() {
        return studentid;
    }

    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    @Override
    public String toString() {
        return "ParentInfoDb{" +
                "id=" + id +
                ", rfid=" + rfid +
                ", relationship='" + relationship + '\'' +
                ", studentid=" + studentid +
                ", parentID=" + parentID +
                '}';
    }
}
