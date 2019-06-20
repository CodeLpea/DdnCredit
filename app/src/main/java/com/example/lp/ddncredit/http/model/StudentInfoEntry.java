package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/7/25.
 */

public class StudentInfoEntry {
    /**
     *  "birthday": "2016-12-19",
     "class_id": "96",
     "class_title": "小诺A班",
     "gender": "1",
     "id": "2179",
     "is_registered": "1",
     "name": "小诺",
     "nickname": "",
     "parents_list": [
     {
     "parent_id": "8752",
     "parent_phone": "18682043985",
     "parent_rfid": "1058372992",
     "parent_title": "爸爸",
     "parent_type": "1"
     "veri_face":"dsds1d2sd12s1d32.jpg"
     },
     */
    @SerializedName("name")
    private String mName;
    @SerializedName("nickname")
    private String mNickName;
    @SerializedName("id")
    private long mId;
    @SerializedName("gender")
    private int mGender;
    @SerializedName("birthday")
    private String mBirthday;
    @SerializedName("is_registered")
    private int mIsRegistered;
    @SerializedName("class_id")
    private int mClassId;
    @SerializedName("class_title")
    private String mClassName;
    @SerializedName("parents_list")
    private List<ParentsInfoEntry> mParents;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        this.mNickName = nickName;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public int getGender() {
        return mGender;
    }

    public void setGender(int gender) {
        this.mGender = gender;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public void setBirthday(String birthday) {
        this.mBirthday = birthday;
    }

    public int getIsRegistered() {
        return mIsRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.mIsRegistered = isRegistered;
    }

    public int getClassId() {
        return mClassId;
    }

    public void setClassId(int classId) {
        this.mClassId = classId;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String className) {
        this.mClassName = className;
    }

    public List<ParentsInfoEntry> getParents() {
        return mParents;
    }

    public void setParents(List<ParentsInfoEntry> parents) {
        this.mParents = parents;
    }

    @Override
    public String toString() {
        return "StudentInfoEntry{" +
                "mName='" + mName + '\'' +
                ", mNickName='" + mNickName + '\'' +
                ", mId='" + mId + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mBirthday='" + mBirthday + '\'' +
                ", mIsRegistered='" + mIsRegistered + '\'' +
                ", mClassId='" + mClassId + '\'' +
                ", mClassName='" + mClassName + '\'' +
                ", mParents=" + mParents +
                '}';
    }
}
