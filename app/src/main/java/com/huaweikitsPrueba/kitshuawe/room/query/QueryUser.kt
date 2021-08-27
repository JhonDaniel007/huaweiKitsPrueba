package com.huaweikitsPrueba.kitshuawe.room.query

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.room.DatabaseConfiguration
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.material.snackbar.Snackbar
import com.huaweikitsPrueba.kitshuawe.InicioActivity
import com.huaweikitsPrueba.kitshuawe.R
import com.huaweikitsPrueba.kitshuawe.room.config.DBConfig
import com.huaweikitsPrueba.kitshuawe.room.entities.User
import kotlinx.coroutines.*
import java.util.ArrayList

class QueryUser private constructor() {



    companion object{

        const val KEY_SESSION="SESSION"
        const val KEY_EMAIl="EMAIL"
        fun getDatabase(context: Context): DBConfig {
            return Room.databaseBuilder(context,
                DBConfig::class.java,
                DBConfig.DATABASE_NAME
            ).build()
        }

        private fun getSharePref(context: Context):SharedPreferences{
            return context.getSharedPreferences(
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE
            )
        }

        fun loginUser(email:String, password:String, context: Context):Boolean{
            val db= getDatabase(context)
            var userList:ArrayList<User>
            var coincidencia=false

            val job= runBlocking {
                withContext(Dispatchers.IO){
                    userList=db.userDao().selectAllUsers() as ArrayList<User>
                }
                for(user in userList)
                    if(email==user.email && password==user.password){
                        setSession(true,email,context)
                        coincidencia=true

                    }

            }

            Log.d("COINCIDENCIAS","$coincidencia")
            return coincidencia

        }

        fun registerUser(user: User, context: Context):Boolean{

            val bd= getDatabase(context)
            var registroExitoso=false

            val job= runBlocking {
                withContext(Dispatchers.IO){
                    bd.userDao().insertUser(user)
                    registroExitoso = true
                }

                withContext(Dispatchers.IO){
                    setSession(true,user.email,context)
                }
                Log.d("Register","Usuario Registrado")

            }
            Log.d("REGISITROEXITOSO","$registroExitoso")
            return registroExitoso

        }

        fun sessionActiva(context: Context):Boolean{
            val preferences= getSharePref(context)
            val activa=preferences.getBoolean(KEY_SESSION,false)
            if(activa)
                Log.d("Session USER","Esta Activa")
            else
                Log.d("Session USER","No Esta Activa")
            return activa
        }

        fun setSession(session:Boolean,email: String,context: Context){
            val preferences= getSharePref(context)
            preferences.edit {
                this.putBoolean(KEY_SESSION,session)
                this.putString(KEY_EMAIl,email)
                commit()
            }
        }

        fun getCurrentUSer(context: Context):User?{
            val bd= getDatabase(context)
            val preferences= getSharePref(context)
            var user:User?=null

            runBlocking {
                launch(Dispatchers.IO) {
                    val email=preferences.getString(KEY_EMAIl,null)
                     user= email?.let { bd.userDao().selectUserByEmail(it) }
                }

            }
            Log.d("USERNAME","${user?.name}")
            return user
        }


    }

}