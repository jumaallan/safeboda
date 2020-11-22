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

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.safeboda.R
import com.safeboda.core.html.HtmlStyler
import com.safeboda.core.html.HtmlStyler.OnLinkClickListener
import com.safeboda.core.span.LabelColor
import com.safeboda.core.span.LabelColor.GRAY
import com.safeboda.core.utils.DeepLinkRouter
import com.safeboda.databinding.*
import com.safeboda.ui.base.BindingViewHolder
import com.safeboda.ui.interfaces.OnUserOrOrganizationSelectedListener
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.*
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_DIVIDER
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_FOLLOWERS
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_FOLLOWING
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_HEADER
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_LOADING
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_MENU_BUTTON
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_SPACER
import timber.log.Timber

class UserOrOrganizationAdapter(
    context: Context?,
    private val onUserOrOrganizationSelectedListener: OnUserOrOrganizationSelectedListener
) : RecyclerView.Adapter<BindingViewHolder<ViewDataBinding>>(), OnLinkClickListener {

    init {
        setHasStableIds(true)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val data = mutableListOf<ListItemProfile>()
    private val iconTint = ContextCompat.getColor(context!!, R.color.iconSecondary)

    fun setData(dataNew: List<ListItemProfile>?) {
        data.clear()
        if (dataNew != null) {
            data.addAll(dataNew)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = data[position].itemType

    override fun getItemId(position: Int) = data[position].adapterId

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewDataBinding> {
        val binding: ViewDataBinding
        when (viewType) {
            ITEM_TYPE_HEADER -> {
                binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.list_item_user_organization_profile_header,
                    parent,
                    false
                ) as ListItemUserOrganizationProfileHeaderBinding
                binding.userProfileLink.setOnClickListener { view ->
                    onLinkClicked(view, binding.userProfileLink.tag as? String ?: "")
                }
                binding.statusBackground.background =
                    LabelColor.backgroundDrawable(parent.context, GRAY)
            }
            ITEM_TYPE_FOLLOWING -> {
                binding = DataBindingUtil.inflate(
                    inflater,

                    R.layout.row_user_following,
                    parent,
                    false
                )

                val followingAdapter = FollowingAdapter(onUserOrOrganizationSelectedListener)
                val rowUserFollowingBinding = binding as RowUserFollowingBinding
                rowUserFollowingBinding.userFollowingRecyclerView.adapter =
                    followingAdapter
            }
            ITEM_TYPE_FOLLOWERS -> {
                binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.row_user_followers,
                    parent,
                    false
                )

                val followersAdapter = FollowersAdapter(onUserOrOrganizationSelectedListener)
                val rowUserFollowersBinding = binding as RowUserFollowersBinding
                rowUserFollowersBinding.userFollowersRecyclerView.adapter =
                    followersAdapter
            }
            ITEM_TYPE_MENU_BUTTON -> {
                binding =
                    DataBindingUtil.inflate(inflater, R.layout.list_item_menu_button, parent, false)
            }
            ITEM_TYPE_LOADING -> {
                binding =
                    DataBindingUtil.inflate(inflater, R.layout.list_item_loading, parent, false)
            }
            ITEM_TYPE_DIVIDER -> {
                binding =
                    DataBindingUtil.inflate(inflater, R.layout.list_item_divider, parent, false)
            }
            ITEM_TYPE_SPACER -> {
                binding =
                    DataBindingUtil.inflate(
                    inflater,
                    R.layout.list_item_spacer,
                    parent,
                    false
                ) as ListItemSpacerBinding
                binding.height =
                    binding.root.resources.getDimensionPixelSize(R.dimen.margin_medium)
            }
            else -> {
                throw IllegalStateException("Unimplemented list item type $viewType.")
            }
        }
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int) {
        when (val item = data[position]) {
            is HeaderItem -> {
                val binding = holder.binding as ListItemUserOrganizationProfileHeaderBinding
                binding.headerItem = item

                binding.userProfileLink.tag = item.websiteUrl
                HtmlStyler.styleText(binding.userProfileStatusEmoji, item.emojiHtml, this)
                HtmlStyler.styleText(binding.userProfileCompany, item.companyHtml, this)
                HtmlStyler.styleText(binding.userProfileBio, item.bioHtml, this)

                binding.userProfileCompany.compoundDrawablesRelative.first().setTint(iconTint)
                binding.userProfileFollowers.compoundDrawablesRelative.first().setTint(iconTint)
                binding.userProfileLocation.compoundDrawablesRelative.first().setTint(iconTint)
                binding.userProfileLink.compoundDrawablesRelative.first().setTint(iconTint)
            }

            is FollowingItem -> {
                val binding = holder.binding as RowUserFollowingBinding
                binding.userTitle.text =
                    """Following (""" + item.followingTotalCount + """)"""
                binding.userTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_followers_following,
                    0,
                    0,
                    0
                )
                binding.userTitle.compoundDrawablesRelative.first().mutate().setTint(iconTint)
                val followingAdapter = binding.userFollowingRecyclerView.adapter
                if (followingAdapter is FollowingAdapter) {
                    followingAdapter.submitList(item.following)
                }
            }

            is FollowersItem -> {
                val binding = holder.binding as RowUserFollowersBinding
                binding.userTitle.text =
                    """Followers (""" + item.followersTotalCount + """)"""
                binding.userTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_followers_following,
                    0,
                    0,
                    0
                )
                binding.userTitle.compoundDrawablesRelative.first().mutate().setTint(iconTint)
                val followersAdapter = binding.userFollowersRecyclerView.adapter
                if (followersAdapter is FollowersAdapter) {
                    followersAdapter.submitList(item.followers)
                }
            }

            is MenuButtonItem -> {
                val binding = holder.binding as ListItemMenuButtonBinding
                binding.menuName.text = binding.root.context.getString(item.text)
                binding.menuNumberSize.text = item.value.toString()
                binding.root.setTag(R.id.tag_profile, item.profile)
                binding.root.tag = item.type
            }

            is LoadingItem -> Unit
            is ListItemDivider -> Unit
            is Spacer -> Unit
        }
        holder.binding.executePendingBindings()
    }

    override fun onLinkClicked(view: View, url: String) {
        Timber.d("the clicked link is $url")
        DeepLinkRouter.handleRoute(view.context, Uri.parse(url))
    }
}