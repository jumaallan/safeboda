package com.safeboda.di

import androidx.room.Room
import com.safeboda.data.local.Database
import com.safeboda.data.repository.UserRepository
import com.safeboda.ui.viewmodel.FollowersUsersViewModel
import com.safeboda.ui.viewmodel.FollowingUsersViewModel
import com.safeboda.ui.viewmodel.UserOrganizationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val databaseModule: Module = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "safeboda-db"
        ).build()
    }
}

private val daoModule: Module = module {
    single { get<Database>().userDao() }
}

private val repositoryModule: Module = module {
    single { UserRepository(get()) }
}

private val viewModelModule: Module = module {
    viewModel { UserOrganizationViewModel(get()) }
    viewModel { FollowersUsersViewModel(get()) }
    viewModel { FollowingUsersViewModel(get()) }
}

val appModules: List<Module> = listOf(
    databaseModule,
    daoModule,
    repositoryModule,
    viewModelModule,
)