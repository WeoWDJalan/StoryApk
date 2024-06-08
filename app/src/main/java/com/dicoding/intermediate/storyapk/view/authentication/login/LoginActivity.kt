package com.dicoding.intermediate.storyapk.view.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.storyapk.R
import com.dicoding.intermediate.storyapk.data.model.UserPreferences
import com.dicoding.intermediate.storyapk.data.model.ViewModelFactory
import com.dicoding.intermediate.storyapk.databinding.ActivityLoginBinding
import com.dicoding.intermediate.storyapk.view.authentication.register.RegisterActivity
import com.dicoding.intermediate.storyapk.view.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var loginViewModel: LoginViewModels
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModels::class.java]

        loginViewModel.errorMessage.observe(this) {
            when (it) {
                "success" -> {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.alertTitle), Snackbar.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

                "onFailure" -> {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.failureMessage), Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.emailFormatError), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        loginViewModel.loading.observe(this) {
            showLoading(it)
        }
    }


    fun setupAction(){
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginViewModel.login(email, password)
        }

        binding.actionRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitleLoginPage, View.ALPHA, 1f).setDuration(400)
        val message = ObjectAnimator.ofFloat(binding.tvMessageLoginPage, View.ALPHA, 1f).setDuration(400)
        val emailEdit = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(400)
        val passwordEdit = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(400)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)
        val belumPunyaAkun = ObjectAnimator.ofFloat(binding.tvBelumpunyaakun, View.ALPHA, 1f).setDuration(400)
        val registerAction = ObjectAnimator.ofFloat(binding.actionRegister, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(title, message, emailEdit, passwordEdit, login, belumPunyaAkun, registerAction)
            start()
        }
    }

}