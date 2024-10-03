package com.correct.alahmdy.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.correct.alahmdy.data.user.User


@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("update user set username = :username where id = :id")
    suspend fun updateUsername(id: Int, username: String)

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getById(id: Int): User?

    @Delete
    suspend fun delete(entity: User)

}