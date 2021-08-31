package com.huaweikitsPrueba.kitshuawe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions

class ScanKit : AppCompatActivity() {


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

        val options = HmsScanAnalyzerOptions.Creator (). setHmsScanTypes (HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create()

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

            if (obj!=null){
                // Toast.makeText(this, "Resultado Scaneo : $obj", Toast.LENGTH_LONG).show()
                   // obj.getContactDetail().peopleName.toString()

                Toast.makeText(this, "Resutado : ${obj.getLinkUrl().linkValue}", Toast.LENGTH_LONG).show()
                alertResultadoCodigoQr(this)
            }
        }
    }
    private fun alertResultadoCodigoQr(result: ScanKit){
        var alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("LECTURA CODIGO QR")
        alertDialog.setMessage("RESULTADO DE LA LECTURA DEL CODIGO QR : ")
    }
}