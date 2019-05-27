package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/25.
 */

public class ParentsInfoEntry {
    /**
     *   {
     "parent_id": "8752",
     "parent_phone": "18682043985",
     "parent_rfid": "1058372992",
     "parent_title": "爸爸",
     "parent_type": "1"
     },
     */
    @SerializedName("parent_id")
    private long mId;
    @SerializedName("parent_phone")
    private String mPhone;
    @SerializedName("parent_rfid")
    private String mRfid;
    @SerializedName("parent_title")
    private String mRelationship;
    @SerializedName("parent_type")
    private int mType;

    public long getId() {
        return mId;
    }

    public void setId(long Id) {
        this.mId = Id;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String Phone) {
        this.mPhone = Phone;
    }

    public String getRfid() {
        return mRfid;
    }

    public void setRfid(String rfid) {
        this.mRfid = rfid;
    }

    public String getRelationship() {
        return mRelationship;
    }

    public void setRelationship(String relationship) {
        this.mRelationship = relationship;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    @Override
    public String toString() {
        return "ParentsInfoEntry{" +
                "mId='" + mId + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mRfid='" + mRfid + '\'' +
                ", mRelationship='" + mRelationship + '\'' +
                ", mType='" + mType + '\'' +
                '}';
    }
}
