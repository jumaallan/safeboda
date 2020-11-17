package com.safeboda.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.safeboda.data.local.entities.User
import com.safeboda.data.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getUserByUserID(userID: String): LiveData<User> = userRepository.getUserByUserID(userID)

    fun fetchUsers(): LiveData<List<User>> = userRepository.fetchUsers().asLiveData()
}