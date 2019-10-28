package com.ezviz.configwifidemo;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;

import ezviz.ezopensdkcommon.common.LogUtil;
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivityPresenter;

public class MixedConfigCallbackDelegate extends EZConfigWifiCallback {

    private final static String TAG = MixedConfigCallbackDelegate.class.getSimpleName();

    private ConfigWifiExecutingActivityPresenter.Callback mCallback;

    public MixedConfigCallbackDelegate(ConfigWifiExecutingActivityPresenter.Callback callback){
        mCallback = callback;
    }

    @Override
    public void onInfo(int code, String message) {
        super.onInfo(code, message);
        LogUtil.i(TAG, code + ", " + message);
        if (mCallback == null){
            return;
        }
        if (code == EZConfigWifiInfoEnum.CONNECTED_TO_WIFI.code){
            mCallback.onConnectedToWifi();
        }else{
            mCallback.onConfigInfo(code);
        }
    }
    @Override
    public void onError(int code, String description) {
        LogUtil.e(TAG, code + ", " + description);
        if (mCallback == null){
            return;
        }
        if (code == EZConfigWifiErrorEnum.CONFIG_TIMEOUT.code){
            mCallback.onTimeout();
        }else{
            mCallback.onConfigError(code, description);
        }
    }
}
