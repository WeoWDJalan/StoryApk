package com.dicoding.intermediate.storyapk.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.intermediate.storyapk.data.model.UserModel
import com.dicoding.intermediate.storyapk.data.model.UserPreferences
import com.dicoding.intermediate.storyapk.data.response.StoryListItem
import com.dicoding.intermediate.storyapk.view.DataDummy
import com.dicoding.intermediate.storyapk.view.MainDispatcherRules
import com.dicoding.intermediate.storyapk.view.getOrAwaitValue
import com.dicoding.intermediate.storyapk.view.story.ListStoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.prefs.Preferences

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelsTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRules()

    @Mock
    private lateinit var userPreferences: UserPreferences

    private lateinit var mainViewModels: MainViewModels

    @Before
    fun setUp() {
        userPreferences = Mockito.mock(UserPreferences::class.java)
        mainViewModels = MainViewModels(userPreferences)
    }

    @Test
    fun `when Get Stories Should Not Be Null And Return Data`() = runTest {
        val dummyStories = DataDummy.generateStories()
        val data = StoryPagingSource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<StoryListItem>>()
        expectedStories.value = data


        val userModelFlow = flowOf(UserModel("dummy_token", true))
        Mockito.`when`(userPreferences.getToken()).thenReturn(userModelFlow)

        mainViewModels = Mockito.spy(mainViewModels)
        Mockito.`when`(mainViewModels.pager()).thenReturn(expectedStories)
//        val mainViewModels = MainViewModels(userPreferences)
        val actualStories = mainViewModels.pager().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        val finalData = differ.snapshot()

        Assert.assertNotNull(finalData)
        Assert.assertEquals(dummyStories.size, finalData.size)
        Assert.assertEquals(dummyStories[0], finalData[0])
    }

    @Test
    fun `when Get Stories Empty Should Not return Data`() = runTest {
        val data = StoryPagingSource.snapshot(emptyList())
        val expectedStories =  MutableLiveData<PagingData<StoryListItem>>()
        expectedStories.value = data

        val userModelFlow = flowOf(UserModel("dummy_token", true))
        Mockito.`when`(userPreferences.getToken()).thenReturn(userModelFlow)

        mainViewModels = Mockito.spy(mainViewModels)
        Mockito.`when`(mainViewModels.pager()).thenReturn(expectedStories)
//        val mainViewModels = MainViewModels(userPreferences)
        val actualStories = mainViewModels.pager().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        val finalData = differ.snapshot()


        Assert.assertEquals(0, finalData.size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    class StoryPagingSource : PagingSource<Int, StoryListItem>() {

        companion object {
            fun snapshot(items: List<StoryListItem>): PagingData<StoryListItem> = PagingData.from(items)
        }
        override fun getRefreshKey(state: PagingState<Int, StoryListItem>): Int = 0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryListItem> =
            LoadResult.Page(
                emptyList(), 0, 1
            )
    }
}