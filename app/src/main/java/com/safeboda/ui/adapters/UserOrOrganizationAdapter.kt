package com.safeboda.ui.adapters

import android.content.Context
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
import com.safeboda.databinding.ListItemSpacerBinding
import com.safeboda.databinding.ListItemUserOrganizationProfileHeaderBinding
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.*
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_DIVIDER
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_HEADER
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_LOADING
import com.safeboda.ui.viewmodel.UserOrganizationViewModel.ListItemProfile.Companion.ITEM_TYPE_SPACER
import timber.log.Timber

class UserOrOrganizationAdapter(
    context: Context?
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
                    binding.root.resources.getDimensionPixelSize(R.dimen.margin_largest)
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
                binding.avatarUrl = item.avatarUrl
                binding.name = item.name
                binding.login = item.login
                binding.websiteUrl = item.websiteUrl
                binding.companyHtml = item.companyHtml
                binding.bioHtml = item.bioHtml
                binding.emojiHtml = item.emojiHtml
                binding.statusMessage = item.statusMessage
                binding.location = item.location
                binding.followersCount = item.followersCount
                binding.followingCount = item.followingCount
                binding.isFollowing = item.isFollowing
                binding.showFollowButton = item.showFollowButton

                binding.userProfileLink.tag = item.websiteUrl
                HtmlStyler.styleText(binding.userProfileStatusEmoji, item.emojiHtml, this)
                HtmlStyler.styleText(binding.userProfileCompany, item.companyHtml, this)
                HtmlStyler.styleText(binding.userProfileBio, item.bioHtml, this)

                binding.userProfileCompany.compoundDrawablesRelative.first().setTint(iconTint)
                binding.userProfileFollowers.compoundDrawablesRelative.first().setTint(iconTint)
                binding.userProfileLocation.compoundDrawablesRelative.first().setTint(iconTint)
                binding.userProfileLink.compoundDrawablesRelative.first().setTint(iconTint)
            }
            is LoadingItem -> Unit
            is ListItemDivider -> Unit
            is Spacer -> Unit
        }
        holder.binding.executePendingBindings()
    }

    override fun onLinkClicked(view: View, url: String) {
        Timber.d("the clicked link is $url")
    }
}