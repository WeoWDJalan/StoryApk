package com.dicoding.intermediate.storyapk.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryModel(
    @field:SerializedName("id")
    val storyId: String? = null,

    @field:SerializedName("name")
    val username: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("created")
    val created: String? = null,
): Parcelable
