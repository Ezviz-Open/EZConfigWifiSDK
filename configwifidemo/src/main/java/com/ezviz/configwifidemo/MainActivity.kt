package com.ezviz.configwifidemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.ezviz.sdk.configwifi.Config
import ezviz.ezopensdkcommon.common.IntentConstants
import ezviz.ezopensdkcommon.common.RootActivity
import ezviz.ezopensdkcommon.common.RouteNavigator
import ezviz.ezopensdkcommon.configwifi.AutoWifiPrepareStepOneActivity
import ezviz.ezopensdkcommon.configwifi.ConfigWifiExecutingActivityPresenter
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = RouteNavigator.CONFIG_WIFI_MAIN_PAGE)
class MainActivity : RootActivity() {

    private val TAG = MainActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Config.LOGGING = true

        initUi()
    }

    @SuppressLint("SetTextI18n")
    override fun initUi() {
        config_wifi_demo_tv_sdk_version.text = "version: ${Config.VERSION}"
    }

    private fun initConfigWifiPresenters() {
        ConfigWifiExecutingActivityPresenter.addPresenter(ApConfigWifiPresenter())
        ConfigWifiExecutingActivityPresenter.addPresenter(SmartConfigWifiPresenter())
        ConfigWifiExecutingActivityPresenter.addPresenter(SoundWaveConfigWifiPresenter())
    }

    fun onClickNext(view: View) {
        val isEmptySerial = TextUtils.isEmpty(app_common_device_serial.text.toString())
        val isEmptyVerifyCode = TextUtils.isEmpty(app_common_device_verify_code.text.toString())
        if (isEmptySerial || isEmptyVerifyCode){
            showToast(getString(R.string.serial_or_verify_code_not_empty))
            return
        }
        initConfigWifiPresenters()
        val jumpIntent = Intent(this, AutoWifiPrepareStepOneActivity::class.java)
        jumpIntent.putExtra(IntentConstants.USING_CONFIG_WIFI_SDK, true)
        jumpIntent.putExtra(IntentConstants.DEVICE_SERIAL, app_common_device_serial.text.toString())
        jumpIntent.putExtra(IntentConstants.DEVICE_VERIFY_CODE, app_common_device_verify_code.text.toString())
        startActivity(jumpIntent)
    }
}
