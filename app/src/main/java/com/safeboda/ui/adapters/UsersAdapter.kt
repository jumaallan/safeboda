package com.safeboda.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safeboda.databinding.ListItemUserBinding
import com.safeboda.ui.viewmodel.UsersViewModel.ListItemUser

internal typealias ClickListener = (ListItemUser) -> Unit

object UserDiffer : DiffUtil.ItemCallback<ListItemUser>() {

    override fun areItemsTheSame(oldItem: ListItemUser, newItem: ListItemUser) =
        oldItem.login == newItem.login

    override fun areContentsTheSame(oldItem: ListItemUser, newItem: ListItemUser) =
        oldItem == newItem
}

internal class UsersAdapter(
    private val listener: ClickListener
) : ListAdapter<ListItemUser, UsersAdapter.ViewHolder>(UserDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), listener)

    inner class ViewHolder(val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListItemUser, listener: ClickListener) {
            binding.root.setOnClickListener {
                listener.invoke(item)
            }
            binding.user = item
            binding.userBio.text = item.htmlText
            binding.executePendingBindings()
        }
    }
}