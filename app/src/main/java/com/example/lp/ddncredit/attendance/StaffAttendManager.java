package com.example.lp.ddncredit.attendance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.lp.ddncredit.MainActivity;
import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.datebase.StaffAttendInfoDb;
import com.example.lp.ddncredit.datebase.StaffInfoDb;
import com.example.lp.ddncredit.mainview.view.adapter.AttendShowBean;
import com.example.lp.ddncredit.utils.BitmapUtil;
import com.example.lp.ddncredit.utils.PicturesManager;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.TimeUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;

public class StaffAttendManager {
    private static final String TAG = "StaffAttendManager";
    private long mRfid;

    public StaffAttendManager() {
    }

    /**
     * 查询信息并处理
     *
     * @return true表示查询并处理完成，false表示数据库中没有对应的信息
     */
    public boolean execute(long rfid) {
        mRfid = rfid;
        boolean ret = false;
        Log.i(TAG, " mRfid = " + mRfid);
        StaffInfoDb staffInfoDb = null;
        if (mRfid != 0) {
            //根据卡号查询
            List<StaffInfoDb> staffInfoDbList = LitePal.where("rfid= ?", String.valueOf(mRfid)).find(StaffInfoDb.class);
            if (staffInfoDbList.size() > 0) {//如果能找到匹配的rfid
                staffInfoDb = staffInfoDbList.get(0);
                //根据学生id查找家长对应的学生
            }

            if (staffInfoDb != null) {
                Log.i(TAG, staffInfoDb.toString());
                ret = true;

                sendStaffAttend(staffInfoDb);//发送考勤信息准备弹窗

                Bitmap currentBitmap = BitmapFactory.decodeResource(Myapplication.getInstance().getResources(), R.drawable.notify_app_logo);
                ;
                //有考勤者的信息，先拍照
                MainActivity.MainObject cameraObject = MainActivity.getInstance();
                if (cameraObject != null) {
                    currentBitmap = cameraObject.takePicture();
                }
                Log.i(TAG, "currentBitmap " + currentBitmap);


                //记录上传结果
                //显示结果
                String createTime = TimeUtil.getYMDHMSDate();
                StaffAttendInfoDb staffAttendInfoDb = new StaffAttendInfoDb();
                staffAttendInfoDb.setStaffID(staffInfoDb.getStaffID());
                staffAttendInfoDb.setCreateTime(createTime);
                //再次保存一张临时照片，用于结果上传
                String path = PicturesManager.getInstance().getTmpCachePicDir() + File.separator + staffInfoDb.getStaffID() + "_" + createTime + ".jpg";
                BitmapUtil.save2jpg(currentBitmap, path);
                currentBitmap.recycle();
                currentBitmap = null;

                staffAttendInfoDb.setPicPath(path);
                //结果保存，用于上传服务读取记录
                staffAttendInfoDb.save();

                //播放语音
                TtsSpeek.getInstance().SpeechFlush(staffInfoDb.getName() + "老师你好", SPUtil.readInt(SP_NAME, VOICE_LEVEL));
            }


        }

        return ret;
    }


    private void sendStaffAttend(StaffInfoDb staffInfoDb ){
        AttendShowBean showBean = new AttendShowBean();
        //并将信息装载到显示类中
        showBean.setAttendtime(TimeUtil.getYMDHMSDate001());
        showBean.setBabyname(staffInfoDb.getName());
        showBean.setIcnumber(String.valueOf(mRfid));
        showBean.setRelation("老师");
        showBean.getUrls().add(staffInfoDb.getVeriFace());
        showBean.getReletions().add(staffInfoDb.getName());
        EventBus.getDefault().post(showBean);//发出event
    }

}
