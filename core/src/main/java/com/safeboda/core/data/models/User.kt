package com.safeboda.core.data.models

import com.safeboda.core.fragment.UserListItemFragment

data class User(
    val id: String,
    val name: String?,
    val login: String,
    val bioHtml: String,
    val avatarUrl: String
) {
    constructor(fragment: UserListItemFragment) : this(
        fragment.id,
        fragment.name,
        fragment.login,
        fragment.bioHTML as String,
        fragment.avatarUrl as String
    )
}