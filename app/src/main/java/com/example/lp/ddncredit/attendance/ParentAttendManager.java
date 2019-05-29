package com.example.lp.ddncredit.attendance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.datebase.ParentAttendInfoDb;
import com.example.lp.ddncredit.datebase.ParentInfoDb;
import com.example.lp.ddncredit.datebase.StudentInfoDb;
import com.example.lp.ddncredit.http.CloudClient;
import com.example.lp.ddncredit.http.model.ParentAttendancedStatusRequest;
import com.example.lp.ddncredit.http.model.ParentAttendancedStatusResponse;
import com.example.lp.ddncredit.utils.BitmapUtil;
import com.example.lp.ddncredit.utils.PicturesManager;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.TimeUtil;
import com.example.lp.ddncredit.voice.TtsSpeek;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;


/**
 * 家长考勤管理
 * lp
 * 2019/05/28
 */

public class ParentAttendManager {
    private static final String TAG = "ParentAttendManager";
    private long mRfid;
    public ParentAttendManager(){
    }
    /**
     查询信息并处理
     * @return true表示查询并处理完成，false表示数据库中没有对应的信息
      * */
    public boolean execute(long rfid){
        mRfid = rfid;
        boolean ret = false;
        Log.i(TAG,  " mRfid = " + mRfid);
        ParentInfoDb parentInfoDb = null;
        StudentInfoDb studentInfoDb = null;
        if(mRfid != 0){
            //根据卡号查询
            List<ParentInfoDb> getParentInfoDbList=LitePal.where("rfid= ?", String.valueOf(mRfid)).find(ParentInfoDb.class);
            if(getParentInfoDbList.size()>0){//如果能找到匹配的rfid
                parentInfoDb = getParentInfoDbList.get(0);
                //根据学生id查找家长对应的学生
                List<StudentInfoDb> getStudentInfoDbDbList=LitePal.where("stuId= ?", String.valueOf(parentInfoDb.getStudentid())).find(StudentInfoDb.class);
                if(getStudentInfoDbDbList.size()>0){//如果能找到匹配的学生id
                    studentInfoDb = getStudentInfoDbDbList.get(0);
                }
            }

        if(parentInfoDb != null&&studentInfoDb!=null) {
            Log.i(TAG, parentInfoDb.toString());
            ret = true;

            Bitmap currentBitmap = BitmapFactory.decodeResource(Myapplication.getInstance().getResources(), R.drawable.notify_app_logo);;
            /*//有考勤者的信息，先拍照
            CameraPreviewFragment.CameraObject cameraObject = CameraPreviewFragment.newCameraObject();
            if(cameraObject != null) {
                currentBitmap = cameraObject.takePicture();
            }
            Log.i(TAG, "currentBitmap " + currentBitmap);*/
            //查询家长的打卡记录，并根据服务器返回做出对应的处理
            ProcessQueryResult queryResult = queryRecord(parentInfoDb,studentInfoDb,false);
            //查询结果来决定是否上传结果到服务器
            if (queryResult.isUpload()) {
                //记录上传结果
                //显示结果
                String createTime = TimeUtil.getYMDHMSDate();
                ParentAttendInfoDb parentAttendInfoDb = new ParentAttendInfoDb();
                parentAttendInfoDb.setStudentID(studentInfoDb.getStuId());
                parentAttendInfoDb.setParentID(parentInfoDb.getParentID());
                parentAttendInfoDb.setCreateTime(createTime);

                //再次保存一张临时照片，用于结果上传
                String path = PicturesManager.getInstance().getTmpCachePicDir() + File.separator + parentInfoDb.getParentID() + "_" + createTime + ".jpg";
                BitmapUtil.save2jpg(currentBitmap, path);
                parentAttendInfoDb.setPicPath(path);
                //结果保存
                parentAttendInfoDb.save();
            }
            //播放语音
            TtsSpeek.getInstance().SpeechFlush(queryResult.getWords(), SPUtil.readInt(SP_NAME,VOICE_LEVEL));

        }

        }

        return ret;
    }

