package com.huaweikitsPrueba.kitshuawe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.huaweikitsPrueba.kitshuawe.databinding.ActivityLogInBinding
import com.huaweikitsPrueba.kitshuawe.room.entities.User
import com.huaweikitsPrueba.kitshuawe.room.query.QueryUser

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnRegisterUser.setOnClickListener { registerUSer() }

    }


    private fun registerUSer(){

        val emptyName=validarCampo(binding.txtUsername,binding.txtLayoutUsername)
        val emptyAge=validarCampo(binding.txtUserAge,binding.txtLayoutUserAge)
        val emptyEmail=validarCampo(binding.txtUserEmail,binding.txtLayoutUserEmail)
        val emptyPass=validarCampo(binding.txtUserPass,binding.txtLayoutUserPass)
        val emptyConfirmPass=validarCampo(binding.txtUserConfirnPass,binding.txtLayoutUserConfirmPass)

        if(!(emptyName && emptyAge && emptyEmail && emptyPass && emptyConfirmPass)){

            val name=binding.txtUsername.text.toString()
            val age = binding.txtUserAge.text.toString()
            val email=binding.txtUserEmail.text.toString()
            val pass=binding.txtUserPass.text.toString()
            val confirmPass=binding.txtUserConfirnPass.text.toString()

            if(!validarEmail(email,binding.txtLayoutUserEmail))
                return

            if(pass != confirmPass){
                binding.txtLayoutUserConfirmPass.error="Las contraseñas no coinciden"
                return
            }
            val user=User(name=name,age=age.toInt(),email=email,password = pass)
            if(QueryUser.registerUser(user,this)){
                startActivity(Intent(this@LogInActivity,InicioActivity::class.java))
                finish()
            }else
                Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarCampo(editText: TextInputEditText, txtLayout: TextInputLayout): Boolean {
        val empty=editText.text.toString().isEmpty()
        if(empty)
            txtLayout.error="Complete este campo"
        else
            txtLayout.error=null
        return empty
    }

    private fun validarEmail(email:String,layout:TextInputLayout):Boolean{
        val emailCorrecto= Patterns.EMAIL_ADDRESS
        val emailValido= emailCorrecto.matcher(email).matches()

        if(!emailValido)
            layout.error="Ingreses un email valido"

        return emailValido
    }


}