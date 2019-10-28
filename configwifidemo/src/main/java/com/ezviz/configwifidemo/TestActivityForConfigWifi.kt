package com.ezviz.configwifidemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ezviz.sdk.configwifi.Config
import ezviz.ezopensdkcommon.common.IntentConstants
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivity
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivityPresenter
import ezviz.ezopensdkcommon.configwifi.ConfigWifiTypeConstants

class TestActivityForConfigWifi : AppCompatActivity() {

//    private val deviceSerial = "C54348326"
//    private val deviceVerifyCode = "SMDXHV"

    private val deviceSerial = "C54348757"
    private val deviceVerifyCode = "FTFPKL"

    private val routerWifiName = "Yu"
    private val routerWifiPwd = "12344321"
    private val configWifiPrefix = "EZVIZ_"
    private val deviceHotspotSsid = configWifiPrefix + deviceSerial
    private val deviceHotspotPwd = configWifiPrefix + deviceVerifyCode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Config.LOGGING = true
    }

    fun onCLickStop(view: View) {

    }

    fun onClickStart(view: View) {
        initConfigWifiPresenters()
        val intent = Intent(this, ConfigWifiExecutingActivity::class.java)

//         公共参数
        intent.putExtra(IntentConstants.USING_CONFIG_WIFI_SDK, true)
        intent.putExtra(IntentConstants.ROUTER_WIFI_SSID, routerWifiName)
        intent.putExtra(IntentConstants.ROUTER_WIFI_PASSWORD, routerWifiPwd)
        intent.putExtra(IntentConstants.DEVICE_SERIAL, deviceSerial)
        intent.putExtra(IntentConstants.DEVICE_VERIFY_CODE, deviceVerifyCode)

//         AP配网参数
        intent.putExtra(IntentConstants.SELECTED_PRESENTER_TYPE, ConfigWifiTypeConstants.CONFIG_WIFI_SDK_AP)
        intent.putExtra(IntentConstants.DEVICE_HOTSPOT_SSID, deviceHotspotSsid)
        intent.putExtra(IntentConstants.DEVICE_HOTSPOT_PWD, deviceHotspotPwd)

//         SmartConfig特定配网参数
//        intent.putExtra(IntentConstants.SELECTED_PRESENTER_TYPE, ConfigWifiTypeConstants.CONFIG_WIFI_SDK_SMART_CONFIG)

//        // 声波配网特定参数
//        intent.putExtra(IntentConstants.SELECTED_PRESENTER_TYPE, ConfigWifiTypeConstants.CONFIG_WIFI_SDK_SOUND_WAVE)

        startActivity(intent)
    }

    private fun initConfigWifiPresenters() {
        ConfigWifiExecutingActivityPresenter.addPresenter(ApConfigWifiPresenter())
        ConfigWifiExecutingActivityPresenter.addPresenter(SmartConfigWifiPresenter())
        ConfigWifiExecutingActivityPresenter.addPresenter(SoundWaveConfigWifiPresenter())
    }

}
