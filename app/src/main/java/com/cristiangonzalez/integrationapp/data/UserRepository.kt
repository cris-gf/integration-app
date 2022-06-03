package com.cristiangonzalez.integrationapp.data

import com.cristiangonzalez.integrationapp.database.daos.UserDao
import com.cristiangonzalez.integrationapp.database.entities.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun findUser(user: String) = userDao.findUser(user)

    suspend fun insertUser(user: UserEntity) = userDao.insert(user)
}