package com.huaweikitsPrueba.kitshuawe.room.dao

import androidx.room.*
import com.huaweikitsPrueba.kitshuawe.room.entities.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user:User)

    @Query("select * from user")
    fun selectAllUsers():List<User>

    @Query("select * from user where id = :idUser")
    fun selectUserById(idUser:Int):User

    @Query("select * from user where email like :emailUSer")
    fun selectUserByEmail(emailUSer:String):User

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}