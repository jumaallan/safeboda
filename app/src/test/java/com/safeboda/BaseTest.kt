package com.safeboda

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.safeboda.data.local.Database
import com.safeboda.data.local.dao.FollowersDao
import com.safeboda.data.local.dao.FollowingDao
import com.safeboda.data.local.dao.UserDao
import org.junit.After
import org.junit.Before
import java.io.IOException

internal open class BaseTest {

    // database and dao
    private lateinit var database: Database
    protected lateinit var userDao: UserDao
    protected lateinit var followersDao: FollowersDao
    protected lateinit var followingDao: FollowingDao

    @Before
    open fun setup() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries()
                .build()
        userDao = database.userDao()
        followersDao = database.followersDao()
        followingDao = database.followingDao()
    }

    @After
    @Throws(IOException::class)
    open fun tearDown() {

        database.close()
    }
}