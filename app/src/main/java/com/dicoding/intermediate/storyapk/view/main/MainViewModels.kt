package com.dicoding.intermediate.storyapk.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.dicoding.intermediate.storyapk.data.api.ConfigAPI
import com.dicoding.intermediate.storyapk.data.api.ServiceAPI
import com.dicoding.intermediate.storyapk.data.model.StoryModel
import com.dicoding.intermediate.storyapk.data.model.UserModel
import com.dicoding.intermediate.storyapk.data.model.UserPreferences
import com.dicoding.intermediate.storyapk.data.paging.StoryPaging
import com.dicoding.intermediate.storyapk.data.response.StoryListItem
import com.dicoding.intermediate.storyapk.data.response.StoryListResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModels (private val pref: UserPreferences) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    val loading = MutableLiveData<Boolean>()
    val listStory = MutableLiveData<List<StoryListItem>>()

    fun pager(): LiveData<PagingData<StoryListItem>> = liveData {
        val token = pref.getToken().first() // Retrieve the token asynchronously
        emitSource(
            Pager(
                config = PagingConfig(pageSize = 5),
                pagingSourceFactory = { StoryPaging(ConfigAPI.getApiService(), "Bearer ${token.token}") }
            ).liveData.cachedIn(viewModelScope)
        )
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun getToken(): LiveData<UserModel> {
        return pref.getToken().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}