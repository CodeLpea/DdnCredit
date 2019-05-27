package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/7/25.
 */

public class SchoolStaffsInfoEntry {
    /**
     *   "data": {
     "school_id": "166",
     "staff_list": [
     {
     "name": "易梨",
     "rfid": "719544755",
     "id": "5603",
     "type": "32",
     "phone": "18228117027"
     },
     {
     "name": "郭卫琼",
     "rfid": "725177539",
     "id": "5631",
     "type": "32",
     "phone": "18328152824"
     }
     ],
     "school_title": "眉山市第一幼儿园"
     },
     */
    @SerializedName("school_id")
    private int mId;
    @SerializedName("school_title")
    private String mName;
    @SerializedName("staff_list")
    private List<StaffInfoEntry> mStaffs;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public List<StaffInfoEntry> getStaffs() {
        return mStaffs;
    }

    public void setStaffs(List<StaffInfoEntry> staffs) {
        this.mStaffs = staffs;
    }

    @Override
    public String toString() {
        return "SchoolStaffsInfoEntry{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mStaffs=" + mStaffs +
                '}';
    }
}
