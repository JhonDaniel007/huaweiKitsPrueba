package com.huaweikitsPrueba.kitshuawe.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int =0,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "age")
    var age:Int,

    @ColumnInfo(name = "email")
    var email:String,

    @ColumnInfo(name = "password")
    var password:String

)
