package com.safeboda.ui.viewmodel

import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeboda.core.data.models.User
import com.safeboda.core.network.ApiModel
import com.safeboda.core.network.ApiRequestStatus
import com.safeboda.core.network.ApiRequestStatus.LOADING
import com.safeboda.core.network.Page
import com.safeboda.ui.interfaces.PaginatedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class UsersViewModel(
    private val defaultScheduler: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), PaginatedViewModel {

    protected val _userModel: MutableLiveData<ApiModel<List<ListItemUser>>> = MutableLiveData()

    val userModel: LiveData<ApiModel<List<ListItemUser>>>
        get() = _userModel

    override val requestStatus: ApiRequestStatus
        get() = userModel.value?.status ?: LOADING
    override var currentPage: Page = Page(false, null, true)

    abstract suspend fun fetchData(
        root: String,
        endCursor: String?
    ): Flow<Pair<List<User>, Page>>

    override fun loadHead() {
        loadUsers()
    }

    override fun loadNextPage() {
        loadUsers(currentPage.endCursor)
    }

    private fun loadUsers(after: String? = null) {
        _userModel.postValue(ApiModel.loading(userModel.value?.data))

        viewModelScope.launch(defaultScheduler) {
            fetchData("MDQ6VXNlcjI1MDg1MTQ2", after)
                .collect { (users, page) ->
                    currentPage = page
                    val models =
                        if (after == null) emptyList() else userModel.value?.data.orEmpty()
                    _userModel.postValue(ApiModel.success(models + parseListItems(users)))
                }
        }
    }

    private fun parseListItems(response: List<User>): List<ListItemUser> {
        return response.map {
            ListItemUser(
                it,
                HtmlCompat.fromHtml(it.bioHtml, HtmlCompat.FROM_HTML_MODE_LEGACY).trim()
            )
        }
    }

    data class ListItemUser(private val user: User, val htmlText: CharSequence) :
        com.safeboda.ui.interfaces.ListItemUser {
        override val id: String
            get() = user.id
        override val name: String?
            get() = user.name
        override val login: String
            get() = user.login
        override val bioHtml: String
            get() = if (htmlText.isBlank()) "" else user.bioHtml
        override val avatarUrl: String
            get() = user.avatarUrl
    }

}