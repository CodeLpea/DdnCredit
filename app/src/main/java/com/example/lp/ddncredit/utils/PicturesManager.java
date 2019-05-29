package com.example.lp.ddncredit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.StringDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.lp.ddncredit.constant.Constants.CACHE_PIC_DIR;
import static com.example.lp.ddncredit.constant.Constants.PERSONAL_PIC_DIR;
import static com.example.lp.ddncredit.constant.Constants.TMP_CACHE_PIC_DIR;

/**
 * Created by Long on 2018/8/10.
 */

public class PicturesManager {
    private static final String TAG = "PicturesManager";
    private static PicturesManager mInstance = null;
    public static final String PARENT = "parent";
    public static final String STAFF = "staff";

    /**
     * 使用StringDef注解来替代枚举，限制下面的函数的传参范围
     */
    @StringDef({PARENT, STAFF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Subdirectory{
    }

    private PicturesManager(){
        //检查个人照片路径是否存在
        if(!FileUtil.isFileExist(PERSONAL_PIC_DIR + File.separator + PARENT)){
            FileUtil.createDirs(PERSONAL_PIC_DIR + File.separator + PARENT);
        }
        if(!FileUtil.isFileExist(PERSONAL_PIC_DIR + File.separator + STAFF)){
            FileUtil.createDirs(PERSONAL_PIC_DIR + File.separator + STAFF);
        }

        //检查考勤照片缓存路径是否存在
        if(!FileUtil.isFileExist(CACHE_PIC_DIR + File.separator + PARENT)){
            FileUtil.createDirs(CACHE_PIC_DIR + File.separator + PARENT);
        }

        if(!FileUtil.isFileExist(CACHE_PIC_DIR + File.separator + STAFF)){
            FileUtil.createDirs(CACHE_PIC_DIR + File.separator + STAFF);
        }

        //考勤临时缓存照片
        if(!FileUtil.isFileExist(TMP_CACHE_PIC_DIR)){
            FileUtil.createDirs(TMP_CACHE_PIC_DIR);
        }
    }

    public static PicturesManager getInstance(){
        if(mInstance == null){
            synchronized (PicturesManager.class) {
                if(mInstance == null) {
                    mInstance = new PicturesManager();
                }
            }
        }
        return mInstance;
    }

    public String getPersonalPicDir(@Subdirectory String sub, final long id){
        return PERSONAL_PIC_DIR + File.separator + sub + File.separator + id + ".jpg";
    }

    public String getCachePicDir(@Subdirectory String sub, final long id){
        return CACHE_PIC_DIR + File.separator + sub + File.separator + id + ".jpg";
    }

    public String getTmpCachePicDir(){
        return TMP_CACHE_PIC_DIR;
    }

    /**
     * 获取指定Id的缓存照片
     * @param id 缓存照片的名字以Id命名
     * @return 成功返回bitmap 失败返回null
     */
    public Bitmap getCacheBitmap(@Subdirectory String sub, final long id){
        String path = getCachePicDir(sub, id);
        return getBitmap(path);
    }

    /**
     * 保存照片到照片缓存目录下
     * @param bitmap
     * @param id
     * @return
     */
    public boolean saveBitmapInCacheDir(Bitmap bitmap, @Subdirectory String sub, final long id){
        String path = getCachePicDir(sub, id);
        return BitmapUtil.save2jpg(bitmap, path);
    }


    /**
     * 从个人照片目录下获取照片，个人照片目录是永久存储，它与特征库的特征值一一对应
     * @param id
     * @return
     */
    public Bitmap getPersonalBitmap(@Subdirectory String sub, final long id){
        String path = getPersonalPicDir(sub, id);
        return getBitmap(path);
    }

    /**
     * 保存照片到个人照片目录下
     * @param bitmap
     * @param id
     * @return
     */
    public boolean saveBitmapInPersonalDir(Bitmap bitmap, @Subdirectory String sub, final long id){
        String path = getPersonalPicDir(sub, id);
        return BitmapUtil.save2jpg(bitmap, path);
    }

    /**
     * 保存临时照片
     * @param bitmap
     * @param name 照片的名字，不带路径
     * @return
     */
    public boolean saveBitmapInTmpCacheDir(Bitmap bitmap, String name){
        String path = TMP_CACHE_PIC_DIR + File.separator + name;
        return BitmapUtil.save2jpg(bitmap, path);
    }

    /**
     * 从指定路径下获取照片的bitmap
     * @param path
     * @return
     */
    private Bitmap getBitmap(String path){
        Bitmap bitmap = null;
        if(FileUtil.isFileExist(path)){
            bitmap = BitmapFactory.decodeFile(path);
        }
        return bitmap;
    }

    /**
     * 删除指定的缓存照片
     * @param id
     */
    public void deletePicInCacheDir(@Subdirectory String sub, final long id){
        String path = getCachePicDir(sub, id);
        FileUtil.deleteFile(path);
    }

    /**
     * 删除指定的正式图片
     * @param id
     */
    public void deletePicInPersonalDir(@Subdirectory String sub, final long id){
        String path = getPersonalPicDir(sub, id);
        FileUtil.deleteFile(path);
    }


    public void movePicInCacheDir2PersonalDir(@Subdirectory String sub, final long id){
        String cachePath = getCachePicDir(sub, id);
        String personalPath = getPersonalPicDir(sub, id);
        FileUtil.moveFile(cachePath, personalPath);
    }

}
