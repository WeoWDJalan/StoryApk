package com.dicoding.intermediate.storyapk.data.response

import com.google.gson.annotations.SerializedName

data class StoryListResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<StoryListItem>,

    @field:SerializedName("error")
    val error: Boolean,
)

data class StoryListItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: String,

    @field:SerializedName("lon")
    val lon: String
)

data class FileUploadResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)