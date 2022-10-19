package com.robosoft.passwordmanagermobile.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robosoft.passwordmanagermobile.dataclass.User
import com.robosoft.passwordmanagermobile.interfacee.UserDAO

@Database(entities = [User::class], version = 1)
abstract class UserDB: RoomDatabase() {

    abstract fun userDao(): UserDAO

    companion object{

        @Volatile
        private var INSTANCE : UserDB? = null

        fun getDatabase(context: Context): UserDB{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDB::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }

}