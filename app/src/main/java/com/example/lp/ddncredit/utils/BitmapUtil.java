package com.example.lp.ddncredit.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;

import static java.lang.System.gc;

/**
 * Created by xiaoyuren on 2018/1/24.
 * 项目名称：didano-robot
 * 类描述：Bitmap工具类
 * company：www.didano.cn
 * email：vin.qin@didano.cn
 * 创建时间：2018/1/24 19:48
 */

public class BitmapUtil {
    private static final String TAG = "BitmapUtil";
 /*   *//**
     * @param bitmap 转换为YUV格式
     * @return 返回转换后的YUV数据
     *//*
    public static byte[] convert2YUV(Bitmap bitmap) {
        byte[] data = new byte[bitmap.getWidth() * bitmap.getHeight() * 3 / 2];
        ImageConverter convert = new ImageConverter();
        convert.initial(bitmap.getWidth(), bitmap.getHeight(), ImageConverter.CP_PAF_NV21);
        boolean ret = convert.convert(bitmap, data);
        convert.destroy();
        if (!ret) {
            Log.e(TAG,"bitmap convert to yuv failed!");
        }
        return data;
    }*/

    /**
     * 保存图片
     *
     * @param bitmap
     * @param fileName 全路径
     * @return
     */
    public static boolean save2jpg(Bitmap bitmap, String fileName) {
        File file = new File(fileName);
        try {
            FileOutputStream fs = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fs);
            bitmap.recycle();
            fs.flush();
            fs.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取图片亮度值
     *
     * @param bitmap
     * @return
     */
    public static int getLumValue(Bitmap bitmap) {
        if(bitmap == null)
            return -1;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r, g, b;
        int count = 0;
        int bright = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                count++;
                int localTemp = bitmap.getPixel(i, j);
                r = (localTemp | 0xff00ffff) >> 16 & 0x00ff;
                g = (localTemp | 0xffff00ff) >> 8 & 0x0000ff;
                b = (localTemp | 0xffffff00) & 0x0000ff;
                bright = (int) (bright + 0.299 * r + 0.587 * g + 0.114 * b);
            }
        }
        return bright / count;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        //recycle会回收掉origin的内存
//        if (!origin.isRecycled()) {
//            origin.recycle();
//        }
        return newBM;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        int cropHeight = (int) (cropWidth / 1.2);
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false);
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM;
        try{
            newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        }catch(OutOfMemoryError localOutOfMemoryError){
            localOutOfMemoryError.printStackTrace();
            gc();
            newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        }

        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();

        return newBM;
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    public static Bitmap skewBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postSkew(-0.6f, -0.3f);
        Bitmap newBM;
        try{
            newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        }catch(OutOfMemoryError localOutOfMemoryError){
            localOutOfMemoryError.printStackTrace();
            gc();
            newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        }

        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();


        return newBM;
    }

    /**
     * 图片做左右翻转
     * @param bitmap
     * @return
     */
    public static Bitmap mirrorBitmap(Bitmap bitmap){
        Matrix matrix1 = new Matrix();
        matrix1.setScale(-1, 1);
        try{
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
        }catch(OutOfMemoryError localOutOfMemoryError){
            localOutOfMemoryError.printStackTrace();
            gc();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
        }

        return bitmap;
    }

    /**
     * bitmap拷贝
     * @param paramBitmap
     * @param config
     * @param isMutable
     * @return
     */
    public static Bitmap copy(Bitmap paramBitmap, Bitmap.Config config, boolean isMutable) {
        Bitmap bitmap;
        try {
            bitmap = paramBitmap.copy(config, isMutable);
        } catch (OutOfMemoryError localOutOfMemoryError) {
            gc();
            bitmap = paramBitmap.copy(config, isMutable);
        }
        return bitmap;
    }


}
