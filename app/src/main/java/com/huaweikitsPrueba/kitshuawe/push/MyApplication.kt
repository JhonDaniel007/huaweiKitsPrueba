package com.huaweikitsPrueba.kitshuawe.push

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.huaweikitsPrueba.kitshuawe.R

class MyApplication: Application() {
    companion object{
        const val CHANNEL_IDD="ChannelId"
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name=getString(R.string.channel_name)
            val descriptionChannel=getString(R.string.channel_description)
            val impotance=NotificationManager.IMPORTANCE_HIGH
            val mChannel=NotificationChannel(CHANNEL_IDD, name, impotance)
                .apply { description=descriptionChannel }

            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }

}