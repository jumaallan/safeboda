package com.safeboda.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safeboda.core.data.models.UserOrOrganization.Following
import com.safeboda.core.html.HtmlStyler
import com.safeboda.databinding.ListItemUserFollowingBinding

internal typealias FollowingClickListener = (Following) -> Unit

object FollowingDiffer : DiffUtil.ItemCallback<Following>() {

    override fun areItemsTheSame(oldItem: Following, newItem: Following) =
        oldItem.login == newItem.login

    override fun areContentsTheSame(oldItem: Following, newItem: Following) =
        oldItem == newItem
}

internal class FollowingAdapter(
    private val listener: FollowingClickListener
) : ListAdapter<Following, FollowingAdapter.ViewHolder>(FollowingDiffer) {

    companion object {
        private const val ITEM_WIDTH_RATIO = 0.75f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemUserFollowingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), listener)

    inner class ViewHolder(val binding: ListItemUserFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Following, listener: FollowingClickListener) {
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