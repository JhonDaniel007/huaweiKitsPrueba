package com.huaweikitsPrueba.kitshuawe

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.huawei.hms.framework.common.ContextCompat
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.huaweikitsPrueba.kitshuawe.push.MyApplication

class ScanKit : AppCompatActivity() {

    var listScans: MutableList<String> =ArrayList()

    companion object{
        const val DEFAULT_VIEW=0x22
        const val REQUES_CODE_SCAN=0x01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_kit)

        scanKit()

    }
    private fun scanKit(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            DEFAULT_VIEW)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val options = HmsScanAnalyzerOptions.Creator().setHmsScanTypes (HmsScan.ALL_SCAN_TYPE).create()
        if (requestCode == REQUES_CODE_SCAN && grantResults.size<2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (requestCode== DEFAULT_VIEW){
            ScanUtil.startScan(this, REQUES_CODE_SCAN, options)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== RESULT_OK || data==null){
            return
        }
        if (requestCode== REQUES_CODE_SCAN){
            var obj = data.getParcelableExtra(ScanUtil.RESULT) as HmsScan?
            obj?.showResult

            var alerDialog=AlertDialog.Builder(this)
            alerDialog.setTitle("Resultado")
            alerDialog.setCancelable(false)
            alerDialog.setMessage("${obj?.showResult}")
            alerDialog.setPositiveButton("Continuar"){_,_->
                displayNotify()

                scanKit()

            }
            alerDialog.create().show()
            Toast.makeText(this, "${obj}", Toast.LENGTH_SHORT).show()
            if (obj!=null){
               // Toast.makeText(this, "Resutado : ${obj}", Toast.LENGTH_LONG).show()
                Log.d("Resultado Scanneo", obj.showResult.toString())

                    listScans.add(obj.showResult.toString())
                    Log.d("LISTA", listScans.toString())

            }
        }
    }

    private fun displayNotify(){
        val varColorNotify=resources.getColor(R.color.comprar)

        val intent=Intent(this, InicioActivity::class.java)
        val pendingIntent= PendingIntent.getActivity(this, 100,intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notificacionBuilder= NotificationCompat.Builder(this, MyApplication.CHANNEL_IDD)
        notificacionBuilder.setContentIntent(pendingIntent)
            .setContentTitle("Compra exitosa")
            .setContentText("Has hecho una compra en un restaurante")
            .setSmallIcon(R.drawable.ic_help)
            .setAutoCancel(true)
            .color = varColorNotify

        val notificationManager= ContextCompat.getSystemService(
            this,
            NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.notify(1, notificacionBuilder.build() )
    }


}