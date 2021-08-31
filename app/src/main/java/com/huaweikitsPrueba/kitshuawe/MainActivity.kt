package com.huaweikitsPrueba.kitshuawe

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huaweikitsPrueba.kitshuawe.databinding.ActivityMainBinding
import com.huaweikitsPrueba.kitshuawe.push.GetTokenAction
import com.huaweikitsPrueba.kitshuawe.room.query.QueryUser

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var permisoCamarKitScan:Int=1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.idBtnLogin.setOnClickListener {
            loginHuawei()
        }
        GetTokenAction().getToken(this) {
            Log.d("Token Push", it)
        }


        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener { signInRoom() }

    }
    //PERMISO PARA KIT MAP
    fun permisos() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(ContentValues.TAG, "android sdk <= 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            // Dynamically apply for the android.permission.ACCESS_BACKGROUND_LOCATION permission in addition to the preceding permissions if the API level is higher than 28.
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }
    }

    //PERMISO PARA SCAN KIT
    private fun permisosScanKit(){

        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED->{
                tomarFoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)->{
                Toast.makeText(this, "Debes dar permiso para la camara", Toast.LENGTH_SHORT).show()
            }
            else->{
                requestPermissions(arrayOf(Manifest.permission.CAMERA), permisoCamarKitScan)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            permisoCamarKitScan->{
                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissssssoooos", Toast.LENGTH_SHORT).show()
                }
            }
            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }


    }
    private fun tomarFoto(){

    }

    private fun loginHuawei() {
        var myIdAuthParamsHelper =
            HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setId()
                .setIdToken()
                .setAccessToken()
                .setEmail()
                .setProfile()
                .createParams()

        var myAuthManager = HuaweiIdAuthManager.getService(this, myIdAuthParamsHelper)
        startActivityForResult(myAuthManager.signInIntent, 100)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Login cancelado", Toast.LENGTH_SHORT).show()
                loginFalido()
            } else if (resultCode == Activity.RESULT_OK) {
                var loginTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
                if (loginTask.isSuccessful) {
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                    loginExitoso()
                } else {
                    Toast.makeText(this, "TRevisa tu conexion a iunternet", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun loginExitoso() {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("BIENVENIDO")
        alertDialog.setMessage("Gracias por preferirnos, en nuestra app encontraras multidud de contenido de alto interes para ti")
        alertDialog.setCancelable(false)
        alertDialog.setIcon(R.drawable.hwid_auth_button_normal)
        alertDialog.setPositiveButton("Continuar") { _, _ ->
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            permisos()
            permisosScanKit()
        }
        alertDialog.setNegativeButton("Cancelar") { _, _ ->
            finish()
        }

        alertDialog.create().show()
    }

    fun loginFalido() {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("ERROR")
        alertDialog.setMessage("Hemos un error en el servicio, por favor revisa tu conexion a intenet o intentalo más tarde")
        alertDialog.setCancelable(false)
        alertDialog.setIcon(R.drawable.hwid_auth_button_normal)
        alertDialog.setPositiveButton("Continuar") { _, _ ->
            alertDialog.setCancelable(false)
        }
        alertDialog.setNegativeButton("Cancelar") { _, _ ->
            finish()
        }
        alertDialog.create().show()

    }

    fun signInRoom() {

        val emptyEmail = validarCampo(binding.txtEmail, binding.txtLayoutEmail)
        val emptyPass = validarCampo(binding.txtPassword, binding.txtLayoutPass)
        if (!emptyEmail && !emptyPass) {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            if (QueryUser.loginUser(email, password, this)) {
                startActivity(Intent(this@MainActivity, InicioActivity::class.java))
                finish()
            } else
                Toast.makeText(this, "El email o contraseña son incorrectos", Toast.LENGTH_SHORT)
                    .show()
        }

    }


    fun validarCampo(editText: TextInputEditText, textLayout: TextInputLayout): Boolean {
        val empty = editText.text.toString().isEmpty()
        if (empty)
            textLayout.error = "Complete este campo"
        else
            textLayout.error = null

        return empty
    }


}