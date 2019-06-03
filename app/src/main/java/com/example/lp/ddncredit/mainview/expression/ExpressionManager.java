package com.example.lp.ddncredit.mainview.expression;

import android.util.Log;

import com.example.lp.ddncredit.Myapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

import static com.example.lp.ddncredit.mainview.expression.ExpressionInterface.ExpressionList;

/**
 * 表情管理类
 * */
public class ExpressionManager {
    private static final String TAG="ExpressionManager";
    public static ExpressionManager instance;
    private  static List<GifDrawable> gifDrawableList=new ArrayList<GifDrawable>();
    private GifDrawable gifDrawable;

    public void setGifDrawableList(List<GifDrawable> gifDrawableList) {
        this.gifDrawableList = gifDrawableList;
    }

    public static ExpressionManager getInstance() {
        if (instance==null){
            instance=new ExpressionManager();
            getGifDrawableList();
            Log.i(TAG, "getInstance: ");
        }
        return instance;
    }

    private static List<GifDrawable> getGifDrawableList() {
        if(gifDrawableList.size()<2){
            Log.i(TAG, "第一次，初始化:getGifDrawableList ");
            for(String expressionInterface:ExpressionList){
                try {
                    gifDrawableList.add(new GifDrawable(Myapplication.getInstance().getAssets(),expressionInterface));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return gifDrawableList;
    }

    public GifDrawable getGifDrawable(int random){
        getGifDrawableList();
        gifDrawable=gifDrawableList.get(random);
        return gifDrawable;
    }


}
