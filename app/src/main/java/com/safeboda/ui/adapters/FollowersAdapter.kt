package com.safeboda.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safeboda.core.data.models.UserOrOrganization.Follower
import com.safeboda.core.html.HtmlStyler
import com.safeboda.databinding.ListItemUserFollowersBinding

internal typealias FollowersClickListener = (Follower) -> Unit

object FollowersDiffer : DiffUtil.ItemCallback<Follower>() {

    override fun areItemsTheSame(oldItem: Follower, newItem: Follower) =
        oldItem.login == newItem.login

    override fun areContentsTheSame(oldItem: Follower, newItem: Follower) =
        oldItem == newItem
}

internal class FollowersAdapter(
    private val listener: FollowersClickListener
) : ListAdapter<Follower, FollowersAdapter.ViewHolder>(FollowersDiffer) {

    companion object {
        private const val ITEM_WIDTH_RATIO = 0.75f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemUserFollowersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), listener)

    inner class ViewHolder(val binding: ListItemUserFollowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Follower, listener: FollowersClickListener) {
            binding.root.setOnClickListener {
                listener.invoke(item)
            }
            binding.user = item
            binding.userName.text = item.name
            binding.userName.text = item.login
            HtmlStyler.styleText(binding.userBio, item.bioHtml, null)
            binding.widthRatio = ITEM_WIDTH_RATIO
            binding.executePendingBindings()
        }
    }
}