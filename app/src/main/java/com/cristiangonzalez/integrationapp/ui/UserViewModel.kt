package com.cristiangonzalez.integrationapp.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import com.cristiangonzalez.integrationapp.data.UserRepository
import com.cristiangonzalez.integrationapp.database.entities.UserEntity
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    suspend fun findUser(email: String) : UserEntity? {
        return userRepository.findUser(email)
    }

    suspend fun insertUser(user: UserEntity) {
        return userRepository.insertUser(user)
    }
}
