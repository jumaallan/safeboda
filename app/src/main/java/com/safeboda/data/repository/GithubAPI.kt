package com.safeboda.data.repository

import com.safeboda.core.data.models.UserOrOrganization
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface GithubAPI {

    @GET("user/followers")
    suspend fun getFollowers(login: String): Flow<UserOrOrganization>
}