package com.cristiangonzalez.integrationapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristiangonzalez.integrationapp.database.daos.UserDao
import com.cristiangonzalez.integrationapp.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao():UserDao
}