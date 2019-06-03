package com.example.lp.ddncredit.mainview.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lp.ddncredit.R;

/**
 * 设置界面碎片
 * lp
 * 2019/5/30
 */
public class SetFragment extends Fragment {
    private static final String TAG="SetFragment";


    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        super.onHiddenChanged(hidden);
        if(hidden){
            Log.i(TAG, "不在最前端界面显示 ");
        }else{//重新显示到最前端 ,相当于调用了onResume()
            //进行网络数据刷新  此处执行必须要在 Fragment与Activity绑定了 即需要添加判断是否完成绑定，否则将会报空（即非第一个显示出来的fragment，虽然onCreateView没有被调用,
            //但是onHiddenChanged也会被调用，所以如果你尝试去获取活动的话，注意防止出现空指针）
            Log.i(TAG, "重新显示到最前端");

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "SetFragment onAttach: ");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "SetFragment onDetach: ");

    }

}
