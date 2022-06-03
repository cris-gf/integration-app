package com.cristiangonzalez.integrationapp.data

import com.cristiangonzalez.integrationapp.database.daos.UserDao
import com.cristiangonzalez.integrationapp.database.entities.UserEntity
import javax.inject.Inject

//Inyeccion de metodos DAO
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun findUser(email: String) = userDao.findUser(email)

    suspend fun insertUser(user: UserEntity) = userDao.insert(user)

}