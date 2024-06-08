package com.dicoding.intermediate.storyapk.view

import com.dicoding.intermediate.storyapk.data.response.StoryListItem

object DataDummy {
    fun generateStories() =
        List(100) {
            StoryListItem(
                id = "$it",
                name = "name $it",
                description = "kura kura terbang",
                photoUrl = "photo $it",
                createdAt = "createdAt $it",
                lat = "",
                lon = "",
            )
        }
}