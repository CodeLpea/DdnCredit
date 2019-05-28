package com.example.lp.ddncredit.attendance;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;


import java.io.File;



/**
 * 家长考勤管理
 * lp
 * 2019/05/28
 */

public class parentAttendManagement {
    private static final String TAG = "parentAttendManagement";
    private Context mContext;
   /* private ParentInfoDaoUtils mParentInfoDaoUtils;
    private long mRfid;
    public parentAttendManagement(Context context){
        mContext = context;
        mParentInfoDaoUtils = new ParentInfoDaoUtils(mContext);
    }

    *//**
     * 根据rfid卡号查询
     * @param rfid
     * @return
     *//*
    public parentAttendManagement setRFID(long rfid){
        mRfid = rfid;
        return this;
    }


    *//**
     * 查询信息并处理
     * @return true表示查询并处理完成，false表示数据库中没有对应的信息
     *//*
    public boolean execute(){
        boolean ret = false;
        Log.i(TAG,  " mRfid = " + mRfid);
        ParentInfoDBEntry parentInfoDBEntry = null;
        if(mRfid != 0){
            //根据卡号查询
            parentInfoDBEntry = mParentInfoDaoUtils.queryByRfid(mRfid);
        }
        if(parentInfoDBEntry != null) {
            Log.i(TAG, parentInfoDBEntry.toString());
            ret = true;
            String recogWord = null;
            //有考勤者的信息，先拍照
            CameraPreviewFragment.CameraObject cameraObject = CameraPreviewFragment.newCameraObject();
            Bitmap currentBitmap = null;
            if(cameraObject != null) {
                currentBitmap = cameraObject.takePicture();
            }
            Log.i(TAG, "currentBitmap " + currentBitmap);
            //查询家长的打卡记录，并根据服务器返回做出对应的处理
            ProcessQueryResult queryResult = queryRecord(parentInfoDBEntry);
            //查询结果来决定是否上传结果到服务器
            if (queryResult.isUpload()) {
                //记录上传结果
                StuAttendancedInfoDBEntry stuAttendancedInfoDBEntry = new StuAttendancedInfoDBEntry();
                stuAttendancedInfoDBEntry.setStudentID(parentInfoDBEntry.getChild().getStuID());
                stuAttendancedInfoDBEntry.setParentID(parentInfoDBEntry.getParentID());
                stuAttendancedInfoDBEntry.setCreateTime(createTime);
//                //缓存照片，用作下一次结果比对使用
//                PicturesManager.getInstance().saveBitmapInCacheDir(currentBitmap, parentInfoDBEntry.getParentID());
                //再次保存一张临时照片，用于结果上传
                String path = PicturesManager.getInstance().getTmpCachePicDir() + File.separator + parentInfoDBEntry.getParentID() + "_" + createTime + ".jpg";
                BitmapUtil.save2jpg(currentBitmap, path);
                stuAttendancedInfoDBEntry.setPicPath(path);
                //结果上传
                new StuAttendancedInfoDaoUtils(mContext).Write(stuAttendancedInfoDBEntry);
            }
            //播放语音
            mTts.say(queryResult.getWords(), true);

        }


        return ret;
    }

    *//**
     * 访问服务器查询当天的考勤记录，根据服务器的返回做响应的处理
     * @param
     * @return true表示可以上传本次考勤记录， false表示不再上传本次考勤记录
     *//*
    private ProcessQueryResult queryRecord(ParentInfoDBEntry parentInfoDBEntry){
        ProcessQueryResult result = new ProcessQueryResult();
        //查询家长的打卡记录
        StuAttendancedStatusRequest stuAttendancedStatusRequest = new StuAttendancedStatusRequest();
        stuAttendancedStatusRequest.setStudentId(parentInfoDBEntry.getChild().getStuID());
        stuAttendancedStatusRequest.setParentId(parentInfoDBEntry.getParentID());
        stuAttendancedStatusRequest.setCreateTime(TimeUtil.getYMDHMSDate());
        StuAttendancedStatusResponse stuAttendancedStatusResponse = CloudClient.getInstance().queryStudentAttendancedStatus(stuAttendancedStatusRequest);
        if (stuAttendancedStatusResponse != null) {
            Log.i(TAG, "stuAttendancedStatusResponse : " + stuAttendancedStatusResponse.toString());
            //检查状态
            switch (stuAttendancedStatusResponse.getStatus()) {
                case StuAttendancedStatusResponse.ONLY_BRUSH_ONCE_OCCASION_UPLOAD:
                    Log.i(TAG, "====ONLY_BRUSH_ONCE_OCCASION_UPLOAD===");
                    result.setUpload(true);
                    result.setWords(parentInfoDBEntry.getChild().getName() + "小朋友的" + parentInfoDBEntry.getRelationship() + "您好");
                    break;
                case StuAttendancedStatusResponse.ONLY_BRUSH_ONCE_OCCASION_NOT_UPLOAD:
                    Log.i(TAG, "====ONLY_BRUSH_ONCE_OCCASION_NOT_UPLOAD===");
                    result.setUpload(false);
                    result.setWords(parentInfoDBEntry.getChild().getName() + "小朋友已经被" + stuAttendancedStatusResponse.getRelationShip() + "接走");
                    break;
                case StuAttendancedStatusResponse.MULTI_BRUSH_OCCASION_UPLOAD:
                    Log.i(TAG, "====MULTI_BRUSH_OCCASION_UPLOAD===");
                    result.setUpload(true);
                    if (stuAttendancedStatusResponse.getRelationShip() != null && stuAttendancedStatusResponse.getRelationShip().isEmpty()) {
                        result.setWords(parentInfoDBEntry.getChild().getName() + "小朋友已经被" + stuAttendancedStatusResponse.getRelationShip() + "接走");
                    } else {
                        result.setWords(parentInfoDBEntry.getChild().getName() + "小朋友的" + parentInfoDBEntry.getRelationship() + "您好");
                    }
                    break;
                case StuAttendancedStatusResponse.MULTI_BRUSH_OCCASION_PROMPT_FREQUENT:
                    Log.i(TAG, "====MULTI_BRUSH_OCCASION_PROMPT_FREQUENT===");
                    result.setUpload(false);
                    result.setWords("请勿频繁刷卡");
                    break;
                case 99:
                    Log.i(TAG, "谛达诺幼儿园专用测试...");
                    result.setUpload(true);
                    result.setWords(parentInfoDBEntry.getChild().getName() + "小朋友的" + parentInfoDBEntry.getRelationship() + "您好");
                    break;
                default:
                    break;
            }

        } else {
            //为null要么网络异常
            result.setUpload(true);
            result.setWords(parentInfoDBEntry.getChild().getName() + "小朋友的" + parentInfoDBEntry.getRelationship() + "您好");
        }

        return result;
    }*/


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
