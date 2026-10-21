package com.example.githubclient.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubclient.R
import com.example.githubclient.data.RepoDto
import com.example.githubclient.databinding.ItemRepoBinding

class RepoAdapter(
    private var items: List<RepoDto> = emptyList(),
    private val onItemClick: (RepoDto) -> Unit
) : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    fun updateList(newItems: List<RepoDto>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RepoViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepoDto) {
            binding.tvRepoName.text = item.name

            if (item.description.isNullOrBlank()) {
                binding.tvRepoDescription.text = binding.root.context.getString(R.string.no_description_message)
            } else {
                binding.tvRepoDescription.text = item.description
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}