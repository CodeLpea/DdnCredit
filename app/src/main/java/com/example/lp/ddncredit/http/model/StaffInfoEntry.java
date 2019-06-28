package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Long on 2018/7/25.
 */

public class StaffInfoEntry {
    /**
     *  {
     "name": "易梨",
     "rfid": "719544755",
     "id": "5603",
     "type": "32",
     "veriFace": "xxxxx.jpg",
     "phone": "18228117027"
     },
     */

    @SerializedName("name")
    private String mName;
    @SerializedName("id")
    private long mId;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("rfid")
    private String mRfid;
    @SerializedName("veriFace")
    private String mveriFace;
    @SerializedName("type")
    private int mType;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getRfid() {
        return mRfid;
    }

    public void setRfid(String rfid) {
        this.mRfid = rfid;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getMveriFace() {
        return mveriFace;
    }

    public void setMveriFace(String mveriFace) {
        this.mveriFace = mveriFace;
    }

    @Override
    public String toString() {
        return "StaffInfoEntry{" +
                "mName='" + mName + '\'' +
                ", mId=" + mId +
                ", mPhone='" + mPhone + '\'' +
                ", mRfid='" + mRfid + '\'' +
                ", mveriFace='" + mveriFace + '\'' +
                ", mType=" + mType +
                '}';
    }
}
