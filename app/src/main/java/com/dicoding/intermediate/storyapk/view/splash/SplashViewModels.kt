package com.dicoding.intermediate.storyapk.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.intermediate.storyapk.data.model.UserModel
import com.dicoding.intermediate.storyapk.data.model.UserPreferences

class SplashViewModels (private val pref: UserPreferences) : ViewModel() {
    fun getToken(): LiveData<UserModel> {
        return pref.getToken().asLiveData()
    }
}