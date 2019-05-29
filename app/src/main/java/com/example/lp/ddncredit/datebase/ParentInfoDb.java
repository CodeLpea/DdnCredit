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
    private StudentInfoDb studentInfo;//一对一学生

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

    public StudentInfoDb getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfoDb studentInfo) {
        this.studentInfo = studentInfo;
    }
}
