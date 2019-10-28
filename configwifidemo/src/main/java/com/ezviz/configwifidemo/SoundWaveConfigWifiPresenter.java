package com.ezviz.configwifidemo;

import android.app.Application;
import android.content.Intent;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.EZWiFiConfigManager;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.soundwave.SoundWaveConfigParam;

import ezviz.ezopensdkcommon.common.IntentConstants;
import ezviz.ezopensdkcommon.common.LogUtil;
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivityPresenter;
import ezviz.ezopensdkcommon.configwifi.ConfigWifiTypeConstants;

public class SoundWaveConfigWifiPresenter extends ConfigWifiExecutingActivityPresenter {

    private final static String TAG = SoundWaveConfigWifiPresenter.class.getSimpleName();

    public SoundWaveConfigWifiPresenter(){
        mType = ConfigWifiTypeConstants.CONFIG_WIFI_SDK_SOUND_WAVE;
    }

    @Override
    public void startConfigWifi(Application app, Intent configParam) {
        // step1.准备配网参数
        SoundWaveConfigParam param = new SoundWaveConfigParam();
        param.routerWifiSsid /*路由器wifi名称*/= configParam.getStringExtra(IntentConstants.ROUTER_WIFI_SSID);
        param.routerWifiPwd /*路由器wifi密码*/= configParam.getStringExtra(IntentConstants.ROUTER_WIFI_PASSWORD);
        param.deviceSerial /*设备序列号*/= configParam.getStringExtra(IntentConstants.DEVICE_SERIAL);
        // step2.开始配网
        EZWiFiConfigManager.startSoundWaveConfig(app, param, new EZConfigWifiCallback() {
            @Override
            public void onInfo(int code, String message) {
                super.onInfo(code, message);
                LogUtil.i(TAG, "onInfo: " + code);
                if (mCallback == null){
                    return;
                }

                // step3.结束配网
                if (EZConfigWifiInfoEnum.CONNECTED_TO_WIFI.code == code){
                    mCallback.onConnectedToWifi();
                    // todo 提示用户配网成功

                    // 配网成功后，需要停止配网
                    EZWiFiConfigManager.stopSoundWaveConfig();
                }
            }

            @Override
            public void onError(int code, String description) {
                super.onError(code, description);
                LogUtil.i(TAG, "onError: " + code);
                if (mCallback == null){
                    return;
                }
                mCallback.onConfigError(code, description);

                // step3.结束配网
                if (code == EZConfigWifiErrorEnum.CONFIG_TIMEOUT.code){
                    mCallback.onTimeout();
                    // todo 提示用户配网超时

                    // 配网失败后，需要停止配网
                    EZWiFiConfigManager.stopSoundWaveConfig();
                }else if (code == EZConfigWifiErrorEnum.PHONE_MEDIA_VALUE_NOT_MAX.code){
                    // todo 提示用户使用声波配网时将音量调到最大，用以提高配网成功率
                }
            }
        });
    }

    @Override
    public void stopConfigWifi() {
        mCallback = null;
        EZWiFiConfigManager.stopSoundWaveConfig();
    }

}
