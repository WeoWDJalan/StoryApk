package com.dicoding.intermediate.storyapk.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.storyapk.data.api.ServiceAPI
import com.dicoding.intermediate.storyapk.view.authentication.login.LoginViewModels
import com.dicoding.intermediate.storyapk.view.authentication.register.RegisterViewModels
import com.dicoding.intermediate.storyapk.view.main.MainViewModels
import com.dicoding.intermediate.storyapk.view.maps.MapsViewModels
import com.dicoding.intermediate.storyapk.view.splash.SplashViewModels
import com.dicoding.intermediate.storyapk.view.story.AddStoryViewModels

class ViewModelFactory(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModels::class.java) -> {
                SplashViewModels(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModels::class.java) -> {
                MainViewModels(pref) as T
            }

            modelClass.isAssignableFrom(RegisterViewModels::class.java) -> {
                RegisterViewModels() as T
            }

            modelClass.isAssignableFrom(LoginViewModels::class.java) -> {
                LoginViewModels(pref) as T
            }

            modelClass.isAssignableFrom(MapsViewModels::class.java) -> {
                MapsViewModels(pref) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModels::class.java) -> {
                AddStoryViewModels(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown Viewmodel Class: " + modelClass.name)
        }
    }

}