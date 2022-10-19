package com.robosoft.passwordmanagermobile.interfacee

import androidx.room.*
import com.robosoft.passwordmanagermobile.dataclass.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    /* @Query("SELECT * FROM student_table WHERE uid IN (:userIds)")
     fun loadAllByIds(userIds: IntArray): List<Student>*/

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

}