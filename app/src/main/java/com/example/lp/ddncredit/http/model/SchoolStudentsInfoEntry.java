package com.example.lp.ddncredit.http.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Long on 2018/7/25.
 */

public class SchoolStudentsInfoEntry {
    /**
     *  "school_id": "5",
     "student": [
     {
     "birthday": "2016-12-17",
     "class_id": "96",
     "class_title": "小诺A班",
     "gender": "2",
     "id": "2178",
     "is_registered": "1",
     "name": "小草",
     "nickname": "收到",
     "parents_list": [
     {
     "parent_id": "119582",
     "parent_phone": "18381669790",
     "parent_rfid": "",
     "parent_title": "外公",
     "parent_type": "5"
     },
     {
     "parent_id": "119578",
     "parent_phone": "18381669767",
     "parent_rfid": "",
     "parent_title": "妈妈",
     "parent_type": "2"
     },
     {
     "parent_id": "70389",
     "parent_phone": "18381669700",
     "parent_rfid": "",
     "parent_title": "爷爷",
     "parent_type": "3"
     },
     {
     "parent_id": "78590",
     "parent_phone": "18200180935",
     "parent_rfid": "350830597",
     "parent_title": "奶奶",
     "parent_type": "4"
     }
     ]
     },
     {
     "birthday": "2016-12-19",
     "class_id": "96",
     "class_title": "小诺A班",
     "gender": "1",
     "id": "2179",
     "is_registered": "1",
     "name": "覃福乾",
     "nickname": "",
     "parents_list": [
     {
     "parent_id": "8752",
     "parent_phone": "18682043985",
     "parent_rfid": "1058372992",
     "parent_title": "爸爸",
     "parent_type": "1"
     },
     {
     "parent_id": "66225",
     "parent_phone": "18725897890",
     "parent_rfid": "",
     "parent_title": "爸爸",
     "parent_type": "1"
     },
     {
     "parent_id": "64902",
     "parent_phone": "13334567894",
     "parent_rfid": "",
     "parent_title": "爷爷",
     "parent_type": "3"
     },
     {
     "parent_id": "64899",
     "parent_phone": "13332456756",
     "parent_rfid": "",
     "parent_title": "爸爸",
     "parent_type": "1"
     }
     ]
     }
     ],
     "school_title": "谛达诺幼儿园"
     },
     */

    @SerializedName("school_id")
    private int mSchoolId;
    @SerializedName("school_title")
    private String mSchoolName;
    @SerializedName("student")
    private List<StudentInfoEntry> mStudents;


    public int getSchoolId() {
        return mSchoolId;
    }

    public void setSchoolId(int schoolId) {
        this.mSchoolId = schoolId;
    }

    public String getSchoolName() {
        return mSchoolName;
    }

    public void setSchoolName(String schoolName) {
        this.mSchoolName = schoolName;
    }

    public List<StudentInfoEntry> getStudents() {
        return mStudents;
    }

    public void setStudents(List<StudentInfoEntry> students) {
        this.mStudents = students;
    }

    @Override
    public String toString() {
        return "SchoolStudentsInfoEntry{" +
                "mSchoolId='" + mSchoolId + '\'' +
                ", mSchoolName='" + mSchoolName + '\'' +
                ", mStudents=" + mStudents +
                '}';
    }
}
