package com.huaweikitsPrueba.kitshuawe.room.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huaweikitsPrueba.kitshuawe.room.dao.UserDao
import com.huaweikitsPrueba.kitshuawe.room.entities.User

@Database(entities = arrayOf(User::class), version = 1)
abstract class DBConfig: RoomDatabase() {

    abstract fun userDao():UserDao

    companion object{
        const val DATABASE_NAME="users"
    }
}