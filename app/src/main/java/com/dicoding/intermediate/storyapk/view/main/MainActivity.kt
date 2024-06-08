package com.dicoding.intermediate.storyapk.view.main

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.intermediate.storyapk.R
import com.dicoding.intermediate.storyapk.data.api.ConfigAPI.getApiService
import com.dicoding.intermediate.storyapk.data.api.ServiceAPI
import com.dicoding.intermediate.storyapk.data.model.StoryModel
import com.dicoding.intermediate.storyapk.data.model.UserPreferences
import com.dicoding.intermediate.storyapk.data.model.ViewModelFactory
import com.dicoding.intermediate.storyapk.data.paging.StoryPaging
import com.dicoding.intermediate.storyapk.databinding.ActivityMainBinding
import com.dicoding.intermediate.storyapk.view.authentication.login.LoginActivity
import com.dicoding.intermediate.storyapk.view.maps.MapsActivity
import com.dicoding.intermediate.storyapk.view.story.AddStoryActivity
import com.dicoding.intermediate.storyapk.view.story.ListStoryAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var mainViewModel : MainViewModels
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvStory: RecyclerView
    private val list = ArrayList<StoryModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        rvStory = binding.rvListStory
        rvStory.setHasFixedSize(true)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModels::class.java]

        mainViewModel.pager().cachedIn(mainViewModel).observe(this) {
            Log.d(TAG, it.toString())
            list.clear()

            rvStory.layoutManager = LinearLayoutManager(this)

            val listStoryAdapter = ListStoryAdapter()
                listStoryAdapter.submitData(lifecycle, it)
                rvStory.adapter = listStoryAdapter

            super.onResume()
            // Memuat ulang data ketika aktivitas kembali aktif
            mainViewModel.pager().cachedIn(mainViewModel).observe(this) {
                Log.d(TAG, it.toString())
                listStoryAdapter.submitData(lifecycle, it)
            }
        }

        mainViewModel.loading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMessage.observe(this) {
            when (it) {
                "Story Loaded Successfully" -> {
                    Toast.makeText(this@MainActivity, getString(R.string.storyLoadedSuccess), Toast.LENGTH_SHORT).show()
                }
                "onFailure" -> {
                    Toast.makeText(this@MainActivity, getString(R.string.failureMessage), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupAction() {
        val swipeRefresh = binding.swipeRefresh
        swipeRefresh.setOnRefreshListener {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            swipeRefresh.isRefreshing = false
        }

        binding.ivAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMenu(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMenu(itemId: Int) {
        when (itemId) {
            R.id.action_maps->{
                startActivity(Intent(this@MainActivity,MapsActivity::class.java))
            }
            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.action_logout -> {
                val builder = AlertDialog.Builder(this)
                val alert = builder.create()
                builder
                    .setTitle(getString(R.string.menuLogout))
                    .setMessage(getString(R.string.alertMassageLogout))
                    .setPositiveButton(getString(R.string.yesLogout)) { _, _ ->
                        mainViewModel.logout()
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                    }
                    .setNegativeButton(getString(R.string.cancelLogout)) { _, _ ->
                        alert.cancel()
                    }
                    .show()
            }
        }
    }
}