    /**
     * 访问服务器查询当天的考勤记录，根据服务器的返回做响应的处理
     * @param parentInfoDb 家长信息
     * @param studentInfoDb 学生信息
     * @param b 是否在播报之前进行服务器询问之前刷卡状态
     * @return true表示可以上传本次考勤记录， false表示不再上传本次考勤记录
     * */
    private ProcessQueryResult queryRecord(ParentInfoDb parentInfoDb,StudentInfoDb studentInfoDb,boolean b){
        ProcessQueryResult result = new ProcessQueryResult();
        if(b){//同意从后台先获取状态再返回
            //查询家长的打卡记录
            ParentAttendancedStatusRequest parentAttendancedStatusRequest = new ParentAttendancedStatusRequest();
            parentAttendancedStatusRequest.setStudentId(studentInfoDb.getStuId());
            parentAttendancedStatusRequest.setParentId(parentInfoDb.getParentID());
            parentAttendancedStatusRequest.setCreateTime(TimeUtil.getYMDHMSDate());
            ParentAttendancedStatusResponse stuAttendancedStatusResponse = CloudClient.getInstance().queryStudentAttendancedStatus(parentAttendancedStatusRequest);
            if (stuAttendancedStatusResponse != null) {
                Log.i(TAG, "stuAttendancedStatusResponse : " + stuAttendancedStatusResponse.toString());
                //检查状态
                switch (stuAttendancedStatusResponse.getStatus()) {
                    case ParentAttendancedStatusResponse.ONLY_BRUSH_ONCE_OCCASION_UPLOAD:
                        Log.i(TAG, "====ONLY_BRUSH_ONCE_OCCASION_UPLOAD===");
                        result.setUpload(true);
                        result.setWords(studentInfoDb.getName() + "小朋友的" + parentInfoDb.getRelationship() + "您好");
                        break;
                    case ParentAttendancedStatusResponse.ONLY_BRUSH_ONCE_OCCASION_NOT_UPLOAD:
                        Log.i(TAG, "====ONLY_BRUSH_ONCE_OCCASION_NOT_UPLOAD===");
                        result.setUpload(false);
                        result.setWords(studentInfoDb.getName() + "小朋友已经被" + stuAttendancedStatusResponse.getRelationShip() + "接走");
                        break;
                    case ParentAttendancedStatusResponse.MULTI_BRUSH_OCCASION_UPLOAD:
                        Log.i(TAG, "====MULTI_BRUSH_OCCASION_UPLOAD===");
                        result.setUpload(true);
                        if (stuAttendancedStatusResponse.getRelationShip() != null && stuAttendancedStatusResponse.getRelationShip().isEmpty()) {
                            result.setWords(studentInfoDb.getName() + "小朋友已经被" + stuAttendancedStatusResponse.getRelationShip() + "接走");
                        } else {
                            result.setWords(studentInfoDb.getName() + "小朋友的" + parentInfoDb.getRelationship() + "您好");
                        }
                        break;
                    case ParentAttendancedStatusResponse.MULTI_BRUSH_OCCASION_PROMPT_FREQUENT:
                        Log.i(TAG, "====MULTI_BRUSH_OCCASION_PROMPT_FREQUENT===");
                        result.setUpload(false);
                        result.setWords("请勿频繁刷卡");
                        break;
                    case 99:
                        Log.i(TAG, "谛达诺幼儿园专用测试...");
                        result.setUpload(true);
                        result.setWords(studentInfoDb.getName() + "小朋友的" + parentInfoDb.getRelationship() + "您好");
                        break;
                    default:
                        break;
                }

            } else {
                //为null要么网络异常
                result.setUpload(true);
                result.setWords(studentInfoDb.getName() + "小朋友的" + parentInfoDb.getRelationship() + "您好");
            }
        }else {//不询问后台情况，直接上报考勤数据
            result.setUpload(true);
            result.setWords(studentInfoDb.getName() + "小朋友的" + parentInfoDb.getRelationship() + "您好");
        }

        return result;
    }


    class ProcessQueryResult{
        private boolean isUpload;
        private String words;

        public boolean isUpload() {
            return isUpload;
        }

        public void setUpload(boolean upload) {
            isUpload = upload;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        @Override
        public String toString() {
            return "RecordQueryResult{" +
                    "isUpload=" + isUpload +
                    ", words='" + words + '\'' +
                    '}';
        }
    }
}
