package com.dicoding.intermediate.storyapk.view.authentication.register

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
import com.dicoding.intermediate.storyapk.data.response.RegisterResult
import com.dicoding.intermediate.storyapk.databinding.ActivityRegisterBinding
import com.dicoding.intermediate.storyapk.view.authentication.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var registerViewModel: RegisterViewModels
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupViewModel()
        playAnimation()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[RegisterViewModels::class.java]

        registerViewModel.errorMessage.observe(this) { message ->
            when (message) {
                "User created" -> {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.alertCreatAccount), Snackbar.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                "onFailure" -> {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.failureMessage), Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    binding.edRegisterEmail.error = getString(R.string.emailFormatError)
                }
            }
        }

        registerViewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    fun setupAction(){
        binding.actionMasuk.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterNama.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            registerViewModel.registerUser(RegisterResult(name, email, password))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titteRegister, View.ALPHA, 1f).setDuration(600)
        val nameEdit = ObjectAnimator.ofFloat(binding.edRegisterNama, View.ALPHA, 1f).setDuration(600)
        val emailEdit = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(600)
        val passwordEdit = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(600)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(600)
        val tv_sudahpunyaakun = ObjectAnimator.ofFloat(binding.tvSudahpunyaakun, View.ALPHA, 1f).setDuration(600)
        val loginAction = ObjectAnimator.ofFloat(binding.actionMasuk, View.ALPHA, 1f).setDuration(600)

        AnimatorSet().apply {
            playSequentially(title, nameEdit, emailEdit, passwordEdit, register, tv_sudahpunyaakun, loginAction)
            start()
        }
    }
}