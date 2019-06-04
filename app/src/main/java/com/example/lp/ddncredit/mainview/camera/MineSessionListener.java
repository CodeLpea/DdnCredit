package com.example.lp.ddncredit.mainview.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * 用于接收关于照相机捕获会话的状态更新的回调对象
 * lp
 * 2019/06/03
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public abstract class MineSessionListener extends CameraCaptureSession.StateCallback{
    /**
     * 在配置是调用
     * @param session
     */
    @Override
    public final void onConfigured(CameraCaptureSession session) {
        onSessionConfigured(session);
    }
    /**
     * 配置失败时调用
     * @param session
     */
    @Override
    public void onConfigureFailed(CameraCaptureSession session) {
        Log.w("MineSessionListener", "onConfigureFailed: ");
    }

    /**
     * 在配置是调用
     * @param session
     */
    public abstract void onSessionConfigured(CameraCaptureSession session);
}
