package com.dicoding.intermediate.storyapk.view.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.storyapk.R
import com.dicoding.intermediate.storyapk.data.model.UserPreferences
import com.dicoding.intermediate.storyapk.data.model.ViewModelFactory
import com.dicoding.intermediate.storyapk.view.authentication.login.LoginActivity
import com.dicoding.intermediate.storyapk.view.main.MainActivity

class SplashScreen : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var splashViewModel: SplashViewModels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        setupViewModel()

        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.getToken().observe(this) { session ->
                if (session.Login) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }, 2000)
    }

    private fun setupViewModel(){
        splashViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[SplashViewModels::class.java]
    }
}