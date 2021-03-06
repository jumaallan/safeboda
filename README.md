## Safeboda Interview Solution

👀  Writing Safeboda Interview Solution App using [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/), in 100% Kotlin, using Android Jetpack Components.

The final app looks like this:

<img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/profile.gif" width="270"/> <img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/wangerekaharun.png" width="270"/> <img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/tamzi.png" width="270"/>

### Prerequisites

Before running this app, you need to add your Github Personal Access Token, in your `local.properties` file:

```yaml
GITHUB_TOKEN="xxxx-xxxx-xxx"
```

Before every commit, make sure you run the following bash script:

```shell script
./codeAnalysis
```

To test code coverage, run the following bash script:

```shell script
./coverage
```

If you have Fastlane installed, you can run the develop lane:

```shell script
fastlane branch conf:debug
```

To check for dependency updates, run the following gradlew command:

```shell script
./gradlew dependencyUpdate
```

To inspect the local db/cache, there are two ways to do it:

Open Chrome, and type in this URL. Uses [Stetho](https://github.com/facebook/stetho), which is bundled on the debug APK

```yaml
chrome://inspect/
```

Using Android Studio database inspector - Bump up the minimum version to 26, inside the `Dependencies.kt`

```kotlin
object AndroidSdk {
    const val minSdkVersion = 26 // is set to 23 on the repo
    ...
}
```

Refer to this [issue](https://github.com/gradle/gradle/issues/10248), if you get any issues running the lint commands on the terminal :rocket:

### Background

Develop an application that uses Github APIs to achieve the following features:

* Search for a user profile on Github and display his account details, including, but not limited, to image, bio, username, and full name.

* Have the ability to display the user followers, and following lists. (the accounts that the user is following, and the accounts that are followed by the selected user)

## Tech-stack

* Tech-stack
    * [Kotlin](https://kotlinlang.org/) - a cross-platform, statically typed, general-purpose programming language with type inference.
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations.
    * [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - handle the stream of data asynchronously that executes sequentially.
    * [KOIN](https://insert-koin.io/) - a pragmatic lightweight dependency injection framework.
    * [Apollo GraphQL Client](https://www.apollographql.com/docs/android/essentials/get-started-kotlin/) - Apollo is a platform for building a data graph.
    * [Chuck](https://github.com/jgilfelt/chuck) - An in-app HTTP inspector for Android OkHttp clients
    * [Jetpack](https://developer.android.com/jetpack)
        * [Room](https://developer.android.com/topic/libraries/architecture/room) - a persistence library provides an abstraction layer over SQLite.
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - is an observable data holder.
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform action when lifecycle state changes.
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way.
    * [Stetho](http://facebook.github.io/stetho/) - application debugging tool.
    * [Timber](https://github.com/JakeWharton/timber) - a highly extensible android logger.
    * [Leak Canary](https://github.com/square/leakcanary) - a memory leak detection library for Android.

* Architecture
    * MVVM - Model View View Model
* Tests
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit](https://junit.org/junit4/)) - a simple framework to write repeatable tests.
    * [MockK](https://github.com/mockk) - mocking library for Kotlin
    * [Kluent](https://github.com/MarkusAmshove/Kluent) - Fluent Assertion-Library for Kotlin
    * [Kakao](https://github.com/agoda-com/Kakao) - Nice and simple DSL for Espresso in Kotlin
* Gradle
    * [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - For reference purposes, here's an [article explaining the migration](https://medium.com/@evanschepsiror/migrating-to-kotlin-dsl-4ee0d6d5c977).
    * Plugins
        * [Ktlint](https://github.com/JLLeitschuh/ktlint-gradle) - creates convenient tasks in your Gradle project that run ktlint checks or do code auto format.
        * [Detekt](https://github.com/detekt/detekt) - a static code analysis tool for the Kotlin programming language.
        * [Spotless](https://github.com/diffplug/spotless) - format java, groovy, markdown and license headers using gradle.
        * [Dokka](https://github.com/Kotlin/dokka) - a documentation engine for Kotlin, performing the same function as javadoc for Java.
        * [jacoco](https://github.com/jacoco/jacoco) - a Code Coverage Library
* CI/CD
    * Github Actions
    * [Fastlane](https://fastlane.tools)


## Dependencies

All the dependencies (external libraries) are defined in the single place - Gradle `buildSrc` folder. This approach allows to easily manage dependencies and use the same dependency version across all modules.

## UI and Unit Tests

The screenshot below shows the tests that are done on the repo:

#### UI Tests

The UI Tests are tested on an Emulator Running Android 10 (API 29) - Might be flaky on some API levels

<img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/ui_tests.gif" width="320"/>

The UI tests are written using Kakao

<img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/ui_tests.png"/>

#### Unit Tests on App Module

The Unit Tests here basically test the DAOs, Repo and the ViewModel

<img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/unit_test_app.png"/>

#### Unit Tests on Core Module

The Unit Tests here test the GraphQL call to the Github API

<img src="https://github.com/jumaallan/safeboda/blob/master/screenshots/unit_test_core.png"/>

More tests can be added

## License
```
MIT License

Copyright (c) 2020 Juma Allan

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
