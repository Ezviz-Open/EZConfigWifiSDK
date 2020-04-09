@file:Suppress("UNUSED_PARAMETER")

package com.ezviz.configwifidemo

import android.content.Context
import android.content.Intent
import android.net.*
import android.net.wifi.WifiNetworkSpecifier
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.PatternMatcher
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ezviz.sdk.configwifi.Config
import ezviz.ezopensdkcommon.common.IntentConstants
import ezviz.ezopensdkcommon.common.LogUtil
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivity
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivityPresenter
import ezviz.ezopensdkcommon.configwifi.ConfigWifiTypeConstants

class TestActivityForConfigWifi : AppCompatActivity() {

//    private val deviceSerial = "C54348326"
//    private val deviceVerifyCode = "SMDXHV"

    private val deviceSerial = "C54348757"
    private val deviceVerifyCode = "FTFPKL"

    private val routerWifiName = "test"
    private val routerWifiPwd = "12345687"
    private val configWifiPrefix = "EZVIZ_"
    private val deviceHotspotSsid = configWifiPrefix + deviceSerial
    private val deviceHotspotPwd = configWifiPrefix + deviceVerifyCode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Config.LOGGING = true
    }

    fun onCLickStop(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }else{
            LogUtil.e(getTag(), "请在Android-Q及以上版本运行")
        }
    }

    fun onClickStart(view: View) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//            val specifier = WifiNetworkSpecifier.Builder()
//                    .setSsidPattern(PatternMatcher("test", PatternMatcher.PATTERN_PREFIX))
////                    .setBssidPattern(MacAddress.fromString("10:03:23:00:00:00"), MacAddress.fromString("ff:ff:ff:00:00:00"))
////                    .setWpa2Passphrase("12345687")
//                    .build()
//
//            val request = NetworkRequest.Builder()
//                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                    .setNetworkSpecifier(specifier)
//                    .build()
//
//            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            connectivityManager.requestNetwork(request, networkCallback)
//        }else{
//            LogUtil.e(getTag(), "请在Android-Q及以上版本运行")
//            initConfigWifiPresenters()
//            val intent = Intent(this, ConfigWifiExecutingActivity::class.java)
//
////         公共参数
//            intent.putExtra(IntentConstants.USING_CONFIG_WIFI_SDK, true)
//            intent.putExtra(IntentConstants.ROUTER_WIFI_SSID, routerWifiName)
//            intent.putExtra(IntentConstants.ROUTER_WIFI_PASSWORD, routerWifiPwd)
//            intent.putExtra(IntentConstants.DEVICE_SERIAL, deviceSerial)
//            intent.putExtra(IntentConstants.DEVICE_VERIFY_CODE, deviceVerifyCode)
//
////         AP配网参数
//            intent.putExtra(IntentConstants.SELECTED_PRESENTER_TYPE, ConfigWifiTypeConstants.CONFIG_WIFI_SDK_AP)
//            intent.putExtra(IntentConstants.DEVICE_HOTSPOT_SSID, deviceHotspotSsid)
//            intent.putExtra(IntentConstants.DEVICE_HOTSPOT_PWD, deviceHotspotPwd)
//
////         SmartConfig特定配网参数
////        intent.putExtra(IntentConstants.SELECTED_PRESENTER_TYPE, ConfigWifiTypeConstants.CONFIG_WIFI_SDK_SMART_CONFIG)
//
////        // 声波配网特定参数
////        intent.putExtra(IntentConstants.SELECTED_PRESENTER_TYPE, ConfigWifiTypeConstants.CONFIG_WIFI_SDK_SOUND_WAVE)
//
//            startActivity(intent)
//        }

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

    private fun getTag(): String{
        return this.javaClass.simpleName
    }

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            LogUtil.e(getTag(), "网络可用")
        }

        override fun onUnavailable() {
            LogUtil.e(getTag(), "网络不可用")
        }
    }

    private fun initConfigWifiPresenters() {
        ConfigWifiExecutingActivityPresenter.addPresenter(ApConfigWifiPresenter())
        ConfigWifiExecutingActivityPresenter.addPresenter(SmartConfigWifiPresenter())
        ConfigWifiExecutingActivityPresenter.addPresenter(SoundWaveConfigWifiPresenter())
    }

}
