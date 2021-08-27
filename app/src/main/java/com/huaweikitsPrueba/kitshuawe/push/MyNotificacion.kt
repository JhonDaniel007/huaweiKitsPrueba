package com.huaweikitsPrueba.kitshuawe.push

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import javax.security.auth.callback.Callback
import kotlin.concurrent.thread

class MyNotificacion:HmsMessageService() {

companion object{
    private const val TAG="HuaweiPushService"
}

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG,"MENSAJE rECIBIDO")
        remoteMessage?.let {
            Log.d(TAG," -Data : ${it.data}")
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d(TAG,"Huawei push :$token")
    }
}
class GetTokenAction(){
    var handler:Handler= Handler(Looper.getMainLooper())
    fun getToken(context: Context, callback: (String)->Unit){
        thread {
            try {
                var AppId=AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                var token=HmsInstanceId.getInstance(context).getToken(AppId,"HCM")
                handler.post{callback(token)}
            }catch (e:Exception){
                Log.d("ERROR", e.toString())
            }
        }
    }
}
