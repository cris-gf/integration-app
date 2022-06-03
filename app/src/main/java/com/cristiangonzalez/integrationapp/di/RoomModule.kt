package com.cristiangonzalez.integrationapp.di

import android.content.Context
import androidx.room.Room
import com.cristiangonzalez.integrationapp.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val USER_DATABASE_NAME = "user_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, UserDatabase::class.java, USER_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideUserDao(db:UserDatabase) = db.userDao()

}