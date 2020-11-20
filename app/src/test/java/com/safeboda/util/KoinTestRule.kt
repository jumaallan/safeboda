package com.safeboda.util

import com.safeboda.di.appModules
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class KoinTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {
                stopKoin()
                startKoin { modules(appModules) }
                base.evaluate()
                stopKoin()
            }
        }
    }
}