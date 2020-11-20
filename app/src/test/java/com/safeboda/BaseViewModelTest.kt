package com.safeboda

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.safeboda.util.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

abstract class BaseViewModelTest : BaseKoinTest() {

    /**
     * Swaps the background executor used by the Architecture Components with a different one which
     * executes each task synchronously.
     **/
    @get:Rule
    open val instantExecutorRule = InstantTaskExecutorRule()

    /**
     * A test rule to allow testing coroutines that use the main dispatcher
     */
    @ExperimentalCoroutinesApi
    @get:Rule
    open val coroutineTestRule = CoroutineTestRule()
}