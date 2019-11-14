package com.ezviz.configwifidemo;

import android.app.Application;
import android.content.Intent;

import com.ezviz.sdk.configwifi.EZConfigWifiErrorEnum;
import com.ezviz.sdk.configwifi.EZConfigWifiInfoEnum;
import com.ezviz.sdk.configwifi.EZWiFiConfigManager;
import com.ezviz.sdk.configwifi.common.EZConfigWifiCallback;
import com.ezviz.sdk.configwifi.smartconfig.SmartConfigParam;

import ezviz.ezopensdkcommon.common.IntentConstants;
import ezviz.ezopensdkcommon.common.LogUtil;
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivityPresenter;
import ezviz.ezopensdkcommon.configwifi.ConfigWifiTypeConstants;

public class SmartConfigWifiPresenter extends ConfigWifiExecutingActivityPresenter {

    private final static String TAG = SmartConfigWifiPresenter.class.getSimpleName();

    public SmartConfigWifiPresenter(){
        mType = ConfigWifiTypeConstants.CONFIG_WIFI_SDK_SMART_CONFIG;
    }

    @Override
    public void startConfigWifi(Application app, Intent configParam) {
        // 开启日志
        EZWiFiConfigManager.showLog(true);

        // step1.准备配网参数
        SmartConfigParam param = new SmartConfigParam();
        param.routerWifiSsid /*路由器wifi名称*/= configParam.getStringExtra(IntentConstants.ROUTER_WIFI_SSID);
        param.routerWifiPwd /*路由器wifi密码*/= configParam.getStringExtra(IntentConstants.ROUTER_WIFI_PASSWORD);
        param.deviceSerial /*设备序列号*/= configParam.getStringExtra(IntentConstants.DEVICE_SERIAL);

        // step2.开始配网
        EZWiFiConfigManager.startSmartConfig(app, param, new EZConfigWifiCallback() {
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
                    EZWiFiConfigManager.stopSmartConfig();
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
                    EZWiFiConfigManager.stopSmartConfig();
                }else if (code == EZConfigWifiErrorEnum.CAN_NOT_SEND_CONFIGURATION_TO_DEVICE.code){
                    // todo 提示用户使用SmartConfig配网时，手机必须连接到wifi
                }
            }
        });
    }

    @Override
    public void stopConfigWifi() {
        mCallback = null;
        EZWiFiConfigManager.stopSmartConfig();
    }

}
