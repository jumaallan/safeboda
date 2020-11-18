package com.safeboda.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Reusable [RecyclerView.ViewHolder] to hold a single [ViewDataBinding] object.
 */
class BindingViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)