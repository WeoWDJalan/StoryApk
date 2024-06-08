package com.dicoding.intermediate.storyapk.view.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.intermediate.storyapk.data.model.StoryModel
import com.dicoding.intermediate.storyapk.data.response.StoryListItem
import com.dicoding.intermediate.storyapk.databinding.ItemRowStoryBinding
import com.dicoding.intermediate.storyapk.view.story.detail.DetailActivity

class ListStoryAdapter () : PagingDataAdapter<StoryListItem,ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    inner class ListViewHolder(binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private var photo = binding.ivItemPhoto
        private var profileName = binding.firstLetterName
        private var userName = binding.tvItemName



        fun bind(story: StoryListItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(photo)
            profileName.text = story.name?.substring(0, 1) ?: ""
            userName.text = story.name

            val storyModel= StoryModel(
                storyId = story.id,
                username = story.name,
                description = story.description,
                photo = story.photoUrl,
                created = story.createdAt

            )

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("Story", storyModel)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryListItem>() {
            override fun areItemsTheSame(oldItem: StoryListItem, newItem:StoryListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryListItem, newItem: StoryListItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}