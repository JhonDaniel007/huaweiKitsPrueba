package com.huaweikitsPrueba.kitshuawe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huaweikitsPrueba.kitshuawe.databinding.ActivityInicioBinding

class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this,"Bienvenido",Toast.LENGTH_SHORT).show()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_inicio)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.idBtnCerrarSesion.setOnClickListener{
            confirmarSalida()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun salir(){
       val mAuthParamsHelper=HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()

        val myAuthManager=HuaweiIdAuthManager.getService(this, mAuthParamsHelper)
        val logouthTasK=myAuthManager.signOut()

        logouthTasK.addOnSuccessListener {
            startActivity(Intent(this@InicioActivity, MainActivity::class.java))
            finish()
        }

        logouthTasK.addOnFailureListener {
            Toast.makeText(this, "Logouth Fail", Toast.LENGTH_SHORT).show()
        }

    }

    private fun confirmarSalida(){
        var alertDialog =AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.hwid_auth_button_normal)
        alertDialog.setTitle("Salir")
        alertDialog.setMessage("¿Estás seguro que quieres salir de nuestra app?")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Salir"){ _,_->
            salir()
            finish()
        }
        alertDialog.setNegativeButton("Cancelar"){_,_->
            alertDialog.setCancelable(true)
        }
        alertDialog.create().show()
        }


}