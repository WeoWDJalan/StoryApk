package com.dicoding.intermediate.storyapk.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate.storyapk.data.api.ConfigAPI
import com.dicoding.intermediate.storyapk.data.model.UserModel
import com.dicoding.intermediate.storyapk.data.model.UserPreferences
import com.dicoding.intermediate.storyapk.data.response.LoginResponse
import com.dicoding.intermediate.storyapk.data.response.StoryListItem
import com.dicoding.intermediate.storyapk.data.response.StoryListResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModels (private val pref: UserPreferences) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    private var job: Job? = null
    val loading = MutableLiveData<Boolean>()
    val listStory = MutableLiveData<List<StoryListItem>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getToken(): LiveData<UserModel> {
        return pref.getToken().asLiveData()
    }

    fun mapsData(token: String) {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val service = ConfigAPI.getApiService().getStoriesWithLocation("Bearer $token")

            withContext(Dispatchers.Main) {
                service.enqueue(object : Callback<StoryListResponse> {
                    override fun onResponse(
                        call: Call<StoryListResponse>,
                        response: Response<StoryListResponse>
                    ) {
                        val responseBody = response.body()
                        if (response.isSuccessful ) {
                            if (responseBody != null && !responseBody.error) {
                                loading.value = false

                                listStory.postValue(responseBody.listStory)
                                onError(responseBody.message)
                            }
                        } else {
                            onError("Error : ${response.message()} ")
                        }
                    }

                    override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                        onError("onFailure")
                    }
                })

            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}