package com.dicoding.intermediate.storyapk.view.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.intermediate.storyapk.data.model.StoryModel
import com.dicoding.intermediate.storyapk.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryModel>("Story") as StoryModel
        Glide.with(applicationContext)
            .load(story.photo)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story.username
        binding.tvDetailDescription.text = story.description
        binding.tvFirstLetterName.text = story.username?.substring(0, 1) ?: ""
        binding.tvDetailCreated.text = story.created?.substringBefore("T") ?: ""
    }
}