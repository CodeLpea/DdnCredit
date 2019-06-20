package com.example.lp.ddncredit;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

import static com.example.lp.ddncredit.utils.NavigationBarUtil.hideNavigationBar;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        //hideBottomUIMenu();
        hideNavigationBar(this.getWindow());
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();

    }

/**
 * 判断当前显示的是哪一个Fragment
 * */
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}
