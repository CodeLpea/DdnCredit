package com.example.lp.ddncredit.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lp.ddncredit.datebase.ParentInfoDb;
import com.example.lp.ddncredit.datebase.StaffInfoDb;
import com.example.lp.ddncredit.datebase.StudentInfoDb;
import com.example.lp.ddncredit.http.CloudClient;
import com.example.lp.ddncredit.http.model.SchoolStaffsInfoEntry;
import com.example.lp.ddncredit.http.model.SchoolStudentsInfoEntry;
import com.example.lp.ddncredit.http.model.StaffInfoEntry;
import com.example.lp.ddncredit.http.model.StudentInfoEntry;
import com.example.lp.ddncredit.utils.NetUtil;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.TimeUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SchoolID;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SchoolName;

/**
 * 获取学校所有学生、家长和员工的信息
 * lp
 * 2019/05/29
 */

public class UpdateSchoolInfoService extends IntentService {
    private static final String TAG = "UpdateSchoolInfoService";
    private static boolean isRunning = false;
    public UpdateSchoolInfoService(){
        super(UpdateSchoolInfoService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "开始下载学校信息: ");
        Log.i(TAG, "onHandleIntent thread id is " + Thread.currentThread().getId());
        isRunning = true;
        //检查网络
        while(!NetUtil.isNetworkConnected(getApplicationContext()) && isRunning){
            TimeUtil.delayMs(2000);
        }
        //网络连通，获取学校信息，耗时操作，可以在
        while(true){
            if(getSchoolInfoList()&&isRunning){
                Log.i(TAG, "获取信息成功，间隔二十分钟更新: ");
                TimeUtil.delayMs(20 * 60000);//获取成功就二十分钟更新一次
            }else {
                Log.i(TAG, "获取信息不成功，每间隔十秒重新尝试: ");
                TimeUtil.delayMs(10 * 1000);//不成功，每间隔十秒重新尝试
            }

        }

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand thread id is " + Thread.currentThread().getId());
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.i(TAG, "onDestroy...");
    }


    /**
     * 从服务器上获取学校的信息
     */
    private boolean getSchoolInfoList(){
        Log.i(TAG, "从服务器上获取学校的信息: ");
        boolean studentRet = false;
        boolean staffRet = false;
        //获取学生信息包含家长信息
        SchoolStudentsInfoEntry studentsInfoEntry = CloudClient.getInstance().getSchoolStudentList();
        if(studentsInfoEntry != null) {
            studentRet = true;
            List<ParentInfoDb> parentInfoDbArrayList = new ArrayList<>();
            List<StudentInfoDb> studentInfoDbArrayList = new ArrayList<>();
            List<StudentInfoEntry> students = studentsInfoEntry.getStudents();
            if(students != null) {
                for (int i = 0; i < students.size(); i++) {
                   // Log.i(TAG, i + "-->" + students.get(i).toString());
                    StudentInfoDb studentInfoDBEntry = new StudentInfoDb();
                    studentInfoDBEntry.setStuId(students.get(i).getId());
                    studentInfoDBEntry.setName(students.get(i).getName());
                    studentInfoDBEntry.setClazztitle(students.get(i).getClassName());
                    studentInfoDbArrayList.add(studentInfoDBEntry);
                    for(int j = 0; j < students.get(i).getParents().size(); j++){
                        ParentInfoDb   parentInfoDBEntry = new ParentInfoDb();
                        String rfid = students.get(i).getParents().get(j).getRfid();
                        if(rfid != null && !rfid.trim().isEmpty()){
                            parentInfoDBEntry.setRfid(Long.parseLong(rfid.trim()));
                        }
                        parentInfoDBEntry.setRelationship(students.get(i).getParents().get(j).getRelationship());
                        parentInfoDBEntry.setStudentid(students.get(i).getId());
                        parentInfoDBEntry.setParentID(students.get(i).getParents().get(j).getId());
                        parentInfoDBEntry.setFaceurl(students.get(i).getParents().get(j).getFace());
                        parentInfoDbArrayList.add(parentInfoDBEntry);
                    }
                }
                    LitePal.deleteAll(ParentInfoDb.class);
                    LitePal.deleteAll(StudentInfoDb.class);
                    LitePal.saveAll(parentInfoDbArrayList);
                    LitePal.saveAll(studentInfoDbArrayList);
                    Log.i(TAG, "有学生数据: "+ LitePal.count(StudentInfoDb.class));
                    Log.i(TAG, "有家长数据: "+ LitePal.count(ParentInfoDb.class));
                SPUtil.writeString(SP_NAME,SchoolName,studentsInfoEntry.getSchoolName());
                SPUtil.writeString(SP_NAME,SchoolID,String.valueOf(studentsInfoEntry.getSchoolId()));
            }
        }
        //获取员工信息
        SchoolStaffsInfoEntry staffsInfoEntry = CloudClient.getInstance().getSchoolStaffList();
        List<StaffInfoDb> staffInfoDBEntries = new ArrayList<>();
        if(staffsInfoEntry != null){
            staffRet = true;
            List<StaffInfoEntry> staffs = staffsInfoEntry.getStaffs();
            if(staffs != null){
                for(int i = 0; i < staffs.size(); i++){
                   // Log.i(TAG, i + "-->" + staffs.get(i).toString());
                    StaffInfoDb staffInfoDBEntry = new StaffInfoDb();
                    staffInfoDBEntry.setStaffID(staffs.get(i).getId());
                    staffInfoDBEntry.setName(staffs.get(i).getName());
                    staffInfoDBEntry.setPhone(staffs.get(i).getPhone());
                    staffInfoDBEntry.setVeriFace(staffs.get(i).getMveriFace());
                    String rfid = staffs.get(i).getRfid();
                    if(rfid != null && !rfid.trim().isEmpty()) {
                        staffInfoDBEntry.setRfid(Long.parseLong(rfid.trim()));
                    }
                    staffInfoDBEntry.setType(staffs.get(i).getType());
                    staffInfoDBEntries.add(staffInfoDBEntry);

                }
                    LitePal.deleteAll(StaffInfoDb.class);//先清空表，然后保存
                    LitePal.saveAll(staffInfoDBEntries);//保存
                    Log.i(TAG, "有教师数据: "+ LitePal.count(StaffInfoDb.class));



            }
        }

        return (studentRet & staffRet);
    }

    public static boolean isServiceRunning(){
        return isRunning;
    }
}
