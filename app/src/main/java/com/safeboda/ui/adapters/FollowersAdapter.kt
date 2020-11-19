/*
 * Copyright 2020 Safeboda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.safeboda.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safeboda.core.data.models.UserOrOrganization.Follower
import com.safeboda.core.html.HtmlStyler
import com.safeboda.databinding.ListItemUserFollowersBinding
import com.safeboda.ui.interfaces.OnUserOrOrganizationSelectedListener
import timber.log.Timber

object FollowersDiffer : DiffUtil.ItemCallback<Follower>() {

    override fun areItemsTheSame(oldItem: Follower, newItem: Follower) =
        oldItem.login == newItem.login

    override fun areContentsTheSame(oldItem: Follower, newItem: Follower) =
        oldItem == newItem
}

internal class FollowersAdapter(val onUserOrOrganizationSelectedListener: OnUserOrOrganizationSelectedListener) :
    ListAdapter<Follower, FollowersAdapter.ViewHolder>(FollowersDiffer) {

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
        holder.bind(getItem(position))

    inner class ViewHolder(val binding: ListItemUserFollowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Follower) {
            binding.user = item
            binding.followers.setOnClickListener {
                onUserOrOrganizationSelectedListener.onUserOrOrgSelected(item.login)
            }
            binding.userName.text = item.name
            binding.userLogin.text = item.login
            HtmlStyler.styleText(binding.userBio, item.bioHtml, null)
            binding.widthRatio = ITEM_WIDTH_RATIO
            binding.executePendingBindings()
        }
    }
}