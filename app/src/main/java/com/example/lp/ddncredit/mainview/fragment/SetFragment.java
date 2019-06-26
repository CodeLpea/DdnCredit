package com.example.lp.ddncredit.mainview.fragment;


import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lp.ddncredit.Myapplication;
import com.example.lp.ddncredit.R;
import com.example.lp.ddncredit.mainview.view.bgToast;
import com.example.lp.ddncredit.mainview.view.dialog.AdressDialog;
import com.example.lp.ddncredit.utils.AppUtils;
import com.example.lp.ddncredit.utils.SPUtil;
import com.example.lp.ddncredit.utils.voice.TtsSpeek;
import com.xw.repo.BubbleSeekBar;

import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.APISP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SP_NAME;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.SchoolName;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_LEVEL;
import static com.example.lp.ddncredit.constant.Constants.SP_HDetect_NAME.VOICE_SPEED_LEVEL;

/**
 * 设置界面碎片
 * lp
 * 2019/5/30
 */
public class SetFragment extends BaseFragment {
    private static final String TAG = "SetFragment";
    private FrameLayout frameLayout;
    private TextView tv_id, tv_schoolname, tv_appversion, tv_voice, tv_speed,tv_curent_adress;
    private ImageView iv_address_setin;
    private BubbleSeekBar voiceSeekBar, speedSeekBar;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = this.getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set, container, false);

        initView(view);
        return view;
    }


    private void initView(View view) {
        frameLayout = view.findViewById(R.id.framelayout_set);
        setFrameLayout(frameLayout);
        tv_id = view.findViewById(R.id.text_dev_id);
        tv_schoolname = view.findViewById(R.id.text_schoolName);
        tv_appversion = view.findViewById(R.id.text_app_ver);
        tv_voice = view.findViewById(R.id.tv_voice);
        tv_speed = view.findViewById(R.id.tv_voice_speed);
        tv_curent_adress=view.findViewById(R.id.tv_current_adress);
        iv_address_setin=view.findViewById(R.id.iv_adress_set);
        setOnclickLister setOnclickLister=new setOnclickLister();

        iv_address_setin.setOnClickListener(new AdressDialog(this.getContext(), new AdressDialog.AdressResultListenr() {
            @Override
            public void AdressResult(int i) {//接收返回
                Log.i(TAG, "AdressResult: ");
                tv_curent_adress.setText(SPUtil.readString(SP_NAME, APISP_NAME));//读取当前服务器地址
            }
        }));//进入服务器地址设置


        voiceSeekBar = view.findViewById(R.id.BubbleSeekBar_voice);
        voiceSeekBar.setOnProgressChangedListener(new VoiceProgressChangedListener());

        speedSeekBar = view.findViewById(R.id.BubbleSeekBar_voice_speed);
        speedSeekBar.setOnProgressChangedListener(new SpeedProgressChangedListener());


    }

    private void initData() {
        tv_voice.setText(String.valueOf(SPUtil.readInt(SP_NAME, VOICE_LEVEL)));
        tv_speed.setText(String.valueOf(SPUtil.readFloat(SP_NAME, VOICE_SPEED_LEVEL)));
        voiceSeekBar.setProgress(SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        speedSeekBar.setProgress(SPUtil.readFloat(SP_NAME, VOICE_SPEED_LEVEL));
        tv_schoolname.setText(SPUtil.readString(SP_NAME, SchoolName));
        tv_appversion.setText(AppUtils.getAppVersionName(Myapplication.getInstance().getApplicationContext()));
        tv_id.setText(AppUtils.getLocalMacAddressFromWifiInfo(Myapplication.getInstance().getApplicationContext()));

        tv_curent_adress.setText(SPUtil.readString(SP_NAME, APISP_NAME));//读取当前服务器地址

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }

    }

    private class VoiceProgressChangedListener implements BubbleSeekBar.OnProgressChangedListener {
        @Override
        public void onProgressChanged(int progress, float progressFloat) {
            tv_voice.setText("" + progress);
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnActionUp: " + progress);
            SPUtil.writeInt(SP_NAME, VOICE_LEVEL, progress);
            bgToast.myToast(activity, "设置成功", 0, 200);
            TtsSpeek.getInstance().SpeechFlush("音量" + progress, progress);
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnFinally: " + progress);

        }
    }

    private class SpeedProgressChangedListener implements BubbleSeekBar.OnProgressChangedListener {
        @Override
        public void onProgressChanged(int progress, float progressFloat) {
            tv_speed.setText("" + progressFloat);
        }

        @Override
        public void getProgressOnActionUp(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnActionUp: " + progressFloat);
            TtsSpeek.getInstance().setSpeed(progressFloat);
            SPUtil.writeFloat(SP_NAME, VOICE_SPEED_LEVEL, progressFloat);
            bgToast.myToast(activity, "设置成功", 0, 200);
            TtsSpeek.getInstance().SpeechFlush("语速" + progressFloat, SPUtil.readInt(SP_NAME, VOICE_LEVEL));
        }

        @Override
        public void getProgressOnFinally(int progress, float progressFloat) {
            Log.i(TAG, "getProgressOnFinally: " + progress);

        }
    }

    private class setOnclickLister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }

        }
    }
}
