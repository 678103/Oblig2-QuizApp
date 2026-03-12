// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Kotlin Android plugin
    kotlin("android") version "1.9.10" apply false

    // KSP plugin
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false

    // Android Application plugin
    id("com.android.application") version "9.0.1" apply false

    // Android Library plugin (hvis du har biblioteksmoduler)
    id("com.android.library") version "9.0.1" apply false
}